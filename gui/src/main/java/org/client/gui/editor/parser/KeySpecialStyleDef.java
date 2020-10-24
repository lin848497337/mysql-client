package org.client.gui.editor.parser;

import java.awt.*;

public interface KeySpecialStyleDef {

    boolean isMatch(String word);

    Color getColor();

    boolean isBold();

    boolean isItalic();
}
