package SupportVectorMachine.Model;

import java.io.Serializable;

/**
 * User: AndreasRydingLund
 * Date: 20-04-2015
 */
public class AltitudeBoundPair implements Serializable {
    private short _min;
    private short _max;

    public AltitudeBoundPair() {
        _min = 0;
        _max = 0;
    }

    public AltitudeBoundPair(short max, short min) {
        _max = max;
        _min = min;
    }

    public short getMin() {
        return _min;
    }

    public void setMin(short _min) {
        this._min = _min;
    }

    public short getMax() {
        return _max;
    }

    public void setMax(short _max) {
        this._max = _max;
    }
}
