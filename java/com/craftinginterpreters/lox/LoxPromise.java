package com.craftinginterpreters.lox;

import java.util.ArrayList;
import java.util.List;

class LoxPromise {
    private Object value;
    private RuntimeError error;
    private boolean isResolved = false;
    private boolean isRejected = false;
    private final List<LoxFunction> thenCallbacks = new ArrayList<>();
    private final List<LoxFunction> catchCallbacks = new ArrayList<>();

    void resolve(Object value) {
        if (isResolved || isRejected) return;
        this.value = value;
        isResolved = true;
        for (LoxFunction callback : thenCallbacks) {
            callback.call(null, List.of(value));
        }
    }

    void reject(RuntimeError error) {
        if (isResolved || isRejected) return;
        this.error = error;
        isRejected = true;
        for (LoxFunction callback : catchCallbacks) {
            callback.call(null, List.of(error));
        }
    }

    LoxPromise then(LoxFunction callback) {
        thenCallbacks.add(callback);
        if (isResolved) {
            callback.call(null, List.of(value));
        }
        return this;
    }

    LoxPromise catch_(LoxFunction callback) {
        catchCallbacks.add(callback);
        if (isRejected) {
            callback.call(null, List.of(error));
        }
        return this;
    }

    boolean isResolved() {
        return isResolved;
    }

    boolean isRejected() {
        return isRejected;
    }

    Object getValue() {
        return value;
    }

    RuntimeError getError() {
        return error;
    }
}