package ru.firstvalery.boiler.sensors;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.firstvalery.boiler.modbus.master.DataExchangeService;
import ru.firstvalery.boiler.model.repository.AnalogParameterRepository;

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
    private final List<AnalogSensor> analogSensors = new CopyOnWriteArrayList<>();
    private final List<HeatingElement> heatingElements = new CopyOnWriteArrayList<>();
    private final AnalogParameterRepository analogParameterRepository;


    public SensorServiceImpl(DataExchangeService dataExchangeService, AnalogParameterRepository analogParameterRepository) {
        this.dataExchangeService = dataExchangeService;
        this.analogParameterRepository = analogParameterRepository;
        this.executorService = Executors.newSingleThreadScheduledExecutor();
    }

    @Override
    public void init() {
        registerAnalogParameters();
        registerHeatingElements();
        executorService.scheduleWithFixedDelay(this::task, 5, 500, TimeUnit.MILLISECONDS);
    }


    private void registerAnalogParameters() {
        analogParameterRepository.findAll().forEach(analogParameter -> {
            AnalogSensor analogSensor = AnalogSensor.builder()
                    .offset(analogParameter.getDataOffset())
                    .name(analogParameter.getDescription())
                    .minAlarm(analogParameter.getAlarmMin())
                    .minLim(analogParameter.getLimMin())
                    .maxLim(analogParameter.getLimMax())
                    .maxAlarm(analogParameter.getAlarmMax())
                    .hysteresis(analogParameter.getHysteresis())
                    .blur(analogParameter.getBlur())
                    .build();
            analogSensors.add(analogSensor);
        });
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
        analogSensors.forEach(analogSensor -> {
            analogSensor.recalculate(area);
        });
    }


    @PreDestroy
    public void shutDown() {
        executorService.shutdown();
    }

    @Override
    public List<AnalogSensor> getAnalogParameters() {
        return analogSensors;
    }

    @Override
    public List<HeatingElement> getHeatingElements() {
        return heatingElements;
    }

}
