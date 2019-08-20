package refusednat;

import java.util.HashSet;
import java.util.Set;

public class PortManager {
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