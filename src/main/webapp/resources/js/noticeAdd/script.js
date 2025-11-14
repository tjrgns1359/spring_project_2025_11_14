//noticeAdd/index.jsp의 폼 전송 버튼을 JavaScript가 완전히 제어
document.getElementById("buttonSubmit").addEventListener("click",function(){
	//객체
	const formData={
		memID:document.getElementById("memID").value,
		title:document.getElementById("title").value,
		content:document.getElementById("content").value,
		writer:document.getElementById("writer").value,
	}



//index.jsp파일에서 만든 메타CSRF 태그 두개를 js 파일로 가져온다.
const csrfToken=document.querySelector("meta[name='_csrf']").getAttribute("content");
const csrfHeader=document.querySelector("meta[name='_csrf_header']").getAttribute("content");

fetch("/menu/add",{	//MenuRestController의 @PostMapping("/menu/add") 메서드로 이 JSON 데이터를 전송한다.
	method:"POST",
	headers:{
		'Content-Type':'application/json',
		[csrfHeader]:csrfToken //CSRF헤더와 토큰을 동적으로 추가
	},
	body:JSON.stringify(formData)
}).then(response=>{
	if(!response.ok){
		throw new Error("공지사항 작성실패.")
	}
	return response.text(); //=> "게시글 잘 작성됨"
}).then(_=>{
	console.log("Success");
	window.location.href="/";
	//	ResponseEntity.ok(...)를 반환하면(성공), 스크립트는 사용자를 메인 페이지(/)로 이동시킨다.
	//localhost:8080 로 페이지가 이동됩니다.
}).catch(error=>{
	console.log("Error가 발생",error);
})

});