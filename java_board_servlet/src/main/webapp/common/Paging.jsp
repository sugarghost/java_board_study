<%--
  Created by IntelliJ IDEA.
  User: YK
  Date: 2023-02-15
  Time: 오전 11:33
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:if test="${pageDTO != null}">
<div class="row">
    <div class="col-12">
        <ul class="pagination d-flex justify-content-center">
            <c:if test="${pageDTO.startPageNum != 1}">
                <li class="page-item">
                    <a class="page-link" href="list.do?pageNum=1${searchManager.getSearchParamsQueryWithOutPageNum()}"><<</a>
                </li>
                <li class="page-item">
                    <a class="page-link" href="list.do?pageNum=${pageDTO.startPageNum - 1}${searchManager.getSearchParamsQueryWithOutPageNum()}"><</a>
                </li>
            </c:if>
            <c:forEach begin="${pageDTO.startPageNum}" end="${pageDTO.endPageNum}" step="1" var="index">

                <c:if test="${index == pageDTO.pageNum}">
                    <li class="page-item active">
                        <a class="page-link">${index}</a>
                    </li>
                </c:if>
                <c:if test="${index != pageDTO.pageNum && index <= pageDTO.totalPage}">
                <li class="page-item">
                    <a class="page-link" href="list.do?pageNum=${index}${searchManager.getSearchParamsQueryWithOutPageNum()}">${index}</a>
                </li>
                </c:if>
            </c:forEach>

            <c:if test="${pageDTO.startPageNum+pageDTO.blockPage <= pageDTO.totalPage}">
                <li class="page-item">
                    <a class="page-link" href="list.do?pageNum=${pageDTO.startPageNum+pageDTO.blockPage}${searchManager.getSearchParamsQueryWithOutPageNum()}">></a>
                </li>
                <li class="page-item">
                    <a class="page-link" href="list.do?pageNum=${pageDTO.totalPage}${searchManager.getSearchParamsQueryWithOutPageNum()}">>></a>
                </li>
            </c:if>

        </ul>
    </div>
</div>
</c:if>