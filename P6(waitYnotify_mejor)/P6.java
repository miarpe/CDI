/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package p6;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author Miarpe
 */
public class P6 {
private static final int numHilos = 2;
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {

        ClassA cA = new ClassA();
        ArrayList<Thread> threads = new ArrayList<>();
        ArrayList<ClassB> cB = new ArrayList<>();

        for (int i = 0; i < numHilos; i++) {
            cB.add(new ClassB(cA,i));
        }

        cB.get(0).setThread(cB.get(1));
        cB.get(1).setThread(cB.get(0));

        for (int i = 0; i < numHilos; i++) {
            threads.add(new Thread(cB.get(i)));
            threads.get(i).start();
        }

        try {
            Thread.sleep(100);
        } catch (InterruptedException ex) {
        }

        synchronized (cB.get(0)) {
            cB.get(0).notify();
        }

        try {
            Thread.sleep(1000);
        } catch (InterruptedException ex) {
        }
        
        for (int i = 0; i < numHilos; i++) {
            try {
                threads.get(i).interrupt();
                threads.get(i).join();
            } catch (InterruptedException ex) {
            }
        }
        
        System.out.println("Program come to end");

    }

}

class ClassA {


    public ClassA() {
    }

    public void EnterAndWait() {
        
        System.out.println("Executing thread: " + Thread.currentThread().getName() );
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
        }
        System.out.println("Finishing thread: " + Thread.currentThread().getName());
        
    }
}

class ClassB implements Runnable {

    ClassA obj;
    private int i;
    ClassB cB;

    public ClassB(ClassA obj,int i) {
        this.obj = obj;
        this.i = i;
    }

    public void setThread(ClassB t) {
        this.cB = t;
    }

    @Override
    public void run() {
        while (true) {
            synchronized (this) {
                try {
                    this.wait();
                } catch (InterruptedException e) {
                    break;
                }
            }
            synchronized (cB) {
                this.obj.EnterAndWait();
                cB.notify();
            }
        }
    }

}