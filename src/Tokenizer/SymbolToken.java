package Tokenizer;

/**
 * Created by james on 22/11/16.
 */
public class SymbolToken extends AbstractToken {
    private char symbol;
    public SymbolToken(char c){
        symbol = c ;
        type = TokenType.Symbol;
    }
    @Override
    public Object getValue() {
        return symbol;
    }
}
