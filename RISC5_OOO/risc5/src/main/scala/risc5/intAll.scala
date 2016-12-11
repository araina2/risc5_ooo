package risc5

import Chisel._
import scala.sys.process._


class intAll extends Module {
//add (implicit val p: Parameters) before extends if using param
        
        val io = new Bundle {

        val fetchInstructiond0 = UInt(INPUT,32) 
        val fetchAddressd0 = UInt(INPUT, 64)
        val fetchRobTagd0  = UInt(INPUT, 7)
        val fetchBranchTakend0 = UInt(INPUT, 1)
        val fetchValidd0= UInt(INPUT, 1)
        }

// register for the arbitor logic
        val Cache_enable = Reg(init=UInt(0,1))
        val Cache_Address = Reg(init=UInt(0,64))
        val Cache_Compare = Reg(init=UInt(0,1))
        val Cache_tag_in = Reg(init=UInt(0,7))
        val Cache_valid_in = Reg(init=UInt(0,1))

        val Cache_enable2 = Reg(init=UInt(0,1))
        val Cache_Address2 = Reg(init=UInt(0,64))
        val Cache_Compare2 = Reg(init=UInt(0,1))
        val Cache_tag_in2 = Reg(init=UInt(0,7))
        val Cache_valid_in2 = Reg(init=UInt(0,1))

        //need to run Perl script - can do in scala, but Chisel???
        
        //TODO: Fetch Module stuff
        //Sorry, spent so much time matching up other signals, didn't get to the perl script




        /////////////////////////////////
        // Module INstantiation
        //4 decodes go to one rename.....
        ///////////////////////////////////
        val d0 = Module(new DecodeChannel())
        val d1 = Module(new DecodeChannel())
        val d2 = Module(new DecodeChannel())
        val d3 = Module(new DecodeChannel())
        val r = Module(new RegAliasTable())
        val rob = Module(new ROB())
        val f0 = Module(new FunctionalUnit())
        val f1 = Module(new FunctionalUnit())
        val f2 = Module(new FunctionalUnit())
        val f3 = Module(new FunctionalUnit())
        val iss = Module(new IssueQueueModule())
        val lsq = Module(new LoadStoreQueueModule())
        val dmem = Module(new dmem(64,64))      
                
        //pretty sure that there aren't any pipelined state regs or wiresin design, if wrong, add here

    
        /*
        Required inputs for all the decodes (from fetch mostldy)
        
        d0.io.fetchInstruction 
        d0.io.fetchAddress
        d0.io.fetchRobTag  
        d0.io.fetchBranchTaken 
        d0.io.fetchValid 
        d0.io.lSFull
        d0.io.issueFull


        */

      ////////////////////////////////////////////////////////
      //        Deocde Channles - Make 4 :)                 //
      ///////////////////////////////////////////////////////

       d0.io.issueFull := iss.io.IssueQueueFull
       d0.io.lSFull := lsq.io.LoadStoreFull
       d0.io.fetchInstruction := io.fetchInstructiond0 
       d0.io.fetchAddress := io.fetchAddressd0
       d0.io.fetchRobTag  := io.fetchRobTagd0
       d0.io.fetchBranchTaken := io.fetchBranchTakend0
       d0.io.fetchValid := io.fetchValidd0
      
      

      ////////////////////////////////////////////////////////
      //                    Rename inputs                   //
      ///////////////////////////////////////////////////////
      //Completed
      ///////////////////////////////////////////////////////
        r.io.FUBroadcastValue0 := f0.io.FUBroadcastValue
        r.io.FUBroadcastTag0 := f0.io.FUBroadcastTag
        r.io.FUBroadcastValue1 := f1.io.FUBroadcastValue
        r.io.FUBroadcastTag1 := f1.io.FUBroadcastTag
        r.io.FUBroadcastValue2 := f2.io.FUBroadcastValue
        r.io.FUBroadcastTag2 := f2.io.FUBroadcastTag
        r.io.FUBroadcastValue3 := f3.io.FUBroadcastValue
        r.io.FUBroadcastTag3 := f3.io.FUBroadcastTag
        r.io.LoadStoreDestVal0 := lsq.io.LoadStoreDestValue
        r.io.LoadStoreDestTag0 := lsq.io.LoadStoreDestTag_out
        r.io.IssueBroadcastFreeRow0 := iss.io.IssueBroadCastFreeRow_0
        r.io.IssueBroadcastFreeRow1 := iss.io.IssueBroadCastFreeRow_1
        r.io.IssueBroadcastFreeRow2 := iss.io.IssueBroadCastFreeRow_2
        r.io.IssueBroadcastFreeRow3 := iss.io.IssueBroadCastFreeRow_3
          
