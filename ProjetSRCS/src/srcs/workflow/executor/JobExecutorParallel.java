package srcs.workflow.executor;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import srcs.workflow.graph.Graph;
import srcs.workflow.job.Context;
import srcs.workflow.job.Job;
import srcs.workflow.job.JobValidator;
import srcs.workflow.job.LinkFrom;
import srcs.workflow.job.Task;
import srcs.workflow.job.ValidationException;

/**
 * Job executor with a parallel execution strategy
 * 
 * @author check-leo
 *
 */
public class JobExecutorParallel extends JobExecutor {

	JobValidator		jv;
	Graph<String>		graphTask;
	Map<String, Object>	context;

	public JobExecutorParallel(Job job) {

		super(job);

		try {
			jv = new JobValidator(job);
			graphTask = jv.getTaskGraph();
			context = new ConcurrentHashMap<>(job.getContext());
		} catch (ValidationException e) {
			e.printStackTrace();
		}
	}

	@Override
	public Map<String, Object> execute() throws Exception {

		Map<String, Object>	res		= new ConcurrentHashMap<>();
		List<Thread>		threads	= new ArrayList<>();

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

			for (Thread t : threads)
				t.join();

			invokeRes = m.invoke(job, args);
			res.put(idTask, invokeRes);
			return invokeRes;

		} catch (IllegalAccessException | IllegalArgumentException
				| InvocationTargetException | InterruptedException e) {
			e.printStackTrace();
		}
		return null;
	}

}
