import java.math.BigDecimal;

/*
 * [CouponFlatDiscountPolicy.java]
 * 특정 쿠폰 코드 사용 시 일정 금액을 차감하는 정책.
 */
public class CouponFlatDiscountPolicy implements DiscountPolicy {
    private final String code;
    private final BigDecimal amount;
    public CouponFlatDiscountPolicy(String code, BigDecimal amount) {
        this.code = code; this.amount = amount;
    }
    @Override
    public BigDecimal apply(BigDecimal subtotal, CheckoutRequest req) {
        if (code.equalsIgnoreCase(req.couponCode())) {
            return subtotal.subtract(amount);
        }
        return subtotal;
    }
}
