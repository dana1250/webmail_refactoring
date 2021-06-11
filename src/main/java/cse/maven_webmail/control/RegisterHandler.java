package cse.maven_webmail.control;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import cse.maven_webmail.model.UserAdminAgent;
import java.io.UnsupportedEncodingException;
import static java.lang.System.out;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class RegisterHandler extends HttpServlet {

    static Connection conn = null;
    static PreparedStatement pstmt = null;

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
            HttpSession session = request.getSession();
            String userid = (String) session.getAttribute("userid");

            request.setCharacterEncoding("UTF-8");
            int select = Integer.parseInt((String) request.getParameter("menu"));

            if (select == CommandType.ADD_USER_COMMAND) {
                adduser(request, response, out);
            }
        } catch (Exception ex) {
            System.err.println(ex.toString());
        }
    }

    private void adduser(HttpServletRequest request, HttpServletResponse response, PrintWriter out) {
        try {
            String className = "com.mysql.cj.jdbc.Driver";
            Class.forName(className);
            String url = "jdbc:mysql://113.198.235.233:20002/webmail?serverTimezone=Asia/Seoul";
            String User = "team7";
            String Password = "1234";
            String id = request.getParameter("username");
            String password = request.getParameter("password");
            String password2 = request.getParameter("password2");
            conn = DriverManager.getConnection(url, User, Password);
            String sql = "INSERT INTO users VALUES(?,?)";
            boolean isMatched = password.equals(password2);
            boolean isMatchedPWDrange = password.matches("^[a-zA-Z0-9]{8,10}$");

            if(!isMatched){
                StringBuilder Popup = new StringBuilder();
                Popup.append("<script>alert('비밀번호가 일치하지 않습니다!'); location.href='register.jsp';</script>");
                out.println(Popup.toString());
            } else if(!isMatchedPWDrange){
                StringBuilder Popup = new StringBuilder();
                Popup.append("<script>alert('비밀번호는 영어 대소문자와 숫자로 입력하세요!'); location.href='register.jsp';</script>");
                out.println(Popup.toString());
            } else {
                StringBuilder Popup = new StringBuilder();
                Popup.append("<script>alert('OK'); location.href='register.jsp';</script>");
                out.println(Popup.toString());
            }
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, id);
            pstmt.setString(2, password);

            int count = pstmt.executeUpdate();

            if (count >= 1) {
                StringBuilder Popup = new StringBuilder();
                Popup.append("<script>alert('회원가입 성공!'); location.href='index.jsp';</script>");
                out.println(Popup.toString());
            } else {
                StringBuilder Popup = new StringBuilder();
                Popup.append("<script>alert('회원가입 실패 - 서버 상태를 확인하세요.'); window.history.back();</script>");
                out.println(Popup.toString());
            }
            pstmt.close();
            conn.close();
            out.flush();
        } catch (Exception ex) {
            out.println("시스템 접속에 실패했습니다.");
            out.println(ex.getMessage());
            ex.printStackTrace();
        }
    }
}
