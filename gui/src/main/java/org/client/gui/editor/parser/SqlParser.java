package org.client.gui.editor.parser;

import java.awt.*;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class SqlParser extends KeyWordParser{


    public SqlParser() {
        super(Arrays.asList(defineKey(), defineSign(), new NumberKeyStyleDef(Color.decode("#6699FF"), false, false)));
    }

    private static DefaultKeySpecialStyleDef defineSign(){
        Set<String> keyWorldSet = new HashSet<String>();
        keyWorldSet.add(";");
        keyWorldSet.add(",");
        keyWorldSet.add("'");
        keyWorldSet.add("\"");
        return new DefaultKeySpecialStyleDef(keyWorldSet, Color.decode("#FF9900"), true, false);
    }

    private static DefaultKeySpecialStyleDef defineKey(){
        Set<String> keyWorldSet = new HashSet<String>();
        keyWorldSet.add("SELECT");
        keyWorldSet.add("FROM");
        keyWorldSet.add("TABLE");
        keyWorldSet.add("WHERE");
        keyWorldSet.add("ALL");
        keyWorldSet.add("GRANT");
        keyWorldSet.add("DELETE");
        keyWorldSet.add("INSERT");
        keyWorldSet.add("UPDATE");
        keyWorldSet.add("IN");
        keyWorldSet.add("VALUES");
        keyWorldSet.add("INTO");
        keyWorldSet.add("OR");
        keyWorldSet.add("LIMIT");
        keyWorldSet.add("GROUP");
        keyWorldSet.add("BY");
        keyWorldSet.add("TO");
        keyWorldSet.add("ON");
        keyWorldSet.add("JOIN");
        keyWorldSet.add("AND");
        keyWorldSet.add("INNER");
        keyWorldSet.add("OUTER");
        keyWorldSet.add("KEY");
        keyWorldSet.add("INDEX");
        keyWorldSet.add("PRIMARY");
        keyWorldSet.add("AUTO");
        keyWorldSet.add("INCREMENT");
        keyWorldSet.add("AUTO_INCREMENT");
        keyWorldSet.add("BEGIN");
        keyWorldSet.add("END");
        keyWorldSet.add("COMMIT");
        keyWorldSet.add("SHOW");
        keyWorldSet.add("DESC");
        keyWorldSet.add("ASC");
        keyWorldSet.add("ORDER");
        keyWorldSet.add("SET");
        keyWorldSet.add("AS");
        keyWorldSet.add("DISTINCT");
        keyWorldSet.add("VARCHAR");
        keyWorldSet.add("BITINT");
        keyWorldSet.add("INT");
        keyWorldSet.add("TEXT");
        keyWorldSet.add("LONGTEXT");
        return new DefaultKeySpecialStyleDef(keyWorldSet, Color.decode("#FF9900"), true, false);
    }
}
