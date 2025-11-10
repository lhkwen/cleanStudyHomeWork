import java.math.BigDecimal;

/*
 * [ShippingPolicy.java]
 * 배송비 계산 정책 인터페이스.
 * 상품 종류 및 수량에 따라 배송비를 결정한다.
 */
public interface ShippingPolicy {
    BigDecimal shippingCost(String itemId, int quantity);
}
