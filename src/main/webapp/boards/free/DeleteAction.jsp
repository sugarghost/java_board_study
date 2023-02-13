<%@ page import="com.board.jsp.yoony.article.ArticleDAO" %>
<%@ page import="com.board.jsp.yoony.article.file.FileDAO" %><%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2023-02-12
  Time: 오전 4:09
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<%
    request.setCharacterEncoding("UTF-8");
    ArticleDAO articleDAO = ArticleDAO.getInstance();
    int articleId = Integer.parseInt(request.getParameter("articleId"));
    String password = request.getParameter("password");

    boolean isPasswordValid = articleDAO.getPasswordCheck(articleId, password);
    if(isPasswordValid){
        int result = articleDAO.deleteArticle(articleId, password);

        if (result > 0) {
            FileDAO fileDAO = FileDAO.getInstance();
            // 게시글이 지워지면 파일도 전부 삭제(실제 파일 삭제는 보류)
            fileDAO.deleteAllFile(articleId);
            response.sendRedirect("List.jsp");
        } else {
          // model2에서는 예외처리를 공통적으로 처리할 수 있는 부분을 고민하기
            response.sendRedirect(request.getHeader("referer") + "&deleteError=1");
        }
    } else {
        response.sendRedirect(request.getHeader("referer") + "&passwordError=1");
    }
%>
</body>
</html>
