/*
 * [CheckoutRequest.java]
 * 사용자가 주문 시 입력한 데이터를 담는 요청 DTO.
 * vip 여부, 쿠폰 코드, 수량 등 모든 입력 정보를 불변(Immutable) 상태로 보관한다.
 */
public class CheckoutRequest {
    private final String userId;
    private final String itemId;
    private final int quantity;
    private final boolean isVip;
    private final String couponCode;

    public CheckoutRequest(String userId, String itemId, int quantity, boolean isVip, String couponCode) {
        this.userId = userId;
        this.itemId = itemId;
        this.quantity = quantity;
        this.isVip = isVip;
        this.couponCode = couponCode == null ? "" : couponCode.trim();
    }

    public String userId() { return userId; }
    public String itemId() { return itemId; }
    public int quantity() { return quantity; }
    public boolean isVip() { return isVip; }
    public String couponCode() { return couponCode; }
}
