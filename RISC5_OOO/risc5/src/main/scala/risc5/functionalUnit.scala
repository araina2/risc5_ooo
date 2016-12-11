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
		val issuePC          = UInt(INPUT, 64)

		//OUTPUTS
		val FUBroadcastValue = UInt(OUTPUT, 64)
		val FUBroadcastTag   = UInt(OUTPUT, 10)
		val FUBroadcastValid = UInt(OUTPUT, 1)
		val FUBusyBit        = UInt(OUTPUT, 1)

	}
		val valid = Reg(UInt(0))


		val destTag = Reg(next = io.issueDestTag)
		io.FUBroadcastTag := destTag
		// Register Input
		val result = Reg(UInt())

		val valueA = UInt()
		val valueB = UInt()
		val rawResult = UInt()//result before sign extension	
		val derp = SInt()
		val derp2 = SInt()		
		valueA := io.issueSourceValA
	when (io.issueType === UInt(0x0)){
		//Use R values
		valueA := io.issueSourceValA
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
	// Opcode = 0000011
	// Loads, not handled by functional units
	// Treat as NoOp because it shouldn't be here anyways
	valid := UInt(0x0)
	
	}
	.elsewhen(io.issueFUOpcode === UInt(0x37)){
		derp := io.issueImm << UInt(12)
		result := Cat(Fill(32, derp(31)), derp)
	}
	.elsewhen(io.issueFUOpcode === UInt(0x17)){
		derp := io.issueImm << UInt(12)
		result := Cat(Fill(32, derp(31)), derp) + io.issuePC
	}
	.elsewhen(io.issueFUOpcode === UInt(0x67)){
		// Opcode = 1100111
		// JALR: Jump And Link Register
		// Treat as NO OP, since we use perfect branch predictor
		valid := UInt(0x0)
	}
	.elsewhen(io.issueFUOpcode === UInt(0x6F)){
		// Opcode = 1101111
		// JAL: Jump And Link
		// Treat as NO OP, since we use perfect branch predictor
		valid := UInt(0x0)
	}
	.elsewhen(io.issueFUOpcode === UInt(0x63)){
		// Opcode = 1100011
		when(io.issueFunc3 === UInt(0x0)){
			// func3 = 000
			// BEQ: Branch Equal
			// Treat as NO OP, since we use perfect branch predictor
			valid := UInt(0x0)

		}
		.elsewhen(io.issueFunc3 === UInt(0x1)){
			// func3 = 001
			// BNE: Branch Not Equal
			// Treat as NO OP, since we use perfect branch predictor
			valid := UInt(0x0)

		}
		.elsewhen(io.issueFunc3 === UInt(0x4)){
			// func3 = 100
			// BLT: Branch Less Than
			// Treat as NO OP, since we use perfect branch predictor
			valid := UInt(0x0)

		}
		.elsewhen(io.issueFunc3 === UInt(0x5)){
			// func3 = 101
			// BGE: Branch Greater or Equal
			// Treat as NO OP, since we use perfect branch predictor
			valid := UInt(0x0)

		}
		.elsewhen(io.issueFunc3 === UInt(0x6)){
			// func3 = 110
			// BLTU: Branch Less Than or Unequal
			// Treat as NO OP, since we use perfect branch predictor
			valid := UInt(0x0)

		}
		.elsewhen(io.issueFunc3 === UInt(0x7)){
			// func3 = 111
			// BGEU: Branch Greater or Equal
			// Treat as NO OP, since we use perfect branch predictor
			valid := UInt(0x0)

		}

	}
	.elsewhen(io.issueFUOpcode === UInt(0x13)){ //matt
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
		
		//when(io.FUBroadcastValid === UInt(1)){
		//	valid   := UInt(0)
		//	destTag := UInt(0)
		//	result  := UInt(0)
		//}

		when(io.issueFunc3 === UInt("b000")){
		//ADDI
		//Add Value A and Value B
			result := valueA + valueB
			valid  := UInt(1)
		}

		.elsewhen(io.issueFunc3 === UInt("b010")){
		//SLTI
		//Set RD to 1 if Imm < RS (operands are signed)	
			when(valueB < valueA){
				result := UInt(0x1)
			}
			.otherwise{
				result := UInt(0x0)
			}
			valid := UInt(1)
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
			valid := UInt(1)
		}

		.elsewhen(io.issueFunc3 === UInt("b100")){
		//XORI
			result := valueA^valueB
			valid := UInt(1)	
		}

		.elsewhen(io.issueFunc3 === UInt("b110")){
		//ORI
			result := valueA | valueB
			valid  := UInt(1)
		}
		.elsewhen(io.issueFunc3 === UInt("b111")){
			result := valueA & valueB
			valid := UInt(1)
		}
	}
	.elsewhen(io.issueFUOpcode === UInt(0x33)){ //matt
	//R-Type
	//~~~~~~~~~~~~~~~~~~~~~~~~~~
	// f3  | INST  | f7
	//~~~~~~~~~~~~~~~~~~~~~~~~~~
	// 000 | ADD   | 0000000
	// 000 | SUB   | 0100000
	// 001 | SLL   | X
	// 010 | SLT   | X
	// 011 | SLTU  | X
	// 100 | XOR   | X
	// 101 | SRL   | 0000000
	// 101 | SRA   | 0100000
	// 110 | OR    | X
	// 111 | AND   | X
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~
		when((io.issueFunc3 === UInt("b000")) && (io.issueFunc7 === UInt(0x0))){
			result := valueA + valueB
			valid := UInt(1)
		}
		
		.elsewhen((io.issueFunc3 === UInt("b000")) && (io.issueFunc7 === UInt(0x20))){
			result := valueA - valueB
			valid := UInt(1)
		}
		.elsewhen(io.issueFunc3 === UInt("b001")){
			// SLL
			result := valueA << (valueB & UInt(0x1F))
			valid := UInt(1)
		}
		.elsewhen(io.issueFunc3 === UInt("b010")){
			//SLT
			when(valueB < valueA){
				result := UInt(0x1)
			}
			.otherwise{
				result := UInt(0x0)
			}
			valid := UInt(1)
		}
		.elsewhen(io.issueFunc3 === UInt("b011")){
			//SLTU
			when(valueB < valueA){
				result := UInt(0x1)
			}
			.otherwise{
				result := UInt(0x0)
			}
			valid := UInt(1)
		}
		.elsewhen(io.issueFunc3 === UInt("b100")){
			result := valueA^valueB
			valid := UInt(1)
		}
		.elsewhen(io.issueFunc3 === UInt("b101")){
			val shiftAmmnt = valueB & UInt(0x1F)
			when(io.issueFunc7 === UInt("b0000000")){
				//SRL
				result := (valueA >> shiftAmmnt) // & Cat(Fill(shiftAmmnt, 0x0), Fill(0x14 - shiftAmmnt, 0x1))
				valid := UInt(1)
			}
			.elsewhen(io.issueFunc7 === UInt("b0100000")){
				//SRA
		//		val derp = SInt()
				derp := valueA >> shiftAmmnt
				result := derp	
				valid := UInt(1)
			}
		}
		.elsewhen(io.issueFunc3 === UInt("b110")){
			result := valueA | valueB
			valid := UInt(1)
		}
		.elsewhen(io.issueFunc3 === UInt("b111")){
			result := valueA & valueB
			valid := UInt(1)
		}		
	}
	.elsewhen(io.issueFUOpcode === UInt(0x1B)){
	//I-Type

	}
	.elsewhen(io.issueFUOpcode === UInt(0x3B)){ //matt
	//R-Type
	//~~~~~~~~~~~~~~~~~~~~~~~~~
	// f3  | INST  | f7
	//~~~~~~~~~~~~~~~~~~~~~~~~~
	// 000 | ADDW  | 0000000
	// 000 | SUBW  | 0100000
	// 001 | SLLW  | X
	// 101 | SRLW  | 0000000
	// 101 | SRAW  | 0100000
	//~~~~~~~~~~~~~~~~~~~~~~~~~~
	
		when(io.issueFunc3 === UInt("b000")){
			when(io.issueFunc7 === UInt("b0000000")){
			//ADDW
			derp := valueA + valueB
			result := Cat(Fill(32, derp(31)), derp)
			}
			.elsewhen(io.issueFunc7 === UInt("b0100000")){
			//SUBW
			derp := valueA - valueB
			result := Cat(Fill(32, derp(31)), derp)
			}
			valid := UInt(1)
		}
		.elsewhen(io.issueFunc3 === UInt("b001")){
		//SLLW
			derp := valueA << (valueB & UInt(0x1F))
			result := Cat(Fill(32, derp(31)), derp)
			valid := UInt(1)
	
		}
		.elsewhen(io.issueFunc3 === UInt("b101")){
			val shiftAmmnt = valueB & UInt(0x1F)
			when(io.issueFunc7 === UInt("b0000000")){
				// SRLW
				derp := (valueA >> shiftAmmnt) 
				result := Cat(Fill(32, derp(31)), derp)
				valid := UInt(1)
	
			}
			.elsewhen(io.issueFunc7 === UInt("b0100000")){
				// SRAW
			//	val derp = SInt()
				derp2 := valueA >> shiftAmmnt
					
				result := Cat(Fill(32, derp2(31)), derp2)
				valid := UInt(1)
			}		
		}		

	}	
	.elsewhen(io.issueFUOpcode === UInt(0x73)){ //matt
	//this is a NOOP
		valid := UInt(0)
	}

	.otherwise{
		valid := UInt(0)	
		derp := UInt(0)
		derp2 := UInt(0)
	}



