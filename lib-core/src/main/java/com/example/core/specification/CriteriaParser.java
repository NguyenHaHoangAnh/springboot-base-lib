package com.example.core.specification;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.example.core.model.Filter;
import com.example.core.util.DateUtil;
import com.google.common.base.Joiner;

public class CriteriaParser {

    /*
     * Map chứa toán tử AND / OR + độ ưu tiên
     * AND (2) > OR (1)
     */
    private static Map<String, Operator> ops;

    /*
     * Regex parse condition
     *
     * Ví dụ:
     *   chagWho:*LUANDT*
     *
     * group(1) = field     → chagWho
     * group(2) = operator  → :
     * group(3) = prefix    → *
     * group(4) = value     → LUANDT
     * group(5) = suffix    → *
     */
    private static Pattern SpecCriteraRegex =
            Pattern.compile("^(\\w+?)(" +
                    Joiner.on("|\\").join(SearchOperation.SIMPLE_OPERATION_SET) +
                    ")(\\*?)(\\-?.+?)(\\*?)$");

    /*
     * Enum operator + độ ưu tiên
     */
    private enum Operator {
        OR(1),
        AND(2);

        final int precedence;

        Operator(int p) {
            this.precedence = p;
        }
    }

    /*
     * Khởi tạo map operator
     */
    static {
        Map<String, Operator> tempMap = new HashMap<>();
        tempMap.put("AND", Operator.AND);
        tempMap.put("OR", Operator.OR);
        tempMap.put("and", Operator.AND);
        tempMap.put("or", Operator.OR);

        ops = Collections.unmodifiableMap(tempMap);
    }

    /*
     * So sánh độ ưu tiên operator
     */
    private static boolean isHigherPrecedenceOperator(String currOp, String prevOp) {
        return ops.containsKey(prevOp)
                && ops.get(prevOp).precedence >= ops.get(currOp).precedence;
    }

    /*
     * =========================================
     * PARSE STRING → POSTFIX (RPN)
     * =========================================
     *
     * Input:
     *   serverType:1 and ( chagWho:*LUANDT* or status:1 )
     *
     * Output (postfix):
     *   serverType:1
     *   chagWho:*LUANDT*
     *   status:1
     *   OR
     *   AND
     */
    public Deque<?> parse(String searchParam) {

        Deque<Object> output = new LinkedList<>();
        Deque<String> stack = new LinkedList<>();

        // tách token theo space
        Arrays.stream(searchParam.split("\\s+")).forEach(token -> {

            // ========================
            // CASE 1: AND / OR
            // ========================
            if (ops.containsKey(token)) {

                // xử lý precedence
                while (!stack.isEmpty()
                        && isHigherPrecedenceOperator(token, stack.peek())) {

                    output.push(
                            stack.pop().equalsIgnoreCase(SearchOperation.OR_OPERATOR)
                                    ? SearchOperation.OR_OPERATOR
                                    : SearchOperation.AND_OPERATOR
                    );
                }

                // push operator vào stack
                stack.push(
                        token.equalsIgnoreCase(SearchOperation.OR_OPERATOR)
                                ? SearchOperation.OR_OPERATOR
                                : SearchOperation.AND_OPERATOR
                );
            }

            // ========================
            // CASE 2: "("
            // ========================
            else if (token.equals(SearchOperation.LEFT_PARANTHESIS)) {
                stack.push(SearchOperation.LEFT_PARANTHESIS);
            }

            // ========================
            // CASE 3: ")"
            // ========================
            else if (token.equals(SearchOperation.RIGHT_PARANTHESIS)) {

                // pop cho đến khi gặp "("
                while (!stack.peek().equals(SearchOperation.LEFT_PARANTHESIS)) {
                    output.push(stack.pop());
                }

                // bỏ "("
                stack.pop();
            }

            // ========================
            // CASE 4: CONDITION
            // ========================
            else {

                Matcher matcher = SpecCriteraRegex.matcher(token);

                while (matcher.find()) {

                    output.push(new SpecSearchCriteria(
                            matcher.group(1), // field
                            matcher.group(2), // operator
                            matcher.group(3), // prefix (*)
                            matcher.group(4), // value
                            matcher.group(5)  // suffix (*)
                    ));
                }
            }
        });

        // pop hết operator còn lại
        while (!stack.isEmpty()) {
            output.push(stack.pop());
        }

        return output;
    }

    /*
     * =========================================
     * PARSE FILTER ARRAY (JSON)
     * =========================================
     *
     * Input:
     * filters = [
     *   { field: "status", compare: "in", value: [1,2] },
     *   { field: "type", compare: "equals", value: "A" }
     * ]
     *
     * Output:
     *   status IN (...)
     *   type = 'A'
     *   AND
     */
    public Deque<?> parse(Filter[] filters) throws Exception {

        Deque<Object> output = new LinkedList<>();

        for (int i = 1; i < filters.length + 1; i++) {

            Filter item = filters[i - 1];

            // nếu là date → convert sang Timestamp
            if (item.getType().equalsIgnoreCase("date")) {
                item.setValue(DateUtil.getSqlTimestamp(item.getValue().toString()));
            }

            // push condition
            output.push(new SpecSearchCriteria(
                    item.getField(),
                    item.getType(),
                    item.getCompare(),
                    item.getValue()
            ));

            // mặc định nối AND
            if (i >= 2) {
                output.push(SearchOperation.AND_OPERATOR);
            }
        }

        return output;
    }
}
