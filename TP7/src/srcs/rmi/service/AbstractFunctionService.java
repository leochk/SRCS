package srcs.rmi.service;

import java.io.*;
import java.rmi.RemoteException;

import static srcs.rmi.service.util.deepCopy;


public abstract class AbstractFunctionService<P extends Serializable, R extends Serializable>
        implements FunctionService<P, R> {

    protected String name;
    private FunctionService<P, R> deployed = null;

    public AbstractFunctionService(String name) {
        this.name = name;
    }

    @Override
    public String getName() throws RemoteException {
        if (deployed != null) return deployed.getName();
        return name;
    }

    @Override
    public R invoke(P arg) throws RemoteException {
        //System.out.println(this);

        if (deployed != null) return deployed.invoke(arg);
        //System.out.println("eho" + this);

        return perform(arg);
    }

    @Override
    public FunctionService<P, R> migrateTo(Host host) throws RemoteException {
        if (deployed != null) throw new RemoteException("Service déjà migré");


        if (host.getServices().contains(this.name)) {
            throw new RemoteException("Service déjà existant");
        }
        try {
            AbstractFunctionService s = (AbstractFunctionService) deepCopy(this);
            deployed = host.deployExistingService(s);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return deployed;
    }

    protected abstract R perform(P param);


}
