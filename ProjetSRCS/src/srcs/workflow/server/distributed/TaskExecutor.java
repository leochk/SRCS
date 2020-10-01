package srcs.workflow.server.distributed;

import java.lang.reflect.InvocationTargetException;
import java.rmi.RemoteException;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import srcs.workflow.job.Job;
import srcs.workflow.job.JobValidator;
import srcs.workflow.job.ValidationException;

public class TaskExecutor implements TaskExecutorRemote {
	private ExecutorService pool;

	public TaskExecutor(int sizePool) throws RemoteException {

		this.pool = Executors.newFixedThreadPool(sizePool);
	}

	@Override
	public Object execTask(Job j, String idTask, Object[] args)
			throws RemoteException {

		Future<Object> res = pool.submit(new TaskToExecute(j, idTask, args));
		try {
			return res.get();
		} catch (InterruptedException | ExecutionException e) {
			return null;
		}
	}

	class TaskToExecute implements Callable<Object> {
		Job			j;
		String		idTask;
		Object[]	args;

		public TaskToExecute(Job j, String idTask, Object[] args) {

			this.j = j;
			this.idTask = idTask;
			this.args = args;
		}

		@Override
		public Object call() throws Exception {

			try {
				JobValidator jv = new JobValidator(j);
				return jv.getMethod(idTask).invoke(j, args);
			} catch (ValidationException | IllegalAccessException
					| IllegalArgumentException | InvocationTargetException e) {
				return null;
			}
		}
	}
}
