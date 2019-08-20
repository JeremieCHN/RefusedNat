package refusednat.table;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Nat Table, accelerate search by two map
 */
public class PortTable {
    private Map<Integer, PortTableItem> _portMap;
    private Map<InetSocketAddress, List<PortTableItem>> _lanAddrMap;

    public PortTable() {
        _portMap = new HashMap<>();
        _lanAddrMap = new HashMap<>();
    }

    public void add(int port, InetSocketAddress lanAddr) {
        PortTableItem item = new PortTableItem(port, lanAddr);
        _portMap.put(port, item);
        List<PortTableItem> list = new LinkedList<>();
        list.add(item);
        _lanAddrMap.put(lanAddr, list);
    }

    public void remove(int port) {
        if (_portMap.containsKey(port)) {
            PortTableItem item = _portMap.get(port);
            if (_lanAddrMap.containsKey(item.lanAddr)) {
                for (int i = 0; i < _lanAddrMap.get(item.lanAddr).size(); i++) {
                    if (_lanAddrMap.get(item.lanAddr).get(i).port == item.port) {
                        _lanAddrMap.get(item.lanAddr).remove(i);
                        break;
                    }
                }
            }
            _portMap.remove(port);
        }
    }

    public boolean checkAllowed(InetSocketAddress wanAddr, int port) {
        return _portMap.containsKey(port) && _portMap.get(port).allowedPort.contains(wanAddr);
    }

    public boolean checkRefused(InetSocketAddress wanAddr, int port) {
        return _portMap.containsKey(port) && _portMap.get(port).refusedPort.contains(wanAddr);
    }

    public void addRefused(int port, InetSocketAddress addr) {
        if (_portMap.containsKey(port))
            _portMap.get(port).refusedPort.add(addr);
    }

    public void addAllowed(int port, InetSocketAddress addr) {
        if (_portMap.containsKey(port))
            _portMap.get(port).allowedPort.add(addr);
    }

    public PortTableItem get(int port) {
        return _portMap.get(port);
    }

    public List<PortTableItem> get(InetSocketAddress lanAddr) {
        return _lanAddrMap.get(lanAddr);
    }

    public void print() {
        for (PortTableItem item : _portMap.values()) {
            System.out.println(item.toPrintStr());
        }
    }
}
