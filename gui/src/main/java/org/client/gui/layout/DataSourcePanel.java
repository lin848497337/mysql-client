package org.client.gui.layout;

import org.client.common.StringUtils;
import org.client.datasources.Host;
import org.client.datasources.MultiDataSourcesManager;
import org.client.datasources.serial.Instance;
import org.client.gui.component.DataSourceDialog;
import org.client.gui.component.IconNode;
import org.client.gui.listener.NewEditorListener;
import org.client.gui.listener.UpdateListener;

import javax.swing.*;
import javax.swing.event.MouseInputAdapter;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

public class DataSourcePanel extends JPanel {

    IconNode         root      = new IconNode(new ImageIcon("/icon/databases.jpg"), "Databases", IconNode.TYPE_ROOT);
    DefaultTreeModel treeModel = new DefaultTreeModel(root);

    private ImageIcon databaseIcon;
    private ImageIcon tableIcon;
    private ImageIcon instanceIcon;

    private JTree tree;

    private NewEditorListener newEditorListener;

    private UpdateListener updateListener;


    public DataSourcePanel() {
        JToolBar toolBar = new JToolBar();
        toolBar.setFloatable(false);
        JButton addBtn = new JButton("+");
        tree = new JTree(treeModel);
        addBtn.addActionListener((e)->{
            DataSourceDialog dataSourceDialog = new DataSourceDialog();
            dataSourceDialog.setLocationRelativeTo(this.getParent());
            dataSourceDialog.setVisible(true);
            if (dataSourceDialog.isOk()){
                try {
                    String database = dataSourceDialog.getDatabaseField();
                    java.util.List<String> dbList = new ArrayList<>();
                    if(!StringUtils.isBlank(database)){
                        dbList.add(database);
                    }
                    addInstance(dataSourceDialog.getNameField(), dbList);
                    if (updateListener != null){
                        updateListener.onUpdate();
                    }
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        });
        toolBar.add(addBtn);

        JButton delBtn = new JButton("-");
        toolBar.add(delBtn);

        ImageIcon databaseSrcIcon = new ImageIcon(getClass().getResource("/icon/Database.png"));
        databaseIcon = new ImageIcon(databaseSrcIcon.getImage().getScaledInstance(16, 16, Image.SCALE_DEFAULT));

        ImageIcon tableSrcIcon = new ImageIcon(getClass().getResource("/icon/table.png"));
        tableIcon = new ImageIcon(tableSrcIcon.getImage().getScaledInstance(16, 16, Image.SCALE_DEFAULT));

        ImageIcon instanceSrcIcon = new ImageIcon(getClass().getResource("/icon/instance.png"));
        instanceIcon = new ImageIcon(instanceSrcIcon.getImage().getScaledInstance(16, 16, Image.SCALE_DEFAULT));


        tree.setCellRenderer(new DefaultTreeCellRenderer(){

            @Override
            public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded,
                                                          boolean leaf, int row, boolean hasFocus) {
                super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
                Icon icon = ((IconNode) value).getIcon();// 从节点读取图片
                String txt = ((IconNode) value).getText(); // 从节点读取文本
                setIcon(icon);// 设置图片
                setText(txt);// 设置文本
                return this;
            }
        });
        setLayout(new BorderLayout());
        add(toolBar, BorderLayout.NORTH);
        add(new JScrollPane(tree, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED , JScrollPane.HORIZONTAL_SCROLLBAR_NEVER), BorderLayout.CENTER);
        tree.addMouseListener(new MouseInputAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON3){
                    JPopupMenu textMenu = new JPopupMenu();
                    JMenuItem copy = new JMenuItem("Copy");
                    TreePath treePath = tree.getSelectionPath();
                    IconNode iconNode = (IconNode) treePath.getLastPathComponent();
                    copy.addActionListener((ev)->{
                        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                        // 封装文本内容
                        Transferable trans = new StringSelection(iconNode.getText().trim());
                        // 把文本内容设置到系统剪贴板
                        clipboard.setContents(trans, null);
                    });


                    if (iconNode.isDb()){
                        JMenuItem newEditor = new JMenuItem("Open New Editor");
                        newEditor.addActionListener((ev)->{
                            if (newEditorListener != null){
                                newEditorListener.actionPerform(iconNode.getText());
                            }
                        });
                        textMenu.add(newEditor);
                    }
                    textMenu.add(copy);
                    textMenu.show(e.getComponent(), e.getX(), e.getY());
                }
            }
        });
    }

    public void addUpdateListener(UpdateListener listener){
        this.updateListener = listener;
    }

    /**
     * 增加实力，如果dbName为空，则会执行show databases;
     * @param alisName
     * @param dbNameList
     * @throws SQLException
     */
    private void addInstance(String alisName, java.util.List<String> dbNameList) throws SQLException {

        IconNode firstNode = null ;
        if (root.getChildCount() > 0){
            firstNode = (IconNode) root.getFirstChild();
        }

        while (firstNode!=null && !firstNode.getText().equals(alisName)){
            firstNode = (IconNode) firstNode.getNextSibling();
        }

        IconNode node1 = null;
        if (firstNode != null){
            node1 = firstNode;
        }else{
            node1 = new IconNode(instanceIcon, alisName, IconNode.TYPE_INSTANCE);
            treeModel.insertNodeInto(node1, root, root.getChildCount());
        }

        if (dbNameList == null || dbNameList.isEmpty()){
            dbNameList = MultiDataSourcesManager.getInstance().getDbListName(alisName);
        }
        for (String d : dbNameList){
            addDatabase(node1, d, MultiDataSourcesManager.getInstance().getTableList(d));
        }

    }

    private void addDatabase(IconNode parent, String database, Collection<String> tableList){
        IconNode node1 = new IconNode(databaseIcon, database, IconNode.TYPE_DB);
        for (String table : tableList){
            treeModel.insertNodeInto(new IconNode(tableIcon, table, IconNode.TYPE_TABLE), node1, node1.getChildCount());
        }
        treeModel.insertNodeInto(node1, parent, parent.getChildCount());
    }

    public void setNewEditorListener(NewEditorListener newEditorListener) {
        this.newEditorListener = newEditorListener;
    }

    public void reload(java.util.List<Instance> instanceList){
        for (Instance instance : instanceList){
            java.util.List<String> dbList = new ArrayList<>();
            for (Host host : instance.getHost()){
                dbList.add(host.getDbName());
            }
            try {
                addInstance(instance.getName(), dbList);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }
}
