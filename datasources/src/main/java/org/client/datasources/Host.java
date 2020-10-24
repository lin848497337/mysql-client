package org.client.datasources;

import java.util.Objects;

public class Host {

    private String ip;
    private int port;
    private String user;
    private String pwd;
    private String dbName;
    private String encoding = "utf8";


    public String getDbName() {
        return dbName;
    }

    public void setDbName(String dbName) {
        this.dbName = dbName;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getEncoding() {
        return encoding;
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Host host = (Host) o;
        return port == host.port && Objects.equals(ip, host.ip) && Objects.equals(user, host.user)
               && Objects.equals(pwd, host.pwd) && Objects.equals(encoding, host.encoding);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ip, port, user, pwd, encoding);
    }

    public Host copy(){
        Host host = new Host();
        host.setDbName(dbName);
        host.setPwd(pwd);
        host.setUser(user);
        host.setPort(port);
        host.setIp(ip);
        host.setEncoding(encoding);
        return host;
    }
}