        //Adding the valid signals pertaining to broadcast free row in Issue queue
        r.io.IssueRowValid0 := iss.io.Valid_0
        r.io.IssueRowValid1 := iss.io.Valid_1
        r.io.IssueRowValid2 := iss.io.Valid_2
        r.io.IssueRowValid3 := iss.io.Valid_3

        //Adding the valid signals pertaining to broadcast free row in LS queue
        r.io.LoadStoreRowValid := lsq.io.LoadStoreDestValid_out

        r.io.IssueFull0 := iss.io.Full_0
        r.io.IssueFull1 := iss.io.Full_1
        r.io.IssueFull2 := iss.io.Full_2
        r.io.IssueFull3 := iss.io.Full_3
        r.io.IssueFull := iss.io.IssueQueueFull
        r.io.LoadStoreFull := lsq.io.LoadStoreFull
        r.io.LoadStoreBroadcastFreeRow0 := lsq.io.LoadStoreFreeRow 
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
        r.io.DecodeValid0 := d0.io.decodeValid
        r.io.DecodeValid1 := d1.io.decodeValid
        r.io.DecodeValid2 := d2.io.decodeValid
        r.io.DecodeValid3 := d3.io.decodeValid

       //////////////////////////////////////////////
       //                ROB INPUTS                //
       //////////////////////////////////////////////
       //Should be done
       //////////////////////////////////////////////
        rob.io.FUBroadcastValue0 := f0.io.FUBroadcastValue
        rob.io.FUBroadcastTag0 := f0.io.FUBroadcastTag
        rob.io.FUBroadcastValid0 := f0.io.FUBroadcastValid
        rob.io.FUBroadcastValue1 := f1.io.FUBroadcastValue
        rob.io.FUBroadcastTag1 := f1.io.FUBroadcastTag
        rob.io.FUBroadcastValid1 := f1.io.FUBroadcastValid
        rob.io.FUBroadcastValue2 := f2.io.FUBroadcastValue
        rob.io.FUBroadcastTag2 := f2.io.FUBroadcastTag
        rob.io.FUBroadcastValid2 := f2.io.FUBroadcastValid
        rob.io.FUBroadcastValue3 := f3.io.FUBroadcastValue
        rob.io.FUBroadcastTag3 := f3.io.FUBroadcastTag
        rob.io.FUBroadcastValid3 := f3.io.FUBroadcastValid
        rob.io.LoadStoreDestVal0 := lsq.io.LoadStoreDestValue
        rob.io.LoadStoreDestTag0 := lsq.io.LoadStoreDestTag_out
       // rob.io.LoadStoreDestReg0 := lsq.io.LoadStoreDestReg //Currently not using
        rob.io.LoadStoreDestAddress0 := lsq.io.LoadStoreDestAddress
        rob.io.RenameDestTag0 := r.io.RenameDestTag0
        rob.io.RenameDestTag1 := r.io.RenameDestTag1
        rob.io.RenameDestTag2 := r.io.RenameDestTag2
        rob.io.RenameDestTag3 := r.io.RenameDestTag3
        rob.io.Rename_dest_0 := r.io.Rename_dest_0
        rob.io.Rename_dest_1 := r.io.Rename_dest_1
        rob.io.Rename_dest_2 := r.io.Rename_dest_2
        rob.io.Rename_dest_3 := r.io.Rename_dest_3
        //TODO(need fix)rob.io.RenameValid := r.io.RenameValid
        rob.io.RenameROBtag0 := r.io.RenameROBtag0
        rob.io.RenameROBtag1 := r.io.RenameROBtag1
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

        //Adding the valid signals for issue and load store queue in ROB
        rob.io.RenameIssueValid0 := r.io.RenameIssueValid0  
        rob.io.RenameIssueValid1 := r.io.RenameIssueValid1  
        rob.io.RenameIssueValid2 := r.io.RenameIssueValid2  
        rob.io.RenameIssueValid3 := r.io.RenameIssueValid3  
         
        rob.io.RenameLoadStoreValid0 := r.io.RenameLoadStoreValid0
        rob.io.RenameLoadStoreValid1 := r.io.RenameLoadStoreValid1
        rob.io.RenameLoadStoreValid2 := r.io.RenameLoadStoreValid2
        rob.io.RenameLoadStoreValid3 := r.io.RenameLoadStoreValid3

