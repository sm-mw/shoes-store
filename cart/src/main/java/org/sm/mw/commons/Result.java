package org.sm.mw.commons;

public class Result {

    private boolean successful;

    private Result(boolean isSuccess) {
        this.successful = isSuccess;
    }

    public boolean isSuccessful() {
        return this.successful;
    }

    public static Result success() {
        return new Result(true);
    }

    public static Result failure() {
        return new Result(false);
    }
}
