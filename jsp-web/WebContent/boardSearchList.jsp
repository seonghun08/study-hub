<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<title>게시판 검색결과 조회</title>
	</head>
	<body>
		<h3>게시판 검색 결과 조회</h3>
		<%
			String LoginStatus = (String) session.getAttribute("LoginStatus");
			
			if (LoginStatus != null && LoginStatus == "1") {
		%>
				[${NAME }]님 접속중입니다. 
				<a href="http://localhost:8080/Sample/Logout.jsp">[로그아웃]</a>
		<%
			} else if (LoginStatus != null && LoginStatus == "2"){
		%>
				손님으로 접속중입니다.<br/>
				일부기능이 제한됩니다.
				<a href="http://localhost:8080/Sample/Login.jsp">[로그인]</a>
		<%
			} else {
				response.sendRedirect("http://localhost:8080/Sample/Login.jsp");
			}
		%>
		<table border="1">
		<%
			if (LoginStatus != null && LoginStatus == "1") {
		%>
			<tr>
				<td colspan="7" align="right">
					<a href="boardWriteForm.bbs">[새글쓰기]</a>
				</td>
			</tr>
		<%
			}
		%>
			<tr>
				<td>글 번호</td>
				<td>글 제목</td>
				<td>작성자</td>
				<td>작성일</td>
				<td>작성시간</td>
				<td>조회수</td>
				<td>답글수</td>
			</tr>
			<c:forEach items="${boardList }" var="dto">
				<tr>
					<td>
						<a href="boardRead.bbs?num=${dto.num }">${dto.num }</a>
					</td>
					<td>
						<c:forEach begin="1" end="${dto.lev }">
							<%="&nbsp;&nbsp;" %>
						</c:forEach>
						<a href="boardRead.bbs?num=${dto.num }">${dto.subject }</a>
					</td>
						<td>${dto.name }</td>
						<td>${dto.writeDate }</td>
						<td>${dto.writeTime }</td>
						<td>${dto.readCnt }</td>
						<td>${dto.childCnt }</td>
				</tr>
			</c:forEach>
			<tr>
				<td colspan="7" align="center">
					<a href="boardList.bbs">[첫 페이지로]</a>
				</td>
			</tr>
		</table>
	</body>
</html>