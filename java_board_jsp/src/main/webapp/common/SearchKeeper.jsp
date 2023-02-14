<%@ page import="java.net.URLEncoder" %><%--
  Created by IntelliJ IDEA.
  User: YK
  Date: 2023-02-13
  Time: 오후 4:50
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" language="java" %>
<%
    String searchKeeperPageNum =
            (request.getParameter("pageNum") != null) ? request.getParameter("pageNum") : "";
    String searchKeeperSearchWord =
            (request.getParameter("searchWord") != null) ? request.getParameter("searchWord") : "";
    String searchKeeperCategory =
            (request.getParameter("category") != null) ? request.getParameter("category") : "";
    String searchKeeperStartDate =
            (request.getParameter("startDate") != null) ? request.getParameter("startDate") : "";
    String searchKeeperEndDate =
            (request.getParameter("endDate") != null) ? request.getParameter("endDate") : "";

    String searchKeeperSearchParams = "";
    searchKeeperSearchParams += "&pageNum=" + searchKeeperPageNum;
    searchKeeperSearchParams += "&searchWord=" + URLEncoder.encode(searchKeeperSearchWord,"UTF-8");
    searchKeeperSearchParams += "&category=" + URLEncoder.encode(searchKeeperCategory,"UTF-8");
    searchKeeperSearchParams += "&startDate=" + searchKeeperStartDate;
    searchKeeperSearchParams += "&endDate=" + searchKeeperEndDate;

    pageContext.setAttribute("searchKeeperSearchParams",searchKeeperSearchParams);
%>