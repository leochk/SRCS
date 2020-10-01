package srcs.workflow.server.central;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import srcs.workflow.executor.JobExecutor;
import srcs.workflow.job.Job;

public class JobExecutorRemoteCentral extends JobExecutor {

	private JobExecutorServerRemote	j;
	private Registry				registry	= LocateRegistry
			.getRegistry("localhost");
	private long					id;

	public JobExecutorRemoteCentral(Job job)
			throws RemoteException, NotBoundException {

		super(job);
		this.j = (JobExecutorServerRemote) registry
				.lookup("serverExecutorCentral");
		this.id = j.newJobToExec(job);
	}

	@Override
	public Map<String, Object> execute() throws Exception {

		Map<String, Object>	res	= new ConcurrentHashMap<>();
		Thread				t;

		t = new Thread(() -> {

			try {
				Map<String, Object> r = j.execute(id);
				for (String k : r.keySet())
					res.put(k, r.get(k));
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		});
		t.start();

		while (t.isAlive())
			System.out.println(j.getNbFinishedTasks(id));

		return res;

	}

}
