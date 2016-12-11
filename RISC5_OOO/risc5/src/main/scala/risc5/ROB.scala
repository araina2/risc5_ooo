package risc5
import Chisel._

class ROB extends Module {
   val io = new Bundle {
   // Inputs coming from rename
   val RenameDestTag0 = UInt(INPUT, 10)
   val RenameDestTag1 = UInt(INPUT, 10)  
   val RenameDestTag2 = UInt(INPUT, 10)
   val RenameDestTag3 = UInt(INPUT, 10)
   val Rename_dest_0 = UInt(INPUT,5)
   val Rename_dest_1 = UInt(INPUT,5)
   val Rename_dest_2 = UInt(INPUT,5)
   val Rename_dest_3 = UInt(INPUT,5)
   val RenameValid = UInt(INPUT, 1)
   val RenameROBtag0 = UInt(INPUT,7)
   val RenameROBtag1 = UInt(INPUT,7)
   val RenameROBtag2 = UInt(INPUT,7)
   val RenameROBtag3 = UInt(INPUT,7)
   val RenameQueueSelect0 = Bool(INPUT)
   val RenameQueueSelect1 = Bool(INPUT)
   val RenameQueueSelect2 = Bool(INPUT)
   val RenameQueueSelect3 = Bool(INPUT)
   val RenameStoreSelect0 = Bool(INPUT)
   val RenameStoreSelect1 = Bool(INPUT)
   val RenameStoreSelect2 = Bool(INPUT)
   val RenameStoreSelect3 = Bool(INPUT)
    
   val RenameIssueValid0 = UInt(INPUT,1)
   val RenameIssueValid1 = UInt(INPUT,1)
   val RenameIssueValid2 = UInt(INPUT,1)
   val RenameIssueValid3 = UInt(INPUT,1)
    
   val RenameLoadStoreValid0 = UInt(INPUT,1)
   val RenameLoadStoreValid1 = UInt(INPUT,1)
   val RenameLoadStoreValid2 = UInt(INPUT,1)
   val RenameLoadStoreValid3 = UInt(INPUT,1)
    
    //Broadcast from the functional units
   val FUBroadcastValue0 = UInt(INPUT,64)
   val FUBroadcastTag0 = UInt(INPUT,10)
   val FUBroadcastValue1 = UInt(INPUT,64)
   val FUBroadcastTag1 = UInt(INPUT,10)
   val FUBroadcastValue2 = UInt(INPUT,64)
   val FUBroadcastTag2 = UInt(INPUT,10)
   val FUBroadcastValue3 = UInt(INPUT,64)
   val FUBroadcastTag3 = UInt(INPUT,10)

   //Inputs coming from load/store queue
   val LoadStoreDestVal0 = UInt(INPUT,64)
   //val LoadStoreDestVal1 = UInt(INPUT,64)
   val LoadStoreDestTag0 = UInt(INPUT,10)
   //val LoadStoreDestTag1 = UInt(INPUT,10)
   val LoadStoreDestReg0 = UInt(INPUT,5)
   //val LoadStoreDestReg1 = UInt(INPUT,5)
   val LoadStoreDestAddress0 = UInt(INPUT,64)
   ////val LoadStoreDestAddress1 = UInt(INPUT,64)

   //Inputs from dmem 
    val dmembusy = Bool(INPUT)
    

   //Output signals from ROB
   val ROBStoreSelect0 = Bool(OUTPUT)
   //val ROBStoreSelect1 = Bool(OUTPUT)
   //val ROBStoreSelect2 = Bool(OUTPUT)
   //val ROBStoreSelect3 = Bool(OUTPUT)
   val ROBMemAddress0 = UInt(OUTPUT,64) 
   //val ROBMemAddress1 = UInt(OUTPUT,64) 
   //val ROBMemAddress2 = UInt(OUTPUT,64) 
   //val ROBMemAddress3 = UInt(OUTPUT,64) 
   val ROBValue0 = UInt(OUTPUT,64) 
   //val ROBValue1 = UInt(OUTPUT,64) 
   //val ROBValue2 = UInt(OUTPUT,64) 
   //val ROBValue3 = UInt(OUTPUT,64) 
   val ROBValueValid0 = UInt(OUTPUT,1) 
   //val ROBValueValid1 = UInt(OUTPUT,1) 
   //val ROBValueValid2 = UInt(OUTPUT,1) 
   //val ROBValueValid3 = UInt(OUTPUT,1)

   //Outputs to the memory for commit
   // val din  = UInt(OUTPUT, 64)
   // val addr  = UInt(OUTPUT, 64)
   // val en = Bool(OUTPUT)
   // val wr = Bool(OUTPUT)
   }
   
