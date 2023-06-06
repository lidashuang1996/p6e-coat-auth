package club.p6e.cloud.test.infrastructure.converter;

import club.p6e.cloud.test.error.GlobalExceptionContext;
import com.darvi.hksi.badminton.lib.SearchableContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lidashuang
 * @version 1.0
 */
public class SearchableConverter {

    public static Predicate to(SearchableContext<?> context, Root<?> rt, CriteriaBuilder cb) {
        return to(context, rt, cb, null);
    }

    public static Predicate to(SearchableContext<?> context, Root<?> rt, CriteriaBuilder cb, Predicate def) {
        return execute(context, rt, cb, def);
    }

    private static Predicate execute(SearchableContext<?> context, Root<?> rt, CriteriaBuilder cb, Predicate def) {
        if (context == null) {
            return def;
        } else {
            return execute(context, rt, cb);
        }
    }

    private static Predicate execute(SearchableContext<?> context, Root<?> rt, CriteriaBuilder cb) {
        int hierarchy = 0;
        boolean bool = false;
        SearchableContext<SearchableContext.Option> searchable = null;
        final List<Predicate> predicates = new ArrayList<>();
        for (final SearchableContext.Option option : context) {
            if (SearchableContext.LEFT_TYPE.equals(option.getType())) {
                hierarchy++;
                if (searchable == null) {
                    searchable = new SearchableContext<>(option.getRelationship());
                    if (SearchableContext.AND_RELATIONSHIP_TYPE
                            .equalsIgnoreCase(searchable.getRelationship())) {
                        bool = true;
                    }
                } else {
                    searchable.add(option);
                }
            }
            if (SearchableContext.RIGHT_TYPE.equals(option.getType())) {
                if (searchable == null) {
                    throw GlobalExceptionContext.executeParameterException(
                            SearchableConverter.class,
                            "fun execute(SearchableContext<?> context, Root<?> rt, CriteriaBuilder cb).",
                            "Unexpected end symbol exception."
                    );
                } else {
                    hierarchy--;
                    if (hierarchy == 0) {
                        if (searchable.size() > 0) {
                            if (SearchableContext.OR_RELATIONSHIP_TYPE
                                    .equalsIgnoreCase(searchable.getRelationship())) {
                                predicates.add(execute(searchable, rt, cb));
                            }
                            if (SearchableContext.AND_RELATIONSHIP_TYPE
                                    .equalsIgnoreCase(searchable.getRelationship())) {
                                predicates.add(execute(searchable, rt, cb));
                            }
                        }
                        searchable = null;
                    } else {
                        searchable.add(option);
                    }
                }
            }
            if (SearchableContext.CONDITION_TYPE.equals(option.getType())) {
                if (searchable == null) {
                    if (SearchableContext.OR_RELATIONSHIP_TYPE
                            .equalsIgnoreCase(option.getRelationship())) {
                        final Predicate predicate = execute(option, rt, cb);
                        if (predicate != null) {
                            predicates.add(predicate);
                        }
                    }
                    if (SearchableContext.AND_RELATIONSHIP_TYPE
                            .equalsIgnoreCase(option.getRelationship())) {
                        final Predicate predicate = execute(option, rt, cb);
                        if (predicate != null) {
                            bool = true;
                            predicates.add(predicate);
                        }
                    }
                } else {
                    searchable.add(option);
                }
            }
        }
        return bool ? cb.and(predicates.toArray(new Predicate[0])) : cb.or(predicates.toArray(new Predicate[0]));
    }

    private static Predicate execute(SearchableContext.Option option, Root<?> rt, CriteriaBuilder cb) {
        if (option.getKey() == null || option.getCondition() == null) {
            return null;
        }
        if (SearchableContext
                .EQUAL_OPTION_CONDITION
                .equalsIgnoreCase(option.getCondition())
                && option.getValue() != null) {
            return cb.equal(rt.get(option.getKey()), option.getValue());
        }
        if (SearchableContext
                .GREATER_THAN_OPTION_CONDITION
                .equalsIgnoreCase(option.getCondition())
                && option.getValue() != null) {
            return cb.greaterThan(rt.get(option.getKey()), option.getValue());
        }
        if (SearchableContext
                .GREATER_THAN_OR_EQUAL_OPTION_CONDITION
                .equalsIgnoreCase(option.getCondition())
                && option.getValue() != null) {
            return cb.greaterThanOrEqualTo(rt.get(option.getKey()), option.getValue());
        }
        if (SearchableContext
                .LESS_THAN_OPTION_CONDITION
                .equalsIgnoreCase(option.getCondition())
                && option.getValue() != null) {
            return cb.lessThan(rt.get(option.getKey()), option.getValue());
        }
        if (SearchableContext
                .LESS_THAN_OR_EQUAL_OPTION_CONDITION
                .equalsIgnoreCase(option.getCondition())
                && option.getValue() != null) {
            return cb.lessThanOrEqualTo(rt.get(option.getKey()), option.getValue());
        }
        if (SearchableContext
                .IS_NULL_OPTION_CONDITION
                .equalsIgnoreCase(option.getCondition())) {
            return cb.isNull(rt.get(option.getKey()));
        }
        if (SearchableContext
                .IS_NOT_NULL_OPTION_CONDITION
                .equalsIgnoreCase(option.getCondition())
                && option.getValue() != null) {
            return cb.isNotNull(rt.get(option.getKey()));
        }
        return null;
    }

}
