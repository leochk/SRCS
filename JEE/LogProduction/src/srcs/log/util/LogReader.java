package srcs.log.util;

import java.io.BufferedReader;
import java.io.IOException;

import srcs.log.DateLog;
import srcs.log.Log;
import srcs.log.Machine;

public class LogReader implements AutoCloseable {

	private String machine;
	private BufferedReader br;
	
	public LogReader(String machine, BufferedReader br) {
		this.machine = machine;
		this.br = br;
	}
	
	public Log nextLog() {
		String line;
		try {
			line = br.readLine();
			if (line == null) return null;

			DateLog date = new DateLog();
			date.setAnnee(Parser.getYear(line));
			date.setMois(Parser.getMonth(line));
			date.setJour(Parser.getDay(line));
			date.setHeure(Parser.getHour(line));
			date.setMinute(Parser.getMinute(line));
			date.setSeconde(Parser.getSecond(line));
			date.setMilliseconde(Parser.getMilliSecond(line));
			
			Log res = new Log();
			res.setDatelog(date);
			res.setLevel(Parser.getLevel(line));
			Machine m = new Machine();
			m.setNom(machine);
			res.setMachine(m);
			res.setMessage(Parser.getMessage(line));
			res.setNameClasse(Parser.getNameClasse(line));
				
			return res;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@Override
	public void close() {
		try {
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}