   io.ROBStoreSelect0 := Bool(false)
   //io.ROBStoreSelect1 := Bool(false)
   //io.ROBStoreSelect2 := Bool(false)
   //io.ROBStoreSelect3 := Bool(false)
   io.ROBMemAddress0 := UInt(0) 
   //io.ROBMemAddress1 := UInt(0) 
   //io.ROBMemAddress2 := UInt(0) 
   //io.ROBMemAddress3 := UInt(0) 
   io.ROBValue0 := UInt(0) 
   //io.ROBValue1 := UInt(0) 
   //io.ROBValue2 := UInt(0) 
   //io.ROBValue3 := UInt(0) 
   io.ROBValueValid0 := UInt(0) 
   //io.ROBValueValid1 := UInt(0) 
   //io.ROBValueValid2 := UInt(0) 
   //io.ROBValueValid3 := UInt(0) 
 
   //Valid array in the ROB
  val valid = Vec.fill(96) { Reg(init = UInt(0, width = 1)) }
  //Tag array in the ROB 
  val tag = Vec.fill(96) { Reg(init = UInt(0, width = 10)) }
  //Value field in the ROB 
  val value = Vec.fill(96) { Reg(init = UInt(0, width = 64)) }
  //ROB Tag
  val rob_tag = Vec.fill(96) { Reg(init = UInt(0, width = 7)) }
  //Field telling that whether an instruction is store or not 
  val store_select = Vec.fill(96) { Reg(init = UInt(0, width = 1)) }
  //Field telling the address of store  
  val store_address = Vec.fill(96) { Reg(init = UInt(0, width = 64)) }

  //Updating the value and valid bit in case of a broadcast
  for (i <- 0 until 96) {
    when((io.FUBroadcastTag0 === tag(i)) && (io.FUBroadcastTag0 != UInt(0))) {
      //printf("\nComing into io.FUBroadcastTag0 check\n");
      value(i) := io.FUBroadcastValue0
      valid(i) := UInt(1)
    }
    when((io.FUBroadcastTag1 === tag(i)) && (io.FUBroadcastTag1 != UInt(0))) {
      //printf("\nComing into io.FUBroadcastTag1 check\n");
      value(i) := io.FUBroadcastValue1
      valid(i) := UInt(1)
    }
    when((io.FUBroadcastTag2 === tag(i)) && (io.FUBroadcastTag2 != UInt(0))) {
      //printf("\nComing into io.FUBroadcastTag2 check\n");
      value(i) := io.FUBroadcastValue2
      valid(i) := UInt(1)
    }
    when((io.FUBroadcastTag3 === tag(i)) && (io.FUBroadcastTag3 != UInt(0))) {
      //printf("\nComing into io.FUBroadcastTag3 check\n");
      value(i) := io.FUBroadcastValue3
      valid(i) := UInt(1)
    }
    when((io.LoadStoreDestTag0 === tag(i)) && (io.LoadStoreDestTag0 != UInt(0))) {
      //printf("\nComing into io.LoadStoreDestTag0 check\n");
      value(i) := io.LoadStoreDestVal0
      valid(i) := UInt(1)
      store_address(i) := io.LoadStoreDestAddress0
    }
  }

  //Putting the new entry in the ROB
  val index = UInt(width=10) //Reg(init = UInt(0, width = 10)
  index := UInt(0)
  for (i <- 95 to 0 by -1) {
    when(tag(i) === UInt(0)) {
      index := UInt(i)
    }
  }

