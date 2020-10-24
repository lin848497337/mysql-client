package org.client.gui.editor.parser;

import java.util.StringTokenizer;

public class SyntaxStringTokenizer extends StringTokenizer {

    private String text;
    private int offset;
    private int tokenLength;
    public SyntaxStringTokenizer(String str) {
        super(str, " ,;\n+-*/%=\'()[]", true);
        this.text = str;
    }


    @Override
    public String nextToken() {
        String nextToken = super.nextToken();
        offset = text.indexOf(nextToken, offset + tokenLength);
        tokenLength = nextToken.length();
        return nextToken;
    }

    public int getTokenPosition(){
        return offset;
    }
}
