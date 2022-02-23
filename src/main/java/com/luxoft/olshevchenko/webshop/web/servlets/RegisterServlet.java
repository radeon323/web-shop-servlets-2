package com.luxoft.olshevchenko.webshop.web.servlets;

import com.luxoft.olshevchenko.webshop.entity.User;
import com.luxoft.olshevchenko.webshop.service.SecurityService;
import com.luxoft.olshevchenko.webshop.service.UserService;
import com.luxoft.olshevchenko.webshop.web.templater.PageGenerator;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.Map;

/**
 * @author Oleksandr Shevchenko
 */
public class RegisterServlet extends HttpServlet {
    private final UserService userService;

    public RegisterServlet(UserService userService) {
        this.userService = userService;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String page = PageGenerator.getPage("register.html");
        response.getWriter().println(page);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            String email = request.getParameter("email");
            String password = request.getParameter("password");

            if (!userService.isUserExist(email)) {

                if (email != null && email.length() > 0 && password != null) {
                    try {
                        User user = getUserFromRequest(request);
                        userService.add(user);
                    } catch (SQLException e) {
                        e.printStackTrace();
                        throw new NullPointerException();
                    }

                    String msgSuccess = "User <i>" + email + "</i> was successfully registered!";
                    Map<String, Object> parameters = Map.of("msgSuccess", msgSuccess);
                    String page = PageGenerator.getPage("register.html", parameters);
                    response.getWriter().write(page);

                } else {
                    String errorMsg = "Please fill up all fields!";
                    Map<String, Object> parameters = Map.of("errorMsg", errorMsg);
                    String pageError = PageGenerator.getPage("register.html", parameters);
                    response.getWriter().write(pageError);
                }

            } else {
                String errorMsg = "This user is already exist! <a href='/login'> Login page</a>";
                Map<String, Object> parameters = Map.of("errorMsg", errorMsg);
                String pageError = PageGenerator.getPage("register.html", parameters);
                response.getWriter().write(pageError);
            }


        } catch (IOException e) {
//                throw new NullPointerException();
            String errorMsg = "Please fill up all fields!";
            Map<String, Object> parameters = Map.of("errorMsg", errorMsg);
            String pageError = PageGenerator.getPage("register.html", parameters);
            response.getWriter().write(pageError);
        }

    }

    private User getUserFromRequest(HttpServletRequest request) throws SQLException {

        String email = request.getParameter("email");
        String password = request.getParameter("password");

        String gender = nullOff("gender", request);
        String firstName = nullOff("firstName", request);
        String lastName = nullOff("lastName", request);
        String about = nullOff("about", request);
        int age = 0;
        try {
            age = Integer.parseInt(request.getParameter("age"));
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        String md5 = null;
        try {
            md5 = SecurityService.md5(password);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return User.builder().
                email(email)
                .password(md5)
                .gender(gender)
                .firstName(firstName)
                .lastName(lastName)
                .about(about)
                .age(age)
                .build();
    }

    private String nullOff(String txt, HttpServletRequest request) {
        String text = null;
        if(request.getParameter(txt) == null) {
            text = "";
        } else {
            text = request.getParameter(txt);
        }
        return text;
    }


}
