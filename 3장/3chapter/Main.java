/*
 * [Main.java]
 * 실행 진입점 클래스.
 * 정책(Pricing/Shipping/Discount)과 저장소, 인벤토리 객체를 조합해 서비스 실행 흐름을 확인한다.
 * 정상 케이스 / 검증 실패 / 재고 부족 등 3가지 테스트 케이스를 포함함.
 */
public class Main {
    public static void main(String[] args) {
        PricingPolicy pricing = new PrefixPricingPolicy();
        ShippingPolicy shipping = new KRShippingPolicy();
        DiscountPolicy discount = new CompositeDiscountPolicy(
                java.util.List.of(
                        new VipRateDiscountPolicy(new java.math.BigDecimal("0.10")),
                        new CouponFlatDiscountPolicy("COUPON10", new java.math.BigDecimal("10"))
                )
        );
        Inventory inventory = new InMemoryInventory();
        OrderRepository repo = new InMemoryOrderRepository();

        RefactoredOrderService svc = new RefactoredOrderService(pricing, shipping, discount, inventory, repo);

        // 정상 케이스
        CheckoutRequest ok = new CheckoutRequest("vip001", "A-100", 2, true, "COUPON10");
        Receipt r = svc.checkout(ok);
        System.out.println("RECEIPT=" + r);

        // 검증 실패 케이스
        try {
            svc.checkout(new CheckoutRequest("", "A-100", -1, false, ""));
            System.out.println("FAIL: should have thrown");
        } catch (ValidationException e) {
            System.out.println("PASS: " + e.getMessage());
        }

        // 재고 부족 케이스
        try {
            svc.checkout(new CheckoutRequest("u1", "B-100", 99, false, ""));
            System.out.println("FAIL: should have thrown");
        } catch (OutOfStockException e) {
            System.out.println("PASS: OUT_OF_STOCK");
        }
    }
}
