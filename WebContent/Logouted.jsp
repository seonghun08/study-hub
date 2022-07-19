<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<title>로그아웃 페이지</title>
	</head>
	<body>
		안전하게 로그아웃 되었습니다.<hr/>
		다시 로그인하시려면<br/>
		<%
			pageContext.include("/Login.jsp");
		%>
		<hr/>
		<form action="Login.jsp" method="post">
			<input type="submit" value="첫 화면으로"/>
		</form>
	</body>
</html>