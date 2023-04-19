package ua.nechay.lacon;

import ua.nechay.lacon.ast.AST;
import ua.nechay.lacon.ast.AssignmentAST;
import ua.nechay.lacon.ast.BinaryOperationAST;
import ua.nechay.lacon.ast.EmptyAST;
import ua.nechay.lacon.ast.SemicolonAST;
import ua.nechay.lacon.ast.VariableDeclarationAST;
import ua.nechay.lacon.ast.IntNumAST;
import ua.nechay.lacon.ast.RealNumAST;
import ua.nechay.lacon.ast.StatementListAST;
import ua.nechay.lacon.ast.UnaryOperationAST;
import ua.nechay.lacon.ast.VariableReferenceAST;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
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
            throw new IllegalStateException("Illegal syntax: " + currentToken + " at position: " + currentToken.getStartPos());
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
            return new IntNumAST(token);
        }
        if (type == LaconTokenType.REAL) {
            eat(LaconTokenType.REAL);
            return new RealNumAST(token);
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
                eat(LaconTokenType.MUL);
            } else if (token.getType() == LaconTokenType.DIV) {
                eat(LaconTokenType.DIV);
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

    public AST assignOrDeclarationStatement() {
        LaconToken identifier = getCurrentToken();
        AST left = new VariableReferenceAST(identifier);
        eat(LaconTokenType.IDENTIFIER);
        if (getCurrentToken().getType() == LaconTokenType.COLON) {
            eat(LaconTokenType.COLON);
            if (getCurrentToken().getType() != LaconTokenType.TYPE) {
                throw new IllegalStateException("Type expected! Has been gotten instead: " + getCurrentToken());
            }
            LaconToken type = getCurrentToken();
            left = new VariableDeclarationAST(identifier, type);
            eat(LaconTokenType.TYPE);
            if (getCurrentToken().getType() == LaconTokenType.SEMICOLON) {
                return left;
            }
        }
        if (getCurrentToken().getType() == LaconTokenType.ASSIGNMENT) {
            LaconToken assignment = getCurrentToken();
            eat(LaconTokenType.ASSIGNMENT);
            return new AssignmentAST(left, assignment, expression());
        } else {
            throw new IllegalStateException("Unexpected token: " + getCurrentToken() + ". '=' expected");
        }
    }

    public AST statement() {
        if (getCurrentToken().getType() == LaconTokenType.SEMICOLON) {
            eat(LaconTokenType.SEMICOLON);
            return new SemicolonAST();
        }
        if (getCurrentToken().getType() == LaconTokenType.RIGHT_CURLY_BRACKET) {
            return new EmptyAST();
        }
        if (getCurrentToken().getType() != LaconTokenType.IDENTIFIER) {
            return expression();
        }
        return assignOrDeclarationStatement();
    }

    public List<AST> statementList() {
        List<AST> results = new ArrayList<>();
        AST firstStatement = statement();
        results.add(firstStatement);

        while (getCurrentToken().getType() == LaconTokenType.SEMICOLON) {
            eat(LaconTokenType.SEMICOLON);
            results.add(statement());
        }
        if (getCurrentToken().getType() == LaconTokenType.IDENTIFIER) {
            throw new IllegalStateException("Unexpected token: " + getCurrentToken() + ". It should be the }");
        }
        return results;
    }

    public AST compoundStatement() {
        eat(LaconTokenType.LEFT_CURLY_BRACKET);
        List<AST> nodes = statementList();
        eat(LaconTokenType.RIGHT_CURLY_BRACKET);
        return new StatementListAST(nodes);
    }

    public AST program() {
        return compoundStatement();
    }

    @Nonnull
    @Override
    public AST parse() {
        var resultNode = program();
        if (this.currentToken.getType() != LaconTokenType.EOF) {
            throw new IllegalStateException("Unexpected token: " + getCurrentToken());
        }
        return resultNode;
    }

    public LaconToken getCurrentToken() {
        return currentToken;
    }

    public LaconToken getPreviousToken() {
        return previousToken;
    }
}
