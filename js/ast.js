"use strict";

function Expr() {}
function Stmt() {}

function Program(statements) {
  this.statements = statements;
}

exports.Expr = Expr;
exports.Stmt = Stmt;
exports.Program = Program;

function defineAst(name, baseClass, baseName, fields) {
  var constructor = function() {
    for (var i = 0; i < fields.length; i++) {
      this[fields[i]] = arguments[i];
    }
  }

  constructor.prototype = Object.create(baseClass.prototype);

  constructor.prototype.accept = function(visitor, data) {
    return visitor["visit" + name + baseName](this, data);
  }

  exports[name + baseName] = constructor;
}

function defineExpr(name, fields) {
  defineAst(name, Expr, "Expr", fields);
}

function defineStmt(name, fields) {
  defineAst(name, Stmt, "Stmt", fields);
}

defineExpr("Assign",      ["target", "value"]);
defineExpr("Binary",      ["left", "op", "right"]);
defineExpr("Call",        ["fn", "args"]);
defineExpr("Logical",     ["left", "op", "right"]);
defineExpr("Number",      ["value"]);
defineExpr("Property",    ["object", "name"]);
defineExpr("String",      ["value"]);
defineExpr("Unary",       ["op", "right"]);
defineExpr("Variable",    ["name"]);

defineStmt("Block",       ["statements"]);
defineStmt("Class",       ["name", "superclass", "methods"]);
defineStmt("Expression",  ["expression"]);
defineStmt("For",         ["name", "iterator", "body"]);
defineStmt("Fun",         ["name", "parameters", "body"]);
defineStmt("If",          ["condition", "thenBranch", "elseBranch"]);
defineStmt("Return",      ["value"]);
defineStmt("Var",         ["name", "initializer"]);
defineStmt("While",       ["condition", "body"]);

// Old manual code:
/*
function Expr() {
}

function BinaryExpr(left, op, right) {
  Expr.call(this);
  this.left = left;
  this.op = op;
  this.right = right;
}

BinaryExpr.prototype = Object.create(Expr.prototype);

BinaryExpr.prototype.accept = function(visitor) {
  return visitor.visitBinaryExpr(this);
}

function CallExpr(fn, args) {
  Expr.call(this);
  this.fn = fn;
  this.args = args;
}

CallExpr.prototype = Object.create(Expr.prototype);

CallExpr.prototype.accept = function(visitor) {
  return visitor.visitCallExpr(this);
}

function NumberExpr(value) {
  Expr.call(this);
  this.value = value;
}

NumberExpr.prototype = Object.create(Expr.prototype);

NumberExpr.prototype.accept = function(visitor) {
  return visitor.visitNumberExpr(this);
}

function StringExpr(value) {
  Expr.call(this);
  this.value = value;
}

StringExpr.prototype = Object.create(Expr.prototype);

StringExpr.prototype.accept = function(visitor) {
  return visitor.visitStringExpr(this);
}

function UnaryExpr(op, right) {
  Expr.call(this);
  this.op = op;
  this.right = right;
}

UnaryExpr.prototype = Object.create(Expr.prototype);

UnaryExpr.prototype.accept = function(visitor) {
  return visitor.visitUnaryExpr(this);
}

function VariableExpr(name) {
  Expr.call(this);
  this.name = name;
}

VariableExpr.prototype = Object.create(Expr.prototype);

VariableExpr.prototype.accept = function(visitor) {
  return visitor.visitVariableExpr(this);
}

function Stmt() {
}

function BlockStmt(statements) {
  Stmt.call(this);
  this.statements = statements;
}

BlockStmt.prototype = Object.create(Stmt.prototype);

BlockStmt.prototype.accept = function(visitor) {
  return visitor.visitBlockStmt(this);
}

function ExpressionStmt(expression) {
  Stmt.call(this);
  this.expression = expression;
}

ExpressionStmt.prototype = Object.create(Stmt.prototype);

ExpressionStmt.prototype.accept = function(visitor) {
  return visitor.visitExpressionStmt(this);
}

exports.Expr = Expr;
exports.BinaryExpr = BinaryExpr;
exports.CallExpr = CallExpr;
exports.NumberExpr = NumberExpr;
exports.StringExpr = StringExpr;
exports.UnaryExpr = UnaryExpr;
exports.VariableExpr = VariableExpr;
exports.Stmt = Stmt;
exports.BlockStmt = BlockStmt;
exports.ExpressionStmt = ExpressionStmt;
*/
