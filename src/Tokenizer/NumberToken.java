package Tokenizer;

/**
 * Created by james on 22/11/16.
 */
public class NumberToken extends AbstractToken {
    private int number;
    public NumberToken(int n) {
        number = n;
        type = TokenType.Number;
    }
    int value;

    @Override
    public Object getValue() {
        return number;
    }
}
