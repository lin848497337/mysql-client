package org.client.common;

public class StringUtils {

    public static boolean isEmpty(String str){
        return str == null || str.isEmpty();
    }


    public static boolean isBlank(String str){
        return str == null || str.trim().isEmpty();
    }
}
