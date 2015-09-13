/**
 * Created by Pasupuleti Sravya on 9/12/2015.
 */
/**
 * Created by Pasupuleti Sravya on 9/12/2015.
 */
/**
 * Created by Pasupuleti Sravya on 9/12/2015.
 */

import java.security.MessageDigest
import akka.actor.{Actor, ActorSystem, Props}
import akka.routing.RoundRobinRouter
import com.typesafe.config.ConfigFactory

object Project1 {

  case class AssignWorkers()
  case class WorkAllocated(chunk: Int, zero_count: Int)
  case class CheckforduplicateMaster(bitcoin: String, sha_input: String)
  case class RegisteratServer()
  case class RemoteWorkerAvailable()
  case class ChunkComplete()
  case class End()

  def main(args: Array[String]) {
    val workers_count = 10
    val chunk = 500
    val workers_call = 20

    //println("Entered the main")
    //This for akka remote communication
    val server_configuration = ConfigFactory.parseString(
      """
      akka{
    		actor{
    			provider = "akka.remote.RemoteActorRefProvider"
    		}
    		remote{
          enabled-transports = ["akka.remote.netty.tcp"]
          netty.tcp{
    			  hostname = "127.0.0.1"
    			  port = 2325
          }
        }
      }""")

    //For a remote actor configuration is as follows
    val remote_worker_configuration = ConfigFactory.parseString(
      """
      akka{
          actor{
            provider = "akka.remote.RemoteActorRefProvider"
		  		}
		  		remote{
            enabled-transports = ["akka.remote.netty.tcp"]
		  			netty.tcp{
						  hostname = "127.0.0.1"
              port = 0
					  }
				  }
      }""")

    if (!args(0).isEmpty()) {
      // when the argument given is ip address
      //println("Entered the not empty loop")
      if (!args(0).contains('.')) {
        //println("Entered the non ip loop")
        val system = ActorSystem("Server", ConfigFactory.load(server_configuration))
        val Master = system.actorOf(Props(new Master(workers_count, args(0).toInt, chunk, workers_call)), name = "Master")
        //println("Send message to master")
        Master ! AssignWorkers()
      }
      else {
        //println("This is remote")
        val system = ActorSystem("Remote", ConfigFactory.load(remote_worker_configuration))
        val remote_worker = system.actorOf(Props(new RemoteWorker(args(0))), name = "remote_worker")
        remote_worker ! RegisteratServer()
      }
    }
  }

  class Master(workers_count: Int, zero_count: Int, chunk: Int, workers_call: Int) extends Actor {
    val work_assigner = context.actorOf(Props[Worker].withRouter(RoundRobinRouter(workers_count)), name = "work_assigner")
    var total_work_calls_with_remote = workers_call
    var count = 0
    //val workers_call = 20
    //Map for unique bitcoins
    var unique_bitcoins = scala.collection.mutable.Map[String, String]()
    val real_start_time: Long = System.currentTimeMillis

    //var workers_call: Int = _
    def receive = {
      case AssignWorkers() =>
        //println("Master received message")
        for (i <- 0 until workers_call) work_assigner ! WorkAllocated(chunk, zero_count)

      case CheckforduplicateMaster(bitcoin, sha_input) =>
        //sha_input is the key and bitcoin the value
        if (!unique_bitcoins.keySet.exists(_ == sha_input)) {
          unique_bitcoins += (sha_input -> bitcoin)
          println(bitcoin)
        }

      case RemoteWorkerAvailable() =>
        total_work_calls_with_remote += 1
        sender ! WorkAllocated(chunk, zero_count)

      case ChunkComplete() =>
        count += 1
        //println("Count is " + count)
        if (count == total_work_calls_with_remote){
          sender ! End()
          context.stop(self)

          //Just for final printing of bitcoin number-------------------------------
          println("\n" + "The number of unique valid bitcoins found = " + unique_bitcoins.size)
          //-----------------------------------------------------------------------

          println("\n" + "Real time taken = " + ((System.currentTimeMillis - real_start_time) * 0.001) + "seconds ")

          context.system.shutdown()
        }
    }

  }


  class Worker extends Actor {

    def receive = {
      case WorkAllocated(chunk, zero_count) =>
        //println("work was allocated by master to me")
        val prefix: String = "sravya;"
        var sha_input: String = ""
        var sha_output: String = ""
        var number_results: Int = 0
        var num_zeroes_req: String = "0"
        var keep_generating = true
        var bitcoin: String = ""

        //Just generating the sequence for check
        //println("I am generating sequence for check")
        //println("zero count is : " + zero_count)
        for (i <- 0 until (zero_count - 1))
          num_zeroes_req = num_zeroes_req.concat("0")

        //println("no of zeroes is:" + num_zeroes_req)

        //println("now i will keep on generating")
        while (keep_generating) {
          sha_input = prefix.concat(generatenonce(15))
          sha_output = hex_Digest(sha_input)

          if (checkForValidCoin(sha_output, num_zeroes_req)) {
            number_results += 1
            bitcoin = sha_input + "\t" + sha_output
            sender ! CheckforduplicateMaster(bitcoin, sha_input)
          }

          if (number_results > chunk) {
            keep_generating = false
            sender ! ChunkComplete()
          }

        }
      case End() =>
        context.stop(self)
    }
  }

  def hex_Digest(s: String): String = {
    val sha = MessageDigest.getInstance("SHA-256")

    sha.digest(s.getBytes).foldLeft("")((s: String, b: Byte) =>
      s + Character.forDigit((b & 0xf0) >> 4, 16) + Character.forDigit(b & 0x0f, 16))
  }

  def generatenonce(number_chars: Int): String = {
    val random = new scala.util.Random(System.nanoTime)
    val string2 = new StringBuilder(number_chars)
    val string1 = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz"
    for (i <- 0 until number_chars) {
      string2.append(string1(random.nextInt(string1.length)))
    }
    string2.toString()
  }

  def checkForValidCoin(sha_output: String, num_zeroes_req: String): Boolean = {
    if (sha_output.startsWith(num_zeroes_req)) {
      return true
    }
    false
  }

  class RemoteWorker(ip_address: String) extends Actor {
    val server = context.actorSelection("akka.tcp://Server@" + ip_address + ":2325/user/Master")

    def receive = {
      case RegisteratServer() =>
        server ! RemoteWorkerAvailable()

      case WorkAllocated(chunk, zero_count) =>
        //println("work was allocated by master to me")
        val prefix: String = "sravya;"
        var sha_input: String = ""
        var sha_output: String = ""
        var number_results: Int = 0
        var num_zeroes_req: String = "0"
        var keep_generating = true
        var bitcoin: String = ""

        //Just generating the sequence for check
        //println("I am generating sequence for check")
        //println("zero count is : " + zero_count)
        for (i <- 0 until (zero_count - 1))
          num_zeroes_req = num_zeroes_req.concat("0")

        //println("no of zeroes is:" + num_zeroes_req)

        //println("now i will keep on generating")
        while (keep_generating) {
          sha_input = prefix.concat(generatenonce(15))
          sha_output = hex_Digest(sha_input)

          if (checkForValidCoin(sha_output, num_zeroes_req)) {
            number_results += 1
            bitcoin = sha_input + "\t" + sha_output
            //println("I am from remote")
            server ! CheckforduplicateMaster(bitcoin, sha_input)
          }

          if (number_results >= chunk) {
            keep_generating = false
            server ! ChunkComplete()
          }
        }

      case End() =>
        context.stop(self)
    }
  }

}