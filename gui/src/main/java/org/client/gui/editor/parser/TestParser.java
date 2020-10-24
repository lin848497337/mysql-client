package org.client.gui.editor.parser;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class TestParser implements IParser{
    private int i=0;

    @Override
    public List<TextNode> parse(String text) {
        SyntaxStringTokenizer tokenizer = new SyntaxStringTokenizer(text);
        List<TextNode> textNodes = new ArrayList<>();
        while (tokenizer.hasMoreTokens()){
            String keyWorld = tokenizer.nextToken();
            Color color;
            if (i++%2==0){
                color = Color.RED;
            }else {
                color = Color.GREEN;
            }
            TextNode textNode = new TextNode(keyWorld, tokenizer.getTokenPosition(), color, true, true);
            textNodes.add(textNode);
        }


        return textNodes;
    }
}