io.FUBroadcastValue := result
io.FUBroadcastValid := valid

}

//////////////////////////////////////////////////////////////////




class FunctionalUnitTester(f:FunctionalUnit) extends Tester(f) {


/////////////////////////////////////////
//		OPCODE 0x13
/////////////////////////////////////////

//////////////////ADDI///////////////////

	val sampleTag_addi = 0xB5A
	val expectedTag_addi = 0xB5A
	
	val sampleOpcode_addi  = 0x13
	val sampleFunc3_addi   = 0x0
	val sampleImm_addi     = 0x1
	val sampleSourceA_addi = 0x3
	val sampleSourceB_addi = 0x5
	val sampleType_addi    = 0x1
	
	val expectedResult_addi = 0x4


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

//////////////////////XORI///////////////////

	 val sampleTag_xori = 0xB51
	 val expectedTag_xori = 0xB51
	
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


//////////////////////SLTIU////////////////////	

	val sampleTag_sltiu = 0xB52
	val expectedTag_sltiu = 0xB52
	
	val sampleOpcode_sltiu  = 0x13
	val sampleFunc3_sltiu   = 0x3
	val sampleImm_sltiu     = 0x1
	val sampleSourceA_sltiu = 0x3
	val sampleSourceB_sltiu = 0x5
	val sampleType_sltiu    = 0x1
	
