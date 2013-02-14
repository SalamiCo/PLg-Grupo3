package plg.gr3.gui;

import java.awt.event.ActionListener;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import javax.swing.SwingWorker;

import plg.gr3.vm.VirtualMachine;

public final class VirtualMachineWorker extends SwingWorker<Void, Void> {
    
    private final VirtualMachine vm;
    
    private final ActionListener al;
    
    private final ReentrantLock lock = new ReentrantLock();
    
    private final Condition cond = lock.newCondition();
    
    private volatile boolean exec = false;
    
    private volatile boolean step = false;
    
    /**
     * @param vm Máquina virtual a usar
     */
    public VirtualMachineWorker (VirtualMachine vm, ActionListener al) {
        this.vm = vm;
        this.al = al;
    }
    
    @Override
    protected Void doInBackground () throws Exception {
        while (!vm.isStopped()) {
            lock.lock();
            try {
                if (!step && !exec) {
                    cond.await();
                }
            } finally {
                lock.unlock();
            }
            
            if (!vm.isStopped()) {
                boolean execL = false;
                do {
                    vm.step();
                    
                    lock.lock();
                    try {
                        step = false;
                        execL = exec;
                    } finally {
                        lock.unlock();
                    }
                } while (execL);
                publish();
            }
        }
        
        publish();
        return null;
    }
    
    @Override
    protected void process (List<Void> chunks) {
        al.actionPerformed(null);
    }
    
    public void executeProgram () {
        lock.lock();
        try {
            exec = true;
            step = false;
            cond.signal();
        } finally {
            lock.unlock();
        }
    }
    
    public void stepProgram () {
        lock.lock();
        try {
            exec = false;
            step = true;
            cond.signal();
        } finally {
            lock.unlock();
        }
    }
    
    public void stopProgram () {
        lock.lock();
        try {
            vm.stop();
            cond.signal();
        } finally {
            lock.unlock();
        }
    }
    
}
