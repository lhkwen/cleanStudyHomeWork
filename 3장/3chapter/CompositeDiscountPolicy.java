import java.math.BigDecimal;
import java.util.List;

/*
 * [CompositeDiscountPolicy.java]
 * 여러 할인 정책을 순차적으로 적용할 수 있게 묶어주는 클래스.
 * 예: VIP 10% 할인 후 쿠폰 10원 차감.
 */
public class CompositeDiscountPolicy implements DiscountPolicy {
    private final List<DiscountPolicy> policies;
    public CompositeDiscountPolicy(List<DiscountPolicy> policies) { this.policies = policies; }

    @Override
    public BigDecimal apply(BigDecimal subtotal, CheckoutRequest req) {
        BigDecimal result = subtotal;
        for (DiscountPolicy p : policies) result = p.apply(result, req);
        return result;
    }
}
