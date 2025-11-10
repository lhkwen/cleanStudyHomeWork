import java.math.BigDecimal;

/*
 * [VipRateDiscountPolicy.java]
 * VIP 고객에게 일정 비율(예: 10%) 할인 적용하는 정책.
 */
public class VipRateDiscountPolicy implements DiscountPolicy {
    private final BigDecimal rate;
    public VipRateDiscountPolicy(BigDecimal rate) { this.rate = rate; }

    @Override
    public BigDecimal apply(BigDecimal subtotal, CheckoutRequest req) {
        return req.isVip() ? subtotal.multiply(BigDecimal.ONE.subtract(rate)) : subtotal;
    }
}
