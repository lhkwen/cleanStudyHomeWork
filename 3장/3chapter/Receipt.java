import java.math.BigDecimal;

/*
 * [Receipt.java]
 * 결제 완료 후 반환되는 결과 DTO.
 * 주문 ID, 금액, 상품 정보를 포함하며, toString()으로 영수증 형식 문자열을 반환한다.
 */
public class Receipt {
    private final String orderId;
    private final String userId;
    private final String itemId;
    private final int quantity;
    private final BigDecimal amount;

    public Receipt(String orderId, String userId, String itemId, int quantity, BigDecimal amount) {
        this.orderId = orderId;
        this.userId = userId;
        this.itemId = itemId;
        this.quantity = quantity;
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "OK:" + orderId + ":" + userId + ":" + itemId + ":" + quantity + ":" + amount;
    }
}
