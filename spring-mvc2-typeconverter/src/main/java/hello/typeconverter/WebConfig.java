package hello.typeconverter;

import hello.typeconverter.converter.IpPortToStringConverter;
import hello.typeconverter.converter.StringToIpPortConverter;
import hello.typeconverter.fomatter.MyNumberFormatter;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new StringToIpPortConverter());
        registry.addConverter(new IpPortToStringConverter());

        /*
         * 컨버터가 포멧터보다 우선 순위이기 때문에 주석처리
         * registry.addConverter(new StringToIntegerConverter());
         * registry.addConverter(new IntegerToStringConverter());
         */
        registry.addFormatter(new MyNumberFormatter());
    }
}
