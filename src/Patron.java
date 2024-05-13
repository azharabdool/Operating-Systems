//M. M. Kuttel 2024 mkuttel@gmail.com
package barScheduling;

import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;
import java.util.concurrent.CountDownLatch;

/*
 This is the basicclass, representing the patrons at the bar
 */

public class Patron extends Thread {
	
	private Random random = new Random();// for variation in Patron behaviour

	private CountDownLatch startSignal; //all start at once, actually shared
	private Barman theBarman; //the Barman is actually shared though

	private int ID; //thread ID 
	private int lengthOfOrder;
	private long startTime, endTime; //for all the metrics
	private long firstResponseTime; // time from order placed to order received

	//public FileWriter fileW;


	
	Patron(int ID, CountDownLatch startSignal, Barman aBarman) {
		this.ID=ID;
		this.startSignal=startSignal;
		this.theBarman=aBarman;
		
		this.lengthOfOrder=random.nextInt(5)+1;//between 1 and 5 drinks
		//this.fileW = fileW;
	}
	
	
	
	

	public void run() {
		try {
			//Do NOT change the block of code below - this is the arrival times
			startSignal.countDown(); //this patron is ready
			startSignal.await(); //wait till everyone is ready
	        int arrivalTime = random.nextInt(300)+ID*100;  // patrons arrive gradually later
	        sleep(arrivalTime);// Patrons arrive at staggered  times depending on ID 
			System.out.println("thirsty Patron "+ this.ID +" arrived");
			//END do not change
			
	        //create drinks order
	        DrinkOrder[] drinksOrder = new DrinkOrder[lengthOfOrder];
			for(int i=0;i<lengthOfOrder;i++) {
	        	drinksOrder[i]=new DrinkOrder(this.ID);
	        	
	        }
			System.out.println("Patron "+ this.ID + " submitting order of " + lengthOfOrder +" drinks"); //output in standard format  - do not change this
	        
			startTime = System.currentTimeMillis();// started placing orders
			//need to get response time
			
			for (DrinkOrder order : drinksOrder) {
                System.out.println("Order placed by " + order.toString());
                theBarman.placeDrinkOrder(order);
            }
			for (DrinkOrder order : drinksOrder) {
                order.waitForOrder();
                if (order.equals(drinksOrder[0])) {
                    firstResponseTime = System.currentTimeMillis();
                }
            }
			
			endTime = System.currentTimeMillis();
            long totalTime = endTime - startTime;
            long responseTime = firstResponseTime - startTime;
            long waitingTime = totalTime - orderProcessingTime(drinksOrder);

			//synchronized (fileW) {
            //    fileW.write(String.format("%d,%d,%d,%d,%d,%d\n", ID, arrivalTime, totalTime, lengthOfOrder, responseTime, waitingTime));
            //}
			
			//writeToFile( String.format("%d,%d,%d\n",ID,arrivalTime,totalTime));
			System.out.println("Patron " + this.ID + " got order in " + totalTime + "ms");
            System.out.println("Patron " + this.ID + " got first drink after " + (responseTime + "ms of waiting"));
            System.out.println("Patron " + this.ID + " waited for " + waitingTime + "ms");

			
		} catch (InterruptedException e1) {  //do nothing
		} //catch (IOException e) {
			//  Auto-generated catch block
			//e.printStackTrace();
		//}

	}
	// Helper method to calculate the total processing time for all orders
    private long orderProcessingTime(DrinkOrder[] orders) {
        long totalProcessingTime = 0;
        for (DrinkOrder order : orders) {
            totalProcessingTime += order.getExecutionTime();
        }
        return totalProcessingTime;
    }
}
	

