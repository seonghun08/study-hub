<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
		<title>회원가입 정보확인 페이지</title>
	</head>
		<% 
			request.setCharacterEncoding("utf-8");
		
			String name = request.getParameter("name");
			String gender = request.getParameter("gender");
			String id = request.getParameter("id");
			String password = request.getParameter("password");
			String re_password = request.getParameter("re_password");
			String address = request.getParameter("address");
			String phone_Number = request.getParameter("phone_Number");
			
			String email = request.getParameter("email");
			String email_1 = request.getParameter("email_1");
			String email_2 = request.getParameter("email_2");
			if (email.equals("9")) {
				email = email_2;
			}
			String emailSet = email_1 + "@" + email;
		%>
	<body>
		<%
			if (!id.equals("") && !password.equals("") && 
				!re_password.equals("") && password.equals(re_password) && 
				id != null && password != null && re_password != null &&
				!email.equals("9") && !email.equals("0")) {
				
				session.setAttribute("name", name);
				session.setAttribute("gender", gender);
				session.setAttribute("id", id);
				session.setAttribute("password", password);
				session.setAttribute("address", address);
				session.setAttribute("phone_Number", phone_Number);
				session.setAttribute("emailSet", emailSet);
		%>
				<h3>작성하신 내용은 아래와 같습니다.<br/>
				수정을 원하시면 밑에 있는 수정을 눌러주십시오.</h3>
				이름: <%=name %><hr/>
				성별: <%=gender %><hr/>
				아이디: <%=id %><hr/>
				주소: <%=address %><hr/>
				전화번호: <%=phone_Number %><hr/>
				이메일주소: <%=emailSet %><hr/>
				
				<form action="SignUp3.jsp" method="post"> 
					<a href="http://localhost:8080/Sample/SignUp.jsp">[수정]</a>
					<a href="http://localhost:8080/Sample/Login.jsp">[취소]</a>
					<input type="submit" value="회원가입 완료">
				</form>
		<%
			} else {
		%>
				회원가입에 실패하셨습니다.<br/>
				비밀번호가 서로 일치하지 않거나, 이메일을 잘못 입력하셨거나, 아이디 또는 비밀번호가 제대로 입력되지 않았습니다.<br/>
				다시 입력하여 주십시오.<hr/>
		<%
				pageContext.include("SignUp.jsp");
			}
					
					
		%>
	</body>
</html>