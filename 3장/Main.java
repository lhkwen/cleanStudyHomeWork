import java.util.*;
import java.math.BigDecimal;

/*
 * [리팩터링 요약]
 * - checkout 메서드가 너무 많은 일을 하던 구조를 역할별 클래스로 분리함.
 * - 파싱, 검증, 계산, 할인, 저장, 로그 등 단계별로 책임을 분리.
 * - boolean vip, List<String> errors 같은 플래그/출력 인자 제거.
 * - 예외 기반 흐름으로 전환하고 DTO(Request/Receipt) 사용.
 * - 매직넘버 대신 정책(가격/배송/할인) 클래스로 명시화.
 */
public class Main {
    public static void main(String[] args) {
        // 구성: 정책 / 인벤토리 / 저장소 / 서비스 생성
        PricingPolicy pricing = new PrefixPricingPolicy();
        ShippingPolicy shipping = new KRShippingPolicy();
        DiscountPolicy discount = new CompositeDiscountPolicy(
                List.of(
                        new VipRateDiscountPolicy(new BigDecimal("0.10")),
                        new CouponFlatDiscountPolicy("COUPON10", new BigDecimal("10"))
                )
        );
        Inventory inventory = new InMemoryInventory();
        OrderRepository repo = new InMemoryOrderRepository();

        RefactoredOrderService svc = new RefactoredOrderService(pricing, shipping, discount, inventory, repo);

        // 1) 정상 케이스
        CheckoutRequest ok = new CheckoutRequest("vip001", "A-100", 2, true, "COUPON10");
        Receipt r = svc.checkout(ok);
        System.out.println("RECEIPT=" + r);

        // 2) 검증 실패 케이스
        try {
            svc.checkout(new CheckoutRequest("", "A-100", -1, false, ""));
            System.out.println("FAIL: should have thrown");
        } catch (ValidationException e) {
            System.out.println("PASS: " + e.getMessage());
        }

        // 3) 재고 부족 케이스
        try {
            svc.checkout(new CheckoutRequest("u1", "B-100", 99, false, ""));
            System.out.println("FAIL: should have thrown");
        } catch (OutOfStockException e) {
            System.out.println("PASS: OUT_OF_STOCK");
        }
    }
}

/* -----------------------------
 * 요청 / 결과 DTO
 * ----------------------------- */
