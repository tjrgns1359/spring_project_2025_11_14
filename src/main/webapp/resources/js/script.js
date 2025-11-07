//DOM 객체 연결
const container= document.getElementById("container");
const menuAdmin=document.getElementById("menuAdmin");
const menuList=document.getElementById("menuList");
const paginationContainer = document.getElementById("pagination-container");
const searchInput = document.getElementById("search-input");
const searchButton = document.getElementById("search-button");
const paginationButtons = document.getElementById("pagination-buttons");

//CSRF 토큰과 헤더이름 가져오기 
const csrfToken = document.querySelector("meta[name='_csrf']").getAttribute('content');
const csrfHeader = document.querySelector("meta[name='_csrf_header']").getAttribute('content');

// 현재 검색어를 저장할 변수
let currentSearchTerm = '';
// 🔽 [추가] 현재 페이지네이션 "슬라이더"의 시작 번호를 추적하는 전역 변수
let currentSlidingStart = 1;
const pageBlockSize = 5; // 👈 [설정] 한 번에 5개의 페이지만 표시




// fetchMenus 함수가 page와 search 인자를 받도록 수정
function fetchMenus(page = 1, search = ''){    //기본 페이지는 1  ,기본 검색어 없음(전체 조회)
	
	let url = `/menu/all?page=${page}`;  //현재 페이지 번호를 URL에 포함
	if (search && search.trim() !== '') {
		url += `&search=${encodeURIComponent(search)}`;   //search 검색어가 있을때만 추가해서 보냄
	}
	
	fetch(url)      //만약에 요청이 실패하게 되면 에러 메시지를 던짐
		.then(response => {
			if (!response.ok) {
				throw new Error('데이터 로드 실패');
			}
			return response.json();
		})
		.then(data => { 
			menuList.innerHTML=''; // 기존 메뉴 목록 초기화
			
			if (data.menus == null || data.menus.length === 0) {   //글이 하나도 없으면 
				menuList.innerHTML = '<p>게시글이 없습니다.</p>';  //게시글이 없습니다 표시, 페이지네이션 숨김
				paginationButtons.innerHTML = '';
				return;
			}
			
			// data.menus 배열을 순회
			data.menus.forEach(menu=>{           //글이 있으면
				const menuItem=document.createElement('div');
				menuItem.className='menu-item';            //div. menu-item 을 만들어 각 작성자,작성일,조회수 표시
				menuItem.innerHTML=`
				<a href="#" class="menu-link" style="text-decoration:none;color:black;">
					<h3>${menu.title}</h3>
					<p>${menu.content}</p>
					<small>작성자:${menu.writer},작성일:${menu.indate},조회수:${menu.count}</small>
				</a>
				<br/>
				<br/>
				`
				menuItem.querySelector(".menu-link").addEventListener('click',(event)=>{
					event.preventDefault();
					console.log(`event:${event}`);
					incrementCount(menu.idx).then(()=>window.location.href=`/noticeCheckPage?idx=${menu.idx}`)  //제목글 클릭시 incrementCount로 조회수 올리고 상세 글페이지 이동
				});
				menuList.appendChild(menuItem);
			});
			
		// 🔽 [수정] 데이터 로드 성공 시, 슬라이더의 위치를 재계산
					// (현재 페이지가 중앙에 오도록)
					let newStart = Math.max(1, data.currentPage - Math.floor(pageBlockSize / 2));
					// 끝 범위를 넘어가지 않도록 조정 (totalPages - 4)
					newStart = Math.min(newStart, Math.max(1, data.totalPages - pageBlockSize + 1));
					
					currentSlidingStart = newStart; // 👈 전역 변수(슬라이더 시작점) 업데이트
					
					// 🔽 [수정] 페이지네이션 버튼 렌더링 함수 호출
					renderPagination(data.totalPages, data.currentPage);			
			
		}) 
		.catch(error => {
			console.error("Error fetching menus:", error);
			menuList.innerHTML = '<p>게시글을 불러오는 데 실패했습니다.</p>';
		});
}




