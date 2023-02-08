<%@ page import="com.board.jsp.yoony.category.CategoryDTO" %>
<%@ page import="java.util.List" %>
<%@ page import="com.board.jsp.yoony.category.CategoryDAO" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: YK
  Date: 2023-02-06
  Time: 오후 4:14
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" language="java" %>
<html>
<head>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8">
    <meta charset="UTF-8">
    <title>게시물 - 등록</title>
    <script>
      function validateForm(form) {
        if (form.category.value == "") {
          alert("카테고리를 선택해주세요.");
          form.category.focus();
          return false;
        }
        if (form.writer.value == "" || !/^.{3,4}$/g.test(form.writer.value)) {
          alert("작성자를 3글자 이상, 5글자 미만으로 입력해주세요.");
          form.writer.focus();
          return false;
        }
        if (form.password.value == ""
            || !/^(?=.*[A-Za-z])(?=.*\d)(?=.*[$@$!%*#?&])[A-Za-z\d$@$!%*#?&]{4,15}$/.test(
                form.password.value)) {
          alert("비밀번호를 영문, 숫자, 특수문자를 포함해 4글자 이상, 16글자 미만으로 입력해주세요.");
          form.password.focus();
          return false;
        }
        if (form.password.value != form.passwordCheck.value) {
          alert("비밀번호가 일치하지 않습니다.");
          form.password.focus();
          return false;
        }
        if (form.title.value == "" || !/^.{4,99}$/g.test(form.title.value)) {
          alert("제목을 4글자 이상, 100글자 미만으로 입력해주세요.");
          form.title.focus();
          return false;
        }
        if (form.content.value == "" || !/^.{4,1999}$/g.test(form.content.value)) {
          alert("내용을 4글자 이상, 2000글자 미만으로 입력해주세요.");
          form.content.focus();
          return false;
        }
      }
    </script>
</head>
<body>
<%
    CategoryDAO categoryDAO = CategoryDAO.getInstance();
    List<CategoryDTO> categoryList = categoryDAO.getCategoryList();
    pageContext.setAttribute("categoryList", categoryList);
%>
<h1>게시판 - 등록</h1>
<form name="articleForm" method="post" action="WriteAction.jsp"
      onsubmit="return validateForm(this);">

    <table>
        <tr>
            <td>카테고리 *</td>
            <td>
                <select name="category">
                    <option value="">카테고리</option>
                    <c:forEach items="${categoryList}" var="categoryDTO">
                        <option value="${categoryDTO.getName()}">${categoryDTO.getName()}</option>
                    </c:forEach>
                </select>
            </td>
        </tr>
        <tr>
            <td>작성자 *</td>
            <td><input type="text" name="writer"/></td>
        </tr>

        <tr>
            <td>비밀번호 *</td>
            <td><input type="password" name="password"/><input type="password" name="passwordCheck">
            </td>
        </tr>
        <tr>
            <td>제목 *</td>
            <td><input type="text" name="title"/></td>
        </tr>
        <tr>
            <td>내용 *</td>
            <td><textarea name="content"></textarea></td>
        </tr>
        <tr>
            <td>파일첨부</td>
            <td><input type="file" name="file"></td>
        </tr>
    </table>
    <div>
        <button type="button" onclick="location.href = 'List.jsp'">취소</button>
    </div>
    <div>
        <button type="submit">저장</button>
    </div>
</form>
</body>
</html>
