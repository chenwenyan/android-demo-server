package com.nenu.android.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.nenu.android.service.AbstractMachineService;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * AbstractMachineServiceImpl
 * Author： wychen
 * Date: 2017/6/15
 * Time: 14:16
 */
@Service("abstractMachineService")
public class AbstractMachineServiceImpl implements AbstractMachineService{

    //动态环境 map键值对形式存储
    private static Map<String,Integer> DEnv = new HashMap<String,Integer>();
    //初始化控制区操作符或者常量/变量个数
    private static int controlSize;
    //初始化栈顶元素
    private static int stackTop;
    //存放解析后的操作符、常量、变量
    private String control[] = new String[100];
    //存放中间变量运算结果
    private String stack[] = new String[100];
    //标记使用规则
    private String rule = "";
    private int count = 1;
    LinkedHashMap<Integer,JSONObject> map = new LinkedHashMap<Integer, JSONObject>();

    //存放结果
    List<JSONObject> res = new ArrayList<JSONObject>();

    public JSONObject start(String expression, String initDEnv) throws Exception {

        stack = new String[100];
        controlSize = 0 ;
        stackTop = 0;

        JSONObject json = new JSONObject();
        DEnv = init_DEnv(initDEnv);
        control[controlSize++] = expression;
        String initControlField = initControl();
        String initStackField = initStack();
        String initDEnvFeild = initDEnv();
        json.put("initControlField",initControlField);
        json.put("initStackField",initStackField);
        json.put("initDEnvField",initDEnvFeild);
        res.clear();
        count = 1;
        return json;
    }

