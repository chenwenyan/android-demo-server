package com.nenu.android.controller;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.util.JSONPObject;
import com.nenu.android.service.AbstractMachineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;


/**
 * AbstractMachineController
 * Author： wychen
 * Date: 2017/6/15
 * Time: 9:28
 */
@Controller
public class AbstractMachineController {

    @Autowired
    private AbstractMachineService abstractMachineService;

    private List<JSONObject> result = new ArrayList<JSONObject>();

    @RequestMapping(value = "/start", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject start(HttpServletRequest request, HttpServletResponse response,
                                      Model model) {
        JSONObject json = new JSONObject();
        JSONObject jsonObject = new JSONObject();
        int code = 0;
        String msg = "";
        String expression = request.getParameter("expressionInput");
        String initDenv = request.getParameter("initDenvInput");
        if (expression.trim() == "" || expression.trim() == null) {
            msg = "输入字符为空!";
        } else try {
            jsonObject = abstractMachineService.start(expression, initDenv);
            code = 1;
            msg = "初始化成功！";
        } catch (Exception e) {
            msg = "解析出错！";
        }
        json.put("code", code);
        json.put("data", jsonObject);
        json.put("msg", msg);
        return json;
    }

    @RequestMapping(value = "/next", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject next(HttpServletRequest request, HttpServletResponse response,
                                                Model model) {
        JSONObject json = new JSONObject();//存放返回到页面的结果
        JSONObject jsonObject = new JSONObject();//解析结果
        String control = request.getParameter("control");
        String stack = request.getParameter("stack");
        String DEnv = request.getParameter("DEnv");
        int num = Integer.parseInt(request.getParameter("num"));
        String msg = "";
        int code = 1;
        if(isMatch(control)){
            try{
                if(num == 0){
                    result = abstractMachineService.next(control,stack,DEnv);
                }
                if(num == result.size()){
                    jsonObject = null;
                }else{
                    jsonObject = result.get(num);
                }
                msg = "解析正确！";
            }catch (Exception e){
                code = 0;
                msg = "解析出错了！";
            }
        }else {
            msg = "控制区括号不匹配！";
        }
        json.put("code",code);
        json.put("data",jsonObject);
        json.put("msg",msg);
        json.put("steps",result.size());
        return json;
    }

    private boolean isMatch(String s) {
        Stack<Character> sk = new Stack<Character>();
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == '(') {
                sk.push('(');
            }
            if (s.charAt(i) == ')') {
                if (!sk.isEmpty() && sk.pop() == '(')
                    continue;
                else
                    return false;
            }
            if (s.charAt(i) == '{') {
                sk.push('{');
            }
            if (s.charAt(i) == '}') {
                if (!sk.isEmpty() && sk.pop() == '{')
                    continue;
                else
                    return false;
            }
        }
        if (sk.isEmpty())
            return true;
        else
            return false;
    }

}



