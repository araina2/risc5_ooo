package risc5

import Chisel._



class intRenameROB extends Module {

        /////////////////////////////////
        // Module INstantiation
        ///////////////////////////////////       
        val r = Module(new RegAliasTable())
        val rob = Module(new ROB())

        val io = new Bundle{

        val FUBroadcastValue0 = UInt(INPUT,64)
        val FUBroadcastTag0 = UInt(INPUT,10)
        val FUBroadcastValue1 = UInt(INPUT,64)
        val FUBroadcastTag1 = UInt(INPUT,10)
        val FUBroadcastValue2 = UInt(INPUT,64)
        val FUBroadcastTag2 = UInt(INPUT,10)
        val FUBroadcastValue3 = UInt(INPUT,64)
        val FUBroadcastTag3 = UInt(INPUT,10)
    
        //Tags from the load/store queue
        val LoadStoreDestVal0 = UInt(INPUT,64)
        val LoadStoreDestTag0 = UInt(INPUT,10)
       val LoadStoreDestReg0 = UInt(INPUT,5)
         
              val LoadStoreDestAddress0 = UInt(INPUT,64)


        val ROBStoreSelect0 = Bool(OUTPUT)
                 val ROBStoreSelect1 = Bool(OUTPUT)
                    val ROBStoreSelect2 = Bool(OUTPUT)
                       val ROBStoreSelect3 = Bool(OUTPUT)
                          val ROBMemAddress0 = UInt(OUTPUT,64) 
                             val ROBMemAddress1 = UInt(OUTPUT,64) 
                                val ROBMemAddress2 = UInt(OUTPUT,64) 
                                   val ROBMemAddress3 = UInt(OUTPUT,64) 
                                      val ROBValue0 = UInt(OUTPUT,64) 
                                         val ROBValue1 = UInt(OUTPUT,64) 
                                            val ROBValue2 = UInt(OUTPUT,64) 
                                               val ROBValue3 = UInt(OUTPUT,64) 
                                                  val ROBValueValid0 = UInt(OUTPUT,1) 
                                                     val ROBValueValid1 = UInt(OUTPUT,1) 
                                                        val ROBValueValid2 = UInt(OUTPUT,1)
                                                           val ROBValueValid3 = UInt(OUTPUT,1)
        }



       /* 
        Rename inputs required
        r.io.FUBroadcastValue0 
        r.io.FUBroadcastTag0 
        r.io.FUBroadcastValue1 
        r.io.FUBroadcastTag1 
        r.io.FUBroadcastValue2 
        r.io.FUBroadcastTag2 
        r.io.FUBroadcastValue3 
        r.io.FUBroadcastTag3 
        r.io.LoadStoreDestVal0
        r.io.LoadStoreDestTag0
        r.io.IssueBroadcastFreeRow0
        r.io.IssueBroadcastFreeRow1
        r.io.IssueBroadcastFreeRow2 
        r.io.IssueBroadcastFreeRow3
        r.io.IssueFull0 
        r.io.IssueFull1 
        r.io.IssueFull2 
        r.io.IssueFull3 
        r.io.IssueFull 
        r.io.LoadStoreFull 
        r.io.LoadStoreBroadcastFreeTag0     
        r.io.FUBranchMispredict
        r.io.DecodeQueueSelect0 := d0.io.decodeQueueSelect 
        r.io.DecodeQueueSelect1 := d1.io.decodeQueueSelect 
        r.io.DecodeQueueSelect2 := d2.io.decodeQueueSelect 
        r.io.DecodeQueueSelect3 := d3.io.decodeQueueSelect 
       //pretty sure we need 4 of these?? commenting out 3 just in case...
        r.io.DecodeType :=  d0.io.decodeType
        r.io.DecodeType1 := d1.io.decodeType
        r.io.DecodeType2 :=  d2.io.decodeType
        r.io.DecodeType3 :=  d3.io.decodeType
        r.io.Decode_dest_0 := d0.io.decodeRd 
        r.io.Decode_dest_1 := d1.io.decodeRd 
        r.io.Decode_dest_2 := d2.io.decodeRd 
        r.io.Decode_dest_3 := d3.io.decodeRd 
        r.io.DecodeSource1_0 := d0.io.decodeRs1 
        r.io.DecodeSource1_1 := d1.io.decodeRs1 
        r.io.DecodeSource1_2 := d2.io.decodeRs1 
        r.io.DecodeSource1_3 := d3.io.decodeRs1 
        r.io.DecodeSource2_0 := d0.io.decodeRs2 
        r.io.DecodeSource2_1 := d1.io.decodeRs2 
        r.io.DecodeSource2_2 := d2.io.decodeRs2 
        r.io.DecodeSource2_3 := d3.io.decodeRs2 
        r.io.DecodeROBtag0 := d0.io.decodeRobTag 
        r.io.DecodeROBtag1 := d1.io.decodeRobTag 
        r.io.DecodeROBtag2 := d2.io.decodeRobTag 
        r.io.DecodeROBtag3 := d3.io.decodeRobTag 
        r.io.Decode_Imm_0 := d0.io.decode_Imm   
        r.io.Decode_Imm_1 := d1.io.decode_Imm 
        r.io.Decode_Imm_2 := d2.io.decode_Imm 
        r.io.Decode_Imm_3 := d3.io.decode_Imm 
        r.io.Decode_Opcode_0 := d0.io.decodeOpcode 
        r.io.Decode_Opcode_1 := d1.io.decodeOpcode 
        r.io.Decode_Opcode_2 := d2.io.decodeOpcode 
        r.io.Decode_Opcode_3 := d3.io.decodeOpcode 
        r.io.Decode_func3_0 := d0.io.decodeFunky3 
        r.io.Decode_func3_1 := d1.io.decodeFunky3 
        r.io.Decode_func3_2 := d2.io.decodeFunky3 
        r.io.Decode_func3_3 := d3.io.decodeFunky3 
        r.io.Decode_func7_0 := d0.io.decodeFunky7 
        r.io.Decode_func7_1 := d1.io.decodeFunky7 
        r.io.Decode_func7_2 := d2.io.decodeFunky7 
        r.io.Decode_func7_3 := d3.io.decodeFunky7 
        r.io.DecodeStoreSelect0 := d0.io.decodeIsStore 
        r.io.DecodeStoreSelect1 := d1.io.decodeIsStore 
        r.io.DecodeStoreSelect2 := d2.io.decodeIsStore 
        r.io.DecodeStoreSelect3 := d3.io.decodeIsStore 
        */