	val expectedResult_sltiu = 0x1


	poke(f.io.issueDestTag, sampleTag_sltiu)
	poke(f.io.issueFUOpcode, sampleOpcode_sltiu)
	poke(f.io.issueFunc3, sampleFunc3_sltiu)
	poke(f.io.issueImm, sampleImm_sltiu)
	poke(f.io.issueSourceValA, sampleSourceA_sltiu)
	poke(f.io.issueSourceValB, sampleSourceB_sltiu)
	poke(f.io.issueType, sampleType_sltiu)

	step(1)

	expect(f.io.FUBroadcastTag, expectedTag_sltiu)
	expect(f.io.FUBroadcastValue, expectedResult_sltiu)
	expect(f.io.FUBroadcastValid, 0x1)

/////////////////////////////////////////////
//making sure that valid only is high for 1 cycle
////////////////////////////////////////////

	//this is garbage
	poke(f.io.issueSourceValA, 0x3)
	poke(f.io.issueSourceValB, 0x4)
	poke(f.io.issueFUOpcode, 0x77)
	poke(f.io.issueFunc3, 0x2)
	poke(f.io.issueFunc7, 0x3)
	poke(f.io.issueImm, 0x20)
	poke(f.io.issueType, 0x2)
	poke(f.io.issueDestTag, 0x8)
	poke(f.io.issueFull, 0x1)
	poke(f.io.issueValid, 0x1)

	step(1)

	expect(f.io.FUBroadcastValid, 0x0)




////////////////////////////////////////
//		OPCODE 0x67
////////////////////////////////////////


