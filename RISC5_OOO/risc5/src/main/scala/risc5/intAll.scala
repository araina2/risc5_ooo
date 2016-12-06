package risc5

import Chisel._
import scala.sys.process._


class intAll extends Module {
//add (implicit val p: Parameters) before extends if using param
        
        val io = new Bundle {

        val fetchstart = UInt(INPUT, 3)


        }
        //need to run Perl script - can do in scala, but Chisel???
        





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


        r.io.DecodeQueueSelect0 := d0.io.decodeQueueSelect 
        r.io.DecodeQueueSelect1 := d1.io.decodeQueueSelect 
        r.io.DecodeQueueSelect2 := d2.io.decodeQueueSelect 
        r.io.DecodeQueueSelect3 := d3.io.decodeQueueSelect 
       //pretty sure we need 4 of these?? commenting out 3 just in case...
        r.io.DecodeType :=  d0.io.decodeType
        //r.io.DecodeType1 := d1.io.decodeType
        //r.io.DecodeType2 :=  d2.io.decodeType
        //r.io.DecodeType3 :=  d3.io.decodeType
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






}
