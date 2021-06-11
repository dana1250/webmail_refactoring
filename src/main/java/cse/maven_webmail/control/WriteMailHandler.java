/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cse.maven_webmail.control;


import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import cse.maven_webmail.model.FormParser;
import cse.maven_webmail.model.SmtpAgent;
import cse.maven_webmail.model.loadDBConfig;
//import jdk.internal.jline.internal.Nullable;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

/**
 *
 * @author jongmin
 */
public class WriteMailHandler extends HttpServlet {

    /** 
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        PrintWriter out = null;

        try {
            request.setCharacterEncoding("UTF-8");
            int select = Integer.parseInt((String) request.getParameter("menu"));
            switch (select) {
                case CommandType.SEND_MAIL_COMMAND: // 실제 메일 전송하기
                    out = response.getWriter();
                    boolean status = sendMessage(request);
                    out.println(getMailTransportPopUp(status));
                    break;

                default:
                    out = response.getWriter();
                    out.println("없는 메뉴를 선택하셨습니다. 어떻게 이 곳에 들어오셨나요?");
                    break;
            }
        } catch (Exception ex) {
            out.println(ex.toString());
        } finally {
            out.close();
        }

    }

    private boolean sendMessage(HttpServletRequest request) {
        boolean status = false;
        Connection conn = null;
        PreparedStatement pstmt = null;
        
        // 1. toAddress, ccAddress, subject, body, file1 정보를 파싱하여 추출
        FormParser parser = new FormParser(request);
        parser.parse();

        // 2.  request 객체에서 HttpSession 객체 얻기
        HttpSession session = (HttpSession) request.getSession();

        // 3. HttpSession 객체에서 메일 서버, 메일 사용자 ID 정보 얻기
        String host = (String) session.getAttribute("host");
        String userid = (String) session.getAttribute("userid");
        
        // 202105 KYH - @ 예약메일 여부 확인
        if(parser.getIsReservation()){
            try {
                String className = loadDBConfig.getInstance().getDriver();
                String url = loadDBConfig.getInstance().getUrl();
                String User = loadDBConfig.getInstance().getId();
                String Password = loadDBConfig.getInstance().getPw();
                Class.forName(loadDBConfig.getInstance().getDriver());
                conn = DriverManager.getConnection(url, User, Password);
                String sql = "INSERT INTO reservation_mail(host, user_id, toaddr, ccaddr, subject, body, filename, file, reservation_date) VALUES (?,?,?,?,?,?,?,?,?)";
                StringBuilder date = new StringBuilder();
                pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, host);
                pstmt.setString(2, userid);
                pstmt.setString(3, parser.getToAddress());
                pstmt.setString(4, parser.getCcAddress());
                pstmt.setString(5, parser.getSubject());
                pstmt.setString(6, parser.getBody());
                pstmt.setTimestamp(9, new java.sql.Timestamp(parser.getReservationDate().getTime()));
                // 첨부파일이 존재하지 않을 때
                if (parser.getFileName() == null || (parser.getFileName().equals(""))) {
                    pstmt.setNull(7, java.sql.Types.VARCHAR);
                    pstmt.setNull(8, java.sql.Types.BLOB);
                }
                else {
                    // 첨부파일을 보내기 위한 사전 작업.
                    String fileName = parser.getFileName();
                    System.out.println(parser.getFileName());
                    File file = new File(fileName);
                    InputStream fileStream = null;
                    fileStream = new FileInputStream(file);
                    int fileSize = 0;
                    fileSize = (int) file.length();
                    pstmt.setString(7, parser.getAliasFileName());
                    pstmt.setBinaryStream(8, fileStream, fileSize);
                }
                
                int count = pstmt.executeUpdate();
                if (count >= 1) {
                    StringBuilder Popup = new StringBuilder();
                    Popup.append("<script>alert('예약전송 추가 완료'); location.href='index.jsp';</script>");
                    System.out.println(Popup.toString());
                } else {
                    StringBuilder Popup = new StringBuilder();
                    Popup.append("<script>alert('예약전송 추가 실패- 서버 상태를 확인하세요.'); window.history.back();</script>");
                    System.out.println("******" + Popup.toString());
                }
                pstmt.close();
                conn.close();
                return true;
            } catch (Exception ex) {
                System.out.println("시스템 접속에 실패했습니다.");
                System.out.println(ex.getMessage());
                ex.printStackTrace();
                return false;
            } finally{
                System.out.flush();
            }
        // END IF 예약 전송
        }
        else {
            // 4. SmtpAgent 객체에 메일 관련 정보 설정
            SmtpAgent agent = new SmtpAgent(host, userid);
            agent.setTo(parser.getToAddress());
            agent.setCc(parser.getCcAddress());
            agent.setSubj(parser.getSubject());
            agent.setBody(parser.getBody());
            String fileName = parser.getFileName();
            System.out.println("WriteMailHandler.sendMessage() : fileName = " + fileName);
            if (fileName != null) {
                agent.setFile1(fileName);
            }

            // 5. 메일 전송 권한 위임
            if (agent.sendMessage()) {
                status = true;
            }
            return status;
        }
    }  // sendMessage()

    private String getMailTransportPopUp(boolean success) {
        String alertMessage = null;
        if (success) {
            alertMessage = "메일 전송이 성공했습니다.";
        } else {
            alertMessage = "메일 전송이 실패했습니다.";
        }

        StringBuilder successPopUp = new StringBuilder();
        successPopUp.append("<html>");
        successPopUp.append("<head>");

        successPopUp.append("<title>메일 전송 결과</title>");
        successPopUp.append("<link type=\"text/css\" rel=\"stylesheet\" href=\"css/main_style.css\" />");
        successPopUp.append("</head>");
        successPopUp.append("<body onload=\"goMainMenu()\">");
        successPopUp.append("<script type=\"text/javascript\">");
        successPopUp.append("function goMainMenu() {");
        successPopUp.append("alert(\"");
        successPopUp.append(alertMessage);
        successPopUp.append("\"); ");
        successPopUp.append("window.location = \"main_menu.jsp\"; ");
        successPopUp.append("}  </script>");
        successPopUp.append("</body></html>");
        return successPopUp.toString();
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
        try {
            processRequest(request, response);
        } catch (Exception ex) {

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
