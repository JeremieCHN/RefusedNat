package refusednat.table;

import java.net.InetSocketAddress;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * NAT Table Item, used to save info
 */
public class PortTableItem {
    public int port;
    public InetSocketAddress lanAddr;
    public Set<InetSocketAddress> allowedPort;
    public Set<InetSocketAddress> refusedPort;

    public PortTableItem(int port, InetSocketAddress lanAddr) {
        this.port = port;
        this.lanAddr = lanAddr;
        allowedPort = new HashSet<>();
        refusedPort = new HashSet<>();
    }

    public String toPrintStr() {
        StringBuilder builder = new StringBuilder();
        builder.append("Port: ").append(port).append("\n");
        builder.append("Lan: ").append(lanAddr).append("\n");
        builder.append("AllowedPort:\n");
        for (InetSocketAddress addr : allowedPort)
            builder.append("\t").append(addr).append("\n");
        builder.append("RefusedPort:\n");
        for (InetSocketAddress addr : refusedPort)
            builder.append("\t").append(addr).append("\n");
        return builder.toString();
    }
}
