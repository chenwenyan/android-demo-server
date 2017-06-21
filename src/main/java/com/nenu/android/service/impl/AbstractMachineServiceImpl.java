package com.nenu.android.service.impl;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * AbstractMachineServiceImpl
 * Author： wychen
 * Date: 2017/6/15
 * Time: 14:16
 */
@Service("abstractMachineService")
public class AbstractMachineServiceImpl {

    //变量名寄存字段，临时寄存变量名
    private StringBuffer variableRegister = new StringBuffer();

    //数字寄存字段，临时寄存一个数字
    private StringBuffer digitRegister = new StringBuffer();

    public List<String> analylis(String expression) throws Exception {
        List<String> result = new ArrayList<String>();

        for (int i = 0; i < expression.length(); i++) {
            char ch = expression.charAt(i);
            if (isLetter(ch)) {
                variableRegister.append(ch);
                continue;
            }
            if(isDigit(ch)){
                digitRegister.append(ch);
                continue;
            }
            if(isPoint(ch)){
                digitRegister.append(ch);
                continue;
            }
        }

        return result;
    }

    /**
     * 判断是否为英文字符
     *
     * @param ch
     * @return
     */
    private boolean isLetter(char ch) {
        if ((ch >= 65 && ch <= 90) || (ch >= 97 && ch <= 122)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 判断是否为数字
     *
     * @param ch
     * @return
     */
    private boolean isDigit(char ch) {
        if (ch >= 48 && ch <= 57) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 判断是否为小数点
     *
     * @param ch
     * @return
     */
    private boolean isPoint(char ch) {
        if (ch == 46) {
            return true;
        } else {
            return false;
        }
    }

//    private boolean isOperator(char ch){
//        switch (ch){
//            case 51:
//
//        }
//    }
}
