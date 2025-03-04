package com.craftinginterpreters.lox;

class LoxPromise {
    private Object value;
    private RuntimeError error;
    private boolean isResolved = false;
    private boolean isRejected = false;

    public void resolve(Object value) {
        if (isResolved || isRejected) return;
        this.value = value;
        isResolved = true;
    }

    public void reject(RuntimeError error) {
        if (isResolved || isRejected) return;
        this.error = error;
        isRejected = true;
    }

    public boolean isResolved() {
        return isResolved;
    }

    public boolean isRejected() {
        return isRejected;
    }

    public Object getValue() {
        if (!isResolved && !isRejected) {
            throw new RuntimeError(null, "Cannot get value of unresolved promise");
        }
        if (isRejected) {
            throw error;
        }
        return value;
    }

    public RuntimeError getError() {
        return error;
    }
}