        // Connecting the dmem busy output port to ROB input
        rob.io.dmembusy := dmem.io.busy
        
        
        
        
        //////////////////////////////////////////////////////////
        //                 Issue Queue In                      //
        //////////////////////////////////////////////////////////
        //
        //QUESTION TODO Is tyupe never poased out of register alsisa tanle stage?
        iss.io.RenameValid_0 := r.io.RenameIssueValid0
        iss.io.RenameValid_1 := r.io.RenameIssueValid1
        iss.io.RenameValid_2 := r.io.RenameIssueValid2
        iss.io.RenameValid_3 := r.io.RenameIssueValid3
        iss.io.FUBroadcastValue0_0 := f0.io.FUBroadcastValue
        iss.io.FUBroadcastTag0_0 := f0.io.FUBroadcastTag
        iss.io.FUBroadcastValue1_0 := f1.io.FUBroadcastValue
        iss.io.FUBroadcastTag1_0 := f1.io.FUBroadcastTag
        iss.io.FUBroadcastValue2_0 := f2.io.FUBroadcastValue
        iss.io.FUBroadcastTag2_0 := f2.io.FUBroadcastTag
        iss.io.FUBroadcastValue3_0 := f3.io.FUBroadcastValue
        iss.io.FUBroadcastTag3_0 := f3.io.FUBroadcastTag
        iss.io.LoadStoreDestVal_0 := lsq.io.LoadStoreDestValue
        iss.io.LoadStoreDestTag_0 := lsq.io.LoadStoreDestTag_out
        iss.io.LoadStoreDestValid_0 := lsq.io.LoadStoreDestValid_out
        iss.io.FUBroadcastValid0_0 := f0.io.FUBroadcastValid
        //TODO iss.io.FUisBusy_0 := f0.io.busy
        iss.io.RenameRowSelect0 := r.io.RenameIssueRowSelectTag0
        iss.io.RenameSourceAValue_0 := r.io.RenameSourceAValue0
        iss.io.RenameSourceAValueValid_0 := r.io.RenameSourceAValueValid0
        iss.io.RenameSourceATag_0 := r.io.RenameSourceATag0
        iss.io.RenameSourceBValue_0 := r.io.RenameSourceBValue0
        iss.io.RenameSourceBValueValid_0 := r.io.RenameSourceBValueValid0
        iss.io.RenameSourceBTag_0:= r.io.RenameSourceBTag0
        iss.io.RenameDestTag_0 := r.io.RenameDestTag0
        iss.io.Decode_Opcode_0 := r.io.Rename_Opcode_0
        iss.io.Decode_Func3_0 := r.io.Rename_func3_0
        iss.io.Decode_Func7_0 := r.io.Rename_func7_0
        iss.io.Decode_Imm_0 := r.io.Rename_Imm_0
        iss.io.DecodeROB_0 := r.io.RenameROBtag0
        iss.io.DecodeType_0 := d0.io.decodeType
        
        iss.io.FUBroadcastValue0_1 := f0.io.FUBroadcastValue
        iss.io.FUBroadcastTag0_1 := f0.io.FUBroadcastTag
        iss.io.FUBroadcastValue1_1 := f1.io.FUBroadcastValue
        iss.io.FUBroadcastTag1_1 := f1.io.FUBroadcastTag
        iss.io.FUBroadcastValue2_1 := f2.io.FUBroadcastValue
        iss.io.FUBroadcastTag2_1 := f2.io.FUBroadcastTag
        iss.io.FUBroadcastValue3_1 := f3.io.FUBroadcastValue
        iss.io.FUBroadcastTag3_1 := f3.io.FUBroadcastTag
        iss.io.LoadStoreDestVal_1 := lsq.io.LoadStoreDestValue
        iss.io.LoadStoreDestTag_1 := lsq.io.LoadStoreDestTag_out
        iss.io.LoadStoreDestValid_1 := lsq.io.LoadStoreDestValid_out
        iss.io.FUBroadcastValid0_1 := f1.io.FUBroadcastValid
        //TODOiss.io.FUisBusy_1 := f1.io.busy
        iss.io.RenameRowSelect1 := r.io.RenameIssueRowSelectTag1
        iss.io.RenameSourceAValue_1 := r.io.RenameSourceAValue1
        iss.io.RenameSourceAValueValid_1 := r.io.RenameSourceAValueValid1
        iss.io.RenameSourceATag_1 := r.io.RenameSourceATag1
        iss.io.RenameSourceBValue_1 := r.io.RenameSourceBValue1
        iss.io.RenameSourceBValueValid_1 := r.io.RenameSourceBValueValid1
        iss.io.RenameSourceBTag_1:= r.io.RenameSourceBTag1
        iss.io.RenameDestTag_1 := r.io.RenameDestTag1
        iss.io.Decode_Opcode_1 := r.io.Rename_Opcode_1
        iss.io.Decode_Func3_1 := r.io.Rename_func3_1
        iss.io.Decode_Func7_1 := r.io.Rename_func7_1
        iss.io.Decode_Imm_1 := r.io.Rename_Imm_1
        iss.io.DecodeROB_1 := r.io.RenameROBtag1
        iss.io.DecodeType_1 := d1.io.decodeType
        
