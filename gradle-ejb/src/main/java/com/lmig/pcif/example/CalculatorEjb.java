package com.lmig.pcif.example;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.Local;
import javax.ejb.Remote;
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
