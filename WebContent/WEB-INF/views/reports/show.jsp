<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:import url="/WEB-INF/views/layout/app.jsp">
    <c:param name="content">
        <c:choose>
            <c:when test="${report != null}">
                <h2>日報　詳細ページ</h2>

                <table>
                    <tbody>
                        <tr>
                            <th>氏名</th>
                            <td><c:out value="${report.employee.name}" /></td>
                        </tr>
                        <tr>
                            <th>日付</th>
                            <td><fmt:formatDate value="${report.report_date}" pattern="yyyy-MM-dd" /></td>
                        </tr>
                        <tr>
                            <th>内容</th>
                            <td>
                                <pre><c:out value="${report.content}" /></pre>
                            </td>
                        </tr>
                        <tr>
                            <th>出勤時刻</th>
                            <td>
                            <c:choose>
                                <c:when test="${attendance.inTime == null}">
                                    <pre>出勤時刻は登録されていません</pre>
                                </c:when>
                                <c:otherwise>
                                    <fmt:formatDate value="${attendance.inTime}" pattern="yyyy-MM-dd HH:mm:ss" />
                                </c:otherwise>
                            </c:choose>

                            </td>
                        </tr>
                        <tr>
                            <th>退勤時刻</th>
                            <td>
                            <c:choose>
                                <c:when test="${attendance.outTime == null}">
                                    <pre>退勤時刻は登録されていません</pre>
                                </c:when>
                                <c:otherwise>
                                    <fmt:formatDate value="${attendance.outTime}" pattern="yyyy-MM-dd HH:mm:ss" />
                                </c:otherwise>
                            </c:choose>
                            </td>
                        </tr>
                        <tr>
                            <th>課長承認</th>
                            <td>
                            <c:choose>
                                <c:when test="${report.section_manager_approval != null}">
                                   <pre><c:out value="${report.section_manager_approval.name}" /></pre>
                                </c:when>
                                <c:otherwise>
                                   <pre>未承認</pre>
                                </c:otherwise>
                            </c:choose>
                            </td>
                        </tr>
                        <tr>
                            <th>部長承認</th>
                            <td>
                            <c:choose>
                                <c:when test="${report.manager_approval != null}">
                                   <pre><c:out value="${report.manager_approval.name}" /></pre>
                                </c:when>
                                <c:otherwise>
                                   <pre>未承認</pre>
                                </c:otherwise>
                            </c:choose>

                            </td>
                        </tr>
                        <tr>
                            <th>登録日時</th>
                            <td>
                                <fmt:formatDate value="${report.created_at}" pattern="yyyy-MM-dd HH:mm:ss" />
                            </td>
                        </tr>
                        <tr>
                            <th>更新日時</th>
                            <td>
                                <fmt:formatDate value="${report.updated_at}" pattern="yyyy-MM-dd HH:mm:ss" />
                            </td>
                        </tr>
                    </tbody>
                </table>

                <c:if test="${sessionScope.login_employee.id == report.employee.id}">
                    <p><a href="<c:url value="/reports/edit?id=${report.id}" />">この日報を編集する</a></p>
                </c:if>

            </c:when>
            <c:otherwise>
                <h2>お探しのデータは見つかりませんでした。</h2>
            </c:otherwise>
        </c:choose>

        <span><a href="<c:url value="/reports/index" />">一覧に戻る</a></span>

        <c:choose>
          <c:when test="${followDeta == 0}">
             <form class="follow" method="POST" action="<c:url value='/follow?reportId=${report.id}' />">
                <button>この社員をフォローする</button>
             </form>
          </c:when>
          <c:when test="${followDeta == 1}">
            <form class="follow" method="POST" action="<c:url value='/follow/remove?reportId=${report.id}' />">
                <button>フォローを解除する</button>
             </form>
          </c:when>
        </c:choose>
        <c:if test="${login_employee.id != employee.id}">
            <c:if test="${login_employee.position_flag == 2 && report.section_manager_approval == null}">
                <form class="follow" method="POST" action="<c:url value='/approval?reportId=${report.id}' />">
                      <button>課長承認する</button>
                </form>
            </c:if>
            <c:if test="${login_employee.position_flag == 3 && report.section_manager_approval != null && report.manager_approval == null}">
                <form class="follow" method="POST" action="<c:url value='/approval?reportId=${report.id}' />">
                      <button>部長承認する</button>
                </form>
            </c:if>
        </c:if>
    </c:param>
</c:import>