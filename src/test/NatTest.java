package test;

import refusednat.NatResult;
import refusednat.RefusedNat;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;

public class NatTest {

    public static void main(String[] args) throws UnknownHostException {
        new NatTest().test();
    }

    private RefusedNat nat = new RefusedNat(InetAddress.getLocalHost());

    public NatTest() throws UnknownHostException {
    }

    private void test() throws UnknownHostException {

        // 1.Simple communication
        printResult(nat.recvLAN(new InetSocketAddress("192.168.1.2", 1234), new InetSocketAddress("222.222.1.1", 1)));
        printResult(nat.recvWAN(new InetSocketAddress("222.222.1.1", 1),new InetSocketAddress(InetAddress.getLocalHost(), 1)));

        // 2.Refuse Example
        printResult(nat.recvWAN(new InetSocketAddress("222.222.1.1", 2),new InetSocketAddress(InetAddress.getLocalHost(), 1)));
        printResult(nat.recvLAN(new InetSocketAddress("192.168.1.2", 1234), new InetSocketAddress("222.222.1.1", 2)));
        printResult(nat.recvWAN(new InetSocketAddress("222.222.1.1", 2),new InetSocketAddress(InetAddress.getLocalHost(), 2)));
    }

    private void printResult(NatResult result) {
        if (result.isDrop()) {
            System.out.println("Drop this packet");
        } else if (result.isSendToLan()) {
            System.out.println("Send to Lan");
            if (result.isChangeTarget())
                System.out.println("Changed its Target to " + result.getTarget());
        } else if (result.isSendToWan()) {
            System.out.println("Send to Wan");
            if (result.isChangeSource())
                System.out.println("Changed its Source to " + result.getSource());
        } else {
            System.out.println("Unknown Result");
        }
    }
}
