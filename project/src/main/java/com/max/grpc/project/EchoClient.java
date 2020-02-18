
package com.max.grpc.project;

import com.max.grpc.protos.EchoEntity;
import com.max.grpc.protos.EchoServiceGrpc;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;

import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

public class EchoClient {
    private static final Logger logger = Logger.getLogger(EchoClient.class.getName());

    private final ManagedChannel channel;
    private final EchoServiceGrpc.EchoServiceBlockingStub blockingStub;

    public EchoClient(String host, int port) {
        this(ManagedChannelBuilder.forAddress(host, port)
            .usePlaintext()
            .build());
    }

    EchoClient(ManagedChannel channel) {
        this.channel = channel;
        blockingStub = EchoServiceGrpc.newBlockingStub(channel);
    }

    public EchoEntity sendEcho(int code, String message) throws StatusRuntimeException {
        logger.info("Trying to send echo message");
        EchoEntity request = EchoEntity.newBuilder().setCode(123).setMessage(message).build();
        EchoEntity response = blockingStub.sendEcho(request);
        //logger.info("Request sent");
        String logString = String.format("\nResponse received: \n* Code : %d\n* Message : %s", response.getCode(), response.getMessage());
        logger.info(logString);
        return response;
    }

    public void shutdown() throws InterruptedException {
        channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
    }
}
