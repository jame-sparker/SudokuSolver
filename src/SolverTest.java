import SudokuObjects.Board;
import SudokuObjects.Tile;
import Tokenizer.Lexer;
import Tokenizer.Tokens;
import org.junit.Test;
import Parser.Parser;

import java.text.ParseException;

import static org.junit.Assert.*;

/**
 * Created by james on 22/11/16.
 */
public class SolverTest {
    private static Solver mySolver;
    static{
        Tokens myTokens = Lexer.produceLexers(
                        "[3, ,5,6, , , , ,2]" +
                        "[ ,9, , , ,4,5, ,1]" +
                        "[ , ,2,5, ,8, , , ]" +
                        "[ , ,1, , , , ,9,5]" +
                        "[8, , , ,5,2, , ,6]" +
                        "[ ,7,6, , ,9, ,2, ]" +
                        "[9, ,8,1, ,7, , , ]" +
                        "[2,1, ,8, ,5, ,6,7]" +
                        "[ ,6,4, , , , ,5,8]");

        Parser myParser = new Parser(myTokens);
        Board myBoard = new Board(null);
        try {
            myBoard = myParser.parseBoard();
        } catch (ParseException pe) {
            pe.printStackTrace();
        }

        mySolver = new Solver(myBoard);

    }

    @Test
    public void testSolve() throws Exception {

    }

    @Test
    public void testCheckLines() throws Exception {
        Tile[] line = {
                new Tile(1),
                new Tile(2),
                new Tile(3),
                new Tile(4),
                new Tile(5),
                new Tile(6),
                new Tile(7),
                new Tile(8),
                new Tile()
        };
        mySolver.checkLine(line);
        assertEquals(line[8].getPossibleValues().size(), 1);
        assertEquals((long)line[8].getPossibleValues().iterator().next(), 9);
        line[8].checkTile();
        assertTrue("Now the tile should be finalized", line[8].isFinalized());

        Tile[] line2 = {
                new Tile(1),
                new Tile(2),
                new Tile(3),
                new Tile(4),
                new Tile(5),
                new Tile(6),
                new Tile(7),
                new Tile(),
                new Tile()
        };
        mySolver.checkLine(line2);
        assertEquals(line2[8].getPossibleValues().size(), 2);
        assertTrue("Should contain value 8", line2[8].getPossibleValues().contains(8));
        assertTrue("Should contain value 9", line2[8].getPossibleValues().contains(9));
        assertTrue("Should not contain value 7", !line2[8].getPossibleValues().contains(7));
    }

    @Test
    public void testCheckBoxes() throws Exception {

    }

    @Test
    public void testGetDeterminedValues() throws Exception {

    }

    @Test
    public void testGetDeterminedValues1() throws Exception {

    }

    @Test
    public void testDisplayBoard() throws Exception {

    }
}