package org.client.datasources.serial;

import org.client.datasources.Host;

import java.util.ArrayList;
import java.util.List;

public class Instance {

    private String     name;
    private List<Host> host = new ArrayList<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Host> getHost() {
        return host;
    }

    public void setHost(List<Host> host) {
        this.host = host;
    }

    @Override
    public String toString() {
        return "Instance{" + "name='" + name + '\'' + ", host=" + host + '}';
    }
}
