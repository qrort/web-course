package ru.itmo.wp.servlet;
import ru.itmo.wp.util.ImageUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Base64;
import java.util.Random;

public class CaptchaFilter extends HttpFilter {
    private void generateForm(HttpServletRequest request, HttpServletResponse response, String targetURI) throws IOException {
        request.getSession().setAttribute("captcha_shown", "true");
        String key = Integer.toString(new Random().nextInt(900) + 100);
        request.getSession().setAttribute("captcha", key);
        byte[] image = ImageUtils.toPng(key);
        response.setContentType("text/html");
        String body[] = {
                "<img src=\"data:image/png;base64, ",
                Base64.getEncoder().encodeToString(image),
                "\"/>",
                "<form method=\"get\" action=\"",
                targetURI,
                "\">\n" +
                        "  <label>Enter captcha</label>\n" +
                        "  <input type=\"text\" name=\"data\">\n" +
                        "</form>"
        };
        OutputStream outputStream = response.getOutputStream();
        for (String s : body) outputStream.write(s.getBytes());
        outputStream.flush();
    }
    @Override
    protected void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        if ("GET".equals(request.getMethod())) {
             if ("true".equals(request.getSession().getAttribute("captcha_shown"))
                    && request.getSession().getAttribute("captcha").equals(request.getParameter("data"))) {
                request.getSession().setAttribute("captcha_is_correct", "true");
            }
            if ("true".equals(request.getSession().getAttribute("captcha_shown"))
            && "true".equals(request.getSession().getAttribute("captcha_is_correct"))) {
                chain.doFilter(request, response);
            } else {
                generateForm(request, response, request.getRequestURI());
            }
        } else {
            chain.doFilter(request, response);
        }
    }
}
