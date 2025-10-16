package me.duanbn.snailfish.test.util;

import java.util.LinkedHashMap;

import org.junit.Assert;
import org.junit.jupiter.api.Test;

import lombok.extern.slf4j.Slf4j;
import me.duanbn.snailfish.util.collection.LRU;

@Slf4j
public class LRUTest {

    @Test
    public void test() {
        LRU<String, String> lru = new LRU<>(3);
        lru.put("a", "A");
        lru.put("b", "B");
        lru.put("c", "C");
        log.info("{}", lru);

        lru.get("a");
        lru.get("c");
        LinkedHashMap<Object, Object> expect = new LinkedHashMap<>();
        expect.put("b", "B");
        expect.put("a", "A");
        expect.put("c", "C");
        Assert.assertEquals(expect, lru);

        lru.put("d", "D");
        expect.clear();
        expect.put("a", "A");
        expect.put("c", "C");
        expect.put("d", "D");
        Assert.assertEquals(expect, lru);
    }

}
