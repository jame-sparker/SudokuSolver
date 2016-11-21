package Tokenizer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by james on 22/11/16.
 */
public class Lexer {
    public static List<AbstractToken> produceLexers(String inputString){
        List<AbstractToken> tokens = new ArrayList<>();
        for (char c : inputString.toCharArray()){
            if (c < '9' && c > '0') {
                AbstractToken t = new NumberToken(Character.digit(c, 10))
                tokens.add(t);
            } else if (c == '[' || c == ']' || c == ','){
                AbstractToken t = new SymbolToken(c);
                tokens.add(t);
            }
        }
        return tokens;
    }
}
