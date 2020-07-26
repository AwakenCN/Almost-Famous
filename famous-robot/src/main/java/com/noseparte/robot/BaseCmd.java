package com.noseparte.robot;


import com.noseparte.common.global.Misc;
import com.noseparte.common.http.KeyValuePair;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
public abstract class BaseCmd implements Serializable {

    public int index;
    public int cmd;
    public long uid;
    public String token;
    public long rid;

    public List<KeyValuePair> toKeyValuePair() {
        List<KeyValuePair> pairs = new ArrayList<>();
        Field[] fields = Misc.getAllField(this);
        try {
            for (Field field : fields) {
//                System.out.println(field.getName() + " = " + field.getGenericType().getTypeName());
                Method m = (Method) getClass().getMethod("get" + Misc.getMethodName(field.getName()));
                KeyValuePair pair = new KeyValuePair(field.getName(), m.invoke(this) + "");
                pairs.add(pair);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return pairs;
    }
}
