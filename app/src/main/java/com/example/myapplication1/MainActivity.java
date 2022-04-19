package com.example.myapplication1;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Stack;

public class MainActivity extends AppCompatActivity {
    Button btn;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = findViewById(R.id.textView);
    }

    public void onClick(View v) {
        int id = v.getId();
        btn = findViewById(id);
        String btnText = (String) btn.getText();
        String textViewText = (String) textView.getText();
        String myArray = "0123456789+-x/%.^";
        String operations = "+-x/%^";
        try {
            if (btnText.equals("=")) {
                textViewText = textViewText.concat(" ");
                double result = evaluate(textViewText);
                textView.setText(String.valueOf(result));
            } else if (operations.indexOf(btnText.charAt(0)) >= 0 && operations.indexOf(textViewText.charAt(textViewText.length() - 1)) >= 0) {
                textViewText = textViewText.substring(0, textViewText.length() - 1);
                textViewText = textViewText.concat(btnText);
                textView.setText(textViewText);
            } else if (btnText.equals("Del")) {
                try {
                    textViewText = textViewText.substring(0, textViewText.length() - 1);
                    textView.setText(textViewText);
                } catch (Exception e) {
                    textView.setText(null);
                }
            } else if (btnText.equals("C")) {
                textView.setText(null);
                return;
            } else if (myArray.indexOf(btnText) >= 0) {
                textViewText = textViewText.concat(btnText);
                textView.setText(textViewText);
                return;
            }
        }
        catch(Exception e){
            textView.setText(null);
        }
    }

    public static double evaluate(String expression)
    {
        char[] tokens = expression.toCharArray();
        // Stack for numbers: 'values'
        Stack<Double> values = new Stack<Double>();

        // Stack for Operators: 'ops'
        Stack<Character> ops = new Stack<Character>();
        String num="0123456789.";
        for (int i=0,j = 0; i < tokens.length; i++)
        {
            // Current token is a number, push it to stack for numbers
            if (num.indexOf(tokens[i])>=0)
            {
                j=i;
                StringBuffer sbuf = new StringBuffer();
                // There may be more than one digits in number
                while (num.indexOf(tokens[j])>=0)
                {
                    sbuf.append(tokens[j]);
                    j++;
                }
                values.push(Double.parseDouble(sbuf.toString()));
                System.out.println("Pushing in values:"+Double.parseDouble(sbuf.toString()));
                i=j-1;
            }
            // Current token is an operator.
            else if (tokens[i] == '+' || tokens[i] == '-' ||
                    tokens[i] == 'x' || tokens[i] == '/' || tokens[i]=='%' ||tokens[i]=='^')
            {
                // While top of 'ops' has same or greater precedence to current
                // token, which is an operator. Apply operator on top of 'ops'
                // to top two elements in values stack
                while (!ops.empty() && hasPrecedence(tokens[i], ops.peek())){
                    double res= applyOp(ops.pop(), values.pop(), values.pop());
                    values.push(res);
                    System.out.println("Pushing the result back in values: "+res);
                }
                // Push current token to 'ops'.
                ops.push(tokens[i]);
                System.out.println("Pushing the operation: "+ tokens[i]);

            }
        }

        // Entire expression has been parsed at this point, apply remaining
        // ops to remaining values
        while (!ops.empty())
            values.push(applyOp(ops.pop(), values.pop(), values.pop()));

        // Top of 'values' contains result, return it
        return Double.parseDouble(String.valueOf(values.pop()));
    }

    // Returns true if 'op2' has higher or same precedence as 'op1',
    // otherwise returns false.
    public static boolean hasPrecedence(char op1, char op2)
    {
        if ((op1 == 'x' || op1 == '%'|| op1 == '/') && (op2 == '+' || op2 == '-'))
            return false;
        if ((op1 == '^' )&& (op2 == '+' || op2 == '-' ||op1 == '%'|| op2 == 'x' || op2 == '/'))
            return false;
        else
            return true;
    }

    // A utility method to apply an operator 'op' on operands 'a'
    // and 'b'. Return the result.
    public static double applyOp(char op, double b, double a)
    {
        switch (op)
        {
            case '+':
                return a + b;
            case '^':
                return Math.pow(a,b);
            case '-':
                return a - b;
            case 'x':
                return a * b;
            case '/':
            {
                if (b == 0) {
                    return -1;
                } else {
                    return a / b;
                }
            }
        }
        return 0;
    }
}