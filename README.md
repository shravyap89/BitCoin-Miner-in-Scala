# BitCoin-Miner-in-Scala

Group Members:
1. Sravya Pasupuleti 21229828
2. Priti Changlani   17824230

Project structure:
Project1withSBT
	|
	|---> project	       	
	|---> target          |---> main 
	|---> src------------>|---> resources        
	|---> build.sbt	      |---> scala-----------> Project1.scala
	|---> README.txt			     	

Results obtained:
1. We defined the work unit to be "Chunk" which is basically the number of bit coins that each worker has to generate in each call
   by the master. Master then checks whether that bit coin was already present or not. 
   "Worker_Calls" defines the number of calls to workers assigned in roundrobinfashion(whichever worker gets free it is assigned the next call). 
   "Number of workers" is the total number of Actors which are going to be used for mining the bitcoins.
   The best performance was for work unit size = 500, number of workers 25 and Worker_call = 10

2&3. Ratio of CPU time to real time for number of zeroes = 4 and number of zeroes = 5. These runs are for a single machine.
| Number of zeroes | Workers | Chunk | Worker_Call | Real Time  | User Time  | System Time | Ratio of CPU to Real Time | Bitcoins |
|------------------|---------|-------|-------------|------------|------------|-------------|---------------------------|----------|
|            4     |    10   |   10  |         10  | 0m22.603s  | 1m5.489s   | 0m1.027s    | 2.94                      | 108      |
|            4     |    10   |   50  |         20  | 3m0.806s   | 8m57.804s  | 0m8.236s    | 2.97                      | 994      |
|            4     |    15   |   50  |         20  | 3m0.671s   | 8m44.69s   | 0m8.412s    | 2.95                      | 992      |
|            4     |    25   |   500 |         10  | 14m27.374s | 44m36.314s | 0m36.943s   | 3.21                      | 4866     |
|            5     |    15   |   30  |         15  | 5m14.672s  | 15m34.340s | 0m11.853s   | 3.00                      | 107      |

4. Coin with most number of zeroes: 7
   We found that the coin with largest number of zeroes = 
   sravya;JAAT9p3UUHg0neo	0000000166d45a347e5eee4181d75333b93bcf4b4b1a5c2d1f6193446a456a62

5. The largest number of working machines you were able to run your code with : 2
   We used one machine as the server and one machine as the remote worker. Due to time crunch, we could run it only with 2 machines.

Steps to Run:
1. We downloaded and placed akka and scala folder on our desktop
   /Users/PRITI/Downloads/akka-2.3.13
   /Users/PRITI/Downloads/scala-2.11.7
   
On common prompt we ran the following commands: 
2. To compile: scalac -classpath "/Users/PRITI/Downloads/akka-2.3.13/lib/akka/akka-actor_2.11-2.3.13.jar:/Users/PRITI/Downloads/akka-2.3.13/lib/akka/akka-remote_2.11-2.3.13.jar:/Users/PRITI/Downloads/akka-2.3.13/lib/akka/netty-3.8.0.Final.jar:/Users/PRITI/Downloads/akka-2.3.13/lib/akka/protobuf-java-2.5.0.jar:/Users/PRITI/Downloads/akka-2.3.13/lib/akka/config-1.2.1.jar:/Users/PRITI/Downloads/scala-2.11.7/lib/scala-library.jar" /Users/PRITI/Downloads/lastTry/src/main/scala/Project1.scala 4

3. To run: time scala -classpath "/Users/PRITI/Downloads/akka-2.3.13/lib/akka/akka-actor_2.11-2.3.13.jar:/Users/PRITI/Downloads/akka-2.3.13/lib/akka/akka-remote_2.11-2.3.13.jar:/Users/PRITI/Downloads/akka-2.3.13/lib/akka/netty-3.8.0.Final.jar:/Users/PRITI/Downloads/akka-2.3.13/lib/akka/protobuf-java-2.5.0.jar:/Users/PRITI/Downloads/akka-2.3.13/lib/akka/config-1.2.1.jar:/Users/PRITI/Downloads/scala-2.11.7/lib/scala-library.jar" /Users/PRITI/Downloads/lastTry/src/main/scala/Project1.scala 4

 