package club.p6e.cloud.console.infrastructure.converter;

import com.darvi.hksi.badminton.lib.SortableContext;
import org.springframework.data.domain.Sort;

/**
 * @author lidashuang
 * @version 1.0
 */
public class SortableConverter {

    public static Sort to(SortableContext<?> context) {
        return to(context, null);
    }

    public static Sort to(SortableContext<?> context, Sort def) {
        return execute(context, def);
    }

    private static Sort execute(SortableContext<?> context, Sort def) {
        if (context == null) {
            return def;
        } else {
            return Sort.by(context.stream().map(i -> SortableContext.DESC.equals(i.getCondition())
                    ? Sort.Order.desc(i.getContent()) : Sort.Order.asc(i.getContent())).toList());
        }
    }

}
