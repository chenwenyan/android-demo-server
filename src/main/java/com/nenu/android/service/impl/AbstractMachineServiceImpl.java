package com.nenu.android.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.nenu.android.service.AbstractMachineService;
import com.nenu.android.service.IndexService;
import org.apache.xerces.xs.StringList;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * AbstractMachineServiceImpl
 * Author： wychen
 * Date: 2017/6/15
 * Time: 14:16
 */
@Service("abstractMachineService")
public class AbstractMachineServiceImpl implements AbstractMachineService{

    private static String expression = "";
    private static String initDenv = "";
    //动态环境 map键值对形式存储
    private static Map<String,Integer> DEnv = new HashMap<String,Integer>();
    private static int controlSize = 0 ;
    private static int stackTop = 0;

    private String control[] = new String[100];

    private String stack[] = new String[100];

    public JSONObject start(String expression, String initDenv) throws Exception {
        JSONObject json = new JSONObject();
        DEnv = init_Denv(initDenv);
        control[controlSize++] = expression;
        String initControlField = initControl();
        String initStackField = initStack();
        String initDenvFeild = initDenv();
        json.put("initControlField",initControlField);
        json.put("initStackField",initStackField);
        json.put("initDenvField",initDenvFeild);
        return json;
    }

    public JSONObject next(String controlField,String stackField,String initDenvField) throws Exception {
        JSONObject json = new JSONObject();
        while(controlSize > 0){
            cal(controlSize,control,stack,DEnv,stackTop);
        }
        return json;

    }

    private String initControl(){
        String controlInitial = "[";
        for (int i = controlSize - 1; i >= 0; i--) {
            controlInitial += control[i];
            if (i != 0)
                controlInitial += ", ";
        }
        controlInitial += "]";
        System.out.println("Control: " + controlInitial);
        return controlInitial;
    }

    private String initStack(){
        String stackInitial = "[";
        for (int i = stackTop - 1; i >= 0; i--) {
            stackInitial += stack[i];
            if (i != 0)
                stackInitial += ", ";
        }
        stackInitial += "]";
        System.out.println("Stack   :" + stackInitial);
        return stackInitial;
    }

    private String initDenv(){
        String denvInitial = "[";
        Boolean flaginitial = false;
        for (Map.Entry<String, Integer> x : DEnv.entrySet()) {
            if (flaginitial)
                denvInitial += ", ";
            flaginitial = true;
            denvInitial += x.getKey() + "->" + String.valueOf(x.getValue());
        }
        denvInitial += "]";
        System.out.println("DEnv   :" + denvInitial);
        return denvInitial;
    }



    private JSONObject cal(int controlsize, String control[], String stack[], Map<String, Integer> Denv, int stacktop) {

        //存放结果
        JSONObject jsonObject = new JSONObject();

        while (controlsize > 0) {
            String now = control[controlsize - 1];
            controlsize--;
            String fir = getfirString(now);
            if (fir.equals("ge")) {
                if (now.length() > 2)
                    Devide(now, control, controlsize);
                else
                    gaoGe(stack, stacktop);
            } else if (fir.equals("se")) {
                if (now.length() > 2)
                    Devide(now, control, controlsize);
                else
                    gaoSe(stack, stacktop);
            } else if (fir.equals("add")) {
                if (now.length() > 3)
                    Devide(now, control, controlsize);
                else
                    gaoAdd(stack, stacktop);
            } else if (fir.equals("sub")) {
                if (now.length() > 3)
                    Devide(now, control, controlsize);
                else
                    gaoSub(stack, stacktop);
            } else if (fir.equals("mul")) {
                if (now.length() > 3)
                    Devide(now, control, controlsize);
                else
                    gaoMul(stack, stacktop);
            } else if (fir.equals("div")) {
                if (now.length() > 3)
                    Devide(now, control, controlsize);
                else
                    gaoDiv(stack, stacktop);
            } else if (fir.equals("cons")) {
                System.out.println("常量规则：(vs, const(c):e, delta) => (c:vs, e, delta)");
                stack[stacktop++] = getAll(now);
            } else {
                System.out.println("变量规则：(vs, var(c):e, delta) => (delta(x):vs, e, delta)");
                stack[stacktop++] = String.valueOf(Denv.get(getAll(now)));
            }

            String controlOut = "[";
            for (int i = controlsize - 1; i >= 0; i--) {
                controlOut += control[i];
                if (i != 0)
                    controlOut += ", ";
            }
            controlOut += "]";
            System.out.println("Control: " + controlOut);

            String stackOut = "[";
            for (int i = stacktop - 1; i >= 0; i--) {
                stackOut += stack[i];
                if (i != 0)
                    stackOut += ", ";
            }
            stackOut += "]";
            System.out.println("Stack   :" + stackOut);

            String denvOut = "[";
            Boolean flagOut = false;
            for (Map.Entry<String, Integer> x : Denv.entrySet()) {
                if (flagOut)
                    denvOut += ", ";
                flagOut = true;
                denvOut += x.getKey() + "->" + String.valueOf(x.getValue());
            }
            denvOut += "]";
            System.out.println("DEnv   :" + denvOut);

            jsonObject.put("rule",)
            jsonObject.put("control",controlOut);
        }

        System.out.println("结束 ");
    }


