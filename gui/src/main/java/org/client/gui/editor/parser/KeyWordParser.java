package org.client.gui.editor.parser;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class KeyWordParser implements IParser{

    private List<KeySpecialStyleDef> defList;

    public KeyWordParser(List<KeySpecialStyleDef> defList) {
        this.defList = defList;
    }

    @Override
    public List<TextNode> parse(String text) {
        SyntaxStringTokenizer tokenizer = new SyntaxStringTokenizer(text);
        List<TextNode> textNodes = new ArrayList<>();
        SyntaxContext context = new SyntaxContext();
        while (tokenizer.hasMoreTokens()){
            String token = tokenizer.nextToken();

            TextNode textNode = context.scan(token, tokenizer.getTokenPosition());
            if (textNode != null){
                textNodes.add(textNode);
            }
            for (KeySpecialStyleDef def : defList){
                if (def.isMatch(token)){
                    textNode = new TextNode(token, tokenizer.getTokenPosition(), def.getColor(), def.isBold(), def.isItalic());
                    textNodes.add(textNode);
                }
            }
        }
        return textNodes;
    }
}