       /*
        Inputs to ROB not from Rename - have to be accounted for in tests
                FUBroadcasValue0,1,2,3
                FUBroadcastTag0,1,2,3
                LoadStoreDestVal0 
                LoadStoreDestTag0 
                LoadStoreDestReg0 
                LoadStoreDestAddress0 
        */


       ////////////////////////////////////
       //Assogmomh rename outs as ROB ins
       ////////////////////////////////////

        rob.io.RenameDestTag0 := r.io.RenameDestTag0 
        rob.io.RenameDestTag1 := r.io.RenameDestTag1 
        rob.io.RenameDestTag2 := r.io.RenameDestTag2 
        rob.io.RenameDestTag3 := r.io.RenameDestTag3 
        rob.io.Rename_dest_0 := r.io.Rename_dest_0
        rob.io.Rename_dest_1 := r.io.Rename_dest_1
        rob.io.Rename_dest_2 := r.io.Rename_dest_2
        rob.io.Rename_dest_3 := r.io.Rename_dest_3
        rob.io.RenameValid := r.io.RenameValid
        rob.io.RenameROBtag0 := r.io.RenameROBtag0
        rob.io.RenameROBtag1 :=r.io.RenameROBtag1
        rob.io.RenameROBtag2 := r.io.RenameROBtag2
        rob.io.RenameROBtag3 := r.io.RenameROBtag3
        rob.io.RenameQueueSelect0 := r.io.RenameQueueSelect0 
        rob.io.RenameQueueSelect1 := r.io.RenameQueueSelect1 
        rob.io.RenameQueueSelect2 := r.io.RenameQueueSelect2 
        rob.io.RenameQueueSelect3 := r.io.RenameQueueSelect3 
        rob.io.RenameStoreSelect0 := r.io.RenameStoreSelect0 
        rob.io.RenameStoreSelect1 := r.io.RenameStoreSelect1 
        rob.io.RenameStoreSelect2 := r.io.RenameStoreSelect2 
        rob.io.RenameStoreSelect3 := r.io.RenameStoreSelect3 
}


/*class intRR extends Tester{

//All pokes go to Decode and all expecteds come from RAT

  
        
        //is testing even legal here?

}

class intRRTestGenerator extends TestGenerator{

        //def genMod(): Module = Module(new intRenameROB())
        //def genTest[T <: Module](c:T):Tester[T] = 

}*/