        iss.io.FUBroadcastValue0_2 := f0.io.FUBroadcastValue
        iss.io.FUBroadcastTag0_2 := f0.io.FUBroadcastTag
        iss.io.FUBroadcastValue1_2 := f1.io.FUBroadcastValue
        iss.io.FUBroadcastTag1_2 := f1.io.FUBroadcastTag
        iss.io.FUBroadcastValue2_2 := f2.io.FUBroadcastValue
        iss.io.FUBroadcastTag2_2 := f2.io.FUBroadcastTag
        iss.io.FUBroadcastValue3_2 := f3.io.FUBroadcastValue
        iss.io.FUBroadcastTag3_2 := f3.io.FUBroadcastTag
        iss.io.LoadStoreDestVal_2 := lsq.io.LoadStoreDestValue
        iss.io.LoadStoreDestTag_2 := lsq.io.LoadStoreDestTag_out
        iss.io.LoadStoreDestValid_2 := lsq.io.LoadStoreDestValid_out
        iss.io.FUBroadcastValid0_2 := f2.io.FUBroadcastValid
       //TODO: iss.io.FUisBusy_2 := f2.io.busy
        iss.io.RenameRowSelect2 := r.io.RenameIssueRowSelectTag2
        iss.io.RenameSourceAValue_2 := r.io.RenameSourceAValue2
        iss.io.RenameSourceAValueValid_2 := r.io.RenameSourceAValueValid2
        iss.io.RenameSourceATag_2 := r.io.RenameSourceATag2
        iss.io.RenameSourceBValue_2 := r.io.RenameSourceBValue2
        iss.io.RenameSourceBValueValid_2 := r.io.RenameSourceBValueValid2
        iss.io.RenameSourceBTag_2:= r.io.RenameSourceBTag2
        iss.io.RenameDestTag_2 := r.io.RenameDestTag2
        iss.io.Decode_Opcode_2 := r.io.Rename_Opcode_2
        iss.io.Decode_Func3_2 := r.io.Rename_func3_2
        iss.io.Decode_Func7_2 := r.io.Rename_func7_2
        iss.io.Decode_Imm_2 := r.io.Rename_Imm_2
        iss.io.DecodeROB_2 := r.io.RenameROBtag2
        iss.io.DecodeType_2 := d2.io.decodeType

        iss.io.FUBroadcastValue0_3 := f0.io.FUBroadcastValue
        iss.io.FUBroadcastTag0_3 := f0.io.FUBroadcastTag
        iss.io.FUBroadcastValue1_3 := f1.io.FUBroadcastValue
        iss.io.FUBroadcastTag1_3 := f1.io.FUBroadcastTag
        iss.io.FUBroadcastValue2_3 := f2.io.FUBroadcastValue
        iss.io.FUBroadcastTag2_3 := f2.io.FUBroadcastTag
        iss.io.FUBroadcastValue3_3 := f3.io.FUBroadcastValue
        iss.io.FUBroadcastTag3_3 := f3.io.FUBroadcastTag
        iss.io.LoadStoreDestVal_3 := lsq.io.LoadStoreDestValue
        iss.io.LoadStoreDestTag_3 := lsq.io.LoadStoreDestTag_out
        iss.io.LoadStoreDestValid_3 := lsq.io.LoadStoreDestValid_out
        iss.io.FUBroadcastValid0_3 := f3.io.FUBroadcastValid
       //TODO iss.io.FUisBusy_3 := f3.io.busy
        iss.io.RenameRowSelect3:= r.io.RenameIssueRowSelectTag3
        iss.io.RenameSourceAValue_3 := r.io.RenameSourceAValue3
        iss.io.RenameSourceAValueValid_3 := r.io.RenameSourceAValueValid3
        iss.io.RenameSourceATag_3 := r.io.RenameSourceATag3
        iss.io.RenameSourceBValue_3 := r.io.RenameSourceBValue3
        iss.io.RenameSourceBValueValid_3 := r.io.RenameSourceBValueValid3
        iss.io.RenameSourceBTag_3:= r.io.RenameSourceBTag3
        iss.io.RenameDestTag_3 := r.io.RenameDestTag3
        iss.io.Decode_Opcode_3 := r.io.Rename_Opcode_3
        iss.io.Decode_Func3_3 := r.io.Rename_func3_3
        iss.io.Decode_Func7_3 := r.io.Rename_func7_3
        iss.io.Decode_Imm_3 := r.io.Rename_Imm_3
        iss.io.DecodeROB_3 := r.io.RenameROBtag3
        iss.io.DecodeType_3 := d3.io.decodeType

