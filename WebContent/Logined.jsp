<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import = "java.sql.*" %>
    
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<title>로그인 페이지</title>
	</head>
	<body>
		<%
			if (session.getAttribute("ID") != null) {
				String id = (String) session.getAttribute("ID");
		%>
			<form action="Logout.jsp" method="post">
				<table border="1">
					<tr>
						<td>
							<%=(String) session.getAttribute("NAME") %>님 로그인을 환영합니다.
							
						</td>
					</tr>
					<tr>
						<td colspan="2" align="right">
							<a href="http://localhost:8080/Sample/boardList.bbs">[게시판 목록 입장]</a>
							<input type="submit" value="로그아웃"/>
						</td>
					</tr>
				</table>
			</form>
		<%	
			} else {
				out.print("잘못된 접근입니다.<br/>");
				out.print("다시 로그인해주세요.<hr/>");
				pageContext.include("/Login.jsp");
			} 
		%>
	</body>
</html>