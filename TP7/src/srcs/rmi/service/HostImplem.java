package srcs.rmi.service;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;
import java.util.stream.Collectors;

public class HostImplem implements Host {

    Map<String, FunctionService<? extends Serializable, ? extends Serializable>> hm = new HashMap<>();

    @Override
    public <P extends Serializable, R extends Serializable> FunctionService<P, R> deployNewService(String name, Class<? extends FunctionService<P, R>> service) throws RemoteException {
        FunctionService<P, R> s;
        try {
            if (this.hm.containsKey(name)) throw new RemoteException("Service existant");

            s = service.getConstructor(String.class).newInstance(name);
            this.hm.put(name, s);
            UnicastRemoteObject.exportObject(s, 0);
            return s;

        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public <P extends Serializable, R extends Serializable> FunctionService<P, R> deployExistingService(FunctionService<P, R> service) throws RemoteException {
        UnicastRemoteObject.exportObject(service, 0);
        this.hm.put(service.getName(), service);
        return service;
    }

    @Override
    public boolean undeployService(String name) throws RemoteException {
        if (!this.hm.containsKey(name)) return false;
        UnicastRemoteObject.unexportObject(hm.get(name), true);
        return this.hm.remove(name) != null;
    }

    @Override
    public List<String> getServices() throws RemoteException {
        return new ArrayList<>(hm.keySet());
    }
}
