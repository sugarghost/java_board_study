<%@ page import="com.board.jsp.yoony.article.ArticleDAO" %>
<%@ page import="com.board.jsp.yoony.article.ArticleDTO" %>
<%@ page import="com.oreilly.servlet.MultipartRequest" %>
<%@ page import="com.oreilly.servlet.multipart.DefaultFileRenamePolicy" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.io.File" %>
<%@ page import="java.util.Enumeration" %>
<%@ page import="com.board.jsp.yoony.article.file.FileDTO" %>
<%@ page import="com.board.jsp.yoony.article.file.FileDAO" %>
<%@ page import="java.util.UUID" %><%--
  Created by IntelliJ IDEA.
  User: YK
  Date: 2023-02-06
  Time: 오후 4:23
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" language="java" %>
<html>
<head>
    <%@ include file="/common/Encode.jsp" %>
    <title>게시판 - 등록</title>
    <script>
      function articleInsertSuccess() {
        alert("게시글 등록 성공");
        location.href = "List.jsp?startDate=&endDate=&category=&searchWord=&pageNum=1";
      }

      function articleInsertFailed() {
        alert("게시글 등록 실패");
        history.back();
      }
    </script>
</head>
<body>
<%@ include file="/common/SearchKeeper.jsp" %>
<%
    ArticleDTO articleDTO = new ArticleDTO();
    ArticleDAO articleDAO = ArticleDAO.getInstance();

    // String saveDirectory = application.getRealPath("/uploads");
    String saveDirectory = "C:\\tempUploads";
    int maxPostSize = 10 * 1024 * 1024; // 10MB 제한
    String encoding = "UTF-8";

    MultipartRequest multi = new MultipartRequest(request, saveDirectory, maxPostSize,
            encoding, new DefaultFileRenamePolicy());
    articleDTO.setTitle(multi.getParameter("title"));
    articleDTO.setContent(multi.getParameter("content"));
    articleDTO.setWriter(multi.getParameter("writer"));
    articleDTO.setCategory(multi.getParameter("category"));
    articleDTO.setPassword(multi.getParameter("password"));

    int articleInsertResult = articleDAO.insertArticle(articleDTO);
    boolean isFileExist = false;
    if (articleInsertResult != 0) {

        try {
            Enumeration files = multi.getFileNames();
            while (files.hasMoreElements()) {
                String file = (String) files.nextElement();
                String fileName = multi.getOriginalFileName(file);
                if (fileName == null) {
                    continue;
                }
                isFileExist = true;
                String realFileName = multi.getFilesystemName(file);
                String ext = fileName.substring(fileName.lastIndexOf(".") + 1);

                String newFileName = UUID.randomUUID() + "." + ext;

                File oldFile = new File(saveDirectory + File.separator + realFileName);
                File newFile = new File(saveDirectory + File.separator + newFileName);
                oldFile.renameTo(newFile);

                FileDTO fileDTO = new FileDTO();
                fileDTO.setArticleId(articleInsertResult);
                fileDTO.setFileOriginName(fileName);
                fileDTO.setFileSaveName(newFileName);
                fileDTO.setFileType(ext);

                FileDAO fileDAO = FileDAO.getInstance();
                int fileInsertResult = fileDAO.insertFile(fileDTO);

                if (fileInsertResult == 0) {
                    // TODO: 자바쪽에서 스크립트 호출 방식은 안좋은 방식
                    // COMMENT: 다음 Servlet 방식에서 파일 등록시 실패에 대한 분기를 마련하는 방안으로 고려
                    out.println("<script>alert('" + fileName + " 파일 등록 실패!')</script>");
                }
            }

            if (isFileExist) {
                articleDAO.updateArticleFileExist(articleInsertResult, true);
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.getRequestDispatcher("Write.jsp?" + searchKeeperSearchParams).forward(request, response);
        }
        out.println("<script>articleInsertSuccess()</script>");
    } else {
        out.println("<script>articleInsertFailed()</script>");
    }
%>
</body>
</html>
