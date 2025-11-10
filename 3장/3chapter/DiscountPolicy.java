import java.math.BigDecimal;

/*
 * [DiscountPolicy.java]
 * 할인 정책 공통 인터페이스.
 * VIP, 쿠폰 등 다양한 정책을 구현체로 확장 가능.
 */
public interface DiscountPolicy {
    BigDecimal apply(BigDecimal subtotal, CheckoutRequest req);
}
