package risc5

import Chisel._


class FetchModule extends Module {

  val io = new Bundle {
    // All INPUT ports
    val CacheInstruction0 = UInt(INPUT,64)
    val CacheInstruction1 = UInt(INPUT,64)
    val CacheInstruction2 = UInt(INPUT,64)
    val CacheInstruction3 = UInt(INPUT,64)
    val Stall = UInt(INPUT,1)
    val FUBranchMispredict = UInt(INPUT,1)
    val FUBranchMispredictAddress = UInt(INPUT,64)
    //FUBranchTaken = UInt(INPUT,1) //rnayar:do not need this
    val DecodeUpdateBTB = UInt(INPUT,64)
    val PC_Decode = UInt(INPUT,64) //rnayar:not sure
    val cache_hit = UInt(INPUT,1)

    // All OUPUT ports
    val PC = UInt(OUTPUT,64)
    val FetchInstruction0 = UInt(OUTPUT,64)
    val FetchInstruction1 = UInt(OUTPUT,64)
    val FetchInstruction2 = UInt(OUTPUT,64)
    val FetchInstruction3 = UInt(OUTPUT,64)
    val FetchBranchTakenTag0 = UInt(OUTPUT,1)
    val FetchBranchTakenTag1 = UInt(OUTPUT,1)
    val FetchBranchTakenTag2 = UInt(OUTPUT,1)
    val FetchBranchTakenTag3 = UInt(OUTPUT,1)
    val ROBTag0 = UInt(OUTPUT,7)
    val ROBTag1 = UInt(OUTPUT,7)
    val ROBTag2 = UInt(OUTPUT,7)
    val ROBTag3 = UInt(OUTPUT,7)
    val Valid = UInt(OUTPUT,1)
    
  }
  

  val r0 = Reg(init = UInt(0,width=64))
  val r1 = Reg(init = UInt(0,width=64))
  val r2 = Reg(init = UInt(0,width=64))
  val r3 = Reg(init = UInt(0,width=64))
  val nextPC = Reg(init = UInt(0,width=64)) // initialsed PC to ZERO , may need to set a fuction to initialise to some other value
  val FetchBranchTakenTag0 = Reg(init = UInt(0,width=1))
  val FetchBranchTakenTag1 = Reg(init = UInt(0,width=1))
  val FetchBranchTakenTag2 = Reg(init = UInt(0,width=1))
  val FetchBranchTakenTag3 = Reg(init = UInt(0,width=1))
  val ROBTag0 = Reg(init = UInt(0,width=7))
  val ROBTag1 = Reg(init = UInt(1,width=7))
  val ROBTag2 = Reg(init = UInt(2,width=7))
  val ROBTag3 = Reg(init = UInt(3,width=7))
  val ROBTag0_out = Reg(init = UInt(0,width=7))
  val ROBTag1_out = Reg(init = UInt(1,width=7))
  val ROBTag2_out = Reg(init = UInt(1,width=7))
  val ROBTag3_out = Reg(init = UInt(1,width=7))
  val Valid = Reg(init = UInt(1,width=1))
  // Store the four instructions passed to the fetch module
  when(reset) {
    r0 := UInt(0)
    r1 := UInt(0)
    r2 := UInt(0)
    r3 := UInt(0)
    nextPC := UInt(2123123)
    FetchBranchTakenTag0 := UInt(0)
    FetchBranchTakenTag1 := UInt(0)
    FetchBranchTakenTag2 := UInt(0)
    FetchBranchTakenTag3 := UInt(0)
    ROBTag0 := UInt(0)
    ROBTag1 := UInt(1)
    ROBTag2 := UInt(2)
    ROBTag3 := UInt(3)
    ROBTag0_out := UInt(0)
    ROBTag1_out := UInt(0)
    ROBTag2_out := UInt(0)
    ROBTag3_out := UInt(0)

  }
  .elsewhen((io.Stall === UInt(0)) && (io.cache_hit === UInt(1))) {
  r0 := io.CacheInstruction0
  r1 := io.CacheInstruction1
  r2 := io.CacheInstruction2
  r3 := io.CacheInstruction3
  nextPC := nextPC + UInt(16)
  ROBTag0_out := ROBTag0
  ROBTag1_out := ROBTag1
  ROBTag2_out := ROBTag2
  ROBTag3_out := ROBTag3
  ROBTag0 := ((ROBTag0 + UInt(4))%UInt(128))
  ROBTag1 := ((ROBTag1 + UInt(4))%UInt(128))
  ROBTag2 := ((ROBTag2 + UInt(4))%UInt(128))
  ROBTag3 := ((ROBTag3 + UInt(4))%UInt(128))
  }
  when (io.cache_hit === UInt(1)){
    Valid := UInt(1)
  }.otherwise { Valid := UInt(0)}
  when((io.Stall === UInt(1))){
    io.PC := UInt(0)
    io.FetchInstruction0 := UInt(0)
    io.FetchInstruction1 := UInt(0)
    io.FetchInstruction2 := UInt(0)
    io.FetchInstruction3 := UInt(0)
    io.FetchBranchTakenTag0 := UInt(0)
    io.FetchBranchTakenTag1 := UInt(0)
    io.FetchBranchTakenTag2 := UInt(0)
    io.FetchBranchTakenTag3 := UInt(0)
    io.ROBTag0 := UInt(0)
    io.ROBTag1 := UInt(0)
    io.ROBTag2 := UInt(0)
    io.ROBTag3 := UInt(0)
    io.Valid := UInt(0)
  }
  .otherwise{
  io.PC := nextPC
  io.FetchInstruction0 := r0
  io.FetchInstruction1 := r1
  io.FetchInstruction2 := r2
  io.FetchInstruction3 := r3
  io.FetchBranchTakenTag0 := FetchBranchTakenTag0
  io.FetchBranchTakenTag1 := FetchBranchTakenTag1
  io.FetchBranchTakenTag2 := FetchBranchTakenTag2
  io.FetchBranchTakenTag3 := FetchBranchTakenTag3
  io.ROBTag0 := Cat(UInt(0),ROBTag0_out)
  io.ROBTag1 := Cat(UInt(0),ROBTag1_out)
  io.ROBTag2 := Cat(UInt(0),ROBTag2_out)
  io.ROBTag3 := Cat(UInt(0),ROBTag3_out)
  io.Valid := Valid
  }
}

