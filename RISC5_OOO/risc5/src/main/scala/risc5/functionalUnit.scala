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
		// Register Input
		val result = Reg(UInt())

		val valueA = UInt()
		val valueB = UInt()
		val rawResult = UInt()	
		
		valueA := io.issueSourceValA
	when (io.issueType === UInt(0x0)){
		//Use R values
		//valueA := io.issueSourceValA
		valueB := io.issueSourceValB


	}
	.elsewhen (io.issueType === UInt(0x1)){
		//Use I Values
	//	valueA := io.issueSourceValA
		valueB := io.issueImm
	}
	
	.elsewhen (io.issueType === UInt(0x2)){
		//Use S Values
	//	valueA := io.issueSourceValA
		valueB := io.issueSourceValB
	}

	.elsewhen (io.issueType === UInt(0x3)){
		//Use SB Values
	//	valueA := io.issueSourceValA
		valueB := io.issueSourceValB
	}
		
	.elsewhen (io.issueType === UInt(0x4)){
		//Use U Values
	}
	
	.elsewhen (io.issueType === UInt(0x5)){
		//Use UJ Values
	}
	.otherwise{
		valueB := UInt(0x0)
	}
	
	val signedValueA = Cat(Fill(44, io.issueImm(19)),io.issueImm)	


/////////////////////////////////////////////////////////////////////
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
			result := valueA + valueB
		}

		.elsewhen(io.issueFunc3 === UInt("b010")){
		//SLTI
		//Set RD to 1 if Imm < RS (operands are signed)
		}

		.elsewhen(io.issueFunc3 === UInt("b011")){
		//SLTIU
		//Set RD to 1 if Imm < RS (operands are unsigned)
			when(valueB < valueA){
				result := UInt(0x1)
			}
			.otherwise{
				result := UInt(0x0)
			}
		}

		.elsewhen(io.issueFunc3 === UInt("b100")){
		//XORI
			result := valueA^valueB
				
		}

		.elsewhen(io.issueFunc3 === UInt("b110")){
		//ORI
		}

//				
		
		
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
	.elsewhen(io.issueFUOpcode === UInt(0x73)){

	}
	.otherwise{
	

	}
//	.elsewhen(io.issueFU


io.FUBroadcastValue := result

}

//////////////////////////////////////////////////////////////////




class FunctionalUnitTester(f:FunctionalUnit) extends Tester(f) {

	val sampleTag_addi = 0xB5A
	val expectedTag_addi = 0x5A
	
	val sampleOpcode_addi  = 0x13
	val sampleFunc3_addi   = 0x3
	val sampleImm_addi     = 0x1
	val sampleSourceA_addi = 0x3
	val sampleSourceB_addi = 0x5
	val sampleType_addi    = 0x1
	
	val expectedResult_addi = 0x1


	poke(f.io.issueDestTag, sampleTag_addi)
	poke(f.io.issueFUOpcode, sampleOpcode_addi)
	poke(f.io.issueFunc3, sampleFunc3_addi)
	poke(f.io.issueImm, sampleImm_addi)
	poke(f.io.issueSourceValA, sampleSourceA_addi)
	poke(f.io.issueSourceValB, sampleSourceB_addi)
	poke(f.io.issueType, sampleType_addi)

	step(1)

	expect(f.io.FUBroadcastTag, expectedTag_addi)
	expect(f.io.FUBroadcastValue, expectedResult_addi)


	 val sampleTag_xori = 0xB51
	 val expectedTag_xori = 0x51
	
	 val sampleOpcode_xori  = 0x13
	 val sampleFunc3_xori   = 0x4
	 val sampleImm_xori     = 0x3
	 val sampleSourceA_xori = (-0x1)
	 val sampleSourceB_xori = 0x5
	 val sampleType_xori    = 0x1
	
	val expectedResult_xori = (-0x4)


	poke(f.io.issueDestTag, sampleTag_xori)
	poke(f.io.issueFUOpcode, sampleOpcode_xori)
	poke(f.io.issueFunc3, sampleFunc3_xori)
	poke(f.io.issueImm, sampleImm_xori)
	poke(f.io.issueSourceValA, sampleSourceA_xori)
	poke(f.io.issueSourceValB, sampleSourceB_xori)
	poke(f.io.issueType, sampleType_xori)

	step(1)

	expect(f.io.FUBroadcastTag, expectedTag_xori)
	expect(f.io.FUBroadcastValue, expectedResult_xori)


}

class FunctionalModuleGenerator extends TestGenerator {
  def genMod(): Module = Module(new FunctionalUnit())
  def genTest[T <: Module](f: T): Tester[T] =
    (new FunctionalUnitTester(f.asInstanceOf[FunctionalUnit])).asInstanceOf[Tester[T]]
}

