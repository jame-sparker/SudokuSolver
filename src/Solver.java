import Parser.Parser;
import SudokuObjects.Board;
import SudokuObjects.Tile;
import SudokuObjects.UnsolvablePuzzleException;
import Tokenizer.Lexer;
import Tokenizer.Tokens;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * Created by james on 22/11/16.
 * TODO: Some functions should be in Board class
 */
public class Solver {
    private static final int ITERATIONS = 15;

    public static void main(String[] args) {

        Tokens myTokens = Lexer.produceLexers(
                "[ , , , ,3,6, ,5, ]" +
                "[3, , , , , ,4, , ]" +
                "[ , ,5,8, ,1, ,3, ]" +
                "[9, ,6, , ,3, , , ]" +
                "[ ,2, , , , , ,8, ]" +
                "[ , , ,4, , ,5, ,6]" +
                "[ ,1, ,7, ,8,3, , ]" +
                "[ , ,7, , , , , ,9]" +
                "[ ,4, ,1,6, , , , ]");

        Parser myParser = new Parser(myTokens);
        Board myBoard = new Board(null);
        try {
            myBoard = myParser.parseBoard();
        } catch (ParseException pe) {
            pe.printStackTrace();
        }

        Solver mySolver = new Solver(myBoard);
        mySolver.solve();
    }

    private Board board;

    public Solver(Board myBoard){
        board = myBoard;
    }

    public void solve(){
        /*Solve for 15 iterations*/
        System.out.print("------ Initial Board ------\n\n");
        displayBoard();
        System.out.println();
        int i = 1;
        for (; i <= 15; i++){
            solveRoutine(i);
            System.out.println(board.detailedRepresentation());
            if (board.isCompleted()) break;
        }

        if (board.isCompleted()) {
            System.out.println("Solved after " + i + " iterations");
        } else {
            System.out.println("Could not solve after " + ITERATIONS + " iterations");
        }

        displayBoard();
        System.out.println(board.detailedRepresentation());
    }

    public void solveRoutine(int it){
        checkLines();
        checkBlocks();
        checkBlockLine();
        checkLineBlock();
        groupChecks();

        try {
            board.updateAllTiles();
        } catch (UnsolvablePuzzleException e) {
            System.err.println("The puzzle is unsolvable after " + it + " iterations");
            displayBoard();
            System.exit(-1);
        }
    }

    public void checkLines(){
        /*Update Row*/
        for (int i = 1; i <= 9; i++){
            checkLine(board.getRow(i));
        }

        /*Update Column*/
        for (int j = 1; j <= 9; j++){
            checkLine(board.getColumn(j));
        }
    }

    public void checkLine(Tile[] line){
        Set<Integer> determined = Board.getDeterminedValues(line);
        updateLine(line, determined);
    }

    public void updateLine(Tile[] line, Set<Integer> impossibleValues){
        for (Tile t: line){
            if (!t.isFinalized()) {
                t.updatePossibleValues(impossibleValues);
            }
        }
    }

    public void checkBlocks(){
        for (int i = 1; i <= 3; i++){
            for (int j = 1; j <= 3; j++){
                Tile[][] block = board.getBlock(i, j);
                Set<Integer> determined = Board.getDeterminedValues(block);
                for (Tile[] row: block){
                    updateLine(row, determined);
                }
            }
        }
    }

