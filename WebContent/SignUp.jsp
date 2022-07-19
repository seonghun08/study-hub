<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<title>회원가입 페이지</title>
	</head>
		<script type="text/javascript">
			function email_change(){
			if(document.SignUp.email.options[document.SignUp.email.selectedIndex].value == '0'){
				document.SignUp.email_2.disabled = true;
				document.SignUp.email_2.value = "";
			}
				
			if(document.SignUp.email.options[document.SignUp.email.selectedIndex].value == '9'){
				document.SignUp.email_2.disabled = false;
				document.SignUp.email_2.value = "";
				document.SignUp.email_2.focus();
			} else {
				document.SignUp.email_2.disabled = true;
				document.SignUp.email_2.value = document.SignUp.email.options[document.SignUp.email.selectedIndex].value;
				}
			}
		</script>
	<body>
	
		<h3>회원정보를 입력하십시오.</h3><hr/>
		<form name="SignUp" action="SignUp2.jsp" method="post">
			<table>
				<tr>
					<td>이름</td>
					<td>
						<input type="text" name="name"/>
					</td>
				<tr>
					<td>성별</td>
					<td>
						<select name="gender">
							<option value="남성">남자</option>
							<option value="여성">여성</option>
						</select>
					</td>
				<tr>
					<td>아이디</td>
					<td>
						<input type="text" name="id"/>
					</td>
				<tr>
					<td>비밀번호</td>
					<td>
						<input type="password" name="password"/>
					</td>
				</tr>
				<tr>
					<td>비밀번호 확인</td>
					<td>
						<input type="password" name="re_password"/>
					</td>
				</tr>
				<tr>
					<td>주소</td>
					<td>
						<input type="text" name="address"/>
					</td>
				<tr>
					<td>전화번호</td>
					<td>
						<input type="text" name="phone_Number"/>
					</td>
				</tr>
				<tr>
					<td>이메일 주소</td>
					<td>
						<input type="text" name="email_1" value="" onfocus="this.value='';" size="10"> @ 
						<input type="text" name="email_2" value="" disabled size="10">
						<select name="email" onchange="email_change()">
							<option value="0">선택하세요</option>
							<option value="9">직접입력</option>
							<option value="naver.com">naver.com</option>
							<option value="nate.com">nate.com</option>
							<option value="google.com">google.com</option>
							<option value="kakako.com">kakako.com</option>
						</select>
					</td>
				</tr>
			</table>
			<hr/>
			<a href="http://localhost:8080/Sample/Login.jsp">[취소]</a>
			<input type="submit" value="회원가입"/>
		</form>
	</body>
</html>