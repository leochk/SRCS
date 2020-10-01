package srcs.workflow.server.distributed;

import java.rmi.Remote;
import java.rmi.RemoteException;

import srcs.workflow.job.Job;

public interface TaskExecutorRemote extends Remote {
	/**
	 * Execute a task of the inputed Job, identified by the inputed idTask, with
	 * inputed arguments args.
	 * 
	 * @param j the job
	 * @param idTask the id of the task
	 * @param args arguments of task
	 * @return result of the task
	 * @throws RemoteException
	 */
	Object execTask(Job j, String idTask, Object[] args) throws RemoteException;
}
