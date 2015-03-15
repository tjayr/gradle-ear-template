package com.lmig.pcif.example;

import static org.junit.Assert.assertEquals;

import org.apache.log4j.Logger;
import org.apache.log4j.spi.LoggerFactory;
import org.hsqldb.Server;
import org.hsqldb.persist.HsqlProperties;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.persistence.DataSource;
import org.jboss.arquillian.persistence.ShouldMatchDataSet;
import org.jboss.arquillian.persistence.UsingDataSet;
import org.jboss.arquillian.transaction.api.annotation.Transactional;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.EnterpriseArchive;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.ejb.EJB;
import javax.naming.NamingException;
import java.io.File;

/**
 * CalculatorTest.
 */
@RunWith(Arquillian.class)
public class CalculatorEjbTest {

    private static org.apache.log4j.Logger log = Logger.getLogger(CalculatorEjb.class);

    private Server server;

    @EJB
    private CalculatorInterface calculator;

    @Deployment(name = "CalculatorEjbTest")
    public static Archive<?> createDeployment() {
        log.info("create deployment");
        JavaArchive jar = ShrinkWrap.create(JavaArchive.class, "ejb.jar")
                .addClasses(CalculatorEjb.class, CalculatorEjbTest.class, CalculatorInterface.class)
                .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml")
                .addAsManifestResource("ibm-ejb-bnd.xml")
                .addAsManifestResource("was-persistence/persistence.xml", "persistence.xml");

        //File[] deps = Maven.resolver().resolve("org.hsqldb:hsqldb:2.3.1").withTransitivity().asFile();

        File[] deps = Maven.resolver().resolve("org.hibernate:hibernate-entitymanager:3.6.7.Final").withTransitivity().asFile();


        EnterpriseArchive ear = ShrinkWrap.create(EnterpriseArchive.class, "calculator.ear");
        ear.addAsModule(jar);
        ear.addAsLibraries(deps);
        return ear;
    }


    public void getDS() {
        javax.naming.InitialContext ctx = null;
        javax.sql.DataSource ds = null;
        log.info("Attempting connection...");
        System.out.println("connect");
        try {
            ctx = new javax.naming.InitialContext();
            ds = (javax.sql.DataSource) ctx.lookup("jdbc/derby");
            log.info(ds.toString());
            System.out.println(ds.toString());
        } catch (NamingException e) {
            System.out.println(e) ;
            log.error("peformanceappraisalstatus: COULDN'T CREATE CONNECTION!");
            e.printStackTrace();
        }
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
        log.debug("testAdd");
        System.out.println("testAdd");
        getDS();
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
