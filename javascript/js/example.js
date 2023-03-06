document.write("환영합니다.<hr/>");

var userName = prompt("당신의 영문 이름을 입력하세요.");
var upperName = userName.toUpperCase();
document.write(upperName, "<br/>");

var userNum = prompt("당신의 연락처를 입력하세요.");
var result = userNum.substring(0, userName.length - 4) + "****";
document.write(result, "<br/>");