	// Tyler Testing
	// JAL
	poke(f.io.issueSourceValA, 0x46)
	poke(f.io.issueSourceValB, 0x47)
	poke(f.io.issueFUOpcode, 0x67)
	poke(f.io.issueFunc3, 0x2)
	poke(f.io.issueFunc7, 0x3)
	poke(f.io.issueImm, 0x20)
	poke(f.io.issueType, 0x2)
	poke(f.io.issueDestTag, 0x9)
	poke(f.io.issueFull, 0x1)
	poke(f.io.issueValid, 0x1)

	step(1)

	expect(f.io.FUBroadcastValid, 0x0)

//////////////////////////////////////////
//		OPCODE 0x6F
//////////////////////////////////////////

	// JALR
	poke(f.io.issueSourceValA, 0x46)
	poke(f.io.issueSourceValB, 0x47)
	poke(f.io.issueFUOpcode, 0x6F)
	poke(f.io.issueFunc3, 0x2)
	poke(f.io.issueFunc7, 0x3)
	poke(f.io.issueImm, 0x20)
	poke(f.io.issueType, 0x2)
	poke(f.io.issueDestTag, 0x9)
	poke(f.io.issueFull, 0x1)
	poke(f.io.issueValid, 0x1)

	step(1)

	expect(f.io.FUBroadcastValid, 0x0)
	
	// BEQ
	poke(f.io.issueSourceValA, 0x46)
	poke(f.io.issueSourceValB, 0x47)
	poke(f.io.issueFUOpcode, 0x6F)
	poke(f.io.issueFunc3, 0x0)
	poke(f.io.issueFunc7, 0x3)
	poke(f.io.issueImm, 0x20)
	poke(f.io.issueType, 0x2)
	poke(f.io.issueDestTag, 0x9)
	poke(f.io.issueFull, 0x1)
	poke(f.io.issueValid, 0x1)

	step(1)


	expect(f.io.FUBroadcastValid, 0x0)
//////////////////////////////////////////////////////////
	val sampleTag_xori2 = 0x869
	val expectedTag_xori2 = 0x869
	
	val sampleOpcode_xori2  = 0x13
	val sampleFunc3_xori2   = 0x4
	val sampleImm_xori2     = 0x3
	val sampleSourceA_xori2 = (-0x1)
	val sampleSourceB_xori2 = 0x5
	val sampleType_xori2    = 0x1
	
	val expectedResult_xori2 = (-0x4)


	poke(f.io.issueDestTag, sampleTag_xori2)
	poke(f.io.issueFUOpcode, sampleOpcode_xori2)
	poke(f.io.issueFunc3, sampleFunc3_xori2)
	poke(f.io.issueFunc7, 0x0)
	poke(f.io.issueImm, sampleImm_xori2)
	poke(f.io.issueSourceValA, sampleSourceA_xori2)
	poke(f.io.issueSourceValB, sampleSourceB_xori2)
	poke(f.io.issueType, sampleType_xori2)

	step(1)

	expect(f.io.FUBroadcastTag, expectedTag_xori2)
	expect(f.io.FUBroadcastValue, expectedResult_xori2)
	expect(f.io.FUBroadcastValid, 0x1)



////////////////////////////////////////////////////////////
	
	// BNE
	poke(f.io.issueSourceValA, 0x46)
	poke(f.io.issueSourceValB, 0x47)
	poke(f.io.issueFUOpcode, 0x6F)
	poke(f.io.issueFunc3, 0x1)
	poke(f.io.issueFunc7, 0x3)
	poke(f.io.issueImm, 0x20)
	poke(f.io.issueType, 0x2)
	poke(f.io.issueDestTag, 0x9)
	poke(f.io.issueFull, 0x1)
	poke(f.io.issueValid, 0x1)

	step(1)

	expect(f.io.FUBroadcastValid, 0x0)
	
	// BLT
	poke(f.io.issueSourceValA, 0x46)
	poke(f.io.issueSourceValB, 0x47)
	poke(f.io.issueFUOpcode, 0x6F)
	poke(f.io.issueFunc3, 0x4)
	poke(f.io.issueFunc7, 0x3)
	poke(f.io.issueImm, 0x20)
	poke(f.io.issueType, 0x2)
	poke(f.io.issueDestTag, 0x9)
	poke(f.io.issueFull, 0x1)
	poke(f.io.issueValid, 0x1)

