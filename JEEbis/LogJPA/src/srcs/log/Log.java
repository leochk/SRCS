package srcs.log;

import java.io.Serializable;
import javax.persistence.*;

/**
 * Entity implementation class for Entity: Log
 *
 */
@Entity

public class Log implements Serializable {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private long id;
	@Embedded
	private DateLog dateLog;
	
	private String level;
	private String classe;
	private String msg;
	private Machine machine;
	
	@Override
	public String toString() {
		return "Log [id=" + id + ", dateLog=" + dateLog + "]";
	}

	private static final long serialVersionUID = 1L;

	public Log() {
		super();
	}   
	
	public DateLog getDateLog() {
		return this.dateLog;
	}

	public void setDateLog(DateLog dateLog) {
		this.dateLog = dateLog;
	}

	public String getLevel() {
		return level;
	}

	public void setLevel(String lvl) {
		this.level = lvl;
	}

	public String getClasse() {
		return classe;
	}

	public void setClasse(String classe) {
		this.classe = classe;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Machine getMachine() {
		return machine;
	}

	public void setMachine(Machine machine) {
		this.machine = machine;
	}
   
	
}
