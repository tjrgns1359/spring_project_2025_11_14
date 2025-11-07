document.getElementById('buttonUpdate').addEventListener('click',function(){
	const idx=document.getElementById("idx").value;
	
	const formData={
		title:document.getElementById("title").value,
		content:document.getElementById("content").value,
		writer:document.getElementById("writer").value
	};
	
	
	//index.jsp파일에서 만든 메타CSRF 태그 두개를 js 파일로 가져온다.
	const csrfToken=document.querySelector("meta[name='_csrf']").getAttribute("content");
	const csrfHeader=document.querySelector("meta[name='_csrf_header']").getAttribute("content");

	fetch(`/menu/update/${idx}`,{
		method:'PUT',
		headers:{
			'content-Type':'application/json',
			[csrfHeader]:csrfToken
		},
		body:JSON.stringify(formData)
	}).then(response=>{
		if(!response.ok){
			throw new Error("응답이 진행불가했습니다.");
		}
		return response.text();
	}).then(_=>{
		alert("수정이 성공적으로 진행되었습니다.")
		window.location.href='/';//메인페이지로 이동한다.
	}).catch(error=>{
		console.log(`Error발생:${error}`);
	})
	

})