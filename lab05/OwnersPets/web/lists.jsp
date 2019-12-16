<%-- 
    Document   : lists
    Created on : 27.11.2019, 16:58:13
    Author     : �����
--%>

<%@page import="java.util.List"%>
<%@page import="entities.*"%>
<%@page contentType="text/html" pageEncoding="windows-1251"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=windows-1251">
        <title>JSP Page</title>
    </head>
    <body>   
        <h1> ����� ���������� � �������� � �� ����������: </h1>
        
        <c:out value="���������:" />
        
        <table border ="1" width="500" align="center"> 
         <tr bgcolor="00FF7F"> 
          <th><b>ID</b></th> 
          <th><b>���</b></th> 
          <th><b>�����</b></th> 
         </tr> 

        <c:forEach items="${dataP}" var="p"> 
            <tr>
                <th> ${p.getId()}</th>
                <th> ${p.getName()} </th>
                <th> ${p.getAddress()} </th>
            </tr>
        </c:forEach>
        </table> 
          
        <c:out value="�������:" />
        
        <table border ="1" width="500" align="center"> 
         <tr bgcolor="00FF7F">
          <th><b>ID</b></th>
          <th><b>���</b></th> 
          <th><b>���� ��������</b></th> 
          <th><b>���</b></th>
          <th><b>��������_Id</b></th> 
         </tr> 
         
        <c:forEach items="${dataO}" var="o"> 
            <tr>
                <th>${o.getId()}</th>
                <th>${o.getName()}</th>
                <th>${o.getBirthDate()}</th>
                <th>${o.getType()}</th>
                <th>${o.getPetOwnerId()}</th>
            </tr>
         </c:forEach>
            
        </table> 
        <a href='<c:url value="Index.jsp" />'><c:url value="�� �������" /></a>
        <br/>
        <a href='<c:url value="index.jsp" />'><c:url value="�������� ������" /></a>
        
    </body>
</html>

