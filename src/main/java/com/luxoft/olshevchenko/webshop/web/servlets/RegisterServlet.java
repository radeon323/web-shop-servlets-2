package com.luxoft.olshevchenko.webshop.web.servlets;

import com.luxoft.olshevchenko.webshop.entity.User;
import com.luxoft.olshevchenko.webshop.service.SecurityService;
import com.luxoft.olshevchenko.webshop.service.UserService;
import com.luxoft.olshevchenko.webshop.web.templater.PageGenerator;
import lombok.SneakyThrows;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.Map;
import java.util.Optional;

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
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
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
                    writeErrorResponse(response, errorMsg);
                }
            } else {
                String errorMsg = "This user is already exist! <a href='/login'> Login page</a>";
                writeErrorResponse(response, errorMsg);
            }
        } catch (IOException e) {
            String errorMsg = "Please fill up all fields!";
            writeErrorResponse(response, errorMsg);
            throw new NullPointerException();
        }
    }

    private User getUserFromRequest(HttpServletRequest request) throws SQLException {

        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String gender = Optional.ofNullable(request.getParameter("gender")).orElse("");
        String firstName = Optional.ofNullable(request.getParameter("firstName")).orElse("");
        String lastName = Optional.ofNullable(request.getParameter("lastName")).orElse("");
        String about = Optional.ofNullable(request.getParameter("about")).orElse("");

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

    @SneakyThrows
    private void writeErrorResponse(HttpServletResponse response, String errorMsg) {
        Map<String, Object> parameters = Map.of("errorMsg", errorMsg);
        String pageError = PageGenerator.getPage("register.html", parameters);
        response.getWriter().write(pageError);
    }


}
