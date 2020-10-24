package org.client.datasources;

import java.util.ArrayList;
import java.util.List;

public class ResultSetResult extends ExecuteRsult{


    private List<List<String>> resultList = new ArrayList<>();

    private List<String> columnNameList = new ArrayList<>();

    public void addRow(List<String> row){
        resultList.add(row);
    }

    public void addColumn(String column){
        columnNameList.add(column);
    }

    public List<List<String>> getResultList() {
        return resultList;
    }

    public List<String> getColumnNameList() {
        return columnNameList;
    }

    @Override
    public boolean isQuery() {
        return true;
    }
}
