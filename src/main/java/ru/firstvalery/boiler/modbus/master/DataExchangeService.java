package ru.firstvalery.boiler.modbus.master;

import ru.firstvalery.boiler.init.Initializable;

public interface DataExchangeService extends Initializable {

    short[] getReadArray();

    void setWriteArray(short[] values);
}
