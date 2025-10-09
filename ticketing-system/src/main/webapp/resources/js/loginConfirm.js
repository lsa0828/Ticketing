document.addEventListener('click', (e) => {
    if (e.target.classList.contains('book-btn')) {
        e.preventDefault();

        const href = e.target.getAttribute('href');

        if (isLoggedIn === false) {
            const goLogin = confirm('로그인이 필요한 서비스입니다.\n로그인하시겠습니까?');
            if (goLogin) {
                window.location.href = `${contextPath}/login`;
            }
        } else {
            window.location.href = href;
        }
    }
})