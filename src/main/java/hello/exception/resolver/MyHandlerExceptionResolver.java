package hello.exception.resolver;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class MyHandlerExceptionResolver implements HandlerExceptionResolver {

    /**
     * @return
     * - 빈 ModelAndView: 뷰를 렌더링 하지 않고, 정상 흐름으로 서블릿이 리턴된다.
     * - ModelAndView: ModelAndView 에 View, Model 등의 정보를 지정해서 반환하면 뷰를 렌더링한다.
     * - null: 다른 ExceptionResolver 를 찾아 실행한다. 만약 처리할 수 있는 ExceptionResolver 가 없는 경우 기존에 발생한 예외를 서블릿 밖으로 던진다.
     */
    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {

        log.info("call MyHandlerExceptionResolver", ex);

        try {
            if (ex instanceof IllegalArgumentException) {
                log.info("IllegalArgumentException resolver to 400");

                response.sendError(HttpServletResponse.SC_BAD_REQUEST, ex.getMessage());
                return new ModelAndView();
            }
        } catch (IOException e) {
            log.error("resolver ex", e);
            e.printStackTrace();
        }

        return null;
    }
}
