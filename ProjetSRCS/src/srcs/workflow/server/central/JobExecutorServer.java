package srcs.workflow.server.central;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import srcs.workflow.executor.JobExecutor;
import srcs.workflow.graph.Graph;
import srcs.workflow.job.Context;
import srcs.workflow.job.Job;
import srcs.workflow.job.JobValidator;
import srcs.workflow.job.LinkFrom;
import srcs.workflow.job.Task;
import srcs.workflow.job.ValidationException;

public class JobExecutorServer implements JobExecutorServerRemote {

	private Map<Long, WorkflowToExec>	workflows	= new ConcurrentHashMap<>();
	private static long					cpt			= 0;

	public JobExecutorServer() throws RemoteException {

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public long newJobToExec(Job j) throws RemoteException {

		this.workflows.put(cpt, new WorkflowToExec(j));
		return cpt++;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Map<String, Object> execute(long id) throws RemoteException {

		try {
			return workflows.get(id).execute();
		} catch (RemoteException | InterruptedException e) {
			return new ConcurrentHashMap<>();
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getNbFinishedTasks(long id) throws RemoteException {

		return workflows.get(id).getNbFinishedTasks();
	}

	/**
	 * Intern class to represent a workflow. It uses a parallel execution
	 * strategy.
	 * 
	 * @author check-leo
	 *
	 */
	private class WorkflowToExec extends JobExecutor {
		private JobValidator		jv;
		private Graph<String>		graphTask;
		private Map<String, Object>	context;
		private Object				cptMutex		= new Object();
		private int					cptTaskFinished	= 0;

		public WorkflowToExec(Job j) {

			super(j);

			try {
				jv = new JobValidator(j);
				graphTask = jv.getTaskGraph();
				context = new ConcurrentHashMap<>(j.getContext());
			} catch (ValidationException e) {
				e.printStackTrace();
			}
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @throws InterruptedException
		 */
		@Override
		public Map<String, Object> execute()
				throws RemoteException, InterruptedException {

			Map<String, Object>	res		= new ConcurrentHashMap<>();
			List<Thread>		threads	= new ArrayList<>();

			synchronized (cptMutex) {
				cptTaskFinished = 0;
			}

			/* Recherche des tasks "finaux" (i.e, ceux qui n'ont pas de out) */
			for (String task : graphTask) {

				if (graphTask.getNeighborsOut(task).size() == 0) {
					Thread t = new Thread(
							() -> execMethod(jv.getMethod(task), res));
					threads.add(t);
					t.start();
				}
			}
			for (Thread t : threads)
				t.join();
			return res;
		}

		private Object execMethod(Method m, Map<String, Object> res) {

			Object			invokeRes;
			Object[]		args	= new Object[m.getParameterCount()];
			String			idTask	= m.getAnnotation(Task.class).value();
			int				i		= 0;
			List<Thread>	threads	= new ArrayList<>();

			try {
				/* Check if we've already computed inputted m */
				if ((invokeRes = res.get(idTask)) != null)
					return invokeRes;

				for (Parameter p : m.getParameters()) {
					String idparam;

					/* LinkFrom case */
					if (p.isAnnotationPresent(LinkFrom.class)) {
						idparam = p.getAnnotation(LinkFrom.class).value();
						final int	i_	= i;
						Method		m_	= jv.getMethod(idparam);
						Thread		t	= new Thread(() -> {
											args[i_] = execMethod(m_, res);
										});
						threads.add(t);
						t.start();
					}
					/* Context case */
					else {
						idparam = p.getAnnotation(Context.class).value();
						args[i] = context.get(idparam);
					}
					i++;
				}

				for (Thread t : threads) {
					t.join();
				}

				invokeRes = m.invoke(super.job, args);
				res.put(idTask, invokeRes);

				synchronized (cptMutex) {
					cptTaskFinished++;
				}

				return invokeRes;

			} catch (IllegalAccessException | IllegalArgumentException
					| InvocationTargetException | InterruptedException e) {
				e.printStackTrace();
			}
			return null;
		}

		public int getNbFinishedTasks() throws RemoteException {

			return cptTaskFinished;
		}

	}

}
