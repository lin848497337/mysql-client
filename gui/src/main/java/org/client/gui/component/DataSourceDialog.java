package org.client.gui.component;

import org.client.datasources.Host;
import org.client.datasources.MultiDataSourcesManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.sql.SQLException;

public class DataSourceDialog extends JDialog {

    JTextField nameField;
    JTextField hostField;
    JTextField portField;
    JTextField userField;
    JTextField databaseField;
    JTextField pwdField;
    JLabel tipsLabel;

    private boolean isOk = false;

    public DataSourceDialog() {
        int totalWidth = 600;
        setSize(totalWidth, 300);
        setModal(true);
        JPanel containerPanel = new JPanel();
        setLayout(new BorderLayout());
        containerPanel.setLayout(new BoxLayout(containerPanel, BoxLayout.Y_AXIS));
        add(containerPanel, BorderLayout.CENTER);

        int width = 100;
        int height = 50;

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        JLabel nameLabel = new JLabel("Name: ");
        nameLabel.setPreferredSize(new Dimension(width, height));
        nameField = new JTextField();
        panel.add(nameLabel);
        panel.add(nameField);
        containerPanel.add(panel);


        JPanel urlPanel = new JPanel();
        urlPanel.setLayout(new BoxLayout(urlPanel, BoxLayout.X_AXIS));
        JLabel hostLabel = new JLabel("Host: ");
        hostLabel.setPreferredSize(new Dimension(width, height));
        hostField = new JTextField();
        urlPanel.add(hostLabel);
        urlPanel.add(hostField);
        JLabel portLabel = new JLabel("Port: ");
        portLabel.setPreferredSize(new Dimension(width, height));
        portField = new JTextField();
        portField.setText("3306");
        urlPanel.add(portLabel);
        urlPanel.add(portField);
        containerPanel.add(urlPanel);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        JPanel databasePanel = new JPanel();
        databasePanel.setLayout(new BoxLayout(databasePanel, BoxLayout.X_AXIS));
        JLabel databaseLabel = new JLabel("Database: ");
        databaseLabel.setPreferredSize(new Dimension(width, height));
        databaseField = new JTextField();
        databasePanel.add(databaseLabel);
        databasePanel.add(databaseField);
        containerPanel.add(databasePanel);

        JPanel userPanel = new JPanel();
        userPanel.setLayout(new BoxLayout(userPanel, BoxLayout.X_AXIS));
        JLabel userLabel = new JLabel("User: ");
        userLabel.setPreferredSize(new Dimension(width, height));
        userField = new JTextField();
        userPanel.add(userLabel);
        userPanel.add(userField);
        containerPanel.add(userPanel);

        JPanel pwdPanel = new JPanel();
        pwdPanel.setLayout(new BoxLayout(pwdPanel, BoxLayout.X_AXIS));
        JLabel pwdLabel = new JLabel("Password: ");
        pwdLabel.setPreferredSize(new Dimension(width, height));
        pwdField = new JTextField();
        pwdPanel.add(pwdLabel);
        pwdPanel.add(pwdField);
        containerPanel.add(pwdPanel);

        JButton testConnBtn = new JButton("Test Connection");
        testConnBtn.addActionListener((e)->{
            try {
                Host host = buildHost();
                MultiDataSourcesManager.getInstance().test(host);
                tipsLabel.setText("connect successfull!");
            } catch (SQLException throwables) {
                tipsLabel.setText(throwables.getMessage());
                throwables.printStackTrace();
            }
        });
        containerPanel.add(testConnBtn);

        tipsLabel = new JLabel();
        containerPanel.add(tipsLabel);

        JPanel controllPanel = new JPanel();
        controllPanel.setLayout(new BoxLayout(controllPanel, BoxLayout.X_AXIS));
        JButton okBtn = new JButton("OK");

        okBtn.addActionListener((e -> {

            Host host = buildHost();
            String name = getNameField();
            try {
                MultiDataSourcesManager.getInstance().addConnection(name, host);
            } catch (Exception throwables) {
                tipsLabel.setText(throwables.getMessage());
            }

            isOk = true;
            setVisible(false);
        }));


        JButton cancelBtn = new JButton("CANCEL");

        cancelBtn.addActionListener((e -> {
            setVisible(false);
        }));

        controllPanel.add(okBtn);
        controllPanel.add(cancelBtn);
        containerPanel.add(controllPanel);

        containerPanel.setPreferredSize(new Dimension(totalWidth, 300));
    }

    public Host buildHost(){
        Host host = new Host();
        host.setIp(getHostField());
        host.setPort(getPortField());
        host.setDbName(getDatabaseField());
        host.setUser(getUserField());
        host.setPwd(getPwdField());
        host.setEncoding("utf-8");
        return host;
    }

    public boolean isOk() {
        return isOk;
    }

    public String getNameField() {
        return nameField.getText();
    }

    public String getDatabaseField() {
        return databaseField.getText();
    }

    public String getHostField() {
        return hostField.getText();
    }

    public Integer getPortField() {
        String port =  portField.getText();
        return Integer.parseInt(port);
    }

    public String getUserField() {
        return userField.getText();
    }

    public String getPwdField() {
        return pwdField.getText();
    }

    public static void main(String[] args) {
        DataSourceDialog dataSourceDialog = new DataSourceDialog();
        dataSourceDialog.setVisible(true);
    }
}
