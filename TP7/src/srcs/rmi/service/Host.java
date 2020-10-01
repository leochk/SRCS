package srcs.rmi.service;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Set;

public interface Host extends Remote {
    <P extends Serializable, R extends Serializable> FunctionService<P, R>
        deployNewService(String name, Class<? extends FunctionService<P, R>> service) throws RemoteException;

    <P extends Serializable, R extends Serializable> FunctionService<P, R>
        deployExistingService(FunctionService<P, R> service) throws RemoteException;

    boolean undeployService(String name) throws RemoteException;
    List<String> getServices() throws RemoteException;
}
