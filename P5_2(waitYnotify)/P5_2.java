/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package P5_2;

import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author Miarpe
 */
public class P5_2 {
private static final int numHilos = 5;
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        ClassA cA = new ClassA(6);
        List<Thread> hilos = new ArrayList<>(numHilos);
        
        Thread t0;
        ClassB cB;
        for(int i = 0; i < numHilos; i++){
            cB = new ClassB(cA,i);
            t0 = new Thread(cB,Integer.toString(i));
            hilos.add(t0);
            hilos.get(i).start();
        }
       
        for(int i = 0; i<hilos.size(); i++){
            try {
                hilos.get(i).join();
            } catch (InterruptedException ex) {
            }
        }
        
        System.out.println("Program come to end");
        
    }    
}
class ClassA {
    
    private int counter;
    private int last;
    
    
    public ClassA(int n){
        counter = n;
        last = -1;
    }
    
    public int getActual(){
        return counter; 
    }
    
    public int getLast() {
        return last;
    }
    
    public void setLast(int n){
        last = n; 
    }
    
    public boolean isFinished(){
        return counter == 0 ;
    }
    
    
    public void EnterAndWait() {
        
        System.out.println("Executing thread: " + Thread.currentThread().getName() );
        
        try {
            Thread.sleep(100);
        } catch (InterruptedException ex) {
        }
        
        System.out.println("Finishing thread: " + Thread.currentThread().getName());
        counter--;
        System.out.println("Still " + counter +" to execute");
        
    }
}

class ClassB implements Runnable{

    private ClassA obj;
    private int i;
    
    public ClassB (ClassA obj, int i){
        this.obj = obj;
        this.i = i;
        
    }
    
    
    @Override
    public void run() {
        synchronized(obj){ 
            
            while (!obj.isFinished()) {
                while (obj.getLast() == i) {
                    
                    try{
                        System.out.println("Thread "+ i + " waiting");
                        obj.wait();
                    }catch(InterruptedException ex) {
                        System.out.println(i+" interrupted");
                    }

                }
                
                if(obj.getActual() >= 1){
                    obj.EnterAndWait();
                    obj.setLast(i);
                    obj.notifyAll();
                }
            }
        }
        try{
            Thread.sleep((long) (Math.random()*5000));
        } catch (InterruptedException ex) {
            System.out.println(this.i+" interrupted...");
        }

    }
}