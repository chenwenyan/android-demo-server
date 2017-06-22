package com.nenu.android.le;

import java.util.HashMap;
import java.util.Map;

public class LeProcess implements Runnable {

    /**
     * @Description:这个类用于语言Le的计算
     * @author
     * @date 2016/06/20
     */
    private Display disPlay;

    private Thread thread;

    private String control[] = new String[100];

    private String stack[] = new String[100];

    private static String denv;

    private static Map<String, Integer> Denv = new HashMap<String, Integer>();

    private int controlsize, stacktop;

    @SuppressWarnings("static-access")
    public LeProcess(String exp,String denv, Display disPlay)
    {
        controlsize = 0;
        stacktop = 0;
        Denv.clear();

        this.control[controlsize++] = exp;

        System.out.println(control[0]);
        this.denv=denv;
        this.disPlay = disPlay;

        init_Denv();
    }

    public void cal()
    {
        System.out.println("开始 ");
        disPlay.OutputResult("开始 ");
        System.out.println("初始状态:");
        disPlay.OutputResult("初始状态:");

        String controlIntial = "[";
        for (int i = controlsize - 1; i >= 0; i--)
        {
            controlIntial += control[i];
            if (i != 0)
                controlIntial += ", ";
        }
        controlIntial += "]";
        System.out.println("Control: "+controlIntial);
        disPlay.OutputResult("Control: "+controlIntial);

        String stackIntial = "[";
        for (int i = stacktop - 1; i >= 0; i--)
        {
            stackIntial += stack[i];
            if (i != 0)
                stackIntial += ", ";
        }
        stackIntial += "]";
        System.out.println("Stack   :"+stackIntial);
        disPlay.OutputResult("Stack   : "+stackIntial);

        String denvIntial = "[";
        Boolean flaginitial = false;
        for (Map.Entry<String, Integer> x : Denv.entrySet())
        {
            if (flaginitial)
                denvIntial += ", ";
            flaginitial = true;
            denvIntial += x.getKey() + "->" + String.valueOf(x.getValue());
        }
        denvIntial += "]";
        System.out.println("DEnv   :"+denvIntial);
        disPlay.OutputResult("DEnv   :"+denvIntial);
        disPlay.OutputResult(" ");

        while (controlsize > 0)
        {
            String now = control[controlsize - 1];
            controlsize--;
            String fir = getfirString(now);
            if (fir.equals("ge"))
            {
                if (now.length() > 2)
                    Devide(now);
                else
                    gaoGe();
            }
            else if (fir.equals("se"))
            {
                if (now.length() > 2)
                    Devide(now);
                else
                    gaoSe();
            }
            else if (fir.equals("add"))
            {
                if (now.length() > 3)
                    Devide(now);
                else
                    gaoAdd();
            }
            else if (fir.equals("sub"))
            {
                if (now.length() > 3)
                    Devide(now);
                else
                    gaoSub();
            }
            else if (fir.equals("mul"))
            {
                if (now.length() > 3)
                    Devide(now);
                else
                    gaoMul();
            }
            else if (fir.equals("div"))
            {
                if (now.length() > 3)
                    Devide(now);
                else
                    gaoDiv();
            }
            else if (fir.equals("cons"))
            {
                System.out.println("常量规则：(vs, const(c):e, delta) => (c:vs, e, delta)");
                disPlay.OutputResult("常量规则：(vs, const(c):e, delta) => (c:vs, e, delta)");
                stack[stacktop++] = getAll(now);
            }
            else
            {
                System.out.println("变量规则：(vs, var(c):e, delta) => (delta(x):vs, e, delta)");
                disPlay.OutputResult("变量规则：(vs, var(c):e, delta) => (delta(x):vs, e, delta)");
                stack[stacktop++] = String.valueOf(Denv.get(getAll(now)));
            }

            String controlOut = "[";
            for (int i = controlsize - 1; i >= 0; i--)
            {
                controlOut += control[i];
                if (i != 0)
                    controlOut += ", ";
            }
            controlOut += "]";
            System.out.println("Control: "+controlOut);
            disPlay.OutputResult("Control: "+controlOut);

            String stackOut = "[";
            for (int i = stacktop - 1; i >= 0; i--)
            {
                stackOut += stack[i];
                if (i != 0)
                    stackOut += ", ";
            }
            stackOut += "]";
            System.out.println("Stack   :"+stackOut);
            disPlay.OutputResult("Stack   : "+stackOut);

            String denvOut = "[";
            Boolean flagOut = false;
            for (Map.Entry<String, Integer> x : Denv.entrySet())
            {
                if (flagOut)
                    denvOut += ", ";
                flagOut = true;
                denvOut += x.getKey() + "->" + String.valueOf(x.getValue());
            }
            denvOut += "]";
            System.out.println("DEnv   :"+denvOut);
            disPlay.OutputResult("DEnv   :"+denvOut);
            disPlay.OutputResult(" ");

        }

        System.out.println("结束 ");
        disPlay.OutputResult("结束 ");

    }


