<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:import url="../layout/app.jsp">
    <c:param name="content">

    <h2><c:out value="${employee.name}"/>&nbsp;さんの出勤情報一覧</h2>
    <h3><c:out value="${year}" />年<c:out value="${month}" />月</h3>

    <table id="attendance_list">
            <tbody>
                <tr>
                    <th>日時</th>
                    <th>出勤時間</th>
                    <th>退勤時間</th>
                </tr>
                <c:forEach var="record" items="${records}" varStatus="status">
                    <tr class="row${status.count % 2}">
                        <c:if test="${record.inTime != null}">
                            <td><c:out value="${record.date}" /></td>
                            <td><fmt:formatDate value="${record.inTime}" pattern="HH:mm:ss" /></td>
                                 <c:if test="${record.outTime != null}">
                            <td><fmt:formatDate value="${record.outTime}" pattern="HH:mm:ss" /></td>
                            </c:if>
                                 <c:if test="${record.outTime == null}">
                            <td>データが存在しません</td>
                            </c:if>
                        </c:if>
                    </tr>
                </c:forEach>
            </tbody>
        </table>

        <div id="pagination">
            <c:if test="${year < currentYear}">
                <a href="<c:url value='/attendance/record?id=${employee.id}&month=${month}&year=${year + 1}' />">一年先に進む</a>
            </c:if>
               <c:forEach var="i" begin="1" end="12" step="1">

                    <c:if test="${i < 10}"><a href="<c:url value='/attendance/record?id=${employee.id}&month=0${i}&year=${year}' />"><c:out value="${i}" />月</a></c:if>
                    <c:if test="${i >= 10}"><a href="<c:url value='/attendance/record?id=${employee.id}&month=${i}&year=${year}' />"><c:out value="${i}" />月</a></c:if>
                </c:forEach>
            <c:if test="${year > 2010 }">
                 <a href="<c:url value='/attendance/record?id=${employee.id}&month=${month}&year=${year - 1}' />">一年前に戻る</a>
            </c:if>
        </div>
        <p><a href="<c:url value='/' />">トップページへ戻る</a></p>
    </c:param>
</c:import>