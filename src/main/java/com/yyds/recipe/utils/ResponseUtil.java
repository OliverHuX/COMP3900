package com.yyds.recipe.utils;

import java.util.HashMap;
import java.util.Map;

public class ResponseUtil {
    public static Map<String, Object> getResponse() {
        Map<String, Object> rsp = new HashMap<>(2);
        rsp.put("code", 0);
        rsp.put("error message", "success");
        return rsp;
    }
}
