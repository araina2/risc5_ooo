package risc5
import scala.util.control.Breaks._
import Chisel._

class RegAliasTable extends Module {
  val io = new Bundle {
    val DecodeQueueSelect0 = Bool(INPUT)
    val DecodeQueueSelect1 = Bool(INPUT)
    val DecodeQueueSelect2 = Bool(INPUT)
    val DecodeQueueSelect3 = Bool(INPUT)
    val DecodeType0  = UInt(INPUT,3)
    val DecodeType1  = UInt(INPUT,3)
    val DecodeType2  = UInt(INPUT,3)
    val DecodeType3  = UInt(INPUT,3)
    val Decode_dest_0 = UInt(INPUT,5)
    val Decode_dest_1 = UInt(INPUT,5)
    val Decode_dest_2 = UInt(INPUT,5)
    val Decode_dest_3 = UInt(INPUT,5)
    val DecodeSource1_0 = UInt(INPUT,5)
    val DecodeSource1_1 = UInt(INPUT,5)
    val DecodeSource1_2 = UInt(INPUT,5)
    val DecodeSource1_3 = UInt(INPUT,5)
    val DecodeSource2_0 = UInt(INPUT,5)
    val DecodeSource2_1 = UInt(INPUT,5)
    val DecodeSource2_2 = UInt(INPUT,5)
    val DecodeSource2_3 = UInt(INPUT,5)
    val DecodeROBtag0 = UInt(INPUT,7)
    val DecodeROBtag1 = UInt(INPUT,7)
    val DecodeROBtag2 = UInt(INPUT,7)
    val DecodeROBtag3 = UInt(INPUT,7)
    val Decode_Imm_0  = UInt(INPUT,20)
    val Decode_Imm_1  = UInt(INPUT,20)
    val Decode_Imm_2  = UInt(INPUT,20)
    val Decode_Imm_3  = UInt(INPUT,20)
    val Decode_Opcode_0 = UInt(INPUT,7)
    val Decode_Opcode_1 = UInt(INPUT,7)
    val Decode_Opcode_2 = UInt(INPUT,7)
    val Decode_Opcode_3 = UInt(INPUT,7)
    val Decode_func3_0 = UInt(INPUT,7)
    val Decode_func3_1 = UInt(INPUT,7)
    val Decode_func3_2 = UInt(INPUT,7)
    val Decode_func3_3 = UInt(INPUT,7)
    val Decode_func7_0 = UInt(INPUT,7)
    val Decode_func7_1 = UInt(INPUT,7)
    val Decode_func7_2 = UInt(INPUT,7)
    val Decode_func7_3 = UInt(INPUT,7)

    val DecodeValid0 = UInt(INPUT,1)
    val DecodeValid1 = UInt(INPUT,1)
    val DecodeValid2 = UInt(INPUT,1)
    val DecodeValid3 = UInt(INPUT,1)
     
    //Tags broadcasted from the functional units
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
    //val LoadStoreDestVal1 = UInt(INPUT,64)
    val LoadStoreDestTag0 = UInt(INPUT,10)
    //val LoadStoreDestTag1 = UInt(INPUT,10)
    
    //Free Rows from the issue queues
    val IssueBroadcastFreeRow0 = UInt(INPUT,4)
    val IssueBroadcastFreeRow1 = UInt(INPUT,4)
    val IssueBroadcastFreeRow2 = UInt(INPUT,4)
    val IssueBroadcastFreeRow3 = UInt(INPUT,4)

    //Issue queue signal from the issue queue
    val IssueFull0 = UInt(INPUT,1)
    val IssueFull1 = UInt(INPUT,1)
    val IssueFull2 = UInt(INPUT,1)
    val IssueFull3 = UInt(INPUT,1)
    val IssueFull = UInt(INPUT,1)  //Tells if all the issue queues are full
                                                             
    val LoadStoreFull = UInt(INPUT,1)  //Tells if all the load store queue is full
    //Free Tag information from load/store queues
    val LoadStoreBroadcastFreeRow0 = UInt(INPUT,5)
    //val LoadStoreBroadcastFreeTag1 = UInt(INPUT,7)
    
    val FUBranchMispredict = Bool(INPUT)

    val DecodeStoreSelect0 = Bool(INPUT)
    val DecodeStoreSelect1 = Bool(INPUT)
    val DecodeStoreSelect2 = Bool(INPUT)
    val DecodeStoreSelect3 = Bool(INPUT)
    
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

//Added these signals as they are needed in the load/store queue for some reasons.
    val RenameLoadStoreValid0 = UInt(OUTPUT,1)
    val RenameLoadStoreValid1 = UInt(OUTPUT,1)
    val RenameLoadStoreValid2 = UInt(OUTPUT,1)
    val RenameLoadStoreValid3 = UInt(OUTPUT,1)

//Adding these signals as they are needed in issue queue
    val RenameIssueValid0 = UInt(OUTPUT,1)
    val RenameIssueValid1 = UInt(OUTPUT,1)
    val RenameIssueValid2 = UInt(OUTPUT,1)
    val RenameIssueValid3 = UInt(OUTPUT,1)

    val RenameType0  = UInt(OUTPUT,3)
    val RenameType1  = UInt(OUTPUT,3)
    val RenameType2  = UInt(OUTPUT,3)
    val RenameType3  = UInt(OUTPUT,3)
    //Added ports for testing whether the tags have the proper value 
    /*val Tmptag0 = UInt(OUTPUT,10)
    val Tmptag1 = UInt(OUTPUT,10)
    val Tmptag2 = UInt(OUTPUT,10)
    val Tmptag3 = UInt(OUTPUT,10)*/
  }
  //Valid array in the register map table
  val valid = Vec.fill(32) { Reg(init = UInt(1, width = 1)) }
  //Tag array in the register map table 
  //val tag = Vec.tabulate(32) { Reg(init = UInt(0, width = 7)) }
  val tag = Vec.fill(32) { Reg(init = UInt(0, width = 10)) }
  //Value field in the register map table 
  val value = Vec.fill(32) { Reg(init = UInt(0, width = 64)) }
  //ROB Tag in the register map table 
  val rob_tag = Vec.fill(32) { Reg(init = UInt(0, width = 7)) }

