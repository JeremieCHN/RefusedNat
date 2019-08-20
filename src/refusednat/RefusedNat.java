package refusednat;

import refusednat.table.PortTable;

import java.net.SocketAddress;

public class RefusedNat {

    public RefusedNat() {}

    public void recvWAN(SocketAddress from, SocketAddress To) {}

    public void recvLAN(SocketAddress from, SocketAddress To) {}

    public void printNatTable() {}

    private PortTable table;
}