    /*If the number only exists along a line in a block,
    * we can eliminate the chance of the number
    * happening in the tiles along the lines */
    public void checkBlockLine(){
        for (int i = 1; i <= 3; i++){
            for (int j = 1; j <= 3; j++){
                Tile[][] block = board.getBlock(i, j);
                Set<Integer> determined = Board.getDeterminedValues(block);
                for (int x = 1; x <= Board.SIZE; x++){
                    //if the number is free in the box
                    if (!determined.contains(x)){
                        Set<Integer> rowValue = new TreeSet<>();
                        Set<Integer> columnValue = new TreeSet<>();

                        for (int a = 0; a < block.length; a++){
                            for (int b = 0; b < block[a].length; b++){
                                Tile t = block[a][b];
                                if (!t.isFinalized() && t.isPossible(x)){
                                    rowValue.add(t.getY());
                                    columnValue.add(t.getX());
                                }
                            }
                        }
                        /*eliminate*/
                        Tile[] lineSubset;
                        if (rowValue.size() == 1) {
                            int yVal = rowValue.iterator().next();
                            lineSubset = board.getRowSubset(yVal, j);
                            for (Tile t : lineSubset){
                                t.updatePossibleValues(x);
                            }
                        } else if (columnValue.size() == 1){
                            int xVal = columnValue.iterator().next();
                            lineSubset = board.getColumnSubset(xVal, i);
                            for (Tile t : lineSubset){
                                t.updatePossibleValues(x);
                            }
                        }

                    }
                }
            }
        }
    }
    /*if the numbers in the line exists only within certain block,
    * then we can eliminate the chance of the number happening in
    * the remaining part of the block.
    * */
    public void checkLineBlock(){
        /*for rows*/
        for(int i = 1; i <= Board.SIZE; i++){
            Tile[] row = board.getRow(i);
            Tile[] rowSubset = Board.getUndeterminedTiles(row);
            Set<Integer> determined = Board.getDeterminedValues(row);
            for(int x = 1; x <= Board.SIZE; x++){
                if (determined.contains(x)) continue;
                Set<Integer> boxNumbers = new TreeSet<>();

                for(int a = 0; a < rowSubset.length; a++){
                    if (rowSubset[a].getPossibleValues().contains(x)){
                        int boxIndex = (rowSubset[a].getX() + 2) / 3;
                        boxNumbers.add(boxIndex);
                    }
                }

                if (boxNumbers.size() == 1) {
                    int xBoxIndex = boxNumbers.iterator().next();
                    int yBoxIndex = ((i - 1) / 3) + 1;
                    Tile[] boxSubset = board.getBlockSubsetWithoutRow(xBoxIndex, yBoxIndex, i);
                    for(Tile t : boxSubset){
                        if (!t.isFinalized())
                            t.updatePossibleValues(x);
                    }
                }
            }
        }
        /*for columns*/

        for(int i = 1; i <= Board.SIZE; i++){
            Tile[] column = board.getColumn(i);
            Tile[] columnSubset = Board.getUndeterminedTiles(column);
            Set<Integer> determined = Board.getDeterminedValues(column);
            for(int x = 1; x <= Board.SIZE; x++){
                if (determined.contains(x)) continue;
                Set<Integer> boxNumbers = new TreeSet<>();

                for(int a = 0; a < columnSubset.length; a++){
                    if (columnSubset[a].getPossibleValues().contains(x)){
                        int boxIndex = (columnSubset[a].getY() + 2) / 3;
                        boxNumbers.add(boxIndex);
                    }
                }

                if (boxNumbers.size() == 1) {
                    int xBoxIndex = ((i - 1) / 3) + 1;
                    int yBoxIndex = boxNumbers.iterator().next();
                    Tile[] boxSubset = board.getBlockSubsetWithoutColumn(xBoxIndex, yBoxIndex, i);
                    for(Tile t : boxSubset){
                        if (!t.isFinalized())
                            t.updatePossibleValues(x);
                    }
                }
            }
        }

    }

    public void groupChecks(){
        for(int i = 1; i <= Board.SIZE; i++){
            Tile[] row    = board.getRow(i);
            Tile[] column = board.getColumn(i);

            groupCheck(row);
            groupCheck(column);
        }

        for(int i = 1; i <= 3; i++){
            for(int j = 1; j <= 3; j++){
                Tile[] block = board.getFlattenedBlock(i, j);
                groupCheck(block);
            }
        }

    }

    /* If there are n possible numbers in n tiles within a block or line,
    * then those numbers are not possible in other tiles in the corresponding area */
    public void groupCheck(Tile[] tiles) {
        /*for every not finalized tile, check whether the */
        Tile[] udt = Board.getUndeterminedTiles(tiles);

        for (int i = 0; i < udt.length; i++){
            Set<Integer> possibleValues = udt[i].getPossibleValues();
            if (possibleValues.size() == udt.length) continue; // no point in analyzing
            List<Tile> nonSubsets = new ArrayList<>();
            int count = 1;
            for (int j = 0; j < udt.length; j++){
                if (i == j) continue;
                Set<Integer> thisValues = udt[j].getPossibleValues();
                if (possibleValues.containsAll(thisValues)) {
                    count++;
                } else {
                    nonSubsets.add(udt[j]);
                }
            }
            assert count <= possibleValues.size();
            if (count == possibleValues.size()){
                for (Tile t : nonSubsets){
                    t.updatePossibleValues(possibleValues);
                }
            }
        }
    }


    public void displayBoard(){
        System.out.println(board);
    }
}
