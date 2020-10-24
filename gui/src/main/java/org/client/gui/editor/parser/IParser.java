package org.client.gui.editor.parser;

import java.util.List;

public interface IParser {
   List<TextNode> parse(String text);
}
