package com.example.core.specification;

import com.example.core.util.DateUtil;
import com.google.common.collect.Lists;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Base Specification class for dynamic filtering
 * Supports multiple operations like EQUALITY, CONTAINS, IN, etc.
 */
public abstract class SpecificationBase<U> implements Specification<U> {
    private static char ESCAPE_CHAR = '\\';
    private static String[] SPECIAL_CHAR = {"_"};
    private SpecSearchCriteria criteria;

    public SpecificationBase(final SpecSearchCriteria criteria) {
        super();
        this.criteria = criteria;
    }

    public SpecSearchCriteria getCriteria() {
        return criteria;
    }

    private void standardizedValue(SpecSearchCriteria criteria) {
        if (criteria.getValue() != null && criteria.getValue() instanceof String) {
            String newValue = criteria.getValue().toString();

            // Replace space
            newValue = newValue.replace("@sp@", " ");

            // Escape special char
            switch (criteria.getOperation()) {
                case LIKE:
                case STARTS_WITH:
                case NOT_STARTS_WITH:
                case ENDS_WITH:
                case NOT_ENDS_WITH:
                case CONTAINS:
                case NOT_CONTAINS:
                    for (String s : SPECIAL_CHAR) {
                        newValue = newValue.replace(s, "\\" + s);
                    }
            }

            // Set new value
            criteria.setValue(newValue);
        }
    }