  //Internal structure for keeping track of the tag in the issue queue. It was previously used, not used now.
  //val arith_queue_tag_table = Vec.tabulate(64) { i => UInt(i+1) }
  //Internal structure for keeping track of valid entry in the issue queue
  val arith_queue_valid_table = Vec.fill(64) { Reg (init = UInt(1, width = 1))}
  //Internal structure for keeping track of the tag in the load/store queue
  val ldst_queue_valid_table = Vec.fill(32) { Reg (init = UInt(1, width = 1)) }
  //Internal structure for keeping track of the valid in the load/store queue
  //val ldst_queue_valid_table = Vec.tabulate(64) { i => UInt(1) }
  
  
   //Adding new registers here
    val Rename_dest = Vec.fill(4) { Reg(init = UInt(0, width = 5)) }
    val RenameROBtag = Vec.fill(4) { Reg(init = UInt(0, width = 7)) }
    val Rename_Imm = Vec.fill(4) { Reg(init = UInt(0, width = 20)) }
    val Rename_Opcode = Vec.fill(4) { Reg(init = UInt(0, width = 7)) }
    val Rename_func3 = Vec.fill(4) { Reg(init = UInt(0, width = 7)) }
    val Rename_func7 = Vec.fill(4) { Reg(init = UInt(0, width = 7)) }
    val RenameQueueSelect = Vec.fill(4) { Reg(init = UInt(0, width = 1)) } 
    val RenameStoreSelect = Vec.fill(4) { Reg(init = UInt(0, width = 1)) } 
    val RenameType = Vec.fill(4) { Reg(init = UInt(0, width = 3)) } 

    val RenameIssueValid = Vec.fill(4) { Reg(init = UInt(0, width = 1)) } 
    val RenameLoadStoreValid = Vec.fill(4) { Reg(init = UInt(0, width = 1)) } 

    //Rename_func3
    //Mandatory initialization for stupid Chisel 
    io.RenameSourceAValue0 := UInt(0)
    io.RenameSourceATag0 := UInt(0) 
    io.RenameSourceAValueValid0 := UInt(0)
    io.RenameSourceAValue1 := UInt(0)
    io.RenameSourceATag1 := UInt(0)
    io.RenameSourceAValueValid1 := UInt(0)
    io.RenameSourceAValue2 := UInt(0)
    io.RenameSourceATag2 := UInt(0)
    io.RenameSourceAValueValid2 := UInt(0)
    io.RenameSourceAValue3 := UInt(0)
    io.RenameSourceATag3 := UInt(0)
    io.RenameSourceAValueValid3 := UInt(0)

    io.RenameSourceBValue0 := UInt(0)
    io.RenameSourceBTag0 := UInt(0) 
    io.RenameSourceBValueValid0 := UInt(0)
    io.RenameSourceBValue1 := UInt(0)
    io.RenameSourceBTag1 := UInt(0)
    io.RenameSourceBValueValid1 := UInt(0)
    io.RenameSourceBValue2 := UInt(0)
    io.RenameSourceBTag2 := UInt(0)
    io.RenameSourceBValueValid2 := UInt(0)
    io.RenameSourceBValue3 := UInt(0)
    io.RenameSourceBTag3 := UInt(0)
    io.RenameSourceBValueValid3 := UInt(0)
    io.RenameDestTag0 := UInt(0)
    io.RenameDestTag1 := UInt(0)
    io.RenameDestTag2 := UInt(0)
    io.RenameDestTag3 := UInt(0)
    io.RenameIssueRowSelectTag0 := UInt(0)
    io.RenameIssueRowSelectTag1 := UInt(0)
    io.RenameIssueRowSelectTag2 := UInt(0)
    io.RenameIssueRowSelectTag3 := UInt(0)
    io.RenameLoadStoreRowSelect0 := UInt(0)
    io.RenameLoadStoreRowSelect1 := UInt(0)
    io.RenameLoadStoreRowSelect2 := UInt(0)
    io.RenameLoadStoreRowSelect3 := UInt(0)
    io.RenameValid := UInt(1)
    io.Rename_dest_0 := UInt(0)
    io.Rename_dest_1 := UInt(0)
    io.Rename_dest_2 := UInt(0)
    io.Rename_dest_3 := UInt(0)
    io.RenameROBtag0 := UInt(0)
    io.RenameROBtag1 := UInt(0)
    io.RenameROBtag2 := UInt(0)
    io.RenameROBtag3 := UInt(0)
    io.Rename_Imm_0  := UInt(0)
    io.Rename_Imm_1  := UInt(0)
    io.Rename_Imm_2  := UInt(0)
    io.Rename_Imm_3  := UInt(0)
    io.Rename_Opcode_0 := UInt(0)
    io.Rename_Opcode_1 := UInt(0)
    io.Rename_Opcode_2 := UInt(0)
    io.Rename_Opcode_3 := UInt(0)
    io.Rename_func3_0 := UInt(0)
    io.Rename_func3_1 := UInt(0)
    io.Rename_func3_2 := UInt(0)
    io.Rename_func3_3 := UInt(0)
    io.Rename_func7_0 := UInt(0)
    io.Rename_func7_1 := UInt(0)
    io.Rename_func7_2 := UInt(0)
    io.Rename_func7_3 := UInt(0)
    io.RenameQueueSelect0 := Bool(false)
    io.RenameQueueSelect1 := Bool(false)
    io.RenameQueueSelect2 := Bool(false)
    io.RenameQueueSelect3 := Bool(false)
    io.RenameStoreSelect0 := Bool(false)
    io.RenameStoreSelect1 := Bool(false)
    io.RenameStoreSelect2 := Bool(false)
    io.RenameStoreSelect3 := Bool(false)
    io.RenameLoadStoreValid0 := UInt(0)
    io.RenameLoadStoreValid1 := UInt(0)
    io.RenameLoadStoreValid2 := UInt(0)
    io.RenameLoadStoreValid3 := UInt(0)
    io.RenameIssueValid0 := UInt(0)
    io.RenameIssueValid1 := UInt(0)
    io.RenameIssueValid2 := UInt(0)
    io.RenameIssueValid3 := UInt(0)
   