    public List<JSONObject> next(String controlField,String stackField,String initDEnvField) throws Exception {
        JSONObject json = new JSONObject();
//        while(controlSize > 0){
//            json.put("res",cal());
//            json = cal();
            cal();
            json.put("data",res);
//        }
        return res;
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

    private String initDEnv(){
        String DEnvInitial = "[";
        Boolean flagInitial = false;
        for (Map.Entry<String, Integer> x : DEnv.entrySet()) {
            if (flagInitial)
                DEnvInitial += ", ";
            flagInitial = true;
            DEnvInitial += x.getKey() + "->" + String.valueOf(x.getValue());
        }
        DEnvInitial += "]";
        System.out.println("DEnv   :" + DEnvInitial);
        return DEnvInitial;
    }



    private  void cal() {

        while (controlSize > 0) {
            JSONObject jsonObject = new JSONObject();
            String now = control[controlSize - 1];
            controlSize--;
            String fir = getfirString(now);
            if (fir.equals("ge")) {
                if (now.length() > 2)
                    Devide(now);
                else
                    gaoGe();
            } else if (fir.equals("se")) {
                if (now.length() > 2)
                    Devide(now);
                else
                    gaoSe();
            } else if (fir.equals("add")) {
                if (now.length() > 3)
                    Devide(now);
                else
                    gaoAdd();
            } else if (fir.equals("sub")) {
                if (now.length() > 3)
                    Devide(now);
                else
                    gaoSub();
            } else if (fir.equals("mul")) {
                if (now.length() > 3)
                    Devide(now);
                else
                    gaoMul();
            } else if (fir.equals("div")) {
                if (now.length() > 3)
                    Devide(now);
                else
                    gaoDiv();
            } else if (fir.equals("cons")) {
                rule = "常量规则：(vs, const(c):e, delta) => (c:vs, e, delta)";
                System.out.println("常量规则：(vs, const(c):e, delta) => (c:vs, e, delta)");
                stack[stackTop++] = getAll(now);
            } else {
                rule = "变量规则：(vs, var(c):e, delta) => (delta(x):vs, e, delta)";
                System.out.println("变量规则：(vs, var(c):e, delta) => (delta(x):vs, e, delta)");
                stack[stackTop++] = String.valueOf(DEnv.get(getAll(now)));
            }

            String controlOut = "[";
            for (int i = controlSize - 1; i >= 0; i--) {
                controlOut += control[i];
                if (i != 0)
                    controlOut += ", ";
            }
            controlOut += "]";
            System.out.println("Control: " + controlOut);

            String stackOut = "[";
            for (int i = stackTop - 1; i >= 0; i--) {
                stackOut += stack[i];
                if (i != 0)
                    stackOut += ", ";
            }
            stackOut += "]";
            System.out.println("Stack   :" + stackOut);

            String DEnvOut = "[";
            Boolean flagOut = false;
            for (Map.Entry<String, Integer> x : DEnv.entrySet()) {
                if (flagOut)
                    DEnvOut += ", ";
                flagOut = true;
                DEnvOut += x.getKey() + "->" + String.valueOf(x.getValue());
            }
            DEnvOut += "]";
            System.out.println("DEnv   :" + DEnvOut);

            jsonObject.put("rule",rule);
            jsonObject.put("control",controlOut);
            jsonObject.put("stack",stackOut);
            jsonObject.put("DEnv",DEnvOut);
            System.out.println(jsonObject);
            map.put(count,jsonObject);
            res.add(jsonObject);
//            res.set(count, jsonObject);
            count++;
        }
        System.out.println("结束 ");
        System.out.println(map);
//        return jsonObject;
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

    private void gaoDiv() {
        rule = "除法规则：(n1:n2:vs, div:e, delta) => (n:vs, e, delta), n= n1/n2";
        System.out.println("除法规则：(n1:n2:vs, div:e, delta) => (n:vs, e, delta), n= n1/n2");
        int a = Integer.valueOf(stack[--stackTop]);
        int b = Integer.valueOf(stack[--stackTop]);
        stack[stackTop++] = String.valueOf(a / b);
    }

    private void gaoMul() {
        rule = "乘法规则：(n1:n2:vs, mul:e, delta) => (n:vs, e, delta), n= n1*n2";
        System.out.println("乘法规则：(n1:n2:vs, mul:e, delta) => (n:vs, e, delta), n= n1*n2");
        int a = Integer.valueOf(stack[--stackTop]);
        int b = Integer.valueOf(stack[--stackTop]);
        stack[stackTop++] = String.valueOf(a * b);
    }

    private void gaoSub() {
        rule = "减法规则：(n1:n2:vs, sub:e, delta) => (n:vs, e, delta), n= n1-n2";
        System.out.println("减法规则：(n1:n2:vs, sub:e, delta) => (n:vs, e, delta), n= n1-n2");
        int a = Integer.valueOf(stack[--stackTop]);
        int b = Integer.valueOf(stack[--stackTop]);
        stack[stackTop++] = String.valueOf(a - b);
    }

    private void gaoAdd() {
        System.out.println("加法规则：(n1:n2:vs, add:e, delta) => (n:vs, e, delta), n= n1+n2");
        int a = Integer.valueOf(stack[--stackTop]);
        int b = Integer.valueOf(stack[--stackTop]);
        stack[stackTop++] = String.valueOf(a + b);
    }

    private void gaoSe() {
        rule = "比较规则：(n1:n2:vs, se:e, delta) => (n:vs, e, delta), n = (n1<=n2)";
        System.out.println("比较规则：(n1:n2:vs, se:e, delta) => (n:vs, e, delta), n = (n1<=n2)");
        int a = Integer.valueOf(stack[--stackTop]);
        int b = Integer.valueOf(stack[--stackTop]);
        if (a <= b)
            stack[stackTop++] = "true";
        else
            stack[stackTop++] = "false";
    }

    private void gaoGe() {
        rule = "比较规则：(n1:n2:vs, ge:e, delta) => (n:vs, e, delta), n = (n1>=n2)";
        System.out.println("比较规则：(n1:n2:vs, ge:e, delta) => (n:vs, e, delta), n = (n1>=n2)");
        int a = Integer.valueOf(stack[--stackTop]);
        int b = Integer.valueOf(stack[--stackTop]);
        if (a >= b)
            stack[stackTop++] = "true";
        else
            stack[stackTop++] = "false";
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
        System.out.println("getFirString:" + ret);
        return ret;
    }

    private void Devide(String now) {
        rule = "分解规则:(vs, op(e1,e2):e, delta) => (vs, e2:e1:op:e, delta)";
        System.out.println("分解规则:(vs, op(e1,e2):e, delta) => (vs, e2:e1:op:e, delta)");
        control[controlSize++] = getfirString(now);
        control[controlSize++] = getPartone(now);
        control[controlSize++] = getParttwo(now);
    }

    private String getPartone(String now) {
        int len = now.length(), i = 0;
        while (i < len && now.charAt(i) != '('){
            i++;
        }
        i++;
        String ret = "";
        int cnt = 0;
        while (i < len && now.charAt(i) == ' '){
            i++;
        }
        while (i < len && (now.charAt(i) != ',' || cnt != 0)) {
            ret += now.charAt(i);
            if (now.charAt(i) == '('){
                cnt++;
            }
            if (now.charAt(i) == ')'){
                cnt--;
            }
            i++;
        }
        System.out.println("getPartOne :" + ret );
        return ret;
    }

    private String getParttwo(String now) {
        int len = now.length(), i = 0;
        while (i < len && now.charAt(i) != '(')
            i++;
        i++;
        int cnt = 0;
        while (i < len && now.charAt(i) == ' '){
            i++;
        }
        while (i < len && (now.charAt(i) != ',' || cnt != 0)) {
            if (now.charAt(i) == '('){
                cnt++;
            }
            if (now.charAt(i) == ')'){
                cnt--;
            }
            i++;
        }
        i++;
        while (i < len && now.charAt(i) == ' '){
            i++;
        }
        String ret = "";
        while (i < len - 1) {
            ret += now.charAt(i);
            i++;
        }
        System.out.println("getPartTwo :" + ret );
        return ret;
    }

    /**
     * 初始化动态环境
     *
     * @param Denv
     * @return
     */
    private Map<String, Integer> init_DEnv(String Denv) {
        int len = Denv.length();
        for (int i = 0; i < len; i++) {
            char c = Denv.charAt(i);
            while (i < len && (c < 'a' || c > 'z')) {
                i++;
                if (i < len)
                    c = Denv.charAt(i);
            }
            String letter = "";
            while (i < len && c >= 'a' && c <= 'z') {
                letter += c;
                i++;
                if (i < len)
                    c = Denv.charAt(i);
            }
            while (i < len && (c < '0' || c > '9')) {
                i++;
                if (i < len)
                    c = Denv.charAt(i);
            }
            int number = 0;
            while (i < len && c >= '0' && c <= '9') {
                number = number * 10 + c - '0';
                i++;
                if (i < len)
                    c = Denv.charAt(i);
            }
            DEnv.put(letter, number);
        }
        return DEnv;
    }
}
