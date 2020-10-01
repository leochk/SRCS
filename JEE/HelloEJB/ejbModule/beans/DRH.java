package beans;

import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.entreprise.Employe;

/**
 * Session Bean implementation class DRH
 */
@Stateless
@LocalBean
public class DRH implements DRHRemote {

	/**
	 * Default constructor.
	 */
	public DRH() {
		// TODO Auto-generated constructor stub
	}

	@PersistenceContext(unitName = "HelloJPA")
	private EntityManager em;

	@Override
	public boolean recrutement(String nom, double salaire) {
		Employe emp = em.find(Employe.class, nom);
		if (emp != null) {
			return false;
		}
		emp = new Employe();
		emp.setName(nom);
		emp.setSalary(salaire);
		em.persist(emp);
		em.flush();
		return true;
	}

	@Override
	public double getSalaire(String nom) {
		Employe emp = em.find(Employe.class, nom);
		if (emp != null) {
			return emp.getSalary();
		}
		return -1.0f;
	}

	@Override
	public void promotion(String nom, double v) {
		Employe emp = em.find(Employe.class, nom);
		if (emp != null) {
			emp.setSalary(emp.getSalary() + v);
			em.flush();
		}

	}

	@Override
	public Employe[] list_personnel() {
		Query q = em.createQuery("SELECT e FROM Employe e ");
		List<?> l = q.getResultList();
		Employe[] res = new Employe[l.size()];
		return l.toArray(res);
	}

	@Override
	public void tu_es_vire(String nom) {
		Employe emp = em.find(Employe.class, nom);
		if (emp != null) {
			em.remove(emp);
			em.flush();
		}
	}

}
