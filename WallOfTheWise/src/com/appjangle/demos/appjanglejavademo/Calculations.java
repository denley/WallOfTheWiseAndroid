/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.appjangle.demos.appjanglejavademo;

import io.nextweb.Node;
import io.nextweb.NodeList;
import io.nextweb.Session;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author <a href="http://www.mxro.de">Max Rohde</a>
 */
public class Calculations {

    Types t;
    Session session;
    
    public Map<String, Integer> calculatePostsPerUser(Node posts) {
        Map<String, Integer> res = new HashMap<String, Integer>();

        NodeList allUserNames = posts.selectAll(t.aPost())
                                     .selectAll(t.aUserName()).get();

        for (Node userName : allUserNames) {
            incrementUserPostCount(userName.value(String.class), res);
        }     

        return res;
    }

    private void incrementUserPostCount(String userName, Map<String, Integer> counts) {
        if (!counts.containsKey(userName)) {
            counts.put(userName, 1);
            return;
        }
        
        counts.put(userName, counts.get(userName) +1);
    }
    
    public Calculations(Session session) {
        this.session = session;
        this.t = new Types(session);
    }
}
