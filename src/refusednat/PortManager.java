package refusednat;

import java.util.*;

public class PortManager {

    private PortDispatcher portDispatcher;
    private Map<Integer, Long> portTimeSet;
    private long configTime = 3000;

    public PortManager() {
        portDispatcher = new PortDispatcher(new HashSet<>());
        portTimeSet = new HashMap<>();
    }


    /**
     * get a new port, and its overtime is the default
     *
     * @return port
     */
    public int get() {
        int port = portDispatcher.get();
        portTimeSet.put(port, System.currentTimeMillis());
        return port;
    }

    /**
     * recover the port
     *
     * @param port the port
     */
    public void recover(int port) {
        portTimeSet.remove(port);
        portDispatcher.recovery(port);
    }

    /**
     * Extend the overtime of the port given
     *
     * @param port the port
     */
    public void renewalPort(int port) {
        portTimeSet.put(port, System.currentTimeMillis());
    }

    /**
     * Set the overtime of new ports
     *
     * @param time millisecond
     */
    public void setConfigTime(long time) {
        configTime = time;
    }

    /**
     * check whether the port has overtime
     *
     * @param port the port to Check
     * @return check result
     */
    public boolean checkOvertime(int port) {
        if (portTimeSet.containsKey(port)) {
            long time = portTimeSet.get(port);
            if (System.currentTimeMillis() - time > configTime) {
                recover(port);
                return true;
            } else {
                return false;
            }
        }
        return true;
    }

    /**
     * Dispatcher of ports, it does not concert about multi thread
     */
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

}
