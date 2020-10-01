package srcs.workflow.server.central;

import java.io.IOException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class JobTrackerCentral {
	static Registry registry = null;

	public static void main(String args[])
			throws IOException, InterruptedException {

		new ProcessBuilder().command("killall", "-q", "rmiregistry").start();
		Thread.sleep(500);

		registry = LocateRegistry.createRegistry(1099);

		JobExecutorServer j = new JobExecutorServer();
		UnicastRemoteObject.exportObject(j, 0);
		registry.rebind("serverExecutorCentral", j);
	}
}
