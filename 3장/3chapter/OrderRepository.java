import java.math.BigDecimal;

/*
 * [OrderRepository.java]
 * 주문 데이터를 저장하는 인터페이스.
 * 실제 구현에서는 DB 연동이 되겠지만, 이번 예제는 InMemory로 처리.
 */
public interface OrderRepository {
    String save(String userId, String itemId, int qty, BigDecimal amount);
}
