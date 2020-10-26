package org.client.gui.component;

import javax.swing.*;
import javax.swing.border.AbstractBorder;
import java.awt.*;

public class LineNumberBorder extends AbstractBorder {

    /*
     * Insets 对象是容器边界的表示形式。 它指定容器必须在其各个边缘留出的空间。
     * 此方法在实例化时自动调用
     * 此方法关系到边框是否占用组件的空间
     */
    @Override
    public Insets getBorderInsets(Component c) {
        return getBorderInsets(c, new Insets(0, 0, 0, 0));
    }

    @Override
    public Insets getBorderInsets(Component c, Insets insets) {
        if (c instanceof JTextPane) {
            //这里设置行号左边边距
            JTextPane textPane = (JTextPane) c;
            int maxLen = lineNumberWidth(textPane);
            insets.left = maxLen;
        }

        return insets;

    }

    @Override
    public boolean isBorderOpaque() {
        return false;
    }

    // 边框的绘制方法
    // 此方法必须实现
    @Override
    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        // 获得当前剪贴区域的边界矩形。
        JTextPane textPane = (JTextPane) c;
        int maxLen = lineNumberWidth(textPane);
        java.awt.Rectangle clip = g.getClipBounds();
        FontMetrics fm = g.getFontMetrics();
        int fontHeight = fm.getHeight();

        // starting location at the "top" of the page...
        // y is the starting baseline for the font...
        int ybaseline = y + fm.getAscent();

        // now determine if it is the "top" of the page...or somewhere
        // else
        int startingLineNumber = (clip.y / fontHeight) + 1;

        if (startingLineNumber != 1) {
            ybaseline = y + startingLineNumber * fontHeight - (fontHeight - fm.getAscent());
        }

        int yend = ybaseline + height;
        if (yend > (y + height)) {
            yend = y + height;
        }

        FontMetrics fontMetrics = textPane.getFontMetrics(textPane.getFont());

        g.setColor(Color.blue);
        // 绘制行号
        while (ybaseline < yend) {
            String label = padLabel(startingLineNumber, 0, true);
            int curLen = fontMetrics.stringWidth(label);
            g.drawString(label, maxLen - curLen, ybaseline);
            ybaseline += fontHeight;
            startingLineNumber++;
        }
    }

    // 寻找适合的数字宽度
    private int lineNumberWidth(JTextPane textPane) {
        int lineCount = textPane.getDocument().getDefaultRootElement().getElementCount();
        return textPane.getFontMetrics(textPane.getFont()).stringWidth(lineCount + " ");
    }

    private String padLabel(int lineNumber, int length, boolean addSpace) {
        StringBuffer buffer = new StringBuffer();
        buffer.append(lineNumber);
        for (int count = (length - buffer.length()); count > 0; count--) {
            buffer.insert(0, ' ');
        }
        if (addSpace) {
            buffer.append(' ');
        }
        return buffer.toString();
    }
}