	step(1)

	expect(f.io.FUBroadcastValid, 0x0)
	
	// BGE
	poke(f.io.issueSourceValA, 0x46)
	poke(f.io.issueSourceValB, 0x47)
	poke(f.io.issueFUOpcode, 0x6F)
	poke(f.io.issueFunc3, 0x5)
	poke(f.io.issueFunc7, 0x3)
	poke(f.io.issueImm, 0x20)
	poke(f.io.issueType, 0x2)
	poke(f.io.issueDestTag, 0x9)
	poke(f.io.issueFull, 0x1)
	poke(f.io.issueValid, 0x1)

	step(1)

	expect(f.io.FUBroadcastValid, 0x0)
	
	// BLTU
	poke(f.io.issueSourceValA, 0x46)
	poke(f.io.issueSourceValB, 0x47)
	poke(f.io.issueFUOpcode, 0x6F)
	poke(f.io.issueFunc3, 0x6)
	poke(f.io.issueFunc7, 0x3)
	poke(f.io.issueImm, 0x20)
	poke(f.io.issueType, 0x2)
	poke(f.io.issueDestTag, 0x9)
	poke(f.io.issueFull, 0x1)
	poke(f.io.issueValid, 0x1)

	step(1)

	expect(f.io.FUBroadcastValid, 0x0)
	
	// BGEU
	poke(f.io.issueSourceValA, 0x46)
	poke(f.io.issueSourceValB, 0x47)
	poke(f.io.issueFUOpcode, 0x6F)
	poke(f.io.issueFunc3, 0x7)
	poke(f.io.issueFunc7, 0x3)
	poke(f.io.issueImm, 0x20)
	poke(f.io.issueType, 0x2)
	poke(f.io.issueDestTag, 0x9)
	poke(f.io.issueFull, 0x1)
	poke(f.io.issueValid, 0x1)

	step(1)

	expect(f.io.FUBroadcastValid, 0x0)

////////////////////////////////////////////
//		OPCODE 0x3
////////////////////////////////////////////
	
	// LOADS
	poke(f.io.issueSourceValA, 0x46)
	poke(f.io.issueSourceValB, 0x47)
	poke(f.io.issueFUOpcode, 0x3)
	poke(f.io.issueFunc3, 0x7)
	poke(f.io.issueFunc7, 0x3)
	poke(f.io.issueImm, 0x20)
	poke(f.io.issueType, 0x2)
	poke(f.io.issueDestTag, 0x9)
	poke(f.io.issueFull, 0x1)
	poke(f.io.issueValid, 0x1)

	step(1)

	expect(f.io.FUBroadcastValid, 0x0)

	// STORES
	poke(f.io.issueSourceValA, 0x46)
	poke(f.io.issueSourceValB, 0x47)
	poke(f.io.issueFUOpcode, 0x23)
	poke(f.io.issueFunc3, 0x7)
	poke(f.io.issueFunc7, 0x3)
	poke(f.io.issueImm, 0x20)
	poke(f.io.issueType, 0x2)
	poke(f.io.issueDestTag, 0x9)
	poke(f.io.issueFull, 0x1)
	poke(f.io.issueValid, 0x1)

	step(1)

	expect(f.io.FUBroadcastValid, 0x0)

//////////////////////////////////////////
//		OPCODE 0x33
//////////////////////////////////////////

////////////////////XOR///////////////////

	 val sampleTag_xor = 0xB53
	 val expectedTag_xor = 0xB53
	
	 val sampleOpcode_xor  = 0x33
	 val sampleFunc3_xor   = 0x4
	 val sampleImm_xor     = 0x5
	 val sampleSourceA_xor = (-0x1)
	 val sampleSourceB_xor = 0x3
	 val sampleType_xor    = 0x0
	
	val expectedResult_xor = (-0x4)


	poke(f.io.issueDestTag, sampleTag_xor)
	poke(f.io.issueFUOpcode, sampleOpcode_xor)
	poke(f.io.issueFunc3, sampleFunc3_xor)
	poke(f.io.issueImm, sampleImm_xor)
	poke(f.io.issueSourceValA, sampleSourceA_xor)
	poke(f.io.issueSourceValB, sampleSourceB_xor)
	poke(f.io.issueType, sampleType_xor)

