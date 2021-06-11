<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="cse.maven_webmail.control.CommandType" %>

<!DOCTYPE html">

<html lang="en">
    <body>
    <head>
    <div id="main" style="width: 50%; margin-left: 25%; margin-right: 25%;">
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>회원가입</title>
        <link type="text/css" rel="stylesheet" href="css/main_style.css" />
    </head>
        <%@include file="header.jspf"%>


    <div id="main">

                    <td>비밀번호는 영어 대소문자와 숫자 조합이며, 8~10자로 작성하세요! </td>
        <form name="AddUser" action="UserAdmin.do?menu=<%= CommandType.ADD_USER_MENU%>" method="POST">
            <table>
                <tr>
                    <th scope="col">border="0"</th>
                    <th scope="col">align="left"</th>
                </tr>
                <tr>
                    <td>아이디 </td>
                    <td> <input type="id" name="id" value="" size="25" placeholder="ID"  maxlength="30" required>
                </tr>
                        <tr>
                            <td>비밀번호 </td>
                            <td> <input type="password" name="password" value="" size="25" placeholder="비밀번호" maxlength="20" id="pw" onchange="isSame()" /> </td>
                        </tr>
                        <tr>
                            <td>비밀번호 확인 </td>
                            <td> <input type="password" name="password2" id="checkpw" onchange="isSame()" value="" size="25" placeholder="비밀번호 확인" maxlength="15" /> </td>
                        <br> <span id="same"></span> </td>
                        <tr>
                            <td colspan="2">
                                <input type="submit" value="회원가입"onClick ="return getConfirmResult()"/>
                                <input type="reset" value="취소" name="reset" />
                            </td>

                            </table>
                    </form>
                    </div>
            <br />

                    <jsp:include page="footer.jsp" />

                    </body>
                    </html>
