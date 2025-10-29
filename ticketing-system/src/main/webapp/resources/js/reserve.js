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
let selectedCoupon = null; // {type: 'COUPON', amount: 0, extraData: {memberCouponId: 1}}
let selectedPoint = null; // {type: 'POINT', amount: 0, extraData: null}
let selectedIMP = null; // {type: 'IMP', amount: 0, extraData: {transactionId: 1}}}

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
    selectedCoupon = {type: 'COUPON', amount: 0, extraData: {memberCouponId: ''}};
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
        selectedCoupon = {type: 'COUPON', amount: 0, extraData: {memberCouponId: ''}};
        unselectCoupon();
    } else {
        document.querySelectorAll('.coupon-item').forEach(item => {
            if (target === item) item.classList.add('selected');
            else item.classList.remove('selected');
        })
        const discountedPrice = parseInt(target.dataset.discountedPrice, 10);
        selectedCoupon = {type: 'COUPON', amount: priceInt - discountedPrice, extraData: {memberCouponId: target.dataset.id}};
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
        selectedIMP = {type: 'IMP', amount: 0, extraData: {transactionId: '1'}};
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
        .filter(sel => sel && (sel.amount > 0 || sel.type === 'IMP'));
}

paymentBtn.addEventListener('click', async () => {
    if (!checkTotalAmount()) {
        alert('결제 방법을 선택해주세요.');
        return;
    }
    try {
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