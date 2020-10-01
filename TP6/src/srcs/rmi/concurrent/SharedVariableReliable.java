package srcs.rmi.concurrent;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class SharedVariableReliable<T extends Serializable>
        implements SharedVariable<T> {

    private T var;

    private boolean taken = false;
    private Lock l = new ReentrantLock(true);
    private Condition c = l.newCondition();

    private Timer timer = new Timer();

    public SharedVariableReliable(T var) throws RemoteException {
        this.var = var;
    }

    @Override
    public T obtenir() throws RemoteException {
        l.lock();
        try {
            while (taken)
                c.await();
            taken = true;

            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    l.lock();
                    try {
                        taken = false;
                        c.signalAll();
                    } finally {
                        l.unlock();
                    }
                }
            }, 2500);
            return var;

        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            l.unlock();
        }
        return null;
    }

    @Override
    public void relacher(T x) throws RemoteException {
        l.lock();
        try {
            taken = false;
            timer.cancel();
            timer = new Timer();
            var = x;
            c.signalAll();
        } finally {
            l.unlock();
        }
    }

}
