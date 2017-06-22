package com.nenu.android.controller;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.util.JSONPObject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Stack;


/**
 * AbstractMachineController
 * Author： wychen
 * Date: 2017/6/15
 * Time: 9:28
 */
@Controller
public class AbstractMachineController {

    @RequestMapping(value = "/abstractMachine", method = RequestMethod.GET)
    public String getExpress(HttpServletRequest request, HttpServletResponse response) {
        return "abstractMachine";
    }

    @RequestMapping(value = "/start", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject transExpression(HttpServletRequest request, HttpServletResponse response,
                                      Model model) {
        JSONObject json = new JSONObject();
        String expression = request.getParameter("expressionInput");
        if (expression.trim() == "" || expression.trim() == null) {
            json.put("code", 1);
            json.put("data", null);
            json.put("msg", "输入字符为空！");
        } else {

        }
        return json;
    }

    @RequestMapping(value = "/next", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject comExpression(HttpServletRequest request, HttpServletResponse response,
                                                Model model) {
        JSONObject json = new JSONObject();//存放处理结果
        String control = request.getParameter("controlInput");
        String DEnv = request.getParameter("DEnvInput");
        String msg = "";
        String temp = "";
        int key = 0;
        Stack<String> opt = new Stack<String>(); //操作符
        Stack<String> varible = new Stack<String>(); //变量
        Stack<String> consts = new Stack<String>(); //常量
        if(isMatch(control)){
            for(int i = 0; i < control.length(); i++){
//                System.out.println(control.charAt(i));
                char letter = control.charAt(i);
                if(letter == '('){
                   key = i + 1;
                   continue;
                }else{
                    if(i >= 1) {
                        temp = control.substring(key, i - 1);
                    }
                    if(temp == "mul" || temp == "add" || temp == "ge"){
                        opt.push(temp);
                    }else if(temp == "const"){
                       consts.push(temp);
                    }else{
                        varible.push(temp);
                    }
                }

            }
            msg = "控制区初始化正确";
            System.out.println("opt");
            System.out.println(opt);
            System.out.println("varible");
            System.out.println(varible);
            System.out.println("consts");
            System.out.println(consts);
        }else {
            msg = "控制区括号不匹配！";
        }
        json.put("control",opt);
        json.put("stack",varible);
        json.put("DEnv",consts);
        json.put("msg",msg);
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



