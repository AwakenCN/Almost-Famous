package com.noseparte.game.bean.proto.msg;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.google.protobuf.GeneratedMessageV3;
import com.google.protobuf.Message;

public class MsgIds {

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
	}
	
	/**
	 * 初始化消息ID与消息CLASS的对应关系
	 */
	private static void initIdToClass() {
	}
	/**
	 * 根据消息id解析消息
	 *
	 * @param msgId
	 * @param s
	 */
	public static GeneratedMessageV3 parseFrom(int msgId, byte[] s) throws IOException{
		switch(msgId){
		}
		return null;
	}
}

