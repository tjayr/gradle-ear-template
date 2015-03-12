package com.lmig.pcif.example;

import static org.junit.Assert.assertEquals;

import org.hsqldb.Server;
import org.hsqldb.persist.HsqlProperties;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.EnterpriseArchive;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.EJB;
import java.io.File;
import java.io.IOException;

/**
 * CalculatorTest.
 */
@RunWith(Arquillian.class)
public class CalculatorEjbTest {

    private static Logger log = LoggerFactory.getLogger(CalculatorEjbTest.class);

    private Server server;

    @EJB
    private CalculatorInterface calculator;

    @After
    public void stopDb() {
        if(server != null) {
            server.stop();
        }
    }

    @Before
    public void startDb() throws Exception {

        log.info("start db");
        String path = System.getProperty("user.dir");

        System.out.println("Starting Database");
        HsqlProperties p = new HsqlProperties();
        p.setProperty("server.database.0", "file:"+path+"/testdb");
        p.setProperty("server.dbname.0", "mydb");
        p.setProperty("server.port", "9001");
        server = new Server();
        server.setProperties(p);
        server.setLogWriter(null); // can use custom writer
        server.setErrWriter(null); // can use custom writer
        server.start();


    }

    @Deployment(name = "CalculatorEjbTest")
    public static Archive<?> createDeployment() {
        log.info("create deployment");
        JavaArchive jar = ShrinkWrap.create(JavaArchive.class, "ejb.jar")
                .addClasses(CalculatorEjb.class, CalculatorEjbTest.class, CalculatorInterface.class)
                .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
        File[] deps = Maven.resolver().resolve("org.hsqldb:hsqldb:2.3.1").withTransitivity().asFile();

        EnterpriseArchive ear = ShrinkWrap.create(EnterpriseArchive.class, "calculator.ear");
        ear.addAsModule(jar);
        ear.addAsLibraries(deps);
        return ear;
    }

    /**
     * testAdd.
     */
    @Test
    public void testAdd() {
        int result = calculator.add(new int[]{1, 2, 3, 4});
        assertEquals(10, result);
    }

    /**
     * testMultiply.
     */
    @Test
    public void testMultiply() {
        int result = calculator.multiply(2, 5);
        assertEquals(10, result);
    }
}
