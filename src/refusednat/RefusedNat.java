package refusednat;

import refusednat.table.PortTable;
import refusednat.table.PortTableItem;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.List;

public class RefusedNat {

    private PortManager portManager;
    private PortTable portTable;
    private InetAddress wanAddr;

    public RefusedNat(InetAddress wanAddr) {
        portManager = new PortManager();
        portTable = new PortTable();
        this.wanAddr = wanAddr;
    }

    public NatResult recvWAN(InetSocketAddress from, InetSocketAddress to) {
        NatResult drop = new NatResult();
        drop.setDrop(true);

        PortTableItem item = portTable.get(to.getPort());

        // the port do not assign to any lan port
        if (item == null)
            return drop;

        // the port has over time
        if (portManager.checkOvertime(item.port)) {
            portTable.remove(item.port);
            return drop;
        }

        // the port do not allow this wan address
        if (!item.allowedPort.contains(from)) {
            item.refusedPort.add(from);
            return drop;
        }

        // the wan address is passed
        NatResult result = new NatResult();
        result.setSendToLan(true);
        result.setChangeTarget(true);
        result.setTarget(item.lanAddr);
        return result;
    }

    public NatResult recvLAN(InetSocketAddress from, InetSocketAddress to) {
        // check the ports
        List<PortTableItem> items = portTable.get(from);

        if (items != null)
            for (PortTableItem item : items) {
                if (portManager.checkOvertime(item.port)) {
                    portTable.remove(item.port);
                } else if (!item.refusedPort.contains(to)) {
                    item.allowedPort.add(to);

                    NatResult result = new NatResult();
                    result.setSendToWan(true);
                    result.setChangeSource(true);
                    result.setSource(new InetSocketAddress(wanAddr, item.port));
                    return result;
                }
            }

        // new a port
        int newPort = portManager.get();
        portTable.add(newPort, from);
        portTable.addAllowed(newPort, to);

        NatResult result = new NatResult();
        result.setSendToWan(true);
        result.setChangeSource(true);
        result.setSource(new InetSocketAddress(wanAddr, newPort));
        return result;
    }

    public void printNatTable() {

    }
}
