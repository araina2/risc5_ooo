package risc5
import scala.util.control.Breaks._
import Chisel._

// Replace all "Module_Name" to the name you want "x" is the instance used in Tester and TestGenerator
// also update the bashrc to export the $MODULE_GEN name
class Module_NameModule extends Module {

  val io = new Bundle {
    // All INPUT ports

    // All OUPUT ports
    
  }
 // Declare all the registers here 

  when(reset) {
  }
}

class Module_NameTester(x:Module_NameModule) extends Tester(x) {
// some random valus for testing
// SAMPLE:  val rand1 = rnd.nextInt(4967295)

}

class Module_NameGenerator extends TestGenerator {
  def genMod(): Module = Module(new Module_NameModule())
  def genTest[T <: Module](x: T): Tester[T] = 
    (new Module_NameTester(x.asInstanceOf[Module_NameModule])).asInstanceOf[Tester[T]]
}
