<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="ja">
    <head>
        <meta charset="UTF-8">
        <title>日報管理システム</title>
        <link rel="stylesheet" href="<c:url value='/css/reset.css' />">
        <link rel="stylesheet" href="<c:url value='/css/style.css' />">
    </head>
    <body>
        <div id="wrapper">
            <div id="header">
                <div id="header_menu">
                    <h1><a href="<c:url value='/' />">日報管理システム</a></h1>&nbsp;&nbsp;&nbsp;
                    <c:if test="${sessionScope.login_employee != null}">
                        <c:if test="${sessionScope.login_employee.position_flag != 0}">
                            <a href="<c:url value='/employees/index' />">従業員管理</a>&nbsp;
                        </c:if>
                        <a href="<c:url value='/reports/index' />">日報管理</a>&nbsp;
                        <a href="<c:url value='/follow/index' />">従業員をフォローする</a>&nbsp;
                        <a href="<c:url value='/follow/followemployee' />">フォロー&nbsp;<c:out value="${followCount}"/>&nbsp;人</a>&nbsp;
                        <a href="<c:url value='/follow/followeremployee' />">フォロワー&nbsp;<c:out value="${followerCount}"/>&nbsp;人</a>&nbsp;
                    </c:if>
                </div>
                <c:if test="${sessionScope.login_employee != null}">
                    <div id="employee_name">
                        <c:out value="${sessionScope.login_employee.name}" />&nbsp;さん&nbsp;&nbsp;&nbsp;
                        <a href="<c:url value='/logout' />">ログアウト</a>
                    </div>
                </c:if>
            </div>
            <div id="content">
                ${param.content}
            </div>
            <div id="footer">
                by Iwaki Matsumoto.
            </div>
        </div>
    </body>
</html>