    //printf("\nThe value of index for 1st instruction is %d\n", index)
   //For 1st instruction 
    //printf("\nThe value of 1st index is %d\n", index)
    when(io.RenameDestTag0 != UInt(0) && ((io.RenameIssueValid0 === UInt(1)) || (io.RenameLoadStoreValid0 === UInt(1)))) {
    valid(index) := UInt(0)
    value(index) := UInt(0)
    tag(index) := io.RenameDestTag0
    store_select(index) := io.RenameStoreSelect0

    /*when(index < UInt(4) && (io.RenameStoreSelect0 === Bool(true)) && index === UInt(0)) {
        io.ROBStoreSelect0 := Bool(true)
    }
    when(index < UInt(4) && (io.RenameStoreSelect1 === Bool(true)) && index === UInt(1)) {
        io.ROBStoreSelect1 := Bool(true)
    }
    when(index < UInt(4) && (io.RenameStoreSelect2 === Bool(true)) && index === UInt(2)) {
        io.ROBStoreSelect2 := Bool(true)
    }
    when(index < UInt(4) && (io.RenameStoreSelect3 === Bool(true)) && index === UInt(3)) {
        printf("Coming to the 1st index")
        io.ROBStoreSelect3 := Bool(true)
    }*/

    rob_tag(index) := io.RenameROBtag0
    }
    
    //For 2nd instruction
    when(io.RenameDestTag0 != UInt(0) && io.RenameDestTag1 != UInt(0) && ((io.RenameIssueValid1 === UInt(1)) || (io.RenameLoadStoreValid1 === UInt(1)))) {
      val res = index + UInt(1)
      //printf("\nThe value of 2nd index if one is there is %d\n", res)
      valid(res) := UInt(0)
      value(res) := UInt(0)
      tag(res) := io.RenameDestTag1
      store_select(res) := io.RenameStoreSelect1

      /*when(res < UInt(4) && (io.RenameStoreSelect0 === Bool(true)) && index === UInt(0)) {
          io.ROBStoreSelect0 := Bool(true)
      }
      when(res < UInt(4) && (io.RenameStoreSelect1 === Bool(true)) && index === UInt(1)) {
          io.ROBStoreSelect1 := Bool(true)
      }
      when(res < UInt(4) && (io.RenameStoreSelect2 === Bool(true)) && index === UInt(2)) {
          io.ROBStoreSelect2 := Bool(true)
      }
      when(res < UInt(4) && (io.RenameStoreSelect3 === Bool(true)) && index === UInt(3)) {
          printf("Coming to the 2nd index")
          io.ROBStoreSelect3 := UInt(1)
      }*/

      rob_tag(res) := io.RenameROBtag1
    }
    .elsewhen(io.RenameDestTag0 === UInt(0) && io.RenameDestTag1 != UInt(0) && ((io.RenameIssueValid1 === UInt(1)) || (io.RenameLoadStoreValid1 === UInt(1)))) {
      //printf("\nThe value of 2nd index if one is not there is %d\n", index)
      valid(index) := UInt(0)
      value(index) := UInt(0)
      tag(index) := io.RenameDestTag1
      store_select(index) := io.RenameStoreSelect1

      /*when(index < UInt(4) && (io.RenameStoreSelect0 === Bool(true)) && index === UInt(0)) {
          io.ROBStoreSelect0 := Bool(true)
      }
      when(index < UInt(4) && (io.RenameStoreSelect1 === Bool(true)) && index === UInt(1)) {
          io.ROBStoreSelect1 := Bool(true)
      }
      when(index < UInt(4) && (io.RenameStoreSelect2 === Bool(true)) && index === UInt(2)) {
          io.ROBStoreSelect2 := Bool(true)
      }
      when(index < UInt(4) && (io.RenameStoreSelect3 === Bool(true)) && index === UInt(3)) {
          io.ROBStoreSelect3 := Bool(true)
      }*/

      rob_tag(index) := io.RenameROBtag1
    }

