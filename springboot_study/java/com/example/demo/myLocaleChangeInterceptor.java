package com.example.demo;

import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class myLocaleChangeInterceptor extends LocaleChangeInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws ServletException {
        if(hascookielocale(request,getParamName()))
            return true;
        return super.preHandle(request, response, handler);
    }
    private boolean hascookielocale(HttpServletRequest request, String par_localename){
        Cookie[] cookies = request.getCookies();
        for(Cookie c:cookies)
            if(c.getValue()==par_localename)
                return true;
        return false;
    }
}
