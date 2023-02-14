<%@ page import="com.board.jsp.yoony.article.ArticleDAO" %>
<%@ page import="com.board.jsp.yoony.article.ArticleDTO" %>
<%@ page import="com.board.jsp.yoony.article.file.FileDAO" %>
<%@ page import="com.board.jsp.yoony.article.file.FileDTO" %>
<%@ page import="java.util.List" %>
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
    <title>게시판 - 수정</title>
    <%@ include file="/common/Bootstrap.jsp" %>
</head>
<body>
<%@ include file="/common/SearchKeeper.jsp" %>
<%
    if (("1").equals(request.getParameter("passwordError"))) {
        out.println("<script>alert('비밀번호가 일치하지 않습니다!')</script>");
    }
    if (("1").equals(request.getParameter("fileError"))) {
        out.println("<script>alert('파일 업로드에 실패했습니다!')</script>");
    }

    ArticleDAO articleDAO = ArticleDAO.getInstance();
    int articleId = Integer.parseInt(request.getParameter("articleId"));
    ArticleDTO articleDTO = articleDAO.getArticle(articleId);
    if (articleDTO == null) {
        out.println("해당 게시물이 존재하지 않습니다.");
        return;
    }
    pageContext.setAttribute("articleDTO", articleDTO);

    if (articleDTO.getFileExistFlag()) {
        FileDAO fileDAO = FileDAO.getInstance();
        List<FileDTO> fileList = fileDAO.getFileList(articleId);
        pageContext.setAttribute("fileList", fileList);
    }

%>

<div class="container">
    <h1>게시판 - 수정</h1>
    <form name="articleForm" method="post" action="ModifyAction.jsp?${searchKeeperSearchParams}"
          enctype="multipart/form-data"
          onsubmit="return validateForm(this);">
        <input type="hidden" name="articleId" value="${articleDTO.articleId}"/>
        <table class="table">
            <tbody>
            <tr>
                <td class="bg-light">카테고리 *</td>
                <td>
                    ${articleDTO.category}
                </td>
            </tr>
            <tr>
                <td class="bg-light">등록 일시</td>
                <td>${articleDTO.createdDate}</td>
            </tr>
            <tr>
                <td class="bg-light">수정 일시</td>
                <td>${(articleDTO.modifiedDate != null)? articleDTO.modifiedDate:"-"}</td>
            </tr>
            <tr>
                <td class="bg-light">조회수</td>
                <td>${articleDTO.viewCount}</td>
            </tr>
            <tr>
                <td class="bg-light">작성자 *</td>
                <td><input type="text" class="form-control" name="writer"
                           value="${articleDTO.writer}"/></td>
            </tr>

            <tr>
                <td class="bg-light">비밀번호 *</td>
                <td><input type="password" class="form-control" name="password"/></td>
            </tr>
            <tr>
                <td class="bg-light">제목 *</td>
                <td><input type="text" class="form-control" name="title"
                           value="${articleDTO.title}"/></td>
            </tr>
            <tr>
                <td class="bg-light">내용 *</td>
                <td><textarea class="form-control" name="content">${articleDTO.content}</textarea>
                </td>
            </tr>
            <tr>
                <td class="bg-light">첨부 파일</td>
                <td>
                    <c:if test="${articleDTO.fileExistFlag}">
                        <c:forEach var="fileDTO" items="${fileList}">
                            <div class="row" id="file_${fileDTO.fileId}">
                                    ${fileDTO.fileOriginName}
                                <button type="button"
                                        class="btn border"
                                        onclick="location.href = 'Download.jsp?fileSaveName=${fileDTO.fileSaveName}&fileOriginName=${fileDTO.fileOriginName}${searchKeeperSearchParams}'">
                                    Download
                                </button>
                                <button type="button" class="btn border"
                                        onclick="deleteFile('file_${fileDTO.fileId}')">X
                                </button>
                            </div>
                        </c:forEach>
                    </c:if>
                    <div class="input-group">
                        <div class="custom-file">
                            <input type="file" class="custom-file-input" id="customFileInput"
                                   name="file">
                            <label class="custom-file-label" for="customFileInput"
                                   aria-describedby="inputGroupFile">파일 찾기 file</label>
                        </div>
                    </div>
                </td>
            </tbody>
        </table>
        <div>
            <button type="button"
                    class="btn border float-left"
                    onclick="location.href = 'View.jsp?articleId=${articleDTO.articleId}${searchKeeperSearchParams}'">취소
            </button>
        </div>
        <div>
            <button type="submit" class="btn btn-primary float-right">저장</button>
        </div>
    </form>
</div>
<script>
  function validateForm(form) {
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

  // 파일 삭제를 바로하는게 아닌 삭제 대상 Element는 지워줌
  // 지운 자리는 보이지않는 input 태그로 대체해 수정 작업시 삭제 대상 Id를 전송
  function deleteFile(fileID) {
    let fileRow = document.getElementById(fileID);
    let fileInput = document.createElement("input");
    fileInput.type = "hidden";
    fileInput.name = "deleteFileId";
    fileInput.value = fileID.split("_")[1];
    fileRow.replaceWith(fileInput);
  }

  // custom file input을 사용하며 선택한 파일 명을 update하기 위한 리스너
  document.querySelector('.custom-file-input').addEventListener('change', function (e) {
    let fileName = e.target.files[0].name;
    let nextSibling = e.target.nextElementSibling
    nextSibling.innerText = fileName
  })
</script>
</body>
</html>
