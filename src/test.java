import refusednat.table.PortTable;

import java.net.InetSocketAddress;
import java.util.*;

public class test {
    public static void main(String[] args) {
        PortTable table = new PortTable();
        InetSocketAddress lan = new InetSocketAddress("192.168.1.1", 123);
        InetSocketAddress wan = new InetSocketAddress("222.222.1.1", 124);
        // suppose a packet from lan to wan
        System.out.println(table.get(lan) == null);
        table.add(100, lan);
        table.addAllowed(100, wan);
        table.print();
        // suppose a packet from unknown wan to lan
        InetSocketAddress wan1 = new InetSocketAddress("222.222.1.1", 125);
        System.out.println(table.checkAllowed(wan1, 100));
        table.addRefused(100, wan1);
        table.print();
        // suppose a packet form lan to wan1
        // in fact, lan may be mapped to more than one port, so, I need a policy of selection
        System.out.println(table.checkRefused(wan1, 100));
        table.add(101, lan);
        table.addAllowed(101, wan);
        table.print();
    }
}
