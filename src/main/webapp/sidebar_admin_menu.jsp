<%-- 
    Document   : sidebar_admin_menu.jsp
    Author     : jongmin
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="cse.maven_webmail.control.CommandType" %>

<!DOCTYPE html>

<html lang="en">
    <head>
        <meta charset="utf-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <title>웹메일 시스템 메뉴</title>
    </head>
    <body>
        <br> <br>
        
        <span style="color: indigo"> <strong>사용자: <%= session.getAttribute("userid") %> </strong> </span> <br>
        <%--
        <p><a href="UserAdmin.do?select=<%= CommandType.ADD_USER_MENU %>">사용자 추가</a></p>
        <p><a href="UserAdmin.do?select=<%= CommandType.DELETE_USER_MENU %>">사용자 제거</a></p>
        --%>
        <p><a href="add_user.jsp">사용자 추가</a> </p>
        <p><a href="delete_user.jsp"> 사용자 제거</a> </p>
        <p><a href="Login.do?menu=<%= CommandType.LOGOUT %>">로그아웃</a></p>
    </body>
</html>
