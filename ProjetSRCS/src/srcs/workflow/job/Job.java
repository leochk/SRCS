package srcs.workflow.job;

import java.io.Serializable;
import java.util.Map;

public abstract class Job implements Serializable {
	/**
	 * 
	 */
	private static final long			serialVersionUID	= 1L;

	protected final String				name;
	protected final Map<String, Object>	context;

	public Job(String name, Map<String, Object> context) {

		super();
		this.name = name;
		this.context = context;
	}

	public String getName() {

		return name;
	}

	public Map<String, Object> getContext() {

		return context;
	}

}
