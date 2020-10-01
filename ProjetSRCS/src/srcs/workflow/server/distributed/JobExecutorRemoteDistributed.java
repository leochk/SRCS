package srcs.workflow.server.distributed;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Map;

import srcs.workflow.executor.JobExecutor;
import srcs.workflow.job.Job;

public class JobExecutorRemoteDistributed extends JobExecutor {

	private JobExecutorMasterRemote	j;
	private Registry				registry	= LocateRegistry
			.getRegistry("localhost");
	private long					id;

	public JobExecutorRemoteDistributed(Job job)
			throws RemoteException, NotBoundException {

		super(job);
		this.j = (JobExecutorMasterRemote) registry
				.lookup("serverExecutorMaster");
		this.id = j.addJob(job);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Map<String, Object> execute() throws Exception {

		return j.execute(id);
	}

}
