<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:if test="${errors != null}">
    <div id="flush_error">
        入力内容にエラーがあります。<br />
        <c:forEach var="error" items="${errors}">
            ・<c:out value="${error}" /><br />
        </c:forEach>

    </div>
</c:if>
<label for="report_date">日付</label><br />
<input type="date" name="report_date" value="<fmt:formatDate value='${report.report_date}' pattern='yyyy-MM-dd' />" />
<br /><br />

<label for="name">氏名</label><br />
<c:out value="${sessionScope.login_employee.name}" />
<br /><br />

<label for="title">タイトル</label><br />
<input type="text" name="title" value="${report.title}" />
<br /><br />

<label for="content">内容</label><br />
<textarea name="content" rows="10" cols="50">${report.content}</textarea>
<br /><br />

<label for="name">取引先</label><br />
<select name="client">
                <option value="">無し</option>
                <c:forEach var="client" items="${clients}">
                     <c:if test="${client.delete_flag == 0 || report.client == client}">
                        <option value="${client.id}"<c:if test="${report.client == client}">selected</c:if>><c:out value="${client.name}" /></option>
                     </c:if>
                </c:forEach>
              </select>
<br /><br />

<label for="content">商談状況</label><br />
<textarea name="negotiation_status" rows="10" cols="50">${report.negotiation_status}</textarea>
<br /><br />

<input type="hidden" name="_token" value="${_token}" />
<input type="hidden" name="approval_status" value="${report.approval_status}" />
<c:if test="${report.content == null || report.approval_status == 0 || report.approval_status == 2 || report.approval_status == 4}">
    <input type="submit" name="cmd" value="保存"/>
</c:if>
<c:if test="${report.content == null || report.approval_status == 0}">
    <input type="submit" name="cmd" value="提出する"/>
</c:if>
<c:if test="${report.approval_status == 2 || report.approval_status == 4}">
    <input type="submit" name="cmd" value="再提出する"/>
</c:if>
<c:if test="${report.approval_status == 1 || report.approval_status == 3}">
    <input type="submit" name="cmd" value="取り下げる"/>
</c:if>