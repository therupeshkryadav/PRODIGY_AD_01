package com.example.myapplication;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.button.MaterialButton;

import java.util.Stack;

public class MainActivity extends AppCompatActivity {
    private TextView solutionTv, resultTv;
    private String solution = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        solutionTv = findViewById(R.id.solution_tv);
        resultTv = findViewById(R.id.result_tv);

        assignButtonListeners();
    }

    private void assignButtonListeners() {
        int[] buttonIds = {
                R.id.button_c, R.id.open_bracket, R.id.close_bracket, R.id.divide,
                R.id.button_seven, R.id.button_eight, R.id.button_nine, R.id.multiply,
                R.id.button_four, R.id.button_five, R.id.button_six, R.id.add,
                R.id.button_three, R.id.button_two, R.id.button_one, R.id.sub,
                R.id.button_AC, R.id.button_zero, R.id.button_dot, R.id.equal
        };

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MaterialButton button = (MaterialButton) view;
                String buttonText = button.getText().toString();
                handleInput(buttonText);
            }
        };

        for (int id : buttonIds) {
            findViewById(id).setOnClickListener(listener);
        }
    }

    private void handleInput(String input) {
        switch (input) {
            case "C":
                if (!solution.isEmpty()) {
                    solution = solution.substring(0, solution.length() - 1);
                }
                break;
            case "AC":
                solution = "";
                break;
            case "=":
                resultTv.setText(evaluateExpression(solution));
                return;
            default:
                solution += input;
                break;
        }
        solutionTv.setText(solution);
    }

    private String evaluateExpression(String expression) {
        try {
            return String.valueOf(evaluate(expression));
        } catch (Exception e) {
            return "Error";
        }
    }

    private double evaluate(String expression) {
        Stack<Double> values = new Stack<>();
        Stack<Character> ops = new Stack<>();

        for (int i = 0; i < expression.length(); i++) {
            char c = expression.charAt(i);

            if (Character.isDigit(c)) {
                StringBuilder sbuf = new StringBuilder();
                while (i < expression.length() && (Character.isDigit(expression.charAt(i)) || expression.charAt(i) == '.')) {
                    sbuf.append(expression.charAt(i++));
                }
                values.push(Double.parseDouble(sbuf.toString()));
                i--;
            } else if (c == '(') {
                ops.push(c);
            } else if (c == ')') {
                while (ops.peek() != '(') {
                    values.push(applyOp(ops.pop(), values.pop(), values.pop()));
                }
                ops.pop();
            } else if (isOperator(c)) {
                while (!ops.isEmpty() && hasPrecedence(c, ops.peek())) {
                    values.push(applyOp(ops.pop(), values.pop(), values.pop()));
                }
                ops.push(c);
            }
        }

        while (!ops.isEmpty()) {
            values.push(applyOp(ops.pop(), values.pop(), values.pop()));
        }

        return values.pop();
    }

    private boolean isOperator(char op) {
        return op == '+' || op == '-' || op == '*' || op == '/';
    }

    private boolean hasPrecedence(char op1, char op2) {
        if (op2 == '(' || op2 == ')') {
            return false;
        }
        if ((op1 == '*' || op1 == '/') && (op2 == '+' || op2 == '-')) {
            return false;
        }
        return true;
    }

    private double applyOp(char op, double b, double a) {
        switch (op) {
            case '+':
                return a + b;
            case '-':
                return a - b;
            case '*':
                return a * b;
            case '/':
                if (b == 0) {
                    throw new UnsupportedOperationException("Cannot divide by zero");
                }
                return a / b;
        }
        return 0;
    }
}
