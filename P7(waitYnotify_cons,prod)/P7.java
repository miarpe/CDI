/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package p7;

/**
 *
 * @author Miarpe
 */
import java.util.ArrayList;
import java.util.LinkedList;

//b) El deadlock se podría producir en el momento que tanto consumidor como productor intenten utilizar el buffer a la vez,
// y por eso se utiliza la función syncronized en write y read. 
//Las líneas donde se podría producir el deadlock es en la línea 98 y 119 que es cuando se llama a estas funcionas, y por ende en las líneas 54 y 69que es donde se encuentran las llamadas
public class P7 {
    
    static final int CAPACIDAD_MAX = 10;

    public static void main(String args[]) {
	Buffer buffer = new Buffer(CAPACIDAD_MAX);
	Productor productor = new Productor(buffer);
	Consumidor consumidor = new Consumidor(buffer);

	Thread th1 = new Thread(productor, "Productor");
	Thread th2 = new Thread(consumidor, "Consumidor");
	th1.start();
	th2.start();

	try {
            Thread.sleep(500);
            th1.interrupt();
            th2.interrupt();
            th1.join();
            th2.join();
	} catch (InterruptedException e) {
	}
	System.out.println("Program come to end");
    }
}

class Buffer {
    
    private LinkedList<Integer> productos;
    private int capacidad;

    public Buffer(int capacidad) {
	productos = new LinkedList<>();
	this.capacidad = capacidad;
    }

    public void write() {
	synchronized (productos) {
            while (productos.size() >= capacidad){
                try {
                    System.out.println(Thread.currentThread().getName() + " is waiting until Consumidor reads");
                    productos.wait();
                    } catch (InterruptedException e) {
                }
            }
            productos.add(0);
            System.out.println(Thread.currentThread().getName() + " is writting, new size: " + productos.size());
            productos.notifyAll();
	}
    }

    public void read() {
        synchronized (productos) {
            while (productos.size() <= 0) {
                try {
                    System.out.println(Thread.currentThread().getName() + " is waiting until Productor writes");
                    productos.wait();
                    } catch (InterruptedException e) {
                }
            }
            productos.remove();
            System.out.println(Thread.currentThread().getName() + " is reading, new size: " + productos.size());
            productos.notifyAll();
        }
    }
}

class Productor implements Runnable {
    
    Buffer buffer;

    public Productor(Buffer buffer) {
	this.buffer = buffer;
    }

    @Override
    public void run() {
        while (true){
            try {
                Thread.sleep((long) (Math.random()*5));
                buffer.write();
		} catch (InterruptedException e) {
                    break;
            }
        }
    }
}

class Consumidor implements Runnable {

    Buffer buffer;

    public Consumidor(Buffer buffer) {
        this.buffer = buffer;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep((long) (Math.random()*5));
                buffer.read();
                }catch (InterruptedException e) {
                break; 
            }
	}
    }
}
