/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package P5_1;

import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author Miarpe
 */


//d) Sin usar la funcion synchronized se ejecutan todos los hilos a la vez
//e) Usando la funcion synchronized se ejecutará uno por uno esperando a que el anterior finalice
public class P5_1 {
private static final int numHilos = 5;
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        ClassA cA = new ClassA();
        List<Thread> hilos = new ArrayList<>(numHilos);
        
        Thread t0;
        ClassB cB;
        for(int i = 0; i < numHilos; i++){
            cB = new ClassB(cA,i);
            t0 = new Thread(cB);
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
    
    
    public void EnterAndWait() {
        
        System.out.println("Executing thread: " + Thread.currentThread().getName() );
        
        try {
            Thread.sleep(100);
        } catch (InterruptedException ex) {
        }
        
        System.out.println("Finishing thread: " + Thread.currentThread().getName());
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
            obj.EnterAndWait();
        }
    }
}
