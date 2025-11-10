/*
 * [ValidationException.java]
 * 입력값 검증 실패 시 발생하는 예외.
 * ex) 사용자ID, 수량, 상품ID 등 누락 또는 잘못된 값일 때 사용.
 */
public class ValidationException extends RuntimeException {
    public ValidationException(String msg) { super(msg); }
}