    io.RenameType0 := UInt(0)
    io.RenameType1 := UInt(0)
    io.RenameType2 := UInt(0)
    io.RenameType3 := UInt(0)
    //Added ports to test whether the expected tags are generated 
    /*io.Tmptag0  := UInt(0)
    io.Tmptag1  := UInt(0)
    io.Tmptag2  := UInt(0)
    io.Tmptag3  := UInt(0)*/

    //offset := UInt(0)
 when (reset) {
  //printf("Coming in the reset")
  for (i <- 0 until 32) {
      valid(i) := UInt(1)
      value(i) := UInt(0)
      tag(i) := UInt(0)
      //ldst_queue_tag_table(i) := UInt(i+1)
      //ldst_queue_valid_table(i) := UInt(1)
   }

  for (i <- 0 until 64) {
    arith_queue_valid_table(i) := UInt(1)
  }
  for (i <- 0 until 32) {
    ldst_queue_valid_table(i) := UInt(1)
  }
 }

  /*for (i <- 0 until 64) {
    arith_queue_valid_table(i) := UInt(1)
  }*/
    for (i <- 0 until 32) {
    //Updating the value, tag and valid bit in the register map table when FUBroadcastTagX arrives
    when (tag(i) === io.FUBroadcastTag0 && (tag(i) != UInt(0))) { 
      valid(i) := UInt(1)
      tag(i) := UInt(0)
      value(i) := io.FUBroadcastValue0 
    }
    when (tag(i) === io.FUBroadcastTag1 && (tag(i) != UInt(0))) {
      valid(i) := UInt(1)
      tag(i) := UInt(0)
      value(i) := io.FUBroadcastValue1
    }
    when (tag(i) === io.FUBroadcastTag2 && (tag(i) != UInt(0))) { 
      valid(i) := UInt(1)
      tag(i) := UInt(0)
      value(i) := io.FUBroadcastValue2
    }
    when (tag(i) === io.FUBroadcastTag3 && (tag(i) != UInt(0))) { 
      valid(i) := UInt(1)
      tag(i) := UInt(0)
      value(i) := io.FUBroadcastValue3
    }
    //Updating the value, tag and valid bit in the register map table when LoadStoreDestTagX arrives
    when (tag(i) === io.LoadStoreDestTag0 && (tag(i) != UInt(0))) {
      valid(i) := UInt(1)
      tag(i) := UInt(0)
      value(i) := io.LoadStoreDestVal0
    } 
    /*when (tag(i) === io.LoadStoreDestTag1 && (tag(i) != UInt(0))) {
      valid(i) := UInt(1)
      tag(i) := UInt(0)
      value(i) := io.LoadStoreDestVal1
    }*/
    
    //Checking whether the current register matches the values of sources and updating the output signals 
    when (UInt(i) === io.DecodeSource1_0) {
      //printf("\nIn the decodesource1_0. The value is %d\n", valid(i))
      io.RenameSourceAValue0 := value(i)
      io.RenameSourceATag0 := tag(i) 
      io.RenameSourceAValueValid0 := valid(i)
    }
    when (UInt(i) === io.DecodeSource1_1) {
      //printf("\nIn the decodesource1_1. The value is %d\n", valid(i))
      io.RenameSourceAValue1 := value(i)
      io.RenameSourceATag1 := tag(i) 
      io.RenameSourceAValueValid1 := valid(i)
    }
    when (UInt(i) === io.DecodeSource1_2) {
      //printf("\nIn the decodesource1_2. The value is %d\n", valid(i))
      io.RenameSourceAValue2 := value(i)
      io.RenameSourceATag2 := tag(i) 
      io.RenameSourceAValueValid2 := valid(i)
    }
    when (UInt(i) === io.DecodeSource1_3) {
      //printf("\nIn the decodesource1_3. The value is %d\n", valid(i))
      io.RenameSourceAValue3 := value(i)
      io.RenameSourceATag3 := tag(i) 
      io.RenameSourceAValueValid3 := valid(i)
    }


    when (UInt(i) === io.DecodeSource2_0) {
      //printf("\nIn the decodesource2_0. The value is %d\n", valid(i))
      io.RenameSourceBValue0 := value(i)
      io.RenameSourceBTag0 := tag(i) 
      io.RenameSourceBValueValid0 := valid(i)
    }
    when (UInt(i) === io.DecodeSource2_1) {
      //printf("\nIn the decodesource2_1. The value is %d\n", valid(i))
      io.RenameSourceBValue1 := value(i)
      io.RenameSourceBTag1 := tag(i) 
      io.RenameSourceBValueValid1 := valid(i)
    }
    when (UInt(i) === io.DecodeSource2_2) {
      //printf("\nIn the decodesource2_2. The value is %d\n", valid(i))
      io.RenameSourceBValue2 := value(i)
      io.RenameSourceBTag2 := tag(i) 
      io.RenameSourceBValueValid2 := valid(i)
    }
    when (UInt(i) === io.DecodeSource2_3) {
      //printf("\nIn the decodesource2_3. The value is %d\n", valid(i))
      io.RenameSourceBValue3 := value(i)
      io.RenameSourceBTag3 := tag(i)
      io.RenameSourceBValueValid3 := valid(i)
    }

        //Updating the internal valid for the issue queue
        for ( j <- 0 until 16 ) {
           when ((Bool(io.IssueBroadcastFreeRow0 != UInt(0)) && (io.IssueBroadcastFreeRow0 === UInt(j)))) {
            arith_queue_valid_table(j) := UInt(1)
           }
           when (Bool(io.IssueBroadcastFreeRow1 != UInt(0)) && Bool(io.IssueBroadcastFreeRow1 === UInt(j))) {
            arith_queue_valid_table(j+16) := UInt(1)
           }
           when (Bool(io.IssueBroadcastFreeRow2 != UInt(0)) && Bool(io.IssueBroadcastFreeRow2 === UInt(j))) {
            arith_queue_valid_table(j+32) := UInt(1)
           }
           when (Bool(io.IssueBroadcastFreeRow3 != UInt(0)) && Bool(io.IssueBroadcastFreeRow3 === UInt(j))) {
            arith_queue_valid_table(j+48) := UInt(1)
           }
        }
        //Updating the internal valid for the load/store queue
        for (j <- 0 until 32) {
           when (Bool(io.LoadStoreBroadcastFreeRow0 != UInt(0)) && Bool(io.LoadStoreBroadcastFreeRow0 === UInt(j))) {
            ldst_queue_valid_table(j) := UInt(1)
          }
           /*when (Bool(io.LoadStoreBroadcastFreeTag1 != UInt(0)) && Bool(io.LoadStoreBroadcastFreeTag1 === UInt(j))) {
            ldst_queue_valid_table(j) := UInt(1)
          }*/
        }
      
   
  //Updating the tag for destination registers which is different from the issue queue tag
        when ((Bool(UInt(i) === io.Decode_dest_0 && io.DecodeValid0 === Bool(true)) && Bool(io.DecodeQueueSelect0 === Bool(false)))) {
          valid(i) := UInt(0)        
          io.RenameDestTag0 := (tag(i) + UInt(1)) % UInt(32)
          //count_issue_tag0 := (count_issue_tag0 + UInt(1)) % UInt(32)
          tag(i) := (tag(i) + UInt(1)) % UInt(32)//count_issue_tag0
          //io.Tmptag0 := tag(i)
          for (j <- 15 to 0 by -1) {
           // val temp_valid = arith_queue_valid_table(j)
            when (arith_queue_valid_table(j) === UInt(1)) {
              io.RenameIssueRowSelectTag0 := UInt(j)//UInt(temp_row_select)
            }
          }
          arith_queue_valid_table(io.RenameIssueRowSelectTag0) := UInt(0)
          //printf("\nThe value of issuerowselecttag1 is %d",io.RenameIssueRowSelectTag0)
          //printf("\nThe value of Renamedesttag0(i) in dest_0 is %d", io.RenameDestTag0)
          //printf("\nThe value of tag(i) in dest_0 is %d", tag(i))
          when (io.IssueFull0 === UInt(1) || io.IssueFull === UInt(1)) {
            io.RenameValid := UInt(0)
          }
          RenameIssueValid(0) := UInt(1)
          io.RenameIssueValid0 := RenameIssueValid(0)
        }
        .elsewhen (io.DecodeValid0 === Bool(false)) {
          RenameIssueValid(0) := UInt(0)
          io.RenameIssueValid0 := RenameIssueValid(0)
        }

        when ((Bool(UInt(i) === io.Decode_dest_1  && io.DecodeValid1 === Bool(true)) && Bool(io.DecodeQueueSelect1 === Bool(false)))) {
          valid(i) := UInt(0)        
          io.RenameDestTag1 := (tag(i) + UInt(1)) % UInt(32) + UInt(32)
          //count_issue_tag1 := (count_issue_tag1 + UInt(1)) % UInt(32) + UInt(32)
          tag(i) := (tag(i) + UInt(1)) % UInt(32) + UInt(32)
          //io.Tmptag1 := tag(i)
          //for (j <- 15 to 0 by -1) {
          //tag(i) := count_issue_tag1
         // val temp_row_select = UInt(i)
          for (j <- 15 to 0 by -1) {
            when (arith_queue_valid_table(j+16) === UInt(1)) {
              io.RenameIssueRowSelectTag1 := UInt(j+16)
            }
          }
          arith_queue_valid_table(io.RenameIssueRowSelectTag1) := UInt(0)
          //printf("\nThe value of issuerowselecttag1 is %d",io.RenameIssueRowSelectTag1);
          //printf("\nThe value of renamedesttag1 in dest_1 is %d",io.RenameDestTag1)
          //printf("\nThe value of tag(i) in dest_1 is %d", tag(i))
          when (io.IssueFull1 === UInt(1) || io.IssueFull === UInt(1)) {
            io.RenameValid := UInt(0)
          }
          RenameIssueValid(1) := UInt(1)
          io.RenameIssueValid1 := RenameIssueValid(1)
        }
        .elsewhen (io.DecodeValid1 === Bool(false)) {
          RenameIssueValid(1) := UInt(0)
          io.RenameIssueValid1 := RenameIssueValid(1)
        }

        when ((Bool(UInt(i) === io.Decode_dest_2  && io.DecodeValid2 === Bool(true)) && Bool(io.DecodeQueueSelect2 === Bool(false)))) {
          valid(i) := UInt(0)        
          io.RenameDestTag2 := (tag(i) + UInt(1)) % UInt(32) + UInt(64)
          //count_issue_tag2 := ((count_issue_tag2 + UInt(1)) % UInt(32)) + UInt(64)
          tag(i) := ((tag(i) + UInt(1)) % UInt(32)) + UInt(64)
          //io.Tmptag2 := tag(i)
          //for (j <- 15 to 0 by -1) {
          //tag(i) := count_issue_tag2
          for (j <- 15 to 0 by -1) {
            when (arith_queue_valid_table(j+32) === UInt(1)) {
              io.RenameIssueRowSelectTag2 := UInt(j+32)
            }
          }
          arith_queue_valid_table(io.RenameIssueRowSelectTag2) := UInt(0)
          //io.RenameIssueRowSelectTag2 := UInt(temp_row_select)
          //printf("\nThe value of issuerowselecttag2 is %d",io.RenameIssueRowSelectTag2);
          //printf("\nThe value of renamedesttag2 in dest_2 is %d",io.RenameDestTag2)
          //printf("\nThe value of tag(i) in dest_2 is %d", tag(i))
          when (io.IssueFull2 === UInt(1) || io.IssueFull === UInt(1)) {
            io.RenameValid := UInt(0)
          }
          RenameIssueValid(2) := UInt(1)
          io.RenameIssueValid2 := RenameIssueValid(2)
        }
        .elsewhen (io.DecodeValid2 === Bool(false)) {
          RenameIssueValid(2) := UInt(0)
          io.RenameIssueValid2 := RenameIssueValid(2)
        }

        when ((Bool(UInt(i) === io.Decode_dest_3  && io.DecodeValid3 === Bool(true)) && Bool(io.DecodeQueueSelect3 === Bool(false)))) {
          valid(i) := UInt(0)        
          io.RenameDestTag3 := (tag(i) + UInt(1)) % UInt(32) + UInt(96)
          //count_issue_tag3 := (count_issue_tag3 + UInt(1)) % UInt(32) + UInt(96)
          tag(i) := (tag(i) + UInt(1)) % UInt(32) + UInt(96)
          //io.Tmptag3 := tag(i)
          //for (j <- 15 to 0 by -1) {
          //tag(i) := count_issue_tag3
          //val temp_row_select = UInt(i)
          for (j <- 15 to 0 by -1) {
            when (arith_queue_valid_table(j+48) === UInt(1)) {
              io.RenameIssueRowSelectTag3 := UInt(j+48)
            }
          }
          arith_queue_valid_table(io.RenameIssueRowSelectTag3) := UInt(0)
          //printf("\nThe value of issuerowselecttag3 is %d",io.RenameIssueRowSelectTag3);
          //printf("\nThe value of renamedesttag3 in dest_3 is %d",io.RenameDestTag3)
          //printf("\nThe value of tag(i) in dest_3 is %d", tag(i))
          when (io.IssueFull3 === UInt(1) || io.IssueFull === UInt(1)) {
            io.RenameValid := UInt(0)
          }
          RenameIssueValid(3) := UInt(1)
          io.RenameIssueValid3 := RenameIssueValid(3)
        }
        .elsewhen (io.DecodeValid3 === Bool(false)) {
          RenameIssueValid(3) := UInt(0)
          io.RenameIssueValid3 := RenameIssueValid(3)
        }

        when ((Bool(UInt(i) === io.Decode_dest_0) && Bool(io.DecodeQueueSelect0 === Bool(true)))) {
          when (io.DecodeStoreSelect0 === Bool(false)) { 
            valid(i) := UInt(0)
            io.RenameDestTag0 := (tag(i) + UInt(1)) % UInt(32) + UInt(128)
            //count_loadstore_tag0 := (count_loadstore_tag0 + UInt(1)) % UInt(32) + UInt(128)
            tag(i) := (tag(i) + UInt(1)) % UInt(32) + UInt(128)//count_issue_tag0
            //io.Tmptag0 := tag(i)
          }
          
          for (j <- 31 to 0 by -1) {
            when (ldst_queue_valid_table(j) === UInt(1)) {
            io.RenameLoadStoreRowSelect0 := UInt(j)
            }
          }

          ldst_queue_valid_table(io.RenameLoadStoreRowSelect0) := UInt(0)
          //printf("The value of loadstorerowselecttag0 is %d",io.RenameLoadStoreRowSelect0);
          //printf("\nThe value of tag(i) in Load Store dest_0 is %d", tag(i))
          //printf("\nThe value of renameloadstore(i) in Load Store dest_0 is %d", io.RenameDestTag0)
          RenameLoadStoreValid(0) := UInt(1)
          io.RenameLoadStoreValid0 := RenameIssueValid(0)
          
          when (io.LoadStoreFull === UInt(1)) {
            io.RenameValid := UInt(0)
          }
        }
        .elsewhen (io.DecodeValid0 === Bool(false)) {
          RenameLoadStoreValid(0) := UInt(0)
          io.RenameLoadStoreValid0 := RenameIssueValid(0)
        }

        when ((Bool(UInt(i) === io.Decode_dest_1) && Bool(io.DecodeQueueSelect1 === Bool(true)))) {
          when (io.DecodeStoreSelect1 === Bool(false)) { 
            valid(i) := UInt(0)
            io.RenameDestTag1 := (tag(i) + UInt(1)) % UInt(32) + UInt(160)
            //count_loadstore_tag1 := (count_loadstore_tag1 + UInt(1)) % UInt(32) + UInt(160)
            tag(i) := (tag(i) + UInt(1)) % UInt(32) + UInt(160)//count_issue_tag0
            //io.Tmptag1 := tag(i)
          }
          
          for (j <- 31 to 0 by -1) {
            when (ldst_queue_valid_table(j) === UInt(1)) {
              when (io.DecodeQueueSelect0 === Bool(true)) {
                io.RenameLoadStoreRowSelect1 := UInt(j+1)
              }
              .otherwise {
                io.RenameLoadStoreRowSelect1 := UInt(j)
              }
            }
          }
          /*when (io.DecodeQueueSelect0 === Bool(true)) {
            val temp = io.RenameLoadStoreRowSelect1 + UInt(1)
            io.RenameLoadStoreRowSelect1 := temp
          }*/
          ldst_queue_valid_table(io.RenameLoadStoreRowSelect1) := UInt(0)
          RenameLoadStoreValid(1) := UInt(1)
          io.RenameLoadStoreValid1 := RenameIssueValid(1)
          //printf("The value of loadstorerowselecttag1 is %d",io.RenameLoadStoreRowSelect1);
          //printf("\nThe value of tag(i) in Load Store dest_1 is %d", tag(i))
          //printf("\nThe value of renameloadstore(i) in Load Store dest_1 is %d", io.RenameDestTag1)
          when (io.LoadStoreFull === UInt(1)) {
            io.RenameValid := UInt(0)
          }
        }
        .elsewhen (io.DecodeValid1 === Bool(false)) {
          RenameLoadStoreValid(1) := UInt(0)
          io.RenameLoadStoreValid1 := RenameIssueValid(1)
        }

        when ((Bool(UInt(i) === io.Decode_dest_2) && Bool(io.DecodeQueueSelect2 === Bool(true)))) {
          when (io.DecodeStoreSelect2 === Bool(false)) { 
            valid(i) := UInt(0)
            io.RenameDestTag2 := (tag(i) + UInt(1)) % UInt(32) + UInt(192)
            tag(i) := (tag(i) + UInt(1)) % UInt(32) + UInt(192)
            //io.Tmptag2 := tag(i)
            //tag(i) := count_loadstore_tag2
          }
          
          for (j <- 31 to 0 by -1) {
            when (ldst_queue_valid_table(j) === UInt(1)) {
              when (io.DecodeQueueSelect0 === Bool(true) && io.DecodeQueueSelect1 === Bool(true)) {
                io.RenameLoadStoreRowSelect2 := UInt(j+2)
              }
              .elsewhen (io.DecodeQueueSelect0 === Bool(true) || io.DecodeQueueSelect1 === Bool(true)) {
                io.RenameLoadStoreRowSelect2 := UInt(j+1)
              }
              .otherwise {
                io.RenameLoadStoreRowSelect2 := UInt(j)
              }
            }
          }
          /*when (io.DecodeQueueSelect0 === Bool(true)) {
            val temp = io.RenameLoadStoreRowSelect2 + UInt(1)
            io.RenameLoadStoreRowSelect2 := temp
          }
          when (io.DecodeQueueSelect1 === Bool(true)) {
            val temp = io.RenameLoadStoreRowSelect2 + UInt(1)
            io.RenameLoadStoreRowSelect2 := temp
          }*/
          ldst_queue_valid_table(io.RenameLoadStoreRowSelect2) := UInt(0)
          RenameLoadStoreValid(2) := UInt(1)
          io.RenameLoadStoreValid2 := RenameIssueValid(2)
          //printf("The value of loadstorerowselecttag2 is %d",io.RenameLoadStoreRowSelect2);
          //printf("\nThe value of tag(i) in Load Store dest_1 is %d", tag(i))
          //printf("\nThe value of renameloadstore(i) in Load Store dest_2 is %d", io.RenameDestTag2)
          when (io.LoadStoreFull === UInt(1)) {
            io.RenameValid := UInt(0)
          }
        }
        .elsewhen (io.DecodeValid2 === Bool(false)) {
          RenameLoadStoreValid(2) := UInt(0)
          io.RenameLoadStoreValid2 := RenameIssueValid(2)
        }

        when ((Bool(UInt(i) === io.Decode_dest_3) && Bool(io.DecodeQueueSelect3 === Bool(true)))) {
          when (io.DecodeStoreSelect3 === Bool(false)) { 
            valid(i) := UInt(0)
            io.RenameDestTag3 := (tag(i) + UInt(1)) % UInt(32) + UInt(224)
            tag(i) := (tag(i) + UInt(1)) % UInt(32) + UInt(224)
            //io.Tmptag3 := tag(i)
            //tag(i) := count_loadstore_tag3
          }
          
          for (j <- 31 to 0 by -1) {
            when (ldst_queue_valid_table(j) === UInt(1)) {
              when (io.DecodeQueueSelect0 === Bool(true) && io.DecodeQueueSelect1 === Bool(true) && io.DecodeQueueSelect2 === Bool(true)) {
                io.RenameLoadStoreRowSelect3 := UInt(j+3)
              }
              .elsewhen ((io.DecodeQueueSelect0 === Bool(true) && io.DecodeQueueSelect1 === Bool(true)) || (io.DecodeQueueSelect0 === Bool(true) && io.DecodeQueueSelect2 === Bool(true)) || (io.DecodeQueueSelect1 === Bool(true) && io.DecodeQueueSelect2 === Bool(true))) {
                io.RenameLoadStoreRowSelect3 := UInt(j+2)
              }
              .elsewhen(io.DecodeQueueSelect0 === Bool(true) || io.DecodeQueueSelect1 === Bool(true) || io.DecodeQueueSelect2 === Bool(true)) {
                io.RenameLoadStoreRowSelect3 := UInt(j+1)
               }
              .otherwise {
                io.RenameLoadStoreRowSelect3 := UInt(j)
              }
            }
          }
          /*
          when (io.DecodeQueueSelect0 === Bool(true)) {
            val temp = io.RenameLoadStoreRowSelect3 + UInt(1)
            io.RenameLoadStoreRowSelect3 := temp
          }
          when (io.DecodeQueueSelect1 === Bool(true)) {
            val temp = io.RenameLoadStoreRowSelect3 + UInt(1)
            io.RenameLoadStoreRowSelect3 := temp
          }
          when (io.DecodeQueueSelect2 === Bool(true)) {
            val temp = io.RenameLoadStoreRowSelect3 + UInt(1)
            io.RenameLoadStoreRowSelect3 := temp
          }*/
          ldst_queue_valid_table(io.RenameLoadStoreRowSelect3) := UInt(0)
          RenameLoadStoreValid(3) := UInt(1)
          io.RenameLoadStoreValid3 := RenameIssueValid(3)

          //printf("The value of loadstorerowselecttag3 is %d",io.RenameLoadStoreRowSelect3);
          //printf("\nThe value of tag(i) in Load Store dest_1 is %d", tag(i))
          //printf("\nThe value of renameloadstore(i) in Load Store dest_3 is %d", io.RenameDestTag3)
          when (io.LoadStoreFull === UInt(1)) {
            io.RenameValid := UInt(0)
          }
        }
        .elsewhen (io.DecodeValid3 === Bool(false)) {
          RenameLoadStoreValid(3) := UInt(0)
          io.RenameLoadStoreValid3 := RenameIssueValid(3)
        }
  }
    Rename_dest(0) := io.Decode_dest_0
    Rename_dest(1) := io.Decode_dest_1
    Rename_dest(2) := io.Decode_dest_2
    Rename_dest(3) := io.Decode_dest_3
    RenameROBtag(0) := io.DecodeROBtag0
    RenameROBtag(1) := io.DecodeROBtag1
    RenameROBtag(2) := io.DecodeROBtag2
    RenameROBtag(3) := io.DecodeROBtag3
    Rename_Imm(0) := io.Decode_Imm_0
    Rename_Imm(1) := io.Decode_Imm_1
    Rename_Imm(2) := io.Decode_Imm_2
    Rename_Imm(3) := io.Decode_Imm_3
    Rename_Opcode(0) := io.Decode_Opcode_0
    Rename_Opcode(1) := io.Decode_Opcode_1
    Rename_Opcode(2) := io.Decode_Opcode_2
    Rename_Opcode(3) := io.Decode_Opcode_3
    Rename_func3(0) := io.Decode_func3_0
    Rename_func3(1) := io.Decode_func3_1
    Rename_func3(2) := io.Decode_func3_2
    Rename_func3(3) := io.Decode_func3_3
    Rename_func7(0) := io.Decode_func7_0
    Rename_func7(1) := io.Decode_func7_1
    Rename_func7(2) := io.Decode_func7_2
    Rename_func7(3) := io.Decode_func7_3
    RenameQueueSelect(0) := io.RenameQueueSelect0 
    RenameQueueSelect(1) := io.RenameQueueSelect1 
    RenameQueueSelect(2) := io.RenameQueueSelect2 
    RenameQueueSelect(3) := io.RenameQueueSelect3 
    RenameStoreSelect(0) := io.RenameStoreSelect0 
    RenameStoreSelect(1) := io.RenameStoreSelect1 
    RenameStoreSelect(2) := io.RenameStoreSelect2 
    RenameStoreSelect(3) := io.RenameStoreSelect3 
    RenameType(0) := io.DecodeType0
    RenameType(1) := io.DecodeType1
    RenameType(2) := io.DecodeType2
    RenameType(3) := io.DecodeType3

