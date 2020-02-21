
package com.max.grpc.projects.echo.server;

public class Main {
    public static void main(String[] args) {
        var server = new EchoServer(50000);
        server.start();
        server.blockUntilShutdown();
    }
}
