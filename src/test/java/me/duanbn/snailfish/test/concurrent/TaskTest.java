/**
 * 
 */
package me.duanbn.snailfish.test.concurrent;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.junit.jupiter.api.Test;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import me.duanbn.snailfish.core.concurrent.Task;

/**
 * @author bingnan.dbn
 *
 */
@Slf4j
public class TaskTest {

    @Test
    public void test() throws Exception {
        MyContext myContext = new MyContext();
        myContext.setBuffer(new LinkedBlockingQueue<>());

        Producer producer = new Producer();
        producer.setSleep(100L);
        producer.setContext(myContext);
        Consumer consumer = new Consumer();
        consumer.setSleep(100L);
        consumer.setContext(myContext);

        producer.start();
        consumer.start();

        Thread.sleep(3000);

        producer.stop();
        consumer.stop();

        Thread.sleep(2000);
    }

    @Data
    public static class MyContext {
        private BlockingQueue<String> buffer;
    }

    public static class Producer extends Task<MyContext> {
        @Override
        protected void beforeStart(MyContext context) throws Exception {
            log.info("this is before start");
        }

        @Override
        protected void afterStop(MyContext context) throws Exception {
            log.info("this is after stop");
        }

        @Override
        public void doWork(MyContext context) throws Exception {
            context.getBuffer().put("running...");
        }
    }

    public static class Consumer extends Task<MyContext> {
        @Override
        protected void beforeStart(MyContext context) throws Exception {
            log.info("this is before start");
        }

        @Override
        protected void afterStop(MyContext context) throws Exception {
            log.info("this is after stop");
        }

        @Override
        public void doWork(MyContext context) throws Exception {
            log.info("{}", context.getBuffer().take());
        }
    }

}
