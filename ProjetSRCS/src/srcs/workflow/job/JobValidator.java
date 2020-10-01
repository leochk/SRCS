package srcs.workflow.job;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.Map;

import srcs.workflow.graph.Graph;
import srcs.workflow.graph.GraphImpl;

/**
 * Util class to check the validity of a job
 * 
 * @author check-leo
 *
 */
public class JobValidator {

	/* Return type of Job's tasks */
	private Map<String, Class<?>>	returnType	= new HashMap<>();
	/* Task-Annotated methods of Job */
	private Map<String, Method>		methodsMap	= new HashMap<>();
	private Graph<String>			graph		= new GraphImpl<>();
	private Job						job;

	public JobValidator(Job job) throws ValidationException {

		Map<String, Object>	context	= job.context;

		/* Testing methods */
		boolean				found	= false;

		for (Method m : job.getClass().getMethods()) {

			if (m.isAnnotationPresent(Task.class)) {
				String id = m.getAnnotation(Task.class).value();
				/* we encoutered a Task */
				found = true;

				/* Check return type non-void */
				if (m.getReturnType() == Void.TYPE)
					throw new ValidationException(
							"Method " + m.getName() + " returns void!");
				/* Check modifier not static */
				if (Modifier.isStatic(m.getModifiers()))
					throw new ValidationException(
							"Method " + m.getName() + " is static!");
				/* Check duplicate */
				if (graph.existNode(id))
					throw new ValidationException(
							"2 methods are annotated Task with value of " + id);

				graph.addNode(id);
				returnType.put(id, m.getReturnType());
				methodsMap.put(id, m);
			}
		}

		if (!found)
			throw new ValidationException("There is no method annotated Task");

		/* Testing parameters of methods */
		for (Method m : job.getClass().getMethods()) {

			if (m.isAnnotationPresent(Task.class)) {
				String id = m.getAnnotation(Task.class).value();

				for (Parameter p : m.getParameters()) {
					String idparam;

					/* LinkFrom case */
					if (p.isAnnotationPresent(LinkFrom.class)) {

						idparam = p.getAnnotation(LinkFrom.class).value();

						/*
						 * Check if we have encountered Task idparam while
						 * testing methods
						 */
						if (returnType.get(idparam) == null)
							throw new ValidationException(
									"No " + idparam + " Task declared");
						/*
						 * Check return type between current method and Task
						 * idparam
						 */
						if (!p.getType()
								.isAssignableFrom(returnType.get(idparam)))
							throw new ValidationException("Type of param "
									+ p.getName() + " of method " + m.getName()
									+ " do not match with Task " + idparam);

						graph.addEdge(idparam, id);

					}
					/* Context case */
					else if (p.isAnnotationPresent(Context.class)) {
						idparam = p.getAnnotation(Context.class).value();

						Object o = context.get(idparam);
						/* Check if context exists */
						if (o == null)
							throw new ValidationException("Not such object "
									+ idparam + " in context");
						/* Check type between context and current parameter */
						if (!o.getClass().isAssignableFrom(p.getType()))
							throw new ValidationException("Type of param "
									+ p.getName() + " of method " + m.getName()
									+ " do not match with context " + idparam);

					}

					else
						throw new ValidationException("Parameter " + p.getName()
								+ " of method " + m.getName()
								+ "is not correctly annotated!");
				}
			}
		}

		if (!graph.isDAG())
			throw new ValidationException("The builded graph should be DAG!");

		this.job = job;
	}

	public Graph<String> getTaskGraph() {

		return graph;
	}

	public Method getMethod(String id) {

		Method m = methodsMap.get(id);
		if (m == null)
			throw new IllegalArgumentException(
					"Task " + id + " does not exist");
		return m;

	}

	public Job getJob() {

		return job;
	}
}
