package com.nowcoder.toutiao.util;


import org.json.JSONException;
import com.alibaba.fastjson.JSONObject;
import org.omg.CORBA.INTERNAL;


import java.security.MessageDigest;
import java.util.*;

public class Toutiao {

    public static String getJSONString(int code) {
        JSONObject json = new JSONObject();
        json.put("code",code);
        return json.toJSONString();
    }

    public static String getJSONString(int code,String msg){
        JSONObject json = new JSONObject();
        json.put("code",code);
        json.put("msg",msg);
        return json.toJSONString();
    }

    public static String getJSONString(int code,Map<String,Object> map){
        JSONObject json = new JSONObject();
        json.put("code",code);
        for(Map.Entry<String,Object> entry : map.entrySet()){
            json.put(entry.getKey(),entry.getValue());
        }
        return json.toJSONString();
    }


    public static String MD5(String key) {
        char hexDigits[] = {
                '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'
        };
        try {
            byte[] btInput = key.getBytes();
            // 获得MD5摘要算法的 MessageDigest 对象
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            // 使用指定的字节更新摘要
            mdInst.update(btInput);
            // 获得密文
            byte[] md = mdInst.digest();
            // 把密文转换成十六进制的字符串形式
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
            //logger.error("生成MD5失败", e);
            return null;
        }
    }

    public static String IMAGE_DIR = "/Users/hyt/toutiaoImage/";
    public static String Toutiao_Domain = "http://127.0.0.1:8080/";
    public static String[] IMAGE_FILE_EXT = new String[]{"png","jpg","bmp","jpeg"};
    public static boolean isfileAllowed(String fileExt){
        for(int i=0;i<IMAGE_FILE_EXT.length;i++){
            if(fileExt.equals(IMAGE_FILE_EXT[i])){
                return true;
            }
        }
        return false;
    }

    public int FindGreatestSumOfSubArray(int[] array){
        int res = array[0];
        int max = array[0];
        for(int i=1; i<array.length; i++){
            max = Math.max(max+array[i],array[i]);
            res = Math.max(res,max);
        }
        return res;
    }

    public String PrintMinNumber(int [] numbers){
        if(numbers == null ||numbers.length == 0){
            return "";
        }
        StringBuffer sb = new StringBuffer();
        ArrayList<String> arrayList = new ArrayList<String>();
        for(int i = 0; i<numbers.length;i++){
            arrayList.add(String.valueOf(numbers[i]));
        }
        arrayList.sort(new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                String str1 = o1+o2;
                String str2 = o2+o1;
                return str1.compareTo(str2);
            }
        });
        for(int i = 0; i<arrayList.size();i++){
            sb.append(arrayList.get(i));
        }
        return sb.toString();
    }
}
