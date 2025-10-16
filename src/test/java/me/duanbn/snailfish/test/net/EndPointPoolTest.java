package me.duanbn.snailfish.test.net;

import org.junit.jupiter.api.Test;

import lombok.extern.slf4j.Slf4j;
import me.duanbn.snailfish.core.concurrent.Executor;
import me.duanbn.snailfish.core.concurrent.Executors;
import me.duanbn.snailfish.net.EndPointConfig;
import me.duanbn.snailfish.net.EndPointFactory;
import me.duanbn.snailfish.net.message.SendMessage;
import me.duanbn.snailfish.net.netty.NettyEndPointFactory;
import me.duanbn.snailfish.net.pool.EndPointPool;
import me.duanbn.snailfish.net.pool.EndPointWrapper;
import me.duanbn.snailfish.util.Random;
import me.duanbn.snailfish.util.Sleep;

@Slf4j
public class EndPointPoolTest {

    private Executors executors = Executors.getInstance();

    @Test
    public void testNettyPointPool() throws Exception {
        EndPointConfig config = new EndPointConfig();
        config.setMinEndPoint(1).setMaxEndPoint(5).setMaxIdleTime(1000);
        EndPointFactory instance = NettyEndPointFactory.getInstance(config);
        EndPointPool endPointPool = instance.createEndPointPool("127.0.0.1", 9999);

        Executor executor = executors.create();

        for (int i = 0; i < config.getMaxEndPoint(); i++) {
            executor.submit(() -> {
                while (true) {
                    try {
                        EndPointWrapper endPoint = endPointPool.getEndPoint();
                        log.info("{}", endPoint.syncSend(new SendMessage<>("hello pool")).getData());
                        endPoint.close();
                    } catch (Exception e) {
                        log.error("", e);
                    }
                    Sleep.doSleep(Random.nextInt(3000, 5000));
                }
            });
        }

        System.in.read();
    }

}
