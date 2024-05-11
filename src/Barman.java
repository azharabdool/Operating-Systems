package barScheduling;

import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;

import java.io.FileWriter;
import java.io.IOException;

import java.util.Comparator;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;

/*
 Barman Thread class.
 */

public class Barman extends Thread {
	

	private CountDownLatch startSignal;
	private BlockingQueue<DrinkOrder> orderQueue;
    private FileWriter responseTimeWriter;
    private FileWriter turnaroundTimeWriter;
    private FileWriter waitingTimeWriter;

	
	public Barman(  CountDownLatch startSignal,int schedAlg) {
		if (schedAlg==0)
			this.orderQueue = new PriorityBlockingQueue<>();
		//FIX below
		else
            this.orderQueue = new PriorityBlockingQueue<>(); // Placeholder for other scheduling algorithms
		this.startSignal = startSignal;
        
		try {
			responseTimeWriter = new FileWriter("response_time.txt");
			turnaroundTimeWriter = new FileWriter("turnaround_time.txt");
			waitingTimeWriter = new FileWriter("waiting_time.txt");
			System.out.println("FileWriters initialized successfully.");

		} catch (IOException e) {
			e.printStackTrace();
		}
	}	
	
	public void placeDrinkOrder(DrinkOrder order) throws InterruptedException {
        orderQueue.put(order);
    }
	
	
	public void run() {
		try {
			DrinkOrder nextOrder;
			
			startSignal.countDown(); //barman ready
			startSignal.await(); //check latch - don't start until told to do so

			while(true) {
				nextOrder=orderQueue.take();
				long startTime = System.currentTimeMillis();
				System.out.println("---Barman preparing order for patron "+ nextOrder.toString());
				sleep(nextOrder.getExecutionTime()); //processing order
				long endTime = System.currentTimeMillis();
				System.out.println("---Barman has made order for patron "+ nextOrder.toString());
				nextOrder.orderDone();

				long responseTime = startTime - nextOrder.getArrivalTime;
            	long turnaroundTime = endTime - nextOrder.getArrivalTime;
                long waitingTime = endTime - startTime;

				try {
                    System.out.println("Writing metrics to files...");
                    responseTimeWriter.write(Long.toString(responseTime) + "\n");
					responseTimeWriter.flush();

					turnaroundTimeWriter.write(Long.toString(turnaroundTime) + "\n");
                    turnaroundTimeWriter.flush();
					waitingTimeWriter.write(Long.toString(waitingTime) + "\n");
                    waitingTimeWriter.flush();
					// Flush to ensure immediate writing
                    
                    
                    
                    System.out.println("Metrics written successfully.");
                } catch (IOException e) {
                    e.printStackTrace();
                }
			}
				
		} catch (InterruptedException e1) {
			System.out.println("---Barman is packing up ");
		} finally {
            // Close file writers
            try {
                responseTimeWriter.close();
                turnaroundTimeWriter.close();
                waitingTimeWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


}



