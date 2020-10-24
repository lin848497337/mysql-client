package org.client.datasources.serial;

import java.util.List;

public class UserData {
    private java.util.List<Instance> instanceList;
    private List<SqlLog> logList;

    public List<Instance> getInstanceList() {
        return instanceList;
    }

    public void setInstanceList(List<Instance> instanceList) {
        this.instanceList = instanceList;
    }

    public List<SqlLog> getLogList() {
        return logList;
    }

    public void setLogList(List<SqlLog> logList) {
        this.logList = logList;
    }
}
