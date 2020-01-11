package com.example.calculator;

import android.app.Activity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import java.text.DecimalFormat;
import java.text.NumberFormat;

public class MainActivity extends Activity {

    TextView calculation, answer;
    String sCalculation = "", sAnswer = "", number_one = "", number_two = "", current_operator = "", prev_ans = "";
    Double Result = 0.0, numberOne = 0.0, numberTwo = 0.0, temp = 0.0;
    Boolean dot_present = false, number_allow = true, power_present = false;

    NumberFormat format, longformate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        calculation = findViewById(R.id.calculation);
        calculation.setMovementMethod(new ScrollingMovementMethod());
        answer = findViewById(R.id.answer);
        format = new DecimalFormat("#.####");
        longformate = new DecimalFormat("0.#E0");
    }

    public void onClickNumber(View v) {
        if (number_allow) {
            Button bn = (Button) v;
            sCalculation += bn.getText();
            number_one += bn.getText();
            numberOne = Double.parseDouble(number_one);
            }
            switch (current_operator) {
                case "":
                case "+":
                    if (power_present) {
                        temp = Result + Math.pow(numberTwo, numberOne);
                    }else{
                        temp = Result + numberOne;
                    }
                    break;
                case "-":
                    if (power_present) {
                        temp = Result - Math.pow(numberTwo, numberOne);
                    } else {
                        temp = Result - numberOne;
                    }
                    break;
                case "x":
                    if (power_present) {
                        temp = Result * Math.pow(numberTwo, numberOne);
                    } else {
                        temp = Result * numberOne;
                    }
                    break;
                case "/":
                    try {
                        // divided by 0 cause exception
                        if (power_present) {
                            temp = Result / Math.pow(numberTwo, numberOne);
                        } else {
                            temp = Result / numberOne;
                        }
                    } catch (Exception e) {
                        sAnswer = e.getMessage();
                    }
                    break;
            }
            sAnswer = format.format(temp);
            updateCalculation();
        }

    public void onClickOperator(View v) {
        Button ob = (Button) v;
        if (sAnswer != "") {
            if (current_operator != "") {
                char c = getcharfromLast(sCalculation, 2);
                if (c == '+' || c == '-' || c == 'x' || c == '/') {
                    sCalculation = sCalculation.substring(0, sCalculation.length() - 3);
                }
            }
            sCalculation = sCalculation + "\n" + ob.getText() + " ";
            number_one = "";
            Result = temp;
            current_operator = ob.getText().toString();
            updateCalculation();
            number_two = "";
            numberTwo = 0.0;
            dot_present = false;
            number_allow = true;
            power_present = false;
        }
    }

    private char getcharfromLast(String s, int i) {
        char c = s.charAt(s.length() - i);
        return c;
    }

    public void onClickClear(View v) {
        cleardata();
    }

    public void cleardata() {
        sCalculation = "";
        sAnswer = "";
        current_operator = "";
        number_one = "";
        number_two = "";
        prev_ans = "";
        Result = 0.0;
        numberOne = 0.0;
        numberTwo = 0.0;
        temp = 0.0;
        updateCalculation();
        dot_present = false;
        number_allow = true;
        power_present = false;
    }

    public void updateCalculation() {
        calculation.setText(sCalculation);
        answer.setText(sAnswer);
    }

    public void onDotClick(View view) {
        if (!dot_present) {
            if (number_one.length() == 0) {
                number_one = "0.";
                sCalculation += "0.";
                sAnswer = "0.";
                dot_present = true;
                updateCalculation();
            } else {
                number_one += ".";
                sCalculation += ".";
                sAnswer += ".";
                dot_present = true;
                updateCalculation();
            }
        }
    }

    public void onClickEqual(View view) {
        showresult();
    }

    public void showresult() {
        if (sAnswer != "" && sAnswer != prev_ans) {
            sCalculation += "\n= " + sAnswer + "\n----------\n" + sAnswer + " ";
            number_one = "";
            number_two = "";
            numberTwo = 0.0;
            numberOne = 0.0;
            Result = temp;
            prev_ans = sAnswer;
            updateCalculation();
            dot_present = true;
            power_present = false;
            number_allow = false;
        }
    }

    public String removechar(String str, int i) {
        char c = str.charAt(str.length() - i);
        //we need to check if dot is removed or not
        if (c == '.' && !dot_present) {
            dot_present = false;
        }
        if (c == '^') {
            power_present = false;
        }
        if (c == ' ') {
            return str.substring(0, str.length() - (i - 1));
        }
        return str.substring(0, str.length() - i);
    }

    public void onPowerClick(View view) {
        Button power = (Button) view;
        if (sCalculation != "" && !power_present) {
            if (getcharfromLast(sCalculation, 1) != ' ') {
                sCalculation += power.getText().toString();
                //we need second variable for the power
                number_two = number_one;
                numberTwo = numberOne;
                number_one = "";
                power_present = true;
                updateCalculation();
            }
        }
    }

    public void onClickDelete(View view) {
        if (power_present) {
            removePower();
            return;
        }
        if (sAnswer != "") {
            if (getcharfromLast(sCalculation, 1) != ' ') {
                if (number_one.length() < 2 && current_operator != "") {
                    number_one = "";
                    temp = Result;
                    sAnswer = format.format(Result);
                    sCalculation = removechar(sCalculation, 1);
                    updateCalculation();
                } else {
                    switch (current_operator) {
                        case "":
                            if (sCalculation.length() < 2) {
                                cleardata();
                            } else {
                                if (getcharfromLast(sCalculation, 1) == '.') {
                                    dot_present = false;
                                }
                                number_one = removechar(number_one, 1);
                                numberOne = Double.parseDouble(number_one);
                                temp = numberOne;
                                sCalculation = number_one;
                                sAnswer = number_one;
                                updateCalculation();
                            }
                            break;
                        case "+":
                            if (getcharfromLast(sCalculation, 1) == '.') {
                                dot_present = false;
                            }
                            number_one = removechar(number_one, 1);
                            if (number_one.length() == 1 && number_one == ".") {
                                numberOne = Double.parseDouble(number_one);
                            }
                            numberOne = Double.parseDouble(number_one);
                            temp = Result + numberOne;
                            sAnswer = format.format(temp);
                            sCalculation = removechar(sCalculation, 1);
                            updateCalculation();
                            break;

                        case "-":
                            if (getcharfromLast(sCalculation, 1) == '.') {
                                dot_present = false;
                            }
                            number_one = removechar(number_one, 1);
                            if (number_one.length() == 1 && number_one == ".") {
                                numberOne = Double.parseDouble(number_one);
                            }
                            numberOne = Double.parseDouble(number_one);
                            temp = Result - numberOne;
                            sAnswer = format.format(temp);
                            sCalculation = removechar(sCalculation, 1);
                            updateCalculation();
                            break;

                        case "x":
                            if (getcharfromLast(sCalculation, 1) == '.') {
                                dot_present = false;
                            }
                            number_one = removechar(number_one, 1);
                            if (number_one.length() == 1 && number_one == ".") {
                                numberOne = Double.parseDouble(number_one);
                            }
                            numberOne = Double.parseDouble(number_one);
                            temp = Result * numberOne;
                            sAnswer = format.format(temp);
                            sCalculation = removechar(sCalculation, 1);
                            updateCalculation();
                            break;

                        case "/":
                            try {
                                if (getcharfromLast(sCalculation, 1) == '.') {
                                    dot_present = false;
                                }
                                number_one = removechar(number_one, 1);
                                if (number_one.length() == 1 && number_one == ".") {
                                    numberOne = Double.parseDouble(number_one);
                                }
                                numberOne = Double.parseDouble(number_one);
                                temp = Result / numberOne;
                                sAnswer = format.format(temp);
                                sCalculation = removechar(sCalculation, 1);
                            } catch (Exception e) {
                                sAnswer = e.getMessage();
                            }
                            updateCalculation();
                            break;
                    }
                }
            }
        }
    }

    public void removePower() {
        if (sAnswer != "" && sCalculation != "") {
            switch (current_operator) {
                case "":
                    if (getcharfromLast(sCalculation, 1) == '^') {
                        sCalculation = removechar(sCalculation, 1);
                        number_one = number_two;
                        numberOne = Double.parseDouble(number_one);
                        number_two = "";
                        numberTwo = 0.0;
                        updateCalculation();
                    } else if (getcharfromLast(sCalculation, 2) == '^') {
                        number_one = "";
                        numberOne = 0.0;
                        temp = numberTwo;
                        sAnswer = format.format(temp);
                        sCalculation = removechar(sCalculation, 1);
                        updateCalculation();
                    } else {
                        if (getcharfromLast(sCalculation, 1) == '.') {
                            dot_present = false;
                        }
                        number_one = removechar(number_one, 1);
                        numberOne = Double.parseDouble(number_one);
                        temp = Math.pow(numberTwo, numberOne);
                        sAnswer = format.format(temp);
                        sCalculation = removechar(sCalculation, 1);
                        updateCalculation();
                    }
                    break;

                case "+":
                    if (getcharfromLast(sCalculation, 1) == '^') {
                        sCalculation = removechar(sCalculation, 1);
                        number_one = number_two;
                        numberOne = Double.parseDouble(number_one);
                        number_two = "";
                        numberTwo = 0.0;
                        updateCalculation();
                    } else if (getcharfromLast(sCalculation, 2) == '^') {
                        number_one = "";
                        numberOne = 0.0;
                        temp = Result + numberTwo;
                        sAnswer = format.format(temp);
                        sCalculation = removechar(sCalculation, 1);
                        updateCalculation();
                    } else {
                        if (getcharfromLast(sCalculation, 1) == '.') {
                            dot_present = false;
                        }
                        number_one = removechar(number_one, 1);
                        numberOne = Double.parseDouble(number_one);
                        temp = Result + Math.pow(numberTwo, numberOne);
                        sAnswer = format.format(temp);
                        sCalculation = removechar(sCalculation, 1);
                        updateCalculation();
                    }
                    break;

                case "-":
                    if (getcharfromLast(sCalculation, 1) == '^') {
                        sCalculation = removechar(sCalculation, 1);
                        number_one = number_two;
                        numberOne = Double.parseDouble(number_one);
                        number_two = "";
                        numberTwo = 0.0;
                        updateCalculation();
                    } else if (getcharfromLast(sCalculation, 2) == '^') {
                        number_one = "";
                        numberOne = 0.0;
                        temp = Result - numberTwo;
                        sAnswer = format.format(temp);
                        sCalculation = removechar(sCalculation, 1);
                        updateCalculation();
                    } else {
                        if (getcharfromLast(sCalculation, 1) == '.') {
                            dot_present = false;
                        }
                        number_one = removechar(number_one, 1);
                        numberOne = Double.parseDouble(number_one);
                        temp = Result - Math.pow(numberTwo, numberOne);
                        sAnswer = format.format(temp);
                        sCalculation = removechar(sCalculation, 1);
                        updateCalculation();
                    }
                    break;

                case "x":
                    if (getcharfromLast(sCalculation, 1) == '^') {
                        sCalculation = removechar(sCalculation, 1);
                        number_one = number_two;
                        numberOne = Double.parseDouble(number_one);
                        number_two = "";
                        numberTwo = 0.0;
                        updateCalculation();
                    } else if (getcharfromLast(sCalculation, 2) == '^') {
                        number_one = "";
                        numberOne = 0.0;
                        temp = Result * numberTwo;
                        sAnswer = format.format(temp);
                        sCalculation = removechar(sCalculation, 1);
                        updateCalculation();
                    } else {
                        if (getcharfromLast(sCalculation, 1) == '.') {
                            dot_present = false;
                        }
                        number_one = removechar(number_one, 1);
                        numberOne = Double.parseDouble(number_one);
                        temp = Result * Math.pow(numberTwo, numberOne);
                        sAnswer = format.format(temp);
                        sCalculation = removechar(sCalculation, 1);
                        updateCalculation();
                    }
                    break;

                case "/":
                    try {
                        if (getcharfromLast(sCalculation, 1) == '^') {
                            sCalculation = removechar(sCalculation, 1);
                            number_one = number_two;
                            numberOne = Double.parseDouble(number_one);
                            number_two = "";
                            numberTwo = 0.0;
                            updateCalculation();
                        } else if (getcharfromLast(sCalculation, 2) == '^') {
                            number_one = "";
                            numberOne = 0.0;
                            temp = Result / numberTwo;
                            sAnswer = format.format(temp);
                            sCalculation = removechar(sCalculation, 1);
                            updateCalculation();
                        } else {
                            if (getcharfromLast(sCalculation, 1) == '.') {
                                dot_present = false;
                            }
                            number_one = removechar(number_one, 1);
                            numberOne = Double.parseDouble(number_one);
                            temp = Result / Math.pow(numberTwo, numberOne);
                            sAnswer = format.format(temp);
                            sCalculation = removechar(sCalculation, 1);
                            updateCalculation();
                        }
                    } catch (Exception e) {
                        sAnswer = e.getMessage();
                    }
                    updateCalculation();
                    break;
            }
        }
    }


}