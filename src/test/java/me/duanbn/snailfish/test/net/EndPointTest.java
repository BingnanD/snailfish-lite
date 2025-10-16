package me.duanbn.snailfish.test.net;

import java.net.InetSocketAddress;
import java.util.List;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;

import me.duanbn.snailfish.net.EndPoint;
import me.duanbn.snailfish.net.EndPointConfig;
import me.duanbn.snailfish.net.EndPointFactory;
import me.duanbn.snailfish.net.EndPointFuture;
import me.duanbn.snailfish.net.EndPointFuture.FutureListener;
import me.duanbn.snailfish.net.EndPointFuture.FutureResult;
import me.duanbn.snailfish.net.EndPointFutureGroup;
import me.duanbn.snailfish.net.EndPointGroup;
import me.duanbn.snailfish.net.exception.TimeoutException;
import me.duanbn.snailfish.net.message.AckMessage;
import me.duanbn.snailfish.net.message.SendMessage;
import me.duanbn.snailfish.net.netty.NettyEndPointFactory;
import me.duanbn.snailfish.test.Application;

@SpringBootTest(classes = Application.class)
public class EndPointTest {

	private static final Logger LOG = LoggerFactory.getLogger(EndPointTest.class);

	@Test
	public void testServer() throws Exception {
		remoteFactory.createServerEndPoint(9999, new EchoHandler(), new TimeoutHandler()).listen();
		System.in.read();
	}

	@Test
	public void testClient() {
		EndPoint endPoint = remoteFactory.createEndPoint("127.0.0.1", 9999);

		try {
			LOG.info("{}", endPoint.syncSend(new SendMessage<String>("hello sync")).getData());
			// LOG.info("{}", endPoint.syncSend(new UnknowMessage()).getMessage());
		} catch (TimeoutException e) {
			LOG.error(e.getMessage());
		}

		EndPointFuture<AckMessage<String>> remoteFuture = endPoint.asyncSend(new SendMessage<String>("hello async"));
		remoteFuture.addListener(new FutureListener<AckMessage<String>>() {
			@Override
			public void optionComplete(FutureResult<AckMessage<String>> result) {
				AckMessage<String> value = result.getAckMessage();

				if (result.isSuccess()) {
					LOG.info("{}", value.getData());
				} else {
					LOG.error("", result.getCause());
				}
			}
		});

		try {
			Thread.sleep(3000);
		} catch (Exception e) {
		}

		endPoint.shutdown();
	}

	@Test
	public void testGroup() throws Exception {
		EndPointGroup group = remoteFactory.createEndPointGroup(new InetSocketAddress("127.0.0.1", 9999),
				new InetSocketAddress("127.0.0.1", 9998));

		SendMessage<String> msg = new SendMessage<String>("hello");
		EndPointFutureGroup futureGroup = group.send(msg);
		List<AckMessage<String>> result = futureGroup.sync();
		for (AckMessage<String> v : result) {
			LOG.info("{}", v.getData());
		}
	}

	private static EndPointFactory remoteFactory;

	@BeforeClass
	public static void beforeClass() {
		EndPointConfig config = new EndPointConfig();
		remoteFactory = NettyEndPointFactory.getInstance(config);
	}

	@AfterClass
	public static void afterClass() {
		remoteFactory.shutdown();
	}

	public static class UnknowMessage extends SendMessage<Void> {
		private static final long serialVersionUID = 8207110751751262743L;
	}
}
