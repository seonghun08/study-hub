// <1>
$(function(){
    // contains(텍스트)
    $("#inner_1 p:contains(내용1)")
    .css({"background-color":"#ff0"});

    // has(요소명)
    $("#inner_1 p:has(strong)")
    .css({"background-color":"#0ff"});

    // 해당 요소의 하위 요소의 텍스트와 태그 노드 선택
    $("#outer_wrap").contents()
    .css({"border":"1px dashed #00f"});

    // 해당 요소의 첫번째 요소를 제외하고 선택
    $("#inner_2 p").not(":first")
    .css({"background-color":"#0f0"});

    // 해당 요소의 2번째 요소 선택했으나, 다시 end() 메서드를 적용
    // end() 필터링이 실행되기 이전의 요소가 선택
    $("#inner_2 p").eq(2).end()
    .css({"color":"red"});
});

// <2>
$(function(){
    /**
     * find와 fillter의 차이는 '하위 요소'인지 '선택한 요소'인지에 따라 구분 
     */

    // 해당 요소의 하위 요소 중 class 값이 txt1인 경우 적용
    $("#inner_1_2").find(".txt1")
    .css({"background-color":"#ff0"});

    // 해당 요소의 class 값이 txt1인 경우 적용
    $("#inner_1_2 p").filter(".txt2")
    .css({"background-color":"#0ff"});

    console.log("=======================");

    $("#inner_2_2 p").filter(function(idx, obj){
        // console.log(idx);
        console.log(obj);
        return idx % 2 == 0;
    })
    .css({"background-color":"#0f0"});    
});

// <3>
$(function(){
    var result_1 = $("#inner_1_3 p")
    .eq(0).is(":visible");
    console.log(result_1);

    var result_2 = $("#inner_1_3 p")
    .eq(1).is(":visible");
    console.log(result_2);

    var result_3 = $("#chk1").is(":checked");
    console.log(result_3);

    var result_4 = $("#chk2").is(":checked");
    console.log(result_4);
});