<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<html>
<head>
    <title>Ticketing</title>
    <meta charset="UTF-8">
    <link rel="stylesheet" href="<c:url value='/resources/css/layout.css' />">
    <link rel="stylesheet" href="<c:url value='/resources/css/concert.css' />">
</head>
<body>
    <%@ include file="/WEB-INF/views/common/header.jspf" %>
    <div class="main-container">
        <div class="concert-container">
            <div class="inform-container">
                <img src="${contextPath}${concert.imageUrl}" alt="${concert.title}">
                <p class="concert-title">${concert.title}</p>
                <p class="concert-venue">[${concert.venueName}]</p>
            </div>
            <div class="seat-container">
                <div class="seat-inner">
                    <p class="seat-front">Front</p>
                    <div class="seat-group" style="width: ${maxPos.x + 30}px; height: ${maxPos.y + 30}px;">
                        <c:forEach var="s" items="${seats}">
                            <div class="seat ${s.status eq 'AVAILABLE' ? 'available' : 'sold'}"
                                    style="left: ${s.posX}px; top: ${s.posY}px;">
                                ${s.section}${s.number}
                            </div>
                        </c:forEach>
                    </div>
                </div>
            </div>
        </div>
    </div>
</body>
</html>