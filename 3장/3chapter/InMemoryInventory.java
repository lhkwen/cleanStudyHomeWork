/*
 * [InMemoryInventory.java]
 * 메모리 기반의 단순 재고 정책 구현체.
 * 20개 이하까지만 주문 가능하도록 제한.
 */
public class InMemoryInventory implements Inventory {
    @Override
    public void ensureAvailable(String itemId, int requestedQty) {
        if (requestedQty > 20) throw new OutOfStockException("OUT_OF_STOCK");
    }
}
