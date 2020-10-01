package srcs.rmi.service.test;

import org.junit.After;
import org.junit.Before;
import srcs.rmi.service.Host;
import srcs.rmi.service.HostImplem;

import java.io.IOException;
import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.activation.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class SystemDeployer {
    Registry registry = null;

    @Before
    public void launch() throws IOException, InterruptedException {
        new ProcessBuilder().command("killall", "-q", "rmiregistry").start();
        // sleep pour etre sur que le killall a été executé
        Thread.sleep(1000);

        registry = LocateRegistry.createRegistry(1099);
        Host h = new HostImplem();
        Host h2 = new HostImplem();
        UnicastRemoteObject.exportObject(h, 0);
        UnicastRemoteObject.exportObject(h2, 0);
        registry.rebind("host1", h);
        registry.rebind("host2", h2);


    }

    @After
    public void after() throws IOException, NotBoundException {
        registry.unbind("host1");
        registry.unbind("host2");

        UnicastRemoteObject.unexportObject(registry, true);
    }
}