    private String getAll(String now) {
        int len = now.length(), i = 0;
        while (i < len && now.charAt(i) != '(')
            i++;
        i++;
        String ret = "";
        while (i < len && now.charAt(i) != ')') {
            ret += now.charAt(i);
            i++;
        }
        return ret;
    }

    private void gaoDiv(String stack[], int stacktop) {
        System.out.println("除法规则：(n1:n2:vs, div:e, delta) => (n:vs, e, delta), n= n1/n2");
        int a = Integer.valueOf(stack[--stacktop]);
        int b = Integer.valueOf(stack[--stacktop]);
        stack[stacktop++] = String.valueOf(a / b);
    }

    private void gaoMul(String stack[], int stacktop) {
        System.out.println("乘法规则：(n1:n2:vs, mul:e, delta) => (n:vs, e, delta), n= n1*n2");
        int a = Integer.valueOf(stack[--stacktop]);
        int b = Integer.valueOf(stack[--stacktop]);
        stack[stacktop++] = String.valueOf(a * b);
    }

    private void gaoSub(String stack[], int stacktop) {
        System.out.println("减法规则：(n1:n2:vs, sub:e, delta) => (n:vs, e, delta), n= n1-n2");
        int a = Integer.valueOf(stack[--stacktop]);
        int b = Integer.valueOf(stack[--stacktop]);
        stack[stacktop++] = String.valueOf(a - b);
    }

    private void gaoAdd(String stack[], int stacktop) {
        System.out.println("加法规则：(n1:n2:vs, add:e, delta) => (n:vs, e, delta), n= n1+n2");
        int a = Integer.valueOf(stack[--stacktop]);
        int b = Integer.valueOf(stack[--stacktop]);
        stack[stacktop++] = String.valueOf(a + b);
    }

    private void gaoSe(String stack[], int stacktop) {
        System.out.println("比较规则：(n1:n2:vs, se:e, delta) => (n:vs, e, delta), n = (n1<=n2)");
        int a = Integer.valueOf(stack[--stacktop]);
        int b = Integer.valueOf(stack[--stacktop]);
        if (a <= b)
            stack[stacktop++] = "true";
        else
            stack[stacktop++] = "false";
    }

    private void gaoGe(String stack[], int stacktop) {
        System.out.println("比较规则：(n1:n2:vs, ge:e, delta) => (n:vs, e, delta), n = (n1>=n2)");
        int a = Integer.valueOf(stack[--stacktop]);
        int b = Integer.valueOf(stack[--stacktop]);
        if (a >= b)
            stack[stacktop++] = "true";
        else
            stack[stacktop++] = "false";
    }

    private String getfirString(String now) {
        int len = now.length(), i = 0;
        char c = now.charAt(i);
        while (i < len && (c < 'a' || c > 'z')) {
            i++;
            if (i < len)
                c = now.charAt(i);
        }
        String ret = "";
        while (i < len && c >= 'a' && c <= 'z') {
            ret += c;
            i++;
            if (i < len)
                c = now.charAt(i);
        }
        return ret;
    }

    private void Devide(String now, String control[], int controlsize) {
        System.out.println("分解规则:(vs, op(e1,e2):e, delta) => (vs, e2:e1:op:e, delta)");
        control[controlsize++] = getfirString(now);
        control[controlsize++] = getPartone(now);
        control[controlsize++] = getParttwo(now);
    }

    private String getPartone(String now) {
        int len = now.length(), i = 0;
        while (i < len && now.charAt(i) != '(')
            i++;
        i++;
        String ret = "";
        int cnt = 0;
        while (i < len && now.charAt(i) == ' ')
            i++;
        //      System.out.print(i);
        while (i < len && (now.charAt(i) != ',' || cnt != 0)) {
            ret += now.charAt(i);
            if (now.charAt(i) == '(')
                cnt++;
            if (now.charAt(i) == ')')
                cnt--;
            i++;
        }
        return ret;
    }

    private String getParttwo(String now) {
        int len = now.length(), i = 0;
        while (i < len && now.charAt(i) != '(')
            i++;
        i++;
        int cnt = 0;
        while (i < len && now.charAt(i) == ' ')
            i++;
        while (i < len && (now.charAt(i) != ',' || cnt != 0)) {
            if (now.charAt(i) == '(')
                cnt++;
            if (now.charAt(i) == ')')
                cnt--;
            i++;
        }
        i++;
        while (i < len && now.charAt(i) == ' ')
            i++;
        String ret = "";
        while (i < len - 1) {
            ret += now.charAt(i);
            i++;
        }
        return ret;
    }

    private Map<String, Integer> init_Denv(String denv) {
        int len = denv.length();
        for (int i = 0; i < len; i++) {
            char c = denv.charAt(i);
            while (i < len && (c < 'a' || c > 'z')) {
                i++;
                if (i < len)
                    c = denv.charAt(i);
            }
            String letter = "";
            while (i < len && c >= 'a' && c <= 'z') {
                letter += c;
                i++;
                if (i < len)
                    c = denv.charAt(i);
            }
            while (i < len && (c < '0' || c > '9')) {
                i++;
                if (i < len)
                    c = denv.charAt(i);
            }
            int number = 0;
            while (i < len && c >= '0' && c <= '9') {
                number = number * 10 + c - '0';
                i++;
                if (i < len)
                    c = denv.charAt(i);
            }
            DEnv.put(letter, number);
        }
        return DEnv;
    }
}
