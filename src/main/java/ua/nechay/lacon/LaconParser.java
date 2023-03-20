package ua.nechay.lacon;

import ua.nechay.lacon.ast.AST;
import ua.nechay.lacon.ast.BinaryOperationAST;
import ua.nechay.lacon.ast.NumAST;
import ua.nechay.lacon.ast.UnaryOperationAST;

import javax.annotation.Nonnull;
import java.util.EnumSet;
import java.util.Set;

/**
 * @author anechaev
 * @since 06.03.2023
 */
public class LaconParser implements Parser {
    private static final Set<LaconTokenType> TERM_TYPES = EnumSet.of(LaconTokenType.MUL, LaconTokenType.DIV);
    private static final Set<LaconTokenType> EXPRESSION_TYPES = EnumSet.of(LaconTokenType.PLUS, LaconTokenType.MINUS);

    private final Lexer lexer;

    private LaconToken previousToken;
    private LaconToken currentToken;

    public LaconParser(Lexer lexer) {
        this.lexer = lexer;
        this.previousToken = null;
        this.currentToken = lexer.getNextToken(this.previousToken);
    }

    /**
     * Compares the current token type with the passed token type and
     * if they match then "eat" the current token
     * and assign the next token to the {@link this#currentToken},
     * otherwise throws an exception.
     */
    public void eat(@Nonnull LaconTokenType tokenType) {
        if (currentToken.getType() == tokenType) {
            this.previousToken = this.currentToken;
            this.currentToken = lexer.getNextToken(this.previousToken);
        } else {
            throw new IllegalStateException("Illegal syntax: " + currentToken);
        }
    }

    /**
     * INTEGER | LPAREN expr RPAREN
     */
    public AST factor() {
        LaconToken token = this.currentToken;
        LaconTokenType type = token.getType();
        if (type == LaconTokenType.PLUS) {
            eat(LaconTokenType.PLUS);
            return new UnaryOperationAST(token, factor());
        }
        if (type == LaconTokenType.MINUS) {
            eat(LaconTokenType.MINUS);
            return new UnaryOperationAST(token, factor());
        }
        if (type == LaconTokenType.INTEGER) {
            eat(LaconTokenType.INTEGER);
            return new NumAST(token);
        }
        if (type == LaconTokenType.LEFT_BRACKET) {
            eat(LaconTokenType.LEFT_BRACKET);
            AST node = expression();
            eat(LaconTokenType.RIGHT_BRACKET);
            return node;
        }
        return null;
    }

    /**
     * factor ((MUL | DIV) factor)
     */
    public AST term() {
        AST node = factor();
        while (TERM_TYPES.contains(this.currentToken.getType())) {
            LaconToken token = this.currentToken;
            if (token.getType() == LaconTokenType.MUL) {
                this.eat(LaconTokenType.MUL);
            } else if (token.getType() == LaconTokenType.DIV) {
                this.eat(LaconTokenType.DIV);
            }
            node = new BinaryOperationAST(node, token, factor());
        }
        return node;
    }

    /**
     * expr   : term ((PLUS | MINUS) term)
     * term   : factor ((MUL | DIV) factor)
     * factor : INTEGER | LPAREN expr RPAREN
     */
    public AST expression() {
        AST node = term();
        while (EXPRESSION_TYPES.contains(this.currentToken.getType())) {
            LaconToken token = this.currentToken;
            if (token.getType() == LaconTokenType.PLUS) {
                eat(LaconTokenType.PLUS);
            } else if (token.getType() == LaconTokenType.MINUS) {
                eat(LaconTokenType.MINUS);
            }

            node = new BinaryOperationAST(node, token, term());
        }
        return node;
    }

    @Nonnull
    @Override
    public AST parse() {
        return expression();
    }
}
