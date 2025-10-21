package me.duanbn.snailfish.test.eventbus;

import lombok.extern.slf4j.Slf4j;
import me.duanbn.snailfish.core.eventbus.EventHandlerI;

@Slf4j
public class SoWhatEventHandler implements EventHandlerI<SoWhatEvent> {

    @Override
    public void handle(SoWhatEvent event) throws Exception {
        log.info("so what");
    }

}
