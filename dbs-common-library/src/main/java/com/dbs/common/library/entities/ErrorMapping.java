package com.dbs.common.library.entities;

import com.dbs.common.base.utils.CommonHelper;

import java.util.*;

@SuppressWarnings("java:S1118")
public class ErrorMapping {

    public static String newMapped(List<String> groupId, String sourceId, String filenameInput, String filenameOutput, String step, String msg, String msgDbg) {
        String time = CommonHelper.convertDateToString("yyyy-MM-dd HH:mm:ss", new Date());
        Map<String,Object> res = new HashMap<>();
        res.put("groupId", groupId);
        res.put("id", UUID.randomUUID().toString());
        res.put("sourceId",sourceId);
        res.put("input",filenameInput);
        res.put("output",filenameOutput);
        res.put("step",step);
        res.put("time", time);
        res.put("msg",msg);
        res.put("msgDbg",msgDbg);
        return CommonHelper.convertToJsonString(res);
    }

}
