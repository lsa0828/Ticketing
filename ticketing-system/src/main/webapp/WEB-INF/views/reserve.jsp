<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<html>
<head>
    <title>Ticketing</title>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="<c:url value='/resources/css/layout.css' />">
    <link rel="stylesheet" href="<c:url value='/resources/css/reserve.css' />">
    <script src="https://cdn.iamport.kr/js/iamport.payment-1.2.0.js"></script>
</head>
<body>
    <%@ include file="/WEB-INF/views/common/header.jspf" %>
    <div class="main-container">
        <div class="reserve-container">
            <div class="inform-container">
                <div class="img-container">
                    <img src="${contextPath}${concert.imageUrl}" alt="${concert.title}">
                </div>
                <div class="inform">
                    <p class="concert-title">${concert.title}</p>
                    <div class="venue-container">
                        <p class="venue-title">장소</p>
                        <p class="concert-venue">${concert.venueName}</p>
                    </div>
                    <div class="date-container">
                        <p class="date-title">일자</p>
                        <p class="date">${concert.dateFormatted}</p>
                    </div>
                    <div class="seat-container">
                        <p class="seat-title">좌석</p>
                        <p class="seat-inform">${seat.section}열 ${seat.number}</p>
                    </div>
                    <div class="price-container">
                        <p class="price-title">결제 금액</p>
                        <p class="price">0원</p>
                        <p class="discounted-price" style="display: none;">0원</p>
                    </div>
                </div>
            </div>
            <hr />
            <div class="payment-container">
                <p class="payment-title">결제 방법 선택</p>
                <div class="payment-selection">
                    <button class="payment-method" id="useCouponBtn">쿠폰 적용</button>
                    <button class="payment-method" id="usePointBtn">포인트 사용</button>
                    <button class="payment-method" id="useIMPBtn">아임포트 결제</button>
                </div>
                <div class="payment">
                    <div id="couponContainer" style="display: none;">
                        <div id="couponNotice" class="coupon-notice">선택된 쿠폰: 없음</div>
                        <div id="couponList" class="coupon-list">
                            <c:forEach var="cp" items="${coupons}">
                                <div class="coupon-item" data-id="${cp.memberCouponId}" data-name="${cp.name}" data-discounted-price="${cp.discountedPrice}">
                                    <div class="coupon-name">${cp.name}</div>
                                    <div class="coupon-expires-at">~ ${cp.expiresAt}</div>
                                </div>
                            </c:forEach>
                        </div>
                    </div>
                    <div id="pointContainer" style="display: none;">
                        <div>
                            <label for="usePointInput">사용할 포인트:</label>
                            <input type="number" id="usePointInput" step="100" min="0" />
                            <button type="button" id="applyPointBtn">적용</button>
                        </div>
                        <span id="pointNotice"></span>
                    </div>
                </div>
                <div id="realPaymentAmount">0원 결제</div>
                <div class="payment-btn-container">
                    <c:choose>
                        <c:when test="${concert.bookable}">
                            <button class="payment-btn" disabled>결제하기</button>
                        </c:when>
                        <c:otherwise>
                            <button class="not-payment-btn" disabled>결제불가</button>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>
        </div>
    </div>

    <script>
        const price = '${seat.price}';
        const point = '${point}';
        const concertId = '${concert.id}';
        const seatId = '${seat.id}';
        const contextPath = '${contextPath}';
    </script>
</body>
<script src="<c:url value='/resources/js/reserve.js' />"></script>
</html>