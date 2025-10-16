package me.duanbn.snailfish.test.ruleengine;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import lombok.extern.slf4j.Slf4j;
import me.duanbn.snailfish.core.ruleengine.Rule;
import me.duanbn.snailfish.core.ruleengine.RuleFactory;
import me.duanbn.snailfish.core.ruleengine.RulesResult;
import me.duanbn.snailfish.test.Application;

@SpringBootTest(classes = Application.class)
@Slf4j
public class RuleTest {

	@Autowired
	private RuleFactory ruleFactory;

	@Test
	public void test() throws Exception {
		String filePath = this.getClass().getClassLoader().getResource("rule/rule.yml").getFile();
		String ruleContent = FileUtils.readFileToString(new File(filePath), "UTF-8");
		Rule rule = this.ruleFactory.create(ruleContent);
		RulesResult rulesResult = rule.fire();
		log.info("{}", rulesResult.getRuleResult());
	}

}
