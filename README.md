# JavaScript + jQuery
jQuery는 HTML의 DOM 조작과 이벤트 제어, 애니메이션 그리고 Ajax까지 웹 화면을 다루는 자바스크립트 라이브러리이다.<br/>
jQuery는 다음과 같은 기능을 제공한다.
* DOM과 관련된 처리를 쉽게 구현 할 수 있다.
* 규칙성을 가지고 이벤트를 처리 할 수 있다.
* 애니메이션 효과를 쉽게 만들 수 있다.
* AJAX 처리 방식을 편리하게 사용 할 수 있다.

## JavaScript
* 1.기본 문법 (grammar)
* 2.객체 활용 (object)
* 3.함수 활용 (function)
```html
<script>
    var count = 0;
    myFnc();
    theFnc();

    function myFnc() {
        count++;
        document.write("hello " + count, "<br/>");
    }
    var theFnc = function() {
        count++;
        document.write("bye " + count, "<br/>");
    }
</script>
```

## jQuery
제이쿼리를 사용하기 위해서 다음과 같이 적용시켜야 한다.
```html
<script src="js/jquery.js"></script>
```
* 1.선택자 (selecter)
```html
<script>
    $(function() {
        // #아이디명, id="아이디명"에 포함하는 요소에 적용
        $("#subject").css("border", "2px solid blue");
    });

    $(function() {
        // .class
        $(".select").css("border", "2px solid yellow");
    });

    $(function() {
        // tag h1
        $("h1").css("border", "2px dashed #f00");
    });

    $(function() {
        // #tit, h3 여러 요소를 선택하고 싶을 때 사용 (1개 이상 포함되는 경우 적용된다.)
        $("#tit, h3")
        .css("background-color", "#0ff")
        .css("border", "2px dashed blue");
    })

    $(function() {
        // h4.tit 여러 요소를 선택하고 싶을 때 사용 (둘다 포함되는 경우 적용된다.)
        $("h4.tit")
        .css("color", "white")
        .css("background-color", "black")
        .css("border", "2px dashed white");
    })
</script>
```
* 2.이벤트 (event)
```html
<script>
    $(function( ) {
        $(".btn1").click(function() {
            $(".btn1").parent().next()
            .css({"color":"#f00"});
        });

        $(".btn2").on({
            "mouseover focus": function() {
                $(".btn2").parent().next()
                .css({"color":"#0f0"});
            },
            "mouseout blur": function() {
                $(".btn2").parent().next()
                .css({"color":"#000"});
            },
        });   
    });
</script>
```
* 3.이펙트 (effect)
```html
<script>
  $(function( ) {
    $(".btn2").hide();

    $(".btn1").on("click", function( ) {
      $(".box").slideUp(1000, "linear", 
      function( ) {
        $(".btn1").hide( );
        $(".btn2").show( );
      });
    });

    $(".btn2").on("click", function( ) {
      $(".box").fadeIn(1000, "swing", 
      function( ) {
        $(".btn2").hide( );
        $(".btn1").show( );
      });
    });
  });
</script>
```
* 4.제이쿼리 플러그인 (plugin)
* 5.반응형 웹 UI 구현 (project)
