<%@ page import="java.io.File" %>
<%@ page import="java.io.InputStream" %>
<%@ page import="java.io.FileInputStream" %>
<%@ page import="java.io.OutputStream" %>
<%@ page import="java.io.FileNotFoundException" %><%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2023-02-11
  Time: 오후 8:31
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<%@ include file="/common/SearchKeeper.jsp" %>
<%
    // String saveDirectory = application.getRealPath("/uploads");
    // 디렉토리등 공통적으로 쓰는 경우는 프로퍼티로 따로 빼는게 좋음
    // 첨부파일 다운로드 취약점 생각해야함
    String saveDirectory = "C:\\tempUploads";
    String fileSaveName = request.getParameter("fileSaveName");
    String fileOriginName = request.getParameter("fileOriginName");

    try {
        File file = new File(saveDirectory + File.separator + fileSaveName);
        InputStream inStream = new FileInputStream(file);

        // 한글 이름 꺠짐 방지
        String client = request.getHeader("User-Agent");
        if (client.indexOf("WOW64") == -1) {
            fileOriginName = new String(fileOriginName.getBytes("UTF-8"), "ISO-8859-1");
        } else {
            fileOriginName = new String(fileOriginName.getBytes("KSC5601"), "ISO-8859-1");
        }

        // 파일 다운로드용 응답 헤더 설정
        response.reset();
        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition",
                "attachment; filename=\"" + fileOriginName + "\"");
        response.setHeader("Content-Length", String.valueOf(file.length()));
        out.clear();

        OutputStream outStream = response.getOutputStream();

        byte[] buffer = new byte[(int) file.length()];
        int readBuffer = 0;
        while ((readBuffer = inStream.read(buffer)) > 0) {
            outStream.write(buffer, 0, readBuffer);
        }

        inStream.close();
        outStream.close();
    } catch (FileNotFoundException e) {
        out.print("<script>alert('파일이 존재하지 않습니다.');history.back()</script>");
    } catch (Exception e) {
        out.print("<script>alert('예외가 발생하였습니다.');history.back()</script>");
    }
%>
</body>
</html>