class FetchTester(f:FetchModule) extends Tester(f) {


  val rand0 = rnd.nextInt(4967295)
  val rand1 = rnd.nextInt(4967295)
  val rand2 = rnd.nextInt(4967295)
  val rand3 = rnd.nextInt(4967295)

  val inst0 = rand0
  val inst1 = rand1
  val inst2 = rand2
  val inst3 = rand3
  val stall =0
  val cache_hit=1
  val pc=peek(f.io.PC)
  for(t<-0 until 4){
   poke(f.io.CacheInstruction0,inst0)
   poke(f.io.CacheInstruction1,inst1)
   poke(f.io.CacheInstruction2,inst2)
   poke(f.io.CacheInstruction3,inst3)
   poke(f.io.Stall,stall)
   poke(f.io.cache_hit,cache_hit)
   step(1)
   expect(f.io.PC,(pc+(16*(t+1))))
   expect(f.io.FetchInstruction0,inst0)
   expect(f.io.FetchInstruction1,inst1)
   expect(f.io.FetchInstruction2,inst2)
   expect(f.io.FetchInstruction3,inst3)
   expect(f.io.Valid,1)
   peek(f.io.ROBTag0)
   peek(f.io.ROBTag1)
   peek(f.io.ROBTag2)
   peek(f.io.ROBTag3)

  }
  val rand0_1 = rnd.nextInt(4967295)
  val rand1_1 = rnd.nextInt(4967295)
  val rand2_1 = rnd.nextInt(4967295)
  val rand3_1 = rnd.nextInt(4967295)
  val inst0_1 = rand0_1
  val inst1_1 = rand1_1
  val inst2_1 = rand2_1
  val inst3_1 = rand3_1
  val stall1=1
  val cache_hit1=1
  val pc2 = peek(f.io.PC)
  val inst0_old = peek(f.io.FetchInstruction0)
  val inst1_old = peek(f.io.FetchInstruction1)
  val inst2_old = peek(f.io.FetchInstruction2)
  val inst3_old = peek(f.io.FetchInstruction3)
  
  for(t<-0 until 4){
   poke(f.io.CacheInstruction0,inst0_1)
   poke(f.io.CacheInstruction1,inst1_1)
   poke(f.io.CacheInstruction2,inst2_1)
   poke(f.io.CacheInstruction3,inst3_1)
   poke(f.io.Stall,stall1)
   poke(f.io.cache_hit,cache_hit1)
   step(1)
   expect(f.io.PC,0) // PC value should not change
   expect(f.io.FetchInstruction0,0) // still expect the oder instructions
   expect(f.io.FetchInstruction1,0) // still expect the oder instructions
   expect(f.io.FetchInstruction2,0) // still expect the oder instructions
   expect(f.io.FetchInstruction3,0) // still expect the oder instructions
   expect(f.io.Valid,0)
   peek(f.io.ROBTag0)
   peek(f.io.ROBTag1)
   peek(f.io.ROBTag2)
   peek(f.io.ROBTag3)

  }
  val rand0_2 = rnd.nextInt(4967295)
  val rand1_2 = rnd.nextInt(4967295)
  val rand2_2 = rnd.nextInt(4967295)
  val rand3_2 = rnd.nextInt(4967295)
  val inst0_2 = rand0_2
  val inst1_2 = rand1_2
  val inst2_2 = rand2_2
  val inst3_2 = rand3_2
  val stall2=0
  val cache_hit2=0
   poke(f.io.Stall,stall2)
   poke(f.io.cache_hit,cache_hit2)
  val pc3 = peek(f.io.PC)
  val inst0_old1 = peek(f.io.FetchInstruction0)
  val inst1_old1 = peek(f.io.FetchInstruction1)
  val inst2_old1 = peek(f.io.FetchInstruction2)
  val inst3_old1 = peek(f.io.FetchInstruction3)
  
