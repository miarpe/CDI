/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package p9;

/**
 *
 * @author Miarpe
*/

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.ThreadPoolExecutor;

//Si se intenta ejecutar una nueva tarea esta sería rechazada
//mostrando el error de RejectedExecutionException

public class P9 {
	
    public static void main(String[] args) {
	int n = 5;
	ThreadPoolExecutor threadsPool =(ThreadPoolExecutor)Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());;
        for (int i = 0; i < n; i++) {
            int number = (int) (Math.random()*10);
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                int result = 1;
                if ((number == 0) || (number == 1)) {
                result = 1;
                } else {
                    for (int i = 2; i <= number; i++) {
                    result *= i;
                    try {
                        Thread.sleep(20);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
                System.out.println("Factorial of " + number +" = " + result);
                }
            };
			//debe ponerse encima lo que va a ejecutar para que ya esté en memoria, si no habría que ponerlo en el parentesis
            threadsPool.execute(runnable);
	}
	awaitAndShutdown(threadsPool);
        System.out.println("Program come to end");
    }
	
    private static  void awaitAndShutdown(ExecutorService threadsPool) {
	threadsPool.shutdown();
	try {
            if (!threadsPool.awaitTermination(60, TimeUnit.SECONDS)) {
            threadsPool.shutdownNow();
            }
	} catch (InterruptedException ex) {
            threadsPool.shutdownNow();
            Thread.currentThread().interrupt();
	}
    }
}
