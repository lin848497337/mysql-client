package org.client.gui.component;

import org.client.common.StringUtils;
import org.client.gui.editor.TextContext;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.Caret;
import javax.swing.text.SimpleAttributeSet;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Collection;
import java.util.List;

public class AutoTipsContextDialog extends JDialog {

    private DefaultListModel<String> listModel = new DefaultListModel<>();
    private JList<String> listView = new JList<>(listModel);

    private JTextPane textPane;

    private String selectValue;

    private TextContext context = new TextContext();

    private int start;


    public AutoTipsContextDialog(JTextPane textPane){
        this.textPane = textPane;
        setUndecorated(true);
        setSize(new Dimension(300, 200));
        setLayout(new BorderLayout());
        setAutoRequestFocus(false);
        add(new JScrollPane(listView), BorderLayout.CENTER);
        listView.setFont(textPane.getFont());
        listView.addFocusListener(new FocusAdapter() {

            @Override
            public void focusLost(FocusEvent e) {
                AutoTipsContextDialog.this.setVisible(false);
            }
        });
        listView.addKeyListener(new KeyAdapter() {

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER){
                    triggerSelect();
                }
            }
        });
    }

    public void selectFirst(){
        listView.requestFocus();
        listView.setSelectedIndex(0);
    }

    public void triggerSelect(){
        selectValue = listView.getSelectedValue();
        try {
            textPane.getDocument().remove(start, textPane.getCaretPosition() - start);
            textPane.getDocument().insertString(start, selectValue, SimpleAttributeSet.EMPTY);
        } catch (BadLocationException badLocationException) {
            badLocationException.printStackTrace();
        }
        AutoTipsContextDialog.this.setVisible(false);
    }


    public void setTipsList(Collection<String> list){
        listModel.clear();
        list.forEach(listModel::addElement);
        listView.setSelectedIndex(0);
    }

    private int scanBackspace() throws BadLocationException {
        int pos = textPane.getCaretPosition();
        while (true){
            int curPos = Math.max(pos - 200, 0);
            String text = textPane.getText(curPos, pos);
            for (int i=text.length() - 1 ; i>=0 ; i--){
                char v = text.charAt(i);
                if (!Character.isLetter(v) && !Character.isDigit(v)){
                    return i + 1;
                }
            }
            if (curPos == 0){
                return curPos;
            }
            pos = curPos;
        }
    }

    public void input() throws BadLocationException {
        start = scanBackspace();
        int end = textPane.getCaretPosition();

        String value = textPane.getText(start, end - start);
        if (StringUtils.isBlank(value)){
            setVisible(false);
            return;
        }

        List<String> wordTpsList = context.input(value);
        if (wordTpsList != null && !wordTpsList.isEmpty()){
            try {
                locationByCaret(value, start);
            } catch (BadLocationException e) {
                e.printStackTrace();
            }
            setTipsList(wordTpsList);
            setVisible(true);
        }else {
            setVisible(false);
        }
    }

    private void locationByCaret(String value, int curPos) throws BadLocationException {
        if (isVisible()){
            return;
        }
        Caret caret = textPane.getCaret();
        Point pt = caret.getMagicCaretPosition();
        Point tpt = textPane.getLocationOnScreen();
        FontMetrics metrics = textPane.getFontMetrics(textPane.getFont());
        int width = metrics.stringWidth(value);
        setLocation(tpt.x + pt.x - width - 2, tpt.y +  pt.y + metrics.getHeight());
    }

}
