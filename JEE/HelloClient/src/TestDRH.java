
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.entreprise.Employe;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import beans.DRHRemote;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestDRH {

	private static Map<String, Double> employes = new HashMap<>();
	private Context context;
	private DRHRemote drh;

	private static String urlDRH = "java:global/HelloApplication/HelloEJB/DRH!beans.DRHRemote";

	@BeforeClass
	public static void beforeAllTests() {
		employes.put("Dupuis", 1250.0);
		employes.put("Durand", 1500.0);
		employes.put("Dupont", 5620.0);
		employes.put("Martin", 2890.0);
	}

	@Before
	public void beforeEachTest() throws NamingException {
		context = new InitialContext();
		drh = (DRHRemote) context.lookup(urlDRH);
	}

	@After
	public void afterEachTest() throws NamingException {
		drh = null;
		context.close();
	}

//vider la base de données des employes 
	@Test
	public void test1() {

		for (Employe e : drh.list_personnel()) {
			drh.tu_es_vire(e.getName());
		}
		assertEquals(0, drh.list_personnel().length);
	}

//recrutement de tous les employes 73
	@Test
	public void test2() {
		for (Entry<String, Double> e : employes.entrySet()) {
			assertTrue(drh.recrutement(e.getKey(), e.getValue()));
		}
	}

	@Test
//on vérifie que tous le monde est bien dans la base et a bien le bon salaire 

	public void test3() {
		Set<String> tmp = Arrays.stream(drh.list_personnel()).map(emp -> emp.getName()).collect(Collectors.toSet());
		assertEquals(employes.keySet(), tmp);

		for (Entry<String, Double> e : employes.entrySet()) {
			assertEquals(Double.valueOf(drh.getSalaire(e.getKey())), e.getValue());
		}

	}

//on vérifie que tous les employe ne peuvent pas être recrutés deux fois 92
	@Test
	public void test4() {
		for (Entry<String, Double> e : employes.entrySet()) {
			assertFalse(drh.recrutement(e.getKey(), e.getValue()));
		}
	}

//on fait une augmentation de salaire 
	@Test
	public void test5() {
		String nom = employes.keySet().iterator().next();
		double salaire_initial = employes.get(nom);
		double promotion = 500;
		double nouveau_salaire = salaire_initial + promotion;
		drh.promotion(nom, promotion);
		assertEquals(Double.valueOf(nouveau_salaire), Double.valueOf(drh.getSalaire(nom)));
	}

//on vire quelqu’un 
	@Test
	public void test6() {
		String nom = employes.keySet().iterator().next();
		drh.tu_es_vire(nom);
		assertFalse(Arrays.stream(drh.list_personnel()).anyMatch(e -> e.getName().equals(nom)));
	}
}