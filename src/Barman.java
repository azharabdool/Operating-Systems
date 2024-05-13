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
    private int ordersCompleted;
    private long totalTurnaroundTime;
    private long totalWaitingTime;
    private long totalResponseTime;
    private long startTime;
	
	public Barman(  CountDownLatch startSignal,int schedAlg) {
		if (schedAlg==0)
		this.orderQueue = new LinkedBlockingQueue<>();
		//FIX below
		else
            this.orderQueue = new PriorityBlockingQueue<>(); // Placeholder for other scheduling algorithms
		this.startSignal = startSignal;
		this.ordersCompleted = 0;
        this.totalTurnaroundTime = 0;
        this.totalWaitingTime = 0;
        this.totalResponseTime = 0;
		
	}	
	
	
	public void placeDrinkOrder(DrinkOrder order) throws InterruptedException {
        orderQueue.put(order);
    }
	
	public int getOrdersCompleted() {
    	return ordersCompleted;
	}

	public void run() {
		try {
			DrinkOrder nextOrder;
			
			startSignal.countDown(); //barman ready
			startSignal.await(); //check latch - don't start until told to do so
			startTime = System.currentTimeMillis();
			while(true) {
				nextOrder=orderQueue.take();
				System.out.println("---Barman preparing order for patron "+ nextOrder.toString());
				sleep(nextOrder.getExecutionTime()); //processing order
				System.out.println("---Barman has made order for patron "+ nextOrder.toString());
				ordersCompleted++;

				long endTime = System.currentTimeMillis();
				totalTurnaroundTime += (endTime - nextOrder.getArrivalTime());
                totalWaitingTime += (endTime - startTime - nextOrder.getExecutionTime());
                totalResponseTime += (endTime - startTime);

				nextOrder.orderDone();

				
			}
				
		} catch (InterruptedException e1) {
			System.out.println("---Barman is packing up ");
		}
		// Calculate averages
        double averageTurnaroundTime = (double) totalTurnaroundTime / ordersCompleted;
        double averageWaitingTime = (double) totalWaitingTime / ordersCompleted;
        double averageResponseTime = (double) totalResponseTime / ordersCompleted;
		
		System.out.println("Average Turnaround Time: " + averageTurnaroundTime + "ms");
        System.out.println("Average Waiting Time: " + averageWaitingTime + "ms");
        System.out.println("Average Response Time: " + averageResponseTime + "ms");
    }


}



