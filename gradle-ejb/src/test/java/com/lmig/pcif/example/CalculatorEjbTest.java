package com.lmig.pcif.example;

import static org.junit.Assert.assertEquals;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.persistence.*;
import org.jboss.arquillian.transaction.api.annotation.Transactional;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.EnterpriseArchive;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.ejb.EJB;
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

    @Deployment(name = "CalculatorEjbTest")
    public static Archive<?> createDeployment() {
        System.out.println("create deployment");
        JavaArchive jar = ShrinkWrap.create(JavaArchive.class, "ejb.jar")
                .addClasses(CalculatorEjb.class, CalculatorEjbTest.class, CalculatorInterface.class)
                .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml")
                .addAsManifestResource("ibm-ejb-bnd.xml")
                .addAsManifestResource("was-persistence/persistence.xml", "persistence.xml");

        File[] deps = Maven.resolver().resolve("org.eclipse.persistence:eclipselink:2.5.0").withTransitivity().asFile();
        //File[] deps = Maven.resolver().resolve("org.hibernate:hibernate-entitymanager:3.6.7.Final").withTransitivity().asFile();

        EnterpriseArchive ear = ShrinkWrap.create(EnterpriseArchive.class, "calculator.ear");
        ear.addAsModule(jar);
        ear.addAsLibraries(deps);
        return ear;
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
