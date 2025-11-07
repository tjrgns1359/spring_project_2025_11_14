document.getElementById("buttonUpdate").addEventListener('click',function(){
	const idx=document.getElementById("idx").value;
	window.location.href=`/noticeModifyPage?idx=${idx}`;
})


document.getElementById("buttonDelete").addEventListener("click",function(){
	const idx=document.getElementById("idx").value;
	
	const csrfToken=document.querySelector("meta[name='_csrf']").getAttribute("content");
	const csrfHeader=document.querySelector("meta[name='_csrf_header']").getAttribute("content");

	fetch(`/menu/delete/${idx}`,{
		method:'DELETE',
		headers:{
			[csrfHeader]:csrfToken
		}
	}).then(response=>{
		if(!response.ok){
			throw new Error("응답이 잘 안됬습니다.");
		}else{
			return	response.text();
		}
	}).then(_=>{
		alert("삭제가 성공적으로 진행되었습니다.");
		window.location.href='/'; //메인페이지
	}).catch(error=>{
		console.log(`에러발생:${error}`);
	})


})