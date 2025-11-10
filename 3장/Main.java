public class Main {
    public static void main(String[] args) {
        // 구성: 정책 / 인벤토리 / 저장소 / 서비스 생성
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
