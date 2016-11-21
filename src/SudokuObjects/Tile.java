package SudokuObjects;

import java.util.ArrayList;
import java.util.Set;
import java.util.TreeSet;

/**
 * Created by james on 22/11/16.
 */
public class Tile {
    private int value = -1;
    private boolean finalized = false;
    private Set<Integer> possibleValues;

    public Tile() {
        this(-1);
        for (int i = 1; i <= 9; i++) {
            possibleValues.add(i);
        }
    }

    public Tile(int v){
        this(v, true);
        possibleValues.add(v);
    }

    public Tile(int v, boolean b) {
        value = v;
        finalized = b;
        possibleValues = new TreeSet<>();
    }


    public boolean isFinalized() {
        return finalized;
    }

    public void setValue(int v ){
        value = v;
        finalized = true;
    }

    public void removePossible(Set<Integer> removeredNumbers) {
        possibleValues.removeAll(removeredNumbers);
    }
}
