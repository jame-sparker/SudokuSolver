package Tokenizer;

/**
 * Created by james on 22/11/16.
 */
public abstract class AbstractToken {
    public TokenType type;
    public TokenType getTokenType() {
        return type;
    }
    public abstract Object getValue();
}
