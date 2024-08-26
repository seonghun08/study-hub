<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>    
    
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<title>게시판 글 삭제 에러</title>
	</head>
	<body>
		<h3>[${NAME}]님의 게시글이 아니거나,<br/>
		비밀번호가 맞지 않거나,<br/>
		답글이 존재할 경우 삭제할 수 없습니다.</h3>
		<a href="boardList.bbs">[목록으로]</a>
	</body>
</html>