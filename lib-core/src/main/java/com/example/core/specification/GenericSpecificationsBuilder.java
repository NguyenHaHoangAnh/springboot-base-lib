package com.example.core.specification;

import org.springframework.data.jpa.domain.Specification;

import java.util.Collections;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;

public class GenericSpecificationsBuilder<U> {

    public Specification<U> build(
            Deque<?> postFixedExprStack,
            Function<SpecSearchCriteria, Specification<U>> converter) {

        // Stack chứa các Specification đã build
        Deque<Specification<U>> specStack = new LinkedList<>();

        // Đảo stack vì parser trả về theo thứ tự ngược
        Collections.reverse((List) postFixedExprStack);

        // Duyệt từng phần tử trong postfix expression
        while (!postFixedExprStack.isEmpty()) {

            Object mayBeOperand = postFixedExprStack.pop();

            // ========================
            // CASE 1: Là điều kiện (operand)
            // ========================
            if (!(mayBeOperand instanceof String)) {

                // Convert SpecSearchCriteria → Specification
                Specification<U> spec =
                        converter.apply((SpecSearchCriteria) mayBeOperand);

                // Push vào stack
                specStack.push(spec);

            } else {

                // ========================
                // CASE 2: Là toán tử AND / OR
                // ========================

                // Lấy 2 điều kiện gần nhất trong stack
                Specification<U> operand1 = specStack.pop();
                Specification<U> operand2 = specStack.pop();

                // Nếu là AND → combine bằng AND
                if (mayBeOperand.equals("AND")) {
                    specStack.push(
                            Specification.where(operand1).and(operand2)
                    );

                    // Nếu là OR → combine bằng OR
                } else if (mayBeOperand.equals("OR")) {
                    specStack.push(
                            Specification.where(operand1).or(operand2)
                    );
                }
            }
        }

        // Kết quả cuối cùng là 1 Specification hoàn chỉnh
        return specStack.pop();
    }
}
