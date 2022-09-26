package ru.firstvalery.boiler.modbus.master;

import com.serotonin.modbus4j.serial.SerialPortWrapper;
import jssc.SerialPort;
import jssc.SerialPortException;
import lombok.Getter;
import ru.firstvalery.boiler.config.SerialPortConfig;

import java.io.InputStream;
import java.io.OutputStream;

public class SerialPortSimpleWrapper implements SerialPortWrapper {

    private final SerialPort port;
    @Getter
    final private int baudRate;
    @Getter
    final private int flowControlIn;
    @Getter
    final private int flowControlOut;
    @Getter
    final private int dataBits;
    @Getter
    final private int stopBits;
    @Getter
    final private int parity;

    public SerialPortSimpleWrapper(SerialPortConfig serialConfig) {
        this.baudRate = serialConfig.getBaudRate();
        this.flowControlIn = serialConfig.getFlowControlIn();
        this.flowControlOut = serialConfig.getFlowControlOut();
        this.dataBits = serialConfig.getDataBits();
        this.stopBits = serialConfig.getStopBits();
        this.parity = serialConfig.getParity();
        port = new SerialPort(serialConfig.getPortName());
    }

    @Override
    public void close() throws Exception {
        port.closePort();
    }

    @Override
    public void open() throws Exception {
        try {
            port.openPort();
            port.setParams(this.getBaudRate(), this.getDataBits(), this.getStopBits(), this.getParity());
            port.setFlowControlMode(this.getFlowControlIn() | this.getFlowControlOut());
        } catch (SerialPortException ignore) {
        }
    }

    @Override
    public InputStream getInputStream() {
        return new SerialInputStream(port);
    }

    @Override
    public OutputStream getOutputStream() {
        return new SerialOutputStream(port);
    }
}
