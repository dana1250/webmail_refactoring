<%-- 
    Document   : login_duplicate
    Author     : yeonghwa
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>

<!DOCTYPE html>

<script type="text/javascript">
    <!--
    function gohome(){
        window.location = "/maven_webmail/"
    }
    -->
</script>


<html lang="en">
    <head>
        <meta charset="utf-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <title>로그인 실패</title>
        <link type="text/css" rel="stylesheet" href="css/main_style.css" />
    </head>
    <body onload="setTimeout('gohome()', 5000)">

        <jsp:include page="header.jsp" />

        <p id="login_duplicate">
            <%= request.getParameter("userid")%>님, 현재 ID는 다른 곳에서 로그인 중 입니다. 로그아웃 후 사용해주십시요.

            5초 뒤 자동으로 초기 화면으로 돌아갑니다.

            자동으로 화면 전환이 일어나지 않을 경우
            <!-- <a href="/WebMailSystem/" title="초기 화면">초기 화면</a>을 선택해 주세요.-->
            <a href="<%= getServletContext().getInitParameter("HomeDirectory") %>" title="초기 화면">초기 화면</a>을 선택해 주세요.
        </p>

        <jsp:include page="footer.jsp" />

    </body>
</html>
