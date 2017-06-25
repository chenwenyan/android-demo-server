package com.nenu.android.service;

import com.alibaba.fastjson.JSONObject;

import java.util.List;


/**
 * AbstractMachineService
 * Author： wychen
 * Date: 2017/6/15
 * Time: 14:16
 */
public interface AbstractMachineService {

    public JSONObject start(String expression, String initDenv) throws Exception;

    public List<JSONObject> next(String control, String stack, String initDenv) throws Exception;

}
