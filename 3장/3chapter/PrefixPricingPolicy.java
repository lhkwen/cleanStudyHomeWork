import java.math.BigDecimal;

/*
 * [PrefixPricingPolicy.java]
 * 상품ID 접두사에 따라 단가를 결정하는 정책.
 * 예: "A"로 시작하면 100원, 그 외는 150원.
 */
public class PrefixPricingPolicy implements PricingPolicy {
    @Override
    public BigDecimal unitPrice(String itemId) {
        return itemId.startsWith("A") ? new BigDecimal("100") : new BigDecimal("150");
    }
}
