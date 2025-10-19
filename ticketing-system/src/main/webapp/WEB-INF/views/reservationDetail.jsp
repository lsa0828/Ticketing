<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<c:set var="c" value="${reservedConcert}" />
<html>
<head>
    <title>Ticketing</title>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="<c:url value='/resources/css/layout.css' />">
    <link rel="stylesheet" href="<c:url value='/resources/css/reservation-detail.css' />">
</head>
<body>
    <%@ include file="/WEB-INF/views/common/header.jspf" %>
    <div class="main-container">
        <div class="reservation-container">
            <div class="concert-container">
                <div class="img-container">
                    <img src="${contextPath}${c.imageUrl}" alt="${c.title}">
                </div>
                <div class="concert">
                    <p class="concert-title">${c.title}</p>
                    <p class="concert-venue">${c.venueName}</p>
                </div>
            </div>
            <hr />
            <div class="inform-container">
                <div class="seat-container">
                    <p class="seat-title">좌석</p>
                    <p class="seat">${c.section}열 ${c.number}</p>
                </div>
                <div class="price-container">
                    <p class="price-title">가격</p>
                    <p class="price">0원</p>
                </div>
                <div class="date-container">
                    <p class="date-title">예매일</p>
                    <p class="reserved-at">${c.reservedAtFormatted}</p>
                </div>
            </div>
            <c:if test="${c.reservationStatus == 'PAID'}">
                <div class="status">
                    <button>예매 취소하기</button>
                </div>
            </c:if>
        </div>
    </div>

    <script>
        const price = parseInt('${c.price}', 10);
        document.querySelector('.price').textContent = price.toLocaleString() + '원';
    </script>
</body>
</html>