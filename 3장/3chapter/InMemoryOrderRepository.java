import java.math.BigDecimal;
import java.util.UUID;

/*
 * [InMemoryOrderRepository.java]
 * 메모리 기반의 가짜 저장소 구현체.
 * 주문 저장 시 UUID를 생성하여 orderId로 반환한다.
 */
public class InMemoryOrderRepository implements OrderRepository {
    @Override
    public String save(String userId, String itemId, int qty, BigDecimal amount) {
        String id = UUID.randomUUID().toString();
        System.out.println("SAVE: " + id + " user=" + userId + " item=" + itemId + " qty=" + qty + " sum=" + amount);
        return id;
    }
}
