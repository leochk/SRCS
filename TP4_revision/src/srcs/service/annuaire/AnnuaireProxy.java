package srcs.service.annuaire;

import srcs.service.ClientProxy;
import srcs.service.MyProtocolException;

public class AnnuaireProxy extends ClientProxy implements Annuaire{
    public AnnuaireProxy(String machine, int port) {
        super(machine, port);
    }

    @Override
    public String lookup(String nom) {
        String res = "";
        Object[] params = new Object[1];
        params[0] = nom;
        try {
            res = (String) super.invokeService("lookup", params);
        } catch (MyProtocolException e) {
            e.printStackTrace();
        }
        return res;
    }

    @Override
    public void bind(String nom, String val) {
        Object[] params = new Object[2];
        params[0] = nom;
        params[1] = val;
        try {
            super.invokeService("bind", params);
        } catch (MyProtocolException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void unbind(String nom) {
        Object[] params = new Object[1];
        params[0] = nom;
        try {
            super.invokeService("unbind", params);
        } catch (MyProtocolException e) {
            e.printStackTrace();
        }
    }
}