    //For 3rd instruction
    when(io.RenameDestTag0 != UInt(0) && io.RenameDestTag1 != UInt(0) && io.RenameDestTag2 != UInt(0) && ((io.RenameIssueValid2 === UInt(1)) || (io.RenameLoadStoreValid2 === UInt(1)))) {
      val res = index + UInt(2)
      //printf("\nThe value of 3rd index is %d\n", res)
      valid(res) := UInt(0)
      value(res) := UInt(0)
      tag(res) := io.RenameDestTag2
      store_select(res) := io.RenameStoreSelect2
      
      /*when(res < UInt(4) && (io.RenameStoreSelect0 === Bool(true)) && index === UInt(0)) {
          io.ROBStoreSelect0 := Bool(true)
      }
      when(res < UInt(4) && (io.RenameStoreSelect1 === Bool(true)) && index === UInt(1)) {
          io.ROBStoreSelect1 := Bool(true)
      }
      when(res < UInt(4) && (io.RenameStoreSelect2 === Bool(true)) && index === UInt(2)) {
          io.ROBStoreSelect2 := Bool(true)
      }
      when(res < UInt(4) && (io.RenameStoreSelect3 === Bool(true)) && index === UInt(3)) {
          io.ROBStoreSelect3 := Bool(true)
      }*/
      
      rob_tag(res) := io.RenameROBtag2
    }
    .elsewhen(io.RenameDestTag0 != UInt(0) || io.RenameDestTag1 != UInt(0) && io.RenameDestTag2 != UInt(0) && ((io.RenameIssueValid2 === UInt(1)) || (io.RenameLoadStoreValid2 === UInt(1)))) {
      val res = index + UInt(1)
      //printf("\nThe value of 3rd index is %d\n", res)
      valid(res) := UInt(0)
      value(res) := UInt(0)
      tag(res) := io.RenameDestTag2
      store_select(res) := io.RenameStoreSelect2
      
      /*when(res < UInt(4) && (io.RenameStoreSelect0 === Bool(true)) && index === UInt(0)) {
          io.ROBStoreSelect0 := Bool(true)
      }
      when(res < UInt(4) && (io.RenameStoreSelect1 === Bool(true)) && index === UInt(1)) {
          io.ROBStoreSelect1 := Bool(true)
      }
      when(res < UInt(4) && (io.RenameStoreSelect2 === Bool(true)) && index === UInt(2)) {
          io.ROBStoreSelect2 := Bool(true)
      }
      when(res < UInt(4) && (io.RenameStoreSelect3 === Bool(true)) && index === UInt(3)) {
          io.ROBStoreSelect3 := Bool(true)
      }*/

      //store_address(res) := io.LoadStoreDestAddress2
      rob_tag(res) := io.RenameROBtag2
    }
    .elsewhen(io.RenameDestTag0 === UInt(0) && io.RenameDestTag1 === UInt(0) && io.RenameDestTag2 != UInt(0) && ((io.RenameIssueValid2 === UInt(1)) || (io.RenameLoadStoreValid2 === UInt(1)))) {
      //printf("\nThe value of 3rd index is %d\n", index)
      valid(index) := UInt(0)
      value(index) := UInt(0)
      tag(index) := io.RenameDestTag2
      store_select(index) := io.RenameStoreSelect2
     
      /*when(index < UInt(4) && (io.RenameStoreSelect0 === Bool(true)) && index === UInt(0)) {
          io.ROBStoreSelect0 := Bool(true)
      }
      when(index < UInt(4) && (io.RenameStoreSelect1 === Bool(true)) && index === UInt(1)) {
          io.ROBStoreSelect1 := Bool(true)
      }
      when(index < UInt(4) && (io.RenameStoreSelect2 === Bool(true)) && index === UInt(2)) {
          io.ROBStoreSelect2 := Bool(true)
      }
      when(index < UInt(4) && (io.RenameStoreSelect3 === Bool(true)) && index === UInt(3)) {
          io.ROBStoreSelect3 := Bool(true)
      }*/

      //store_address(index) := io.LoadStoreDestAddress2
      rob_tag(index) := io.RenameROBtag2
    }

