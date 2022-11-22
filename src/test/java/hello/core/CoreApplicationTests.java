package hello.core;

import hello.core.discount.DiscountPolicy;
import hello.core.discount.RateDiscountPolicy;
import hello.core.member.*;
import hello.core.order.OrderServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class CoreApplicationTests {

	@Autowired
	OrderServiceImpl orderService;
	@Autowired
	MemberService memberService;

	MemberRepository memberRepository;
	DiscountPolicy discountPolicy;

//	@Test
////	void contextLoads() {
////		Member member = new Member(1L, "memberA", Grade.VIP);
////		memberService.join(member);
////
////		OrderServiceImpl orderService = new OrderServiceImpl(memberRepository, discountPolicy);
////
////		orderService.setDiscountPolicy(new RateDiscountPolicy());
////		orderService.setMemberRepository(new MemoryMemberRepository());
////
////		orderService.createOrder(1L, "itemA", 10000);
////	}

}
