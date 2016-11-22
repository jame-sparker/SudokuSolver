import Parser.Parser;
import SudokuObjects.Board;
import SudokuObjects.Tile;
import Tokenizer.Lexer;
import Tokenizer.Tokens;

import java.text.ParseException;
import java.util.Set;
import java.util.TreeSet;

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

    public void checkLines(){
        /*Update Row*/
        for (int i = 1; i <= 9; i++){
            Tile[] row = board.getRow(i);
            Set<Integer> determined = getDeterminedValues(row);
            updateLine(row, determined);
        }

        /*Update Column*/
        for (int j = 1; j <= 9; j++){
            Tile[] column = board.getColumn(j);
            Set<Integer> determined = getDeterminedValues(column);
            updateLine(column, determined);
        }
    }

    public void updateLine(Tile[] line, Set<Integer> impossibleValues){
        for (Tile t: line){
            if (!t.isFinalized()) {
                t.updatePossibleValues(impossibleValues);
            }
        }
    }

    public void checkBoxes(){
        for (int i = 1; i <= 3; i++){
            for (int j = 1; j <= 3; j++){
                Tile[][] block = board.getBlock(i, j);
                Set<Integer> determined = getDeterminedValues(block);
                for (Tile[] row: block){
                    updateLine(row, determined);
                }
            }
        }
    }

    public Set<Integer> getDeterminedValues (Tile[] tiles ){
        Set<Integer> determined = new TreeSet<>();
        for (Tile t : tiles){
            determined.addAll(t.getValue());
        }
        return determined;
    }

    public Set<Integer> getDeterminedValues (Tile[][] grid ){
        Set<Integer> determined = new TreeSet<>();
        for (Tile[] tiles : grid){
            determined.addAll(getDeterminedValues(tiles));
        }
        return determined;
    }

    public void checkForSingle(){

    }


    public void DisplayBoard(){
        System.out.println(board);
    }
}
