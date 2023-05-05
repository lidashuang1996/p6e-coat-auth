package club.p6e.coat.gateway.permission;

import lombok.Data;
import lombok.experimental.Accessors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * 权限核心对象
 *
 * @author lidashuang
 * @version 1.0
 */
public final class PermissionCore {

    /**
     * 路径节点对象
     */
    @Data
    @Accessors(chain = true)
    public static class PathElement {
        /**
         * 其他节点信息
         */
        private Map<String, PathElement> data = new HashMap<>();

        /**
         * 权限映射对象
         */
        private Map<String, List<PermissionDetails>> mapper = new HashMap<>();
    }

    /**
     * 路径通配符
     */
    private static final String PATH_CHAR = "/";

    /**
     * 路径通配符
     */
    private static final String ADAPTER_CHAR = "*";

    /**
     * 多路径通配符
     */
    private static final String GENERAL_ADAPTER_CHAR = ADAPTER_CHAR + ADAPTER_CHAR;

    /**
     * 日志对象
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(PermissionCore.class);

    /**
     * 路径节点匹配对象
     */
    private static final PathElement PE = new PathElement();

    /**
     * 缓存权限
     *
     * @param list 权限模型列表
     */
    public synchronized static void cache(List<PermissionDetails> list) {
        if (PE.getData() != null && PE.getData().size() > 0) {
            PE.getData().clear();
            LOGGER.info("[CACHE] Cache data cleared successfully.");
        }
        LOGGER.info("[CACHE] Start writing cache.");
        if (list != null && list.size() > 0) {
            list.forEach(PermissionCore::loadPathElementData);
        }
        LOGGER.info("[CACHE] End writing cache.");
        LOGGER.debug("----------------------------");
        executeNodeTreePrinting(PE.getData(), 0);
        LOGGER.debug("----------------------------");
    }

    /**
     * 节点树打印
     *
     * @param map   数据对象
     * @param index 层级索引
     */
    private static void executeNodeTreePrinting(Map<String, PathElement> map, int index) {
        for (String key : map.keySet()) {
            LOGGER.debug("         ".repeat(Math.max(0, index)) + key + "     >>>     " + map.get(key).getMapper());
            if (map.get(key) != null && map.get(key).getData() != null && map.get(key).getData().size() > 0) {
                executeNodeTreePrinting(map.get(key).getData(), index + 1);
            }
        }
    }

    /**
     * 加载路径节点数据
     *
     * @param model 权限模型
     */
    private static void loadPathElementData(PermissionDetails model) {
        LOGGER.info("[CACHE] write ==> " + model);
        final String u = model.getUBaseUrl() + model.getUUrl();
        final String[] us = u.split(PATH_CHAR);
        writePathElementData(PE, model, us, 0);
    }

    /**
     * 写入路径节点数据
     *
     * @param pe    路径节点对象
     * @param model 权限模型
     * @param us    路径节点数组
     * @param index 索引
     */
    private static void writePathElementData(PathElement pe, PermissionDetails model, String[] us, int index) {
        if (index == us.length) {
            pe.getMapper().computeIfAbsent(model.getUMethod(), k -> new ArrayList<>()).add(model);
        } else {
            writePathElementData(pe.getData().computeIfAbsent(us[index], k -> new PathElement()), model, us, index + 1);
        }
    }

    /**
     * 执行权限验证
     *
     * @param url    请求的路径
     * @param method 请求的方法
     * @param groups 请求的权限组
     * @return 权限详情对象
     */
    public static PermissionDetails execute(String url, String method, List<String> groups) {
        if (url != null && method != null && groups != null && groups.size() > 0) {
            final List<PathElement> pes = new ArrayList<>();
            final List<PathElement> nextPes = new ArrayList<>();
            pes.add(PE);
            final String[] us = url.split(PATH_CHAR);
            final List<PathElement> list = new ArrayList<>();
            for (final String u : us) {
                for (PathElement pe : pes) {
                    if (pe.getData().get(GENERAL_ADAPTER_CHAR) != null) {
                        list.add(pe.getData().get(GENERAL_ADAPTER_CHAR));
                    }
                    if (pe.getData().get(ADAPTER_CHAR) != null) {
                        nextPes.add(pe.getData().get(ADAPTER_CHAR));
                    }
                    if (pe.getData().get(u) != null) {
                        nextPes.add(pe.getData().get(u));
                    }
                }
                if (nextPes.size() == 0) {
                    return null;
                } else {
                    pes.clear();
                    pes.addAll(nextPes);
                    nextPes.clear();
                }
            }
            list.addAll(pes);
            final Set<PermissionDetails> pdSet = new HashSet<>();
            for (final PathElement item : list) {
                if (item.getMapper().get(ADAPTER_CHAR) != null) {
                    pdSet.addAll(item.getMapper().get(ADAPTER_CHAR));
                }
                if (item.getMapper().get(method) != null) {
                    pdSet.addAll(item.getMapper().get(method));
                }
            }
            final List<PermissionDetails> pdList = new ArrayList<>(pdSet);
            pdList.sort(Comparator.comparing(PermissionDetails::getGWeight));
            for (final String group : groups) {
                for (final PermissionDetails item : pdList) {
                    if (group.contains(String.valueOf(item.getGid()))) {
                        return item.copy();
                    }
                }
            }
        }
        return null;
    }

}
