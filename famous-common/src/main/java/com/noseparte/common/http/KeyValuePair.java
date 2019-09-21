package com.noseparte.common.http;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.apache.http.NameValuePair;

@Data
@AllArgsConstructor
public class KeyValuePair implements NameValuePair {
    public String name;
    public String value;
}
