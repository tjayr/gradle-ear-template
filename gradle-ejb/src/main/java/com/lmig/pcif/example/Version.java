package com.lmig.pcif.example;

import javax.ejb.Stateless;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Stateless
@Path("version")
public class Version {

    @GET      
    @Produces(MediaType.APPLICATION_XML)
    public String retrieve() {
        return "<version>1.0</version>";
    }
    
}
