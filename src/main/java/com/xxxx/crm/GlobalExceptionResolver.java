package com.xxxx.crm;


import com.alibaba.fastjson.JSON;
import com.xxxx.crm.base.ResultInfo;
import com.xxxx.crm.exceptions.NoLoginException;
import com.xxxx.crm.exceptions.ParamsException;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;


@Component
public class GlobalExceptionResolver implements HandlerExceptionResolver {

    /**
     * 方法返回值类型
     * 1. 默认返回视图ModelAndViwew
     * 2. 返回JSON
     * @param request
     * @param response
     * @param handler
     * @param ex
     * @return
     */
    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response,
                                         Object handler, Exception ex) {

        if (ex instanceof NoLoginException){
            ModelAndView mav=new ModelAndView();
            mav.setViewName("redirect:/index");
            return mav;
        }

        // 设置默认异常处理
        ModelAndView mav = new ModelAndView();
        mav.addObject("code", 400);
        mav.addObject("msg", "系统异常,请稍后再试!!!!");
        mav.setViewName("error");

        //判断handler是否为HandlerMethod类
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            //获取ResponseBody注解
            ResponseBody responseBody = handlerMethod.getMethod().getDeclaredAnnotation(ResponseBody.class);
            //判断ResponseBody注解是否存在(如果不存在, 则表示返回的是视图, 如果存在, 则表示返回的是JSON数据)
            // handler没有ResponseBody注解, 表示返回的是MAV视图
            if (responseBody == null) {
                if (ex instanceof ParamsException) {
                    ParamsException pex = (ParamsException) ex;
                    mav.addObject("code", pex.getCode());
                    mav.addObject("msg", pex.getMsg());
                }
//                return mav;
            } else {
                //有ResponseBody注解,表示返回的是json格式数据
                ResultInfo resultInfo = new ResultInfo();
                resultInfo.setCode(400);
                resultInfo.setMsg("网络异常(), 请重试~~~~~~");
                //入股捕获的是自定义异常
                if (ex instanceof ParamsException) {
                    ParamsException pex = (ParamsException) ex;
                    resultInfo.setCode(pex.getCode());
                    resultInfo.setMsg(pex.getMsg());
                }
                // 设置响应类型和编码格式 （响应JSON格式）
                response.setContentType("application/json;charset=UTF-8");
                //获取输出流
                PrintWriter out = null;
                try {
                    out = response.getWriter();
                    //将对象转换成JSON格式, 通过输出流输出
                    out.write(JSON.toJSONString(resultInfo));
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (out != null) {
                        out.close();
                    }
                }
                return null;
            }
        }
        return mav;
    }
}
