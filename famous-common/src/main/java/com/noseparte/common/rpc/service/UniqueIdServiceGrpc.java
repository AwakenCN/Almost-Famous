package com.noseparte.common.rpc.service;

import io.grpc.stub.ClientCalls;

import static io.grpc.MethodDescriptor.generateFullMethodName;
import static io.grpc.stub.ClientCalls.blockingUnaryCall;
import static io.grpc.stub.ClientCalls.futureUnaryCall;
import static io.grpc.stub.ServerCalls.asyncUnaryCall;
import static io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall;

/**
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.23.0)",
    comments = "Source: service.proto")
public final class UniqueIdServiceGrpc {

  private UniqueIdServiceGrpc() {}

  public static final String SERVICE_NAME = "idservice.UniqueIdService";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<com.noseparte.common.rpc.service.UniqueIdRequest,
      com.noseparte.common.rpc.service.UniqueIdResponse> getGetUniqueIdMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "getUniqueId",
      requestType = com.noseparte.common.rpc.service.UniqueIdRequest.class,
      responseType = com.noseparte.common.rpc.service.UniqueIdResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.noseparte.common.rpc.service.UniqueIdRequest,
      com.noseparte.common.rpc.service.UniqueIdResponse> getGetUniqueIdMethod() {
    io.grpc.MethodDescriptor<com.noseparte.common.rpc.service.UniqueIdRequest, com.noseparte.common.rpc.service.UniqueIdResponse> getGetUniqueIdMethod;
    if ((getGetUniqueIdMethod = UniqueIdServiceGrpc.getGetUniqueIdMethod) == null) {
      synchronized (UniqueIdServiceGrpc.class) {
        if ((getGetUniqueIdMethod = UniqueIdServiceGrpc.getGetUniqueIdMethod) == null) {
          UniqueIdServiceGrpc.getGetUniqueIdMethod = getGetUniqueIdMethod =
              io.grpc.MethodDescriptor.<com.noseparte.common.rpc.service.UniqueIdRequest, com.noseparte.common.rpc.service.UniqueIdResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "getUniqueId"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.noseparte.common.rpc.service.UniqueIdRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.noseparte.common.rpc.service.UniqueIdResponse.getDefaultInstance()))
              .setSchemaDescriptor(new UniqueIdServiceMethodDescriptorSupplier("getUniqueId"))
              .build();
        }
      }
    }
    return getGetUniqueIdMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static UniqueIdServiceStub newStub(io.grpc.Channel channel) {
    return new UniqueIdServiceStub(channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static UniqueIdServiceBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    return new UniqueIdServiceBlockingStub(channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static UniqueIdServiceFutureStub newFutureStub(
      io.grpc.Channel channel) {
    return new UniqueIdServiceFutureStub(channel);
  }

  /**
   */
  public static abstract class UniqueIdServiceImplBase implements io.grpc.BindableService {

    /**
     * <pre>
     * Sends a unique id
     * </pre>
     */
    public void getUniqueId(com.noseparte.common.rpc.service.UniqueIdRequest request,
        io.grpc.stub.StreamObserver<com.noseparte.common.rpc.service.UniqueIdResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getGetUniqueIdMethod(), responseObserver);
    }

    @Override public final io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            getGetUniqueIdMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                com.noseparte.common.rpc.service.UniqueIdRequest,
                com.noseparte.common.rpc.service.UniqueIdResponse>(
                  this, METHODID_GET_UNIQUE_ID)))
          .build();
    }
  }

  /**
   */
  public static final class UniqueIdServiceStub extends io.grpc.stub.AbstractStub<UniqueIdServiceStub> {
    private UniqueIdServiceStub(io.grpc.Channel channel) {
      super(channel);
    }

    private UniqueIdServiceStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @Override
    protected UniqueIdServiceStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new UniqueIdServiceStub(channel, callOptions);
    }

    /**
     * <pre>
     * Sends a unique id
     * </pre>
     */
    public void getUniqueId(com.noseparte.common.rpc.service.UniqueIdRequest request,
        io.grpc.stub.StreamObserver<com.noseparte.common.rpc.service.UniqueIdResponse> responseObserver) {
      ClientCalls.asyncUnaryCall(
          getChannel().newCall(getGetUniqueIdMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   */
  public static final class UniqueIdServiceBlockingStub extends io.grpc.stub.AbstractStub<UniqueIdServiceBlockingStub> {
    private UniqueIdServiceBlockingStub(io.grpc.Channel channel) {
      super(channel);
    }

    private UniqueIdServiceBlockingStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @Override
    protected UniqueIdServiceBlockingStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new UniqueIdServiceBlockingStub(channel, callOptions);
    }

    /**
     * <pre>
     * Sends a unique id
     * </pre>
     */
    public com.noseparte.common.rpc.service.UniqueIdResponse getUniqueId(com.noseparte.common.rpc.service.UniqueIdRequest request) {
      return blockingUnaryCall(
          getChannel(), getGetUniqueIdMethod(), getCallOptions(), request);
    }
  }

  /**
   */
  public static final class UniqueIdServiceFutureStub extends io.grpc.stub.AbstractStub<UniqueIdServiceFutureStub> {
    private UniqueIdServiceFutureStub(io.grpc.Channel channel) {
      super(channel);
    }

    private UniqueIdServiceFutureStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @Override
    protected UniqueIdServiceFutureStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new UniqueIdServiceFutureStub(channel, callOptions);
    }

    /**
     * <pre>
     * Sends a unique id
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<com.noseparte.common.rpc.service.UniqueIdResponse> getUniqueId(
        com.noseparte.common.rpc.service.UniqueIdRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getGetUniqueIdMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_GET_UNIQUE_ID = 0;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final UniqueIdServiceImplBase serviceImpl;
    private final int methodId;

    MethodHandlers(UniqueIdServiceImplBase serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_GET_UNIQUE_ID:
          serviceImpl.getUniqueId((com.noseparte.common.rpc.service.UniqueIdRequest) request,
              (io.grpc.stub.StreamObserver<com.noseparte.common.rpc.service.UniqueIdResponse>) responseObserver);
          break;
        default:
          throw new AssertionError();
      }
    }

    @Override
    @SuppressWarnings("unchecked")
    public io.grpc.stub.StreamObserver<Req> invoke(
        io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        default:
          throw new AssertionError();
      }
    }
  }

  private static abstract class UniqueIdServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    UniqueIdServiceBaseDescriptorSupplier() {}

    @Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return com.noseparte.common.rpc.service.Service.getDescriptor();
    }

    @Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("UniqueIdService");
    }
  }

  private static final class UniqueIdServiceFileDescriptorSupplier
      extends UniqueIdServiceBaseDescriptorSupplier {
    UniqueIdServiceFileDescriptorSupplier() {}
  }

  private static final class UniqueIdServiceMethodDescriptorSupplier
      extends UniqueIdServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final String methodName;

    UniqueIdServiceMethodDescriptorSupplier(String methodName) {
      this.methodName = methodName;
    }

    @Override
    public com.google.protobuf.Descriptors.MethodDescriptor getMethodDescriptor() {
      return getServiceDescriptor().findMethodByName(methodName);
    }
  }

  private static volatile io.grpc.ServiceDescriptor serviceDescriptor;

  public static io.grpc.ServiceDescriptor getServiceDescriptor() {
    io.grpc.ServiceDescriptor result = serviceDescriptor;
    if (result == null) {
      synchronized (UniqueIdServiceGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new UniqueIdServiceFileDescriptorSupplier())
              .addMethod(getGetUniqueIdMethod())
              .build();
        }
      }
    }
    return result;
  }
}