	step(1)

	expect(f.io.FUBroadcastTag, expectedTag_xor)
	expect(f.io.FUBroadcastValue, expectedResult_xor)

	// SRL
	poke(f.io.issueDestTag, 0x1)
	poke(f.io.issueFUOpcode, 0x33)
	poke(f.io.issueFunc3, 0x5)
	poke(f.io.issueImm, 0x0)
	poke(f.io.issueSourceValA, 0x1D)
	poke(f.io.issueSourceValB, 0xE1)
	poke(f.io.issueType, 0x1)
	poke(f.io.issueFunc7, 0x0)
	step(1)

	expect(f.io.FUBroadcastValue, 0xE)

	// SRA
	poke(f.io.issueDestTag, 0x1)
	poke(f.io.issueFUOpcode, 0x33)
	poke(f.io.issueFunc3, 0x5)
	poke(f.io.issueImm, 0x0)
	poke(f.io.issueSourceValA, 0x1D)
	poke(f.io.issueSourceValB, 0xE1)
	poke(f.io.issueType, 0x1)
	poke(f.io.issueFunc7, 0x20)
	step(1)

	expect(f.io.FUBroadcastValue, 0xE)

	// ADDW
	poke(f.io.issueDestTag, 0x1)
	poke(f.io.issueFUOpcode, 0x3B)
	poke(f.io.issueFunc3, 0x0)
	poke(f.io.issueImm, 0x0)
	poke(f.io.issueSourceValA, 0xE)
	poke(f.io.issueSourceValB, 0xF)
	poke(f.io.issueType, 0x1)
	poke(f.io.issueFunc7, 0x0)
	step(1)

	expect(f.io.FUBroadcastValue, 0x1D)

	// SUBW
	poke(f.io.issueDestTag, 0x1)
	poke(f.io.issueFUOpcode, 0x3B)
	poke(f.io.issueFunc3, 0x0)
	poke(f.io.issueImm, 0x0)
	poke(f.io.issueSourceValA, 0x5)
	poke(f.io.issueSourceValB, 0x1)
	poke(f.io.issueType, 0x1)
	poke(f.io.issueFunc7, 0x20)
	step(1)

	expect(f.io.FUBroadcastValue, 0x4)

	// SLLW
	poke(f.io.issueDestTag, 0x1)
	poke(f.io.issueFUOpcode, 0x3B)
	poke(f.io.issueFunc3, 0x1)
	poke(f.io.issueImm, 0x0)
	poke(f.io.issueSourceValA, 0x3FFFFFFF)
	poke(f.io.issueSourceValB, 0x81)
	poke(f.io.issueType, 0x1)
	poke(f.io.issueFunc7, 0x0)
	step(1)

	expect(f.io.FUBroadcastValue, 0x7FFFFFFE)

	// SRLW
	poke(f.io.issueDestTag, 0x1)
	poke(f.io.issueFUOpcode, 0x3B)
	poke(f.io.issueFunc3, 0x5)
	poke(f.io.issueImm, 0x0)
	poke(f.io.issueSourceValA, 0xFFFFFFFE)
	poke(f.io.issueSourceValB, 0x81)
	poke(f.io.issueType, 0x1)
	poke(f.io.issueFunc7, 0x0)
	step(1)

	expect(f.io.FUBroadcastValue, 0xFFFFFFFF)

	// SRAW
	poke(f.io.issueDestTag, 0x1)
	poke(f.io.issueFUOpcode, 0x3B)
	poke(f.io.issueFunc3, 0x5)
	poke(f.io.issueImm, 0x0)
	poke(f.io.issueSourceValA, 0x7FFFFFFE)
	poke(f.io.issueSourceValB, 0x81)
	poke(f.io.issueType, 0x1)
	poke(f.io.issueFunc7, 0x20)
	step(1)

	expect(f.io.FUBroadcastValue, 0x3FFFFFFF)
}

class FunctionalModuleGenerator extends TestGenerator {
  def genMod(): Module = Module(new FunctionalUnit())
  def genTest[T <: Module](f: T): Tester[T] =
    (new FunctionalUnitTester(f.asInstanceOf[FunctionalUnit])).asInstanceOf[Tester[T]]
}

