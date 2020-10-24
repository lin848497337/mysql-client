package org.client.datasources;

import com.alibaba.fastjson.JSON;
import org.client.common.StringUtils;
import org.client.datasources.serial.Instance;

import java.sql.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MultiDataSourcesManager {

    private static MultiDataSourcesManager instance = new MultiDataSourcesManager();

    private static final String connectionUrl = "jdbc:mysql://%s:%d/%s?user=%s&password=%s&characterEncoding=%s&autoReconnect=true&useSSL=false";

    private Map<String, Map<String,ConnectionInfo>> connectionMap = new ConcurrentHashMap<>();

    private MultiDataSourcesManager(){
        try {
            init();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }


    public List<Instance> getData(){
        List<Instance> instanceList = new ArrayList<>();
        for (Map.Entry<String, Map<String,ConnectionInfo>> entry : connectionMap.entrySet()){
            Instance instance = new Instance();
            instance.setName(entry.getKey());
            for (Map.Entry<String, ConnectionInfo> innerEntry : entry.getValue().entrySet()){
                instance.getHost().add(innerEntry.getValue().getHost());
            }
            instanceList.add(instance);
        }
        return instanceList;
    }

    public void init(List<Instance> instanceList){
        Iterator<Instance> instanceIterator = instanceList.iterator();
        while (instanceIterator.hasNext()){
            Instance instance = instanceIterator.next();
            int successCount = 0;
            for (Host host : instance.getHost()){
                try {
                    addConnection(instance.getName(), host);
                    successCount++;
                } catch (Exception throwables) {
                    throwables.printStackTrace();

                }
            }
            if (successCount == 0){
                instanceIterator.remove();
            }
        }
    }

    public void init() throws ClassNotFoundException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        Runtime.getRuntime().addShutdownHook(new Thread(() -> release()));
    }

    public static MultiDataSourcesManager getInstance() {
        return instance;
    }

    public boolean test(Host host) throws SQLException {
        Connection connection = DriverManager.getConnection(String.format(connectionUrl, host.getIp(), host.getPort(), host.getDbName(), host.getUser(), host.getPwd(), host.getEncoding()));
        connection.close();
        return true;
    }

    public void addConnection(String name, Host host) throws Exception {
        Connection connection = DriverManager.getConnection(String.format(connectionUrl, host.getIp(), host.getPort(), host.getDbName(), host.getUser(), host.getPwd(), host.getEncoding()));
        Map<String, ConnectionInfo> connectionInfoMap = connectionMap.get(name);
        if (connectionInfoMap == null){
            connectionInfoMap = new ConcurrentHashMap<>();
            connectionMap.put(name, connectionInfoMap);
        }
        if (!StringUtils.isBlank(host.getDbName())){
            connectionInfoMap.put(host.getDbName(), new ConnectionInfo(connection, host));
        }else {
            PreparedStatement statement = connection.prepareStatement("show databases");
            ResultSet rs = statement.executeQuery();
            while (rs.next()){
                String dbName = rs.getString(1);
                Host cloneHost = host.copy();
                cloneHost.setDbName(dbName);
                connectionInfoMap.put(dbName, new ConnectionInfo(connection, cloneHost));
            }
        }
    }

    public List<String> getDbListName(String name){
        Map<String, ConnectionInfo> connectionInfoMap  = connectionMap.get(name);
        return new ArrayList<String>(connectionInfoMap.keySet());
    }

    private Connection getConnByDbName(String dbName) throws SQLException {
        for (Map<String,ConnectionInfo> connectionInfoMap : connectionMap.values()){
            ConnectionInfo connectionInfo = connectionInfoMap.get(dbName);
            if (connectionInfo == null){
                continue;
            }
            Connection connection = connectionInfo.getConnection();
            useDb(connection, dbName);
            return connection;
        }
        return null;
    }

    private void useDb(Connection connection, String dbName) throws SQLException {
        PreparedStatement ps = connection.prepareStatement("use "+dbName);
        ps.executeUpdate();
        ps.close();
    }

    public ExecuteRsult executeSql(String name, String sql) throws SQLException {
        Connection connection = getConnByDbName(name);
        PreparedStatement statement = connection.prepareStatement(sql);
        try{
            statement.execute();
            ResultSet resultSet = statement.getResultSet();
            if (resultSet != null){
                ResultSetResult resultSetResult = new ResultSetResult();
                resultSetResult.setSrcSql(sql);
                resultSetResult.setTitle(name);
                int columnCount = resultSet.getMetaData().getColumnCount();
                for (int i=0 ; i<columnCount ; i++){
                    resultSetResult.addColumn(resultSet.getMetaData().getColumnName(i + 1));
                }
                while (resultSet.next()){
                    List<String> dataList = new ArrayList<>();
                    for (int i=0 ; i<columnCount ; i++){
                        dataList.add(resultSet.getString(i+1));
                    }
                    resultSetResult.addRow(dataList);
                }
                return resultSetResult;
            }

            int updateCount = statement.getUpdateCount();
            if (updateCount != -1){
                UpdateCountResult updateCountResult = new UpdateCountResult();
                updateCountResult.setCount(updateCount);
                updateCountResult.setSrcSql(sql);
                updateCountResult.setTitle(name);
                return updateCountResult;
            }
        }finally {
            statement.close();
        }

        return null;
    }

    public void release(){
        for (Map<String,ConnectionInfo> connectionInfoMap : connectionMap.values()){
            for (ConnectionInfo connectionInfo : connectionInfoMap.values()){
                try {
                    connectionInfo.getConnection().close();
                } catch (SQLException throwables) {

                }
            }

        }
    }

    public List<String> getTableList(String dbName) throws SQLException {
        Connection connection = getConnByDbName(dbName);
        PreparedStatement statement = connection.prepareStatement("SHOW TABLES");
        ResultSet rs = statement.executeQuery();
        List<String> tableList = new ArrayList<>();
        while (rs.next()){
            tableList.add(rs.getString(1));
        }
        return tableList;
    }
}
