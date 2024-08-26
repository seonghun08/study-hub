<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<title>로그인 페이지</title>
	</head>
	<%
		Cookie[] cookies = request.getCookies();
		
		String rememberID = "temp";
		String id = null;
		String password = null;
		
		if (cookies != null && cookies.length > 0) {
			for (int i = 0; i < cookies.length; i++) {
				if (cookies[i].getName().equals("rememberID")) {
					if (cookies[i].getValue().equals("keep")) {
						rememberID = cookies[i].getValue();
					}
				}
				
				if (cookies[i].getName().equals("ID")) {
					id = cookies[i].getValue();
				}
			}
		}
	%>
	<body>
		<form action="LoginChk.jsp" method="post">
			아이디와 비밀번호를 입력하십시오.<hr/>
			<table border="1">
				<tr>
					<td align="center">아이디</td>
					<td>
						<input type="text" name="id" value="<%=(id == null ? "" : id) %>"/>
					</td>
				</tr>
				<tr>
					<td align="center">비밀번호</td>
					<td>
						<input type="password" name="password"/>
					</td>
				</tr>
				<tr>
					<td colspan="2" align="right">
						<input type="checkbox" name="rememberID" value="keep" <%=rememberID.equals("keep") ? "checked=\"checked\"":"" %>/>아이디 저장
					</td>
				</tr>
				<tr>
					<td colspan="2" align="right">
						<a href="http://localhost:8080/Sample/guest.jsp">[손님입장]</a>
						<a href="http://localhost:8080/Sample/SignUp.jsp">[회원가입]</a>
						<input type="submit" value="로그인"/>
					</td>
				</tr>
			</table>
		</form> 
	</body>
</html>