    //For 4th instruction
    when(io.RenameDestTag0 != UInt(0) && io.RenameDestTag1 != UInt(0) && io.RenameDestTag2 != UInt(0) && io.RenameDestTag3 != UInt(0) && ((io.RenameIssueValid3 === UInt(1)) || (io.RenameLoadStoreValid3 === UInt(1)))) {
      val res = index + UInt(3)
      //printf("\nThe value of 4th index is %d\n", res)
      valid(res) := UInt(0)
      value(res) := UInt(0)
      tag(res) := io.RenameDestTag3
      store_select(res) := io.RenameStoreSelect3
      
      /*when(res < UInt(4) && (io.RenameStoreSelect0 === Bool(true)) && index === UInt(0)) {
          io.ROBStoreSelect0 := Bool(true)
      }
      when(res < UInt(4) && (io.RenameStoreSelect1 === Bool(true)) && index === UInt(1)) {
          io.ROBStoreSelect1 := Bool(true)
      }
      when(res < UInt(4) && (io.RenameStoreSelect2 === Bool(true)) && index === UInt(2)) {
          io.ROBStoreSelect2 := Bool(true)
      }
      when(res < UInt(4) && (io.RenameStoreSelect3 === Bool(true)) && index === UInt(3)) {
          io.ROBStoreSelect3 := UInt(1)
      }*/
      
      rob_tag(res) := io.RenameROBtag3
    }
    .elsewhen((io.RenameDestTag0 != UInt(0) && io.RenameDestTag1 != UInt(0)) || (io.RenameDestTag1 != UInt(0) && io.RenameDestTag2 != UInt(0))
                                                                             || (io.RenameDestTag0 != UInt(0) && io.RenameDestTag2 != UInt(0)) && io.RenameDestTag3 != UInt(0) 
                                                                             && ((io.RenameIssueValid3 === UInt(1)) || (io.RenameLoadStoreValid3 === UInt(1)))) {
      val res = index + UInt(2)
      //printf("\nThe value of 4th index is %d\n", res)
      valid(res) := UInt(0)
      value(res) := UInt(0)
      tag(res) := io.RenameDestTag3
      store_select(res) := io.RenameStoreSelect3
      //Apply for loop here checking all the 4 values in ROB with the incoming tags
      //for(i <- 0 until 4) {
      /*when(res < UInt(4) && (io.RenameStoreSelect0 === Bool(true)) && index === UInt(0)) {
          io.ROBStoreSelect0 := Bool(true)
      }
      when(res < UInt(4) && (io.RenameStoreSelect1 === Bool(true)) && index === UInt(1)) {
          io.ROBStoreSelect1 := Bool(true)
      }
      when(res < UInt(4) && (io.RenameStoreSelect2 === Bool(true)) && index === UInt(2)) {
          io.ROBStoreSelect2 := Bool(true)
      }
      when(res < UInt(4) && (io.RenameStoreSelect3 === Bool(true)) && index === UInt(3)) {
          io.ROBStoreSelect3 := Bool(true)
      }*/
      //}
      //store_address(res) := io.LoadStoreDestAddress3
      rob_tag(res) := io.RenameROBtag3
    }
    .elsewhen(io.RenameDestTag0 != UInt(0) || io.RenameDestTag1 != UInt(0) || io.RenameDestTag2 != UInt(0) && io.RenameDestTag3 != UInt(0) && ((io.RenameIssueValid3 === UInt(1)) || (io.RenameLoadStoreValid3 === UInt(1)))) {
      val res = index + UInt(1)
      //printf("\nThe value of 4th index is %d\n", res)
      valid(res) := UInt(0)
      value(res) := UInt(0)
      tag(res) := io.RenameDestTag3
      store_select(res) := io.RenameStoreSelect3

      /*when(res < UInt(4) && (io.RenameStoreSelect0 === Bool(true)) && index === UInt(0)) {
          io.ROBStoreSelect0 := Bool(true)
      }
      when(res < UInt(4) && (io.RenameStoreSelect1 === Bool(true)) && index === UInt(1)) {
          io.ROBStoreSelect1 := Bool(true)
      }
      when(res < UInt(4) && (io.RenameStoreSelect2 === Bool(true)) && index === UInt(2)) {
          io.ROBStoreSelect2 := Bool(true)
      }
      when(res < UInt(4) && (io.RenameStoreSelect3 === Bool(true)) && index === UInt(3)) {
          io.ROBStoreSelect3 := Bool(true)
      }*/

      //store_address(res) := io.LoadStoreDestAddress3
      rob_tag(res) := io.RenameROBtag3
    }
    .elsewhen(io.RenameDestTag0 != UInt(0) && io.RenameDestTag1 != UInt(0) && io.RenameDestTag2 != UInt(0) && io.RenameDestTag0 != UInt(0) && ((io.RenameIssueValid3 === UInt(1)) || (io.RenameLoadStoreValid3 === UInt(1)))) {
      //printf("\nThe value of 4th index is %d\n", index)
      valid(index) := UInt(0)
      value(index) := UInt(0)
      tag(index) := io.RenameDestTag3
      store_select(index) := io.RenameStoreSelect3
      rob_tag(index) := io.RenameROBtag3
    }
    