    /*
     *Operator
     * */
    public void Start()
    {
        thread = new Thread(this);
        thread.start();
    }

    //@Override
    public void run()
    {
        // TODO Auto-generated method stub
        cal();
    }

    private String getAll(String now)
    {
        // TODO Auto-generated method stub
        int len = now.length(), i = 0;
        while (i < len && now.charAt(i) != '(')
            i++;
        i++;
        String ret = "";
        while (i < len && now.charAt(i) != ')')
        {
            ret += now.charAt(i);
            i++;
        }
        return ret;
    }

    private void gaoDiv()
    {
        // TODO Auto-generated method stub
        System.out.println("除法规则：(n1:n2:vs, div:e, delta) => (n:vs, e, delta), n= n1/n2");
        disPlay.OutputResult("除法规则：(n1:n2:vs, div:e, delta) => (n:vs, e, delta), n= n1/n2");
        int a = Integer.valueOf(stack[--stacktop]);
        int b = Integer.valueOf(stack[--stacktop]);
        stack[stacktop++] = String.valueOf(a / b);
    }

    private void gaoMul()
    {
        // TODO Auto-generated method stub
        System.out.println("乘法规则：(n1:n2:vs, mul:e, delta) => (n:vs, e, delta), n= n1*n2");
        disPlay.OutputResult("乘法规则：(n1:n2:vs, mul:e, delta) => (n:vs, e, delta), n= n1*n2");
        int a = Integer.valueOf(stack[--stacktop]);
        int b = Integer.valueOf(stack[--stacktop]);
        stack[stacktop++] = String.valueOf(a * b);
    }

    private void gaoSub()
    {
        // TODO Auto-generated method stub
        System.out.println("减法规则：(n1:n2:vs, sub:e, delta) => (n:vs, e, delta), n= n1-n2");
        disPlay.OutputResult("减法规则：(n1:n2:vs, sub:e, delta) => (n:vs, e, delta), n= n1-n2");
        int a = Integer.valueOf(stack[--stacktop]);
        int b = Integer.valueOf(stack[--stacktop]);
        stack[stacktop++] = String.valueOf(a - b);
    }

    private void gaoAdd()
    {
        // TODO Auto-generated method stub
        System.out.println("加法规则：(n1:n2:vs, add:e, delta) => (n:vs, e, delta), n= n1+n2");
        disPlay.OutputResult("加法规则：(n1:n2:vs, add:e, delta) => (n:vs, e, delta), n= n1+n2");
        int a = Integer.valueOf(stack[--stacktop]);
        int b = Integer.valueOf(stack[--stacktop]);
        stack[stacktop++] = String.valueOf(a + b);
    }

    private void gaoSe()
    {
        // TODO Auto-generated method stub
        System.out.println("比较规则：(n1:n2:vs, se:e, delta) => (n:vs, e, delta), n = (n1<=n2)");
        disPlay.OutputResult("比较规则：(n1:n2:vs, se:e, delta) => (n:vs, e, delta), n = (n1<=n2)");
        int a = Integer.valueOf(stack[--stacktop]);
        int b = Integer.valueOf(stack[--stacktop]);
        if (a <= b)
            stack[stacktop++] = "true";
        else
            stack[stacktop++] = "false";
    }

