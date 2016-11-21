package Parser;

import SudokuObjects.Board;
import SudokuObjects.Tile;
import Tokenizer.AbstractToken;
import Tokenizer.TokenType;
import Tokenizer.Tokens;

import java.text.ParseException;
import java.util.List;

import static Tokenizer.TokenType.*;

/**
 * Created by james on 22/11/16.
 */
public class Parser {
    private Tokens tokens;

    public Parser(Tokens t){
        tokens = t;
    }

    public Board parseBoard() throws ParseException{
        Board board;
        Tile[][] tiles = new Tile[Board.SIZE][Board.SIZE];
        char symbol;
        boolean isNumberBefore = false;
        int i = -1, j = -1;
        try {
            for (i = 0; i < Board.SIZE; i++) {
                /*First character in a row*/
                AbstractToken t = tokens.next();
                if (t.getTokenType() == TokenType.Symbol){
                    symbol = (char)t.getValue();
                    if (symbol != '[') {
                        throw new ParseException("Not the right token on line", i);
                    }
                } else {
                    throw new ParseException("Not the right type of token on line", i);
                }
                /*next characters representing the row*/
                for (j = 0; j < Board.SIZE - 1; j++) {
                    t = tokens.next();
                    if (t.getTokenType() == TokenType.Number) {
                        int tileValue = (int)t.getValue();
                        tiles[i][j] = new Tile(tileValue);

                        /*Consume extra comma*/
                        t = tokens.next();
                        if (t.getTokenType() != TokenType.Symbol) {
                            throw new ParseException("Found two consecutive numbers", i);
                        }
                        symbol = (char)t.getValue();
                        if (symbol != ','){
                            throw new ParseException("Not the right token on line " + i + "\n Instead found " + symbol, i);
                        }
                    } else {
                        symbol = (char)t.getValue();
                        if (symbol != ',') {
                            throw new ParseException("Not the right token on line " + i + "\n Instead found " + symbol, i);
                        }
                        tiles[i][j] = new Tile();
                    }
                }
                /*the last character in the row*/
                t = tokens.next();
                if (t.getTokenType() == TokenType.Symbol){
                    symbol = (char)t.getValue();
                    tiles[i][Board.SIZE - 1] = new Tile();
                    if (symbol != ']') {
                        throw new ParseException("Not the right token on line. Found " + symbol, i);
                    }
                } else {
                    int tileValue = (int)t.getValue();
                    tiles[i][Board.SIZE - 1] = new Tile (tileValue);
                    t = tokens.next();
                    if (t.getTokenType() == TokenType.Symbol) {
                        symbol = (char) t.getValue();
                        if (symbol != ']') {
                            throw new ParseException("Not the right token on line", i);
                        }
                    } else {
                        throw new ParseException("Not the right type of token on line", i);
                    }
                }
            }
        } catch (IndexOutOfBoundsException ioe){
            System.err.printf("Index out of bounds error at (%d, %d)\n", i, j);
        }

        board = new Board(tiles);
        return board;
    }
}