final class CheckoutRequest {
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

final class Receipt {
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

/* -----------------------------
 * 예외 정의
 * ----------------------------- */
class ValidationException extends RuntimeException {
    public ValidationException(String msg) { super(msg); }
}
class OutOfStockException extends RuntimeException {
    public OutOfStockException(String msg) { super(msg); }
}

/* -----------------------------
 * 요청 검증
 * ----------------------------- */
final class CheckoutValidator {
    public void validate(CheckoutRequest req) {
        List<String> errors = new ArrayList<>();
        if (isBlank(req.userId())) errors.add("USER_REQUIRED");
        if (isBlank(req.itemId())) errors.add("ITEM_REQUIRED");
        if (req.quantity() <= 0) errors.add("QTY_POSITIVE");
        if (!errors.isEmpty()) {
            throw new ValidationException(String.join(", ", errors));
        }
    }
    private boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }
}

/* -----------------------------
 * 정책 인터페이스
 * ----------------------------- */
interface PricingPolicy {
    BigDecimal unitPrice(String itemId);
}
interface ShippingPolicy {
    BigDecimal shippingCost(String itemId, int quantity);
}
interface DiscountPolicy {
    BigDecimal apply(BigDecimal subtotal, CheckoutRequest req);
}

/* -----------------------------
 * 정책 구현
 * ----------------------------- */
final class PrefixPricingPolicy implements PricingPolicy {
    @Override
    public BigDecimal unitPrice(String itemId) {
        return itemId.startsWith("A") ? new BigDecimal("100") : new BigDecimal("150");
    }
}
final class KRShippingPolicy implements ShippingPolicy {
    @Override
    public BigDecimal shippingCost(String itemId, int quantity) {
        BigDecimal base = itemId.startsWith("A") ? new BigDecimal("500") : new BigDecimal("1200");
        int weight = 2 * quantity;
        BigDecimal extra = weight > 10 ? new BigDecimal("800") : BigDecimal.ZERO;
        return base.add(extra);
    }
}
final class VipRateDiscountPolicy implements DiscountPolicy {
    private final BigDecimal rate;
    public VipRateDiscountPolicy(BigDecimal rate) { this.rate = rate; }
    @Override
    public BigDecimal apply(BigDecimal subtotal, CheckoutRequest req) {
        return req.isVip() ? subtotal.multiply(BigDecimal.ONE.subtract(rate)) : subtotal;
    }
}
final class CouponFlatDiscountPolicy implements DiscountPolicy {
    private final String code;
    private final BigDecimal amount;
    public CouponFlatDiscountPolicy(String code, BigDecimal amount) {
        this.code = code; this.amount = amount;
    }
    @Override
    public BigDecimal apply(BigDecimal subtotal, CheckoutRequest req) {
        if (code.equalsIgnoreCase(req.couponCode())) {
            return subtotal.subtract(amount);
        }
        return subtotal;
    }
}
final class CompositeDiscountPolicy implements DiscountPolicy {
    private final List<DiscountPolicy> policies;
    public CompositeDiscountPolicy(List<DiscountPolicy> policies) { this.policies = policies; }
    @Override
    public BigDecimal apply(BigDecimal subtotal, CheckoutRequest req) {
        BigDecimal result = subtotal;
        for (DiscountPolicy p : policies) result = p.apply(result, req);
        return result;
    }
}

/* -----------------------------
 * 저장소 / 재고
 * ----------------------------- */
interface OrderRepository {
    String save(String userId, String itemId, int qty, BigDecimal amount);
}
final class InMemoryOrderRepository implements OrderRepository {
    public String save(String userId, String itemId, int qty, BigDecimal amount) {
        String id = UUID.randomUUID().toString();
        System.out.println("SAVE: " + id + " user=" + userId + " item=" + itemId + " qty=" + qty + " sum=" + amount);
        return id;
    }
}
interface Inventory {
    void ensureAvailable(String itemId, int requestedQty);
}
final class InMemoryInventory implements Inventory {
    @Override
    public void ensureAvailable(String itemId, int requestedQty) {
        if (requestedQty > 20) throw new OutOfStockException("OUT_OF_STOCK");
    }
}

/* -----------------------------
 * 서비스 (흐름 제어)
 * ----------------------------- */
final class RefactoredOrderService {
    private final PricingPolicy pricing;
    private final ShippingPolicy shipping;
    private final DiscountPolicy discount;
    private final Inventory inventory;
    private final OrderRepository repo;
    private final CheckoutValidator validator = new CheckoutValidator();

    public RefactoredOrderService(PricingPolicy pricing, ShippingPolicy shipping,
                                  DiscountPolicy discount, Inventory inventory,
                                  OrderRepository repo) {
        this.pricing = pricing;
        this.shipping = shipping;
        this.discount = discount;
        this.inventory = inventory;
        this.repo = repo;
    }

    public Receipt checkout(CheckoutRequest req) {
        logStart(req);
        validator.validate(req);
        inventory.ensureAvailable(req.itemId(), req.quantity());

        BigDecimal items = pricing.unitPrice(req.itemId()).multiply(new BigDecimal(req.quantity()));
        BigDecimal shippingCost = shipping.shippingCost(req.itemId(), req.quantity());
        BigDecimal subtotal = items.add(shippingCost);
        BigDecimal total = discount.apply(subtotal, req);

        String id = repo.save(req.userId(), req.itemId(), req.quantity(), total);
        Receipt receipt = new Receipt(id, req.userId(), req.itemId(), req.quantity(), total);
        logEnd(receipt);
        return receipt;
    }

    private void logStart(CheckoutRequest req) {
        System.out.println("[CHECKOUT START] " + req.userId() + "," + req.itemId() + "," + req.quantity());
    }
    private void logEnd(Receipt r) {
        System.out.println("[CHECKOUT END] " + r);
    }
}
