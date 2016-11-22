package SudokuObjects;

/**
 * Created by james on 22/11/16.
 * TODO: Returned arrays are not protected. i.e. returns references which is not safe
 */
public class Board {
    public static final int SIZE = 9;
    private Tile[][] grid = new Tile[SIZE][SIZE];

    public Board(Tile[][] tiles){
        grid = tiles;
    }

    /**get 3x3 grid
    * Allow x,y values between 1-3*/
    public Tile[][] getBlock(int x, int y) {
        if (x > 3 || x < 1 || y > 3 || y < 1) {
            throw new IndexOutOfBoundsException("Got request for block x: " + x + " y: " + y);
        }
        Tile block[][] = new Tile[3][3];

        for(int i = 0; i < 3; i++){
            for(int j = 0; j < 3; j++){
                block[i][j] = grid[3 * (x-1) + i][3 * (y-1) + j];
            }
        }
        return block;
    }

    public Tile getTile(int x, int y){
        if (x > 9 || x < 1 || y > 9 || y < 1) {
            throw new IndexOutOfBoundsException("Got request for tile x: " + x + " y: " + y);
        }
        return grid[x - 1][y - 1];
    }

    public Tile[] getRow(int x){
        if (x > 9 || x < 1) {
            throw new IndexOutOfBoundsException("Got request for row x: " + x );
        }
        return grid[x - 1];
    }

    public Tile[] getColumn(int y){
        if (y > 9 || y < 1) {
            throw new IndexOutOfBoundsException("Got request for column y: " + y );
        }
        Tile[] column = new Tile[SIZE];

        for (int i = 0; i < SIZE; i++){
            column[i] = grid[i][y];
        }
        return column;
    }

    @Override
    public String toString(){
        String s = "";
        String diviser = "";

        for (int i = 0; i < SIZE; i++){
            diviser = diviser + "+-";
        }
        diviser = diviser + "+\n";

        for (int i = 0; i < SIZE; i++) {
            String container = "";
            for (int j = 0; j < SIZE; j++){
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
