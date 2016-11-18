package com.kf5.sdk.system.utils;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;


/**
 * author:chosen
 * date:2016/10/18 17:45
 * email:812219713@qq.com
 */

public class SafeJson {


    public static JSONObject parseObj(String jsonString){
        return JSONObject.parseObject(jsonString);
    }

    public static String safeGet(JSONObject object, String field) {

        if (object != null && object.containsKey(field)) {
            try {
                return object.getString(field);
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return null;
    }

    public static JSONObject safeObject(JSONObject object, String fledsString) {

        if (object != null && object.containsKey(fledsString)) {
            try {
                return object.getJSONObject(fledsString);
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return null;
    }

    public static JSONArray safeArray(JSONObject object, String field) {
        if (object != null && object.containsKey(field)) {
            try {
                return object.getJSONArray(field);
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return null;

    }

    public static Float safeFloat(JSONObject object, String field) {
        if (object != null && object.containsKey(field)) {
            try {
                return Float.parseFloat(object.getString(field));
            } catch (NumberFormatException e) {
                return 0F;
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return 0F;
    }

    public static Integer safeInt(JSONObject object, String field) {
        if (object != null && object.containsKey(field)) {
            try {
                return Integer.parseInt(object.getString(field));
            } catch (NumberFormatException e) {
                return -100;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return -100;
    }

    public static Long safeLong(JSONObject object, String field) {
        if (object != null && object.containsKey(field)) {
            try {
                return Long.parseLong(object.getString(field));
            } catch (NumberFormatException e) {
                return 0L;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return 0L;
    }

    public static Boolean safeBoolean(JSONObject object, String field) {
        if (object != null && object.containsKey(field)) {
            try {
                return Boolean.parseBoolean(object.getString(field));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return Boolean.FALSE;
    }

}

