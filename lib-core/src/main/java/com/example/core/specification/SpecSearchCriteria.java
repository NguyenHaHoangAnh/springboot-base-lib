package com.example.core.specification;

import com.example.core.util.DateUtil;

import java.io.Serializable;
import java.sql.Timestamp;

public class SpecSearchCriteria implements Serializable {

    /*
     * Đại diện cho 1 điều kiện filter
     * Ví dụ:
     *   name:*john*
     * → key=name, operation=CONTAINS, value=john
     */
    private String key;
    private String type;
    private SearchOperation operation;
    private Object value;
    private boolean orPredicate;

    public SpecSearchCriteria() {}

    /*
     * Constructor cơ bản
     */
    public SpecSearchCriteria(String key, SearchOperation operation, Object value) {
        this.key = key;
        this.operation = operation;
        this.value = value;
    }

    /*
     * Dùng khi có OR (ký tự ')
     */
    public SpecSearchCriteria(String orPredicate, String key,
                              SearchOperation operation, Object value) {

        // nếu có dấu ' → là OR
        this.orPredicate = orPredicate != null && orPredicate.equals("'");

        this.key = key;
        this.operation = operation;
        this.value = value;
    }

    /*
     * Constructor quan trọng nhất: parse từ string
     */
    public SpecSearchCriteria(String key, String operation,
                              String prefix, String value, String suffix) {

        // B1: xác định operation từ ký tự (:, >, <, ...)
        SearchOperation op = SearchOperation.getSimpleOperation(operation.charAt(0));

        if (op != null) {

            // ========================
            // xử lý >=, <=
            // ========================
            if (op == SearchOperation.GREATER_THAN || op == SearchOperation.LESS_THAN) {

                if (value.startsWith(":")) {
                    value = value.substring(1);

                    op = (op == SearchOperation.GREATER_THAN)
                            ? SearchOperation.GREATER_THAN_EQUALS
                            : SearchOperation.LESS_THAN_EQUALS;
                }
            }

            // ========================
            // xử lý wildcard *
            // ========================
            else if (op == SearchOperation.EQUALITY || op == SearchOperation.NEGATION) {

                boolean startWithAsterisk = prefix != null && prefix.contains("*");
                boolean endWithAsterisk = suffix != null && suffix.contains("*");

                if (startWithAsterisk && endWithAsterisk) {
                    op = (op == SearchOperation.EQUALITY)
                            ? SearchOperation.CONTAINS
                            : SearchOperation.NOT_CONTAINS;

                } else if (startWithAsterisk) {
                    op = (op == SearchOperation.EQUALITY)
                            ? SearchOperation.ENDS_WITH
                            : SearchOperation.NOT_ENDS_WITH;

                } else if (endWithAsterisk) {
                    op = (op == SearchOperation.EQUALITY)
                            ? SearchOperation.STARTS_WITH
                            : SearchOperation.NOT_STARTS_WITH;
                }
            }
        }

        // ========================
        // detect date
        // ========================
        Timestamp dateValue = DateUtil.getSqlTimestamp(value);
        boolean isDate = dateValue != null;

        this.key = key;
        this.operation = op;
        this.type = isDate ? "date" : "";

        this.value = isDate ? dateValue : value;
    }

    /*
     * Constructor dùng khi FE gửi operation dạng string (gte, contains...)
     */
    public SpecSearchCriteria(String key, String type, String operation, Object value) {
        this.key = key;
        this.type = type;
        this.operation = SearchOperation.getSimpleOperationByName(operation);
        this.value = value;
    }

    // ========================
    // Getter / Setter
    // ========================

    public String getKey() { return key; }
    public SearchOperation getOperation() { return operation; }
    public Object getValue() { return value; }
    public boolean isOrPredicate() { return orPredicate; }
    public String getType() { return type; }

    public void setValue(Object value) { this.value = value; }
}
