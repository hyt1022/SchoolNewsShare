package com.nowcoder.toutiao.async;


import org.springframework.stereotype.Component;

import java.util.List;


@Component
public interface EventHandler {
    void doHandle(EventModel model);
    List<EventType> getSupportsEventTypes();

}
