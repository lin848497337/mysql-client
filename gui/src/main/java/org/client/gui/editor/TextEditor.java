package org.client.gui.editor;

import org.client.common.StringUtils;
import org.client.gui.component.LineNumberBorder;
import org.client.gui.editor.parser.IParser;
import org.client.gui.editor.parser.SqlParser;
import org.client.gui.editor.parser.TextNode;
import org.client.gui.listener.UpdateListener;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.List;

public class TextEditor extends JPanel{

    private JTextPane textPane;

    private String title;

    private IParser parser = new SqlParser();

    private UpdateListener updateListener;

    public TextEditor(String title){
        setName(title);
        this.title = title;
        setLayout(new BorderLayout(0,0));
        textPane = new JTextPane();
        textPane.setDocument(new DefaultStyledDocument());
        add(new JScrollPane(textPane), BorderLayout.CENTER);
        textPane.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                if (e.isActionKey()){
                    return;
                }
                try {
                    syntaxParse();
                    if (updateListener != null){
                        updateListener.onUpdate();
                    }
                } catch (BadLocationException badLocationException) {
                    badLocationException.printStackTrace();
                }

            }
        });
        setSize(200,300);
        textPane.setBorder(new LineNumberBorder());
    }

    public void addUpdateListener(UpdateListener listener){
        this.updateListener = listener;
    }

    private void syntaxParse() throws BadLocationException {
        DefaultStyledDocument doc = (DefaultStyledDocument) textPane.getDocument();
        String text = doc.getText(0, doc.getLength());
        if(parser != null){
            List<TextNode> keyNodeList = parser.parse(text);
            doc.setCharacterAttributes(0, doc.getLength(), SimpleAttributeSet.EMPTY, true );
            keyNodeList.forEach(n->{
                MutableAttributeSet attributeSet = new SimpleAttributeSet();
                StyleConstants.setForeground(attributeSet, n.getColor());
                StyleConstants.setBold(attributeSet, n.isBold());
                StyleConstants.setItalic(attributeSet, n.isItalic());
                doc.setCharacterAttributes(n.getStart(), n.getLength(), attributeSet, false);
            });
        }
    }

    public String getSelectSql(){
        String selectText = textPane.getSelectedText();
        if (!StringUtils.isBlank(selectText)){
            return selectText;
        }
        return textPane.getText();
    }


    public String getText(){
        return textPane.getText();
    }

    public void setText(String text){
        textPane.setText(text);
        try {
            syntaxParse();
        } catch (BadLocationException e) {

        }
    }

    public String getTitle() {
        return title;
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("main");
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.setSize(200, 100);
        frame.add(new TextEditor("main"), BorderLayout.CENTER);
        frame.setVisible(true);
    }
}
