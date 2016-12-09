package risc5

import Chisel._



class intDecodeRename extends Module {
//Done except for testing??



        val io = new Bundle{
        val fetchInstruction0 = UInt(INPUT, 32)
        val fetchAddress0 = UInt(INPUT, 64)
        val fetchRobTag0  = UInt(INPUT, 7)
        val fetchBranchTaken0 = UInt(INPUT, 1)
        val fetchValid0 = UInt(INPUT, 1)
        val lSFull0 = UInt(INPUT, 1)
        val issueFull0 = UInt(INPUT, 1)    
        val decodeBranchTaken0 = UInt(OUTPUT, 1)
        val decodeValid0 = UInt(OUTPUT, 1)
        val decodeAddress0 = UInt(OUTPUT,64)
       
        val fetchInstruction1 = UInt(INPUT, 32)
        val fetchAddress1 = UInt(INPUT, 64)
        val fetchRobTag1  = UInt(INPUT, 7)
        val fetchBranchTaken1 = UInt(INPUT, 1)
        val fetchValid1 = UInt(INPUT, 1)
        val lSFull1 = UInt(INPUT, 1)
        val issueFull1 = UInt(INPUT, 1)    
        val decodeBranchTaken1 = UInt(OUTPUT, 1)
        val decodeValid1 = UInt(OUTPUT, 1)
        val decodeAddress1 = UInt(OUTPUT,64)
        
        val fetchInstruction2 = UInt(INPUT, 32)
        val fetchAddress2 = UInt(INPUT, 64)
        val fetchRobTag2  = UInt(INPUT, 7)
        val fetchBranchTaken2 = UInt(INPUT, 1)
        val fetchValid2 = UInt(INPUT, 1)
        val lSFull2 = UInt(INPUT, 1)
        val issueFull2 = UInt(INPUT, 1)    
        val decodeBranchTaken2 = UInt(OUTPUT, 1)
        val decodeValid2 = UInt(OUTPUT, 1)
        val decodeAddress2 = UInt(OUTPUT,64)

        val fetchInstruction3 = UInt(INPUT, 32)
        val fetchAddress3 = UInt(INPUT, 64)
        val fetchRobTag3  = UInt(INPUT, 7)
        val fetchBranchTaken3 = UInt(INPUT, 1)
        val fetchValid3 = UInt(INPUT, 1)
        val lSFull3 = UInt(INPUT, 1)
        val issueFull3 = UInt(INPUT, 1)    
        val decodeBranchTaken3 = UInt(OUTPUT, 1)
        val decodeValid3 = UInt(OUTPUT, 1)
        val decodeAddress3 = UInt(OUTPUT,64)





        val RenameSourceAValue0 = UInt(OUTPUT,64)
            val RenameSourceAValue1 = UInt(OUTPUT,64)
                val RenameSourceAValue2 = UInt(OUTPUT,64)
                    val RenameSourceAValue3 = UInt(OUTPUT,64)
                        val RenameSourceAValueValid0 = UInt(OUTPUT,1)
                            val RenameSourceAValueValid1 = UInt(OUTPUT,1)
                                val RenameSourceAValueValid2 = UInt(OUTPUT,1)
                                    val RenameSourceAValueValid3 = UInt(OUTPUT,1)
                                        val RenameSourceATag0 = UInt(OUTPUT,10)
                                            val RenameSourceATag1 = UInt(OUTPUT,10)
                                                val RenameSourceATag2 = UInt(OUTPUT,10)
                                                    val RenameSourceATag3 = UInt(OUTPUT,10)
                                                        val RenameSourceBValue0 = UInt(OUTPUT,64)
                                                            val RenameSourceBValue1 = UInt(OUTPUT,64)
                                                                val RenameSourceBValue2 = UInt(OUTPUT,64)
                                                                    val RenameSourceBValue3 = UInt(OUTPUT,64)
                                                                        val RenameSourceBValueValid0 = UInt(OUTPUT,1)
                                                                            val RenameSourceBValueValid1 = UInt(OUTPUT,1)
                                                                                val RenameSourceBValueValid2 = UInt(OUTPUT,1)
                                                                                    val RenameSourceBValueValid3 = UInt(OUTPUT,1)
                                                                                        val RenameSourceBTag0 = UInt(OUTPUT,10)
                                                                                            val RenameSourceBTag1 = UInt(OUTPUT,10)
                                                                                                val RenameSourceBTag2 = UInt(OUTPUT,10)
                                                                                                    val RenameSourceBTag3 = UInt(OUTPUT,10)
                                                                                                        val RenameDestTag0 = UInt(OUTPUT, 10) 
                                                                                                            val RenameDestTag1 = UInt(OUTPUT, 10)  
                                                                                                                val RenameDestTag2 = UInt(OUTPUT, 10) 
                                                                                                                    val RenameDestTag3 = UInt(OUTPUT, 10) 
                                                                                                                        val RenameIssueRowSelectTag0 = UInt(OUTPUT, 7)
                                                                                                                            val RenameIssueRowSelectTag1 = UInt(OUTPUT, 7)
                                                                                                                                val RenameIssueRowSelectTag2 = UInt(OUTPUT, 7)
                                                                                                                                    val RenameIssueRowSelectTag3 = UInt(OUTPUT, 7)
                                                                                                                                        val RenameLoadStoreRowSelect0 = UInt(OUTPUT, 5)
                                                                                                                                            val RenameLoadStoreRowSelect1 = UInt(OUTPUT, 5)
                                                                                                                                                val RenameLoadStoreRowSelect2 = UInt(OUTPUT, 5)
                                                                                                                                                    val RenameLoadStoreRowSelect3 = UInt(OUTPUT, 5)


//Signals that are just passed on as it is from the decode stage
    val Rename_dest_0 = UInt(OUTPUT,5)
        val Rename_dest_1 = UInt(OUTPUT,5)
            val Rename_dest_2 = UInt(OUTPUT,5)
                val Rename_dest_3 = UInt(OUTPUT,5)
                    val RenameValid = UInt(OUTPUT, 1)
                        val RenameROBtag0 = UInt(OUTPUT,7)
                            val RenameROBtag1 = UInt(OUTPUT,7)
                                val RenameROBtag2 = UInt(OUTPUT,7)
                                    val RenameROBtag3 = UInt(OUTPUT,7)
                                        val Rename_Imm_0  = UInt(OUTPUT,20)
                                            val Rename_Imm_1  = UInt(OUTPUT,20)
                                                val Rename_Imm_2  = UInt(OUTPUT,20)
                                                    val Rename_Imm_3  = UInt(OUTPUT,20)
                                                        val Rename_Opcode_0 = UInt(OUTPUT,7)
                                                            val Rename_Opcode_1 = UInt(OUTPUT,7)
                                                                val Rename_Opcode_2 = UInt(OUTPUT,7)
                                                                    val Rename_Opcode_3 = UInt(OUTPUT,7)
                                                                        val Rename_func3_0 = UInt(OUTPUT,7)
                                                                            val Rename_func3_1 = UInt(OUTPUT,7)
                                                                                val Rename_func3_2 = UInt(OUTPUT,7)
                                                                                    val Rename_func3_3 = UInt(OUTPUT,7)
                                                                                        val Rename_func7_0 = UInt(OUTPUT,7)
                                                                                            val Rename_func7_1 = UInt(OUTPUT,7)
                                                                                                val Rename_func7_2 = UInt(OUTPUT,7)
                                                                                                    val Rename_func7_3 = UInt(OUTPUT,7)
                                                                                                        val RenameQueueSelect0 = Bool(OUTPUT)
                                                                                                            val RenameQueueSelect1 = Bool(OUTPUT)
                                                                                                                val RenameQueueSelect2 = Bool(OUTPUT)
                                                                                                                    val RenameQueueSelect3 = Bool(OUTPUT)

                                                                                                                        val RenameStoreSelect0 = Bool(OUTPUT)
                                                                                                                            val RenameStoreSelect1 = Bool(OUTPUT)
                                                                                                                                val RenameStoreSelect2 = Bool(OUTPUT)
                                                                                                                                    val RenameStoreSelect3 = Bool(OUTPUT)
                                                                                                                                    

  val RenameLoadStoreValid0 = UInt(OUTPUT,1)
      val RenameLoadStoreValid1 = UInt(OUTPUT,1)
          val RenameLoadStoreValid2 = UInt(OUTPUT,1)
              val RenameLoadStoreValid3 = UInt(OUTPUT,1)

        }

