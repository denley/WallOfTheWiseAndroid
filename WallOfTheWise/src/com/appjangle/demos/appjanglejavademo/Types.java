/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.appjangle.demos.appjanglejavademo;

import io.nextweb.Link;
import io.nextweb.Session;

/**
 *
 * @author mroh004
 */
public class Types {
   
    private final Session session;
    
   
    public Link aPost() {
        return session.link("http://slicnet.com/seed1/seed1/3/6/5/2/h/sd/aPost1");
    } 
    
    public Link aUserName() {
        return session.link("http://slicnet.com/seed1/seed1/3/9/2/3/h/sd/userName");
    }

    public Types(Session session) {
        this.session = session;
    }
    
}
