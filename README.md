# Ticketing Web Application
공연 예매부터 결제, 환불까지 전 과정을 구현 Spring 기반 티켓 예매 웹사이트입니다.
좌석 예약 로직, 쿠폰/포인트 결제, 환불 시스템을 설계하고 구현했습니다.

### 프로젝트 개요
기간: 2025.10 ~ 진행 중

기술: Java, Spring, JSP, JavaScript, CSS, MySQL

환경: Tomcat 10

### 주요 기능
회원 기능
- 로그인/회원가입
- 예매/환불 내역 조회

예매
- 공연 좌석 조회 및 선택
- 예매 진행 시 좌석 상태 AVAILABLE -> BOOKING -> SOLD 업데이트
- 중복 예매 방지를 위한 트랜잭션 처리 (@Transactionl) 적용

결제
- 쿠폰/포인트/KakaoPay 결제 병합 처리
- 결제 시 포인트 차감 및 쿠폰 상태 변경 (NOT-USED -> USED)

환불
- 예약번호 기반 환불 요청 -> 쿠폰 및 포인트 상태 복구
- 공연 시작 전까지만 환불 가능하도록 제한
