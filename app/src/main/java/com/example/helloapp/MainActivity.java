package com.example.helloapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    TextView resultField; //текстовое поле для вывода результата
    EditText numberField; //текстовое поле для ввода цифр
    TextView operationField; // текстовое поле для вывод операций
    Double operand = null; //операнд операции
    String lastOperation = "="; //последняя операция

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        resultField = findViewById(R.id.resultText);
        numberField = findViewById(R.id.numberInput);
        operationField = findViewById(R.id.operationText);
    }

    //сохранение состояния
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString("OPERATION", lastOperation);
        if (operand != null)
            outState.putDouble("OPERAND", operand);
        super.onSaveInstanceState(outState);
    }

    //получение ранее сохраненного состояния
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        lastOperation = savedInstanceState.getString("OPERATION");
        operand = savedInstanceState.getDouble("OPERAND");
        resultField.setText(operand.toString());
        operationField.setText(lastOperation);
    }

    public void onNumberClick(View view) {
        Button button = (Button) view;
        numberField.append(button.getText());

        if (lastOperation.equals("=") && operand != null)
            operand = null;
    }

    public void onOperationClick(View view) {
        Button button = (Button) view;
        String op = button.getText().toString();
        String number = numberField.getText().toString();

        if (number.length() > 0){
            number = number.replace(',', '.');
            try {
                performOperation(Double.valueOf(number), op);
            } catch (NumberFormatException ex) {
                numberField.setText("");
            }
        }
        lastOperation = op;
        operationField.setText(lastOperation);
    }

    public void onSpecialOperation(View view) {
        Button button = (Button) view;
        String op = button.getText().toString();

        if (op.equals("C"))
        {
            lastOperation = "=";
            operand = null;
            operationField.setText("");
            numberField.setText("");
            resultField.setText("");
        }

        String number = numberField.getText().toString();

        if (number.length() > 0){
            switch (op){
                case "<-":
                    number = number.substring(0, number.length() - 1);
                    break;
                case "+-":
                    if (number.startsWith("-"))
                        number = number.substring(1);
                    else
                        number = "-" + number;
                    break;
            }
        }

        numberField.setText(number);
    }

    private void performOperation(Double valueOf, String op) {
        if (operand == null){
            operand = valueOf;
        }
        else {
            if (lastOperation.equals("=")){
                lastOperation = op;
            }
            switch (lastOperation){
                case "=":
                    operand = valueOf;
                    break;
                case "/":
                    if (valueOf==0)
                        operand=0.0;
                    else
                        operand/=valueOf;
                    break;
                case "*":
                    operand*=valueOf;
                    break;
                case "+":
                    operand += valueOf;
                    break;
                case "-":
                    operand -= valueOf;
                    break;
                case "%":
                    operand %= valueOf;
                    break;
            }
        }
        resultField.setText(operand.toString().replace(".", ","));
        numberField.setText("");
    }
}