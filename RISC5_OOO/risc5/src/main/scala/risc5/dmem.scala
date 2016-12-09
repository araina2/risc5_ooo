
package risc5

import Chisel._

class dmem(addr_width: Int, data_width: Int) extends Module {

  val io = new Bundle {
    val din  = UInt(INPUT, data_width)
    val addr  = UInt(INPUT, addr_width)
    val en = Bool(INPUT)
    val wr = Bool(INPUT)
    val TAG_in = UInt(INPUT,7) 
    val dout = UInt(OUTPUT, data_width)
    val valid = Bool(OUTPUT)
    val busy = Bool(OUTPUT)
    val TAG_out = UInt(OUTPUT,7) 
  }

  val mem = Mem(UInt(width=data_width), 1<<addr_width)
  val rand = LFSR16()
  val dout = Reg(init=UInt(0, data_width))
  val TAG_out = Reg(init=UInt(0, 7))
  val valid = Reg(init=Bool(false))
  val stall = Reg(init=UInt(0, 2))

  // This is to perform the write operations on the memory module
  when(io.en && io.wr && stall===UInt(0)) {
    mem(io.addr) := io.din
  }
 // Perform a read operation and if rand is greate than UInt(16384) set the stall to 3
  when(io.en && !io.wr && rand >= UInt(16384) && stall===UInt(0)) {
    stall := UInt(3)
  } .elsewhen (stall > UInt(0)) {
    stall := stall - UInt(1)
  }

  when(io.en && !io.wr && stall===UInt(0)) {
    dout := mem(io.addr)(data_width-1,0)
    TAG_out := io.TAG_in
    valid := Bool(true)
  } .elsewhen (stall > UInt(0)) {
  } .otherwise {
    dout := UInt(0)
    valid := Bool(false)
    TAG_out := UInt(0)
  }

  io.dout := Mux(stall===UInt(0), dout, UInt(0))
  io.valid := Mux(stall===UInt(0), valid, Bool(false))
  io.TAG_out := Mux(stall===UInt(0), TAG_out, UInt(0))
  io.busy := stall != UInt(0)
}


class dmem_tests(c: dmem) extends Tester(c) {

  poke(c.io.addr, 12)

  for (i <- 0 until 30) {
    poke(c.io.en, rnd.nextInt(2))
    poke(c.io.wr, rnd.nextInt(2))
    poke(c.io.din, rnd.nextInt(1024))
    poke(c.io.TAG_in, rnd.nextInt(128))

    peek(c.io.dout)
    peek(c.io.busy)
    peek(c.io.valid)
    peek(c.io.TAG_out)

    step(1)
  }
}

class dmem_TestGenerator extends TestGenerator {
  def genMod(): Module = Module(new dmem(64,64))
  def genTest[T <: Module](c: T): Tester[T] = 
    (new dmem_tests(c.asInstanceOf[dmem])).asInstanceOf[Tester[T]]
}

