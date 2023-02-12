<%@ page import="com.board.jsp.yoony.article.ArticleDAO" %>
<%@ page import="com.board.jsp.yoony.article.ArticleDTO" %>
<%@ page import="com.oreilly.servlet.multipart.DefaultFileRenamePolicy" %>
<%@ page import="com.oreilly.servlet.MultipartRequest" %>
<%@ page import="com.board.jsp.yoony.article.file.FileDAO" %>
<%@ page import="java.util.Enumeration" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.io.File" %>
<%@ page import="com.board.jsp.yoony.article.file.FileDTO" %><%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2023-02-12
  Time: 오전 1:52
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<%

    ArticleDAO articleDAO = ArticleDAO.getInstance();
    ArticleDTO articleDTO = new ArticleDTO();

    // String saveDirectory = application.getRealPath("/uploads");
    String saveDirectory = "C:\\tempUploads";
    System.out.println("saveDirectory : " + saveDirectory);
    int maxPostSize = 10 * 1024 * 1024; // 10MB 제한
    String encoding = "UTF-8";

    MultipartRequest multi = new MultipartRequest(request, saveDirectory, maxPostSize,
            encoding, new DefaultFileRenamePolicy());
    articleDTO.setArticleId(Integer.parseInt(multi.getParameter("articleId")));
    articleDTO.setTitle(multi.getParameter("title"));
    articleDTO.setContent(multi.getParameter("content"));
    articleDTO.setWriter(multi.getParameter("writer"));
    articleDTO.setPassword(multi.getParameter("password"));

    boolean isPasswordValid = articleDAO.getPasswordCheck(articleDTO.getArticleId(),
            articleDTO.getPassword());
    // password가 일치하면 진행
    if (isPasswordValid) {
        int articleUpdateResult = articleDAO.updateArticle(articleDTO);
        if (articleUpdateResult < 1) {
            response.sendRedirect(request.getHeader("referer") + "&articleUpdateError=1");
        }

        // 삭제 대상의 ID들을 받아옴
        String[] files = multi.getParameterValues("deleteFileId");
        FileDAO fileDAO = FileDAO.getInstance();

        if (files != null) {
            for (String file : files) {
                // 시간 관계상 처리가 안된 File에 대한 분기를 생략
                int fileDeleteResult = fileDAO.deleteFile(Integer.parseInt(file), articleDTO.getArticleId());
                if (fileDeleteResult > 0 ) {
                    // 실제 파일 삭제(보류)
                    // File deleteFile = new File(saveDirectory + File.separator + 실제 저장 파일 이름);
                    // deleteFile.delete();
                }
            }

            // 삭제 후 파일이 없는 경우 article 테이블의 fileExist를 false로 변경
            int fileCount = fileDAO.getFileCount(articleDTO.getArticleId());
            if (fileCount == 0 ) {
               articleDAO.updateArticleFileExist(articleDTO.getArticleId(), false);
            }
        }

        // 파일 삭제와 별개로 넘어온 파일이 있는 경우 Upload 처리
        boolean isFileExist = false;
        try {
            Enumeration uploadfiles = multi.getFileNames();
            while (uploadfiles.hasMoreElements()) {
                String file = (String) uploadfiles.nextElement();
                System.out.println("file : " + file);
                String fileName = multi.getOriginalFileName(file);
                if (fileName == null) {
                    continue;
                }
                isFileExist = true;
                System.out.println("fileName : " + fileName);
                String realFileName = multi.getFilesystemName(file);
                System.out.println("realFileName : " + realFileName);
                String ext = fileName.substring(fileName.lastIndexOf(".") + 1);

                String nowDate = new SimpleDateFormat("yyyyMMdd_HmsS").format(System.currentTimeMillis());
                String newFileName = nowDate + "." + ext;

                File oldFile = new File(saveDirectory + File.separator + realFileName);
                File newFile = new File(saveDirectory + File.separator + newFileName);
                oldFile.renameTo(newFile);

                FileDTO fileDTO = new FileDTO();
                fileDTO.setArticleId(articleDTO.getArticleId());
                fileDTO.setFileOriginName(fileName);
                fileDTO.setFileSaveName(newFileName);
                fileDTO.setFileType(ext);

                int fileInsertResult = fileDAO.insertFile(fileDTO);

                if (fileInsertResult == 0) {
                    out.println("<script>alert('"+fileName+" 파일 등록 실패!')</script>");
                }
            }

            if (isFileExist) {
                articleDAO.updateArticleFileExist(articleDTO.getArticleId(),true);
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getHeader("referer") + "&fileError=1");
        }

        response.sendRedirect("View.jsp?articleId=" + articleDTO.getArticleId());
    } else {
        // 패스워드가 일치하지 않는 경우로 passwordError와 함께 이전 요청페이지로 복귀
        // 복귀하면 작성중 내용이 다 초기화 되는데 마음에 안듬(일단 보류)
        response.sendRedirect(request.getHeader("referer") + "&passwordError=1");
    }


%>
</body>
</html>
