
package server.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import server.model.ISServer;

/**
 * Created by Dmitry Khoruzhenko, Oleksandr Dudkin on 15.02.2016.
 * Lab2 - Information system - Server - main class
 */
public class Main_Server {

    public static final Logger log = LogManager.getLogger(Main_Server.class);

    public static void main(String[] args) {
        System.out.println("I am server");  // debug
        ISServer server1 = new ISServer();
        log.info("Server was launched.");
    }
}
