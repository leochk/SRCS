package srcs.log.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;

import org.junit.Test;

import srcs.log.Log;
import srcs.log.util.LogReader;

public class TestLogReader {

	@Test
	public void test() throws IOException {
		
		BufferedReader in = new BufferedReader(new StringReader("2019-05-26 15:16:02,489 INFO prof.JulienSopena: Service indisponible"));
		
		try(LogReader logreader = new LogReader("Bureau.26.200.217", in)){
			Log l = logreader.nextLog();
			assertEquals("Service indisponible",l.getMessage());
			assertEquals("Bureau.26.200.217",l.getMachine().getNom());
			assertEquals("prof.JulienSopena",l.getNameClasse());
			assertEquals("INFO",l.getLevel());
			assertEquals("INFO",l.getLevel());
			assertEquals(2019,l.getDatelog().getAnnee());
			assertEquals(5,l.getDatelog().getMois());
			assertEquals(26,l.getDatelog().getJour());
			assertEquals(15,l.getDatelog().getHeure());
			assertEquals(16,l.getDatelog().getMinute());
			assertEquals(2,l.getDatelog().getSeconde());
			assertEquals(489,l.getDatelog().getMilliseconde());
			assertNull(logreader.nextLog());
		}
		
	}

}
