package com.nenu.android.service;

import java.util.List;

/**
 * AbstractMachineService
 * Author： wychen
 * Date: 2017/6/15
 * Time: 14:16
 */
public interface AbstractMachineService {

    public List<String> start(String expression) throws Exception;

    public List<String> next(String expression) throws Exception;

}
