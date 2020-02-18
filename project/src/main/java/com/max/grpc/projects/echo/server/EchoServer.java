
package com.max.grpc.projects.echo.server;

import com.max.grpc.projects.echo.protos.EchoEntity;
import com.max.grpc.projects.echo.protos.EchoServiceGrpc;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class EchoServer {
    private static final Logger logger = Logger.getLogger(EchoServer.class.getName());

    private int port;
    private Server server;

    public EchoServer(int port) {
        this.port = port;
    }

    public void start() {
        Server server = ServerBuilder.forPort(port).addService(new EchoImpl()).build();
        try {
            server.start();
            logger.info("Server started, listening on " + port);
            this.server = server;
        }
        catch (IOException ex) {
            logger.severe("Server initialization failed");
            System.exit(0);
        }
    }

    public void stop() {
        if (server != null) {
            try {
                server.shutdown().awaitTermination(30, TimeUnit.SECONDS);
            }
            catch (InterruptedException ex) {
                logger.severe("Runtime interrupted");
            }
        }
    }

    public void blockUntilShutdown() {
        if (server != null) {
            try {
                server.awaitTermination();
            }
            catch (InterruptedException ex) {
                logger.severe("Runtime interrupted");
            }
        }
    }

    static class EchoImpl extends EchoServiceGrpc.EchoServiceImplBase {
        @Override
        public void sendEcho(EchoEntity request, StreamObserver<EchoEntity> responseObserver) {
            int requestCode = request.getCode();
            String requestMessage = request.getMessage();

            EchoEntity reply = EchoEntity.newBuilder()
                    .setCode(requestCode)
                    .setMessage(requestMessage)
                    .build();

            responseObserver.onNext(reply);
            responseObserver.onCompleted();
        }
    }
}
