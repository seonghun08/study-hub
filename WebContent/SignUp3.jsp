<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import = "java.sql.*" %> 
 
<!DOCTYPE html>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<title></title>
	</head>
	<body>
		<%
			request.setCharacterEncoding("utf-8");
			
			//session
			String name = null;
			String gender = null;
			String id = null;
			String password = null;
			String address = null;
			String phone_Number = null;
			String emailSet = null;
			
			name = (String) session.getAttribute("name");
			gender = (String) session.getAttribute("gender");
			id = (String) session.getAttribute("id");
			password = (String) session.getAttribute("password");
			address = (String) session.getAttribute("address");
			phone_Number = (String) session.getAttribute("phone_Number");
			emailSet = (String) session.getAttribute("emailSet");
			
			session.invalidate();
			
			//JDBC
			Connection conn = null;
			PreparedStatement pstmt = null;
			String sql = null; 
			try {
				Class.forName("com.mysql.cj.jdbc.Driver");
				
				conn = DriverManager.getConnection(
						"jdbc:mysql://localhost:3306/jspbook?chararterEncoding=UTF-8&serverTimezone=UTC",
						"root",
						"1234"
						);
				
				sql = "INSERT INTO MEMBER (id, password, name, gender, address, phone, email)"
					+ " VALUES (?, ?, ?, ?, ?, ?, ?)";
				
				pstmt = conn.prepareStatement(sql);
				
				pstmt.setString(1, id);
				pstmt.setString(2, password);
				pstmt.setString(3, name);
				pstmt.setString(4, gender);
				pstmt.setString(5, address);
				pstmt.setString(6, phone_Number);
				pstmt.setString(7, emailSet);
				
				pstmt.executeUpdate();
				
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					if (pstmt != null) pstmt.close();
					if (conn != null) conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			response.sendRedirect("http://localhost:8080/Sample/Login.jsp");
		%>
	</body>
</html>