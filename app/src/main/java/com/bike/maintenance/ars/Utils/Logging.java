package com.bike.maintenance.ars.Utils;

import android.util.Log;

import java.io.PrintWriter;
import java.io.StringWriter;

public class Logging {
    private final String mLogTag;
    private final String mClassName;

    public Logging(Class<?> clazz) {
        mLogTag = "Common";
        mClassName = clazz.getSimpleName();
    }

    public void captureStack() {
        StackTraceElement[] trace = Thread.currentThread().getStackTrace();
        for (int i = 3; i < trace.length; i++) {
            error("--", trace[i].toString());
        }
    }

    public void captureException(Exception e) {
        StringWriter writer = new StringWriter();
        e.printStackTrace(new PrintWriter(writer));
        String stackTrace = writer.toString();
        error("Caught exception", stackTrace);
    }

    public void verbose(Object... messageComponents) {
        doLog(Log.VERBOSE, messageComponents);
    }

    public void debug(Object... messageComponents) {
        doLog(Log.DEBUG, messageComponents);
    }

    public void info(Object... message) {
        doLog(Log.INFO, message);
    }

    public void warning(Object... messageComponents) {
        doLog(Log.WARN, messageComponents);
    }

    public void error(Object... message) {
        doLog(Log.ERROR, message);
    }

    public void exception(Exception e) {
        exception(e, "");
    }

    public void exception(Exception e, String message) {

        StackTraceElement ste = e.getStackTrace()[0];
        StackTraceElement[] currentStackTrace = Thread.currentThread().getStackTrace();

        for (int i = currentStackTrace.length - 1; i > 0; i--) {
            if (currentStackTrace[i].toString().contains(getClass().getPackage().getName())) {
                break;
            }
            ste = currentStackTrace[i];
        }

        String exception = "Exception [";
        exception += e.toString() + "] on " +
                ste.getClassName() + ":" +
                ste.getLineNumber() + " " + message;
        doLog(Log.ERROR, exception);
    }

    private String buildMessage(Object... components) {
        StringBuilder tmpMessage = new StringBuilder();

        for (int i = 0; i < components.length; i++) {
            if (i != 0) tmpMessage.append(" ");
            if (components[i] == null) {
                tmpMessage.append("null");
            } else {
                int remaining = Math.max(0, 2048 - tmpMessage.length());
                if (remaining > components[i].toString().length()) {
                    tmpMessage.append(components[i].toString());
                } else {
                    tmpMessage.append(components[i].toString().substring(0, remaining));
                    break;
                }
            }
        }

        return "[" + mClassName + "] " + tmpMessage.toString();
    }

    private String doLog(int priority, Object... messageComponents) {
        String msg = buildMessage(messageComponents);
        Log.println(priority, mLogTag, msg);
        return msg;
    }
}