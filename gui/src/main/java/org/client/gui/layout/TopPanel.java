package org.client.gui.layout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

public class TopPanel extends JPanel {


    private Map<CMD, ActionListener> listenerMap = new HashMap<>();

    public static enum CMD{
        FILE_NEW,
        FILE_OPEN,
        FILE_OPEN_RECENT,
        FILE_SAVE_AS,
        FILE_SAVE,
        FILE_DATASOURCE,


        TOOL_EXECUTE_SQL
    }


    private void initFileMenu(JMenuBar jMenuBar){
        JMenu jMenu = new JMenu("File");

        JMenuItem newMenuItem = new JMenuItem("New");
        newMenuItem.addActionListener(e->{
            onFire(CMD.FILE_NEW, e);
        });
        jMenu.add(newMenuItem);

        JMenuItem openMenuItem = new JMenuItem("Open");
        openMenuItem.addActionListener(e->{
            onFire(CMD.FILE_OPEN, e);
        });
        jMenu.add(openMenuItem);

        JMenuItem openRecentMenuItem = new JMenuItem("Open Recent");
        openRecentMenuItem.addActionListener(e->{
            onFire(CMD.FILE_OPEN_RECENT, e);
        });
        jMenu.add(openRecentMenuItem);

        JMenuItem saveAsMenuItem = new JMenuItem("Save As...");
        saveAsMenuItem.addActionListener(e->{
            onFire(CMD.FILE_SAVE_AS, e);
        });
        jMenu.add(saveAsMenuItem);

        JMenuItem saveMenuItem = new JMenuItem("Save");
        saveMenuItem.addActionListener((e)->{
            onFire(CMD.FILE_SAVE, e);
        });
        jMenu.add(saveMenuItem);

        JMenuItem dataSourceMenuItem = new JMenuItem("DataSources...");
        dataSourceMenuItem.addActionListener(e->{
            onFire(CMD.FILE_DATASOURCE, e);
        });
        jMenu.add(dataSourceMenuItem);


        jMenuBar.add(jMenu);
    }

    private void onFire(CMD cmd, ActionEvent event){
        ActionListener listener = listenerMap.get(cmd);
        if (listener != null){
            listener.actionPerformed(event);
        }
    }

    public void addActionListener(CMD cmd, ActionListener listener){
        if(listenerMap.put(cmd, listener)!=null){
            throw new RuntimeException("duplicate cmd listener "+cmd);
        }
    }

    private void initEditMenu(JMenuBar jMenuBar){
        JMenu jMenu = new JMenu("Edit");
        jMenuBar.add(jMenu);
    }

    private void initViewMenu(JMenuBar jMenuBar){
        JMenu jMenu = new JMenu("View");
        jMenuBar.add(jMenu);
    }

    private void initNavigateMenu(JMenuBar jMenuBar){
        JMenu jMenu = new JMenu(" Navigate");
        jMenuBar.add(jMenu);
    }

    private void initCodeMenu(JMenuBar jMenuBar){
        JMenu jMenu = new JMenu(" Code");
        jMenuBar.add(jMenu);
    }

    private void initRefactorMenu(JMenuBar jMenuBar){
        JMenu jMenu = new JMenu(" Refactor");
        jMenuBar.add(jMenu);
    }

    private void initToolsMenu(JMenuBar jMenuBar){
        JMenu jMenu = new JMenu(" Tools");
        jMenuBar.add(jMenu);
    }

    private void initWindowMenu(JMenuBar jMenuBar){
        JMenu jMenu = new JMenu(" Window");
        jMenuBar.add(jMenu);
    }

    private void initHelpMenu(JMenuBar jMenuBar){
        JMenu jMenu = new JMenu(" Help");
        jMenuBar.add(jMenu);
    }

    public TopPanel() {
        JMenuBar jMenuBar = new JMenuBar();
        initFileMenu(jMenuBar);
        initEditMenu(jMenuBar);
        initViewMenu(jMenuBar);
        initNavigateMenu(jMenuBar);
        initCodeMenu(jMenuBar);
        initRefactorMenu(jMenuBar);
        initToolsMenu(jMenuBar);
        initWindowMenu(jMenuBar);
        initHelpMenu(jMenuBar);

        JToolBar toolBar = new JToolBar();
        toolBar.setFloatable(false);
        JButton executeBtn = new JButton("执行");
        executeBtn.addActionListener((e -> {
            onFire(CMD.TOOL_EXECUTE_SQL, e);
        }));
        toolBar.add(executeBtn);
        setLayout(new BorderLayout());
        add(jMenuBar, BorderLayout.NORTH);
        add(toolBar, BorderLayout.SOUTH);
    }
}
