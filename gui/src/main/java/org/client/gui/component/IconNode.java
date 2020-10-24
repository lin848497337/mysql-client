package org.client.gui.component;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;

public class IconNode  extends DefaultMutableTreeNode {

    public static final int TYPE_ROOT = 0;
    public static final int TYPE_INSTANCE = 1;
    public static final int TYPE_DB = 2;
    public static final int TYPE_TABLE = 3;

    protected final Icon   icon;
    protected final String text;
    protected final int type;

    public IconNode(Icon icon, String text, int type) {
        this.icon = icon;
        this.text = text;
        this.type = type;
    }

    public Icon getIcon() {
        return icon;
    }


    public String getText() {
        return text;
    }

    public boolean isTable(){
        return type == TYPE_TABLE;
    }

    public boolean isDb(){
        return type == TYPE_DB;
    }

    @Override
    public String toString() {
        return "IconNode{" + "icon=" + icon + ", text='" + text + '\'' + ", type=" + type + '}';
    }
}
