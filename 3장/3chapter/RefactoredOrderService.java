import java.math.BigDecimal;

/*
 * [RefactoredOrderService.java]
 * 주문 체크아웃 전체 흐름을 담당하는 서비스 클래스.
 * - 검증, 재고 확인, 가격 계산, 할인 적용, 저장소 저장 순으로 동작한다.
 * - 개별 로직은 외부 정책(인터페이스)으로 분리되어 주입받는다.
 * - 한 가지 추상화 수준만 유지하도록 작성.
 */
public class RefactoredOrderService {
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
