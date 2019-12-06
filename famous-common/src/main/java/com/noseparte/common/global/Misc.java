package com.noseparte.common.global;

import LockstepProto.NetMessage;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.handler.codec.CorruptedFrameException;
import org.apache.commons.lang.StringUtils;

import java.lang.reflect.Field;
import java.nio.charset.Charset;
import java.security.SecureRandom;
import java.util.List;
import java.util.Map;

public class Misc {

    public static final Charset CHARSET = Charset.forName("utf-8");

    public static final int MAX_WEIGHT = 10000;

    public static SecureRandom random = new SecureRandom();

    public static int random(int min, int max) {
        if (min == max) {
            return min;
        }
        if (min > max) {
            return max;
        }
        return min + random.nextInt(max - min);
    }

    public static String generatorName() {
        char[] first = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
        String[] middle = {"aa", "bb", "cc", "dd", "ee", "ff", "gg", "hh", "ii", "jj", "kk", "ll", "mm", "nn", "oo", "pp", "qq", "rr", "ss", "tt", "uu", "vv", "ww", "xx", "yy", "zz", "00", "11", "22", "33", "44", "55", "66", "77", "88", "99"};
        String[] last = {"a1", "b2", "c3", "d4", "e5", "f6", "g7", "h8", "i9", "j0"};

        int fidx = random(0, first.length);
        char f = first[fidx];
        int midx = random(0, middle.length);
        String m = middle[midx];
        int lidx = random(0, last.length);
        String l = last[lidx];

        return f + m + l + "";
    }

    public static <K, V> Map<K, V> parseToMap(String json,
                                              Class<K> keyType,
                                              Class<V> valueType) {
        return JSON.parseObject(json,
                new TypeReference<Map<K, V>>(keyType, valueType) {
                });
    }

    public static <T> List<T> parseToList(String json, Class<T> valueType) {
        return JSON.parseObject(json,
                new TypeReference<List<T>>(valueType) {
                });
    }

    /**
     * 去掉配置文件读取出来的字符串双引号
     */
    public static String cutQuotes(String str) {
        return StringUtils.replace(str, "\"", "");
    }

    public static String[] split(String str, String separatorChars) {
        return StringUtils.split(cutQuotes(str), separatorChars);
    }

    /**
     * long转byte数组，大端模式
     */
    public static byte[] toBytes(long num) {
        byte b[] = new byte[8];
        b[0] = (byte) (0xff & (num >> 56));
        b[1] = (byte) (0xff & (num >> 48));
        b[2] = (byte) (0xff & (num >> 40));
        b[3] = (byte) (0xff & (num >> 32));
        b[4] = (byte) (0xff & (num >> 24));
        b[5] = (byte) (0xff & (num >> 16));
        b[6] = (byte) (0xff & (num >> 8));
        b[7] = (byte) (0xff & num);
        return b;
    }

    public static byte[] toBytes(int num) {
        byte[] result = new byte[4];
        result[0] = (byte) ((num >>> 24) & 0xff);
        result[1] = (byte) ((num >>> 16) & 0xff);
        result[2] = (byte) ((num >>> 8) & 0xff);
        result[3] = (byte) ((num >>> 0) & 0xff);
        return result;
    }

    public static int toInt(ByteBuf in) {
        if (in.readableBytes() < 4) {
            throw new CorruptedFrameException("to int.");
        }
        return ((in.readByte() & 0xff) << 24) | ((in.readByte() & 0xff) << 16) | ((in.readByte() & 0xff) << 8)
                | ((in.readByte() & 0xff) << 0);
    }

    public static int toInt(byte[] bytes) {
        return ((bytes[0] & 0xff) << 24) | ((bytes[1] & 0xff) << 16) | ((bytes[2] & 0xff) << 8)
                | ((bytes[3] & 0xff) << 0);
    }

    public static long toLong(ByteBuf in) {
        if (in.readableBytes() < 8) {
            throw new CorruptedFrameException("to long.");
        }
        return ((in.readByte() & 0xff) << 56) | ((in.readByte() & 0xff) << 48) | ((in.readByte() & 0xff) << 40)
                | ((in.readByte() & 0xff) << 32) | ((in.readByte() & 0xff) << 24) | ((in.readByte() & 0xff) << 16)
                | ((in.readByte() & 0xff) << 8) | ((in.readByte() & 0xff) << 0);
    }

    public static long toLong(byte[] bytes) {
        return ((bytes[0] & 0xff) << 56) | ((bytes[1] & 0xff) << 48) | ((bytes[2] & 0xff) << 40)
                | ((bytes[3] & 0xff) << 32) | ((bytes[4] & 0xff) << 24) | ((bytes[5] & 0xff) << 16)
                | ((bytes[6] & 0xff) << 8) | ((bytes[7] & 0xff) << 0);
    }

    public static Integer increase(int original, int increment) {
        if (increment <= 0) {
            return original;
        }
        return original += increment;
    }

    /**
     * 比较两个list元素是否一致
     *
     * @return true=一致, false=不一致
     */
    public static boolean isListEqual(List l0, List l1) {
        if (l0 == l1)
            return true;
        if (l0 == null && l1 == null)
            return true;
        if (l0 == null || l1 == null)
            return false;
        if (l0.size() != l1.size())
            return false;
        for (Object o : l0) {
            if (!l1.contains(o))
                return false;
        }
        for (Object o : l1) {
            if (!l0.contains(o))
                return false;
        }
        return true;
    }

    public static Field[] getField(Object object) {
        Class clazz = object.getClass();
        Field[] fields = clazz.getDeclaredFields();
        return fields;
    }

    public static Field[] getAllField(Object object) {
        Class clazz = object.getClass();
        Field[] fields = clazz.getFields();
        return fields;
    }

    //第一个字母大写
    public static String getMethodName(String fildeName) throws Exception {
        byte[] items = fildeName.getBytes();
        items[0] = (byte) ((char) items[0] - 'a' + 'A');
        return new String(items);
    }

    // 获取常量表中的某一项 (String类型)
    public static String getStringVariable(int globalId) {
        return ConfigManager.globalVariableConfMap.get(globalId).getValue();
    }

    // 获取常量表中的某一项 (Integer类型)
    public static Integer getIntegerVariable(int globalId) {
        String variable = ConfigManager.globalVariableConfMap.get(globalId).getValue();
        return Integer.parseInt(variable);
    }

    public static int getSid(Channel channel) {
        return channel.id().asShortText().hashCode();
    }

    public static boolean isCheckLoginProtocol(int type) {
        if (type == NetMessage.C2S_HeartBeat_VALUE
                || type == NetMessage.C2S_Reconnect_VALUE
                || type == NetMessage.C2S_Match_VALUE
        ) {
            return false;
        }
        return true;
    }

}