// 🔽 [수정] "슬라이딩 윈도우" 방식의 페이지네이션 함수 (Fetch 없음)
function renderPagination(totalPages, currentPage) {
	paginationButtons.innerHTML = ''; // 버튼 컨테이너 비우기
	
	if (totalPages === 0) return;

	// 1. 슬라이더의 시작/끝 페이지 계산
	let startPage = currentSlidingStart;
	let endPage = Math.min(startPage + pageBlockSize - 1, totalPages);
	
	// (혹시 totalPages가 5개 미만이라 startPage가 밀리는 경우 방지)
	if (endPage - startPage + 1 < pageBlockSize && startPage > 1) {
		startPage = Math.max(1, endPage - pageBlockSize + 1);
		currentSlidingStart = startPage;
	}

	// 2. [<<] (처음) 버튼 생성
	const firstBtn = document.createElement('button');
	firstBtn.className = 'pagination-btn';
	firstBtn.innerText = '<<';
	firstBtn.addEventListener('click', () => {
		currentSlidingStart = 1; // 👈 UI 상태만 1로 변경
		renderPagination(totalPages, currentPage); // 👈 UI만 다시 그림 (Fetch 없음)
	});
	if (startPage === 1) {
		firstBtn.disabled = true; // 1번 블록일 때 비활성화
	}
	paginationButtons.appendChild(firstBtn);

	// 3. [<] (한 칸 뒤로) 버튼 생성
	const prevBtn = document.createElement('button');
	prevBtn.className = 'pagination-btn';
	prevBtn.innerText = '<';
	prevBtn.addEventListener('click', () => {
		currentSlidingStart = Math.max(1, startPage - 1); // 👈 UI 상태만 1 감소
		renderPagination(totalPages, currentPage); // 👈 UI만 다시 그림 (Fetch 없음)
	});
	if (startPage === 1) { 
		prevBtn.disabled = true;// 1번 블록일 때 비활성화
	}
	paginationButtons.appendChild(prevBtn);

	// 4. 숫자 페이지 버튼
	for (let i = startPage; i <= endPage; i++) {
		const pageBtn = document.createElement('button');
		pageBtn.className = 'pagination-btn';
		pageBtn.innerText = i;
		
		if (i === currentPage) {
			pageBtn.classList.add('active'); 
		}
		
		// 🔽 [핵심] 숫자 버튼만 'fetchMenus' (데이터 로드)를 호출
		pageBtn.addEventListener('click', () => {
			fetchMenus(i, currentSearchTerm); 
		});
		
		paginationButtons.appendChild(pageBtn);
	}

	// 5. [>] (한 칸 앞으로) 버튼 생성
	const nextBtn = document.createElement('button');
	nextBtn.className = 'pagination-btn';
	nextBtn.innerText = '>';
	nextBtn.addEventListener('click', () => {
		// 👈 UI 상태만 1 증가 (최대치 제한)
		currentSlidingStart = Math.min(Math.max(1, totalPages - pageBlockSize + 1), startPage + 1);
		renderPagination(totalPages, currentPage); // 👈 UI만 다시 그림 (Fetch 없음)
	});
	if (endPage === totalPages) { 
		nextBtn.disabled = true;
	}
	paginationButtons.appendChild(nextBtn);

	// 6. [>>] (마지막) 버튼 생성
	const lastBtn = document.createElement('button');
	lastBtn.className = 'pagination-btn';
	lastBtn.innerText = '>>';
	lastBtn.addEventListener('click', () => {
		currentSlidingStart = Math.max(1, totalPages - pageBlockSize + 1); // 👈 UI 상태를 마지막 블록으로
		renderPagination(totalPages, currentPage); // 👈 UI만 다시 그림 (Fetch 없음)
	});
	if (endPage === totalPages) {
		lastBtn.disabled = true; 
	}
	paginationButtons.appendChild(lastBtn);
}


// --- (incrementCount 함수는 수정 없이 동일) ---
function incrementCount(idx){
	return fetch(`/menu/count/${idx}`,{
		method:'PUT',
		headers:{
			[csrfHeader]:csrfToken
		}
	}).then(response=>{
		if(!response.ok){
			console.log('데이터가 프론트서버에서 백엔드서러 잘 안넘어감');
		}
	}).catch(error=>{
		console.log(`Error:${error}`);
	})
}


// 검색 버튼 이벤트 리스너
searchButton.addEventListener('click', () => {
	const searchTerm = searchInput.value;
	currentSearchTerm = searchTerm;
	fetchMenus(1, currentSearchTerm); // 👈 검색 시 1페이지 데이터 로드 (이후 renderPagination이 재계산)
});

// 검색창에서 Enter 키를 눌러도 검색 실행
searchInput.addEventListener('keydown', (event) => {
	if (event.key === 'Enter') {
		searchButton.click();
	}
});


// 메인페이지가 열리면 1페이지 & 빈 검색어로 로드
window.addEventListener('load', () => fetchMenus(1, currentSearchTerm));