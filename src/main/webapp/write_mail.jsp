<%-- 
    Document   : write_mail.jsp
    Author     : jongmin
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="cse.maven_webmail.control.CommandType" %>

<!DOCTYPE html>

<%-- @taglib  prefix="c" uri="http://java.sun.com/jsp/jstl/core" --%>


<html lang="en">
    <head>
        <meta charset="utf-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <title>메일 쓰기 화면</title>
        <link type="text/css" rel="stylesheet" href="css/main_style.css" />
    </head>
    <body>
        <head>
        <meta charset="utf-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <title>메일 쓰기 화면</title>
        <link type="text/css" rel="stylesheet" href="css/main_style.css" />
    </head>
    <body>
        <jsp:include page="header.jsp" />
        
        <div id="main">
            <form name="frm" method="POST" enctype="multipart/form-data"
                  action="WriteMail.do?menu=<%= CommandType.SEND_MAIL_COMMAND%>" >
                <table>
                    <tr>
                        <th scope="col"> 수신 </th>
                        <th scope="col"> <input type="text" name="to" size="80"
                                    value=<%=request.getParameter("recv") == null ? "" : request.getParameter("recv")%>>  </th>
                    </tr>
                    <tr>
                        <td>참조</td>
                        <td> <input type="text" name="cc" size="80"
                                    value=<%=request.getParameter("cc") == null ? "" : request.getParameter("cc")%>>  </td>
                    </tr>
                    <tr>
                        <td> 메일 제목 </td>
                        <td> <input type="text" name="subj" size="80"
                                    value=<%=request.getParameter("subj") == null ? "" : request.getParameter("subj")%>>  </td>
                    </tr>
                     <tr>
                        <td> 예약전송 </td>
                        <td> <input type="checkbox" name='reservation'>예약 전송 여부<br>
                            <input type="datetime-local" name="date"
                                   value=<%=request.getParameter("date") == null ? "" : request.getParameter("date")%>> </td>
                    </tr>
                    <tr>
                        <td colspan="2">본  &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 문</td>
                    </tr>
                    <tr>  <%-- TextArea    --%>
                        <td colspan="2">  <textarea rows="15" name="body" cols="80">
                                <%=request.getParameter("body") == null ? "" : request.getParameter("body")%> </textarea>
                        </td>
                    </tr>
                    <tr>
                        <td>첨부 파일</td>
                        <td> <input type="file" name="file1"  size="80">  </td>
                    </tr>
                        
                        <input type="hidden" name="user" value=<%=session.getAttribute("userid")%>>
                    <tr>
                        <td colspan="2">
                            <input type="submit" value="메일 보내기">
                            <input type="button" value="다시 입력" onclick="doReset()">
                            <input type="button" value="임시저장" onclick="doSubmit()" >
                            <script>
                                function doReset(){
                                    document.getElementsByName("to")[0].value="";
                                    document.getElementsByName("cc")[0].value="";
                                    document.getElementsByName("subj")[0].value="";
                                    document.getElementsByName("date")[0].value="";
                                    document.getElementsByName("body")[0].value="";
                                }
                                
                                function doSubmit(){ frm.action = "Storage";
                                    frm.encoding = "application/x-www-form-urlencoded";
                                    frm.submit(); }
                            </script>
                            <a href ="storage_list.jsp"> 임시저장 메일 목록</>
                        </td>
                    </tr>
                </table>
            </form>
        </div>

        <jsp:include page="footer.jsp" />
    </body>
</html>
