package org.client.gui.layout;

import org.client.datasources.ExceptionResult;
import org.client.datasources.ExecuteRsult;
import org.client.datasources.ResultSetResult;
import org.client.datasources.UpdateCountResult;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ResultPanel extends JPanel {

    private DefaultTableModel defaultTableModel;
    private JTable jTable;
    private JTabbedPane tabbedPane;
    private JPanel resultTablePanel;
    private JPanel resultConsolePanel;

    public ResultPanel() {

        setPreferredSize(new Dimension(getWidth(), 200));
        resultTablePanel = new JPanel();
        resultConsolePanel = new JPanel();
        resultTablePanel.setLayout(new BorderLayout());
        resultConsolePanel .setLayout(new BoxLayout(resultConsolePanel, BoxLayout.Y_AXIS));

        setLayout(new BorderLayout());
        tabbedPane = new JTabbedPane(JTabbedPane.TOP, JTabbedPane.SCROLL_TAB_LAYOUT);
        JPanel resultBorderPanel = new JPanel();
        resultBorderPanel.setLayout(new BorderLayout());
        resultBorderPanel.add(new JScrollPane(resultConsolePanel), BorderLayout.CENTER);
        tabbedPane.add("result",resultTablePanel);
        tabbedPane.add("console",resultBorderPanel);
        add(tabbedPane, BorderLayout.CENTER);
    }

    private void clean(){
        if (jTable != null){
            resultTablePanel.removeAll();
        }
    }

    public void showResult(ExecuteRsult rs){
        clean();
        if (rs.isQuery()){
            ResultSetResult result = (ResultSetResult) rs;
            defaultTableModel = new DefaultTableModel(result.getColumnNameList().toArray(), 0);
            for (List<String> row : result.getResultList()){
                defaultTableModel.addRow(row.toArray());
            }
            jTable = new JTable(defaultTableModel);
            resultTablePanel.add(new JScrollPane(jTable, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER), BorderLayout.CENTER);
            resultConsolePanel.add(new JLabel(rs.getSrcSql()));
            resultConsolePanel.add(new JLabel("fetch row size:"+((ResultSetResult) rs).getResultList().size()));
            tabbedPane.setSelectedIndex(0);
        }
        if (rs.isException()){
            ExceptionResult exceptionResult = (ExceptionResult) rs;
            resultConsolePanel.add(new JLabel(exceptionResult.getSrcSql()));
            resultConsolePanel.add(new JLabel("occur exception : "+exceptionResult.getThrowable().getMessage()));
            tabbedPane.setSelectedIndex(1);
        }
        if (rs.isUpdate()){
            UpdateCountResult updateCountResult = (UpdateCountResult) rs;
            tabbedPane.setSelectedIndex(1);
            resultConsolePanel.add(new JLabel(updateCountResult.getSrcSql()));
            resultConsolePanel.add(new JLabel("affect rows : "+updateCountResult.getCount()));
        }
        SwingUtilities.updateComponentTreeUI(this);
    }
}
