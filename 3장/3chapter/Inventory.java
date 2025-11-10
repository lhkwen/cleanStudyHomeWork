/*
 * [Inventory.java]
 * 재고 확인용 인터페이스.
 * 수량이 부족할 경우 OutOfStockException을 던진다.
 */
public interface Inventory {
    void ensureAvailable(String itemId, int requestedQty);
}
