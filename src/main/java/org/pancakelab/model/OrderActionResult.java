package org.pancakelab.model;

public class OrderActionResult<T> {

    final boolean success;
    final String message;
    final T returnObject;

    private OrderActionResult(boolean success, String message, T returnObject) {
        this.success = success;
        this.message = message;
        this.returnObject = returnObject;
    }

    public static <T> OrderActionResult<T> success(T returnObject) {
        return new OrderActionResult<>(true, null, returnObject);
    }

    public static <T> OrderActionResult<T> success() {
        return success(null);
    }

    public static <T> OrderActionResult<T> failed(String message) {
        return new OrderActionResult<>(false, message, null);
    }

    public boolean isSuccess() {
        return success;
    }

    public boolean isFailed() {
        return !isSuccess();
    }

    public T getReturnObject() {
        return returnObject;
    }

    public String getMessage() {
        return message;
    }
}
