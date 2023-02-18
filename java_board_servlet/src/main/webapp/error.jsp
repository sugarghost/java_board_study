<%--
  Created by IntelliJ IDEA.
  User: YK
  Date: 2023-02-14
  Time: 오후 2:05
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" language="java" %>
<html>
<head>
  <%@ include file="/common/encode.jsp" %>
    <title>Error</title>
</head>
<body>
    <h1>에러가 발생했습니다.</h1>
    <h2>에러 내용</h2>
    <p>${errorMessage}</p>

</body>
</html>
