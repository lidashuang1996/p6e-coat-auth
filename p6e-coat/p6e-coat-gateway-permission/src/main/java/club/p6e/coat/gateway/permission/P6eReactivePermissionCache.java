package club.p6e.coat.gateway.permission;

import club.p6e.coat.gateway.permission.model.PermissionModel;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author lidashuang
 * @version 1.0
 */
public final class P6eReactivePermissionCache {

    private static final String ADAPTER_CHAR = "*";
    private static final String GENERAL_ADAPTER_CHAR = ADAPTER_CHAR + ADAPTER_CHAR;
    private static final Map<String, PermissionDetails> DATA_CACHE = new HashMap<>();
    private static final Map<String, Map<String, List<String>>> INDEX_CACHE = new HashMap<>();
    private static final Map<String, Map<String, List<String>>> SPECIAL_INDEX_CACHE = new HashMap<>();

    public synchronized static void cache(List<PermissionModel> list) {
        DATA_CACHE.clear();
        INDEX_CACHE.clear();
        SPECIAL_INDEX_CACHE.clear();
        if (list != null && list.size() > 0) {
            DATA_CACHE.putAll(data);
        }
        if (list != null && list.size() > 0) {
            INDEX_CACHE.putAll(index);
        }
    }

    public static PermissionDetails execute(String url, String method, List<String> groups) {
        if (url != null && method != null && groups != null && groups.size() > 0) {
            Map<String, List<String>> data = INDEX_CACHE.get(url);
            final Iterator<String> iterator = SPECIAL_INDEX_CACHE.keySet().iterator();
            if (data != null) {
                final List<String> list = data.get(method);
                if (list != null && list.size() > 0) {
                    for (final String item : list) {
                        if (groups.contains(item)) {
                            return DATA_CACHE.get(item);
                        }
                    }
                }
            }
            while (iterator.hasNext()) {
                final String key = iterator.next();
                if (executeSpecialMatch(url, key)) {
                    data = SPECIAL_INDEX_CACHE.get(key);
                    final List<String> list = data.get(method);
                    if (list != null && list.size() > 0) {
                        for (final String item : list) {
                            if (groups.contains(item)) {
                                return DATA_CACHE.get(item);
                            }
                        }
                    }
                }
            }
        }
        return null;
    }

    private static boolean executeSpecialMatch(String url, String content) {
        if (url != null && content != null) {
            final String[] us = url.split("/");
            final String[] cs = content.split("/");
            if (us.length == cs.length || GENERAL_ADAPTER_CHAR.equals(cs[cs.length - 1])) {
                boolean status = true;
                final int len = Math.min(us.length, cs.length);
                for (int i = 0; i < len; i++) {
                    if (!us[i].equals(cs[i])
                            && !cs[i].equals(ADAPTER_CHAR)
                            && !cs[i].equals(GENERAL_ADAPTER_CHAR)) {
                        status = false;
                        break;
                    }
                }
                return status;
            }
        }
        return false;
    }

}
