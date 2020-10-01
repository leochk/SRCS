package srcs.workflow.executor;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.Map;

import srcs.workflow.graph.Graph;
import srcs.workflow.job.Context;
import srcs.workflow.job.Job;
import srcs.workflow.job.JobValidator;
import srcs.workflow.job.LinkFrom;
import srcs.workflow.job.Task;
import srcs.workflow.job.ValidationException;

/**
 * Job executor with a sequential execution strategy
 * 
 * @author check-leo
 *
 */
public class JobExecutorSequential extends JobExecutor {

	JobValidator		jv;
	Graph<String>		graphTask;
	Map<String, Object>	context;

	public JobExecutorSequential(Job job) {

		super(job);

		try {
			jv = new JobValidator(job);
			graphTask = jv.getTaskGraph();
			context = job.getContext();
		} catch (ValidationException e) {
			e.printStackTrace();
		}
	}

	@Override
	public Map<String, Object> execute() throws Exception {

		Map<String, Object> res = new HashMap<>();

		/* Recherche des tasks "finaux" (i.e, ceux qui n'ont pas de out) */
		for (String task : graphTask) {

			if (graphTask.getNeighborsOut(task).size() == 0) {
				execMethod(jv.getMethod(task), res);
			}
		}
		return res;
	}

	private Object execMethod(Method m, Map<String, Object> res) {

		Object		invokeRes;
		Object[]	args	= new Object[m.getParameterCount()];
		String		idTask	= m.getAnnotation(Task.class).value();
		int			i		= 0;

		/* Check if we've already computed inputted m */
		if ((invokeRes = res.get(idTask)) != null)
			return invokeRes;

		for (Parameter p : m.getParameters()) {
			String idparam;

			/* LinkFrom case */
			if (p.isAnnotationPresent(LinkFrom.class)) {
				idparam = p.getAnnotation(LinkFrom.class).value();
				args[i] = execMethod(jv.getMethod(idparam), res);
			}
			/* Context case */
			else {
				idparam = p.getAnnotation(Context.class).value();
				args[i] = context.get(idparam);
			}
			i++;
		}

		try {
			invokeRes = m.invoke(job, args);
			res.put(idTask, invokeRes);
			return invokeRes;
		} catch (IllegalAccessException | IllegalArgumentException
				| InvocationTargetException e) {
			e.printStackTrace();
		}
		return null;
	}

}
