package org.client.datasources;

public class ExceptionResult extends ExecuteRsult{


    private Throwable throwable;

    public ExceptionResult(Throwable throwable) {
        this.throwable = throwable;
    }

    public Throwable getThrowable() {
        return throwable;
    }

    @Override
    public boolean isException() {
        return true;
    }
}
