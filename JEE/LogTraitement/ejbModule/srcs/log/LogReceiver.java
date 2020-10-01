package srcs.log;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 * Session Bean implementation class LogReceiver
 */
@Stateless
@LocalBean
public class LogReceiver implements LogReceiverRemote {
	
	@PersistenceContext(unitName = "LogJPA")
	private EntityManager em;

    /**
     * Default constructor. 
     */
    public LogReceiver() {
        // TODO Auto-generated constructor stub
    }

	@Override
	public void newLog(Log l) {
		Machine m2 = em.find(Machine.class, l.getMachine().getId());
		if (m2 == null) {
			em.persist(l.getMachine());
			em.flush();
		}
		em.persist(l);
		em.flush();
	}

	@Override
	public void newLogAsync(Log l) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				newLog(l);
			}
		});
	}

	@Override
	public Machine[] getMachines() {
		Query q = em.createQuery("SELECT e FROM Machine e ");
		List<?> l = q.getResultList();
		Machine[] res = new Machine[l.size()];
		return l.toArray(res);
	}

	@Override
	public Log[] getLogs() {
		Query q = em.createQuery("SELECT e FROM Log e ");
		List<?> l = q.getResultList();
		Log[] res = new Log[l.size()];
		return l.toArray(res);
	}

	@Override
	@SuppressWarnings("unchecked")
	public Log[] getLogsWithLevel(String lvl) {
		Query q = em.createQuery("SELECT f FROM Log f WHERE f.level = :param_level");
		q.setParameter("param_level", lvl);
		
		List<Log> logs = (List<Log>) q.getResultList();
		
		Log[] res_ = new Log[logs.size()];
		return logs.toArray(res_);
	}

	@Override
	public Log[] getLogsWithMachine(String name_machine) {
		Machine tmp = em.find(Machine.class, name_machine);
		
		Query q = em.createQuery("SELECT f FROM Log f WHERE f.machine_id = :param_id");
		q.setParameter("param_id", tmp.getId());
		
		Log[] logs = getLogs();
		List<Log> res = new ArrayList<>();
		for (Log log : logs) 
			if (log.getMachine().equals(name_machine))
				res.add(log);
		Log[] res_ = new Log[res.size()];
		return res.toArray(res_);
	}

	@Override
	public void mr_proper() {
		System.out.println("jflkdsj");
		Log[] logs = getLogs();
		Machine[] machines = getMachines();
		for (Log l : logs) {
			em.remove(l);
			em.flush();
		}
		for (Machine m : machines) {
			em.remove(m);
			em.flush();
		}
		
	}

}