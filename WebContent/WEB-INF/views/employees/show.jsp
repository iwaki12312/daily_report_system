<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<c:import url="/WEB-INF/views/layout/app.jsp">
    <c:param name="content">
        <c:choose>
            <c:when test="${employee != null}">
                <h2>id : ${employee.id} の従業員情報　詳細ページ</h2>

                <table>
                    <tbody>
                        <tr>
                            <th>社員番号</th>
                            <td><c:out value="${employee.code}" /></td>
                        </tr>
                        <tr>
                            <th>氏名</th>
                            <td><c:out value="${employee.name}" /></td>
                        </tr>
                        <tr>
                            <th>権限</th>
                            <td>
                                    <c:if test="${employee.position_flag == 0}">一般</c:if>
                                    <c:if test="${employee.position_flag == 1}">管理者</c:if>
                                    <c:if test="${employee.position_flag == 2}">課長</c:if>
                                    <c:if test="${employee.position_flag == 3}">部長</c:if>
                            </td>
                        </tr>
                        <tr>
                            <th>登録日時</th>
                            <td>
                                <fmt:formatDate value="${employee.created_at}" pattern="yyyy-MM-dd HH:mm:ss" />
                            </td>
                        </tr>
                        <tr>
                            <th>更新日時</th>
                            <td>
                                <fmt:formatDate value="${employee.updated_at}" pattern="yyyy-MM-dd HH:mm:ss" />
                            </td>
                        </tr>
                    </tbody>
                </table>

                <p><a href="<c:url value='/attendance/record?id=${employee.id}&month=${month}&year=${year}' />">この従業員の出勤情報を見る</a></p>
                <span><a href="<c:url value='/employees/edit?id=${employee.id}' />">この従業員情報を編集する</a></span>
            </c:when>
            <c:otherwise>
                <h2>お探しのデータは見つかりませんでした。</h2>
            </c:otherwise>
        </c:choose>

        <p><a href="<c:url value='/employees/index' />">一覧に戻る</a></p>
    </c:param>
</c:import>