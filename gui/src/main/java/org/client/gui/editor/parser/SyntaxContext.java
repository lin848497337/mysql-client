package org.client.gui.editor.parser;

public class SyntaxContext {

    private static final int TYPE_STRING = 1;
    private static final int TYPE_BRACKET = 2;
    private static final int TYPE_NONE = 0;

    private int startPos;

    private int endPos;

    private int type;

    public TextNode scan(String token , int pos){
        return null;
    }

    public boolean isString(){
        return type == TYPE_STRING;
    }

    public boolean isQuote(String token){
        return "'".equals(token);
    }
}
