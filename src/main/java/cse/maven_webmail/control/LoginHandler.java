/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cse.maven_webmail.control;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import cse.maven_webmail.model.Pop3Agent;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Map.Entry;
import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionBindingListener;

/**
 *
 * @author jongmin
 */
public class LoginHandler extends HttpServlet {

    /** 
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    private final String ADMINISTRATOR = "admin";
    private static Hashtable loginUsers = new Hashtable();

    
//    //해당 세션에 이미 로그인 되있는지 체크
//    public boolean isLogin(String sessionID)
//    {
//        boolean isLogin = false;
//        Enumeration e = loginUsers.keys();
//        String key = "";
//        while(e.hasMoreElements())
//        {
//            key = (String)e.nextElement();
//            if(sessionID.equals(key))
//            {
//                isLogin = true;
//            }
//        }
//        return isLogin;
//    }
    
    //중복 로그인 막기 위해 아이디 사용중인지 체크
    public boolean isUsing(String userID)
    {
        // Debug 해당 HashTable의 내용 확인  
//        loginUsers.forEach((k, v) -> {
//            System.out.println(k);
//            System.out.println(v);  
//        });
        
        boolean isUsing = false;
        Enumeration e = loginUsers.keys();
        String key = "";
        while(e.hasMoreElements())
        {
            key = (String)e.nextElement();
            if(userID.equals(loginUsers.get(key)))
            {
                isUsing = true;
            }
        }
        return isUsing;
    }
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        HttpSession session = request.getSession();
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        int selected_menu = Integer.parseInt((String) request.getParameter("menu"));


        try {
            switch (selected_menu) {
                case CommandType.LOGIN:
                    String host = (String) request.getSession().getAttribute("host");
                    String userid = request.getParameter("userid");
                    String password = request.getParameter("passwd");

                    // Check the login information is valid using <<model>>Pop3Agent.
                    Pop3Agent pop3Agent = new Pop3Agent(host, userid, password);
                    boolean isLoginSuccess = pop3Agent.validate();
                    if (!isUsing(userid)) {
                        // Now call the correct page according to its validation result.
                        if (isLoginSuccess) {
                            if (isAdmin(userid)) {
                                // HttpSession 객체에 userid를 등록해 둔다.
                                session.setAttribute("userid", userid);
                                response.sendRedirect("admin_menu.jsp");
                            } else {
                                // HttpSession 객체에 userid와 password를 등록해 둔다.
                                session.setAttribute("userid", userid);
                                session.setAttribute("password", password);
                                loginUsers.put(session.getId(), userid);
                                response.sendRedirect("main_menu.jsp");
                            }
                        } else {
                            RequestDispatcher view = request.getRequestDispatcher("login_fail.jsp");
                            view.forward(request, response);
    //                        response.sendRedirect("login_fail.jsp");
                        }
                        break;
                    }
                    else {
                        RequestDispatcher view = request.getRequestDispatcher("login_duplicate.jsp");
                        view.forward(request, response);
                    }
                    
                case CommandType.LOGOUT:
                    out = response.getWriter();
                    loginUsers.remove(session.getId());
                    session.invalidate();
//                    response.sendRedirect(homeDirectory);
                    response.sendRedirect(getServletContext().getInitParameter("HomeDirectory"));
                    break;
                default:
                    break;
            }
        } catch (Exception ex) {
            System.err.println("LoginCheck - LOGIN error : " + ex);
        } finally {
            out.close();
        }
    }

    protected boolean isAdmin(String userid) {
        boolean status = false;

        if (userid.equals(this.ADMINISTRATOR)) {
            status = true;
        }

        return status;
    }
// <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">

    /**
     * Handles the HTTP <code>GET</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try{
            processRequest(request, response);
        } catch(Exception ex){

        }
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (Exception ex) {

        }
    }

    /**
     * Returns a short description of the servlet.
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";

    }// </editor-fold>
}