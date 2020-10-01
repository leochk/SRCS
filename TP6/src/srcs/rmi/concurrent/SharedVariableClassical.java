package srcs.rmi.concurrent;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.LinkedList;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class SharedVariableClassical<T extends Serializable> implements SharedVariable<T> {
    private T var;

    private int taken = 0;
    private Lock l = new ReentrantLock(true);
    private Condition c = l.newCondition();

    public SharedVariableClassical(T var) throws RemoteException {
        this.var = var;
    }

    @Override
    public T obtenir() throws RemoteException {
        l.lock();
        try {
            while (taken > 0)
                c.await();

            taken++;
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
            taken--;
            var = x;
            c.signalAll();
        } finally {
            l.unlock();
        }
    }
}
