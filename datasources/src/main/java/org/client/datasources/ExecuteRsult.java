package org.client.datasources;

public class ExecuteRsult {
    public boolean isQuery(){return false;}
    public boolean isUpdate(){return false;}
    public boolean isException(){return false;}
    private String srcSql;
    private String title;

    public String getSrcSql() {
        return srcSql;
    }

    public void setSrcSql(String srcSql) {
        this.srcSql = srcSql;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
