import Parser.Parser;
import SudokuObjects.Board;
import Tokenizer.Lexer;
import Tokenizer.Tokens;

import java.text.ParseException;

/**
 * Created by james on 22/11/16.
 */
public class Solver {
    public static void main(String[] args) {

        Tokens myTokens = Lexer.produceLexers(
                "[,,,,,,,,]" +
                "[,,,,,,,,]" +
                "[,,,,,,,,]" +
                "[,,,,,,,,]" +
                "[,,,,,,,,]" +
                "[,,,,,,,,]" +
                "[,,,,,,,,]" +
                "[,,,,,,,,]" +
                "[,,,,,,,,]");

        Parser myParser = new Parser(myTokens);
        Board myBoard = new Board(null);
        try {
            myBoard = myParser.parseBoard();
            System.out.println(myBoard);
        } catch (ParseException pe) {
            pe.printStackTrace();
        }

        Solver mySolver = new Solver(myBoard);

    }

    private Board board;
    public Solver(Board myBoard){
        board = myBoard;
    }


    public void DisplayBoard(){

    }
}
