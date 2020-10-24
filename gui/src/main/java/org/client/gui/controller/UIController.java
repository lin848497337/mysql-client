package org.client.gui.controller;

import org.client.common.StringUtils;
import org.client.datasources.ExceptionResult;
import org.client.datasources.ExecuteParam;
import org.client.datasources.ExecuteRsult;
import org.client.datasources.MultiDataSourcesManager;
import org.client.datasources.serial.Instance;
import org.client.datasources.serial.SqlLog;
import org.client.datasources.serial.UserData;
import org.client.gui.config.ConfigManager;
import org.client.gui.config.DataManager;
import org.client.gui.config.UserPreference;
import org.client.gui.layout.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UIController {


    private JSplitPane hSplitPane;
    private JSplitPane vSplitPane;

    private TopPanel topPanel;

    private ResultPanel resultPanel;

    private EditorContainer editorContainer;
    private DataSourcePanel dataSourcePanel;

    private MainFrame mainFrame;

    private DataManager dataManager = new DataManager();

    private ConfigManager configManager = new ConfigManager();

    private boolean isDirty = false;


    public UIController() {

    }


    public void loadConfig() throws IOException {
        configManager.loadConfig();
    }

    public void initUI(){
        mainFrame = new MainFrame();
        mainFrame.setTitle("mysql-client");
        UserPreference userPreference = configManager.getUserPreference();
        if (userPreference != null){
            mainFrame.setLocation(userPreference.getX(), userPreference.getY());
            mainFrame.setSize(userPreference.getWidth(), userPreference.getHeight());
        }else {
            mainFrame.setSize(400 , 600);
        }
        editorContainer = new EditorContainer();
        editorContainer.addUpdateListener(()->{
            update(true);
        });
        resultPanel = new ResultPanel();
        dataSourcePanel = new DataSourcePanel();
        dataSourcePanel.addUpdateListener(()->{
            update(true);
        });
        initDataSourcePanel();
        hSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, dataSourcePanel, editorContainer);
        hSplitPane.setOneTouchExpandable(true);
        vSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, hSplitPane, resultPanel);
        vSplitPane.setOneTouchExpandable(true);
        topPanel = new TopPanel();
        initTopMenu();
        mainFrame.add(topPanel, BorderLayout.NORTH);
        mainFrame.add(vSplitPane, BorderLayout.CENTER);
        mainFrame.setVisible(true);

        mainFrame.addComponentListener(new ComponentAdapter() {

            @Override
            public void componentResized(ComponentEvent e) {
                super.componentResized(e);
                rebuildUserPreference();
            }

            @Override
            public void componentMoved(ComponentEvent e) {
                super.componentMoved(e);
                rebuildUserPreference();
            }
        });

        hSplitPane.addPropertyChangeListener(e->{
            rebuildUserPreference();
        });

        vSplitPane.addPropertyChangeListener(e->{
            rebuildUserPreference();
        });
    }

    private void update(boolean dirty){
        String title = mainFrame.getTitle();
        if (dirty == isDirty){
            return;
        }
        if (dirty){
            mainFrame.setTitle(title+"-*");
        }else{
            if (title.endsWith("-*")){
                mainFrame.setTitle(title.substring(0, title.length() - 2));
            }
        }
        isDirty = dirty;
    }

    private void rebuildUserPreference(){
        UserPreference userPreference = new UserPreference();
        userPreference.setX(mainFrame.getX());
        userPreference.setY(mainFrame.getY());
        userPreference.setWidth((int) mainFrame.getSize().getWidth());
        userPreference.setHeight((int) mainFrame.getSize().getHeight());
        userPreference.setHonSplitLoc(hSplitPane.getDividerLocation());
        userPreference.setVertSplitLoc(vSplitPane.getDividerLocation());
        configManager.setUserPreference(userPreference);
    }


    public void afterInit(){
        UserPreference userPreference = configManager.getUserPreference();
        if (userPreference != null){
            hSplitPane.setDividerLocation(userPreference.getHonSplitLoc());
            vSplitPane.setDividerLocation(userPreference.getVertSplitLoc());
        }else{
            hSplitPane.setDividerLocation(0.4);
            vSplitPane.setDividerLocation(0.7);
        }

        loadData();
    }

    private void initDataSourcePanel(){
        dataSourcePanel.setNewEditorListener(title->{
            editorContainer.addEditor(title);
        });
    }

    private void initTopMenu(){
        topPanel.addActionListener(TopPanel.CMD.FILE_SAVE, e->{
            save();
        });
        topPanel.addActionListener(TopPanel.CMD.TOOL_EXECUTE_SQL, e->{
            ExecuteParam param = editorContainer.getCurrentEditorSql();
            if (param == null){
                return;
            }
            ExecuteRsult executeRsult = executeCmd(param.getInstance(), param.getSql());
            if (executeRsult != null){
                resultPanel.showResult(executeRsult);
            }
        });
    }

    private ExecuteRsult _innerExecute(String title, String sql){
        if (StringUtils.isBlank(sql)){
            return null;
        }
        try {
            return MultiDataSourcesManager.getInstance().executeSql(title, sql);
        } catch (SQLException throwables) {
            return new ExceptionResult(throwables);
        }
    }

    public ExecuteRsult executeCmd(String instanceName, String sql){
        ExecuteRsult executeRsult = _innerExecute(instanceName, sql);
        if (executeRsult == null){
            return null;
        }
        return executeRsult;
    }



    public void loadData(){
        UserData userData = dataManager.loadData();
        if (userData == null){
            return;
        }
        MultiDataSourcesManager.getInstance().init(userData.getInstanceList());
        dataSourcePanel.reload(userData.getInstanceList());
        editorContainer.reloadData(userData.getLogList());
    }

    public void save(){
        List<Instance> instanceList = MultiDataSourcesManager.getInstance().getData();
        List<SqlLog> logList = editorContainer.getAllLogs();
        UserData userData = new UserData();
        userData.setInstanceList(instanceList);
        userData.setLogList(logList);
        try{
            dataManager.saveData(userData);
            update(false);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
