//M. M. Kuttel 2024 mkuttel@gmail.com

package barScheduling;
// the main class, starts all threads


import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.CountDownLatch;


public class SchedulingSimulation {
	static int noPatrons=10; //number of customers - default value if not provided on command line
	static int sched=1; //which scheduling algorithm, 0= FCFS
			
	static CountDownLatch startSignal;

	
	static Patron[] patrons; // array for customer threads
	static Barman Andre;
	//static FileWriter writer;

	
	public static void main(String[] args) throws InterruptedException, IOException {
		
		

		//deal with command line arguments if provided
		if (args.length==1) {
			noPatrons=Integer.parseInt(args[0]);  //total people to enter room
		} else if (args.length==2) {
			noPatrons=Integer.parseInt(args[0]);  //total people to enter room
			sched=Integer.parseInt(args[1]); 
		}
		
		//writer = new FileWriter("turnaround_time_"+Integer.toString(sched)+".txt", false);
		//writer.write("Patron ID, Arrival Time, Turnaround Time, Length of Order, Response Time, Waiting Time\n");

		startSignal= new CountDownLatch(noPatrons+2);//Barman and patrons and main method must be raeady
		
		//create barman
        Andre= new Barman(startSignal,sched); 
     	Andre.start();
		long startTime = System.currentTimeMillis(); // Start the timer

	    //create all the patrons, who all need access to Andre
		patrons = new Patron[noPatrons];
		for (int i=0;i<noPatrons;i++) {
			patrons[i] = new Patron(i,startSignal,Andre);
			patrons[i].start();
		}
		
		System.out.println("------Andre the Barman Scheduling Simulation------");
		System.out.println("-------------- with "+ Integer.toString(noPatrons) + " patrons---------------");

      	startSignal.countDown(); //main method ready
      	
		
      	//wait till all patrons done, otherwise race condition on the file closing!
      	for (int i=0;i<noPatrons;i++) {
			patrons[i].join();
		}
		
		int ordersCompleted = Andre.getOrdersCompleted();
    	
		
		
		
		System.out.println("------Waiting for Andre------");
    	
		
		
		Andre.interrupt();   //tell Andre to close up
    	Andre.join(); //wait till he has
      	//.close(); //all done, can close file
      	long endTime = System.currentTimeMillis(); // End the timer
		double throughput = ordersCompleted / ((endTime - startTime) / 1000.0); // Calculate the throughput
		System.out.println("Throughput: " + throughput + " orders per second");
		System.out.println("------Bar closed------");
	}

}
