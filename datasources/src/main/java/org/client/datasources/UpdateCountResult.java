package org.client.datasources;

public class UpdateCountResult extends ExecuteRsult{

    private int count;

    public void setCount(int count) {
        this.count = count;
    }

    public int getCount() {
        return count;
    }

    @Override
    public boolean isUpdate() {
        return true;
    }
}
