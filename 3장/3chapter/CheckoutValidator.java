import java.util.ArrayList;
import java.util.List;

/*
 * [CheckoutValidator.java]
 * 주문 요청의 필수 값 검증 전담 클래스.
 * ValidationException을 던지며, 상태를 변경하지 않는다.
 */
public class CheckoutValidator {
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
