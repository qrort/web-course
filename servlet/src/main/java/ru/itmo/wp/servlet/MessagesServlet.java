package ru.itmo.wp.servlet;

import com.google.gson.Gson;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class MessagesServlet extends HttpServlet {
    private ArrayList<Message> messageList = new ArrayList<>();
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (!"POST".equals(request.getMethod())) {
            response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
        } else {
            response.setContentType("application/json");
            String uri = request.getRequestURI();
            String body = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
            if (uri.endsWith("/message/auth")) {
                HttpSession session = request.getSession();
                if (!"".equals(body)) {
                    String user = body.substring(5);
                    session.setAttribute("user", user);
                }
                String json = new Gson().toJson(request.getSession().getAttribute("user"));
                response.getWriter().print(json);
                response.getWriter().flush();
            }
            if (uri.endsWith("/message/findAll")) {
                String json = new Gson().toJson(messageList);
                response.getWriter().print(json);
                response.getWriter().flush();
            }
            if (uri.endsWith("/message/add")) {
                String text = body.substring(5);
                messageList.add(new Message((String) request.getSession().getAttribute("user"), text));
            }
        }
    }
}
