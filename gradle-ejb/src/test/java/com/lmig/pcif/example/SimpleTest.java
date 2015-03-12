package com.lmig.pcif.example;

import org.hsqldb.persist.HsqlProperties;
import org.hsqldb.server.Server;
import org.hsqldb.server.ServerAcl;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Created by tony on 12/03/15.
 */
public class SimpleTest {
    private static org.slf4j.Logger log = LoggerFactory.getLogger(SimpleTest.class);

    private static Server server;

    @BeforeClass
    public static void setup() throws IOException, ServerAcl.AclFormatException {

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

    @AfterClass
    public static void stopDb() throws IOException, ServerAcl.AclFormatException {
        log.info("stop db");
        if(server != null) {
            server.stop();
        }
    }

    @Test
    public void blah() {

    }
}
