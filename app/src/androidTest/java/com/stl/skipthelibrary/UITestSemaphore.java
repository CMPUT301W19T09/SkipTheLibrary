package com.stl.skipthelibrary;

import java.util.Date;

public class UITestSemaphore {
    private boolean inUse;
    private Date lastAccessd;

    public UITestSemaphore() {
        this.lastAccessd = new Date();
    }

    public void setInUse(boolean inUse) {
        this.inUse = inUse;
        this.lastAccessd = new Date();
    }

    public boolean isInUse() {
        return inUse;
    }

    public Date getLastAccessd() {
        return lastAccessd;
    }

    public void setLastAccessd(Date lastAccessd) {
        this.lastAccessd = lastAccessd;
    }
}
