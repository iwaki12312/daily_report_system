<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:import url="../layout/app.jsp">
    <c:param name="content">
        <c:if test="${flush != null}">
            <div id="flush_success">
                <c:out value="${flush}"></c:out>
            </div>
        </c:if>
        <h2 class = "top">日報管理システムへようこそ</h2><br>
        <c:if test="${rejectReportCount > 0}">
           <span class = "approval">！差し戻しのレポートがあります　　</span>
           <button onclick="location.href='reports/reject?id=${employee.id}'">確認する</button><br>
        </c:if>
        <c:if test="${editingReportCount > 0}">
           <span class = "approval">！編集中のレポートがあります　　　</span>
           <button onclick="location.href='reports/editing?id=${employee.id}'">確認する</button><br>
        </c:if>
        <c:if test="${employee.position_flag == 2 && sectionManagerCount > 0}">
           <span class = "approval">！課長承認待ちのレポートがあります</span>
           <button onclick="location.href='approval/reports?position=${employee.position_flag}'">確認する</button><br>
        </c:if>
        <c:if test="${employee.position_flag == 3 && managerCount > 0}">
           <span class = "approval">！部長承認待ちのレポートがあります</span>
           <button onclick="location.href='approval/reports?position=${employee.position_flag}'">確認する</button><br>
        </c:if>
        <br>
        <c:choose>
            <c:when test="${attendance == 0}">
                <form class="attendance" method="POST" action="<c:url value='/attendance/in' />">
                    <button>出勤</button>
                </form>
            </c:when>
            <c:when test="${attendance == 1}">
                <form class="attendance" method="POST" action="<c:url value='/attendance/out' />">
                    <button>退勤</button>
                </form>
                <form method="post" name = "form1" action="<c:url value='/attendance/in' />">
                    <a href="<c:url value='/attendance/in' />" onclick="document.form1.submit();return false;">出勤を取り消す</a>
                </form>
            </c:when>
            <c:when test="${attendance == 2}">
                <form method="post" name = "form1" action="<c:url value='/attendance/out' />">
                    <a href="<c:url value='/attendance/out' />" onclick="document.form1.submit();return false;">退勤を取り消す</a>
                </form>
            </c:when>
        </c:choose>
        <p><a href="<c:url value='/attendance/record?id=${employee.id}&month=${month}&year=${year}' />">自分の出勤情報を見る</a></p>
<!--
         <form class="attendance" method="POST" action="<c:url value='/testattendance' />">
                    <button>出勤退勤テストデータ挿入</button>
                </form>
-->
<!--
         <form class="attendance" method="POST" action="<c:url value='/testdeta' />">
                    <button>従業員テストデータ挿入</button>
                </form>
-->
        <br/> <br/>
        <h3>【自分の日報　一覧】</h3>
        <table id="report_list">
            <tbody>
                <tr>
                    <th class="report_name">氏名</th>
                    <th class="report_date">日付</th>
                    <th class="report_title">タイトル</th>
                    <th class="report_action">操作</th>
                </tr>
                <c:forEach var="report" items="${reports}" varStatus="status">
                    <tr class="row${status.count % 2}">
                        <td class="report_name"><c:out value="${report.employee.name}" /></td>
                        <td class="report_date"><fmt:formatDate value='${report.report_date}' pattern='yyyy-MM-dd' /></td>
                        <td class="report_title">${report.title}</td>
                        <td class="report_action"><a href="<c:url value='/reports/show?id=${report.id}' />">詳細を見る</a></td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>

        <div id="pagination">
            （全 ${reports_count} 件）<br />
            <c:forEach var="i" begin="1" end="${((reports_count - 1) / 15) + 1}" step="1">
                <c:choose>
                    <c:when test="${i == page}">
                        <c:out value="${i}" />&nbsp;
                    </c:when>
                    <c:otherwise>
                        <a href="<c:url value='/?page=${i}' />"><c:out value="${i}" /></a>&nbsp;
                    </c:otherwise>
                </c:choose>
            </c:forEach>
        </div>
        <p><a href="<c:url value='/reports/new' />">新規日報の登録</a></p><br/>
    </c:param>
</c:import>