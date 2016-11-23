package SudokuObjects;

import java.util.ArrayList;
import java.util.Set;
import java.util.TreeSet;

/**
 * Created by james on 22/11/16.
 */
public class Tile implements Comparable {
    private int value = -1;
    private boolean finalized = false;
    private Set<Integer> possibleValues;
    private static int counter = 0;
    private int xCord, yCord; //should be stored as coordinate object

    public Tile() {
        this(-1, false);
        for (int i = 1; i <= 9; i++) {
            possibleValues.add(i);
        }
    }

    public Tile(int v){
        this(v, true);
        possibleValues.add(v);
    }

    public Tile(int v, boolean b) {
        xCord = 1 + (counter % 9);
        yCord = 1 + (counter / 9);
        counter++;
        value = v;
        finalized = b;
        possibleValues = new TreeSet<>();
    }

    public int getX(){
        return xCord;
    }

    public int getY(){
        return yCord;
    }

    public boolean isFinalized() {
        return finalized;
    }

    public void setValue(int v ){
        value = v;
        finalized = true;
        possibleValues = new TreeSet<>();
        possibleValues.add(v);
    }

    public void checkTile() throws UnsolvablePuzzleException{
        if (!finalized && possibleValues.size() == 0){
            throw new UnsolvablePuzzleException();
        }
        if (possibleValues.size() == 1) {
            int firstValue = possibleValues.iterator().next();
            setValue (firstValue);
        }
    }

    public Set<Integer> getValue() {
        Set<Integer> valueSet = new TreeSet<Integer>();
        if(finalized) valueSet.add(value);
        return valueSet;
    }

    public Set<Integer> getPossibleValues(){
        return possibleValues;
    }

    public boolean isPossible(int x){
        return possibleValues.contains(x);
    }

    public void updatePossibleValues (Set<Integer> impossibleValues){
        possibleValues.removeAll(impossibleValues);
    }

    public void updatePossibleValues (int impossibleValue){
        possibleValues.remove(impossibleValue);
    }


    public String getDetail () {
        if (finalized) return "" + value;
        String s = "";
        for (int i : possibleValues){
            s += i;
        }
        return s;

    }
    @Override
    public String toString (){
        return finalized ? "" + value : " ";
    }

    @Override
    public int compareTo(Object o) {
        Tile t2 = (Tile)o;
        return (xCord == t2.xCord)? Integer.compare(yCord, t2.yCord) : Integer.compare(xCord, t2.xCord);
    }
}
