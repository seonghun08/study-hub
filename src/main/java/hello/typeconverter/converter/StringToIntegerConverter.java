package hello.typeconverter.converter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.converter.Converter;

@Slf4j
public class StringToIntegerConverter implements Converter<String, Integer> {

    @Override
    public Integer convert(String source) {
        log.info("convert source={}", source);
        return Integer.valueOf(source);
    }

    @Override
    public <U> Converter<String, U> andThen(Converter<? super Integer, ? extends U> after) {
        return Converter.super.andThen(after);
    }
}