        /////////////////////////////////
        // Module INstantiation
        //4 decodes go to one rename.....
        ///////////////////////////////////
        val d0 = Module(new DecodeChannel())
        val d1 = Module(new DecodeChannel())
        val d2 = Module(new DecodeChannel())
        val d3 = Module(new DecodeChannel())
        
        val r = Module(new RegAliasTable())

        //pretty sure that there aren't any pipelined state regs or wiresin design, if wrong, add here

    
        /*
        Required inputs for all the decodes (from fetch)
        
        d0.io.fetchInstruction 
        d0.io.fetchAddress
        d0.io.fetchRobTag  
        d0.io.fetchBranchTaken 
        d0.io.fetchValid 
        d0.io.lSFull
        d0.io.issueFull
        */

       /*
        Decode outputs that don't go to rename
`       d1.io.decodeBranchTaken
        d1.io.decodeValid
        d1.io.decodeAddress 
       */
      

      /*
        Rename inputs not from Decode
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
     */



        ////////////////////////////////////////////////////////
       //ASsigning vals form out of decode to in of renameing
       ///////////////////////////////////////////////////////

        r.io.DecodeQueueSelect0 := d0.io.decodeQueueSelect 
        r.io.DecodeQueueSelect1 := d1.io.decodeQueueSelect 
        r.io.DecodeQueueSelect2 := d2.io.decodeQueueSelect 
        r.io.DecodeQueueSelect3 := d3.io.decodeQueueSelect
        r.io.DecodeType0 :=  d0.io.decodeType
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

}


/*class inteDRTests extends Tester{

//All pokes go to Decode and all expecteds come from RAT

  
        
        //is testing even legal here?

}

class intDRTestGenerator extends TestGenerator{

        //def genMod(): Module = Module(new intDecodeRename())
        //def genTest[T <: Module](c:T):Tester[T] = 

}*/
