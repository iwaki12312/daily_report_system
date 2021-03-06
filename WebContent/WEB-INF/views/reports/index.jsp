<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:import url="/WEB-INF/views/layout/app.jsp">
    <c:param name="content">
        <c:if test="${flush != null}">
            <div id="flush_success">
                <c:out value="${flush}"></c:out>
            </div>
        </c:if>
        <div class="search">
          <h2>日報　一覧</h2>
          <form action="index" id="reportSearch">
          <p>承認ステータス</p>
              <select name="filter">
                <option value="">全て表示</option>
                <option value="sectionManager"<c:if test="${filter == 'sectionManager'}">selected</c:if>>課長承認待ち</option>
                <option value="manager"<c:if test="${filter == 'manager'}">selected</c:if>>部長承認待ち</option>
                <option value="approved"<c:if test="${filter == 'approved'}">selected</c:if>>承認済み</option>
                <c:if test="${login_employee.position_flag > 1}">
                  <option value="myApproved"<c:if test="${filter == 'myApproved'}">selected</c:if>>自分が承認した日報</option>
                </c:if>
              </select>
              <p>取引先</p>
              <select name="clientFilter">
                <option value="">全て表示</option>
                <c:forEach var="client" items="${clients}">
                   <option value="${client.id}"<c:if test="${clientFilter == client.id}">selected</c:if>><c:out value="${client.name}" /></option>
                </c:forEach>
              </select>
              <p>いいね！</p>
              <select name="likeFilter">
                  <option value="">全て表示</option>
                  <option value="on"<c:if test="${likeFilter == 'on'}">selected</c:if>>自分がいいねした日報</option>
              </select>
              <input type="submit" value="絞り込み">
          </form>

        </div>
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
                        <a href="<c:url value='/reports/index?page=${i}' />"><c:out value="${i}" /></a>&nbsp;
                    </c:otherwise>
                </c:choose>
            </c:forEach>
        </div>
        <p><a href="<c:url value='/reports/new' />">新規日報の登録</a></p>

    </c:param>
</c:import>