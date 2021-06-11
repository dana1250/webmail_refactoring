/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cse.maven_webmail.storage;

import cse.maven_webmail.model.loadDBConfig;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author lee-yejin
 */
@WebServlet(name = "Storage", urlPatterns = {"/Storage"})
public class Storage extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try ( PrintWriter out = response.getWriter()) {
            
            String className = loadDBConfig.getInstance().getDriver();
            String url = loadDBConfig.getInstance().getUrl();
            String User = loadDBConfig.getInstance().getId();
            String Password = loadDBConfig.getInstance().getPw();
            
            try{
                
                Class.forName(loadDBConfig.getInstance().getDriver());
                Connection conn = DriverManager.getConnection(url, User, Password);
                
                String sql = "INSERT INTO save(user,recv,cc,subj,body,date) VALUES (?,?,?,?,?,?)";
                PreparedStatement pstmt = conn.prepareStatement(sql);
                
                request.setCharacterEncoding("UTF-8");
                             
                if (!(request.getParameter("subj") == null) && !request.getParameter("subj").equals("")) {
                    
                    String user =  request.getParameter("user");
                    String recv = request.getParameter("to");
                    String cc = request.getParameter("cc");
                    String subj = request.getParameter("subj");
                    String body = request.getParameter("body");
                    String date = request.getParameter("date");
                    
                    pstmt.setString(1, user);
                    pstmt.setString(2, recv);
                    pstmt.setString(3, cc);
                    pstmt.setString(4, subj);
                    pstmt.setString(5, body);
                    pstmt.setString(6, date);
                    
                    pstmt.executeUpdate();
                }else{
                    response.setContentType("text/html; charset=UTF-8");
                    out.println("<script>alert('제목을 입력해주세요!'); history.back(-1); </script>");
                    out.flush();
                }
                
                pstmt.close();
                conn.close();
                response.sendRedirect("write_mail.jsp");
                 
            } catch (Exception ex) {
               out.println("");
            } finally{
                out.close();
            }
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (Exception ex) {

        }
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
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
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
