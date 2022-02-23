package com.luxoft.olshevchenko.webshop.web.servlets;

import com.luxoft.olshevchenko.webshop.service.SecurityService;

import javax.servlet.http.*;
import java.io.IOException;

/**
 * @author Oleksandr Shevchenko
 */
public class LogoutServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String userToken = SecurityService.getUserToken(request);
        Cookie cookie = new Cookie("user-token", userToken);
        cookie.setValue(null);
        cookie.setMaxAge(0);

        HttpSession session = request.getSession();
        session.removeAttribute("name");
        //session.invalidate();
        response.addCookie(cookie);
        response.sendRedirect("/products");
    }

}

