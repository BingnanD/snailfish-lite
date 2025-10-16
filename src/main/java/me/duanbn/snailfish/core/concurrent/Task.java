/**
 * 
 */
package me.duanbn.snailfish.core.concurrent;

import java.util.concurrent.atomic.AtomicInteger;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * @author bingnan.dbn
 *
 */
@Slf4j
public abstract class Task<C> {

    private static final AtomicInteger counter = new AtomicInteger();
    private java.lang.Thread thread0;

    private boolean run = false;
    @Setter
    @Getter
    private Long sleep = 10L;
    @Setter
    @Getter
    private C context;

    public abstract void doWork(C context) throws Exception;

    protected void beforeStart(C context) throws Exception {
    }

    protected void afterStop(C context) throws Exception {
    }

    @SuppressWarnings("static-access")
    public void start() {
        String name = "snailfish-task-" + counter.getAndIncrement();
        this.run = true;

        try {
            this.beforeStart(this.context);
            if (log.isDebugEnabled()) {
                log.debug("task {} init done", name);
            }
        } catch (Exception e) {
            throw new TaskException("init task err", e);
        }

        this.thread0 = new Thread(() -> {
            while (run && !this.thread0.isInterrupted()) {
                try {
                    doWork(this.context);
                    this.thread0.sleep(sleep);
                } catch (InterruptedException e) {
                    run = false;
                } catch (Exception e) {
                    log.error("task internel err, this task will be stop", e);
                    run = false;
                }
            }
        });

        this.thread0.setName(name);
        this.thread0.start();

        if (log.isDebugEnabled()) {
            log.debug("task {} has start", this.thread0.getName());
        }
    }

    public void stop() {
        if (run) {
            run = false;
            this.thread0.interrupt();
        }

        if (log.isDebugEnabled()) {
            log.debug("task {} has stopped", this.thread0.getName());
        }

        try {
            this.afterStop(this.context);
        } catch (Exception e) {
            throw new TaskException("after stop task err", e);
        }
    }

}
