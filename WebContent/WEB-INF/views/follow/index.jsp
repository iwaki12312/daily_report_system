<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:import url="../layout/app.jsp">
    <c:param name="content">
        <c:if test="${flush != null}">
            <div id="flush_success">
                <c:out value="${flush}"></c:out>
            </div>
        </c:if>
        <div class="search">
            <h2>従業員　一覧</h2>
            <p>氏名で検索</p>
            <form action="index">
                <input type="text" name="search" value="${name}">
                <input type="submit" value="検索">
                <p>フォローしている従業員のみ</p>

                <c:choose>
                    <c:when test="${followsearch == 'on'}">
                        <input type="checkbox" name="followsearch" value="on" checked=true>
                    </c:when>
                    <c:otherwise>
                        <input type="checkbox" name="followsearch" value="on">
                    </c:otherwise>
                </c:choose>
                <input type="hidden" name="followsearch" value="off">
                <p>フォローされている従業員のみ</p>
                <c:choose>
                    <c:when test="${followersearch == 'on'}">
                        <input type="checkbox" name="followersearch" value="on" checked=true>
                    </c:when>
                    <c:otherwise>
                         <input type="checkbox" name="followersearch" value="on">
                    </c:otherwise>
                </c:choose>
                <input type="hidden" name="followersearch" value="off">
            </form>
        </div>
        <table id="employee_list">
            <tbody>
                <tr>
                    <th>社員番号</th>
                    <th>氏名</th>
                    <th>フォロー</th>
                </tr>
                <c:forEach var="employee" items="${employees}" varStatus="status">
                  <tr class="row${status.count % 2}">
                      <td><c:out value="${employee.code}" /></td>
                      <td><c:out value="${employee.name}" /></td>
                      <td>
                         <c:choose>
                            <c:when test="${followCheck[status.count - 1] == 0}">
                               <form class="follow" method="POST" action="<c:url value='/follow?indexId=${employee.id}&page=${page}&search=${name}&followsearch=${followsearch}&followersearch=${followersearch}' />">
                                  <button>フォローする</button>
                               </form>
                            </c:when>
                            <c:when test="${followCheck[status.count - 1] == 1}">
                               <form class="follow" method="POST" action="<c:url value='/follow/remove?indexId=${employee.id}&page=${page}&search=${name}&followsearch=${followsearch}&followersearch=${followersearch}' />">
                                 <button>フォロー解除</button>
                               </form>
                            </c:when>
                          </c:choose>
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
                        <a href="<c:url value='/follow/index?page=${i}&search=${name}&followsearch=${followsearch}&followersearch=${followersearch}' />"><c:out value="${i}" /></a>&nbsp;
                    </c:otherwise>
                </c:choose>
            </c:forEach>
        </div>
        <p><a href="<c:url value='/' />">トップページへ戻る</a></p>

    </c:param>
</c:import>