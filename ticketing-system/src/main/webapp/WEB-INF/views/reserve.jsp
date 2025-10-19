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
                    <div class="seat-container">
                        <p class="seat-title">좌석</p>
                        <p class="seat-inform">${seat.section}열 ${seat.number}</p>
                    </div>
                    <div class="price-container">
                        <p class="price-title">총 결제 금액</p>
                        <p class="price">0원</p>
                    </div>
                </div>
            </div>
            <hr />
            <div class="pay-container">
                <p class="pay-title">결제 방법 선택</p>
                <div class="pay-selection">
                    <button class="pay-method" data-pay-id="1">결제1</button>
                    <button class="pay-method" data-pay-id="2">결제2</button>
                </div>
                <button class="pay-btn" disabled>결제하기</button>
            </div>
        </div>
    </div>

    <script>
        const price = parseInt('${seat.price}', 10);
        document.querySelector('.price').textContent = price.toLocaleString() + '원';

        selectedMethod = null;

        document.querySelectorAll('.pay-method').forEach(method => {
            method.addEventListener('click', () => {
                if (selectedMethod) {
                    selectedMethod.classList.remove('selected');
                }
                if (selectedMethod !== method) {
                    selectedMethod = method;
                    method.classList.add('selected');
                    document.querySelector('.pay-btn').disabled = false;
                } else {
                    selectedMethod = null;
                    document.querySelector('.pay-btn').disabled = true;
                }
            });
        });

        document.querySelector('.pay-btn').addEventListener('click', () => {
            if (!selectedMethod) {
                alert('결제 방법을 선택해주세요.');
                return;
            }
            const payId = selectedMethod.dataset.payId;
            fetch(`${contextPath}/reserve`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({
                    seatId: '${seat.id}',
                    concertId: '${concert.id}',
                    payId: payId
                })
            })
            .then(response => {
                if (!response.ok) {
                    return response.json().then(err => {
                        throw new Error(err.error || '서버 오류');
                    });
                }
                return response.json();
            })
            .then(data => {
                if (data.reservationId) {
                    window.location.href = `${contextPath}/reservation/complete?reservationId=\${data.reservationId}`;
                }
            })
            .catch(err => {
                console.error(err);
                alert('결제 중 오류가 발생했습니다.');
            });
        });
    </script>
</body>
</html>