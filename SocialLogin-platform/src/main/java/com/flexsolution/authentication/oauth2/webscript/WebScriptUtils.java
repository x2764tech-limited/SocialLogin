package com.flexsolution.authentication.oauth2.webscript;

import org.springframework.extensions.surf.ServletUtil;
import org.springframework.extensions.webscripts.WebScriptRequest;
import org.springframework.extensions.webscripts.servlet.WebScriptServletRuntime;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;


public final class WebScriptUtils {

    public static void setSessionAttribute(WebScriptRequest req, String key, Object value) {

        HttpServletRequest servletRequest = WebScriptServletRuntime.getHttpServletRequest(req);
        HttpSession session = servletRequest.getSession(true);
        session.setAttribute(key, value);
        //ServletUtil.getSession().setAttribute(key, value);
        //req.getRuntime().getSession().setValue(key, value);
    }

    public static Object getSessionAttribute(WebScriptRequest req, String key) {
        HttpSession session = WebScriptServletRuntime.getHttpServletRequest(req).getSession();
        return session == null ? null : session.getAttribute(key);
    }

    public static void removeSessionAttribute(WebScriptRequest req, String key) {
        WebScriptServletRuntime.getHttpServletRequest(req).getSession().removeAttribute(key);
    }
}
