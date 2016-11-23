package Tokenizer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by james on 22/11/16.
 */
public class Lexer {
    public static Tokens produceLexers(String inputString){
        Tokens tokens = new Tokens();
        for (char c : inputString.toCharArray()){
            if (c <= '9' && c >= '0') {
                AbstractToken t = new NumberToken(Character.digit(c, 10));
                tokens.insert(t);
            } else if (c == '[' || c == ']' || c == ','){
                AbstractToken t = new SymbolToken(c);
                tokens.insert(t);
            }
        }
        return tokens;
    }
}
