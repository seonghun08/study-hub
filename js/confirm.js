var user_id = "admin";
var user_pw = "1234";

var id = prompt("아이디를 입력하세요.");
var pw = prompt("패스워드를 입력하세요.");

if (id === user_id) {
    if (pw === user_pw) {
        document.write(user_id + "님 반갑습니다.");
    } else {
        alert("아이디 또는 비밀번호가 일치하지 않습니다.");
        location.reload(); // 브라우저 새로고침
    }
} else {
    alert("아이디 또는 비밀번호가 일치하지 않습니다.");
    location.reload(); // 브라우저 새로고침
}

document.write("<hr/>");

var result = confirm("정말로 회원을 탈퇴하시겠습니까?");
if (result) {
    document.write("탈퇴 처리되었습니다.");
} else {
    document.write("탈퇴 취소되었습니다.");
}