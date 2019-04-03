package com.examle.core.common.threadlocal;

public class InternalThread extends Thread  {
    private InternalThreadLocalMap threadLocalMap;

    /**
     * 返回将threadLocal变量绑定到此线程的内部数据结构。
     * 注意此方法只供内部使用，因此可能随时更改
     * @return
     */
    public final InternalThreadLocalMap threadLocalMap() {
        return threadLocalMap;
    }

    public final void setThreadLocalMap(InternalThreadLocalMap threadLocalMap) {
        this.threadLocalMap = threadLocalMap;
    }
}
