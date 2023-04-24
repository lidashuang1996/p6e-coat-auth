package club.p6e.coat.gateway.permission;

import club.p6e.coat.gateway.permission.model.PermissionModel;
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
    public synchronized static void cache(List<PermissionModel> list) {
        if (PE.getData() != null && PE.getData().size() > 0) {
            PE.getData().clear();
            LOGGER.info("[CACHE] Cache data cleared successfully.");
        }
        LOGGER.info("[CACHE] Start writing cache.");
        if (list != null && list.size() > 0) {
            list.forEach(PermissionCore::loadPathElementData);
        }
        LOGGER.info("[CACHE] End writing cache.");
    }

    /**
     * 加载路径节点数据
     *
     * @param model 权限模型
     */
    private static void loadPathElementData(PermissionModel model) {
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
    private static void writePathElementData(PathElement pe, PermissionModel model, String[] us, int index) {
        if (index + 1 == us.length) {
            pe.getMapper().computeIfAbsent(model.getUMethod(), k -> new ArrayList<>());
            final List<PermissionDetails> list = pe.getMapper().get(model.getUMethod());
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).getGWeight() < model.getGWeight()) {
                    list.add(i, new PermissionDetails(model));
                    break;
                }
            }
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
            List<PathElement> pes = new ArrayList<>();
            List<PathElement> nextPes = new ArrayList<>();
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
            final List<PermissionDetails> pds = new ArrayList<>();
            for (final PathElement item : list) {
                if (item.getMapper().get(ADAPTER_CHAR) != null) {
                    pds.addAll(item.getMapper().get(ADAPTER_CHAR));
                }
                if (item.getMapper().get(method) != null) {
                    pds.addAll(item.getMapper().get(method));
                }
            }
            pds.sort(Comparator.comparing(PermissionDetails::getGWeight));
            for (final String group : groups) {
                for (final PermissionDetails item : pds) {
                    if (group.contains(String.valueOf(item.getGid()))) {
                        return item;
                    }
                }
            }
        }
        return null;
    }

}