  /*when((valid(0) === UInt(1)) && (valid(1) === UInt(1))
        && (valid(2) === UInt(1)) && (valid(3) === UInt(1))) {
    printf("\nComing in the all valid check\n")
    io.ROBValueValid0 := UInt(1)
    io.ROBValueValid1 := UInt(1)
    io.ROBValueValid2 := UInt(1)
    io.ROBValueValid3 := UInt(1)

    io.ROBValue0 := valid(0)
    io.ROBValue1 := valid(1)
    io.ROBValue2 := valid(2)
    io.ROBValue3 := valid(3)
    
    when (store_select(0) === Bool(true)) {
      io.ROBMemAddress0 := store_address(0)
      io.ROBStoreSelect0 := Bool(true)
    }

    when (store_select(1) === Bool(true)) {
      io.ROBMemAddress1 := store_address(1)
      io.ROBStoreSelect1 := Bool(true)
    }

    when (store_select(2) === Bool(true)) {
      io.ROBMemAddress2 := store_address(2)
      io.ROBStoreSelect2 := Bool(true)
    }

    when (store_select(3) === Bool(true)) {
      io.ROBMemAddress3 := store_address(3)
      io.ROBStoreSelect3 := Bool(true)
    }

    for (i <- 95 to 4 by -1) {
      valid(i-4) := valid(i)
      tag(i-4) := tag(i)
      value(i-4) := value(i)
      rob_tag(i-4) := rob_tag(i)
      store_select(i-4) := store_select(i)
      store_address(i-4) := store_address(i)
      //printf("\nThe values of valid(i), tag(i), value(i), rob_tag(i) store_select(i) and store_address(i) are %d,%d,%d,%d, %d and %d\n",valid(i), tag(i), value(i), rob_tag(i), store_select(i),store_address(i))
    }
    for (i <- 92 until 96) {
      valid(i) := UInt(0)
      tag(i) := UInt(0)
      value(i) := UInt(0)
      rob_tag(i) := UInt(0)
      store_select(i) := UInt(0)
      store_address(i) := UInt(0)
    }
  }

  when((valid(0) === UInt(1)) && (valid(1) === UInt(1))
        && (valid(2) === UInt(1)) && (valid(3) != UInt(1))) {
    printf("\nComing in the one less valid check\n")
    io.ROBValueValid0 := UInt(1)
    io.ROBValueValid1 := UInt(1)
    io.ROBValueValid2 := UInt(1)
    //io.ROBValueValid3 := UInt(1)

    io.ROBValue0 := valid(0)
    io.ROBValue1 := valid(1)
    io.ROBValue2 := valid(2)
    //io.ROBValue3 := valid(3)
    
    when (store_select(0) === Bool(true)) {
      io.ROBMemAddress0 := store_address(0)
      io.ROBStoreSelect0 := Bool(true)
    }

    when (store_select(1) === Bool(true)) {
      io.ROBMemAddress1 := store_address(1)
      io.ROBStoreSelect1 := Bool(true)
    }

    when (store_select(2) === Bool(true)) {
      io.ROBMemAddress2 := store_address(2)
      io.ROBStoreSelect2 := Bool(true)
    }

    when (store_select(3) === Bool(true)) {
      io.ROBMemAddress3 := store_address(3)
      io.ROBStoreSelect3 := Bool(true)
    }*/

   /* when(tag(0) === io.RenameDestTag0) {
      io.ROBStoreSelect0 := Bool(true)     
    }
    when(tag(1) === io.RenameDestTag1) {
      io.ROBStoreSelect1 := Bool(true)     
    }
    when(tag(2) === io.RenameDestTag2) {
      io.ROBStoreSelect2 := Bool(true)     
    }
    when(tag(3) === io.RenameDestTag3) {
      io.ROBStoreSelect3 := Bool(true)     
    }*/