        ////////////////////////////////////////////////////////
        //          Functional Units Time                     //
        ////////////////////////////////////////////////////////
        //Completed
        ///////////////////////////////////////////////////////
        //F0
        f0.io.issueSourceValA := iss.io.IssueSourceValA_0
        f0.io.issueSourceValB := iss.io.IssueSourceValB_0
        f0.io.issueFUOpcode := iss.io.IssueFUOpcode_0
        f0.io.issueFunc3 := iss.io.Issue_Func3_0
        f0.io.issueFunc7 := iss.io.Issue_Func7_0
        f0.io.issueImm := iss.io.Issue_Imm_0
        f0.io.issueType := iss.io.Issue_Type_0
        f0.io.issueDestTag := iss.io.IssuedestTag_0
        f0.io.issueFull := iss.io.Full_0
        f0.io.issueValid := iss.io.Valid_0
        when(f0.io.issueValid===UInt(1)){
          printf("rnayar: issueValid in the functional unit is set to ONE")
        }
        //F1
        f1.io.issueSourceValA := iss.io.IssueSourceValA_1
        f1.io.issueSourceValB := iss.io.IssueSourceValB_1
        f1.io.issueFUOpcode := iss.io.IssueFUOpcode_1
        f1.io.issueFunc3 := iss.io.Issue_Func3_1
        f1.io.issueFunc7 := iss.io.Issue_Func7_1
        f1.io.issueImm := iss.io.Issue_Imm_1
        f1.io.issueType := iss.io.Issue_Type_1
        f1.io.issueDestTag := iss.io.IssuedestTag_1
        f1.io.issueFull := iss.io.Full_1
        f1.io.issueValid := iss.io.Valid_1
        //F2
        f2.io.issueSourceValA := iss.io.IssueSourceValA_2
        f2.io.issueSourceValB := iss.io.IssueSourceValB_2
        f2.io.issueFUOpcode := iss.io.IssueFUOpcode_2
        f2.io.issueFunc3 := iss.io.Issue_Func3_2
        f2.io.issueFunc7 := iss.io.Issue_Func7_2
        f2.io.issueImm := iss.io.Issue_Imm_2
        f2.io.issueType := iss.io.Issue_Type_2
        f2.io.issueDestTag := iss.io.IssuedestTag_2
        f2.io.issueFull := iss.io.Full_2
        f2.io.issueValid := iss.io.Valid_2
        //F3
        f3.io.issueSourceValA := iss.io.IssueSourceValA_3
        f3.io.issueSourceValB := iss.io.IssueSourceValB_3
        f3.io.issueFUOpcode := iss.io.IssueFUOpcode_3
        f3.io.issueFunc3 := iss.io.Issue_Func3_3
        f3.io.issueFunc7 := iss.io.Issue_Func7_3
        f3.io.issueImm := iss.io.Issue_Imm_3
        f3.io.issueType := iss.io.Issue_Type_3
        f3.io.issueDestTag := iss.io.IssuedestTag_3
        f3.io.issueFull := iss.io.Full_3
        f3.io.issueValid := iss.io.Valid_3

        
        ///////////////////////////////////////////////
        //          Load Store Queeueueueueueueueueu
        ///////////////////////////////////////////////
        //Think I got it all
        ///////////////////////////////////////////////
        lsq.io.RenameLoadStoreRowSelect0 := r.io.RenameLoadStoreRowSelect0
        lsq.io.RenameLoadStoreRowSelect1 := r.io.RenameLoadStoreRowSelect1
        lsq.io.RenameLoadStoreRowSelect2 := r.io.RenameLoadStoreRowSelect2
        lsq.io.RenameLoadStoreRowSelect3 := r.io.RenameLoadStoreRowSelect3
        lsq.io.RenameLoadStoreValid0 := r.io.RenameLoadStoreValid0
        lsq.io.RenameLoadStoreValid1 := r.io.RenameLoadStoreValid1
        lsq.io.RenameLoadStoreValid2 := r.io.RenameLoadStoreValid2
        lsq.io.RenameLoadStoreValid3 := r.io.RenameLoadStoreValid3
        lsq.io.RenameSourceAValue0 := r.io.RenameSourceAValue0
        lsq.io.RenameSourceAValue1 := r.io.RenameSourceAValue1
        lsq.io.RenameSourceAValue2 := r.io.RenameSourceAValue2
        lsq.io.RenameSourceAValue3 := r.io.RenameSourceAValue3
        lsq.io.RenameSourceAValueValid0 := r.io.RenameSourceAValueValid0
        lsq.io.RenameSourceAValueValid1 := r.io.RenameSourceAValueValid1
        lsq.io.RenameSourceAValueValid2 := r.io.RenameSourceAValueValid2
        lsq.io.RenameSourceAValueValid3 := r.io.RenameSourceAValueValid3
        lsq.io.RenameSourceATag0 := r.io.RenameSourceATag0
        lsq.io.RenameSourceATag1 := r.io.RenameSourceATag1
        lsq.io.RenameSourceATag2 := r.io.RenameSourceATag2
        lsq.io.RenameSourceATag3 := r.io.RenameSourceATag3
        lsq.io.RenameSourceBValue0 := r.io.RenameSourceBValue0
        lsq.io.RenameSourceBValue1 := r.io.RenameSourceBValue1
        lsq.io.RenameSourceBValue2 := r.io.RenameSourceBValue2
        lsq.io.RenameSourceBValue3 := r.io.RenameSourceBValue3
        lsq.io.RenameSourceBValueValid0 := r.io.RenameSourceBValueValid0
        lsq.io.RenameSourceBValueValid1 := r.io.RenameSourceBValueValid1
        lsq.io.RenameSourceBValueValid2 := r.io.RenameSourceBValueValid2
        lsq.io.RenameSourceBValueValid3 := r.io.RenameSourceBValueValid3
        lsq.io.RenameSourceBTag0 := r.io.RenameSourceBTag0
        lsq.io.RenameSourceBTag1 := r.io.RenameSourceBTag1
        lsq.io.RenameSourceBTag2 := r.io.RenameSourceBTag2
        lsq.io.RenameSourceBTag3 := r.io.RenameSourceBTag3
        lsq.io.Rename_Opcode_0 := r.io.Rename_Opcode_0
        lsq.io.Rename_Opcode_1 := r.io.Rename_Opcode_1
        lsq.io.Rename_Opcode_2 := r.io.Rename_Opcode_2
        lsq.io.Rename_Opcode_3 := r.io.Rename_Opcode_3
        lsq.io.Rename_destTag_0 := r.io.RenameDestTag0
        lsq.io.Rename_destTag_1 := r.io.RenameDestTag1
        lsq.io.Rename_destTag_2 := r.io.RenameDestTag2
        lsq.io.Rename_destTag_3 := r.io.RenameDestTag3
        lsq.io.Rename_func3_0 := r.io.Rename_func3_0
        lsq.io.Rename_func3_1 := r.io.Rename_func3_1
        lsq.io.Rename_func3_2 := r.io.Rename_func3_2
        lsq.io.Rename_func3_3 := r.io.Rename_func3_3
        lsq.io.Rename_func7_0 := r.io.Rename_func7_0
        lsq.io.Rename_func7_1 := r.io.Rename_func7_1
        lsq.io.Rename_func7_2 := r.io.Rename_func7_2
        lsq.io.Rename_func7_3 := r.io.Rename_func7_3
        lsq.io.Rename_Imm_0 := r.io.Rename_Imm_0
        lsq.io.Rename_Imm_1 := r.io.Rename_Imm_1
        lsq.io.Rename_Imm_2 := r.io.Rename_Imm_2
        lsq.io.Rename_Imm_3 := r.io.Rename_Imm_3
        lsq.io.RenameQueueSelect0 := r.io.RenameQueueSelect0
        lsq.io.RenameQueueSelect1 := r.io.RenameQueueSelect1
        lsq.io.RenameQueueSelect2 := r.io.RenameQueueSelect2
        lsq.io.RenameQueueSelect3 := r.io.RenameQueueSelect3
        lsq.io.RenameStoreSelect0 := r.io.RenameStoreSelect0
        lsq.io.RenameStoreSelect1 := r.io.RenameStoreSelect1
        lsq.io.RenameStoreSelect2 := r.io.RenameStoreSelect2
        lsq.io.RenameStoreSelect3 := r.io.RenameStoreSelect3
        lsq.io.RenameROBtag0 := r.io.RenameROBtag0
        lsq.io.RenameROBtag1 := r.io.RenameROBtag1
        lsq.io.RenameROBtag2 := r.io.RenameROBtag2
        lsq.io.RenameROBtag3 := r.io.RenameROBtag3
        
