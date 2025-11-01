const priceInt = parseInt(price, 10);
const pointInt = parseInt(point, 10);

// DOM 요소
const priceContent = document.querySelector('.price');
const discountedPriceContent = document.querySelector('.discounted-price');
const realPaymentAmount = document.getElementById('realPaymentAmount');
const paymentBtn = document.querySelector('.payment-btn');

const useCouponBtn = document.getElementById('useCouponBtn');
const couponContainer = document.getElementById('couponContainer');
const couponList = document.getElementById('couponList');
const couponNotice = document.getElementById('couponNotice');

const usePointBtn = document.getElementById('usePointBtn');
const pointContainer = document.getElementById('pointContainer');
const usePointInput = document.getElementById('usePointInput');
const applyPointBtn = document.getElementById('applyPointBtn');
const pointNotice = document.getElementById('pointNotice');

const useIMPBtn = document.getElementById('useIMPBtn');

// 상태
let selectedCoupon = null; // {type: 'COUPON', amount: 0, extraData: {memberCouponId: 0}}
let selectedPoint = null; // {type: 'POINT', amount: 0, extraData: null}
let selectedIMP = null; // {type: 'IMP', amount: 0, extraData: {impUid: abc}}}

priceContent.textContent = priceInt.toLocaleString() + '원';
realPaymentAmount.textContent = priceInt.toLocaleString() + '원 결제';

function toggleCouponContainer() {
    if (selectedCoupon) {
        resetCouponSelection();
    } else {
        openCouponSelection();
    }
    checkTotalAmount();
}

useCouponBtn.addEventListener('click', toggleCouponContainer);

function resetCouponSelection() {
    selectedCoupon = null;
    useCouponBtn.classList.remove('selected');
    document.querySelectorAll('.coupon-item').forEach(item => {
        item.classList.remove('selected');
    });
    couponContainer.style.display = 'none';
    unselectCoupon();
}

function openCouponSelection() {
    useCouponBtn.classList.add('selected');
    couponContainer.style.display = 'block';
    selectedCoupon = {type: 'COUPON', amount: 0, extraData: {memberCouponId: 0}};
}

function unselectCoupon() {
    couponNotice.textContent = '선택된 쿠폰: 없음';
    priceContent.classList.remove('discounted');
    discountedPriceContent.style.display = 'none';
    discountedPriceContent.textContent = '0원';
}

couponList.addEventListener('click', (e) => {
    const target = e.target;
    if (!target.classList.contains('coupon-item')) return;

    if (target.dataset.id === selectedCoupon.extraData.id) {
        target.classList.remove('selected');
        selectedCoupon = {type: 'COUPON', amount: 0, extraData: {memberCouponId: 0}};
        unselectCoupon();
    } else {
        document.querySelectorAll('.coupon-item').forEach(item => {
            if (target === item) item.classList.add('selected');
            else item.classList.remove('selected');
        })
        const discountedPrice = parseInt(target.dataset.discountedPrice, 10);
        selectedCoupon = {type: 'COUPON', amount: priceInt - discountedPrice, extraData: {memberCouponId: parseInt(target.dataset.id, 10)}};
        couponNotice.textContent = `선택된 쿠폰: ${target.dataset.name}`;
        priceContent.classList.add('discounted');
        discountedPriceContent.textContent = discountedPrice.toLocaleString() + '원';
        discountedPriceContent.style.display = 'block';
    }
    checkTotalAmount();
});

usePointBtn.addEventListener('click', () => {
    if (selectedPoint) {
        selectedPoint = null;
        usePointBtn.classList.remove('selected');
        pointContainer.style.display = 'none';
        pointNotice.textContent = '';
    } else {
        usePointBtn.classList.add('selected');
        pointContainer.style.display = 'block';
        maxUsable = Math.min(pointInt, getRemainingAmount());
        usePointInput.value = maxUsable;
        selectedPoint = {type: 'POINT', amount: maxUsable, extraData: null};
        pointNotice.textContent = `${maxUsable.toLocaleString()} 포인트를 적용했습니다. 최대 ${maxUsable.toLocaleString()} 포인트까지 사용 가능합니다.`;
    }
    checkTotalAmount();
});

