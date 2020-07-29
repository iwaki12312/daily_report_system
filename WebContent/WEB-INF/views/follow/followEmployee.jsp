<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:import url="../layout/app.jsp">
    <c:param name="content">

        <h2>フォローしている従業員一覧</h2>
        <table id="employee_list">
            <tbody>
                <tr>
                    <th>社員番号</th>
                    <th>氏名</th>
                    <th>操作</th>
                    <th>フォロー</th>

                </tr>
                <c:forEach var="employee" items="${followEmployees}" varStatus="status">
                    <tr class="row${status.count % 2}">
                        <td><c:out value="${employee.code}" /></td>
                        <td><c:out value="${employee.name}" /></td>
                        <td>
                            <c:if test="${login_employee.position_flag == 1 }">
                                <a href="<c:url value='/employees/show?id=${employee.id}' />">社員情報を見る</a><br>
                            </c:if>
                            <a href="<c:url value='/follow/reportindex?id=${employee.id}' />">この従業員の日報一覧を見る</a>
                        </td>
                        <td>
                                <form class="follow" method="POST" action="<c:url value='/follow/remove?followId=${employee.id}' />">
                                    <button>フォロー解除</button>
                                 </form>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>


        <div id="pagination">
            （全 ${employees_count} 件）<br />
            <c:forEach var="i" begin="1" end="${((employees_count - 1) / 15) + 1}" step="1">
                <c:choose>
                    <c:when test="${i == page}">
                        <c:out value="${i}" />&nbsp;
                    </c:when>
                    <c:otherwise>
                        <a href="<c:url value='/employees/index?page=${i}' />"><c:out value="${i}" /></a>&nbsp;
                    </c:otherwise>
                </c:choose>
            </c:forEach>
        </div>
        <p><a href="<c:url value='/' />">トップページへ戻る</a></p>


    </c:param>
</c:import>
