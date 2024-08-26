<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<title>손님 페이지</title>
	</head>
	<body>
		<%
			session.setAttribute("LoginStatus", "2");
		%>
		<form action="guestOut.jsp" method="post">
			<table border="1">
				<tr>
					<td>
						손님의 입장을 환영합니다.
					</td>
				</tr>
				<tr>
					<td colspan="2" align="right">
						<a href="http://localhost:8080/Sample/boardList.bbs">[게시판 목록 입장]</a>
						<input type="submit" value="나가기"/>
					</td>
				</tr>
			</table>
		</form>
	</body>
</html>