    @Override
    public Predicate toPredicate(final Root<U> root, final CriteriaQuery<?> query, final CriteriaBuilder builder) {
        boolean isNullValue = criteria.getValue() == null || criteria.getValue().toString().trim().equals("''");
        standardizedValue(criteria);

        switch (criteria.getOperation()) {
            case EQUALITY:
                if (isNullValue) {
                    return builder.isNull(root.get(criteria.getKey()));
                }

                if (criteria.getType() != null && "date".equalsIgnoreCase(criteria.getType())) {
//                    return builder.equal(root.get(criteria.getKey()).as(Timestamp.class), criteria.getValue());
                    Date startOfDay = DateUtil.startOfDay((Date) criteria.getValue());
                    Date endOfDay = DateUtil.endOfDay((Date) criteria.getValue());
                    return builder.between(root.get(criteria.getKey()).as(Date.class), startOfDay, endOfDay);
                } else {
                    return builder.equal(root.get(criteria.getKey()), criteria.getValue());
                }
            case NEGATION:
                if (isNullValue) {
                    return builder.isNotNull(root.get(criteria.getKey()));
                }

                if (criteria.getType() != null && "date".equalsIgnoreCase(criteria.getType())) {
//                    return builder.notEqual(root.get(criteria.getKey()).as(Timestamp.class), criteria.getValue());
                    Date startOfDay = DateUtil.startOfDay((Date) criteria.getValue());
                    Date endOfDay = DateUtil.endOfDay((Date) criteria.getValue());
                    return builder.or(
                            builder.lessThan(root.get(criteria.getKey()).as(Date.class), startOfDay),
                            builder.greaterThan(root.get(criteria.getKey()).as(Date.class), endOfDay)
                    );
                } else {
                    return builder.notEqual(root.get(criteria.getKey()), criteria.getValue());
                }
            case GREATER_THAN:
                if (criteria.getType() != null && "date".equalsIgnoreCase(criteria.getType())) {
//                    return builder.greaterThan(root.get(criteria.getKey()).as(Timestamp.class), (Timestamp) criteria.getValue());
                    Date endOfDay = DateUtil.endOfDay((Date) criteria.getValue());
                    return builder.greaterThan(root.get(criteria.getKey()).as(Date.class), endOfDay);
                } else {
                    return builder.greaterThan(root.get(criteria.getKey()), criteria.getValue().toString());
                }
            case GREATER_THAN_EQUALS:
                if (criteria.getType() != null && "date".equalsIgnoreCase(criteria.getType())) {
//                    return builder.greaterThanOrEqualTo(root.get(criteria.getKey()).as(Timestamp.class), (Timestamp) criteria.getValue());
                    Date startOfDay = DateUtil.startOfDay((Date) criteria.getValue());
                    return builder.greaterThanOrEqualTo(root.get(criteria.getKey()).as(Date.class), startOfDay);
                } else {
                    return builder.greaterThanOrEqualTo(root.get(criteria.getKey()), criteria.getValue().toString());
                }
            case LESS_THAN:
                if (criteria.getType() != null && "date".equalsIgnoreCase(criteria.getType())) {
//                    return builder.lessThan(root.get(criteria.getKey()).as(Timestamp.class), (Timestamp) criteria.getValue());
                    Date startOfDay = DateUtil.startOfDay((Date) criteria.getValue());
                    return builder.lessThan(root.get(criteria.getKey()).as(Date.class), startOfDay);
                } else {
                    return builder.lessThan(root.get(criteria.getKey()), criteria.getValue().toString());
                }
            case LESS_THAN_EQUALS:
                if (criteria.getType() != null && "date".equalsIgnoreCase(criteria.getType())) {
//                    return builder.lessThanOrEqualTo(root.get(criteria.getKey()).as(Timestamp.class), (Timestamp) criteria.getValue());
                    Date endOfDay = DateUtil.endOfDay((Date) criteria.getValue());
                    return builder.lessThanOrEqualTo(root.get(criteria.getKey()).as(Date.class), endOfDay);
                } else {
                    return builder.lessThanOrEqualTo(root.get(criteria.getKey()), criteria.getValue().toString());
                }
            case LIKE:
                return builder.like(builder.upper(root.get(criteria.getKey())), criteria.getValue().toString().toUpperCase(), ESCAPE_CHAR);
            case STARTS_WITH:
                return builder.like(builder.upper(root.get(criteria.getKey())), criteria.getValue().toString().toUpperCase() + "%", ESCAPE_CHAR);
            case NOT_STARTS_WITH:
                return builder.notLike(builder.upper(root.get(criteria.getKey())), criteria.getValue().toString().toUpperCase() + "%", ESCAPE_CHAR);
            case ENDS_WITH:
                return builder.like(builder.upper(root.get(criteria.getKey())), "%" + criteria.getValue().toString().toUpperCase(), ESCAPE_CHAR);
            case NOT_ENDS_WITH:
                return builder.notLike(builder.upper(root.get(criteria.getKey())), "%" + criteria.getValue().toString().toUpperCase(), ESCAPE_CHAR);
            case CONTAINS:
                return builder.like(builder.upper(root.get(criteria.getKey())), "%" + criteria.getValue().toString().toUpperCase() + "%", ESCAPE_CHAR);
            case NOT_CONTAINS:
                return builder.notLike(builder.upper(root.get(criteria.getKey())), "%" + criteria.getValue().toString().toUpperCase() + "%", ESCAPE_CHAR);
            case IN:
            case NOT_IN:
                if (criteria.getType() != null && "date".equalsIgnoreCase(criteria.getType())) {
                    Date endDate = DateUtil.addDay((Date) criteria.getValue(), 1);
                    Predicate p1, p2;
                    if (criteria.getOperation().equals(SearchOperation.IN)) {
                        p1 = builder.greaterThanOrEqualTo(root.get(criteria.getKey()).as(Timestamp.class), (Date) criteria.getValue());
                        p2 = builder.lessThan(root.get(criteria.getKey()).as(Timestamp.class), endDate);
                        return builder.and(p1, p2);
                    } else {
                        p1 = builder.lessThan(root.get(criteria.getKey()).as(Timestamp.class), (Date) criteria.getValue());
                        p2 = builder.greaterThanOrEqualTo(root.get(criteria.getKey()).as(Timestamp.class), endDate);
                        return builder.or(p1, p2);
                    }

                } else {
                    List<Predicate> predicates = new ArrayList<>();
                    String[] listString = StringUtils.split(criteria.getValue().toString(), ',');
                    List<List<String>> smallerLists = Lists.partition(Arrays.asList(listString), 1000);

                    if (!smallerLists.isEmpty()) {
                        for (List<String> smallerList : smallerLists) {
                            List listIn = new ArrayList();
                            for (String s : smallerList) {
                                if (root.get(criteria.getKey()).getJavaType().equals(String.class)) {
                                    listIn.add(s);
                                } else if (root.get(criteria.getKey()).getJavaType().equals(Long.class)) {
                                    listIn.add(Long.valueOf(s));
                                } else if (root.get(criteria.getKey()).getJavaType().equals(Integer.class)) {
                                    listIn.add(Integer.valueOf(s));
                                }
                            }

                            if (criteria.getOperation().equals(SearchOperation.IN)) {
                                predicates.add(builder.in(root.get(criteria.getKey())).value(castToRequiredType(root.get(criteria.getKey()).getJavaType(), listIn)));
                            } else {
                                predicates.add(builder.in(root.get(criteria.getKey())).value(castToRequiredType(root.get(criteria.getKey()).getJavaType(), listIn)).not());
                            }
                        }
                    }

                    if (!predicates.isEmpty()) {
                        if (criteria.getOperation().equals(SearchOperation.IN)) {
                            return builder.or(predicates.stream().toArray(Predicate[]::new));
                        } else {
                            return builder.and(predicates.stream().toArray(Predicate[]::new));
                        }
                    }
                    return null;
                }

            default:
                return null;
        }
    }

    private Object castToRequiredType(Class fieldType, Object value) {
        List<Object> lists = new ArrayList<>();
        if (value instanceof ArrayList) {
            ArrayList list = (ArrayList) value;
            for (Object o : list) {
                lists.add(castToRequiredType(fieldType, String.valueOf(o)));
            }
        } else {
            lists.add(castToRequiredType(fieldType, String.valueOf(value)));
        }
        return lists;
    }

    private Object castToRequiredType(Class fieldType, String value) {
        if (fieldType.isAssignableFrom(Double.class)) {
            return Double.valueOf(value);
        } else if (fieldType.isAssignableFrom(Integer.class)) {
            return Integer.valueOf(value);
        } else if (fieldType.isAssignableFrom(Long.class)) {
            return Long.valueOf(value);
        } else if (fieldType.isAssignableFrom(BigDecimal.class)) {
            return BigDecimal.valueOf(Long.parseLong(value));
        } else if (fieldType.isAssignableFrom(Short.class)) {
            return Short.valueOf(value);
        } else if (fieldType.isAssignableFrom(Byte.class)) {
            return Byte.valueOf(value);
        } else if (fieldType.isAssignableFrom(Enum.class)) {
            return Enum.valueOf(fieldType, value);
        }
        return value;
    }
}