package srcs.log.util;

import java.io.BufferedReader;

import srcs.log.Log;

public class LogReader implements AutoCloseable {

	private String machine;
	private BufferedReader br;
	
	public LogReader(String machine, BufferedReader br) {
		this.machine = machine;
		this.br = br;
	}
	
	public Log nextLog() {
		return null;
	}
	
	@Override
	public void close() throws Exception {
		br.close();
	}

}
