package srcs.workflow.server.distributed;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import srcs.workflow.graph.Graph;
import srcs.workflow.job.Context;
import srcs.workflow.job.Job;
import srcs.workflow.job.JobValidator;
import srcs.workflow.job.LinkFrom;
import srcs.workflow.job.Task;
import srcs.workflow.job.ValidationException;

public class JobExecutorMaster implements JobExecutorMasterRemote {
	private static long				cpt			= 0;
	private static long				timeOut		= 5000;

	private Map<Long, Job>			workflows	= new ConcurrentHashMap<>();
	private BlockingQueue<String>	trackers	= new LinkedBlockingQueue<>();
	private Registry				registry	= LocateRegistry
			.getRegistry("localhost");
	private Object					mutexCpt	= new Object();

	public JobExecutorMaster() throws RemoteException {

	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void registerTaskTracker(String name) throws RemoteException {

		try {
			trackers.put(name);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	synchronized public long addJob(Job j) {

		this.workflows.put(cpt, j);

		synchronized (mutexCpt) {
			return cpt++;
		}

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Map<String, Object> execute(long id)
			throws RemoteException, NotBoundException {

		Map<String, Object>	res		= new ConcurrentHashMap<>();
		List<Thread>		threads	= new ArrayList<Thread>();

		try {
			Job					j			= workflows.get(id);
			JobValidator		jv			= new JobValidator(j);
			Graph<String>		graphTask	= jv.getTaskGraph();
			Map<String, Object>	context		= j.getContext();

			/* Recherche des tasks "finaux" (i.e, ceux qui n'ont pas de out) */
			for (String task : graphTask) {

				if (graphTask.getNeighborsOut(task).size() == 0) {
					Thread t = new Thread(() -> execMethod(jv.getMethod(task),
							jv, context, res));
					threads.add(t);
					t.start();
				}
			}
			for (Thread t : threads)
				t.join();

		} catch (ValidationException | InterruptedException e) {
			e.printStackTrace();
		}
		return res;

	}

	private Object execMethod(Method m, JobValidator jv,
			Map<String, Object> context, Map<String, Object> res) {

		Object			invokeRes;
		Object[]		args	= new Object[m.getParameterCount()];
		String			idTask	= m.getAnnotation(Task.class).value();
		List<Thread>	threads	= new ArrayList<>();
		int				i		= 0;

		try {
			/* Check if we've already computed inputted m */
			if ((invokeRes = res.get(idTask)) != null)
				return invokeRes;

			for (Parameter p : m.getParameters()) {
				String idparam;

				/* LinkFrom case */
				if (p.isAnnotationPresent(LinkFrom.class)) {
					idparam = p.getAnnotation(LinkFrom.class).value();
					Method		m_	= jv.getMethod(idparam);
					final int	i_	= i;
					Thread		t	= new Thread(() -> {
										args[i_] = execMethod(m_, jv, context,
												res);
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

				for (Thread t : threads)
					t.join();

			}

			while (true) {
				String				tracker		= trackers.take();
				TaskExecutorRemote	exec		= (TaskExecutorRemote) registry
						.lookup(tracker);

				ExecutorService		executor	= Executors
						.newSingleThreadExecutor();
				Future<Object>		future		= executor
						.submit(new Callable<Object>() {

							@Override
							public Object call() throws Exception {
								try {
									return exec.execTask(jv.getJob(), idTask, args);
								} catch (RemoteException e) {
									throw e;
								}
							}
						});

				try {
					invokeRes = future.get(timeOut, TimeUnit.MILLISECONDS);
				} catch (TimeoutException | ExecutionException e) {
					continue;
				}

				res.put(idTask, invokeRes);
				trackers.put(tracker);
				return invokeRes;
			}

		} catch (InterruptedException | RemoteException | NotBoundException e) {
			return null;
		}

	}

}
