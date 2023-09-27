package com.lung.game.proto.msg;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.google.protobuf.GeneratedMessageV3;
import com.google.protobuf.Message;

public class MsgIds {
	public static final int SCServerTime = 1001;
	public static final int CSKeepAlive = 1002;
	public static final int SCErrorMsg = 1005;
	public static final int CSLogin = 1201;
	public static final int SCLogin = 1202;

    /**
     * 消息CLASS与消息ID的对应关系<消息class, 消息ID>
     */
	private static final Map<Class<? extends Message>, Integer> classToId = new HashMap<>();
    /**
     * 消息ID与消息CLASS的对应关系<消息ID, 消息class>
     */
    private static final Map<Integer, Class<? extends Message>> idToClass = new HashMap<>();
	
	static {
		//初始化消息CLASS与消息ID的对应关系
		initClassToId();
		//初始化消息ID与消息CLASS的对应关系
		initIdToClass();
	}
	
	/**
	 * 获取消息ID
	 *
	 * @param clazz
	 * @return
	 */
	public static int getIdByClass(Class<? extends Message> clazz) {
		return classToId.get(clazz);
	}
	
	/**
	 * 获取消息CLASS

	 * @param msgId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> T getClassById(int msgId) {
		return (T) idToClass.get(msgId);
	}
	
	/**
	 * 获取消息名称
	 *
	 * @param msgId
	 * @return
	 */
	public static String getNameById(int msgId) {
		try {
			return idToClass.get(msgId).getSimpleName();
		} catch (Exception e) {
			throw new RuntimeException("获取消息名称是发生错误：msgId={0}" + msgId, e);
		}
	}
	
	/**
	 * 初始化消息CLASS与消息ID的对应关系
	 */
	private static void initClassToId() {
		classToId.put(MsgCommon.SCServerTime.class, SCServerTime);
		classToId.put(MsgCommon.CSKeepAlive.class, CSKeepAlive);
		classToId.put(MsgCommon.SCErrorMsg.class, SCErrorMsg);
		classToId.put(MsgPlayer.CSLogin.class, CSLogin);
		classToId.put(MsgPlayer.SCLogin.class, SCLogin);
	}
	
	/**
	 * 初始化消息ID与消息CLASS的对应关系
	 */
	private static void initIdToClass() {
		idToClass.put(SCServerTime,MsgCommon.SCServerTime.class);
		idToClass.put(CSKeepAlive,MsgCommon.CSKeepAlive.class);
		idToClass.put(SCErrorMsg,MsgCommon.SCErrorMsg.class);
		idToClass.put(CSLogin,MsgPlayer.CSLogin.class);
		idToClass.put(SCLogin,MsgPlayer.SCLogin.class);
	}
	/**
	 * 根据消息id解析消息
	 *
	 * @param msgId
	 * @param s
	 */
	public static GeneratedMessageV3 parseFrom(int msgId, byte[] s) throws IOException{
		switch(msgId){
			case SCServerTime:
				return MsgCommon.SCServerTime.parseFrom(s);
			case CSKeepAlive:
				return MsgCommon.CSKeepAlive.parseFrom(s);
			case SCErrorMsg:
				return MsgCommon.SCErrorMsg.parseFrom(s);
			case CSLogin:
				return MsgPlayer.CSLogin.parseFrom(s);
			case SCLogin:
				return MsgPlayer.SCLogin.parseFrom(s);
		}
		return null;
	}
}