    private void gaoGe()
    {
        // TODO Auto-generated method stub
        System.out.println("比较规则：(n1:n2:vs, ge:e, delta) => (n:vs, e, delta), n = (n1>=n2)");
        disPlay.OutputResult("比较规则：(n1:n2:vs, ge:e, delta) => (n:vs, e, delta), n = (n1>=n2)");
        int a = Integer.valueOf(stack[--stacktop]);
        int b = Integer.valueOf(stack[--stacktop]);
        if (a >= b)
            stack[stacktop++] = "true";
        else
            stack[stacktop++] = "false";
    }

    private String getfirString(String now)
    {
        // TODO Auto-generated method stub
        int len = now.length(), i = 0;
        char c = now.charAt(i);
        while (i < len && (c < 'a' || c > 'z'))
        {
            i++;
            if (i < len)
                c = now.charAt(i);
        }
        String ret = "";
        while (i < len && c >= 'a' && c <= 'z')
        {
            ret += c;
            i++;
            if (i < len)
                c = now.charAt(i);
        }
        return ret;
    }

    private void Devide(String now)
    {
        // TODO Auto-generated method stub
        System.out.println("分解规则:(vs, op(e1,e2):e, delta) => (vs, e2:e1:op:e, delta)");
        disPlay.OutputResult("分解规则:(vs, op(e1,e2):e, delta) => (vs, e2:e1:op:e, delta)");
        control[controlsize++] = getfirString(now);
        control[controlsize++] = getPartone(now);
        control[controlsize++] = getParttwo(now);
    }

    private String getPartone(String now)
    {
        // TODO Auto-generated method stub
        int len = now.length(), i = 0;
        while (i < len && now.charAt(i) != '(')
            i++;
        i++;
        String ret = "";
        int cnt = 0;
        while (i < len && now.charAt(i) == ' ')
            i++;
        //      System.out.print(i);
        while (i < len && (now.charAt(i) != ',' || cnt != 0))
        {
            ret += now.charAt(i);
            if (now.charAt(i) == '(')
                cnt++;
            if (now.charAt(i) == ')')
                cnt--;
            i++;
        }
        //      System.out.println(" Debug partone: " + ret);
        return ret;
    }

    private String getParttwo(String now)
    {
        // TODO Auto-generated method stub
        int len = now.length(), i = 0;
        while (i < len && now.charAt(i) != '(')
            i++;
        i++;
        int cnt = 0;
        while (i < len && now.charAt(i) == ' ')
            i++;
        //      System.out.print(i);
        while (i < len && (now.charAt(i) != ',' || cnt != 0))
        {
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
        while (i < len - 1)
        {
            ret += now.charAt(i);
            i++;
        }
        return ret;
    }

    private static void init_Denv()
    {
        // TODO Auto-generated method stub
        int len = denv.length();


        for (int i = 0; i < len; i++)
        {
            char c = denv.charAt(i);
            while (i < len && (c < 'a' || c > 'z'))
            {
                i++;
                if (i < len)
                    c = denv.charAt(i);
            }
            String letter = "";
            while (i < len && c >= 'a' && c <= 'z')
            {
                letter += c;
                i++;
                if (i < len)
                    c = denv.charAt(i);
            }
            while (i < len && (c < '0' || c > '9'))
            {
                i++;
                if (i < len)
                    c = denv.charAt(i);
            }
            int number = 0;
            while (i < len && c >= '0' && c <= '9')
            {
                number = number * 10 + c - '0';
                i++;
                if (i < len)
                    c = denv.charAt(i);
            }
            //          System.out.println(tmp + num);
            Denv.put(letter, number);
        }
    }
//    仅仅用于测试用
//    public static void main(String[] args)
//    {
//    }

}
