package com.nenu.android.service.impl;

import com.nenu.android.service.IndexService;
import org.springframework.stereotype.Service;

/**
 * IndexService
 *
 * @author: wychen
 * @time: 2017/5/9 16:16
 */
@Service("indexService")
public class IndexServiceImpl implements IndexService {

    public String test(String test)throws Exception{
        return test;
    }
}
