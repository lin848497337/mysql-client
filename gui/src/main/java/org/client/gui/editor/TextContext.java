package org.client.gui.editor;

import org.client.common.StringUtils;

import java.util.*;

public class TextContext {

    private Map<Character, Node> nodeMap = new HashMap<>();


    public TextContext() {
        List<String> keyList = new ArrayList<>();
        keyList.add("SELECT");
        keyList.add("UPDATE");
        keyList.add("INSERT");
        keyList.add("VALUE");
        keyList.add("VALUES");
        keyList.add("DISTINCT");
        keyList.add("TABLE");
        keyList.add("DATABASES");
        keyList.add("WHERE");
        keyList.add("FROM");
        keyList.add("SET");
        keyList.add("GROUP");
        keyList.add("BY");
        keyList.add("LIMIT");
        keyList.add("AND");
        keyList.add("OR");
        keyList.add("DELETE");
        build(keyList);
    }

    private void build(List<String> keyList){
        keyList.forEach(s -> {
            pushKey(s);
            pushKey(s.toLowerCase());
        });
    }

    private void pushKey(String key){
        char c = key.charAt(0);
        Node node = nodeMap.get(c);
        if (node == null){
            node = new Node();
            nodeMap.put(c, node);
        }
        pushKey(node, key,1);
    }

    private void pushKey(Node n,String key, int i){
        if (i >= key.length()){
            n.value = key;
            return;
        }
        char c = key.charAt(i);
        Node node = n.nodeMap.get(c);
        if (node == null){
            node = new Node();
            n.nodeMap.put(c, node);
        }
        pushKey(node, key, i+1);
    }

    public List<String> get(String keyWord){
        List<String> list = new ArrayList<>();
        Map<Character, Node> nm = nodeMap;
        Node n = null;
        for (int i=0 ; i<keyWord.length() ; i++){
            char c = keyWord.charAt(i);
            n = nm.get(c);
            if (n == null){
                return null;
            }
            nm = n.nodeMap;
        }

        if (n.nodeMap != null && !n.nodeMap.isEmpty()){
            LinkedList<Node> tempNodeList = new LinkedList<>();
            tempNodeList.add(n);
            while (!tempNodeList.isEmpty()){
                Node node = tempNodeList.pollFirst();
                if (!StringUtils.isBlank(node.value)){
                    list.add(node.value);
                }
                tempNodeList.addAll(node.nodeMap.values());
            }

        }
        return list;
    }

    public List<String> input(String keyWord){
        return get(keyWord);
    }


    private class Node{
        private String value;
        Map<Character,Node> nodeMap = new HashMap();

    }
}
