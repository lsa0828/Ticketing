<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<html>
<head>
    <title>Sign up</title>
    <meta charset="UTF-8">
    <link rel="stylesheet" href="<c:url value='/resources/css/layout.css' />">
    <link rel="stylesheet" href="<c:url value='/resources/css/auth-form.css' />">
</head>
<body>
    <%@ include file="/WEB-INF/views/common/header.jspf" %>
    <div class="main-container">
        <p class="form-title">회원가입</p>

        <div class="form-container">
            <form action="${contextPath}/register" method="post">
                <div class="form-row">
                    <label for="id">아이디</label>
                    <input type="text" id="id" name="id" required />
                </div>

                <div class="form-row">
                    <label for="password">비밀번호</label>
                    <input type="password" id="password" name="password" required />
                </div>

                <div class="form-row">
                    <label for="passwordConfirm">비밀번호 확인</label>
                    <input type="password" id="passwordConfirm" name="passwordConfirm" required />
                </div>

                <button type="submit">회원가입</button>

                <p class="error ${not empty error ? 'visible' : 'hidden'}">${error}</p>
            </form>
        </div>

        <p class="refer"><a href="${contextPath}/login">로그인 페이지로 돌아가기</a></p>
    </div>
</body>
</html>