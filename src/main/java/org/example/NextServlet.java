package org.example;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@WebServlet("/world")
public class NextServlet extends HttpServlet {
    ConcurrentHashMap<String, MyRecord> mySession = new ConcurrentHashMap<>();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter writer = resp.getWriter();
        writer.println("Good evening, " + getName(req, resp));
    }

    private String getName(HttpServletRequest request, HttpServletResponse resp) {
        String myid = Arrays.stream(request.getCookies())
            .filter(c -> c.getName().equals("MYID"))
            .map(Cookie::getValue)
            .findAny()
            .orElse(null);

        String name = request.getParameter("name");

        if (myid == null) {
            myid = UUID.randomUUID().toString();
            mySession.put(myid, new MyRecord("name", null));
            resp.addCookie(new Cookie("MYID", myid));
        } else if (mySession.get(myid) == null) {
            Map<String, Object> map = new HashMap<>();
            map.put("name", null);
            MyRecord myRecord = new MyRecord("ss", 1);
            myRecord.key();
            mySession.put(myid, new MyRecord("name", null));
        }

        if (name != null) {
            mySession.replace(myid, new MyRecord("name", name));
            return name;
        } else {
            Object name1 = mySession.get(myid).value();
            if (name1 != null) {
                return name1.toString();
            }
            return "Buddy";
        }
    }
}
