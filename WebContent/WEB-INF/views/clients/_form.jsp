<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:if test="${errors != null}">
    <div id="flush_error">
        入力内容にエラーがあります。<br />
        <c:forEach var="error" items="${errors}">
            ・<c:out value="${error}" /><br />
        </c:forEach>

    </div>
</c:if>
<label for="code">取引先コード</label><br />
<input type="text" name="code" value="${client.code}" />
<br /><br />

<label for="name">名称</label><br />
<input type="text" name="name" value="${client.name}" />
<br /><br />

<label for="address">住所</label><br />
<input type="text" name="address" value="${client.address}" />
<br /><br />

<label for="tell">電話番号</label><br />
<input type="text" name="tell" value="${client.tell}"/>
<br /><br />

<input type="hidden" name="_token" value="${_token}" />
<button type="submit">投稿</button>