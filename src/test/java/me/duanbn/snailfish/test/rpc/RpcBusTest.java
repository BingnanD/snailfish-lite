// package me.duanbn.snailfish.test.rpc;
//
// import org.junit.jupiter.api.Test;
// import org.springframework.boot.test.context.SpringBootTest;
//
// import me.duanbn.snailfish.core.rpc.RpcBus;
// import me.duanbn.snailfish.example.domain.caffeeshop.rpc.WaiterEchoRpc;
// import me.duanbn.snailfish.test.Application;
// import me.duanbn.snailfish.util.Random;
// import me.duanbn.snailfish.util.Sleep;
//
// import lombok.extern.slf4j.Slf4j;
//
// @Slf4j
// @SpringBootTest(classes = Application.class)
// public class RpcBusTest {
//
// @Test
// public void test() {
// WaiterEchoRpc waiterEchoRpc = RpcBus.createRpcStub(WaiterEchoRpc.class);
//
// while (true) {
// String msg = waiterEchoRpc.echo("hello snailfish rpc");
// log.info("{}", msg);
// Sleep.doSleep(Random.nextInt(1000, 2000));
// }
// }
//
// }
