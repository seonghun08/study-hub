# Sample web Service
자바 기본 웹 서비스
회원가입/로그인 및 게시판 기능 구현

### 2021.08.29 완료
MPA 방식, 게시판 개념 CRUD

<hr/>

#### java + mysql + jsp + servlet + Model2(MVC 패턴)<br/>
collection of picture 예시 사진 모음<br/>
http://localhost:8080/Sample/boardList.bbs<br/>
MVC패턴으로 Controller를 통해 uri를 받고, view에만 집중 할 수 있도록 하였고 
HttpServlet으로 들어온 request, response를 Cmd를 통해 데이터를 가공하는 방식이다.<br/>
BoardDAO로 DB에 접속하여 가져온 값을 BoardDTO 객체에 담아 request로 view에 뿌리는 방식으로 만들었다.<br/>

