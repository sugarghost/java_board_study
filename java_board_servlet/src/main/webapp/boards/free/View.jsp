<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
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
    <jsp:include page="/common/Encode.jsp"></jsp:include>
    <title>자유게시판 - 보기</title>
    <jsp:include page="/common/Bootstrap.jsp"></jsp:include>
    <jsp:include page="/common/MessageHandler.jsp">
        <jsp:param name="errorMessages" value="${errorMessages}"/>
    </jsp:include>
</head>
<body>
<div class="container">
    <h1>게시판 - 보기</h1>
    <div class="row d-flex">
        <div class="m-2 mr-auto">${articleDTO.writer}</div>
        <div class="m-2">
            등록일시
            <fmt:formatDate pattern="yyyy.MM.dd hh:mm" value="${articleDTO.createdDate}"/>
        </div>
        <div class="m-2">
            수정일시
            <c:if test="${articleDTO.isModifiedDateValid()}">
                <fmt:formatDate pattern="yyyy.MM.dd hh:mm" value="${articleDTO.modifiedDate}"/>
            </c:if>
            <c:if test="${!articleDTO.isModifiedDateValid()}">
                -
            </c:if>
        </div>
    </div>
    <div class="row">
        <div class="m-2">[${articleDTO.categoryName}]</div>
        <div class="m-2 mr-auto text-break">${articleDTO.title}</div>
        <div class="m-2">조회수: ${articleDTO.viewCount}</div>
    </div>
    <hr>
    <div class="row my-3">
        <div class="w-100 border text-break">
            ${articleDTO.content}
        </div>
    </div>
    <div class="row">
        <div class="w-100">
            <c:if test="${articleDTO.isFileExist}">
                <c:forEach var="fileDTO" items="${fileList}">
                    <a href="fileDownloadAction.do?fileId=${fileDTO.fileId}&articleId=${fileDTO.articleId}">${fileDTO.fileOriginName}</a>
                    <br>
                </c:forEach>
            </c:if>
        </div>
    </div>
    <div class="row bg-light">
        <div class="col-12">
            <c:forEach var="commentDTO" items="${commentList}">
                <div class="row justify-content-start px-2 pt-2">
                        ${commentDTO.createdDate}
                </div>
                <div class="row border-bottom px-2 pb-3 text-break">
                        ${commentDTO.content}
                </div>
            </c:forEach>

            <form action="commentWriteAction.do?${searchManager.getSearchParamsQuery()}"
                  method="post" class="w-100"
                  id="commentForm">
                <div class="row py-3">
                    <div class="col-10">
                        <input type="hidden" name="articleId" value="${articleDTO.articleId}">
                        <textarea class="form-control w-100 h-100 m-auto" name="content"
                                  placeholder="댓글을 입력해 주세요."></textarea>

                    </div>
                    <div class="col-2">
                        <button type="submit" class="btn btn-primary w-100 h-100 m-auto">
                            등록
                        </button>
                    </div>
                </div>
            </form>
        </div>
    </div>
    <div class="row">
        <div class="w-100 d-flex justify-content-center">
            <button type="button" class="btn btn-primary"
                    onclick="location.href = 'list.do?${searchManager.getSearchParamsQuery()}'">목록
            </button>
            <button type="button" class="btn border"
                    onclick="location.href = 'modify.do?articleId=${articleDTO.articleId}${searchManager.getSearchParamsQuery()}'">
                수정
            </button>
            <button type="button" class="btn border" data-toggle="modal"
                    data-target="#passwordCheckModal">삭제
            </button>
        </div>
    </div>
</div>

<!-- 패스워드 체크 Modal -->
<div class="modal fade" id="passwordCheckModal" tabindex="-1" role="dialog" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered" role="document">
        <div class="modal-content">
            <form action="deleteAction.do?${searchManager.getSearchParamsQuery()}" method="post"
                  class="w-100">
                <input type="hidden" name="articleId" value="${articleDTO.articleId}">
                <div class="modal-header">
                    <h5 class="modal-title" id="passwordCheckModalTitle">비밀번호 확인</h5>
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                        <span aria-hidden="true">&times;</span>
                    </button>
                </div>
                <div class="modal-body">
                    <div class="col-2">
                        비밀번호 *
                    </div>
                    <div class="col-10">
                        <input type="password" class="form-control" name="password"
                               placeholder="비밀번호를 입력해 주세요.">
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-dismiss="modal">Close
                    </button>
                    <button type="submit" class="btn btn-primary">확인</button>
                </div>
            </form>
        </div>
    </div>
</div>
</body>
</html>
