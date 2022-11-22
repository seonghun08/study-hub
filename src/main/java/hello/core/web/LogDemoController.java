package hello.core.web;

import hello.core.common.MyLogger;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.inject.Provider;
import javax.servlet.http.HttpServletRequest;

@Controller
@RequiredArgsConstructor
public class LogDemoController {

    private final LogDemoService logDemoService;
    private final MyLogger myLogger;

    @RequestMapping("logo-demo")
    @ResponseBody
    public String logDemo(HttpServletRequest requests) {
        String requestURL = requests.getRequestURL().toString();
        myLogger.setRequest(requestURL);

        System.out.println("myLogger1 = " + myLogger.getClass());
        myLogger.log("controller test");
        logDemoService.logic("testId");
        return "OK";
    }
}
