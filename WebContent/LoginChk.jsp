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
			//Cookie
			String id = request.getParameter("id");
			String password = request.getParameter("password");
			String rememberID = request.getParameter("rememberID");
			
			Cookie cookieRememberID = null;
			Cookie cookieID = null;
			
			if (rememberID != null && !rememberID.equals("temp")) {
				if (rememberID.equals("keep")) {
					cookieRememberID = new Cookie("rememberID", "keep");
				}
				
			} else {
				cookieRememberID = new Cookie("rememberID", "temp");
			}
			
			cookieID = new Cookie("ID", id);
			
			response.addCookie(cookieRememberID);
			response.addCookie(cookieID);
			
			// JDBC
			Connection conn = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			String sql = null;
			
			try {
				Class.forName("com.mysql.cj.jdbc.Driver");
				
				conn = DriverManager.getConnection(
						"jdbc:mysql://localhost:3306/jspbook?character=UTF-8&serverTimezone=UTC",
						"root",
						"1234"
						);
				
				sql = "SELECT COUNT(*) as count, NAME"
					+ "  FROM MEMBER"
					+ " WHERE id = ?"
					+ " AND password = ?";
				
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, id);
				pstmt.setString(2, password);
				
				rs = pstmt.executeQuery();
				
				while(rs.next()) {
					if (rs.getString("count").equals("1")) {
						session.setAttribute("ID", id);
						session.setAttribute("PASSWORD", password);
						session.setAttribute("NAME" , rs.getString("name"));
						session.setAttribute("LoginStatus", "1");
						
						response.sendRedirect("http://localhost:8080/Sample/Logined.jsp");
					} else {
						out.println("가입하지 않는 아이디이거나, 잘못된 비밀번호를 입력하셨습니다.");
						pageContext.include("/Login.jsp");
					}
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					if (rs != null) rs.close();
					if (pstmt != null) pstmt.close();
					if (conn != null) conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		%>
	</body>
</html>