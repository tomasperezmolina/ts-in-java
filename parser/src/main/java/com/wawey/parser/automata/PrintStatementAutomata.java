package com.wawey.parser.automata;

import com.wawey.lexer.NoTransitionException;
import com.wawey.lexer.Token;
import com.wawey.lexer.TokenType;
import com.wawey.parser.Rule;
import com.wawey.parser.ast.ASTNode;
import com.wawey.parser.ast.NonTerminalNode;

import java.util.Stack;

/**
 * @author Tomas Perez Molina
 */
public class PrintStatementAutomata extends ParserAutomataImpl {
    public PrintStatementAutomata() {
        super(new InitialState());
    }

    @Override
    public ASTNode getResult() {
        return new NonTerminalNode(Rule.PRINT_STATEMENT, stack.peek());
    }

    private static class InitialState implements ParserAutomataState {
        @Override
        public ParserAutomataState transition(Token token, Stack<ASTNode> stack) {
            if (accepts(token)) {
                return new PostPrintState();
            } else throw new NoTransitionException();
        }

        @Override
        public boolean isAcceptable() {
            return false;
        }

        @Override
        public boolean accepts(Token token) {
            return token.getType() == TokenType.PRINT;
        }
    }

    private static class PostPrintState implements ParserAutomataState {
        @Override
        public ParserAutomataState transition(Token token, Stack<ASTNode> stack) {
            if (accepts(token)) {
                return new InnerAutomataState(new AdditiveExpressionAutomata(), RightParenState::new);
            } else throw new NoTransitionException();
        }

        @Override
        public boolean isAcceptable() {
            return false;
        }

        @Override
        public boolean accepts(Token token) {
            return token.getType() == TokenType.LEFT_PAREN;
        }
    }

    private static class RightParenState implements ParserAutomataState {
        @Override
        public ParserAutomataState transition(Token token, Stack<ASTNode> stack) {
            if (accepts(token)) {
                return new AcceptedState();
            } else throw new NoTransitionException();
        }

        @Override
        public boolean isAcceptable() {
            return false;
        }

        @Override
        public boolean accepts(Token token) {
            return token.getType() == TokenType.RIGHT_PAREN;
        }
    }

    private static class AcceptedState implements ParserAutomataState {
        @Override
        public boolean isAcceptable() {
            return true;
        }

        @Override
        public boolean accepts(Token token) {
            return false;
        }

        @Override
        public ParserAutomataState transition(Token token, Stack<ASTNode> stack) {
            throw new NoTransitionException();
        }
    }
}