  for(t<-0 until 4){
   //poke(f.io.CacheInstruction0,inst0_2)
   //poke(f.io.CacheInstruction1,inst1_2)
   //poke(f.io.CacheInstruction2,inst2_2)
   //poke(f.io.CacheInstruction3,inst3_2)
   step(1)
   expect(f.io.PC,(pc3))
   expect(f.io.FetchInstruction0,inst0_old1) // still expect the oder instructions
   expect(f.io.FetchInstruction1,inst1_old1) // still expect the oder instructions
   expect(f.io.FetchInstruction2,inst2_old1) // still expect the oder instructions
   expect(f.io.FetchInstruction3,inst3_old1) // still expect the oder instructions
   expect(f.io.Valid,0)
   peek(f.io.ROBTag0)
   peek(f.io.ROBTag1)
   peek(f.io.ROBTag2)
   peek(f.io.ROBTag3)

  }
  val rand0_3 = rnd.nextInt(4967295)
  val rand1_3 = rnd.nextInt(4967295)
  val rand2_3 = rnd.nextInt(4967295)
  val rand3_3 = rnd.nextInt(4967295)
  val inst0_3 = rand0_3
  val inst1_3 = rand0_3
  val inst2_3 = rand0_3
  val inst3_3 = rand0_3
  val stall3=1
  val cache_hit3=0
  val pc4 = peek(f.io.PC)
  val inst0_old2 = peek(f.io.FetchInstruction0)
  val inst1_old2 = peek(f.io.FetchInstruction1)
  val inst2_old2 = peek(f.io.FetchInstruction2)
  val inst3_old2 = peek(f.io.FetchInstruction3)
  
  for(t<-0 until 4){
   poke(f.io.CacheInstruction0,inst0_3)
   poke(f.io.CacheInstruction1,inst1_3)
   poke(f.io.CacheInstruction2,inst2_3)
   poke(f.io.CacheInstruction3,inst3_3)
   poke(f.io.Stall,stall3)
   poke(f.io.cache_hit,cache_hit3)
   step(1)
   expect(f.io.PC,0)
   expect(f.io.FetchInstruction0,0) // still expect the oder instructions
   expect(f.io.FetchInstruction1,0) // still expect the oder instructions
   expect(f.io.FetchInstruction2,0) // still expect the oder instructions
   expect(f.io.FetchInstruction3,0) // still expect the oder instructions
   expect(f.io.Valid,0)
   peek(f.io.ROBTag0)
   peek(f.io.ROBTag1)
   peek(f.io.ROBTag2)
   peek(f.io.ROBTag3)

  }
  val rand0_new = rnd.nextInt(4967295)
  val rand1_new = rnd.nextInt(4967295)
  val rand2_new = rnd.nextInt(4967295)
  val rand3_new = rnd.nextInt(4967295)

  val inst0_new = rand0
  val inst1_new = rand1
  val inst2_new = rand2
  val inst3_new = rand3
   poke(f.io.Stall,stall)
   poke(f.io.cache_hit,cache_hit)
  val pc5 = peek(f.io.PC)
  for(t<-0 until 4){
   poke(f.io.CacheInstruction0,inst0_new)
   poke(f.io.CacheInstruction1,inst1_new)
   poke(f.io.CacheInstruction2,inst2_new)
   poke(f.io.CacheInstruction3,inst3_new)
   poke(f.io.Stall,stall)
   poke(f.io.cache_hit,cache_hit)
   step(1)
   expect(f.io.PC,(pc5+(16*(t+1))))
   expect(f.io.FetchInstruction0,inst0_new)
   expect(f.io.FetchInstruction1,inst1_new)
   expect(f.io.FetchInstruction2,inst2_new)
   expect(f.io.FetchInstruction3,inst3_new)
   expect(f.io.Valid,1)
   peek(f.io.ROBTag0)
   peek(f.io.ROBTag1)
   peek(f.io.ROBTag2)
   peek(f.io.ROBTag3)

  }
}

class FetchModuleGenerator extends TestGenerator {
  def genMod(): Module = Module(new FetchModule())
  def genTest[T <: Module](f: T): Tester[T] = 
    (new FetchTester(f.asInstanceOf[FetchModule])).asInstanceOf[Tester[T]]
}
