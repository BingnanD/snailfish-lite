package me.duanbn.snailfish.test.util;

import org.junit.jupiter.api.Test;

import com.alibaba.fastjson.JSON;

import lombok.extern.slf4j.Slf4j;
import me.duanbn.snailfish.test.domain.beans.DomainEntityE;
import me.duanbn.snailfish.util.Optional;

@Slf4j
public class OptionalTest {

    @Test
    public void testNullable() {
        DomainEntityE domainEntityE = new DomainEntityE();
        Optional.ofNullable(domainEntityE).ifPresent(e -> {
            domainEntityE.setName("hello snailfish");
        });
        log.info("{}", JSON.toJSONString(domainEntityE));

        String s = null;
        Optional.ofNullable(s).ifPresent(e -> {
            log.info("accept a null object");
        }).orElse(e -> {
            log.info("s is null");
        });

        s = "";
        Optional.ofNullable(s).ifPresent(e -> {
            log.info("accept a not null object");
        }).orElse(e -> {
            log.info("s is null");
        });
    }

    @Test
    public void testEmptyable() {

        Optional.ofEmptyable("aaa").ifPresent(e -> {
            log.info("hello {}", e);
        }).orElse(e -> {
            log.info("hello empty string");
        });

        Optional.ofEmptyable("").ifPresent(e -> {
            log.info("hello not empty string");
        }).orElse(e -> {
            log.info("hello empty string");
        });
    }

}
