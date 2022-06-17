package ru.firstvalery.boiler.modbus.master;


import com.serotonin.modbus4j.ModbusFactory;
import com.serotonin.modbus4j.ModbusMaster;
import com.serotonin.modbus4j.exception.ModbusInitException;
import com.serotonin.modbus4j.exception.ModbusTransportException;
import com.serotonin.modbus4j.msg.ReadHoldingRegistersRequest;
import com.serotonin.modbus4j.msg.ReadHoldingRegistersResponse;
import com.serotonin.modbus4j.msg.WriteRegistersRequest;
import com.serotonin.modbus4j.msg.WriteRegistersResponse;
import org.springframework.stereotype.Service;
import ru.firstvalery.boiler.config.SerialPortConfig;

import javax.annotation.PreDestroy;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


@Service
public class DataExchangeServiceImpl implements DataExchangeService {
    private final SerialPortConfig serialCfg;
    private static final int READ_REGISTERS_COUNT = 16;
    private static final int WRITE_REGISTERS_COUNT = 16;
    private final short[] readArray = new short[READ_REGISTERS_COUNT];
    private final short[] writeArray = new short[WRITE_REGISTERS_COUNT];
    private static final int SLAVE_ID = 1;
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    public DataExchangeServiceImpl(SerialPortConfig applicationConfig) {
        this.serialCfg = applicationConfig;
    }

    public void init() {
        executorService.execute(this::task);
    }

    private void task() {
        ModbusFactory factory = new ModbusFactory();
        SerialPortSimpleWrapper serialPortSimpleWrapper = new SerialPortSimpleWrapper(serialCfg.getPortName(), serialCfg.getBaudRate(),
                serialCfg.getFlowControlIn(), serialCfg.getFlowControlOut(),
                serialCfg.getDataBits(), serialCfg.getStopBits(), serialCfg.getParity());

        ModbusMaster master = factory.createRtuMaster(serialPortSimpleWrapper);
        master.setTimeout(2000);
        master.setDiscardDataDelay(100);
        master.setRetries(0);
        try {
            master.init();
            while (true) {
                readHoldingRegisters(master, SLAVE_ID, 0, READ_REGISTERS_COUNT);
                writeRegisters(master, SLAVE_ID, READ_REGISTERS_COUNT);
            }
        } catch (ModbusInitException e) {
            System.out.println("Modbus Master Init Error: " + e.getMessage());
        }
    }

    private void readHoldingRegisters(ModbusMaster master, int slaveId, int start, int len) {
        try {
            ReadHoldingRegistersRequest request = new ReadHoldingRegistersRequest(slaveId, start, len);
            ReadHoldingRegistersResponse response = (ReadHoldingRegistersResponse) master.send(request);
            if (response.isException())
                System.out.println("Exception response: message=" + response.getExceptionMessage());
            else {
                synchronized (readArray) {
                    short[] data = response.getShortData();
                    System.arraycopy(data, 0, readArray, 0, data.length);
                }
            }
        } catch (ModbusTransportException e) {
            e.printStackTrace();
        }
    }

    private void writeRegisters(ModbusMaster master, int slaveId, int start) {
        try {
            synchronized (writeArray) {
                WriteRegistersRequest request = new WriteRegistersRequest(slaveId, start, writeArray);
                WriteRegistersResponse response = (WriteRegistersResponse) master.send(request);
                if (response.isException()) {
                    System.out.println("Exception response: message=" + response.getExceptionMessage());
                }
                Arrays.fill(writeArray, (short) 0);
            }
        } catch (ModbusTransportException e) {
            e.printStackTrace();
        }
    }


    public short[] getReadArray() {
        synchronized (readArray) {
            return Arrays.copyOf(readArray, readArray.length);
        }
    }

    public void setWriteArray(short[] values) {
        synchronized (writeArray) {
            System.arraycopy(values, 0, this.writeArray, 0, values.length);
        }
    }

    @PreDestroy
    void shutdown() {
        executorService.shutdown();
    }
}
