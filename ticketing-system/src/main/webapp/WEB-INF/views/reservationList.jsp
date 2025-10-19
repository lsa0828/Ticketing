<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<html>
<head>
    <title>Ticketing</title>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="<c:url value='/resources/css/layout.css' />">
    <link rel="stylesheet" href="<c:url value='/resources/css/reservation-list.css' />">
</head>
<body>
    <%@ include file="/WEB-INF/views/common/header.jspf" %>
    <div class="main-container">
        <div class="reservation-container">
            <p class="reservation-title">예매 내역</p>
            <c:forEach var="c" items="${reservedConcerts}">
                <div class="reservation">
                    <div class="concert-container">
                        <p class="concert-title">${c.title}</p>
                        <div class="date-container">
                            <p class="date-title">예매일</p>
                            <p class="reserved-at">${c.reservedAtFormatted}</p>
                        </div>
                    </div>
                    <div class="status">
                        <c:choose>
                            <c:when test="${c.reservationStatus == 'PAID'}">
                                <a href="${contextPath}/reservation/complete?reservationId=${c.reservationId}">
                                    <button>보기</button>
                                </a>
                            </c:when>
                            <c:otherwise>
                                <p>예매 취소</p>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </div>
            </c:forEach>
        </div>
    </div>

    <script>
    </script>
</body>
</html>