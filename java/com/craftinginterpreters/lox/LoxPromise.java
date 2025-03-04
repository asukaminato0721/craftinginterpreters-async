package com.craftinginterpreters.lox;

class LoxPromise {
    private Object value;
    private RuntimeError error;
    private boolean isResolved = false;
    private boolean isRejected = false;
    private final Object lock = new Object();

    public void resolve(Object value) {
        synchronized (lock) {
            if (isResolved || isRejected) return;
            this.value = value;
            isResolved = true;
            lock.notifyAll();
        }
    }

    public void reject(RuntimeError error) {
        synchronized (lock) {
            if (isResolved || isRejected) return;
            this.error = error;
            isRejected = true;
            lock.notifyAll();
        }
    }

    public boolean isResolved() {
        synchronized (lock) {
            return isResolved;
        }
    }

    public boolean isRejected() {
        synchronized (lock) {
            return isRejected;
        }
    }

    public Object getValue() {
        synchronized (lock) {
            if (!isResolved && !isRejected) {
                throw new RuntimeError(null, "Cannot get value of unresolved promise");
            }
            if (isRejected) {
                throw error;
            }
            return value;
        }
    }

    public RuntimeError getError() {
        synchronized (lock) {
            return error;
        }
    }

    public void await() throws InterruptedException {
        synchronized (lock) {
            while (!isResolved && !isRejected) {
                lock.wait();
            }
        }
    }
}