package srcs.log.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import srcs.log.Log;
import srcs.log.LogReceiverRemote;
import srcs.log.Machine;
import srcs.log.util.LogReader;


@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestLogGenerator {

	private static List<String> names_machine= new ArrayList<>();
	private static String urlLogReceiver = "java:global/LogApplication/LogTraitement/LogReceiver!srcs.log.LogReceiverRemote";
	private Context context;
	private LogReceiverRemote logreceiver ;
	
	@BeforeClass
	public static void beforeAllTests() {
		names_machine.add("datanode");
		names_machine.add("namenode");
		names_machine.add("nodemanager");
		names_machine.add("resourcemanager");
		names_machine.add("secondarynamenode");
	}
	
	@Before
	public  void beforeEachTest() throws NamingException {
		context = new InitialContext();
		logreceiver = (LogReceiverRemote)context.lookup(urlLogReceiver);
	}
	
	@After
	public void afterEachTest() throws NamingException {
		logreceiver=null;
		context.close();
	}
	
	@Test
	public void test1() throws FileNotFoundException, IOException {
		logreceiver.mr_proper();
		Log[] logs = logreceiver.getLogs();
		Machine[] machines = logreceiver.getMachines();
		assertEquals(0, logs.length);
		assertEquals(0, machines.length);
		
		
	}
	
	
	
	
	@Test
	public void test2() throws FileNotFoundException, IOException {
		Map<String,Integer> cpt_level = new HashMap<String, Integer>();
		Map<String,Integer> cpt_machine = new HashMap<String, Integer>();
		int cpt=0;
		for(String name : names_machine) {
			String file = name+".log";
			System.err.println("Processing "+file);
			try(LogReader reader = new LogReader(name, new BufferedReader(new FileReader(file)))){
				Log l ;
				while((l=reader.nextLog()) != null) {			
					logreceiver.newLog(l);
					String level = l.getLevel();
					cpt_level.put(level, cpt_level.getOrDefault(level, 0)+1);
					cpt_machine.put(name, cpt_machine.getOrDefault(name, 0)+1);
					cpt++;
				}				
			}
		}
				
		assertEquals(cpt,logreceiver.getLogs().length);
		assertEquals(names_machine.size(),logreceiver.getMachines().length);
		for(Entry<String,Integer> e : cpt_level.entrySet()) {
			assertEquals(e.getValue().intValue(),logreceiver.getLogsWithLevel(e.getKey()).length);
		}
		for(Entry<String,Integer> e : cpt_machine.entrySet()) {
			assertEquals(e.getValue().intValue(),logreceiver.getLogsWithMachine(e.getKey()).length);
		}
		
	}
	
	
	
	@Test
	public void test3() throws FileNotFoundException, IOException {
		
		logreceiver.mr_proper();
		long start,stop;
		start = System.currentTimeMillis();
		for(String name : names_machine) {
			String file = name+".log";
			try(LogReader reader = new LogReader(name, new BufferedReader(new FileReader(file)))){
				Log l ;
				while((l=reader.nextLog()) != null) {
					logreceiver.newLog(l);				
				}				
			}
		}
		stop = System.currentTimeMillis();
		long time=stop-start;
		
		
		logreceiver.mr_proper();
		start = System.currentTimeMillis();
		for(String name : names_machine) {
			String file = name+".log";
			try(LogReader reader = new LogReader(name, new BufferedReader(new FileReader(file)))){
				Log l ;
				while((l=reader.nextLog()) != null) {
					logreceiver.newLogAsync(l);				
				}				
			}
		}
		stop = System.currentTimeMillis();
		long time_async=stop-start;
		System.err.println("time = "+time);
		System.err.println("time_async = "+time_async);
		assertTrue(time_async<time);//normalement les tests unitaires ne servent que pour vérifier les spec fonctionnelles
		
		
	}
	
}