    /*for (i <- 95 to 3 by -1) {
      valid(i-3) := valid(i)
      tag(i-3) := tag(i)
      value(i-3) := value(i)
      rob_tag(i-3) := rob_tag(i)
      store_select(i-3) := store_select(i)
      store_address(i-3) := store_address(i)
      //printf("\nThe values of valid(i), tag(i), value(i), rob_tag(i) store_select(i) and store_address(i) are %d,%d,%d,%d, %d and %d\n",valid(i), tag(i), value(i), rob_tag(i), store_select(i),store_address(i))
    }
    for (i <- 93 until 96) {
      valid(i) := UInt(0)
      tag(i) := UInt(0)
      value(i) := UInt(0)
      rob_tag(i) := UInt(0)
      store_select(i) := UInt(0)
      store_address(i) := UInt(0)
    }
  }

  when((valid(0) === UInt(1)) && (valid(1) === UInt(1))
        && (valid(2) != UInt(1)) && (valid(3) != UInt(1))) {
    printf("\nComing in the 2 less valid check\n")
    io.ROBValueValid0 := UInt(1)
    io.ROBValueValid1 := UInt(1)
    //io.ROBValueValid2 := UInt(1)
    //io.ROBValueValid3 := UInt(1)

    io.ROBValue0 := valid(0)
    io.ROBValue1 := valid(1)
    //io.ROBValue2 := valid(2)
    //io.ROBValue3 := valid(3)
    
    when (store_select(0) === Bool(true)) {
      io.ROBMemAddress0 := store_address(0)
      io.ROBStoreSelect0 := Bool(true)
    }

    when (store_select(1) === Bool(true)) {
      io.ROBMemAddress1 := store_address(1)
      io.ROBStoreSelect1 := Bool(true)
    }

    when (store_select(2) === Bool(true)) {
      io.ROBMemAddress2 := store_address(2)
      io.ROBStoreSelect2 := Bool(true)
    }

    when (store_select(3) === Bool(true)) {
      io.ROBMemAddress3 := store_address(3)
      io.ROBStoreSelect3 := Bool(true)
    }


    for (i <- 95 to 2 by -1) {
      valid(i-2) := valid(i)
      tag(i-2) := tag(i)
      value(i-2) := value(i)
      rob_tag(i-2) := rob_tag(i)
      store_select(i-2) := store_select(i)
      store_address(i-2) := store_address(i)
      //printf("\nThe values of valid(i), tag(i), value(i), rob_tag(i) store_select(i) and store_address(i) are %d,%d,%d,%d, %d and %d\n",valid(i), tag(i), value(i), rob_tag(i), store_select(i),store_address(i))
    }

    for (i <- 94 until 96) {
      valid(i) := UInt(0)
      tag(i) := UInt(0)
      value(i) := UInt(0)
      rob_tag(i) := UInt(0)
      store_select(i) := UInt(0)
      store_address(i) := UInt(0)
    }
  }*/

  when((valid(0) === UInt(1)) && io.dmembusy===UInt(0) /*&& (valid(1) != UInt(1))
        && (valid(2) != UInt(1)) && (valid(3) != UInt(1))*/) {
    //printf("\nComing in the 3 less valid check\n")
    io.ROBValueValid0 := UInt(1)
    //io.ROBValueValid1 := UInt(1)
    //io.ROBValueValid2 := UInt(1)
    //io.ROBValueValid3 := UInt(1)

    io.ROBValue0 := value(0)
    //io.ROBValue1 := valid(1)
    //io.ROBValue2 := valid(2)
    //io.ROBValue3 := valid(3)
    
    when (store_select(0) === Bool(true)) {
      io.ROBMemAddress0 := store_address(0)
      io.ROBStoreSelect0 := Bool(true)
    }

    /*when (store_select(1) === Bool(true)) {
      io.ROBMemAddress1 := store_address(1)
      io.ROBStoreSelect1 := Bool(true)
    }

    when (store_select(2) === Bool(true)) {
      io.ROBMemAddress2 := store_address(2)
      io.ROBStoreSelect2 := Bool(true)
    }

    when (store_select(3) === Bool(true)) {
      io.ROBMemAddress3 := store_address(3)
      io.ROBStoreSelect3 := Bool(true)
    }*/

    /*when(tag(0) === io.RenameDestTag0) {
      io.ROBStoreSelect0 := Bool(true)     
    }
    when(tag(1) === io.RenameDestTag1) {
      io.ROBStoreSelect1 := Bool(true)     
    }
    when(tag(2) === io.RenameDestTag2) {
      io.ROBStoreSelect2 := Bool(true)     
    }
    when(tag(3) === io.RenameDestTag3) {
      io.ROBStoreSelect3 := Bool(true)     
    }*/


    for (i <- 0 until 95) {
      //printf("\nIn the valid by -1\n")
      valid(i) := valid(i+1)
      tag(i) := tag(i+1)
      value(i) := value(i+1)
      rob_tag(i) := rob_tag(i+1)
      store_select(i) := store_select(i+1)
      store_address(i) := store_address(i+1)
    }

    /*for (i <- 95 to 1 by -1) {
      //printf("\nIn the valid by -1\n")
      valid(i-1) := valid(i)
      tag(i-1) := tag(i)
      value(i-1) := value(i)
      rob_tag(i-1) := rob_tag(i)
      store_select(i-1) := store_select(i)
      store_address(i-1) := store_address(i)
      //printf("\nThe values of valid(i), tag(i), value(i), rob_tag(i) store_select(i) and store_address(i)  and i are %d,%d,%d,%d, %d, %d and %d\n",valid(i), tag(i), value(i), rob_tag(i), store_select(i),store_address(i), UInt(i))
      //printf("\n2nd The values of valid(i), tag(i), value(i), rob_tag(i) and store_select(i) are %d,%d,%d,%d and %d\n",valid(i), tag(i), value(i), rob_tag(i), store_select(i),store_address(i))
    }*/
    //printf("\nThe values of last valid(0), tag(0), value(0), rob_tag(0) store_select(0) and store_address(0) are %d,%d,%d,%d, %d and %d\n",valid(0), tag(0), value(0), rob_tag(0), store_select(0),store_address(0))
    //for (i <- 95 until 96) {
      valid(95) := UInt(0)
      tag(95) := UInt(0)
      value(95) := UInt(0)
      rob_tag(95) := UInt(0)
      store_select(95) := UInt(0)
      store_address(95) := UInt(0)
    //}
  }
}

