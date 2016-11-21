package SudokuObjects;

/**
 * Created by james on 22/11/16.
 */
public class Board {
    public static final int SIZE = 9;
    private Tile[][] grid = new Tile[9][9];

    public Board(Tile[][] tiles){
        grid = tiles;
    }

    @Override
    public String toString(){
        String s = "";
        String diviser = "";


        for (int i = 0; i < Board.SIZE; i++){
            diviser = diviser + "+-";
        }
        diviser = diviser + "+\n";

        for (int i = 0; i < Board.SIZE; i++) {
            String container = "";
            for (int j = 0; j < Board.SIZE; j++){
                container = container + "|" + grid[i][j];
            }
            container = container + "|\n";
            s = s + diviser;
            s = s + container;
        }
        s = s + diviser;
        return s;
    }
}
