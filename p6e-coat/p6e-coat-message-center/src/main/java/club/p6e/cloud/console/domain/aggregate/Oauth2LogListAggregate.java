package club.p6e.cloud.console.domain.aggregate;

import club.p6e.cloud.console.domain.ConfigurationDomain;
import club.p6e.cloud.console.infrastructure.model.Oauth2LogModel;
import com.darvi.hksi.badminton.lib.SearchableContext;
import com.darvi.hksi.badminton.lib.SortableContext;
import com.darvi.hksi.badminton.lib.utils.SpringUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lidashuang
 * @version 1.0
 */
public class Oauth2LogListAggregate extends ConfigurationDomain {

    private final int page;
    private final int size;
    private final long total;
    private final List<Oauth2LogModel> list;

    @SuppressWarnings("ALL")
    private static final String SELECT_SQL = "" +
            "  SELECT    " +
            "    \"A\".\"cid\",    " +
            "    \"A\".\"uid\",    " +
            "    \"A\".\"date\",    " +
            "    \"A\".\"action\",    " +
            "    \"A\".\"content\",    " +
            "    \"A\".\"ip\"     " +
            "  FROM    " +
            "    (  SELECT " +
            "           \"cid\",    " +
            "           \"uid\",    " +
            "           \"date\",    " +
            "           \"action\",    " +
            "           \"content\",    " +
            "           \"ip\"    " +
            "       FROM    " +
            "           \"p6e_oauth2_log\"    " +
            "       LIMIT :limit OFFSET :offset    " +
            "    ) AS \"A\"    " +
            "    LEFT JOIN \"p6e_oauth2_client\" AS \"B\" ON \"A\".\"cid\" = \"B\".\"id\"    " +
            "    LEFT JOIN \"p6e_user\" AS \"C\" ON \"A\".\"uid\" = \"C\".\"id\"    " +
            ";";

    public static Oauth2LogListAggregate search(
            Integer page, Integer size, String query,
            SortableContext<SortableContext.Option> sort,
            SearchableContext<SearchableContext.Option> search) {
        page = initPage(page);
        size = initSize(size);
        final EntityManager manager = SpringUtil.getBean(EntityManager.class);
        final Query nQuery = manager.createNativeQuery(SELECT_SQL);
        nQuery.setParameter("offset", size);
        nQuery.setParameter("limit", (page - 1) * size);
        final List<?> list = nQuery.getResultList();
        System.out.println(list);
        return new Oauth2LogListAggregate(page, size, 0, new ArrayList<>());
    }

    private Oauth2LogListAggregate(int page, int size, long total, List<Oauth2LogModel> list) {
        this.list = list;
        this.page = page;
        this.size = size;
        this.total = total;
    }

    public int getPage() {
        return page;
    }

    public int getSize() {
        return size;
    }

    public long getTotal() {
        return total;
    }

    public List<Oauth2LogModel> getList() {
        return list;
    }

}
