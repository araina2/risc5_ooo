
package risc5

import Chisel._
import java.nio.{ByteBuffer,ByteOrder}
import java.nio.file.{Files, Paths}

class instruction_rom(val n: Int) extends Module {

  require(n > 0 && n <= 4)

  val io = new Bundle {
    //val pc = UInt(OUTPUT, 64*n)
    val stall = Bool(INPUT)
    val FetchInstruction0 = UInt(OUTPUT, 32)
    val FetchInstruction1 = UInt(OUTPUT, 32)
    val FetchInstruction2 = UInt(OUTPUT, 32)
    val FetchInstruction3 = UInt(OUTPUT, 32)
    
    val FetchROBTag0 = UInt(OUTPUT, 8)
    val FetchROBTag1 = UInt(OUTPUT, 8)
    val FetchROBTag2 = UInt(OUTPUT, 8)
    val FetchROBTag3 = UInt(OUTPUT, 8)
    val FetchValid = UInt(OUTPUT,1)
    //val inst = UInt(OUTPUT, 32*n)
  }

  //Initializing the FetchROBTag
  io.FetchROBTag0 := UInt(0)
  io.FetchROBTag1 := UInt(0)
  io.FetchROBTag2 := UInt(0)
  io.FetchROBTag3 := UInt(0)

  val index = Reg(init=UInt(0, width=64))
  
  val pc_rom = Module(new Rom(makeROM("/tmp/pc.bin"), n, 64))
  val inst_rom = Module(new Rom(makeROM("/tmp/inst.bin"), n, 32))

  //printf ("The value of index is %d",index)
  pc_rom.io.addr := index
  //io.pc := pc_rom.io.data

  inst_rom.io.addr := index
  
  io.FetchInstruction0 := (inst_rom.io.data) //& UInt(0xffffffff)
  io.FetchInstruction1 := (inst_rom.io.data >> 32) //& UInt(0xffffffff)
  io.FetchInstruction2 := (inst_rom.io.data >> 64) //& UInt(0xffffffff)
  io.FetchInstruction3 := (inst_rom.io.data >> 96) //& UInt(0xffffffff)
  
  //io.inst := inst_rom.io.data

  when(!io.stall) {
    io.FetchROBTag0 := (index + UInt(1)) % UInt(128)
    io.FetchROBTag1 := (index + UInt(2)) % UInt(128)
    io.FetchROBTag2 := (index + UInt(3)) % UInt(128)
    io.FetchROBTag3 := (index + UInt(4)) % UInt(128)
    index := index + UInt(n)
  }
  
  def makeROM(file: String) = {
    val romdata = Files.readAllBytes(Paths.get(file))
    val rom = ByteBuffer.wrap(romdata)

    rom.order(ByteOrder.LITTLE_ENDIAN)
    rom.array()

  }
  when (io.stall) {
    io.FetchValid := UInt(0)
  }
  .otherwise {
    io.FetchValid := UInt(1)
  }
}

class Rom(contents: Seq[Byte], n: Int, w:Int) extends Module {

  val io = new Bundle {
    val addr = UInt(INPUT, w)
    val data = UInt(OUTPUT, w*n)
  }

  val byteWidth = 8
  val rows = contents.size/byteWidth
  val rom = Vec.tabulate(rows) { i =>
    val slice = contents.slice(i*byteWidth, (i+1)*byteWidth)
    UInt(slice.foldRight(BigInt(0)) { case (x,y) => (y << 8) + (x.toInt & 0xFF) }, w)
  }

  val out = UInt(width=w*n);

  if (n == 1) { out := rom(io.addr) }
  if (n == 2) { out := Cat(rom(io.addr+UInt(1)), rom(io.addr)) }
  if (n == 3) { out := Cat(rom(io.addr+UInt(2)), rom(io.addr+UInt(1)), rom(io.addr)) }
  if (n == 4) { out := Cat(rom(io.addr+UInt(3)), rom(io.addr+UInt(2)), rom(io.addr+UInt(1)), rom(io.addr)) }

  io.data := out
}


class instruction_rom_tests(c: instruction_rom) extends Tester(c) {

  for (i <- 0 until 20) {
    //poke(c.io.stall, 0)
    poke(c.io.stall, rnd.nextInt(2))
    //peek(c.io.pc)
    //peek(c.io.inst)
    peek(c.io.FetchInstruction0)
    peek(c.io.FetchInstruction1)
    peek(c.io.FetchInstruction2)
    peek(c.io.FetchInstruction3)
    peek(c.io.FetchROBTag0)
    peek(c.io.FetchROBTag1)
    peek(c.io.FetchROBTag2)
    peek(c.io.FetchROBTag3)
    peek(c.io.FetchValid)
    step(1)
  }
}

class instruction_rom_generator extends TestGenerator {
    def genMod(): Module = Module(new instruction_rom(4))
      def genTest[T <: Module](c: T): Tester[T] =
            (new instruction_rom_tests(c.asInstanceOf[instruction_rom])).asInstanceOf[Tester[T]]
}
