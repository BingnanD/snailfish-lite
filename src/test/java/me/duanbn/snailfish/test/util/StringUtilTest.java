/**
 * 
 */
package me.duanbn.snailfish.test.util;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;

import lombok.extern.slf4j.Slf4j;
import me.duanbn.snailfish.util.collection.Lists;
import me.duanbn.snailfish.util.lang.StringUtil;

/**
 * @author bingnan.dbn
 *
 */
@Slf4j
public class StringUtilTest {

    @Test
    public void testJoin() {
        ArrayList<String> op = Lists.newArrayList("c", "u", "d");
        String result = StringUtil.join(op, ",");
        log.info("{}", result);
    }

}
