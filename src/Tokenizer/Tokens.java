package Tokenizer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by james on 22/11/16.
 */
public class Tokens {
    private List<AbstractToken> tokens = new ArrayList<>();
    private int index = 0;

    public Tokens(){}

    public void insert(AbstractToken t) {

    };
    public boolean hasNext() {
        return index == tokens.size();
    }
}
