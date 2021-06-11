<%-- 
    Document   : show_message.jsp
    Author     : jongmin
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>

<!DOCTYPE html>

<jsp:useBean id="pop3" scope="page" class="cse.maven_webmail.model.Pop3Agent" />
<%
            pop3.setHost((String) session.getAttribute("host"));
            pop3.setUserid((String) session.getAttribute("userid"));
            pop3.setPassword((String) session.getAttribute("password"));
%>


<html lang="en">
    <head>
        <meta charset="utf-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <title>메일 보기 화면</title>
        <link type="text/css" rel="stylesheet" href="css/main_style.css" />
        <script src="//mozilla.github.io/pdf.js/build/pdf.js"></script>

    </head>
    <body>
        <jsp:include page="header.jsp" />

        <div id="sidebar">
            <jsp:include page="sidebar_previous_menu.jsp" />
        </div>

        <div id="msgBody">
            <% pop3.setRequest(request); %>
            <%= pop3.getMessage(Integer.parseInt((String) request.getParameter("msgid")))%>
        </div>
       <a href="https://github.com/Bo-Yeong/pdftest/blob/main/sample.pdf">view</a>
       <br>
       <iframe src="https://www.koreascience.or.kr/article/CFKO201831342441249.pdf" style="width:500px; height:300px; border:1px solid #00c;"></iframe>

  

        <jsp:include page="footer.jsp" />


    </body>
</html>
