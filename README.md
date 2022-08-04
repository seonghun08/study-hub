# Sample web Service
자바 기본 웹 서비스
회원가입/로그인 및 게시판 기능 구현

### 2021.08.29 완료
MPA 방식, 게시판 개념 CRUD

#### java + mysql + jsp + servlet + Model2(MVC 패턴)
<br>

### 2022.07.19
Eclipse IDE -> IntelliJ로 옮기고 서버 띄우는데 뒤지게 애먹었다.<br>
웹 서비스를 처음 배웠을 쯤에 구현한 것이라 그런지 지금 보니까 jsp 내부에서 jdbc커넥션을 열어서 DB를 접근하는 이상한 방식으로 만들어놨네...
지저분하고 수정해야 할 것 투성이지만 귀찮기 때문에 다시 만드는게 차라리 효율적일 것이라는 핑계를 둬야겠다ㅎㅎ


MVC패턴으로 Controller를 통해 uri를 받고, view에만 집중 할 수 있도록 하였고 
HttpServlet으로 들어온 request, response를 Cmd를 통해 데이터를 가공하는 방식이다.<br>
BoardDAO로 DB에 접속하여 가져온 값을 BoardDTO 객체에 담아 request로 view에 뿌리는 방식으로 만들었다.




