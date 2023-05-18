package ua.nechay.lacon;

import ua.nechay.lacon.ast.AST;
import ua.nechay.lacon.ast.AssignmentAST;
import ua.nechay.lacon.ast.BinaryOperationAST;
import ua.nechay.lacon.ast.ForCycleAST;
import ua.nechay.lacon.ast.InAST;
import ua.nechay.lacon.ast.RangeAST;
import ua.nechay.lacon.ast.VoidAST;
import ua.nechay.lacon.ast.call.CallAST;
import ua.nechay.lacon.ast.CastAST;
import ua.nechay.lacon.ast.ConditionsAST;
import ua.nechay.lacon.ast.EmptyAST;
import ua.nechay.lacon.ast.FunctionArgumentDeclarationAST;
import ua.nechay.lacon.ast.FunctionDeclarationAST;
import ua.nechay.lacon.ast.IndexableAST;
import ua.nechay.lacon.ast.SemicolonAST;
import ua.nechay.lacon.ast.StatementListAST;
import ua.nechay.lacon.ast.UnaryOperationAST;
import ua.nechay.lacon.ast.VariableDeclarationAST;
import ua.nechay.lacon.ast.VariableReferenceAST;
import ua.nechay.lacon.ast.WhileCycleAST;
import ua.nechay.lacon.ast.call.MethodCallAST;
import ua.nechay.lacon.ast.call.NamedCallArgsAST;
import ua.nechay.lacon.ast.call.PositionCallArgsAST;
import ua.nechay.lacon.ast.value.BooleanAST;
import ua.nechay.lacon.ast.value.DictAST;
import ua.nechay.lacon.ast.value.IntNumAST;
import ua.nechay.lacon.ast.value.ListAST;
import ua.nechay.lacon.ast.value.RealNumAST;
import ua.nechay.lacon.ast.value.StringAST;
import ua.nechay.lacon.utils.LaconUtils;
import ua.nechay.lacon.utils.Pair;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author anechaev
 * @since 06.03.2023
 */
public class LaconParser implements Parser {
    private static final Set<LaconTokenType> AFTER_FACTOR_TYPES = EnumSet.of(
        LaconTokenType.LEFT_SQUARE_BRACKET,
        LaconTokenType.LEFT_BRACKET,
        LaconTokenType.DOT);
    private static final Set<LaconTokenType> TERM_TYPES = EnumSet.of(LaconTokenType.MUL, LaconTokenType.DIV, LaconTokenType.MODULUS);
    private static final Set<LaconTokenType> NOTION_TYPES = EnumSet.of(LaconTokenType.PLUS, LaconTokenType.MINUS);
    private static final Set<LaconTokenType> ALLEGATION_TYPES = EnumSet.of(
        LaconTokenType.EQUALS,
        LaconTokenType.NOT_EQUALS,
        LaconTokenType.LESS,
        LaconTokenType.LESS_OR_EQUAL,
        LaconTokenType.GREATER,
        LaconTokenType.GREATER_OR_EQUAL,
        LaconTokenType.IN);

    private final Lexer lexer;

    private LaconToken previousToken;
    private LaconToken currentToken;

