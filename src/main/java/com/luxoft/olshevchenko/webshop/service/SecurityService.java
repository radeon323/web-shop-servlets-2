package com.luxoft.olshevchenko.webshop.service;

import com.luxoft.olshevchenko.webshop.web.ServiceLocator;
import com.luxoft.olshevchenko.webshop.web.servlets.LoginServlet;
import org.apache.commons.codec.binary.Hex;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Oleksandr Shevchenko
 */
public class SecurityService {
//    private final List<String> userTokens;

//    public SecurityService(List<String> userTokens) {
//        this.userTokens = userTokens;
//    }



    public boolean isAuth(HttpServletRequest req) {
        Cookie[] cookies = req.getCookies();
        HttpSession session = req.getSession();

        List<String> userTokens = (List<String>) session.getAttribute("userTokens");
        if (userTokens == null) {
            userTokens = Collections.synchronizedList(new ArrayList<>());
        }

        if(cookies !=null) {
            for (Cookie cookie : cookies) {
                if(cookie.getName().equals("user-token")) {
                    if(userTokens.contains(cookie.getValue())) {
                        return true;
                    }
                    break;
                }
            }
        }
        return false;
    }

    public static String md5(String text) throws NoSuchAlgorithmException {
        MessageDigest messageDigest = MessageDigest.getInstance("MD5");

        List<Object> chars = Collections.synchronizedList(new ArrayList<>());
        for (char c : text.toCharArray()) {
            chars.add(c);
        }
        chars.sort(Collections.reverseOrder());

        String txt = text + chars;
        byte[] bytes = messageDigest.digest(txt.getBytes());
        return Hex.encodeHexString(bytes);
    }

    public static String getUserToken(HttpServletRequest req) {
        Cookie[] cookies = req.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("user-token")) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }


}
