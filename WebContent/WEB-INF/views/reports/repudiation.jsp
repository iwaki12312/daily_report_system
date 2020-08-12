<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:import url="/WEB-INF/views/layout/app.jsp">
    <c:param name="content">
        <h2>否認理由入力ページ</h2>

        <form method="POST" action="<c:url value='/reports/return?id=${report.id}' />">

            <c:if test="${errors != null}">
                <div id="flush_error">
                    入力内容にエラーがあります。<br />
                    <c:forEach var="error" items="${errors}">
                        ・<c:out value="${error}" /><br />
                    </c:forEach>

                </div>
            </c:if>
            <label for="content">否認理由</label><br />
            <textarea name="denial_text" rows="10" cols="50"></textarea>
            <br /><br />
            <input type="hidden" name="_token" value="${_token}" />
            <input type="hidden" name="approval_status" value="${report.approval_status}" />
            <input type="submit" name="cmd" value="投稿する"/>
        </form>

        <p><a href="<c:url value='/reports/show?id=${report.id}' />">レポート詳細へ戻る</a></p>
    </c:param>
</c:import>