    public LaconParser(Lexer lexer) {
        this.lexer = lexer;
        this.previousToken = null;
        this.currentToken = lexer.getNextToken(getPreviousToken());
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

    private Pair<AST,AST> parseChainedCondition() {
        AST ifCondition = expression();
        eat(LaconTokenType.LEFT_CURLY_BRACKET);
        AST ifStatement = new StatementListAST(statementList());
        eat(LaconTokenType.RIGHT_CURLY_BRACKET);
        return Pair.of(ifCondition, ifStatement);
    }

    public AST condition() {
        eat(LaconTokenType.IF);
        List<Pair<AST,AST>> chainedConditions = new ArrayList<>();
        chainedConditions.add(parseChainedCondition());
        while (getCurrentToken().getType() == LaconTokenType.ELIF) {
            eat(LaconTokenType.ELIF);
            chainedConditions.add(parseChainedCondition());
        }
        AST elseStatement = null;
        if (getCurrentToken().getType() == LaconTokenType.ELSE) {
            eat(LaconTokenType.ELSE);
            eat(LaconTokenType.LEFT_CURLY_BRACKET);
            elseStatement = new StatementListAST(statementList());
            eat(LaconTokenType.RIGHT_CURLY_BRACKET);
        }
        return new ConditionsAST(chainedConditions, elseStatement);
    }

    /**
     * INTEGER | LPAREN expr RPAREN
     */
    public AST factor() {
        LaconToken token = getCurrentToken();
        LaconTokenType type = token.getType();
        if (type == LaconTokenType.IF) {
            return condition();
        }
        if (type == LaconTokenType.PLUS) {
            eat(LaconTokenType.PLUS);
            return new UnaryOperationAST(token, factor());
        }
        if (type == LaconTokenType.MINUS) {
            eat(LaconTokenType.MINUS);
            return new UnaryOperationAST(token, factor());
        }
        if (type == LaconTokenType.NOT) {
            eat(LaconTokenType.NOT);
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
        if (type == LaconTokenType.BOOLEAN) {
            eat(LaconTokenType.BOOLEAN);
            return new BooleanAST(token);
        }
        if (type == LaconTokenType.IDENTIFIER) {
            eat(LaconTokenType.IDENTIFIER);
            return new VariableReferenceAST(token);
        }
        if (type == LaconTokenType.QUOTE) {
            eat(LaconTokenType.QUOTE);
            LaconToken strToken = getCurrentToken();
            if (strToken.getType() != LaconTokenType.STRING) {
                throw new IllegalStateException("Illegal token: " + strToken);
            }
            eat(LaconTokenType.STRING);
            eat(LaconTokenType.QUOTE);
            return new StringAST(strToken);
        }
        if (type == LaconTokenType.CAST) {
            eat(LaconTokenType.CAST);
            LaconToken leftBracketToken = getCurrentToken();
            if (leftBracketToken.getType() != LaconTokenType.LEFT_BRACKET) {
                throw new IllegalStateException("Illegal token: " + leftBracketToken);
            }
            eat(LaconTokenType.LEFT_BRACKET);
            AST nodeToCast = expression();
            eat(LaconTokenType.RIGHT_BRACKET);
            return new CastAST(token, nodeToCast);
        }
        if (type == LaconTokenType.LEFT_BRACKET) {
            eat(LaconTokenType.LEFT_BRACKET);
            AST node = expression();
            eat(LaconTokenType.RIGHT_BRACKET);
            return node;
        }
        if (type == LaconTokenType.LEFT_SQUARE_BRACKET) {;
            eat(LaconTokenType.LEFT_SQUARE_BRACKET);
            LaconToken nextToken = getCurrentToken();
            if (nextToken.getType() == LaconTokenType.RIGHT_SQUARE_BRACKET) {
                eat(LaconTokenType.RIGHT_SQUARE_BRACKET);
                return new ListAST();
            } else if (nextToken.getType() == LaconTokenType.RANGE) {
                eat(LaconTokenType.RANGE);
                AST endBound = expression();
                eat(LaconTokenType.RIGHT_SQUARE_BRACKET);
                return RangeAST.createDefault(endBound);
            }
            AST firstAST = expression();
            if (getCurrentToken().getType() == LaconTokenType.RANGE) {
                eat(LaconTokenType.RANGE);
                AST secondAST = expression();
                if (getCurrentToken().getType() == LaconTokenType.RIGHT_SQUARE_BRACKET) {
                    eat(LaconTokenType.RIGHT_SQUARE_BRACKET);
                    return RangeAST.createDefault(firstAST, secondAST);
                }
                eat(LaconTokenType.RANGE);
                AST thirdAST = expression();
                eat(LaconTokenType.RIGHT_SQUARE_BRACKET);
                return new RangeAST(firstAST, secondAST, thirdAST);
            }
            List<AST> listNodes = new ArrayList<>();
            listNodes.add(firstAST);
            while (getCurrentToken().getType() == LaconTokenType.COMA) {
                eat(LaconTokenType.COMA);
                if (getCurrentToken().getType() == LaconTokenType.RIGHT_BRACKET) {
                    break;
                }
                listNodes.add(expression());
            }
            eat(LaconTokenType.RIGHT_SQUARE_BRACKET);
            return new ListAST(listNodes);
        }
        if (type == LaconTokenType.LEFT_CURLY_BRACKET) {
            eat(LaconTokenType.LEFT_CURLY_BRACKET);
            List<Pair<AST, AST>> entries = new ArrayList<>();
            while (getCurrentToken().getType() != LaconTokenType.RIGHT_CURLY_BRACKET) {
                AST key = expression();
                eat(LaconTokenType.COLON);
                AST value = expression();
                entries.add(new Pair<>(key, value));
                if (getCurrentToken().getType() == LaconTokenType.COMA) {
                    eat(LaconTokenType.COMA);
                }
            }
            eat(LaconTokenType.RIGHT_CURLY_BRACKET);
            return new DictAST(entries);
        }
        return null;
    }

    public AST namedCall(LaconToken firstIdentifier) {
        Map<LaconToken, AST> result = new HashMap<>();
        AST firstExpression = expression();
        result.put(firstIdentifier, firstExpression);
        while (getCurrentToken().getType() == LaconTokenType.COMA) {
            eat(LaconTokenType.COMA);
            LaconToken nextIdentifier = getCurrentToken();
            eat(LaconTokenType.IDENTIFIER);
            eat(LaconTokenType.ASSIGNMENT);
            AST nextAST = expression();
            result.put(nextIdentifier, nextAST);
        }
        eat(LaconTokenType.RIGHT_BRACKET);
        return new NamedCallArgsAST(result);
    }

    public AST positionCall(AST firstArgument) {
        List<AST> result = new ArrayList<>();
        result.add(firstArgument);
        while (getCurrentToken().getType() == LaconTokenType.COMA) {
            eat(LaconTokenType.COMA);
            result.add(expression());
        }
        eat(LaconTokenType.RIGHT_BRACKET);
        return new PositionCallArgsAST(result);
    }

    public AST call() {
        eat(LaconTokenType.LEFT_BRACKET);
        if (getCurrentToken().getType() == LaconTokenType.RIGHT_BRACKET) {
            eat(LaconTokenType.RIGHT_BRACKET);
            return new PositionCallArgsAST(Collections.emptyList());
        } else if (getCurrentToken().getType() == LaconTokenType.IDENTIFIER) {
            LaconToken identifier = getCurrentToken();
            eat(LaconTokenType.IDENTIFIER);
            if (getCurrentToken().getType() == LaconTokenType.ASSIGNMENT) {
                eat(LaconTokenType.ASSIGNMENT);
                return namedCall(identifier);
            }
            return positionCall(expression(new VariableReferenceAST(identifier)));
        }
        return positionCall(expression());
    }

    public AST index() {
        eat(LaconTokenType.LEFT_SQUARE_BRACKET);
        AST indexNode = expression();
        eat(LaconTokenType.RIGHT_SQUARE_BRACKET);
        return indexNode;
    }

    public AST handlePostfix(AST factor) {
        AST node = factor;
        while (AFTER_FACTOR_TYPES.contains(getCurrentToken().getType())) {
            if (getCurrentToken().getType() == LaconTokenType.LEFT_SQUARE_BRACKET) {
                node = new IndexableAST(node, index());
            }
            if (getCurrentToken().getType() == LaconTokenType.LEFT_BRACKET) {
                node = new CallAST(node, call());
            }
            if (getCurrentToken().getType() == LaconTokenType.DOT) {
                eat(LaconTokenType.DOT);
                LaconToken methodIdentifier = getCurrentToken();
                eat(LaconTokenType.IDENTIFIER);
                node = new MethodCallAST(node, methodIdentifier, call());
            }
        }
        return node;
    }

    public AST postfix() {
        return handlePostfix(factor());
    }

    public AST handleTerm(AST postFix) {
        AST node = postFix;
        while (TERM_TYPES.contains(getCurrentToken().getType())) {
            LaconToken token = getCurrentToken();
            if (token.getType() == LaconTokenType.MUL) {
                eat(LaconTokenType.MUL);
            } else if (token.getType() == LaconTokenType.DIV) {
                eat(LaconTokenType.DIV);
            } else if (token.getType() == LaconTokenType.MODULUS) {
                eat(LaconTokenType.MODULUS);
            }
            node = new BinaryOperationAST(node, token, postfix());
        }
        return node;
    }

    /**
     * factor ((MUL | DIV) factor)
     */
    public AST term() {
        return handleTerm(postfix());
    }

    public AST handleNotion(AST term) {
        AST node = term;
        while (NOTION_TYPES.contains(getCurrentToken().getType())) {
            LaconToken token = getCurrentToken();
            if (token.getType() == LaconTokenType.PLUS) {
                eat(LaconTokenType.PLUS);
            } else if (token.getType() == LaconTokenType.MINUS) {
                eat(LaconTokenType.MINUS);
            }
            node = new BinaryOperationAST(node, token, term());
        }
        return node;
    }


    /**
     * expr   : term ((PLUS | MINUS) term)
     * term   : factor ((MUL | DIV) factor)
     * factor : INTEGER | LPAREN expr RPAREN
     */
    public AST notion() {
        return handleNotion(term());
    }

    public AST handleAllegation(AST notion) {
        AST node = notion;
        while (ALLEGATION_TYPES.contains(getCurrentToken().getType())) {
            LaconToken token = getCurrentToken();
            if (token.getType() == LaconTokenType.IN) {
                eat(LaconTokenType.IN);
                node = new InAST(node, notion());
                continue;
            }
            eat(token.getType());
            node = new BinaryOperationAST(node, token, notion());
        }
        return node;
    }

    public AST allegation() {
        return handleAllegation(notion());
    }

    public AST handleAssumption(AST allegation) {
        AST node = allegation;
        while (getCurrentToken().getType() == LaconTokenType.OR) {
            LaconToken token = getCurrentToken();
            eat(LaconTokenType.OR);
            node = new BinaryOperationAST(node, token, allegation());
        }
        return node;
    }

    public AST assumption() {
        return handleAssumption(allegation());
    }

    public AST handleExpression(AST assumption) {
        AST node = assumption;
        while (getCurrentToken().getType() == LaconTokenType.AND) {
            LaconToken token = getCurrentToken();
            eat(LaconTokenType.AND);
            node = new BinaryOperationAST(node, token, assumption());
        }
        return node;
    }

    public AST expression(@Nonnull AST factor) {
        return LaconUtils.compose(factor, List.of( //in order of calling
            this::handlePostfix,
            this::handleTerm,
            this::handleNotion,
            this::handleAllegation,
            this::handleAssumption,
            this::handleExpression));
    }

    public AST expression() {
        return handleExpression(assumption());
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
        }
        return expression(left);
    }

    public List<AST> argumentsDeclaration() {
        List<AST> argDeclarations = new ArrayList<>();
        Set<String> argNames = new HashSet<>();
        while (getCurrentToken().getType() == LaconTokenType.IDENTIFIER) {
            LaconToken id = getCurrentToken();
            eat(LaconTokenType.IDENTIFIER);
            eat(LaconTokenType.COLON);
            LaconToken type = getCurrentToken();
            eat(LaconTokenType.TYPE);
            if (!argNames.add(id.getText())) {
                throw new IllegalStateException("Unable to declare arguments with non-unique names: " + id.getText());
            }
            argDeclarations.add(new FunctionArgumentDeclarationAST(id, type));
            if (getCurrentToken().getType() == LaconTokenType.COMA) {
                eat(LaconTokenType.COMA);
            }
        }
        return argDeclarations;
    }

    public AST function() {
        eat(LaconTokenType.DEF);
        LaconToken identifier = getCurrentToken();
        eat(LaconTokenType.IDENTIFIER);
        eat(LaconTokenType.LEFT_BRACKET);
        List<AST> argumentDeclaration = argumentsDeclaration();
        eat(LaconTokenType.RIGHT_BRACKET);
        eat(LaconTokenType.COLON);
        LaconToken returnType = getCurrentToken();
        eat(LaconTokenType.TYPE);
        AST functionBody = compoundStatement();
        return new FunctionDeclarationAST(identifier, argumentDeclaration, returnType, functionBody);
    }

    public AST whileCycle() {
        eat(LaconTokenType.WHILE);
        AST conditionExpression = expression();
        AST whileBlock = compoundStatement();
        return new WhileCycleAST(conditionExpression, whileBlock);
    }

    public AST forCycle() {
        eat(LaconTokenType.FOR);
        LaconToken identifier = getCurrentToken();
        eat(LaconTokenType.IDENTIFIER);
        eat(LaconTokenType.IN);
        AST iterable = expression();
        AST forBlock = compoundStatement();
        return new ForCycleAST(identifier, iterable, forBlock);
    }

    public AST statement() {
        LaconTokenType currentType = getCurrentToken().getType();
        if (currentType == LaconTokenType.SEMICOLON) {
            eat(LaconTokenType.SEMICOLON);
            return new SemicolonAST();
        }
        if (currentType == LaconTokenType.EOF) {
            return new EmptyAST();
        }
        if (currentType == LaconTokenType.COMMENT) {
            eat(LaconTokenType.COMMENT);
            return new EmptyAST();
        }
        if (currentType == LaconTokenType.RIGHT_CURLY_BRACKET) {
            return new EmptyAST();
        }
        if (currentType == LaconTokenType.WHILE) {
            return whileCycle();
        }
        if (currentType == LaconTokenType.FOR) {
            return forCycle();
        }
        if (currentType != LaconTokenType.IDENTIFIER) {
            return expression();
        }
        return assignOrDeclarationStatement();
    }

    public List<AST> statementList() {
        List<AST> results = new ArrayList<>();
        if (getCurrentToken().getType() == LaconTokenType.RETURN) {
            eat(LaconTokenType.RETURN);
            if (getCurrentToken().getType() == LaconTokenType.SEMICOLON) {
                results.add(new VoidAST());
                return results;
            }
            AST expression = expression();
            results.add(expression);
            eatAllSemicolons();
            return results;
        }
        AST firstStatement = statement();
        results.add(firstStatement);

        while (getCurrentToken().getType() == LaconTokenType.SEMICOLON
            || getCurrentToken().getType() != LaconTokenType.RIGHT_CURLY_BRACKET)
        {
            if (getCurrentToken().getType() == LaconTokenType.SEMICOLON) {
                eat(LaconTokenType.SEMICOLON);
            }
            if (getCurrentToken().getType() == LaconTokenType.RETURN) {
                eat(LaconTokenType.RETURN);
                if (getCurrentToken().getType() == LaconTokenType.SEMICOLON) {
                    results.add(new VoidAST());
                    break;
                }
                results.add(expression());
                break;
            }
            results.add(statement());
        }
        eatAllSemicolons();
        if (getCurrentToken().getType() == LaconTokenType.IDENTIFIER) {
            throw new IllegalStateException("Unexpected token: " + getCurrentToken() + ". It should be the }");
        }
        return results;
    }

    public void eatAllSemicolons() {
        while (getCurrentToken().getType() == LaconTokenType.SEMICOLON) {
            eat(LaconTokenType.SEMICOLON);
        }
    }

    public AST compoundStatement() {
        eat(LaconTokenType.LEFT_CURLY_BRACKET);
        List<AST> nodes = statementList();
        eat(LaconTokenType.RIGHT_CURLY_BRACKET);
        return new StatementListAST(nodes);
    }

    public AST program() {
        eat(LaconTokenType.LEFT_CURLY_BRACKET);
        List<AST> result = new ArrayList<>();
        while (getCurrentToken().getType() == LaconTokenType.DEF) {
            result.add(function());
        }
        if (getCurrentToken().getType() == LaconTokenType.RIGHT_CURLY_BRACKET) {
            eat(LaconTokenType.RIGHT_CURLY_BRACKET);
        }
        result.add(new StatementListAST(statementList()));
        eat(LaconTokenType.RIGHT_CURLY_BRACKET);
        return new StatementListAST(result);
    }

    @Nonnull
    @Override
    public AST parse() {
        var resultNode = program();
        if (getCurrentToken().getType() != LaconTokenType.EOF) {
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
