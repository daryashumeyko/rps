<%-- 
    Document   : create
    Created on : 27.11.2019, 17:05:28
    Author     : �����
--%>

<%@page contentType="text/html" pageEncoding="windows-1251"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=windows-1251">
        <title>JSP Page</title>
    </head>
    <body>
        <h1>�������� ���������:</h1>
        <form action="UIController?choose=created" method ="post">
        <dl>
            <dt>Name: </dt>
            <dd><input type="text" name="���:" value="${Name}" /></dd>
        </dl>
        <dl>
            <dt>Address: </dt>
            <dd><input type="text" name="�����:" value="${Address}"/></dd>
        </dl>
         <button type="created">�������</button>
        </form>
    </body>
</html>
