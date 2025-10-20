<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<html>
<head>
    <title>Ticketing</title>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="<c:url value='/resources/css/layout.css' />">
    <link rel="stylesheet" href="<c:url value='/resources/css/main.css' />">
</head>
<body>
    <%@ include file="/WEB-INF/views/common/header.jspf" %>
    <div class="main-container">
        <div class="concert-wrapper">
            <button id="prev" class="arrow prev disabled">&#9664;</button>
            <div id="concert-list" class="concert-list"></div>
            <button id="next" class="arrow next">&#9654;</button>
        </div>
        <div id="page-dots" class="page-dots"></div>
    </div>

    <c:if test="${not empty error}">
        <script>
            alert("${error}");
        </script>
    </c:if>

    <script>
        const concertsData = [
            <c:forEach var="c" items="${concerts}">
                {
                    id:${c.concertId},
                    title:"${c.title}",
                    days: "${c.daysUntilConcert}",
                    disable: ${c.daysUntilConcert < 0},
                    imageUrl:"${contextPath}${c.imageUrl}",
                    soldCount: ${c.soldCount},
                    totalCount: ${c.totalCount}
                },
            </c:forEach>
        ];
        const contextPath = "${contextPath}";
        const isLoggedIn = ${sessionScope.loginMember != null ? 'true' : 'false'};
    </script>
    <script src="<c:url value='/resources/js/main.js' />"></script>
    <script src="<c:url value='/resources/js/loginConfirm.js' />"></script>
</body>
</html>