package org.client.gui.editor.parser;

import java.awt.*;
import java.util.Set;

public class DefaultKeySpecialStyleDef implements KeySpecialStyleDef{

    private Set<String> keyWorldSet;
    private Color color;
    private boolean bold;
    private boolean italic;

    public DefaultKeySpecialStyleDef(Set<String> keyWorldSet, Color color, boolean bold, boolean italic) {
        this.keyWorldSet = keyWorldSet;
        this.color = color;
        this.bold = bold;
        this.italic = italic;
    }

    @Override
    public boolean isMatch(String word){
        return keyWorldSet.contains(word.toUpperCase());
    }

    @Override
    public Color getColor() {
        return color;
    }

    @Override
    public boolean isBold() {
        return bold;
    }

    @Override
    public boolean isItalic() {
        return italic;
    }
}
