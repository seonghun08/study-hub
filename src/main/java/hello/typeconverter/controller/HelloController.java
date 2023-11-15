package hello.typeconverter.controller;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestController
public class HelloController {

    @ExceptionHandler(NumberFormatException.class)
    public ErrorResult errorNumberFormat(Exception e) {
        log.error("exceptionHandler [NumberFormatException]", e);
        return new ErrorResult("400", "숫자가 아닌 다른 값이 입력되었습니다.");
    }

    @GetMapping("/hello-v1")
    public String helloV1(HttpServletRequest request) {
        String data = request.getParameter("data");
        int intValue = Integer.parseInt(data);
        System.out.println("intValue = " + intValue);
        return "ok";
    }

    @GetMapping("/hello-v2")
    public String helloV2(@RequestParam Integer data) {
        System.out.println("data = " + data);
        return "ok";
    }

    @Data
    @AllArgsConstructor
    static class ErrorResult {
        private String code;
        private String message;
    }
}
