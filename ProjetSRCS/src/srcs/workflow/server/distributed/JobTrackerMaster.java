package srcs.workflow.server.distributed;

import java.io.IOException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class JobTrackerMaster {
	private static Registry					registry	= null;
	private static JobExecutorMasterRemote	j;

	public static void main(String args[])
			throws IOException, InterruptedException {

		new ProcessBuilder().command("killall", "-q", "rmiregistry").start();
		Thread.sleep(500);

		registry = LocateRegistry.createRegistry(1099);

		j = new JobExecutorMaster();
		UnicastRemoteObject.exportObject(j, 0);
		registry.rebind("serverExecutorMaster", j);
	}
}