        lsq.io.RenameType_0 := r.io.RenameType0
        lsq.io.RenameType_1 := r.io.RenameType1
        lsq.io.RenameType_2 := r.io.RenameType2
        lsq.io.RenameType_3 := r.io.RenameType3
        lsq.io.FUBroadcastValue0 := f0.io.FUBroadcastValue
        lsq.io.FUBroadcastTag0 := f0.io.FUBroadcastTag
        lsq.io.FUBroadcastValid0 := f0.io.FUBroadcastValid
        lsq.io.FUBroadcastValue1 := f1.io.FUBroadcastValue
        lsq.io.FUBroadcastTag1 := f1.io.FUBroadcastTag
        lsq.io.FUBroadcastValid1 := f1.io.FUBroadcastValid
        lsq.io.FUBroadcastValue2 := f2.io.FUBroadcastValue
        lsq.io.FUBroadcastTag2 := f2.io.FUBroadcastTag
        lsq.io.FUBroadcastValid2 := f2.io.FUBroadcastValid
        lsq.io.FUBroadcastValue3 := f3.io.FUBroadcastValue
        lsq.io.FUBroadcastTag3 := f3.io.FUBroadcastTag
        lsq.io.FUBroadcastValid3 := f3.io.FUBroadcastValid
        lsq.io.LoadStoreDestTag := lsq.io.LoadStoreDestTag_out
        lsq.io.LoadStoreDestVal := lsq.io.LoadStoreDestValue
        lsq.io.LoadStoreDestValid := lsq.io.LoadStoreDestValid_out

