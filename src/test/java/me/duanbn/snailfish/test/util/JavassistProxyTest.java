package me.duanbn.snailfish.test.util;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import org.junit.jupiter.api.Test;

import lombok.extern.slf4j.Slf4j;
import me.duanbn.snailfish.test.pluginbus.PersonExtPt;
import me.duanbn.snailfish.util.JavassistProxy;

@Slf4j
public class JavassistProxyTest {

	@Test
	public void test() throws Exception {
		Object proxyObject = JavassistProxy.newProxyInstance(PersonExtPt.class, new DistributePluginInvocation());
		log.info("{}", proxyObject);
	}

	public static class DistributePluginInvocation implements InvocationHandler {

		@Override
		public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
			return "test";
		}

	}
}
