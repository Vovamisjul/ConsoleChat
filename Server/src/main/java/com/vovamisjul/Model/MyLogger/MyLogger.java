package com.vovamisjul.Model.MyLogger;

public class MyLogger {
    public static void trace(String message) {
        System.out.println("Trace: "+message);
    }
    public static void info(String message) {
        System.out.println("Info: "+message);
    }
    public static void error(String message) {
        System.out.println("Error: "+message);
    }
}
