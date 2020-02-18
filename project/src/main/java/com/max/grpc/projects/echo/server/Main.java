
package com.max.grpc.projects.echo.server;

public class Main {
    public static void main(String[] args) {
        final EchoServer server = new EchoServer();
        server.start();
        server.blockUntilShutdown();
    }
}
