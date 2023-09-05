package com.lung.utils;

public class TraceUtils {

    public static String getTraceInfo(Exception ex) {
        String info = "";
        try {
            StackTraceElement[] stack = ex.getStackTrace();
            info = stack[0].getClassName() + "." + stack[0].getMethodName() + "(" + stack[0].getLineNumber() + ")";
        } catch (Exception e) {
            info = ex.getLocalizedMessage();
        }
        return info;
    }

    public static String getTraceInfo(Throwable cause) {
        String info = "";
        try {
            StackTraceElement[] stack = cause.getStackTrace();
            info = stack[0].getClassName() + "." + stack[0].getMethodName() + "(" + stack[0].getLineNumber() + ")";
        } catch (Exception e) {
            info = cause.getLocalizedMessage();
        }
        return info;
    }
}
