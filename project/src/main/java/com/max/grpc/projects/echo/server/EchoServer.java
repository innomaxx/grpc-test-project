
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

    private int port = 50000;
    private Server server;

    public void start() throws IOException {
        server = ServerBuilder.forPort(port)
                .addService(new EchoImpl())
                .build()
                .start();
        logger.info("Server started, listening on " + port);
    }

    public void stop() throws InterruptedException {
        if (server != null) {
            server.shutdown().awaitTermination(30, TimeUnit.SECONDS);
        }
    }

    public void blockUntilShutdown() throws InterruptedException {
        if (server != null) {
            server.awaitTermination();
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
