package com.darvi.hksi.badminton.lib;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * @author lidashuang
 * @version 1.0
 */
public class SortableContext<I extends SortableContext.Option> extends ArrayList<I> implements Serializable {

    public static final String AES = "AES";
    public static final String DESC = "DESC";

    private boolean validation = false;

    public boolean isValidation() {
        return validation;
    }

    public boolean validation(Class<?> clazz) {
        return validation(clazz, this);
    }

    @Data
    @AllArgsConstructor
    public static class Mapper implements Serializable {
        private String name;
        private String column;
    }

    @Data
    public static class Option implements Serializable {
        private String content;
        private String condition;
    }

    public static boolean validation(Class<?> clazz, SortableContext<?> context) {
        if (context == null) {
            return false;
        } else {
            final Field[] fields = clazz.getDeclaredFields();
            final List<Mapper> mappers = new ArrayList<>();
            for (final Field field : fields) {
                final Sortable sortable = field.getAnnotation(Sortable.class);
                if (sortable != null) {
                    mappers.add(new Mapper(field.getName(),
                            StringUtils.hasText(sortable.value()) ? sortable.value() : field.getName()));
                }
            }
            for (final Option option : context) {
                boolean bool = false;
                String content = option.getContent();
                String condition = option.getCondition();
                if (StringUtils.hasText(content)) {
                    content = content.toLowerCase();
                } else {
                    return false;
                }
                if (StringUtils.hasText(condition)) {
                    condition = condition.toUpperCase();
                    condition = condition.equals(DESC) ? DESC : AES;
                } else {
                    return false;
                }
                for (final Mapper mapper : mappers) {
                    if (mapper.getName().toLowerCase().equals(content)) {
                        bool = true;
                        content = mapper.getColumn();
                        break;
                    }
                }
                if (bool) {
                    option.setContent(content);
                    option.setCondition(condition);
                } else {
                    return false;
                }
            }
            context.validation = true;
            return true;
        }
    }

}
