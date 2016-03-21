//package main.java.server.controller; проблема с путями пекеджей!!! было после закачки и выдавало ошибку
package server.controller; // исправил ошибку

import server.model.ISServer;

/**
 * Created by Dmitry Khoruzhenko, Oleksandr Dudkin on 15.02.2016.
 * Information system Server
 */
public class Main_Server {

    public static void main(String[] args) {
        System.out.println("I am server");  // debug
        ISServer server1 = new ISServer();
    }
}
