package chip;

import Chisel._


class topLevel extends Module {

	/* Theoretical Cache code
	val cacheio = new Bundle {

		val reqAddr = UInt(INPUT, 32)
		val s1_ppn = UInt(INPUT, 1)
		val s1_kill = UInt(INPUT, 1)
		val s2_kill = UInt(INPUT, 1)
		val invalid = UInt(INPUT, 1)

		val instruction0 = UINT(OUTPUT, 32)
		val instruction1 = UINT(OUTPUT, 32)
		val instruction2 = UINT(OUTPUT, 32)
		val instruction3 = UINT(OUTPUT, 32)

	}
	
	val cache = Module(new pCache())
	TODO:  worry about the ins and outs here later
	*/

	val fetchio = new Bundle {
		
		//Inputs for fetch module

		val cacheinstruction0 = UInt(INPUT, 32)
		val cacheinstruction1 = UInt(INPUT, 32)
		val cacheinstruction2 = UInt(INPUT, 32)
		val cacheinstruction3 = UInt(INPUT, 32)

		val stall = UInt(INPUT, 1)
		val fuBranchMispredict = UInt(INPUT, 1)
		val fuBranchMispredictAddr = UInt(INPUT, 32)
		val fuBranchTaken = UInt(INPUT, 1)
		
		/*
		val decode_update_BTB = UInt(INPUT, 32)
		val pc_from_decode = UInt(INPUT, )
		*/
		
	       val cacheHit = UInt(INPUT, 1)



	       //Outputs for fetch
	       val PC_cache = UInt(OUTPUT, 32)
	       val fetchInstruction0 = UInt(OUTPUT, 32)
	       val fetchInstruction1 = UInt(OUTPUT, 32)
	       val fetchInstruction2 = UInt(OUTPUT, 32)
	       val fetchInstruction3 = UInt(OUTPUT, 32)
		
	       val fetchBranchTakenTag0 = UInt(OUTPUT, 1)
	       val fetchBranchTakenTag1 = UInt(OUTPUT, 1)
	       val fetchBranchTakenTag2 = UInt(OUTPUT, 1)
	       val fetchBranchTakenTag3 = UInt(OUTPUT, 1)
	
	       val ROBTag0 = UInt(OUTPUT, 8)
	       val ROBTag1 = UInt(OUTPUT, 8)
	       val ROBTag2 = UInt(OUTPUT, 8)
	       val ROBTag3 = UInt(OUTPUT, 8)
	
	       val valid = UInt(OUTPUT, 1)
	}

	//TODO: Write the module instantiation for Fetch





}
