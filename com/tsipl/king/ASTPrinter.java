package com.tsipl.king;

import com.tsipl.king.Expression.Binary;
import com.tsipl.king.Expression.Grouping;
import com.tsipl.king.Expression.Literal;
import com.tsipl.king.Expression.Unary;

class ASTPrinter implements Expression.Visitor<String> {
    String print(Expression expr) {
        return expr.accept(this);
    }

    @Override
    public String visitBinaryExpression(Binary expr) {
        return parenthesize(expr.operator.lexeme, expr.left, expr.right);
    }

    @Override
    public String visitGroupingExpression(Grouping expr) {
        return parenthesize("group", expr.expression);
    }

    @Override
    public String visitLiteralExpression(Literal expr) {
        if (expr.value == null) {
            return "nil";
        }

        return expr.value.toString();
    }

    @Override
    public String visitUnaryExpression(Unary expr) {
        return parenthesize(expr.operator.lexeme, expr.right);
    }

    private String parenthesize(String name, Expression... items) {
        StringBuilder builder = new StringBuilder();

        builder.append("(").append(name);

        for (Expression expr : items) {
            builder.append(" ");
            builder.append(expr.accept(this));
        }

        builder.append(")");

        return builder.toString();
    }

    public static void main(String[] args) {
        // (* (- 123) (group 45.67))
        Expression sample = new Expression.Binary(
                new Expression.Unary(
                        new Token(TokenType.MINUS, "-", null, 1),
                        new Expression.Literal(123)),
                new Token(TokenType.STAR, "*", null, 1),
                new Expression.Grouping(
                        new Expression.Literal(45.57)));
        System.out.println(new ASTPrinter().print(sample));
    }
}
