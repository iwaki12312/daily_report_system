<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<c:import url="/WEB-INF/views/layout/app.jsp">
    <c:param name="content">
        <c:choose>
            <c:when test="${client != null}">
                <h2>id : ${client.id} の取引先情報　詳細ページ</h2>

                <table>
                    <tbody>
                        <tr>
                            <th>取引先コード</th>
                            <td><c:out value="${client.code}" /></td>
                        </tr>
                        <tr>
                            <th>名称</th>
                            <td><c:out value="${client.name}" /></td>
                        </tr>
                        <tr>
                            <th>住所</th>
                            <td><c:out value="${client.address}" /></td>
                        </tr>
                        <tr>
                            <th>電話番号</th>
                            <td><c:out value="${client.tell}" /></td>
                        </tr>
                        <tr>
                            <th>登録日時</th>
                            <td>
                                <fmt:formatDate value="${client.created_at}" pattern="yyyy-MM-dd HH:mm:ss" />
                            </td>
                        </tr>
                        <tr>
                            <th>更新日時</th>
                            <td>
                                <fmt:formatDate value="${client.updated_at}" pattern="yyyy-MM-dd HH:mm:ss" />
                            </td>
                        </tr>
                    </tbody>
                </table>
                <c:if test="${login_employee.position_flag > 1}">
                    <span class = "editLink"><a href="<c:url value='/clients/edit?id=${client.id}' />">この取引先情報を編集する</a></span><br>
                    <span class = "editLink"><a href="<c:url value='/reports/clientindex?id=${client.id}' />">この取引先に関する日報を表示する</a></span>
                </c:if>
            </c:when>
            <c:otherwise>
                <h2>お探しのデータは見つかりませんでした。</h2>
            </c:otherwise>
        </c:choose>

        <p><a href="<c:url value='/clients/index' />">一覧に戻る</a></p>
    </c:param>
</c:import>