applyPointBtn.addEventListener('click', () => {
    let value = Number(usePointInput.value);
    value = Math.round(value / 10) * 10;
    maxUsable = Math.min(pointInt, getRemainingAmount());
    if (value < 0) value = 0;
    else if (value > maxUsable) value = maxUsable;
    pointNotice.textContent = `${value.toLocaleString()} 포인트를 적용했습니다. 최대 ${maxUsable.toLocaleString()} 포인트까지 사용 가능합니다.`;
    usePointInput.value = value;
    selectedPoint = {type: 'POINT', amount: value, extraData: null};
    checkTotalAmount();
})

useIMPBtn.addEventListener('click', () => {
    if (selectedIMP) {
        selectedIMP = null;
        useIMPBtn.classList.remove('selected');
    } else {
        useIMPBtn.classList.add('selected');
        selectedIMP = {type: 'IMP', amount: 0, extraData: {impUid: '1'}};
    }
    checkTotalAmount();
});

function getTotalAmount() {
    return [selectedCoupon, selectedPoint]
        .filter(sel => sel && sel.amount > 0)
        .reduce((sum, sel) => sum + sel.amount, 0);
}

function getRemainingAmount() {
    const totalAmount = getTotalAmount();
    return priceInt - totalAmount;
}

function checkTotalAmount() {
    const remaining = getRemainingAmount();
    realPaymentAmount.textContent = remaining.toLocaleString() + '원 결제';

    paymentBtn.disabled = !(remaining === 0 || selectedIMP);
    return !paymentBtn.disabled;
}

function getPaymentDetails() {
    return [selectedCoupon, selectedPoint, selectedIMP]
        .filter(sel => sel && (sel.amount > 0));
}

function payWithIMP() {
    return new Promise((resolve, reject) => {
        const merchant_uid = 'order_' + Date.now() + '_' + Math.floor(Math.random() * 100000);
        IMP.init('imp87554144'); // 발급받은 가맹점 식별코드
        IMP.request_pay({
            pg: 'kakaopay.TC0ONETIME', // PG사 코드
            pay_method: 'card',
            merchant_uid: merchant_uid, // 주문번호
            name: '공연 예매 티켓',
            amount: selectedIMP.amount,
            buyer_email: 'user@example.com',
            buyer_name: '홍길동',
            buyer_tel: '010-1234-5678',
            buyer_addr: '서울특별시 강남구 삼성동',
            buyer_postcode: '123-456'
        }, function (rsp) {
            if (rsp.success) {
                selectedIMP.extraData.impUid = rsp.imp_uid;
                resolve(rsp); // 결제 성공
            } else {
                alert('결제가 실패했습니다.: ' + rsp.error_msg);
                reject(new Error(rsp.error_msg)); // 결제 실패
            }
        });
    });
}

paymentBtn.addEventListener('click', async () => {
    if (!checkTotalAmount()) {
        alert('결제 방법을 선택해주세요.');
        return;
    }
    try {
        const remaining = getRemainingAmount();
        if (selectedIMP && remaining > 0) {
            selectedIMP = {type: 'IMP', amount: remaining, extraData: {impUid: null}};
            await payWithIMP();
        }
        const response = await fetch(`${contextPath}/reserve`, {
            method: 'POST',
            headers: {'Content-Type': 'application/json'},
            body: JSON.stringify({
                memberId: null,
                concertId: concertId,
                seatId: seatId,
                reservationId: null,
                totalAmount: priceInt,
                paymentDetails: getPaymentDetails()
            })
        });
        if (!response.ok) {
            const err = await response.json();
            alert(err.error || '결제 중 오류가 발생했습니다.');
            return;
        }
        const data = await response.json();
        if (data.reservationId) {
            window.location.href = `${contextPath}/reservation/complete?reservationId=${data.reservationId}`;
        }
    } catch (err) {
        console.error(err);
    }
});