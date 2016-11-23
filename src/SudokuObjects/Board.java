package SudokuObjects;

import sun.reflect.generics.tree.Tree;

import java.util.Set;
import java.util.TreeSet;

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
        Tile[][] block = new Tile[3][3];

        for(int i = 0; i < 3; i++){
            for(int j = 0; j < 3; j++){
                block[i][j] = grid[3 * (x-1) + i][3 * (y-1) + j];
            }
        }
        return block;
    }

    public Tile[] getFlattenedBlock(int x, int y){
        Tile[][] block = getBlock(x, y);
        Tile[] flattened = new Tile[SIZE];
        for (int i = 0; i < SIZE; i++){
            flattened[i] = block[i/3][i%3];
        }
        return flattened;
    }

    public static Tile[] getUndeterminedTiles(Tile[] tiles){
        int count = 0;
        for(int i = 0; i < tiles.length; i++){
            if (!tiles[i].isFinalized()) count++;
        }
        Tile[] undeterminedTiles = new Tile[count];
        int counter = 0;
        for(int i = 0; counter < count; i++){
            if (!tiles[i].isFinalized()) {
                undeterminedTiles[counter] = tiles[i];
                counter++;
            }
        }
        return undeterminedTiles;
    }

    /**
     * retrieves the desired tile. The indexes needs to be between 1-9.
     * @param x x index
     * @param y y index
     * @return Tile at location (x,y)
     */
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
            column[i] = grid[i][y - 1];
        }
        return column;
    }

    public void updateAllTiles() throws UnsolvablePuzzleException{
        /*Simple check*/
        for(int i = 0; i < 9; i++){
            for(int j = 0; j < 9; j++) {
                grid[i][j].checkTile();
            }
        }

        updateLines();
        updateBlocks();
    }

    public void updateBlocks(){
        for(int i = 1; i <= 3; i++) {
            for (int j = 1; j <= 3; j++) {
                updateByBlock(i, j);
            }
        }
    }
    public void updateByBlock(int i, int j){

        Tile[][] block = getBlock(i, j);
        Set<Integer> determined = getDeterminedValues(block);
        /*if the values do not exist in the set only once then */

        for (int x = 1; x <= 9; x++) {
            if (!determined.contains(x)) {
                Set<Tile> tiles = new TreeSet<>();
                for (int a = 0; a < block.length; a++) {
                    for (int b = 0; b < block[a].length; b++) {
                        Tile t = block[a][b];
                        if (!t.isFinalized() && t.isPossible(x)) tiles.add(t);
                    }
                }
                if (tiles.size() == 1) {
                    tiles.iterator().next().setValue(x);
                }
            }
        }
    }

    public void updateLines(){
        for (int i = 1; i <= 9; i++){
            Tile[] row = getRow(i);
            updateByLine(row);
            Tile[] column = getColumn(i);
            updateByLine(column);
        }
    }

    public void updateByLine(Tile[] line){
        Set<Integer> determined = getDeterminedValues(line);
        for (int x = 1; x <= SIZE; x++){
            if (!determined.contains(x)) {
                Set<Tile> tiles = new TreeSet<>();
                for (int i = 0; i < line.length; i++){
                    Tile t = line[i];
                    if (!t.isFinalized() && t.isPossible(x)) tiles.add(t);
                }
                if (tiles.size() == 1){
                    tiles.iterator().next().setValue(x);
                }
            }
        }
    }

    public Tile[] getRowSubset(int y, int removedBlockNum){
        Tile[] row = getRow(y);
        return removeBlock(row, removedBlockNum);
    }

    public Tile[] getColumnSubset(int x, int removedBlockNum){
        Tile[] column = getColumn(x);
        return removeBlock(column, removedBlockNum);
    }

    private Tile[] removeBlock(Tile[] line, int removedBlock){
        if (removedBlock > 3 || removedBlock < 1) {
            throw new IndexOutOfBoundsException("Got request for remove block: " + removedBlock);
        }
        Tile[] newLine = new Tile[6];
        int i = 0, j = 0;
        while (i < line.length){
            if ((i / 3) == (removedBlock - 1)){
                i++;
            } else {
                newLine[j++] = line[i++];
            }
        }
        return newLine;
    }

    /**
     *
     * @param x block x index
     * @param y block y index
     * @param rowIndex line index to remove (can take values between 1-3 or 1-9)
     * @return
     */
    public Tile[] getBlockSubsetWithoutRow(int x, int y, int rowIndex){
        if (rowIndex > 9 || rowIndex < 1) {
            throw new IndexOutOfBoundsException("Got request for line index : " + rowIndex);
        }
        Tile[][] block = getBlock(y, x);
        Tile[] subBlock = new Tile[6];
        int adjustedRowPos = (rowIndex - 1) % 3; /*returns value between 1-3*/

        int counter = 0;
        for (int i = 0; i < 3; i++){
            if (adjustedRowPos == i) continue; /*skip one line*/
            for (int j = 0; j < 3; j++){
                subBlock[counter++] = block[i][j];
            }
        }
        return subBlock;
    }

    /**
     *
     * @param x block x index
     * @param y block y index
     * @param columnIndex line index to remove (can take values between 1-3 or 1-9)
     * @return
     */
    public Tile[] getBlockSubsetWithoutColumn(int y, int x, int columnIndex){
        if (columnIndex > 9 || columnIndex < 1) {
            throw new IndexOutOfBoundsException("Got request for line index : " + columnIndex);
        }
        Tile[][] block = getBlock(x, y);
        Tile[] subBlock = new Tile[6];
        int adjustedColumnPos = (columnIndex - 1) % 3; /*returns value between 1-3*/

        int counter = 0;
        for (int i = 0; i < 3; i++){
            for (int j = 0; j < 3; j++){
                if (adjustedColumnPos == j) continue; /*skip one line*/
                subBlock[counter++] = block[i][j];
            }
        }
        return subBlock;
    }

    public boolean isCompleted(){
        for(int i = 0; i < 9; i++){
            for(int j = 0; j < 9; j++){
                if (!grid[i][j].isFinalized()) {
                    return false;
                }
            }
        }
        return true;
    }

    public static Set<Integer> getDeterminedValues (Tile[] tiles ){
        Set<Integer> determined = new TreeSet<>();
        for (Tile t : tiles){
            determined.addAll(t.getValue());
        }
        return determined;
    }

    public static Set<Integer> getDeterminedValues (Tile[][] grid ){
        Set<Integer> determined = new TreeSet<>();
        for (Tile[] tiles : grid){
            determined.addAll(getDeterminedValues(tiles));
        }
        return determined;
    }

    public String detailedRepresentation(){
        String s = "";
        String diviser = "";

        for (int i = 0; i < SIZE; i++){
            diviser = diviser + "+---------";
        }
        diviser = diviser + "+\n";

        for (int i = 0; i < SIZE; i++) {
            String container = "";
            for (int j = 0; j < SIZE; j++){
                container = container + (j%3 == 0? "‖" : "|") + String.format("%9s", grid[i][j].getDetail());
            }
            container = container + "‖\n";
            s = s + (i % 3 == 0 ? diviser.replaceAll("-", "=") : diviser);
            s = s + container;
        }

        s = s + diviser.replaceAll("-", "=");
        return s;
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
                container = container + (j%3 == 0? "‖" : "|") + grid[i][j];
            }
            container = container + "‖\n";
            s = s + (i % 3 == 0 ? diviser.replaceAll("-", "=") : diviser);
            s = s + container;
        }

        s = s + diviser.replaceAll("-", "=");
        return s;
    }

}