    io.Rename_dest_0 := Rename_dest(0)
    io.Rename_dest_1 := Rename_dest(1)
    io.Rename_dest_2 := Rename_dest(2)
    io.Rename_dest_3 := Rename_dest(3)
    io.RenameROBtag0 := RenameROBtag(0)
    io.RenameROBtag1 := RenameROBtag(1)
    io.RenameROBtag2 := RenameROBtag(2)
    io.RenameROBtag3 := RenameROBtag(3)
    io.Rename_Imm_0  := Rename_Imm(0)
    io.Rename_Imm_1  := Rename_Imm(1)
    io.Rename_Imm_2  := Rename_Imm(2)
    io.Rename_Imm_3  := Rename_Imm(3)
    io.Rename_Opcode_0 := Rename_Opcode(0)
    io.Rename_Opcode_1 := Rename_Opcode(1)
    io.Rename_Opcode_2 := Rename_Opcode(2)
    io.Rename_Opcode_3 := Rename_Opcode(3)
    io.Rename_func3_0 := Rename_func3(0)
    io.Rename_func3_1 := Rename_func3(1)
    io.Rename_func3_2 := Rename_func3(2)
    io.Rename_func3_3 := Rename_func3(3)
    io.Rename_func7_0 := Rename_func7(0)
    io.Rename_func7_1 := Rename_func7(1)
    io.Rename_func7_2 := Rename_func7(2)
    io.Rename_func7_3 := Rename_func7(3)
    
