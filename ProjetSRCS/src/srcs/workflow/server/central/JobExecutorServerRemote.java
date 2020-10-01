package srcs.workflow.server.central;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Map;

import srcs.workflow.job.Job;

public interface JobExecutorServerRemote extends Remote {
	/**
	 * Declare a new workflow. The caller of this method must keep the return
	 * value which is the id of the newly created workflow, otherwise he would
	 * not be able to execute it in the future.
	 * 
	 * @param j job to execute
	 * @return id of workflow
	 * @throws RemoteException
	 */
	long newJobToExec(Job j) throws RemoteException;

	/**
	 * Execute a workflow identified by the inputed id
	 * 
	 * @param id
	 * @return Map containing result of each workflow's task
	 * @throws RemoteException
	 */
	Map<String, Object> execute(long id) throws RemoteException;

	/**
	 * Getting the number of completed task of a workflow identified by the
	 * inputed id
	 * 
	 * @param id
	 * @return number of completed task
	 * @throws RemoteException
	 */
	int getNbFinishedTasks(long id) throws RemoteException;
}
