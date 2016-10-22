package Hello
import Chisel._
class HelloModule extends Module {
val io = new Bundle {
val out = UInt(OUTPUT, 8)
}
io.out := UInt(42)
}

class HelloModuleTests(c: HelloModule)
extends Tester(c) {
step(1)
expect(c.io.out, 42)
}

object hello {
def main(args: Array[String]): Unit = {
val margs =
Array("--backend", "c", "--genHarness",
"--compile", "--test")
chiselMainTest(margs, () => Module(new HelloModule())) { 
c => new HelloModuleTests(c)
}
}
}
