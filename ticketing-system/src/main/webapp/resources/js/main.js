let currentPage = 0;
const concertsPerPage = 3;
let concerts = concertsData;
let totalPages = Math.ceil(concerts.length / concertsPerPage);

const concertContainer = document.getElementById("concert-list");
const pageDots = document.getElementById('page-dots');
const prevBtn = document.getElementById('prev');
const nextBtn = document.getElementById('next');

for (let i = 0; i < totalPages; i++) {
    const dot = document.createElement('span');
    dot.classList.add('dot');
    dot.addEventListener('click', () => goToPage(i));
    pageDots.appendChild(dot);
}

function render(page) {
    let start = page * concertsPerPage;
    let end = Math.min(start + concertsPerPage, concerts.length);
    if (end === concerts.length) {
        start = Math.max(concerts.length - concertsPerPage, 0);
    }

    let html = `<div class="concerts">`;
    for (let i = start; i < end; i++) {
        const c = concerts[i];
        let days = '지난 공연';
        if (!c.disable) {
            days = 'D-' + c.days;
        }
        html += `
            <div class="concert">
                <div class="image-container">
                    <img src="${c.imageUrl}" alt="${c.title}">
                    <div class="overlay">
                        <div class="days">${days}</div>
                        <a href="${contextPath}/concert/${c.id}" class="book-btn ${c.disable ? 'disabled' : ''}">예매하기</a>
                        <div class="seat-status">
                            ${c.soldCount}/${c.totalCount}
                        </div>
                    </div>
                </div>
                <div class="title">
                    <span data-text="${c.title}">${c.title}</span>
                </div>
            </div>`;
    }
    html += `</div>`;

    concertContainer.innerHTML = html;

    document.querySelectorAll('.page-dots .dot').forEach((dot, idx) => {
        dot.classList.toggle('active', idx === page);
    });

    prevBtn.disabled = page === 0;
    nextBtn.disabled = page === totalPages - 1;
    prevBtn.classList.toggle('disabled', page === 0);
    nextBtn.classList.toggle('disabled', page === totalPages - 1);
}

function goToPage(page) {
    currentPage = page;
    render(currentPage);
}

render(currentPage);

prevBtn.addEventListener('click', () => {
    if (currentPage > 0) goToPage(currentPage - 1);
});
nextBtn.addEventListener('click', () => {
    if (currentPage < totalPages - 1) goToPage(currentPage + 1);
});

document.querySelectorAll('.concert').forEach(concert => {
    const titleSpan = concert.querySelector('.title span');
    const titleWidth = titleSpan.scrollWidth;
    const containerWidth = concert.querySelector('.title').offsetWidth;

    if (titleWidth > containerWidth) {
        concert.addEventListener('mouseenter', () => {
            concert.classList.add('hover');
        });
        concert.addEventListener('mouseleave', () => {
            concert.classList.remove('hover');
        });
    }
})