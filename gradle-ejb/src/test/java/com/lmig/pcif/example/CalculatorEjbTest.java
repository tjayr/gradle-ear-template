package com.lmig.pcif.example;

import static org.junit.Assert.assertEquals;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.persistence.*;
import org.jboss.arquillian.transaction.api.annotation.Transactional;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.asset.StringAsset;
import org.jboss.shrinkwrap.api.spec.EnterpriseArchive;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.ejb.EJB;
import javax.naming.InitialContext;

import java.io.File;

/**
 * CalculatorTest.
 */
@RunWith(Arquillian.class)
@CreateSchema("create_useradd.sql")
@Cleanup(phase = TestExecutionPhase.NONE)
public class CalculatorEjbTest {

    @EJB
    private CalculatorInterface calculator;

    @Deployment(name = "calculator")
    public static Archive<?> createDeployment() {
        System.out.println("create deployment");

        JavaArchive ejbClientJar = ShrinkWrap.create(JavaArchive.class, "client.jar")
                .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml")
                .addClasses(CalculatorEjbTest.class, CalculatorInterface.class);

        JavaArchive jar = ShrinkWrap.create(JavaArchive.class, "calculator-ejb.jar")
                .addClasses(CalculatorEjb.class, CalculatorEjbTest.class, CalculatorInterface.class)
                .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml")
                .addAsManifestResource(
                        new StringAsset(
                                "<ejb-jar xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n"
                                        + " xmlns=\"http://java.sun.com/xml/ns/javaee\"\n"
                                        + " xmlns:ejb=\"http://java.sun.com/xml/ns/javaee/ejb-jar_3_0.xsd\"\n"
                                        + " xsi:schemaLocation=\"http://java.sun.com/xml/ns/javaee http://java.sun.com/xml/ns/javaee/ejb-jar_3_0.xsd\"\n"
                                        + " version=\"3.0\">\n" + " <ejb-client-jar>calculator-ejb.jar</ejb-client-jar>\n"
                                        + "</ejb-jar>"), "ejb-jar.xml")
                //.setManifest(new StringAsset("Manifest-Version: 1.0\nClass-Path: client.jar\n"))
                .addAsManifestResource("was-persistence/persistence.xml", "persistence.xml");


        File[] deps = Maven.resolver().resolve("org.eclipse.persistence:eclipselink:2.5.0").withTransitivity().asFile();


        WebArchive war = ShrinkWrap.create(WebArchive.class);
        war.addAsLibrary(jar);
        war.addAsLibraries(deps);
        war.addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");

        EnterpriseArchive ear = ShrinkWrap.create(EnterpriseArchive.class);
        ear.addAsModule(jar);
        //ear.addAsModule(ejbClientJar);
        ear.addAsLibraries(deps);
        ear.addAsManifestResource("application.xml", "application.xml");


        return war;
    }


    @Test
    @Ignore
    public void should_be_able_to_lookup_and_invoke_ejb() throws Exception {
        String name = "Earthlings";
        String lookup = "java:global/gradle-ear/gradle-ejb/CalculatorEjb";

        CalculatorInterface calc = (CalculatorInterface) new InitialContext().lookup("java:global/test/service/GreeterDelegate");
        calc.add(1,1);
    }


    /**
     * testAdd.
     */
    @Test
    @DataSource("jdbc/derby")
    @UsingDataSet("datasets/dummy-dataset.xml")
    @ShouldMatchDataSet("datasets/dummy-dataset.xml")
    @Transactional
    public void testAdd() {
        System.out.println("testAdd");
        int result = calculator.add(new int[]{1, 2, 3, 4});
        assertEquals(10, result);
    }

    /**
     * testMultiply.
     */
    @Test
    @Transactional
    @DataSource("jdbc/derby")
    public void testMultiply() {
        int result = calculator.multiply(2, 5);
        assertEquals(10, result);
    }
}
