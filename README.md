README
Author: Azhar Abdool
CSC OS2

This project simulates a bar scheduling system where patrons arrive at random times 
and place drink orders, which are then processed by Andre the barman using different 
scheduling algorithms, namely First come first serve(FCFS) and Shortest job first(SJF). 
The aim is to compare the algorithms using metrics like average turnaround time, average 
waiting time, average response time, and throughput. 

To compile the project simply type "make" or alternatively manually compile files with javac

To run the program, either run with "make run" which will run with default parameters, being 
the FCFS scheduling algorithm and 100 patrons
To customise the scheduling algorithm either navigate to SchedulingSimulation.java and change 
the variables or feed the command line arguments when running or use the makefile in the form:
"make NUM_PATRONS=50 SCHEDULING_ALGORITHM=1 run" which will run with 50 patrons and use the 
SJF algorthm 

number_of_patrons = any integer
scheduling_algorithm:
0=FCFS
1=SJF

Output:
when run, the simulation starts and serves patrons, metrics are recorded and the averages are 
printed at the end. 

Structure:
"src/"- contains the java source files
"bin/"- contains compiled class files

Dependencies:
Java JDK
