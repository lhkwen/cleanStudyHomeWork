/*
 * [OutOfStockException.java]
 * 재고 부족 시 발생하는 예외 클래스.
 * Inventory에서 수량 초과 주문이 감지될 경우 던진다.
 */
public class OutOfStockException extends RuntimeException {
    public OutOfStockException(String msg) { super(msg); }
}
