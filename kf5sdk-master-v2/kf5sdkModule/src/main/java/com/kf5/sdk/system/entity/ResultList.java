package com.kf5.sdk.system.entity;

import com.google.gson.Gson;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

/**
 * author:chosen
 * date:2017/3/6 12:24
 * email:812219713@qq.com
 */

public class ResultList<T> {

    private int error;
    private String message;
    private List<T> data;

    public int getError() {
        return error;
    }

    public void setError(int error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

    public static ResultList fromJson(String json, Class clazz) {
        Gson gson = new Gson();
        return gson.fromJson(json, buildType(ResultList.class, clazz));
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
