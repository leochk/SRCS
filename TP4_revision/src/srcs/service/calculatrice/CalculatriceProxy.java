package srcs.service.calculatrice;

import srcs.service.ClientProxy;
import srcs.service.MyProtocolException;

public class CalculatriceProxy extends ClientProxy implements Calculatrice {
    public CalculatriceProxy(String machine, int port) {
        super(machine, port);
    }

    @Override
    public Integer add(Integer a, Integer b) {
        Integer res = 0;
        Object[] params = new Object[2];
        params[0] = a;
        params[1] = b;
        try {
            res = (Integer) super.invokeService("add", params);
        } catch (MyProtocolException e) {
            e.printStackTrace();
        }
        return res;
    }

    @Override
    public Integer sous(Integer a, Integer b) {
        Integer res = 0;
        Object[] params = new Object[2];
        params[0] = a;
        params[1] = b;
        try {
            res = (Integer) super.invokeService("sous", params);
        } catch (MyProtocolException e) {
            e.printStackTrace();
        }
        return res;
    }

    @Override
    public Integer mult(Integer a, Integer b) {
        Integer res = 0;
        Object[] params = new Object[2];
        params[0] = a;
        params[1] = b;
        try {
            res = (Integer) super.invokeService("mult", params);
        } catch (MyProtocolException e) {
            e.printStackTrace();
        }
        return res;
    }

    @Override
    public ResDiv div(Integer a, Integer b) {
        ResDiv res = null;
        Object[] params = new Object[2];
        params[0] = a;
        params[1] = b;
        try {
            res = (ResDiv) super.invokeService("div", params);
        } catch (MyProtocolException e) {
            e.printStackTrace();
        }
        return res;
    }
}
