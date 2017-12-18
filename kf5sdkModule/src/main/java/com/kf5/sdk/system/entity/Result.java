package com.kf5.sdk.system.entity;

import com.google.gson.Gson;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * author:chosen
 * date:2017/2/13 16:06
 * email:812219713@qq.com
 */

public final class Result<T> {

    private int error = -1;
    private String message;
    private T data;

    public int getCode() {
        return error;
    }
    public void setCode(int error) {
        this.error = error;
    }
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public static <T> Result<T> fromJson(String json, Class clazz) {
        Gson gson = new Gson();
        return gson.fromJson(json, buildType(Result.class, clazz));
    }

    private static ParameterizedType buildType(final Class raw, final Type... args) {
        return new ParameterizedType() {
            @Override
            public Type[] getActualTypeArguments() {
                return args;
            }

            @Override
            public Type getRawType() {
                return raw;
            }

            @Override
            public Type getOwnerType() {
                return null;
            }
        };
    }

}
