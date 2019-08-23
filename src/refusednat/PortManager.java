package refusednat;

import java.util.*;

public class PortManager {

    private final Object portDispatcherLock = new Object();
    private PortDispatcher portDispatcher;
    private Map<Integer, Long> timeSet;

    private long validTime = 3000;
    private OvertimeListener overtimeListener = null;

    private Thread overtimeThread;

    public PortManager() {
        Set<Integer> retain = new HashSet<>(Arrays.asList(8080, 123));
        portDispatcher = new PortDispatcher(retain);
        timeSet = new HashMap<>();
        overtimeThread = new Thread(this::checkOverTime);
        overtimeThread.start();
    }

    /**
     * set the valid time for all port
     * @param validTime how long the port will be locked
     */
    public void setValidTime(long validTime) {
        this.validTime = validTime;
    }

    /**
     * set the over time listener
     * @param listener the listener
     */
    public void setOvertimeListener(OvertimeListener listener) {
        this.overtimeListener = listener;
    }

    /**
     * reset the valid time of the port given
     * @param port the port to reset
     */
    public void refreshValidTime(int port) {
        synchronized (portDispatcherLock) {
            if (timeSet.containsKey(port))
                timeSet.put(port, validTime);
        }
    }

    /**
     * get a port
     * @return port, -1 if there is no port
     */
    public synchronized int getPort() {
        int res;
        synchronized (portDispatcherLock) {
            res = portDispatcher.get();
            if (res != -1)
                timeSet.put(res, validTime);
        }
        return res;
    }

    public void recoveryPort(int port) {
        synchronized (portDispatcherLock) {
            if (timeSet.containsKey(port)) {
                timeSet.remove(port);
                portDispatcher.recovery(port);
            }
        }
    }

    /**
     * Check all port each 3 seconds
     */
    private void checkOverTime() {
        while (true) {
            synchronized (portDispatcherLock) {
                timeSet.entrySet().removeIf(entry -> {
                    entry.setValue(entry.getValue() - 3000);
                    if (entry.getValue() < 0) {
                        if (overtimeListener != null)
                            overtimeListener.onOvertime(entry.getKey());
                        return true;
                    } else {
                        return false;
                    }
                });
            }
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                break;
            }
        }
    }

    /**
     * this function will be called when the port over time
     */
    public interface OvertimeListener {
        public void onOvertime(int port);
    }


}

class PortDispatcher {

    PortDispatcher(Set<Integer> retain) {
        this(1, 65535, retain);
    }

    PortDispatcher(int min, int max, Set<Integer> retain) {
        this.min = min;
        this.max = max;
        this.retain = retain;
        this.index = min;
        this.using = new HashSet<>();
    }

    private int min;
    private int max;
    private int index; // next port
    private Set<Integer> retain;
    private Set<Integer> using;

    int get() {
        // if there is no more port
        if (retain.size() + using.size() == max - min + 1)
            return -1;

        // find out a port unused
        while (retain.contains(index) || using.contains(index)) {
            index++;
            if (index > max)
                index = min;
        }
        int tmp = index;
        index++;
        if (index > max)
            index = min;
        using.add(tmp);
        return tmp;
    }

    void recovery(int port) {
        using.remove(port);
    }
}