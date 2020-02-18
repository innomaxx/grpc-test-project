
package com.max.grpc.projects.echo.server;

public class Main {
    public static void main(String[] args) {
        var server = new EchoServer();
        server.start();
        server.blockUntilShutdown();
    }
}
