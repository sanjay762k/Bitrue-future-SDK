package com.bitrue.futures.sdk.client.utils;

import com.alibaba.fastjson.JSON;
import com.bitrue.futures.sdk.client.exception.BitrueApiException;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import org.apache.commons.lang.StringUtils;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;

public class UrlParamsBuilder {

    class ParamsMap {

        final Map<String, String> map = new LinkedHashMap<>();
        final Map<String, List> stringListMap = new HashMap<>();

        void put(String name, String value) {

            if (name == null || "".equals(name)) {
                throw new BitrueApiException(BitrueApiException.RUNTIME_ERROR, "[URL] Key can not be null");
            }
            if (value == null || "".equals(value)) {
                return;
            }

            map.put(name, value);
        }

        void put(String name, Integer value) {
            put(name, value != null ? Integer.toString(value) : null);
        }

        void put(String name, Date value, String format) {
            SimpleDateFormat dateFormatter = new SimpleDateFormat(format);
            put(name, value != null ? dateFormatter.format(value) : null);
        }

        void put(String name, Long value) {
            put(name, value != null ? Long.toString(value) : null);
        }

        void put(String name, BigDecimal value) {
            put(name, value != null ? value.toPlainString() : null);
        }

    }

    private static final MediaType JSON_TYPE = MediaType.parse("application/json");
    private final ParamsMap paramsMap = new ParamsMap();
    private final ParamsMap postBodyMap = new ParamsMap();
    private String method = "GET";

    public static UrlParamsBuilder build() {
        return new UrlParamsBuilder();
    }

    private UrlParamsBuilder() {
    }

    public UrlParamsBuilder setMethod(String mode) {
        method = mode;
        return this;
    }

    public Boolean checkMethod(String mode) {
        return mode.equals(method);
    }

    public String getMethod(){
        return method;
    }

    public <T extends Enum> UrlParamsBuilder putToUrl(String name, T value) {
        if (value != null) {
            paramsMap.put(name, value.toString());
        }
        return this;
    }

    public UrlParamsBuilder putToUrl(String name, String value) {
        paramsMap.put(name, value);
        return this;
    }

    public UrlParamsBuilder putToUrl(String name, Date value, String format) {
        paramsMap.put(name, value, format);
        return this;
    }

    public UrlParamsBuilder putToUrl(String name, Integer value) {
        paramsMap.put(name, value);
        return this;
    }

    public UrlParamsBuilder putToUrl(String name, Long value) {
        paramsMap.put(name, value);
        return this;
    }

    public UrlParamsBuilder putToUrl(String name, BigDecimal value) {
        paramsMap.put(name, value);
        return this;
    }

    public UrlParamsBuilder putToPost(String name, String value) {
        postBodyMap.put(name, value);
        return this;
    }

    public <T extends Enum> UrlParamsBuilder putToPost(String name, T value) {
        if (value != null) {
            postBodyMap.put(name, value.toString());
        }
        return this;
    }

    public UrlParamsBuilder putToPost(String name, Date value, String format) {
        postBodyMap.put(name, value, format);
        return this;
    }

    public UrlParamsBuilder putToPost(String name, Integer value) {
        postBodyMap.put(name, value);
        return this;
    }

    public <T> UrlParamsBuilder putToPost(String name, List<String> list) {
        postBodyMap.stringListMap.put(name, list);
        return this;
    }

    public UrlParamsBuilder putToPost(String name, Long value) {
        postBodyMap.put(name, value);
        return this;
    }

    public UrlParamsBuilder putToPost(String name, BigDecimal value) {
        postBodyMap.put(name, value);
        return this;
    }

    public String buildUrl() {
        Map<String, String> map = new LinkedHashMap<>(paramsMap.map);
        StringBuilder head = new StringBuilder("");
        String result = AppendUrl(map, head);
        if(StringUtils.isBlank(result)){
            return "";
        }
        return "?" + result;
    }

    public String buildSignature(String ts, String path) {
        StringBuilder url = new StringBuilder().append(ts).append(method.toUpperCase()).append(path);
        String queryString = buildUrl();  // include "?"!!!
        if(StringUtils.isNotBlank(queryString)){
            url.append(queryString);
        }
        if(method.equals("POST")){
            url.append(JSON.toJSONString(postBodyMap.map));
        }
        return url.toString();
    }

    private String AppendUrl(Map<String, String> map, StringBuilder stringBuilder) {
        boolean first = true;
        for (Map.Entry<String, String> entry : map.entrySet()) {
            if(!first){
                stringBuilder.append("&");
            }
            stringBuilder.append(entry.getKey());
            stringBuilder.append("=");
            stringBuilder.append(urlEncode(entry.getValue()));
            if(first) {
                first = false;
            }
        }
        return stringBuilder.toString();
    }

    public RequestBody buildPostBody() {
        if (postBodyMap.stringListMap.isEmpty()) {
            if (postBodyMap.map.isEmpty()) {
                return RequestBody.create(JSON_TYPE, "");
            } else {
                return RequestBody.create(JSON_TYPE, JSON.toJSONString(postBodyMap.map));
            }
        } else {
            return RequestBody.create(JSON_TYPE, JSON.toJSONString(postBodyMap.stringListMap));

        }
    }

    public boolean hasPostParam() {
        return !postBodyMap.map.isEmpty() || "POST".equals(method);
    }

    public String buildUrlToJsonString() {
        return JSON.toJSONString(paramsMap.map);
    }

    /**
     * 使用标准URL Encode编码。注意和JDK默认的不同，空格被编码为%20而不是+。
     *
     * @param s String字符串
     * @return URL编码后的字符串
     */
    private static String urlEncode(String s) {
        try {
            return URLEncoder.encode(s, "UTF-8").replaceAll("\\+", "%20");
        } catch (UnsupportedEncodingException e) {
            throw new BitrueApiException(BitrueApiException.RUNTIME_ERROR, "[URL] UTF-8 encoding not supported!");
        }
    }
}