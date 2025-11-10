import java.math.BigDecimal;

/*
 * [KRShippingPolicy.java]
 * 기본 배송비 + 무게 초과시 추가 요금이 붙는 배송 정책.
 * A상품 500원 / 그 외 1200원 + (10kg 초과 시 800원 추가)
 */
public class KRShippingPolicy implements ShippingPolicy {
    @Override
    public BigDecimal shippingCost(String itemId, int quantity) {
        BigDecimal base = itemId.startsWith("A") ? new BigDecimal("500") : new BigDecimal("1200");
        int weight = 2 * quantity;
        BigDecimal extra = weight > 10 ? new BigDecimal("800") : BigDecimal.ZERO;
        return base.add(extra);
    }
}