    io.RenameQueueSelect0 := RenameQueueSelect(0)
    io.RenameQueueSelect1 := RenameQueueSelect(1)
    io.RenameQueueSelect2 := RenameQueueSelect(2)
    io.RenameQueueSelect3 := RenameQueueSelect(3)

    io.RenameStoreSelect0 := RenameStoreSelect(0)
    io.RenameStoreSelect1 := RenameStoreSelect(1)
    io.RenameStoreSelect2 := RenameStoreSelect(2)
    io.RenameStoreSelect3 := RenameStoreSelect(3)

    io.RenameType0 := RenameType(0)
    io.RenameType1 := RenameType(1)
    io.RenameType2 := RenameType(2)
    io.RenameType3 := RenameType(3)
}

class RegAliasTableTests(c: RegAliasTable) extends Tester(c) {
  
  /*poke(c.io.DecodeQueueSelect0, 1 )
  poke(c.io.DecodeStoreSelect0, 1 )
  poke(c.io.DecodeQueueSelect1, 1 )
  poke(c.io.DecodeQueueSelect2, 1 )
  poke(c.io.DecodeQueueSelect3, 1 )
  poke(c.io.Decode_dest_0, 1)
  poke(c.io.Decode_dest_1, 4)
  poke(c.io.Decode_dest_2, 8)
  poke(c.io.Decode_dest_3, 10)
  poke(c.io.DecodeSource1_0, 2)
  poke(c.io.DecodeSource2_0, 3)
  poke(c.io.DecodeSource1_1, 5) 
  poke(c.io.DecodeSource2_1, 7)
  poke(c.io.DecodeSource1_2, 6) 
  poke(c.io.DecodeSource2_2, 9)
  poke(c.io.DecodeSource1_3, 1) 
  poke(c.io.DecodeSource2_3, 5)
  poke(c.io.DecodeROBtag0, 5)
  poke(c.io.DecodeROBtag1, 6)
  poke(c.io.DecodeROBtag2, 7)
  poke(c.io.DecodeROBtag3, 8)
  poke(c.io.Decode_Imm_0, 9)
  poke(c.io.Decode_Imm_1, 10)
  poke(c.io.Decode_Imm_2, 11)
  poke(c.io.Decode_Imm_3, 12)
  poke(c.io.IssueFull1, 1)*/
  //expect(c.io.Tmptag0, 0)
  //expect(c.io.Tmptag1, 0)
  //expect(c.io.Tmptag2, 0)
  //expect(c.io.Tmptag3, 0)
  
