package ru.firstvalery.boiler.sensors;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.firstvalery.boiler.modbus.master.DataExchangeService;

import javax.annotation.PreDestroy;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class SensorServiceImpl implements SensorService {
    private final ScheduledExecutorService executorService;
    private final DataExchangeService dataExchangeService;
    private final List<AnalogParameter> analogParameters = new CopyOnWriteArrayList<>();
    private final List<HeatingElement> heatingElements = new CopyOnWriteArrayList<>();


    public SensorServiceImpl(DataExchangeService dataExchangeService) {
        this.dataExchangeService = dataExchangeService;
        registerAnalogParameters();
        registerHeatingElements();
        this.executorService = Executors.newSingleThreadScheduledExecutor();
        executorService.scheduleWithFixedDelay(this::task, 5, 2, TimeUnit.SECONDS);
    }


    private void registerAnalogParameters() {
        AnalogParameter onBoilerInTemperature = AnalogParameter.builder()
                .name("T трубы на вх обогревателя")
                .offset(0)
                .build();
        analogParameters.add(onBoilerInTemperature);

        AnalogParameter onBoilerOutTemperature = AnalogParameter.builder()
                .name("T трубы на выx обогревателя")
                .offset(1)
                .build();
        analogParameters.add(onBoilerOutTemperature);

        AnalogParameter airTemperature = AnalogParameter.builder()
                .name("Т возд. в помещении")
                .offset(2)
                .build();
        analogParameters.add(airTemperature);
    }

    private void registerHeatingElements() {
        for (int i = 0; i < 3; i++) {
            HeatingElement heatingElement = HeatingElement.builder()
                    .name("ТЭН-" + (i + 1))
                    .build();
            heatingElements.add(heatingElement);
        }
    }

    void task() {
        short[] area = dataExchangeService.getReadArray();
        analogParameters.forEach(analogParameter -> {
            analogParameter.recalculate(area);
        });
    }


    @PreDestroy
    public void shutDown() {
        executorService.shutdown();
    }

    @Override
    public List<AnalogParameter> getAnalogParameters() {
        return analogParameters;
    }

    @Override
    public List<HeatingElement> getHeatingElements() {
        return heatingElements;
    }
}
