package org.client.gui.layout;

import org.client.datasources.ExecuteParam;
import org.client.datasources.serial.SqlLog;
import org.client.gui.component.CloseTabIcon;
import org.client.gui.editor.TextEditor;
import org.client.gui.listener.UpdateListener;

import javax.swing.*;
import javax.swing.event.MouseInputAdapter;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class EditorContainer extends JPanel {

    private JTabbedPane tabbedPane = new JTabbedPane();

    private Map<String, TextEditor> textEditorMap = new HashMap<>();

    private UpdateListener updateListener;

    public EditorContainer() {
        tabbedPane.setTabPlacement(JTabbedPane.TOP);
        tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
        setLayout(new BorderLayout());
        add(tabbedPane, BorderLayout.CENTER);
    }

    public void addUpdateListener(UpdateListener listener){
        this.updateListener = listener;
    }

    public TextEditor addEditor(String title){
        if (textEditorMap.containsKey(title)){
            int index = tabbedPane.indexOfComponent(textEditorMap.get(title));
            tabbedPane.setSelectedIndex(index);
            return null;
        }else{
            TextEditor textEditor = new TextEditor(title);
            textEditor.addUpdateListener(updateListener);
            tabbedPane.addTab(title,new CloseTabIcon(null),  textEditor);
            tabbedPane.addMouseListener(new MouseInputAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    int tabNumber = tabbedPane.getUI().tabForCoordinate(tabbedPane, e.getX(), e.getY());
                    if (tabNumber < 0) {
                        return;
                    }
                    Rectangle rect = ((CloseTabIcon) tabbedPane.getIconAt(tabNumber)).getBounds();
                    if (rect.contains(e.getX(), e.getY())) {
                        // the tab is being closed
                        tabbedPane.removeTabAt(tabNumber);
                        textEditorMap.remove(title);
                    }
                }
            });
            textEditorMap.put(title, textEditor);
            return textEditor;
        }

    }

    public ExecuteParam getCurrentEditorSql(){
        TextEditor textEditor = (TextEditor) tabbedPane.getSelectedComponent();
        if (textEditor == null){
            return null;
        }
        ExecuteParam param = new ExecuteParam();
        param.setInstance(textEditor.getTitle());
        param.setSql(textEditor.getSelectSql());
        return param;
    }

    public void reloadData(java.util.List<SqlLog> logList){
        for (SqlLog sqlLog : logList){
            TextEditor editor = addEditor(sqlLog.getTitle());
            editor.setText(sqlLog.getLogs());
        }
    }

    public java.util.List<SqlLog> getAllLogs(){
        java.util.List<SqlLog> logList = new ArrayList<>();
        for (int i=0 ; i<tabbedPane.getTabCount() ; i++){
            String name = tabbedPane.getTitleAt(i);
            String sqlList = ((TextEditor)tabbedPane.getComponentAt(i)).getText();
            SqlLog log = new SqlLog();
            log.setTitle(name);
            log.setLogs(sqlList);
            logList.add(log);
        }
        return logList;
    }
}
