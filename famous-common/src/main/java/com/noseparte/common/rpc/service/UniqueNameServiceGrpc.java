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
public final class UniqueNameServiceGrpc {

  private UniqueNameServiceGrpc() {}

  public static final String SERVICE_NAME = "idservice.UniqueNameService";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<com.noseparte.common.rpc.service.UniqueNameRequest,
      com.noseparte.common.rpc.service.UniqueNameResponse> getUniqueNameMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "uniqueName",
      requestType = com.noseparte.common.rpc.service.UniqueNameRequest.class,
      responseType = com.noseparte.common.rpc.service.UniqueNameResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.noseparte.common.rpc.service.UniqueNameRequest,
      com.noseparte.common.rpc.service.UniqueNameResponse> getUniqueNameMethod() {
    io.grpc.MethodDescriptor<com.noseparte.common.rpc.service.UniqueNameRequest, com.noseparte.common.rpc.service.UniqueNameResponse> getUniqueNameMethod;
    if ((getUniqueNameMethod = UniqueNameServiceGrpc.getUniqueNameMethod) == null) {
      synchronized (UniqueNameServiceGrpc.class) {
        if ((getUniqueNameMethod = UniqueNameServiceGrpc.getUniqueNameMethod) == null) {
          UniqueNameServiceGrpc.getUniqueNameMethod = getUniqueNameMethod =
              io.grpc.MethodDescriptor.<com.noseparte.common.rpc.service.UniqueNameRequest, com.noseparte.common.rpc.service.UniqueNameResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "uniqueName"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.noseparte.common.rpc.service.UniqueNameRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.noseparte.common.rpc.service.UniqueNameResponse.getDefaultInstance()))
              .setSchemaDescriptor(new UniqueNameServiceMethodDescriptorSupplier("uniqueName"))
              .build();
        }
      }
    }
    return getUniqueNameMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static UniqueNameServiceStub newStub(io.grpc.Channel channel) {
    return new UniqueNameServiceStub(channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static UniqueNameServiceBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    return new UniqueNameServiceBlockingStub(channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static UniqueNameServiceFutureStub newFutureStub(
      io.grpc.Channel channel) {
    return new UniqueNameServiceFutureStub(channel);
  }

  /**
   */
  public static abstract class UniqueNameServiceImplBase implements io.grpc.BindableService {

    /**
     * <pre>
     * Sends a unique id
     * </pre>
     */
    public void uniqueName(com.noseparte.common.rpc.service.UniqueNameRequest request,
        io.grpc.stub.StreamObserver<com.noseparte.common.rpc.service.UniqueNameResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getUniqueNameMethod(), responseObserver);
    }

    @Override public final io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            getUniqueNameMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                com.noseparte.common.rpc.service.UniqueNameRequest,
                com.noseparte.common.rpc.service.UniqueNameResponse>(
                  this, METHODID_UNIQUE_NAME)))
          .build();
    }
  }

  /**
   */
  public static final class UniqueNameServiceStub extends io.grpc.stub.AbstractStub<UniqueNameServiceStub> {
    private UniqueNameServiceStub(io.grpc.Channel channel) {
      super(channel);
    }

    private UniqueNameServiceStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @Override
    protected UniqueNameServiceStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new UniqueNameServiceStub(channel, callOptions);
    }

    /**
     * <pre>
     * Sends a unique id
     * </pre>
     */
    public void uniqueName(com.noseparte.common.rpc.service.UniqueNameRequest request,
        io.grpc.stub.StreamObserver<com.noseparte.common.rpc.service.UniqueNameResponse> responseObserver) {
      ClientCalls.asyncUnaryCall(
          getChannel().newCall(getUniqueNameMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   */
  public static final class UniqueNameServiceBlockingStub extends io.grpc.stub.AbstractStub<UniqueNameServiceBlockingStub> {
    private UniqueNameServiceBlockingStub(io.grpc.Channel channel) {
      super(channel);
    }

    private UniqueNameServiceBlockingStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @Override
    protected UniqueNameServiceBlockingStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new UniqueNameServiceBlockingStub(channel, callOptions);
    }

    /**
     * <pre>
     * Sends a unique id
     * </pre>
     */
    public com.noseparte.common.rpc.service.UniqueNameResponse uniqueName(com.noseparte.common.rpc.service.UniqueNameRequest request) {
      return blockingUnaryCall(
          getChannel(), getUniqueNameMethod(), getCallOptions(), request);
    }
  }

  /**
   */
  public static final class UniqueNameServiceFutureStub extends io.grpc.stub.AbstractStub<UniqueNameServiceFutureStub> {
    private UniqueNameServiceFutureStub(io.grpc.Channel channel) {
      super(channel);
    }

    private UniqueNameServiceFutureStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @Override
    protected UniqueNameServiceFutureStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new UniqueNameServiceFutureStub(channel, callOptions);
    }

    /**
     * <pre>
     * Sends a unique id
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<com.noseparte.common.rpc.service.UniqueNameResponse> uniqueName(
        com.noseparte.common.rpc.service.UniqueNameRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getUniqueNameMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_UNIQUE_NAME = 0;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final UniqueNameServiceImplBase serviceImpl;
    private final int methodId;

    MethodHandlers(UniqueNameServiceImplBase serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_UNIQUE_NAME:
          serviceImpl.uniqueName((com.noseparte.common.rpc.service.UniqueNameRequest) request,
              (io.grpc.stub.StreamObserver<com.noseparte.common.rpc.service.UniqueNameResponse>) responseObserver);
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

  private static abstract class UniqueNameServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    UniqueNameServiceBaseDescriptorSupplier() {}

    @Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return com.noseparte.common.rpc.service.Service.getDescriptor();
    }

    @Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("UniqueNameService");
    }
  }

  private static final class UniqueNameServiceFileDescriptorSupplier
      extends UniqueNameServiceBaseDescriptorSupplier {
    UniqueNameServiceFileDescriptorSupplier() {}
  }

  private static final class UniqueNameServiceMethodDescriptorSupplier
      extends UniqueNameServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final String methodName;

    UniqueNameServiceMethodDescriptorSupplier(String methodName) {
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
      synchronized (UniqueNameServiceGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new UniqueNameServiceFileDescriptorSupplier())
              .addMethod(getUniqueNameMethod())
              .build();
        }
      }
    }
    return result;
  }
}