        lsq.io.Dcache_busy := dmem.io.busy
        lsq.io.Dcache_tag_out := dmem.io.TAG_out
        lsq.io.Dcache_data_out := dmem.io.dout
        lsq.io.Dcache_Valid := dmem.io.valid


        
        ///////////////////////////////////////////////
        //              DCACHE - inputs              //
        //////////////////////////////////////////////
        // only store instructions are pushed into dcache memory

        dmem.io.din := UInt(0)
        dmem.io.addr := UInt(0)
        dmem.io.en := UInt(0)
        dmem.io.wr := UInt(0)
        dmem.io.TAG_in := UInt(0)


        when(rob.io.ROBValueValid0===UInt(1) && (lsq.io.Cache_valid_in===UInt(1)) && Cache_valid_in===UInt(0)){
          
          dmem.io.din := rob.io.ROBValue0
          dmem.io.addr := rob.io.ROBMemAddress0
          dmem.io.en := (rob.io.ROBValueValid0===UInt(1)) && (rob.io.ROBStoreSelect0===Bool(true))
          dmem.io.wr := rob.io.ROBStoreSelect0

          Cache_enable := lsq.io.Cache_enable
          Cache_Address := lsq.io.Cache_Address
          Cache_Compare := lsq.io.Cache_Compare
          Cache_tag_in := lsq.io.Cache_tag_in
          Cache_valid_in := lsq.io.Cache_valid_in

        }

        when(rob.io.ROBValueValid0===UInt(0) && (lsq.io.Cache_valid_in===UInt(0)) && Cache_valid_in===UInt(1)){
          
          dmem.io.addr := Cache_Address
          dmem.io.en := Cache_enable
          dmem.io.wr := !(Cache_valid_in)
          dmem.io.TAG_in := Cache_tag_in

          Cache_enable := UInt(0)
          Cache_Address := UInt(0)
          Cache_Compare := UInt(0)
          Cache_tag_in := UInt(0)
          Cache_valid_in := UInt(0)
        }
        
        when(rob.io.ROBValueValid0===UInt(0) && (lsq.io.Cache_valid_in===UInt(1)) && Cache_valid_in===UInt(0)){
          
          dmem.io.addr := lsq.io.Cache_Address
          dmem.io.en := lsq.io.Cache_enable
          dmem.io.wr := UInt(0)
          dmem.io.TAG_in := lsq.io.Cache_tag_in

          Cache_enable := UInt(0)
          Cache_Address := UInt(0)
          Cache_Compare := UInt(0)
          Cache_tag_in := UInt(0)
          Cache_valid_in := UInt(0)
        }

        when(rob.io.ROBValueValid0===UInt(0) && (lsq.io.Cache_valid_in===UInt(1)) && Cache_valid_in===UInt(1)){
          
          dmem.io.addr := Cache_Address
          dmem.io.en := Cache_enable
          dmem.io.wr := !(Cache_valid_in)
          dmem.io.TAG_in := Cache_tag_in

          Cache_enable := lsq.io.Cache_enable
          Cache_Address := lsq.io.Cache_Address
          Cache_Compare := lsq.io.Cache_Compare
          Cache_tag_in := lsq.io.Cache_tag_in
          Cache_valid_in := lsq.io.Cache_valid_in
        }
        when(rob.io.ROBValueValid0===UInt(1) && (lsq.io.Cache_valid_in===UInt(0))){
          
          dmem.io.din := rob.io.ROBValue0
          dmem.io.addr := rob.io.ROBMemAddress0
          dmem.io.en := (rob.io.ROBValueValid0===UInt(1)) && (rob.io.ROBStoreSelect0===Bool(true))
          dmem.io.wr := rob.io.ROBStoreSelect0

        }


        when(rob.io.ROBValueValid0===UInt(1) && (lsq.io.Cache_valid_in===UInt(1)) && Cache_valid_in===UInt(1)){
          
          dmem.io.din := rob.io.ROBValue0
          dmem.io.addr := rob.io.ROBMemAddress0
          dmem.io.en := (rob.io.ROBValueValid0===UInt(1)) && (rob.io.ROBStoreSelect0===Bool(true))
          dmem.io.wr := rob.io.ROBStoreSelect0

          Cache_enable2 := lsq.io.Cache_enable
          Cache_Address2 := lsq.io.Cache_Address
          Cache_Compare2 := lsq.io.Cache_Compare
          Cache_tag_in2 := lsq.io.Cache_tag_in
          Cache_valid_in2 := lsq.io.Cache_valid_in

        }

