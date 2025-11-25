// "주소 검색" 버튼 DOM 요소를 찾습니다.
const postBtn = document.getElementById("btn-postcode");

// 버튼 클릭 이벤트를 감지합니다. 
postBtn.addEventListener("click", function(){
    
	// 3. Daum Postcode API를 실행합니다.
    new daum.Postcode({
        oncomplete: function(data) {
            // 팝업에서 검색결과 항목을 클릭했을때 실행할 코드를 작성하는 부분

            //  각 주소 정보를 가져옵니다.
            let roadAddr = data.roadAddress; // 도로명 주소 변수
            let jibunAddr = data.jibunAddress; // 지번 주소 변수
            
            //  우편번호와 주소 정보를 해당 필드에 넣습니다.
            document.getElementById('postcode').value = data.zonecode; // 우편번호
            
            if(roadAddr !== ''){
                document.getElementById("address1").value = roadAddr;
            } else if(jibunAddr !== ''){
                document.getElementById("address1").value = jibunAddr;
            }

            //  상세주소 필드로 포커스를 이동시킵니다.
            document.getElementById("address2").focus();
        }
    }).open();
});


//  비밀번호 확인 유효성 검사
document.addEventListener("DOMContentLoaded", function() {
    // 1. 필요한 DOM 요소들을 찾습니다.
    const registerForm = document.getElementById("register-form");
    const password = document.getElementById("password");
    const passwordConfirm = document.getElementById("password-confirm");
    const passwordError = document.getElementById("password-error");

    // 폼(Form)의 submit제출 버튼 클릭시
    registerForm.addEventListener("submit", function(event) {
        
        // 비밀번호와 비밀번호 확인 값이 일치하는지 검사
        if (password.value !== passwordConfirm.value) {
            
            // 불일치할때
            // 폼 제출을 막는다.
            event.preventDefault(); 
            
            // 에러 메시지
            passwordError.textContent = "비밀번호가 일치하지 않습니다.";
            passwordError.style.display = "block";
            
            // 비밀번호 확인 필드에 포커스
            passwordConfirm.focus();
            
        } else {
            
            // 일치할때
            // 에러 메시지를 숨김
            passwordError.textContent = "";
            passwordError.style.display = "none";
            
          
            //    폼은 서버로 정상적으로 제출
        }
		
		
		
		//  캡챠 체크 여부 검사
		// grecaptcha.getResponse()가 빈 문자열이면 체크하지 않은 것임
		if (grecaptcha.getResponse().length === 0) {
		    event.preventDefault(); // 폼 제출 막기
		    alert('자동 가입 방지를 위해 "로봇이 아닙니다"에 체크해주세요.');
		    return;
		}
				
    });
});