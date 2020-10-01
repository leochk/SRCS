package srcs.workflow.server.distributed;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class TaskTracker {
	static Registry				registry	= null;
	static TaskExecutorRemote	j;

	public static void main(String args[])
			throws IOException, InterruptedException, NotBoundException {

		if (args.length != 2)
			return;

		j = new TaskExecutor(Integer.parseInt(args[1]));
		registry = LocateRegistry.getRegistry(1099);
		UnicastRemoteObject.exportObject(j, 0);
		registry.rebind(args[0], j);

		JobExecutorMasterRemote master = (JobExecutorMasterRemote) registry
				.lookup("serverExecutorMaster");
		master.registerTaskTracker(args[0]);

	}

}