        when(rob.io.ROBValueValid0===UInt(0) && (lsq.io.Cache_valid_in===UInt(0)) && Cache_valid_in===UInt(0)){
          
          Cache_enable := Cache_enable2
          Cache_Address := Cache_Address2
          Cache_Compare := Cache_Compare2
          Cache_tag_in := Cache_tag_in2
          Cache_valid_in := Cache_valid_in2

          Cache_enable2 := UInt(0)
          Cache_Address2 := UInt(0)
          Cache_Compare2 := UInt(0)
          Cache_tag_in2 := UInt(0)
          Cache_valid_in2 := UInt(0)
        }


        when(rob.io.ROBValueValid0===UInt(1) && (lsq.io.Cache_valid_in===UInt(1)) && Cache_valid_in===UInt(1) && Cache_valid_in2===UInt(1)){
          
          dmem.io.din := rob.io.ROBValue0
          dmem.io.addr := rob.io.ROBMemAddress0
          dmem.io.en := (rob.io.ROBValueValid0===UInt(1)) && (rob.io.ROBStoreSelect0===Bool(true))
          dmem.io.wr := rob.io.ROBStoreSelect0
        printf("ERROR:EMERGNECY: pipeline may stall LOAD instruction droped from arbitor to cache interface")
        }
        ///////////////////////////////////////////
        //  TODO: Commit??
        //////////////////////////////////////////



//NOTE: MOST TODOs are Really ASKS        

}


class intAllTests(c: intAll) extends Tester(c) {
//ADDI R1 R5 0x5
   val RsampleInstruction = 0x00528093//Integer.parseInt("0000 0000 1010 0101 1000 1010 1011 0011",2)
   val RsampleAddress     = 0x0000000000000000 //Integer.parseInt("0000000000000000000000000000000000000000000000000000000000000001",2)
    val RsampleRobTag      = 0x2D //Integer.parseInt("101101",2)
//LUI R4 0x1000
   val RsampleInstruction2 = 0x01000237//Integer.parseInt("0000 0000 1010 0101 1000 1010 1011 0011",2)
   val RsampleAddress2     = 0x0000000000000000 //Integer.parseInt("0000000000000000000000000000000000000000000000000000000000000001",2)
    val RsampleRobTag2      = 0x2F //Integer.parseInt("101101",2)

    //expected values
    val RexpectedOpcode  = Integer.parseInt("0010011",2)
    val RexpectedRd      = Integer.parseInt("00001",2)
    val RexpectedFunky3  = Integer.parseInt("000",2)
    val RexpectedRs1     = Integer.parseInt("00101",2)
    val RexpectedRs2     = Integer.parseInt("00000",2)
    val RexpectedFunky7  = Integer.parseInt("0000000",2)
    val Rexpected_Imm    = 0x5 //Integer.parseInt("0000 0000 0000 0000 0000",2)


        poke(c.io.fetchInstructiond0, RsampleInstruction)
        poke(c.io.fetchAddressd0, RsampleAddress)
        poke(c.io.fetchRobTagd0, RsampleRobTag) 
        poke(c.io.fetchValidd0, 1) 

        step(1)
        poke(c.io.fetchValidd0, 0) 
        step(85)
        /*expect(d.io.decodeOpcode,  RexpectedOpcode)
        expect(d.io.decodeRd,      RexpectedRd)
        expect(d.io.decodeFunky3,  RexpectedFunky3)
        expect(d.io.decodeRs1,     RexpectedRs1)
        //expect(d.io.decodeRs2,     RexpectedRs2)
        expect(d.io.decodeFunky7,  RexpectedFunky7)
//        expect(d.io.decode_Imm, RexpectedImm_I_S)
        expect(d.io.decode_Imm,   Rexpected_Imm)
        expect(d.io.decodeRobTag,  RsampleRobTag)
        expect(d.io.decodeAddress, RsampleAddress)
        expect(d.io.decodeType,  1)*/
        poke(c.io.fetchInstructiond0, RsampleInstruction2)
        poke(c.io.fetchAddressd0, RsampleAddress2)
        poke(c.io.fetchRobTagd0, RsampleRobTag2) 
        poke(c.io.fetchValidd0, 1) 

        step(1)
        poke(c.io.fetchValidd0, 0) 
        step(85)
}
  
class intAllGenerator extends TestGenerator {
    def genMod(): Module = Module(new intAll())
      def genTest[T <: Module](c: T): Tester[T] =
            (new intAllTests(c.asInstanceOf[intAll])).asInstanceOf[Tester[T]]
}
