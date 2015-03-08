package com.lmig.pcif.example;

import javax.ejb.Remote;

/**
 * Created by tony on 08/03/15.
 */
@Remote
public interface CalculatorInterface {
    int add(int... listOfInt);
    int multiply(int a, int b);
}
