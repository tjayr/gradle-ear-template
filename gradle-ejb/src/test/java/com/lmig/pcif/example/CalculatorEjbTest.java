package com.lmig.pcif.example;

import static org.junit.Assert.assertEquals;

import javax.ejb.EJB;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.EnterpriseArchive;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
public class CalculatorEjbTest {

    @Deployment(name = "CalculatorEjbTest")
    public static Archive<?> createDeployment() {
        JavaArchive jar = ShrinkWrap.create(JavaArchive.class, "ejb.jar")
                .addClasses(CalculatorEjb.class, CalculatorEjbTest.class)
                .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");

        EnterpriseArchive ear = ShrinkWrap.create(EnterpriseArchive.class, "calculator.ear").addAsModule(jar);


        return ear;
    }

    @EJB
    private CalculatorEjb calculator;

    @Test
    public void testAdd() {
        int result = calculator.add(new int[] { 1, 2, 3, 4 });
        assertEquals(10, result);
    }

    @Test
    public void testMultiply() {
        int result = calculator.multiply(2, 5);
        assertEquals(10, result);
    }
}