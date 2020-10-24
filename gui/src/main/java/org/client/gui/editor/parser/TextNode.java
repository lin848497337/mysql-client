package org.client.gui.editor.parser;

import java.awt.*;

public class TextNode {

    private String  text;
    private Color   color;
    private boolean bold;
    private boolean italic;
    private int start;
    private int length;

    public TextNode(String text, int start, Color color, boolean bold, boolean italic) {
        this.text = text;
        this.color = color;
        this.bold = bold;
        this.italic = italic;
        this.start = start;
        this.length = text.length();
    }

    public TextNode(int start, int len, Color color, boolean bold, boolean italic) {
        this.color = color;
        this.bold = bold;
        this.italic = italic;
        this.start = start;
        this.length = len;
    }


    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public boolean isBold() {
        return bold;
    }

    public void setBold(boolean bold) {
        this.bold = bold;
    }

    public boolean isItalic() {
        return italic;
    }

    public void setItalic(boolean italic) {
        this.italic = italic;
    }
}
