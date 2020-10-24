package org.client.gui.editor.parser;

import java.awt.*;

public class NumberKeyStyleDef implements KeySpecialStyleDef{

    private Color color;
    private boolean bold;
    private boolean italic;

    public NumberKeyStyleDef(Color color, boolean bold, boolean italic) {
        this.color = color;
        this.bold = bold;
        this.italic = italic;
    }

    @Override
    public boolean isMatch(String word) {
        return word.matches("\\d+(\\.\\d+)?");
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
