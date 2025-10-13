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
                <p class="concert-title">${concert.title}</p>
                <p class="concert-venue">${concert.venueName}</p>
                <p class="seat-inform">${seat.section}열 ${seat.number}</p>
            </div>
            <div class="pay-container">
                <p class="price">${seat.price}</p>
                <button class="pay-btn" disabled>결제하기</div>
            </div>
        </div>
    </div>

    <script>
        payMethod = true;
        document.querySelector('.pay-btn').addEventListener('click', () => {
            if (!payMethod) {
                alert('결제 방법을 선택해주세요.');
                return;
            }
            window.location.href = `${contextPath}/pay?seatId=\${seat.id}&concertId=\${concert.id}`;
        });
    </script>
</body>
</html>