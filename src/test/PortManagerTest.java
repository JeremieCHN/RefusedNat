package test;

import refusednat.PortManager;
import refusednat.impl.IPortManager;

import java.util.*;

public class PortManagerTest {

    public static void main(String[] args) {
        Thread thread = new Thread(() -> new PortManagerTest().test());
        thread.start();
        Scanner scanner = new Scanner(System.in);
        scanner.nextLine();
        thread.interrupt();
    }


    private final Object MyPortLock = new Object();
    private Set<Integer> MyPort = new HashSet<>();
    private PortManager manager = new PortManager();

    private IPortManager.OvertimeListener listener = port -> {
        synchronized (MyPortLock) {
            MyPort.remove(port);
            System.out.println("Port Overtime: " + port);
            printSet();
        }
    };

    private void test() {
        manager.setOvertimeListener(listener);
        manager.startTimer();

        while (true) {
            randomTest();
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
                manager.stopTimer();
                break;
            }
        }
    }

    private void randomTest() {
        if (new Random().nextBoolean()) {
            synchronized (MyPortLock) {
                int val = manager.get();
                MyPort.add(val);
                System.out.println("Add " + val);
                printSet();
            }
        } else {
            synchronized (MyPortLock) {
                Iterator<Integer> it = MyPort.iterator();
                if (it.hasNext()) {
                    int val = it.next();
                    MyPort.remove(val);
                    manager.recover(val);
                    System.out.println("Remove " + val);
                    printSet();
                }
            }
        }
    }

    private void printSet() {
        System.out.print("Set: ");
        for (Integer integer : MyPort) System.out.print(integer + " ");
        System.out.println();
        System.out.println("---------------------");
    }
}
