//hey tyler, this is a message from Matt
//Follow these instructions to modify this file in order to test git branches
//0. use this command to make sure your branch is up to date:
//     git pull
//1. Make the changes you want to the file you want and save the file
//2. commit the file like this: 
//    git commit -m 'a description of the changes that you made'
//3. push the changes to git hub like this:
//    git push
//
//
// Let me know if you get any error messages

// ONE MORE CHANGE


package risc5

import Chisel._

class FunctionalUnit extends Module {

	val io = new Bundle {

		//INPUTS
		val issueSourceValA  = UInt(INPUT, 64)
		val issueSourceValB  = UInt(INPUT, 64)
		val issueFUOpcode    = UInt(INPUT, 7)
		val issueFunc3       = UInt(INPUT, 3)
		val issueFunc7       = UInt(INPUT, 7)
                val issueImm         = UInt(INPUT, 20)
		val issueType        = UInt(INPUT, 3)
		val issueDestTag     = UInt(INPUT, 10)
		val issueFull        = UInt(INPUT, 1)
		val issueValid       = UInt(INPUT, 1)

		//OUTPUTS
		val FUBroadcastValue = UInt(OUTPUT, 64)
		val FUBroadcastTag   = UInt(OUTPUT, 7)
		val FUBroadcastValid = UInt(OUTPUT, 1)

	}

		val destTag = Reg(next = io.issueDestTag(6,0))
		io.FUBroadcastTag := destTag

		val result = Reg(UInt())
		val valueA = UInt()
		val valueB = UInt()

	when (io.issueType === UInt(0x0)){
		//Use R values
		valueA := io.issueSourceValA
		valueB := io.issueSourceValB


	}
	.elsewhen (io.issueType === UInt(0x1)){
		//Use I Values
		valueA := io.issueSourceValA
		valueB := io.issueImm
	}
	
	.elsewhen (io.issueType === UInt(0x2)){
		//Use S Values
		valueA := io.issueSourceValA
		valueB := io.issueSourceValB
	}

	.elsewhen (io.issueType === UInt(0x3)){
		//Use SB Values
		valueA := io.issueSourceValA
		valueB := io.issueSourceValB
	}
		
	.elsewhen (io.issueType === UInt(0x4)){
		//Use U Values
	}
	
	.elsewhen (io.issueType === UInt(0x5)){
		//Use UJ Values
	}

	
	when(io.issueFUOpcode === UInt(0x03)){
		
	
	}	
	.elsewhen(io.issueFUOpcode === UInt(0x13)){
	//I Type
        //~~~~~~~~~~~~~~~~~~~~~~
	// f3  |  INST| f7
	//~~~~~~~~~~~~~~~~~~~~~~   
	// 000 | ADDI |X
	// 010 | SLTI |X
	// 011 | SLTIU|X
	// 100 | XORI |X
	// 110 | ORI  |X
	// 111 | ANDI |X
	// 001 | SLLI | 0000000
	// 101 | SRLI | 0000000
	// 101 | SRAI | 0100000
	//~~~~~~~~~~~~~~~~~~~~~~~~
		
		when(io.issueFunc3 === UInt("b000")){
		//ADDI
		//Add Value A and Value B

		}

		.elsewhen(io.issueFunc3 === UInt("b010")){
		//SLTI
		//Set RD to 1 if Imm < RS (operands are signed)
		}

		.elsewhen(io.issueFunc3 === UInt("b011")){
		//SLTIU
		//Set RD to 1 if Imm < RS (operands are unsigned)
		}

		.elsewhen(io.issueFunc3 === UInt("b100")){
		//XORI
		}

		.elsewhen(io.issueFunc3 === UInt("b110")){
		//ORI
		}

//		.elsewhen(io.issueFunc3		
		
		
	}
	.elsewhen(io.issueFUOpcode === UInt(0x33)){
	//R-Type
	//~~~~~~~~~~~~~~~~~~~~~~~~~~
	//
	

	}
	.elsewhen(io.issueFUOpcode === UInt(0x1B)){
	//I-Type

	}
	.elsewhen(io.issueFUOpcode === UInt(0x3B)){
	//R-Type

	}

}






class FunctionalUnitTester(f:FunctionalUnit) extends Tester(f) {

	val sampleTag = 0xB5A
	val expectedTag = 0x5A

	poke(f.io.issueDestTag, sampleTag)
	step(1)
	expect(f.io.FUBroadcastTag, expectedTag)


}

class FunctionalModuleGenerator extends TestGenerator {
  def genMod(): Module = Module(new FunctionalUnit())
  def genTest[T <: Module](f: T): Tester[T] =
    (new FunctionalUnitTester(f.asInstanceOf[FunctionalUnit])).asInstanceOf[Tester[T]]
}

