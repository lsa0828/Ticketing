<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<html>
<head>
    <title>Ticketing</title>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="<c:url value='/resources/css/layout.css' />">
    <link rel="stylesheet" href="<c:url value='/resources/css/concert.css' />">
</head>
<body>
    <%@ include file="/WEB-INF/views/common/header.jspf" %>
    <div class="main-container">
        <div class="concert-container">
            <div class="seat-container">
                <div class="seat-inner">
                    <p class="seat-front">Front</p>
                    <div class="seat-group" style="width: ${maxPos.x + 30}px; height: ${maxPos.y + 30}px;">
                        <c:forEach var="s" items="${seats}">
                            <div class="seat ${s.status eq 'AVAILABLE' ? 'available' : 'sold'}"
                                    style="left: ${s.posX}px; top: ${s.posY}px;"
                                    data-seat-id="${s.seatId}" data-concert-id="${s.concertId}" data-price="${s.price}">
                                ${s.section}${s.number}
                            </div>
                        </c:forEach>
                    </div>
                </div>
            </div>
            <div class="inform-container">
                <img src="${contextPath}${concert.imageUrl}" alt="${concert.title}">
                <p class="concert-title">${concert.title}</p>
                <p class="concert-venue">[${concert.venueName}]</p>
                <p class="concert-date">${concert.dateFormatted}</p>
                <div class="price-container">
                    <p class="price-title">결제 금액</p>
                    <p class="price">0원</p>
                </div>
                <c:choose>
                    <c:when test="${concert.bookable}">
                        <button class="book-btn" disabled>예매하기</button>
                    </c:when>
                    <c:otherwise>
                        <button class="not-book-btn" disabled>예매불가</button>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
    </div>
    <script>
        let selectedSeat = null;

        document.querySelectorAll('.seat.available').forEach(seat => {
            seat.addEventListener('click', () => {
                if (selectedSeat) {
                    selectedSeat.classList.remove('selected');
                }
                if (selectedSeat !== seat) {
                    selectedSeat = seat;
                    seat.classList.add('selected');
                    document.querySelector('.book-btn').disabled = false;

                    const price = parseInt(seat.dataset.price, 10);
                    document.querySelector('.price').textContent = price.toLocaleString() + '원';
                } else {
                    selectedSeat = null;
                    document.querySelector('.book-btn').disabled = true;
                    document.querySelector('.price').textContent = '0원';
                }
            });
        });

        document.querySelector('.book-btn').addEventListener('click', () => {
            if (!selectedSeat) {
                alert('좌석을 선택해주세요.');
                return;
            }
            const seatId = selectedSeat.dataset.seatId;
            const concertId = selectedSeat.dataset.concertId;
            window.location.href = `${contextPath}/reserve?seatId=\${seatId}&concertId=\${concertId}`;
        });
    </script>
    <c:if test="${not empty error}">
        <script>
            alert("${error}");
        </script>
    </c:if>
</body>
</html>