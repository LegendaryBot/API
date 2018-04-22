package com.greatmancode.legendarybotapi.utils;

import io.sentry.Sentry;
import io.sentry.SentryClient;

import java.util.Arrays;

public class UncaughtExceptionHandler implements Thread.UncaughtExceptionHandler {

    private static UncaughtExceptionHandler handler = null;
    private SentryClient client;
    public UncaughtExceptionHandler() {
        client = Sentry.init();

    }
    @Override
    public void uncaughtException(Thread t, Throwable e) {
        client.sendException(e);
    }

    public void sendException(Throwable e, String... tags) {
        Arrays.stream(tags).forEach((v) -> client.addTag(v.split(":")[0], v.split(":")[1]));
        client.sendException(e);
        client.setTags(null);
    }
    public static UncaughtExceptionHandler getHandler() {
        if (handler == null) {
            handler = new UncaughtExceptionHandler();
        }
        return handler;
    }
}
