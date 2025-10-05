<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<html>
<head>
    <title>Login</title>
    <meta charset="UTF-8">
    <link rel="stylesheet" href="<c:url value='/resources/css/layout.css' />">
    <link rel="stylesheet" href="<c:url value='/resources/css/auth-form.css' />">
</head>
<body>
    <%@ include file="/WEB-INF/views/common/header.jspf" %>
    <div class="main-container">
        <p class="form-title">로그인</p>

        <div class="form-container">
            <form action="${contextPath}/login" method="post">
                <label for="id">아이디</label>
                <input type="text" id="id" name="id" required />

                <label for="password">비밀번호</label>
                <input type="password" id="password" name="password" required />

                <label>
                    <input type="checkbox" name="rememberMe" />
                </label>

                <button type="submit">로그인</button>

                <c:if test="${param.error eq 'true'}">
                    <p class="error">아이디 또는 비밀번호가 올바르지 않습니다.</p>
                </c:if>
            </form>
        </div>

        <p class="refer">아직 회원이 아니신가요? <a href="${contextPath}/register">회원가입</a></p>
    </div>
</body>
</html>