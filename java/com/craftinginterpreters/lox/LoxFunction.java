//> Functions lox-function
package com.craftinginterpreters.lox;

import java.util.List;

class LoxFunction implements LoxCallable {
  private final Stmt.Function declaration;
  private final Environment closure;
  private final boolean isInitializer;
  private final boolean isAsync;

  LoxFunction(Stmt.Function declaration, Environment closure,
              boolean isInitializer, boolean isAsync) {
    this.isInitializer = isInitializer;
    this.closure = closure;
    this.declaration = declaration;
    this.isAsync = isAsync;
  }

  LoxFunction bind(LoxInstance instance) {
    Environment environment = new Environment(closure);
    environment.define("this", instance);
    return new LoxFunction(declaration, environment,
                           isInitializer, isAsync);
  }

  @Override
  public String toString() {
    return "<fn " + declaration.name.lexeme + ">";
  }

  @Override
  public int arity() {
    return declaration.params.size();
  }

  @Override
  public Object call(Interpreter interpreter,
                     List<Object> arguments) {
    Environment environment = new Environment(closure);
    for (int i = 0; i < declaration.params.size(); i++) {
      environment.define(declaration.params.get(i).lexeme,
          arguments.get(i));
    }

    if (isAsync) {
      System.out.println("[DEBUG] Starting async function: " + declaration.name.lexeme);
      LoxPromise promise = new LoxPromise();
      // Execute asynchronously
      Thread asyncThread = new Thread(() -> {
        try {
          System.out.println("[DEBUG] Executing async function body: " + declaration.name.lexeme);
          Object result = executeBody(interpreter, environment);
          System.out.println("[DEBUG] Async function completed: " + declaration.name.lexeme + " with result: " + result);
          promise.resolve(result);
        } catch (RuntimeError error) {
          System.out.println("[DEBUG] Async function error: " + declaration.name.lexeme + " - " + error.getMessage());
          promise.reject(error);
        } catch (Exception e) {
          System.out.println("[DEBUG] Async function unexpected error: " + declaration.name.lexeme + " - " + e.getMessage());
          promise.reject(new RuntimeError(declaration.name, 
              "Async function threw an unexpected error: " + e.getMessage()));
        }
      });
      asyncThread.setUncaughtExceptionHandler((thread, ex) -> {
        System.out.println("[DEBUG] Async function uncaught error: " + declaration.name.lexeme + " - " + ex.getMessage());
        promise.reject(new RuntimeError(declaration.name,
            "Uncaught error in async function: " + ex.getMessage()));
      });
      asyncThread.start();
      return promise;
    }

    return executeBody(interpreter, environment);
  }

  private Object executeBody(Interpreter interpreter, Environment environment) {
    try {
      interpreter.executeBlock(declaration.body, environment);
    } catch (Return returnValue) {
      if (isInitializer) return closure.getAt(0, "this");
      return returnValue.value;
    }

    if (isInitializer) return closure.getAt(0, "this");
    return null;
  }

  public boolean isAsync() {
    return isAsync;
  }
}
