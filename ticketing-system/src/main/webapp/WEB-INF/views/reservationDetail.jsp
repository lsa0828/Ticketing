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
                <div>
                    <p class="seat-title">좌석</p>
                    <p class="seat">${c.section}열 ${c.number}</p>
                </div>
                <div>
                    <p class="concert-date-title">공연일</p>
                    <p class="concert-date">${c.dateFormatted}</p>
                </div>
            </div>
            <div class="payment-container">
                <c:if test="${not empty c.couponName}">
                    <div>
                        <p class="payment-title">쿠폰 사용</p>
                        <p class="coupon">'${c.couponName}'</p>
                    </div>
                </c:if>
                <c:if test="${not empty c.point and c.point > 0}">
                    <div>
                        <p class="payment-title">포인트 사용</p>
                        <p class="point">${c.point} point</p>
                    </div>
                </c:if>
                <div>
                    <p class="payment-title">결제 금액</p>
                    <p class="price">0원</p>
                </div>
            </div>
            <div class="status">
                <div class="reservation-date-container">
                    <p class="reservation-date-title">예매일</p>
                    <p class="reserved-at">${c.reservedAtFormatted}</p>
                </div>
                <button type="button" onclick="confirmCancel(${c.reservationId})">
                    예매 취소하기
                </button>
            </div>
        </div>
    </div>

    <script>
        const price = parseInt('${c.paidPrice}', 10);
        document.querySelector('.price').textContent = price.toLocaleString() + '원';

        function confirmCancel(reservationId) {
            const confirmed = confirm("예매를 취소하시겠습니까?");
            if (confirmed) {
                window.location.href = `${contextPath}/refund?reservationId=\${reservationId}`;
            }
        }
    </script>
    <c:if test="${not empty error}">
        <script>
            alert("${error}");
        </script>
    </c:if>
</body>
</html>