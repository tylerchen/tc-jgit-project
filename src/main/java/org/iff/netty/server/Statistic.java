/*******************************************************************************
 * Copyright (c) 2019-01-17 @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>.
 * All rights reserved.
 *
 * Contributors:
 *     <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> - initial API and implementation.
 * Auto Generate By foreveross.com Quick Deliver Platform. 
 ******************************************************************************/
package org.iff.netty.server;

import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicLongArray;

/**
 * Statistic
 *
 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>
 * @since 2019-01-17
 * auto generate by qdp.
 */
public class Statistic {

    public static Statistic INSTANCE = new Statistic();

    protected AtomicLong countIn = new AtomicLong(0);
    protected AtomicLong countOut = new AtomicLong(0);
    protected AtomicLong countInvalid = new AtomicLong(0);
    protected AtomicLong countErrors = new AtomicLong(0);
    protected long[] spentTimeRang = new long[]{1, 10, 100, 200, 400, 600, 800, 1000, 2 * 1000, 4 * 1000, 6 * 1000, 8 * 1000, 10 * 1000, 100 * 1000, 1000 * 1000};
    protected AtomicLongArray countSpentTime = new AtomicLongArray(spentTimeRang.length);

    public long countSpentTime(long spentTimeMs) {
        int index = 0;
        for (; index < spentTimeRang.length - 1; index++) {
            if (spentTimeMs < spentTimeRang[index]) {
                index = Math.max(index - 1, 0);
                break;
            }
        }
        return countSpentTime.incrementAndGet(index);
    }

    public long countIn() {
        return countIn.incrementAndGet();
    }

    public long countOut() {
        return countOut.incrementAndGet();
    }

    public long countInvalid() {
        return countInvalid.incrementAndGet();
    }

    public long countErrors() {
        return countErrors.incrementAndGet();
    }

    public AtomicLong getCountIn() {
        return countIn;
    }

    public void setCountIn(AtomicLong countIn) {
        this.countIn = countIn;
    }

    public long getCountOut() {
        return countOut.get();
    }

    public long getCountInvalid() {
        return countInvalid.get();
    }

    public long getCountErrors() {
        return countErrors.get();
    }

    public long[] getSpentTimeRang() {
        return spentTimeRang;
    }

    public long[] getCountSpentTime() {
        long[] values = new long[countSpentTime.length()];
        for (int i = 0; i < countSpentTime.length(); i++) {
            values[i] = countSpentTime.get(i);
        }
        return values;
    }
}
