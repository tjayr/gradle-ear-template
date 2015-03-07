package com.lmig.pcif.example;

import javax.ejb.Stateless;


@Stateless
public class CalculatorEjb  {

    public int add(int... listOfInt) {
        int result = 0;
        for (int i : listOfInt) {
            result += i;
        }
        return result;
    }

    public int multiply(int a, int b) {
        return a * b;
    }

}