  /*step(1)
  expect(c.io.Decode_dest_0, 1)
  expect(c.io.DecodeSource1_0, 5)
  //expect(c.io.DecodeSource2_0, 3)
  step(1)
  
  poke(c.io.DecodeQueueSelect0, 1 )
  poke(c.io.DecodeQueueSelect1, 1 )
  poke(c.io.DecodeQueueSelect2, 1 )
  poke(c.io.DecodeQueueSelect3, 1 )*/
  //expect(c.io.RenameSourceAValueValid0, 1)
  //expect(c.io.RenameSourceBValueValid0, 1)
  /*expect(c.io.RenameSourceAValueValid1, 1)
  expect(c.io.RenameSourceBValueValid1, 1)
  expect(c.io.RenameSourceAValueValid2, 1)
  expect(c.io.RenameSourceBValueValid2, 1)
  expect(c.io.RenameSourceAValueValid3, 1)
  expect(c.io.RenameSourceBValueValid3, 1)
  //expect(c.io.Decode_dest_0, 1)
  expect(c.io.Decode_dest_1, 4)
  expect(c.io.Decode_dest_2, 8)
  expect(c.io.Decode_dest_3, 10)
  expect(c.io.DecodeROBtag0, 5)
  expect(c.io.DecodeROBtag1, 6)
  expect(c.io.DecodeROBtag2, 7)
  expect(c.io.DecodeROBtag3, 8)
  expect(c.io.Decode_Imm_0, 9)
  expect(c.io.Decode_Imm_1, 10)
  expect(c.io.Decode_Imm_2, 11)
  expect(c.io.Decode_Imm_3, 12)
  expect(c.io.RenameValid, 0)
  //expect(c.io.Tmptag0, 129)
  //expect(c.io.Tmptag1, 161)
  //expect(c.io.Tmptag2, 193)
  //expect(c.io.Tmptag3, 225)
  
  
  //peek(c.io.RenameDestTag0)  
  //peek(c.io.RenameSourceAValue0)  

  poke(c.io.Decode_dest_0, 1)
  poke(c.io.Decode_dest_1, 6)
  poke(c.io.Decode_dest_2, 3)
  poke(c.io.Decode_dest_3, 7)
  poke(c.io.DecodeSource1_0, 4)
  poke(c.io.DecodeSource2_0, 8)
  poke(c.io.DecodeSource1_1, 10) 
  poke(c.io.DecodeSource2_1, 15)
  poke(c.io.DecodeSource1_2, 2) 
  poke(c.io.DecodeSource2_2, 9)
  poke(c.io.DecodeSource1_3, 5) 
  poke(c.io.DecodeSource2_3, 6)
  poke(c.io.IssueFull2, 1)
 
  step (1);

  expect(c.io.RenameSourceAValueValid0, 0)
  expect(c.io.RenameSourceBValueValid0, 0)
  expect(c.io.RenameSourceAValueValid1, 0)
  expect(c.io.RenameSourceBValueValid1, 1)
  expect(c.io.RenameSourceAValueValid2, 1)
  expect(c.io.RenameSourceBValueValid2, 1)
  expect(c.io.RenameSourceAValueValid3, 1)
  expect(c.io.RenameSourceBValueValid3, 0)
  expect(c.io.RenameValid, 0)
  
  //expect(c.io.RenameSourceBValue3, 0)
  //expect(c.io.RenameSourceBValueValid3, 1)
  //expect(c.io.RenameSourceBValueValid0, 0)


  //expect(c.io.RenameSourceBValue3, 0)
  //expect(c.io.RenameSourceBValueValid3, 1)
  //expect(c.io.RenameSourceBValueValid0, 0)*/
}

class RegAliasTableGenerator extends TestGenerator {
    def genMod(): Module = Module(new RegAliasTable())
      def genTest[T <: Module](c: T): Tester[T] =
            (new RegAliasTableTests(c.asInstanceOf[RegAliasTable])).asInstanceOf[Tester[T]]
}
