package srcs.workflow.server.distributed;

import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Map;

import srcs.workflow.job.Job;

public interface JobExecutorMasterRemote extends Remote {
	/**
	 * Register a new RMI TaskExecutorRemote, identified by its name.
	 * 
	 * @param name of the new RMI TaskExecutorRemote
	 * @throws RemoteException
	 */
	void registerTaskTracker(String name) throws RemoteException;
	
	/**
	 * Declare a new workflow. The caller of this method must keep the return
	 * value which is the id of the newly created workflow, otherwise he would
	 * not be able to execute it in the future.
	 * 
	 * @param j job to execute
	 * @return id of workflow
	 * @throws RemoteException
	 */
	long addJob(Job j) throws RemoteException;

	/**
	 * Execute a workflow identified by the inputed id
	 * 
	 * @param id
	 * @return Map containing result of each workflow's task
	 * @throws RemoteException
	 */
	Map<String, Object> execute(long id)
			throws RemoteException, NotBoundException;
}
