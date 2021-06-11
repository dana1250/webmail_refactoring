<%-- 
    Document   : storage_list
    Author     : lee-yejin
--%>

<%@page import="cse.maven_webmail.model.loadDBConfig"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="java.sql.Statement"%>
<%@page import="java.sql.DriverManager"%>
<%@page import="java.sql.Connection"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>임시저장 메일 목록</title>
        <link type="text/css" rel="stylesheet" href="css/main_style.css" />
    </head>
    <jsp:include page="header.jsp" />
                
         <%
            String className = loadDBConfig.getInstance().getDriver();
            String url = loadDBConfig.getInstance().getUrl();
            String User = loadDBConfig.getInstance().getId();
            String Password = loadDBConfig.getInstance().getPw();
            try{
           
            Class.forName(loadDBConfig.getInstance().getDriver());
            Connection conn = DriverManager.getConnection(url, User, Password);
                     
            Statement stmt = conn.createStatement();
            String sql = "SELECT * FROM save where user ='" + session.getAttribute("userid") + "'" ;
            ResultSet rs = stmt.executeQuery(sql);
            %>
            
            <center>
            <table border="1">
                <thead>
                    <tr>
                        <th scope="col">수신</th>
                        <th scope="col">제목</th>
                    </tr>
                </thead>
                
               <tbody>
                    <%
                        while(rs.next()){
                     %>
                        
                        <tr>
                        <td><%=rs.getString("recv") %> </td> 
                        
                        <td> <a href='write_mail.jsp?recv=<%=rs.getString("recv")%>
                        &cc=<%=rs.getString("cc")%>
                        &subj=<%=rs.getString("subj")%>
                        &body=<%=rs.getString("body")%>
                        &date=<%=rs.getString("date")%>'>
                        <%= rs.getString("subj")%> </a>
                        </td>
                        
                        <td>
                             <form action ="Delete">
                                <input type="hidden" name="idx" value="<%=rs.getInt("idx")%>">
                                <input type="submit" value="삭제">
                             </form>
                        </tr>                       
                     <%  }
                        rs.close();
                        stmt.close();
                        conn.close();
                     %>
                </tbody>
            </table>
            </center>
                
            <%
                }catch(Exception ex){
                    out.println("오류 발생:" + ex.getMessage()); }
                %>
    
             <jsp:include page="footer.jsp" />
</html>
