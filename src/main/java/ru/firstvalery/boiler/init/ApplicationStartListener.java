package ru.firstvalery.boiler.init;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextStartedEvent;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class ApplicationStartListener implements ApplicationListener<ContextStartedEvent> {
    private final List<Initializable> beans;

    public ApplicationStartListener(List<Initializable> beans) {
        this.beans = beans;
    }

    @Override
    public void onApplicationEvent(ContextStartedEvent event) {
        init();
    }

    private void init() {
        beans.forEach(Initializable::init);
    }
}
