import java.math.BigDecimal;

/*
 * [PricingPolicy.java]
 * 단가 계산 정책 인터페이스.
 * 상품 ID에 따라 기본 단가를 결정한다.
 */
public interface PricingPolicy {
    BigDecimal unitPrice(String itemId);
}
