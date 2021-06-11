<%-- 
    Document   : pdf_viewer
    Created on : 2021. 5. 26., 오전 12:30:11
    Author     : rlaqh
--%>

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN""http://www.w3.org/TR/html4/loose.dtd">


<html lang="en">
    <head>
        <meta charset="utf-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <title>첨부파일 보기</title>
        <script type="text/javascript" src="//mozilla.github.io/pdf.js/build/pdf.js"></script>
        <script type="text/javascript" src="/mozilla.github.io/pdf.js/build/pdf.worker.js"></script>
        
    </head>
    <body>
       
        <iframe width="700px" height="800px"src = "http://127.0.0.1:8081/web/viewer.html?file=My_Everything.pdf"></iframe>
        
    </body>
</html>
