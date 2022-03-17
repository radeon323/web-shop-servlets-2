package com.luxoft.olshevchenko.webshop.web.servlets;

import com.luxoft.olshevchenko.webshop.entity.Product;
import com.luxoft.olshevchenko.webshop.entity.User;
import com.luxoft.olshevchenko.webshop.service.SecurityService;
import com.luxoft.olshevchenko.webshop.service.UserService;
import com.luxoft.olshevchenko.webshop.web.templater.PageGenerator;
import lombok.SneakyThrows;

import javax.servlet.http.*;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @author Oleksandr Shevchenko
 */
public class LoginServlet extends HttpServlet {
    private final UserService userService;
    private final List<String> userTokens;

    public LoginServlet(UserService userService, List<String> userTokens) {
        this.userService = userService;
        this.userTokens = userTokens;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String page = PageGenerator.getPage("login.html");
        response.getWriter().println(page);
    }


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        List<Product> cart = new ArrayList<>();

        if(userService.isUserExist(email)) {
            User user = userService.findByEmail(email);

            String md5 = null;
            try {
                md5 = SecurityService.md5(password);
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }

            if(user.getPassword().equals(md5)) {
                String userToken = UUID.randomUUID().toString();

                userTokens.add(userToken);
                String id = String.valueOf(user.getId());
                HttpSession session = request.getSession();
                session.setAttribute("email", email);
                session.setAttribute("userId", id);
                session.setAttribute("cart", cart);
                session.setMaxInactiveInterval(-1);
                Cookie cookie = new Cookie("user-token", userToken);

                cookie.setMaxAge(3600);
                response.addCookie(cookie);

                response.sendRedirect("/products");

            } else {
                String errorMsg = "Please enter correct password. <a href='/login'> Forgot password?</a>";
                writeErrorResponse(response, errorMsg);
            }
        } else {
            String errorMsg = "User not found. Please enter correct email or <a href='/register'>register</a>.";
            writeErrorResponse(response, errorMsg);
        }
    }

    @SneakyThrows
    private void writeErrorResponse(HttpServletResponse response, String errorMsg) {
        Map<String, Object> parameters = Map.of("errorMsg", errorMsg);
        String pageError = PageGenerator.getPage("login.html", parameters);
        response.getWriter().write(pageError);
    }


}
