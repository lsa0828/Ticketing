<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<html>
<head>
    <title>Ticketing</title>
    <link rel="stylesheet" href="<c:url value='/resources/css/concert.css' />">
</head>
<body>
    <h1>Concert Ticketing</h1>
    <div class="concert-container">
        <div class="concert-wrapper">
            <button id="prev" class="arrow prev disabled">&#9664;</button>
            <div id="concert-list" class="concert-list"></div>
            <button id="next" class="arrow next">&#9654;</button>
        </div>
        <div id="page-dots" class="page-dots"></div>
    </div>

    <script>
        const concertsData = [
            <c:forEach var="c" items="${concerts}">
                {id:${c.id}, title:"${c.title}", imageUrl:"${contextPath}"+"${c.imageUrl}"},
            </c:forEach>
        ];
    </script>
    <script src="<c:url value='/resources/js/concert.js' />"></script>
</body>
</html>