class ROBTests(c: ROB) extends Tester(c) { 
  poke(c.io.RenameValid, 1)
  poke(c.io.RenameDestTag0, 1)
  poke(c.io.RenameDestTag1, 4)
  poke(c.io.RenameDestTag2, 5)
  poke(c.io.RenameDestTag3, 7)
  poke(c.io.RenameROBtag0, 5)
  poke(c.io.RenameROBtag1, 6)
  poke(c.io.RenameROBtag2, 7)
  poke(c.io.RenameROBtag3, 9)
  poke(c.io.RenameQueueSelect0, false)
  poke(c.io.RenameQueueSelect1, false)
  poke(c.io.RenameQueueSelect2, false)
  poke(c.io.RenameQueueSelect3, true)
  poke(c.io.RenameStoreSelect0, false)
  poke(c.io.RenameStoreSelect1, false)
  poke(c.io.RenameStoreSelect2, false)
  poke(c.io.RenameStoreSelect3, true)
  poke(c.io.FUBroadcastValue0, 2)
  poke(c.io.FUBroadcastValue1, 4)
  poke(c.io.FUBroadcastValue2, 6)
  poke(c.io.FUBroadcastValue3, 7)
  poke(c.io.FUBroadcastTag0, 1)
  poke(c.io.FUBroadcastTag1, 4)
  poke(c.io.FUBroadcastTag2, 5)
  poke(c.io.FUBroadcastTag3, 0)
  poke(c.io.LoadStoreDestTag0, 7)
  poke(c.io.LoadStoreDestVal0, 5)
  poke(c.io.LoadStoreDestAddress0, 0x50)
  step(2)
  expect(c.io.ROBStoreSelect0, false)
  //expect(c.io.ROBStoreSelect1, false)
  //expect(c.io.ROBStoreSelect2, false)
  //expect(c.io.ROBStoreSelect3, true)
  expect(c.io.ROBValueValid0, 1)
  //expect(c.io.ROBValueValid1, 1)
  //expect(c.io.ROBValueValid2, 1)
  //expect(c.io.ROBValueValid3, 1)
  expect(c.io.ROBMemAddress0, 0)
  //expect(c.io.ROBMemAddress1, 0)
  //expect(c.io.ROBMemAddress2, 0x0)
  //expect(c.io.ROBMemAddress3, 0x50)
} 

class ROBGenerator extends TestGenerator {
  def genMod(): Module = Module(new ROB())
  def genTest[T <: Module](c: T): Tester[T] = 
    (new ROBTests(c.asInstanceOf[ROB])).asInstanceOf[Tester[T]]
}

