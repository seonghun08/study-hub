package hello.aop.member.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE) // Class 또는 Interface 에 붙일 수 있는 애노테이션
@Retention(RetentionPolicy.RUNTIME) // 런타임 시점에 실행
public @interface ClassAop {
}
