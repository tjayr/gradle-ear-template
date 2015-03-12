package com.lmig.pcif.example;

import javax.ejb.Stateless;


/**
 * Calculator Ejb
 */
@Stateless
public class CalculatorEjb implements CalculatorInterface {



    /**
     * listOfInt
     * @param listOfInt
     * @return
     */
    public int add(int... listOfInt) {

        int result = 0;
        for (int i : listOfInt) {
            result += i;
        }

        return result;
    }

    /**
     * Multiply
     * @param a
     * @param b
     * @return
     */
    public int multiply(int a, int b) {

        return a * b;
    }

}
