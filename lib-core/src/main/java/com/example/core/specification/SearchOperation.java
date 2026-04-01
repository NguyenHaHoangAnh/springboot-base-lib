package com.example.core.specification;

public enum SearchOperation {
    EQUALITY,
    NEGATION,
    GREATER_THAN,
    GREATER_THAN_EQUALS,
    LESS_THAN,
    LESS_THAN_EQUALS,
    LIKE,
    STARTS_WITH,
    NOT_STARTS_WITH,
    ENDS_WITH,
    NOT_ENDS_WITH,
    CONTAINS,
    NOT_CONTAINS,
    IN,
    NOT_IN;

    public static final String[] SIMPLE_OPERATION_SET = new String[]{":", "!", ">", "<", "~", "@", "$"};
    public static final String OR_PREDICATE_FLAG = "'";
    public static final String ZERO_OR_MORE_REGEX = "*";
    public static final String EQUALITY_REGEX = ":";
    public static final String OR_OPERATOR = "OR";
    public static final String AND_OPERATOR = "AND";
    public static final String LEFT_PARANTHESIS = "(";
    public static final String RIGHT_PARANTHESIS = ")";

    public static SearchOperation getSimpleOperation(final char input) {
        switch (input) {
            case '!':
                return NEGATION;
            case '$':
                return NOT_IN;
            case ':':
                return EQUALITY;
            case '<':
                return LESS_THAN;
            case '>':
                return GREATER_THAN;
            case '@':
                return IN;
            case '~':
                return LIKE;
            default:
                return null;
        }
    }

    public static SearchOperation getSimpleOperationByName(final String input) {
        switch (input) {
            case "equals":
                return EQUALITY;
            case "neq":
                return NEGATION;
            case "gt":
                return GREATER_THAN;
            case "gte":
                return GREATER_THAN_EQUALS;
            case "lt":
                return LESS_THAN;
            case "lte":
                return LESS_THAN_EQUALS;
            case "in":
                return IN;
            case "notIn":
                return NOT_IN;
            case "startswith":
                return STARTS_WITH;
            case "notStartswith":
                return NOT_STARTS_WITH;
            case "endswith":
                return ENDS_WITH;
            case "notEndswith":
                return NOT_ENDS_WITH;
            case "contains":
                return CONTAINS;
            case "notContains":
                return NOT_CONTAINS;
            default:
                return null;
        }
    }
}
