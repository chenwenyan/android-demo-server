package com.nenu.android.controller;

import com.nenu.android.service.IndexService;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * indexController
 *
 * @author: wychen
 * @time: 2017/5/8 14:05
 */
@Controller
public class indexController {

    @Autowired
    private IndexService indexService;

    @RequestMapping(value = "test",method = RequestMethod.GET)
    @ResponseBody
    public JSONObject test(HttpServletRequest request, HttpServletResponse response,
                           Model model){
        JSONObject jsonObject = new JSONObject();
        String test = request.getParameter("test");
        System.out.println(test);
        try{
            String result = indexService.test(test);
            jsonObject.put("code",1);
            jsonObject.put("msg","success");
            jsonObject.put("data",result);

        }catch (Exception e){
            jsonObject.put("code",0);
            jsonObject.put("msg","failed");
            e.printStackTrace();
        }
        return jsonObject;
    }

    @RequestMapping(value = "pfpGrowth",method = RequestMethod.GET)
    @ResponseBody
    public JSONObject pfpGrowth(HttpServletRequest request, HttpServletResponse response,
                           Model model){
        JSONObject jsonObject = new JSONObject();
        String test = request.getParameter("test") == null ? "null" : request.getParameter("test");
        System.out.println(test);
        try{
            String result = indexService.PFPGrowth(test);
            jsonObject.put("code",1);
            jsonObject.put("msg","success");
            jsonObject.put("param",test);
            jsonObject.put("data",result);

        }catch (Exception e){
            jsonObject.put("code",0);
            jsonObject.put("msg","failed");
            jsonObject.put("param",test);
            e.printStackTrace();
        }
        return jsonObject;
    }
}
