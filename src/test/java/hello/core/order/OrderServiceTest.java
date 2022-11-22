package hello.core.order;

import hello.core.AppConfig;
import hello.core.discount.DiscountPolicy;
import hello.core.discount.RateDiscountPolicy;
import hello.core.member.*;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class OrderServiceTest {

    MemberService memberService;
    OrderService orderService;

    MemberRepository memberRepository;
    DiscountPolicy discountPolicy;

    @BeforeEach
    public void beforeEach() {
        AppConfig appConfig = new AppConfig();
        memberService = appConfig.memberService();
        orderService = appConfig.orderService();
    }
//    AppConfig appConfig = new AppConfig();
//    MemberService memberService = appConfig.memberService();
//    OrderService orderService = appConfig.orderService();

//    MemberService memberService = new MemberServiceImpl();
//    OrderService orderService = new OrderServiceImpl();

    @Test
    void createOrder() {
        Long memberId = 1L;
        Member member = new Member(memberId, "memberA", Grade.VIP);
        memberService.join(member);

        Order order = orderService.createOrder(memberId, "itemA", 10000);
        Assertions.assertThat(order.getDiscountPrice()).isEqualTo(1000);
    }

//    @Test
//    void filedInjectionTest() {
//        Member member = new Member(1L, "memberA", Grade.VIP);
//        memberService.join(member);
//
//        OrderServiceImpl orderService = new OrderServiceImpl(memberRepository, discountPolicy);
//
//        orderService.setDiscountPolicy(new RateDiscountPolicy());
//        orderService.setMemberRepository(new MemoryMemberRepository());
//
//        orderService.createOrder(1L, "itemA", 10000);
//    }
}
