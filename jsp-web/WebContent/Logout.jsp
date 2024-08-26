<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import = "java.sql.*" %>

<!DOCTYPE html>
	<html>
		<head>
			<meta charset="UTF-8">
			<title></title>
		</head>
	<body>
		<%
			Cookie[] cookies = request.getCookies();
			
			String rememberID = null;
			String id = null;
			
			if (cookies != null && cookies.length > 0) {
				for (int i = 0; i < cookies.length; i++) {
					if (cookies[i].getName().equals("rememberID")) {
						rememberID = cookies[i].getValue();
					}
					if (cookies[i].getName().equals("ID")) {
						id = cookies[i].getValue();
					}
				}
			}
			
			if (rememberID.equals("temp") || rememberID == null) {
				if (cookies != null && cookies.length > 0) {
					for (int i = 0; i < cookies.length; i++) {
						if (cookies[i].getName().equals("ID")) {
							cookies[i].setMaxAge(0);
							response.addCookie(cookies[i]);
						}
					}
				}
			}
			
			session.removeAttribute("ID");
			session.removeAttribute("LoginStatus");
			response.sendRedirect("http://localhost:8080/Sample/Logouted.jsp");
		%>
	</body>
</html>