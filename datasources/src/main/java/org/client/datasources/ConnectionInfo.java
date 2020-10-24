package org.client.datasources;

import java.sql.Connection;

public class ConnectionInfo {

    private final Connection connection;

    private final Host host;

    public ConnectionInfo(Connection connection, Host host) {
        this.connection = connection;
        this.host = host;
    }

    public Connection getConnection() {
        return connection;
    }


    public Host getHost() {
        return host;
    }

}
