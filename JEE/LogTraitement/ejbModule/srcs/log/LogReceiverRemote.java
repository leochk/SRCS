package srcs.log;

import javax.ejb.Remote;

@Remote
public interface LogReceiverRemote {
	void newLog(Log l);
	void newLogAsync(Log l);
	Machine[] getMachines();
	Log[] getLogs();
	Log[] getLogsWithLevel(String lvl);
	void mr_proper();
	Log[] getLogsWithMachine(String name_machine);
}
