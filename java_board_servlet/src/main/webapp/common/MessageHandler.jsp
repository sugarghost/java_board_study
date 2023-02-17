<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: YK
  Date: 2023-02-16
  Time: 오후 8:44
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<script>
  <c:forEach items="${messages}" var="message">
  <c:if test="${message.key eq param.error}">
  alert('${message.value}')
  </c:if>
  </c:forEach>
</script>
