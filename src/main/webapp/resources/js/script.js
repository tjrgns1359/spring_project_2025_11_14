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
// 현재 페이지네이션 "슬라이더"의 시작 번호를 추적하는 전역 변수
let currentSlidingStart = 1;
const pageBlockSize = 5; //  한 번에 5개의 페이지만 표시



// [메뉴 데이터 조회 및 렌더링]
// fetchMenus 함수가 page와 search 인자를 받도록 수정
function fetchMenus(page = 1, search = ''){    //기본 페이지는 1  ,기본 검색어 없음(전체 조회)
	
	let url = `/menu/all?page=${page}`;  //현재 페이지 번호를 URL에 포함
	if (search && search.trim() !== '') {
		url += `&search=${encodeURIComponent(search)}`;   //search 검색어가 있을때만 추가해서 보냄
	}
	
	
	
	// [ 데이터 요청 & 응답 처리]
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
				//  "게시글 없음" 메시지를 테이블 행(tr) 형식으로 반환
				menuList.innerHTML = '<tr><td colspan="4">게시글이 없습니다.</td></tr>';
				paginationButtons.innerHTML = '';
				return;
			}
			
			
			//[메뉴 리스트 동적 생성 & 렌더링(보이는 형태로)]	
			// data.menus 배열을 순회
			data.menus.forEach(menu=>{           //글이 있으면
				// div -> tr (테이블 행)으로 변경
				const menuItem=document.createElement('tr'); 
				
				
				// innerHTML을 div 구조에서 td (테이블 셀) 구조로 변경
				menuItem.innerHTML=`
					<td class="menu-title">
						<a href="#" class="menu-link" data-idx="${menu.idx}">
							${menu.title}
						</a>
					</td>
					<td>${menu.writer}</td>
					<td>${menu.indate}</td>
					<td>${menu.count}</td>
				`;
				
				
				//[상세페이지 이동 이벤트 (조회수 반영 후)].
				// 클릭 이벤트를 data-idx 속성을 사용하도록 변경
				menuItem.querySelector(".menu-link").addEventListener('click',(event)=>{
					event.preventDefault();
					const idx = event.target.getAttribute('data-idx'); // data-idx에서 idx 가져오기 , 즉 idx로 게시글 고유 번호를 파악 하고 
					
					incrementCount(idx).then(()=>window.location.href=`/noticeCheckPage?idx=${idx}`) // 조회수 등 DB에 기록하고 상세페이지로 이동하는 것
				});
				menuList.appendChild(menuItem);
			});
			
			
		//[페이지네이션 위치 계산함수]	
		// 데이터 로드 성공 시, 슬라이더의 버튼 위치를 재계산 (현재 페이지를 중심으로 슬라이더가 너무 앞이나 뒤로 넘어가지 않게 "적절한 시작번호"를 계산하는 과정.)
					let newStart = Math.max(1, data.currentPage - Math.floor(pageBlockSize / 2)); //예를들어 페이지 블록사이즈가 5 이기때문에 예시 10-2 =8 번부터 슬라이더 시작, 만약 계산값이 1보다 작으면 1부터 시작
					newStart = Math.min(newStart, Math.max(1, data.totalPages - pageBlockSize + 1));// 총 20페이지라면 20-5 +1 =16, 슬라이더는 16부터 시작, 슬라이더 시작번호가 16보다 크면 16으로 고정
					currentSlidingStart = newStart; //계산한 슬라이더 시작 번호를 변수에 저장
					
					renderPagination(data.totalPages, data.currentPage);			//슬라이더를 다시 새로 그리는 기능(페이지네이션 UI 렌더링)
			
		}) 
		
		//[에러 발생시 안내 메시지 출력]
		.catch(error => {
			console.error("Error fetching menus:", error);
			menuList.innerHTML = '<tr><td colspan="4">게시글을 불러오는 데 실패했습니다.</td></tr>';
		});
}




//  [슬라이딩 윈도우 방식의 페이지네이션 함수 (Fetch 없음)]

function renderPagination(totalPages, currentPage) {
	paginationButtons.innerHTML = ''; // 버튼 컨테이너 비우기
	
	if (totalPages === 0) return;

	// 1. 슬라이더의 시작/끝 페이지 계산
	
	let startPage = currentSlidingStart;	//위에서 구한 시작점을 가져옴
	let endPage = Math.min(startPage + pageBlockSize - 1, totalPages); //슬라이더 블록의 마지막 페이지 번호 계산, totalPagees를 넘지 않게 
	
	//만약 블록의 갯수가 부족할시에 시작페이지 재조정 및 보정
	if (endPage - startPage + 1 < pageBlockSize && startPage > 1) {	//시작~끝 사이 페이지 개수가 pageBlockSize보다 적고, startpage가 1보다 크면
		startPage = Math.max(1, endPage - pageBlockSize + 1); // 페이지 블록이 항상 pageBolckSize만큼 나오고 앞쪽으로 당간다. 최소값이 1보다 작아지지 않게 보장
		currentSlidingStart = startPage;
	}

	// 2. [<<] (처음) 버튼 생성
	
	const firstBtn = document.createElement('button');
	firstBtn.className = 'pagination-btn';
	firstBtn.innerText = '<<';
	firstBtn.addEventListener('click', () => {
		currentSlidingStart = 1; 
		renderPagination(totalPages, currentPage); 
	});
	if (startPage === 1) {
		firstBtn.disabled = true; 
	}
	paginationButtons.appendChild(firstBtn);

	// 3. [<] (한 칸 뒤로) 버튼 생성
	
	const prevBtn = document.createElement('button');
	prevBtn.className = 'pagination-btn';
	prevBtn.innerText = '<';
	prevBtn.addEventListener('click', () => {
		currentSlidingStart = Math.max(1, startPage - 1); 
		renderPagination(totalPages, currentPage); 
	});
	if (startPage === 1) { 
		prevBtn.disabled = true;
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
		
		// [핵심] 숫자 버튼만 'fetchMenus' (데이터 로드)를 호출
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
		currentSlidingStart = Math.min(Math.max(1, totalPages - pageBlockSize + 1), startPage + 1);
		renderPagination(totalPages, currentPage); 
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
		currentSlidingStart = Math.max(1, totalPages - pageBlockSize + 1); 
		renderPagination(totalPages, currentPage); 
	});
	if (endPage === totalPages) {
		lastBtn.disabled = true; 
	}
	paginationButtons.appendChild(lastBtn);
}


// [incrementCount 함수]
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
	fetchMenus(1, currentSearchTerm); 
});

// 검색창에서 Enter 키를 눌러도 검색 실행
searchInput.addEventListener('keydown', (event) => {
	if (event.key === 'Enter') {
		searchButton.click();
	}
});


// 메인페이지가 열리면 1페이지 & 빈 검색어로 로드
window.addEventListener('load', () => fetchMenus(1, currentSearchTerm));