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
    <%@ include file="/common/Encode.jsp" %>
    <title>게시판 - 등록</title>
    <%@ include file="/common/Bootstrap.jsp" %>
</head>
<body>
<%@ include file="/common/SearchKeeper.jsp" %>
<%
    CategoryDAO categoryDAO = CategoryDAO.getInstance();
    List<CategoryDTO> categoryList = categoryDAO.getCategoryList();
    pageContext.setAttribute("categoryList", categoryList);
%>

<div class="container">
    <h1>게시판 - 등록</h1>
    <form name="articleForm" method="post" action="WriteAction.jsp?${searchKeeperSearchParams}"
          enctype="multipart/form-data"
          onsubmit="return validateForm(this);">

        <table class="table">
            <tbody>
            <tr>
                <td class="bg-light">카테고리 *</td>
                <td>
                    <select class="form-control" name="category">
                        <option value="">카테고리</option>
                        <c:forEach items="${categoryList}" var="categoryDTO">
                            <option value="${categoryDTO.getName()}">${categoryDTO.getName()}</option>
                        </c:forEach>
                    </select>
                </td>
            </tr>
            <tr>
                <td class="bg-light">작성자 *</td>
                <td><input type="text" class="form-control" name="writer"/></td>
            </tr>

            <tr>
                <td class="bg-light">비밀번호 *</td>
                <td>
                    <input type="password" class="form-control" name="password" placeholder="비밀번호"/>
                    <input type="password" class="form-control" name="passwordCheck"
                           placeholder="비밀번호 확인">
                </td>
            </tr>
            <tr>
                <td class="bg-light">제목 *</td>
                <td><input type="text" class="form-control" name="title"/></td>
            </tr>
            <tr>
                <td class="bg-light">내용 *</td>
                <td><textarea class="form-control" name="content"></textarea></td>
            </tr>
            <tr>
                <td class="bg-light">파일첨부</td>
                <td>
                    <div class="input-group">
                        <div class="custom-file">
                            <input type="file" class="custom-file-input" id="customFileInput1"
                                   name="file1">
                            <label class="custom-file-label" for="customFileInput1"
                                   aria-describedby="inputGroupFile1">파일 찾기</label>
                        </div>
                    </div>

                    <div class="input-group">
                        <div class="custom-file">
                            <input type="file" class="custom-file-input" id="customFileInput2"
                                   name="file2">
                            <label class="custom-file-label" for="customFileInput2"
                                   aria-describedby="inputGroupFile2">파일 찾기</label>
                        </div>
                    </div>

                    <div class="input-group">
                        <div class="custom-file">
                            <input type="file" class="custom-file-input" id="customFileInput3"
                                   name="file3">
                            <label class="custom-file-label" for="customFileInput3"
                                   aria-describedby="inputGroupFile3">파일 찾기</label>
                        </div>
                    </div>
                </td>
            </tr>
            </tbody>
        </table>
        <div>
            <button type="button" class="btn border float-left"
                    onclick="location.href = 'List.jsp?${searchKeeperSearchParams}'">취소
            </button>
        </div>
        <div>
            <button type="submit" class="btn btn-primary float-right">저장</button>
        </div>
    </form>
</div>
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

  // custom file input을 사용하며 선택한 파일 명을 update하기 위한 리스너
  document.querySelectorAll('.custom-file-input').forEach((target) => {
    target.addEventListener('change', (e) => {
      let fileName = e.target.files[0].name;
      let nextSibling = e.target.nextElementSibling
      nextSibling.innerText = fileName
    })
  })
</script>
</body>
</html>
