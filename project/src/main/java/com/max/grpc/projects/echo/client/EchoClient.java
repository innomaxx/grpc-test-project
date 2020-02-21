
package com.max.grpc.projects.echo.client;

import com.max.grpc.projects.echo.protos.EchoEntity;
import com.max.grpc.projects.echo.protos.EchoServiceGrpc;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;

import org.apache.log4j.Logger;

import java.util.concurrent.TimeUnit;

import static com.max.grpc.projects.echo.protos.EchoServiceGrpc.EchoServiceBlockingStub;

public class EchoClient {
    private final Logger logger = Logger.getLogger(EchoClient.class);
    private final ManagedChannel managedChannel;
    private final EchoServiceBlockingStub blockingStub;

    public EchoClient(String host, int port) {
        this(ManagedChannelBuilder.forAddress(host, port)
            .usePlaintext()
            .build());
    }

    public EchoClient(ManagedChannel channel) {
        this.managedChannel = channel;
        blockingStub = EchoServiceGrpc.newBlockingStub(channel);
    }

    public void sendEcho(int code, String message) {
        logger.info("Trying to send echo message");
        try {
            EchoEntity request = EchoEntity.newBuilder().setCode(code).setMessage(message).build();
            EchoEntity response = blockingStub.sendEcho(request);
            String logString = String.format("Response received: code = %d, message = \"%s\"", response.getCode(), response.getMessage());
            logger.info(logString);
        }
        catch (StatusRuntimeException ex) {
            logger.fatal("Resource unavailable");
        }
    }

    public void shutdown() {
        try {
            managedChannel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
        }
        catch (InterruptedException ex) {
            logger.error("Failed to stop gRPC channel", ex);
        }
    }
}
