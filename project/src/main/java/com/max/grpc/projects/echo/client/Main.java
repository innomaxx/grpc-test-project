
package com.max.grpc.projects.echo.client;

import io.grpc.StatusRuntimeException;

public class Main {

    static int port = 50000;
    static int messageCode = 1234;
    static String messageText = "hello world";

    public static void main(String[] args) throws InterruptedException {
        var client = new EchoClient("localhost", port);
        try {
            client.sendEcho(messageCode, messageText);
        }
        catch (StatusRuntimeException ex) {
            System.out.println("Resource unavailable");
        }
        finally {
            client.shutdown();
        }
    }
}
