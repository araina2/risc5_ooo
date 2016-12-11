package risc5

import Chisel._


class LoadStoreQueueModule extends Module {

  val io = new Bundle {
        // All INPUT ports ( all the ports in the doc for the issue queue needs to be updated to 64 bits , which is not the case in the doc )
        //

        val RenameLoadStoreRowSelect0 = UInt(INPUT,5)
        val RenameLoadStoreRowSelect1 = UInt(INPUT,5)
        val RenameLoadStoreRowSelect2 = UInt(INPUT,5)
        val RenameLoadStoreRowSelect3 = UInt(INPUT,5)
        val RenameLoadStoreValid0 = UInt(INPUT,1)
        val RenameLoadStoreValid1 = UInt(INPUT,1)
        val RenameLoadStoreValid2 = UInt(INPUT,1)
        val RenameLoadStoreValid3 = UInt(INPUT,1)
        val LoadStoreFreeRow = UInt(OUTPUT,5)
   
        // Source "A" parameters in the loadstore queue
        val RenameSourceAValue0 = UInt(INPUT,64)
        val RenameSourceAValue1 = UInt(INPUT,64)
        val RenameSourceAValue2 = UInt(INPUT,64)
        val RenameSourceAValue3 = UInt(INPUT,64)
        
        val RenameSourceAValueValid0 = UInt(INPUT,1)
        val RenameSourceAValueValid1 = UInt(INPUT,1)
        val RenameSourceAValueValid2 = UInt(INPUT,1)
        val RenameSourceAValueValid3 = UInt(INPUT,1)

        val RenameSourceATag0 = UInt(INPUT,10)
        val RenameSourceATag1 = UInt(INPUT,10)
        val RenameSourceATag2 = UInt(INPUT,10)
        val RenameSourceATag3 = UInt(INPUT,10)

        // Source "B" parameters in the loadstore queue
        val RenameSourceBValue0 = UInt(INPUT,64)
        val RenameSourceBValue1 = UInt(INPUT,64)
        val RenameSourceBValue2 = UInt(INPUT,64)
        val RenameSourceBValue3 = UInt(INPUT,64)
        
        val RenameSourceBValueValid0 = UInt(INPUT,1)
        val RenameSourceBValueValid1 = UInt(INPUT,1)
        val RenameSourceBValueValid2 = UInt(INPUT,1)
        val RenameSourceBValueValid3 = UInt(INPUT,1)

        val RenameSourceBTag0 = UInt(INPUT,10)
        val RenameSourceBTag1 = UInt(INPUT,10)
        val RenameSourceBTag2 = UInt(INPUT,10)
        val RenameSourceBTag3 = UInt(INPUT,10)

        //Decode parameters

        val Rename_Opcode_0 = UInt(INPUT,7)
        val Rename_Opcode_1 = UInt(INPUT,7)
        val Rename_Opcode_2 = UInt(INPUT,7)
        val Rename_Opcode_3 = UInt(INPUT,7)

        
        val Rename_destTag_0 = UInt(INPUT,10)
        val Rename_destTag_1 = UInt(INPUT,10)
        val Rename_destTag_2 = UInt(INPUT,10)
        val Rename_destTag_3 = UInt(INPUT,10)

        val Rename_func3_0 = UInt(INPUT,3)
        val Rename_func3_1 = UInt(INPUT,3)
        val Rename_func3_2 = UInt(INPUT,3)
        val Rename_func3_3 = UInt(INPUT,3)
        
        val Rename_func7_0 = UInt(INPUT,7)
        val Rename_func7_1 = UInt(INPUT,7)
        val Rename_func7_2 = UInt(INPUT,7)
        val Rename_func7_3 = UInt(INPUT,7)
        
        val Rename_Imm_0 = UInt(INPUT,12)
        val Rename_Imm_1 = UInt(INPUT,12)
        val Rename_Imm_2 = UInt(INPUT,12)
        val Rename_Imm_3 = UInt(INPUT,12)
        
        val RenameQueueSelect0 = Bool(INPUT)// use this in the top module to select L/S queue or IssueQueue
        val RenameQueueSelect1 = Bool(INPUT)// use this in the top module to select L/S queue or IssueQueue
        val RenameQueueSelect2 = Bool(INPUT)// use this in the top module to select L/S queue or IssueQueue
        val RenameQueueSelect3 = Bool(INPUT)// use this in the top module to select L/S queue or IssueQueue
        
        val RenameStoreSelect0 = Bool(INPUT)
        val RenameStoreSelect1 = Bool(INPUT)
        val RenameStoreSelect2 = Bool(INPUT)
        val RenameStoreSelect3 = Bool(INPUT)
        
        val RenameROBtag0 = UInt(INPUT,7)
        val RenameROBtag1 = UInt(INPUT,7)
        val RenameROBtag2 = UInt(INPUT,7)
        val RenameROBtag3 = UInt(INPUT,7)

        val RenameType_0 = UInt(INPUT,3)// check with Anshu if this is needed
        val RenameType_1 = UInt(INPUT,3)// check with Anshu if this is needed
        val RenameType_2 = UInt(INPUT,3)// check with Anshu if this is needed
        val RenameType_3 = UInt(INPUT,3)// check with Anshu if this is needed

        //(destination register names) , dest Tag takes care of this, it is not required
        //val Rename_dest_0 = UInt(INPUT,5)
        //val Rename_dest_1 = UInt(INPUT,5)
        //val Rename_dest_2 = UInt(INPUT,5)
        //val Rename_dest_3 = UInt(INPUT,5)

        //D-cache parameters
       // val Dcache_Val0 = UInt(INPUT,64)
       // val Dcache_Val1 = UInt(INPUT,64)
       // val Dcache_Val2 = UInt(INPUT,64)
       // val Dcache_Val3 = UInt(INPUT,64)

       // val Dcache_Valid0 = UInt(INPUT,1)// one valid bit must be there for decache
       // val Dcache_Valid1 = UInt(INPUT,1)// one valid bit must be there for decache
       // val Dcache_Valid2 = UInt(INPUT,1)// one valid bit must be there for decache
       // val Dcache_Valid3 = UInt(INPUT,1)// one valid bit must be there for decache

       // val Dcache_Tag0 = UInt(INPUT,64)
       // val Dcache_Tag1 = UInt(INPUT,64)
       // val Dcache_Tag2 = UInt(INPUT,64)
       // val Dcache_Tag3 = UInt(INPUT,64)

        val Dcache_busy = UInt(INPUT,1) 
        val Dcache_tag_out = UInt(INPUT,7)
        val Dcache_data_out = UInt(INPUT,64)
        val Dcache_Valid = UInt(INPUT,1)

	val FUBroadcastValue0 = UInt(INPUT,64)
	val FUBroadcastTag0 = UInt(INPUT,10)
	val FUBroadcastValid0 = UInt(INPUT,1) //need to be added in the document

	val FUBroadcastValue1 = UInt(INPUT,64)
	val FUBroadcastTag1 = UInt(INPUT,10)
	val FUBroadcastValid1 = UInt(INPUT,1) //need to be added in the document

	val FUBroadcastValue2 = UInt(INPUT,64)
	val FUBroadcastTag2 = UInt(INPUT,10)
	val FUBroadcastValid2 = UInt(INPUT,1) //need to be added in the document

	val FUBroadcastValue3 = UInt(INPUT,64)
	val FUBroadcastTag3 = UInt(INPUT,10)
	val FUBroadcastValid3 = UInt(INPUT,1) //need to be added in the document
	
        val LoadStoreDestVal = UInt(INPUT,64)
	val LoadStoreDestTag = UInt(INPUT,10)
	val LoadStoreDestValid = UInt(INPUT,1) // need to be added in the document

        // All OUPUT ports

        val LoadStoreDestTag_out = UInt(OUTPUT,10)
        val LoadStoreDestAddress = UInt(OUTPUT,64)
        val LoadStoreDestValue = UInt(OUTPUT,64)
        val LoadStoreDestValid_out = UInt(OUTPUT,1)

        val LoadStoreSelect = Bool(OUTPUT)
        val LoadStoreROBTag = UInt(OUTPUT,7)

        val LoadStoreFull = UInt(OUTPUT,1)
        
        val Cache_enable = UInt(OUTPUT,1)
        val Cache_Address = UInt(OUTPUT,64)
        val Cache_Compare = UInt(OUTPUT,1)
        val Cache_tag_in = UInt(OUTPUT,7)
        val Cache_valid_in = UInt(OUTPUT,1)
        
        

    
  }

  // Registers

  val rs1 = Vec.fill(32){Reg(init = UInt(0,width=64))}
  val rs1_valid = Vec.fill(32){Reg(init = UInt(0,width=1))}
  val rs1_tag = Vec.fill(32){Reg(init = UInt(0,width=10))}
  val rs2 = Vec.fill(32){Reg(init = UInt(0,width=64))}
  val rs2_valid = Vec.fill(32){Reg(init = UInt(0,width=1))}
  val rs2_tag = Vec.fill(32){Reg(init = UInt(0,width=10))}

  val Opcode = Vec.fill(32){Reg(init = UInt(0,width=7))}
  val DestTag = Vec.fill(32){Reg(init = UInt(0,width=10))}
  val func3 = Vec.fill(32){Reg(init = UInt(0,width=3))}
  val func7 = Vec.fill(32){Reg(init = UInt(0,width=7))}
  val Imm = Vec.fill(32){Reg(init = UInt(0,width=12))}
  val DestLoadstoreSelect = Vec.fill(32){Reg(init =Bool(false))}
  val DestROB = Vec.fill(32){Reg(init = UInt(0,width=7))}
  //val DestRegister = Vec.fill(32){Reg(init = UInt(0,width=5))}
  val DestAddress = Vec.fill(32){Reg(init = UInt(0,width=64))}
  val DestValue = Vec.fill(32){Reg(init = UInt(0,width=64))}


  val ValidEntry = Vec.fill(32){Reg(init=UInt(0,width=1))}
  val DestAddressValid = Vec.fill(32){Reg(init = UInt(0,width=1))}
  val DestValueValid = Vec.fill(32){Reg(init = UInt(0,width=1))}

  val OrderingBit = Vec.fill(32){Reg(init = UInt(0,width=1))}

  //val rowPlace = Reg(init = SInt(-1,width=1))
  val ROB_list = Vec.fill(128){Reg(init = UInt(0,width=1))}
// wire 
  val lowestROB_Load = UInt(width=7)



  val onlyStoreInstructions = Bool()

  
  val sign_extended_value0 = UInt(width=56)
  val sign_extended_value1 = UInt(width=48)
  val sign_extended_value2 = UInt(width=32)
 
  // setting the default values for output ports    
        io.LoadStoreDestTag_out := UInt(0)
        io.LoadStoreDestAddress := UInt(0)
        io.LoadStoreDestValue := UInt(0)
        io.LoadStoreDestValid_out := UInt(0)

        io.LoadStoreSelect := Bool(false)
        io.LoadStoreROBTag := UInt(0)
        io.LoadStoreFull := UInt(0)
        io.Cache_enable := UInt(0)
        io.Cache_Address := UInt(0)
        io.Cache_Compare := UInt(0)
        io.Cache_tag_in := UInt(0)
        io.Cache_valid_in := UInt(0)
        lowestROB_Load := UInt(127)
// default values for wires
        onlyStoreInstructions := Bool(false)
        sign_extended_value0:= UInt(0)
        sign_extended_value1:= UInt(0)
        sign_extended_value2:= UInt(0)
        
        io.LoadStoreFreeRow := UInt(0)
 
  // reset all the register values
  when(reset) {
    for(i<-0 until 32) {

      
      rs1(i) := UInt(0)
      rs1_valid(i) := UInt(0)
      rs1_tag(i) := UInt(0)
      rs2(i) := UInt(0)
      rs2_valid(i) := UInt(0)
      rs2_tag(i) := UInt(0)

      Opcode(i) := UInt(0)
      DestTag(i) := UInt(0)
      func3(i) := UInt(0)
      func7(i) := UInt(0)
      Imm(i) := UInt(0)
      DestLoadstoreSelect(i) := Bool(false)
      DestROB(i) := UInt(0)
      //DestRegister(i) := UInt(0)
      DestAddress(i) := UInt(0)
      DestValue(i) := UInt(0)

      ValidEntry(i) := UInt(0)
      DestAddressValid(i) := UInt(0)
      DestValueValid(i) := UInt(0)

      OrderingBit(i) := UInt(0)

      ROB_list(i) := UInt(0)

    }
      //rowPlace := SInt(-1)

  } 
  // Update the entry in the load-store-queue based on FU broadcast for FU0
  when(io.FUBroadcastValid0===UInt(1)){
    
    for(j<-0 until 32){
      when((io.FUBroadcastTag0===rs1_tag(j))&&(rs1_valid(j)===UInt(0))&&(ValidEntry(j)===UInt(1))){
      
        rs1(j) := io.FUBroadcastValue0
        rs1_valid(j) := UInt(1)
        printf("\n updated with FU0 value for rs1")

      }
    
      when((io.FUBroadcastTag0 === rs2_tag(j))&&(rs2_valid(j)===UInt(0))&&(ValidEntry(j)===UInt(1))){
      
        rs2(j) := io.FUBroadcastValue0
        rs2_valid(j) := UInt(1)
        printf("\n updated with FU0 value for rs2")

      }
    
    }
  }// when(io.FUBroadcastValid0===1)
  
  // Update the entry in the load-store-queue based on FU broadcast for FU1
  when(io.FUBroadcastValid1===UInt(1)){
    
    for(j<-0 until 32){
      when((io.FUBroadcastTag1 === rs1_tag(j))&&(rs1_valid(j)===UInt(0))&&(ValidEntry(j)===UInt(1))){
      
        rs1(j) := io.FUBroadcastValue1
        rs1_valid(j) := UInt(1)
        printf("\n updated with FU1 value for rs1")

      }
    
      when((io.FUBroadcastTag1 === rs2_tag(j))&&(rs2_valid(j)===UInt(0))&&(ValidEntry(j)===UInt(1))){
      
        rs2(j) := io.FUBroadcastValue1
        rs2_valid(j) := UInt(1)
        printf("\n updated with FU1 value for rs2")

      }
    
    }
  }// when(io.FUBroadcastValid1===1)

  // Update the entry in the load-store-queue based on FU broadcast for FU2
  when(io.FUBroadcastValid2===UInt(1)){
    
    for(j<-0 until 32){
      when((io.FUBroadcastTag2 === rs1_tag(j))&&(rs1_valid(j)===UInt(0))&&(ValidEntry(j)===UInt(1))){
      
        rs1(j) := io.FUBroadcastValue2
        rs1_valid(j) := UInt(1)
        //printf("\n updated with FU2 value for rs1")

      }
    
      when((io.FUBroadcastTag2 === rs2_tag(j))&&(rs2_valid(j)===UInt(0))&&(ValidEntry(j)===UInt(1))){
      
        rs2(j) := io.FUBroadcastValue2
        rs2_valid(j) := UInt(1)
        //printf("\n updated with FU2 value for rs2")

      }
    
    }
  }// when(io.FUBroadcastValid2===1)

  // Update the entry in the load-store-queue based on FU broadcast for FU3
  when(io.FUBroadcastValid3===UInt(1)){
    
    for(j<-0 until 32){
      when((io.FUBroadcastTag3 === rs1_tag(j))&&(rs1_valid(j)===UInt(0))&&(ValidEntry(j)===UInt(1))){
      
        rs1(j) := io.FUBroadcastValue3
        rs1_valid(j) := UInt(1)
        //printf("\n updated with FU3 value for rs1")

      }
    
      when((io.FUBroadcastTag3 === rs2_tag(j))&&(rs2_valid(j)===UInt(0))&&(ValidEntry(j)===UInt(1))){
      
        rs2(j) := io.FUBroadcastValue3
        rs2_valid(j) := UInt(1)
        //printf("\n updated with FU3 value for rs2")

      }
    
    }
  }// when(io.FUBroadcastValid3===1)
// use the dcache output the update the values in the load instructions in the l/s queue TODO(INCOMPLETE)
  when(io.Dcache_Valid===UInt(1)&& io.Dcache_busy!=UInt(1)){
    
    for(j<-0 until 32){
      when((io.Dcache_tag_out === DestROB(j)) && (DestAddressValid(j) === UInt(1)) && (ValidEntry(j) === UInt(1)) && (DestLoadstoreSelect(j) === Bool(false)) && (DestValueValid(j) === UInt(0))){
      
        //printf("\n updated with Dcache value for destValue")
      // LB instruction take 8 bits from rs2 sign extend it to 64 bit
      when(func3(j)===UInt(0)){
        // Sign extention
        when(io.Dcache_data_out(7)===UInt(1)){
          sign_extended_value0:=Fill(56,UInt(1,width=1))
      
          DestValue(j):= Cat(sign_extended_value0,io.Dcache_data_out(7,0))
          DestValueValid(j):=UInt(1)
          io.LoadStoreDestTag_out := DestTag(j)
          io.LoadStoreDestValid_out := UInt(1)
          io.LoadStoreDestValue := Cat(sign_extended_value0,io.Dcache_data_out(7,0))
        }
        .elsewhen(io.Dcache_data_out(7)===UInt(0)){
          sign_extended_value0:=Fill(56,UInt(0,width=1))
      
          DestValue(j):= Cat(sign_extended_value0,io.Dcache_data_out(7,0))
          DestValueValid(j):=UInt(1)
          io.LoadStoreDestTag_out :=  DestTag(j)
          io.LoadStoreDestValid_out := UInt(1)
          io.LoadStoreDestValue := Cat(sign_extended_value0,io.Dcache_data_out(7,0))
        }
      
      }
      // LH instruction take 16 bits from rs2 sign extend it to 64 bit
      .elsewhen(func3(j)===UInt(1)){
        // Sign extention
        when(io.Dcache_data_out(15)===UInt(1)){
          sign_extended_value1:=Fill(48,UInt(1,width=1))
      
          DestValue(j):= Cat(sign_extended_value1,io.Dcache_data_out(15,0))
          DestValueValid(j):=UInt(1)
          io.LoadStoreDestTag_out := DestTag(j) 
          io.LoadStoreDestValid_out := UInt(1)
          io.LoadStoreDestValue := Cat(sign_extended_value1,io.Dcache_data_out(15,0))
        }
        .elsewhen(io.Dcache_data_out(15)===UInt(0)){
          sign_extended_value1:=Fill(48,UInt(0,width=1))
      
          DestValue(j):= Cat(sign_extended_value1,io.Dcache_data_out(15,0))
          DestValueValid(j):=UInt(1)
          io.LoadStoreDestTag_out := DestTag(j)
          io.LoadStoreDestValid_out := UInt(1)
          io.LoadStoreDestValue := Cat(sign_extended_value1,io.Dcache_data_out(15,0))
        }
      
      }
      // LW instruction take 32 bits from rs2 sign extend it to 64 bit
      .elsewhen(func3(j)===UInt(2)){
        // Sign extention
        when(io.Dcache_data_out(32)===UInt(1)){
          sign_extended_value2:=Fill(32,UInt(1,width=1))
      
          DestValue(j):= Cat(sign_extended_value2,io.Dcache_data_out(31,0))
          DestValueValid(j):=UInt(1)
          io.LoadStoreDestTag_out := DestTag(j)
          io.LoadStoreDestValid_out := UInt(1)
          io.LoadStoreDestValue := Cat(sign_extended_value2,io.Dcache_data_out(31,0))
        }
        .elsewhen(io.Dcache_data_out(32)===UInt(0)){
          sign_extended_value2:=Fill(32,UInt(0,width=1))
      
          DestValue(j):= Cat(sign_extended_value2,io.Dcache_data_out(31,0))
          DestValueValid(j):=UInt(1)
          io.LoadStoreDestTag_out := DestTag(j)
          io.LoadStoreDestValid_out := UInt(1)
          io.LoadStoreDestValue := Cat(sign_extended_value2,io.Dcache_data_out(31,0))
        }
      
      }
      // LD instruction take 64 bits from rs2 sign extend it to 64 bit
      .elsewhen(func3(j)===UInt(3)){

        DestValue(j):= io.Dcache_data_out
        DestValueValid(j):=UInt(1)
        io.LoadStoreDestTag_out := DestTag(j)
        io.LoadStoreDestValid_out := UInt(1)
        io.LoadStoreDestValue := io.Dcache_data_out
      }
      // LBU instruction take 8 bits from rs2 sign extend it to 64 bit
      .elsewhen(func3(j)===UInt(4)){
        // Un-Sign extention
          sign_extended_value0:=Fill(56,UInt(0,width=1))
      
          DestValue(j):= Cat(sign_extended_value0,io.Dcache_data_out(7,0))
          DestValueValid(j):=UInt(1)
          io.LoadStoreDestTag_out := DestTag(j)
          io.LoadStoreDestValid_out := UInt(1)
          io.LoadStoreDestValue := Cat(sign_extended_value0,io.Dcache_data_out(7,0))
      
      }
      // LHU instruction take 16 bits from rs2 sign extend it to 64 bit
      .elsewhen(func3(j)===UInt(5)){
        // Un-Sign extention
          sign_extended_value1:=Fill(48,UInt(0,width=1))
      
          DestValue(j):= Cat(sign_extended_value1,io.Dcache_data_out(15,0))
          DestValueValid(j):=UInt(1)
          io.LoadStoreDestTag_out := DestTag(j)
          io.LoadStoreDestValid_out := UInt(1)
          io.LoadStoreDestValue := Cat(sign_extended_value1,io.Dcache_data_out(15,0))
      
      }
      // LWU instruction take 32 bits from rs2 sign extend it to 64 bit
      .elsewhen(func3(j)===UInt(6)){
        // Un-Sign extention
          sign_extended_value2:=Fill(32,UInt(0,width=1))
      
          DestValue(j):= Cat(sign_extended_value2,io.Dcache_data_out(31,0))
          DestValueValid(j):=UInt(1)
          io.LoadStoreDestTag_out := DestTag(j)
          io.LoadStoreDestValid_out := UInt(1)
          io.LoadStoreDestValue := Cat(sign_extended_value2,io.Dcache_data_out(31,0))
    
      }


      }
    }
    
    
  }// when(io.Dcache_Valid===1)

  // Update the entry in the L/S queue based on L/S broadcast
  when(io.LoadStoreDestValid===UInt(1)){
    
    for(j<-0 until 32){
      when((io.LoadStoreDestTag === rs1_tag(j))&&(rs1_valid(j)===UInt(0))&&(ValidEntry(j)===UInt(1))){
      
        rs1(j) := io.LoadStoreDestVal
        rs1_valid(j) := UInt(1)
        //printf("\n updated with L/S value for rs1")

      }
    
      when((io.LoadStoreDestTag === rs2_tag(j))&&(rs2_valid(j)===UInt(0))&&(ValidEntry(j)===UInt(1))){
      
        rs2(j) := io.LoadStoreDestVal
        rs2_valid(j) := UInt(1)
        //printf("\n updated with L/S value for rs2")

      }
    
    }
  }// when(io.LoadStoreDestValid===1)

  //Check if the entry is full in the L/S queue
  io.LoadStoreFull := ValidEntry(0) & ValidEntry(1) & ValidEntry(2) & ValidEntry(3) & ValidEntry(4) & ValidEntry(5) & ValidEntry(6) & ValidEntry(7) & ValidEntry(8) & ValidEntry(9) & ValidEntry(10) & ValidEntry(11) & ValidEntry(12) & ValidEntry(13) & ValidEntry(14) & ValidEntry(15) & ValidEntry(16) & ValidEntry(16) & ValidEntry(17) & ValidEntry(18) & ValidEntry(19) & ValidEntry(20) & ValidEntry(21) & ValidEntry(22) & ValidEntry(23) & ValidEntry(24) & ValidEntry(25) & ValidEntry(26) & ValidEntry(27) & ValidEntry(28) & ValidEntry(29) & ValidEntry(30) & ValidEntry(31)

  for(b<-31 to 0 by -1){
      when((ValidEntry(b)===UInt(0) && (io.LoadStoreFull===UInt(0)))) {
          io.LoadStoreFreeRow := UInt(b) - UInt(1) 
      
      }
    }

      val rownumber0 = UInt(width=5)
      val rownumber1 = UInt(width=5)
      val rownumber2 = UInt(width=5)
      val rownumber3 = UInt(width=5)

      rownumber0 := UInt(0)
      rownumber1 := UInt(0)
      rownumber2 := UInt(0)
      rownumber3 := UInt(0)
  when((io.RenameLoadStoreValid0===UInt(1)) || (io.RenameLoadStoreValid1===UInt(1)) || (io.RenameLoadStoreValid2===UInt(1)) || (io.RenameLoadStoreValid3===UInt(1))) {

    when(io.LoadStoreFull===UInt(1)){

      //printf("\n This l/s queue is full\n")

    }
    .otherwise{
      rownumber0 := UInt(0)
      rownumber1 := UInt(0)
      rownumber2 := UInt(0)
      rownumber3 := UInt(0)
      when(io.RenameLoadStoreValid0===UInt(1)){
        rownumber0 := io.RenameLoadStoreRowSelect0
      }
      when(io.RenameLoadStoreValid1===UInt(1)){
        rownumber1 := io.RenameLoadStoreRowSelect1
      }
      when(io.RenameLoadStoreValid2===UInt(1)){
        rownumber2 := io.RenameLoadStoreRowSelect2
      }
      when(io.RenameLoadStoreValid3===UInt(1)){
        rownumber3 := io.RenameLoadStoreRowSelect3
      }

      // fix the valid entry
      when(io.RenameLoadStoreValid0===UInt(1)){
        ValidEntry(rownumber0) := UInt(1)
        DestAddress(rownumber0) := UInt(0)
        DestValue(rownumber0) := UInt(0)
        DestAddressValid(rownumber0) := UInt(0)
        DestValueValid(rownumber0) := UInt(0)
        OrderingBit(rownumber0) := UInt(0)
      }
      when(io.RenameLoadStoreValid1===UInt(1)){
        ValidEntry(rownumber1) := UInt(1)
        DestAddress(rownumber1) := UInt(0)
        DestValue(rownumber1) := UInt(0)
        DestAddressValid(rownumber1) := UInt(0)
        DestValueValid(rownumber1) := UInt(0)
        OrderingBit(rownumber1) := UInt(0)
      }
      when(io.RenameLoadStoreValid2===UInt(1)){
        ValidEntry(rownumber2) := UInt(1)
        DestAddress(rownumber2) := UInt(0)
        DestValue(rownumber2) := UInt(0)
        DestAddressValid(rownumber2) := UInt(0)
        DestValueValid(rownumber2) := UInt(0)
        OrderingBit(rownumber2) := UInt(0)
      }
      when(io.RenameLoadStoreValid3===UInt(1)){
        ValidEntry(rownumber3) := UInt(1)
        DestAddress(rownumber3) := UInt(0)
        DestValue(rownumber3) := UInt(0)
        DestAddressValid(rownumber3) := UInt(0)
        DestValueValid(rownumber3) := UInt(0)
        OrderingBit(rownumber3) := UInt(0)
      }

      when(io.RenameLoadStoreValid0===UInt(1)){
        rs1(rownumber0) := io.RenameSourceAValue0
      }
      when(io.RenameLoadStoreValid1===UInt(1)){
        rs1(rownumber1) := io.RenameSourceAValue1
      }
      when(io.RenameLoadStoreValid2===UInt(1)){
        rs1(rownumber2) := io.RenameSourceAValue2
      }
      when(io.RenameLoadStoreValid3===UInt(1)){
        rs1(rownumber3) := io.RenameSourceAValue3
      }

      when(io.RenameLoadStoreValid0===UInt(1)){
        rs1_valid(rownumber0) := io.RenameSourceAValueValid0
      }
      when(io.RenameLoadStoreValid1===UInt(1)){
        rs1_valid(rownumber1) := io.RenameSourceAValueValid1
      }
      when(io.RenameLoadStoreValid2===UInt(1)){
        rs1_valid(rownumber2) := io.RenameSourceAValueValid2
      }
      when(io.RenameLoadStoreValid3===UInt(1)){
        rs1_valid(rownumber3) := io.RenameSourceAValueValid3
      }

      when(io.RenameLoadStoreValid0===UInt(1)){
        rs1_tag(rownumber0) := io.RenameSourceATag0
      }
      when(io.RenameLoadStoreValid1===UInt(1)){
        rs1_tag(rownumber1) := io.RenameSourceATag1
      }
      when(io.RenameLoadStoreValid2===UInt(1)){
        rs1_tag(rownumber2) := io.RenameSourceATag2
      }
      when(io.RenameLoadStoreValid3===UInt(1)){
        rs1_tag(rownumber3) := io.RenameSourceATag3
      }

      when(io.RenameLoadStoreValid0===UInt(1)){
        rs2(rownumber0) := io.RenameSourceBValue0
      }
      when(io.RenameLoadStoreValid1===UInt(1)){
        rs2(rownumber1) := io.RenameSourceBValue1
      }
      when(io.RenameLoadStoreValid2===UInt(1)){
        rs2(rownumber2) := io.RenameSourceBValue2
      }
      when(io.RenameLoadStoreValid3===UInt(1)){
        rs2(rownumber3) := io.RenameSourceBValue3
      }

      when(io.RenameLoadStoreValid0===UInt(1)){
        rs2_valid(rownumber0) := io.RenameSourceBValueValid0
      }
      when(io.RenameLoadStoreValid1===UInt(1)){
        rs2_valid(rownumber1) := io.RenameSourceBValueValid1
      }
      when(io.RenameLoadStoreValid2===UInt(1)){
        rs2_valid(rownumber2) := io.RenameSourceBValueValid2
      }
      when(io.RenameLoadStoreValid3===UInt(1)){
        rs2_valid(rownumber3) := io.RenameSourceBValueValid3
      }

      when(io.RenameLoadStoreValid0===UInt(1)){
        rs2_tag(rownumber0) := io.RenameSourceBTag0
      }
      when(io.RenameLoadStoreValid1===UInt(1)){
        rs2_tag(rownumber1) := io.RenameSourceBTag1
      }
      when(io.RenameLoadStoreValid2===UInt(1)){
        rs2_tag(rownumber2) := io.RenameSourceBTag2
      }
      when(io.RenameLoadStoreValid3===UInt(1)){
        rs2_tag(rownumber3) := io.RenameSourceBTag3
      }

      when(io.RenameLoadStoreValid0===UInt(1)){
        Opcode(rownumber0) := io.Rename_Opcode_0
      }
      when(io.RenameLoadStoreValid1===UInt(1)){
        Opcode(rownumber1) := io.Rename_Opcode_1
      }
      when(io.RenameLoadStoreValid2===UInt(1)){
        Opcode(rownumber2) := io.Rename_Opcode_2
      }
      when(io.RenameLoadStoreValid3===UInt(1)){
        Opcode(rownumber3) := io.Rename_Opcode_3
      }

      when(io.RenameLoadStoreValid0===UInt(1)){
        DestTag(rownumber0) := io.Rename_destTag_0
      }
      when(io.RenameLoadStoreValid1===UInt(1)){
        DestTag(rownumber1) := io.Rename_destTag_1
      }
      when(io.RenameLoadStoreValid2===UInt(1)){
        DestTag(rownumber2) := io.Rename_destTag_2
      }
      when(io.RenameLoadStoreValid3===UInt(1)){
        DestTag(rownumber3) := io.Rename_destTag_3
      }

      when(io.RenameLoadStoreValid0===UInt(1)){
        func3(rownumber0) := io.Rename_func3_0
      }
      when(io.RenameLoadStoreValid1===UInt(1)){
        func3(rownumber1) := io.Rename_func3_1
      }
      when(io.RenameLoadStoreValid2===UInt(1)){
        func3(rownumber2) := io.Rename_func3_2
      }
      when(io.RenameLoadStoreValid3===UInt(1)){
        func3(rownumber3) := io.Rename_func3_3
      }

      when(io.RenameLoadStoreValid0===UInt(1)){
        func7(rownumber0) := io.Rename_func7_0
      }
      when(io.RenameLoadStoreValid1===UInt(1)){
        func7(rownumber1) := io.Rename_func7_1
      }
      when(io.RenameLoadStoreValid2===UInt(1)){
        func7(rownumber2) := io.Rename_func7_2
      }
      when(io.RenameLoadStoreValid3===UInt(1)){
        func7(rownumber3) := io.Rename_func7_3
      }

      when(io.RenameLoadStoreValid0===UInt(1)){
        DestLoadstoreSelect(rownumber0) := Bool(io.RenameStoreSelect0)
      }
      when(io.RenameLoadStoreValid1===UInt(1)){
        DestLoadstoreSelect(rownumber1) := Bool(io.RenameStoreSelect1)
      }
      when(io.RenameLoadStoreValid2===UInt(1)){
        DestLoadstoreSelect(rownumber2) := Bool(io.RenameStoreSelect2)
      }
      when(io.RenameLoadStoreValid3===UInt(1)){
        DestLoadstoreSelect(rownumber3) := Bool(io.RenameStoreSelect3)
      }

      when(io.RenameLoadStoreValid0===UInt(1)){
        DestROB(rownumber0) := io.RenameROBtag0
      }
      when(io.RenameLoadStoreValid1===UInt(1)){
        DestROB(rownumber1) := io.RenameROBtag1
      }
      when(io.RenameLoadStoreValid2===UInt(1)){
        DestROB(rownumber2) := io.RenameROBtag2
      }
      when(io.RenameLoadStoreValid3===UInt(1)){
        DestROB(rownumber3) := io.RenameROBtag3
      }
// to store the list of ROB tags used in the issue queue
      when(io.RenameLoadStoreValid0===UInt(1)){
        ROB_list(io.RenameROBtag0) := UInt(1)
      }
      when(io.RenameLoadStoreValid1===UInt(1)){
        ROB_list(io.RenameROBtag1) := UInt(1)
      }
      when(io.RenameLoadStoreValid2===UInt(1)){
        ROB_list(io.RenameROBtag2) := UInt(1)
      }
      when(io.RenameLoadStoreValid3===UInt(1)){
        ROB_list(io.RenameROBtag3) := UInt(1)
      }

      //DestRegister(rownumber0) := io.Rename_dest_0
      //DestRegister(rownumber1) := io.Rename_dest_1
      //DestRegister(rownumber2) := io.Rename_dest_2
      //DestRegister(rownumber3) := io.Rename_dest_3


    }
  }





  for(x<-31 to 0 by -1){

    when(ValidEntry(x)===UInt(1)){
    
      onlyStoreInstructions:=Bool(true) && DestLoadstoreSelect(x)
    }
  }



  when(onlyStoreInstructions===Bool(false)){
    
      for(j<-31 to 0 by -1){
        
        when(ValidEntry(j)===UInt(1)){
          OrderingBit(j):= UInt(1)
        }
      
      }

  }
    .otherwise{

      for(j<-127 to 0 by -1){
       
        when(ROB_list(j)===UInt(1)){
          lowestROB_Load:= UInt(j,width=7)
        
        }
      }

      for(b<-31 to 0 by -1){

        when(DestROB(b)===lowestROB_Load){
          OrderingBit(b):=UInt(1)
        }
      }

        
          
    }
// check all the enteries in the l/s queue and push the ready ones out of the queue
  
  for(k<-31 to 0 by -1){

    when((ValidEntry(k)===UInt(1)) && (OrderingBit(k)===UInt(1)) && (DestAddressValid(k)===UInt(1)) && (DestValueValid(k)===UInt(1))){


      io.LoadStoreDestTag_out := DestTag(k)
      io.LoadStoreDestAddress := DestAddress(k)
      io.LoadStoreDestValue := DestValue(k)
      io.LoadStoreDestValid_out := UInt(1)
      io.LoadStoreSelect := DestLoadstoreSelect(k)
      io.LoadStoreROBTag := DestROB(k)
      //rowPlace:=SInt(k) 
// remove the ROB tag from the ROB list
      ROB_list(io.LoadStoreROBTag):=UInt(0)
      //for(d<-0 to 127) {      

      //  when(UInt(d)===DestROB(k)){
      //    ROB_list(d)===UInt(0)
      //  }
      //}

      //when(rowPlace===UInt(31)){
      //  rs1(31) := UInt(0)
      //  rs1_valid(31) := UInt(0)
      //  rs1_tag(31) := UInt(0)
      //  rs2(31) := UInt(0)
      //  rs2_valid(31) := UInt(0)
      //  rs2_tag(31) := UInt(0)

      //  Opcode(31) := UInt(0)
      //  DestTag(31) := UInt(0)
      //  func3(31) := UInt(0)
      //  func7(31) := UInt(0)
      //  Imm(31) := UInt(0)
      //  DestLoadstoreSelect(31) := UInt(0)
      //  DestROB(31) := UInt(0)
      //  //DestRegister(31) := UInt(0)
      //  DestAddress(31) := UInt(0)
      //  DestValue(31) := UInt(0)

      //  ValidEntry(31) := UInt(0)
      //  DestAddressValid(31) := UInt(0)
      //  DestValueValid(31) := UInt(0)

      //  OrderingBit(31) := UInt(0)

      //  }

      //.otherwise{

        for(y<-k until 31){
          rs1(y)  :=           rs1(y+1) 
          rs1_valid(y)  :=           rs1_valid(y+1) 
          rs1_tag(y)  :=           rs1_tag(y+1) 
          rs2(y)  :=           rs2(y+1) 
          rs2_valid(y)  :=           rs2_valid(y+1) 
          rs2_tag(y)  :=           rs2_tag(y+1) 
          Opcode(y)  :=           Opcode(y+1) 
          DestTag(y)  :=           DestTag(y+1) 
          func3(y)  :=           func3(y+1) 
          func7(y)  :=           func7(y+1) 
          Imm(y)  :=           Imm(y+1) 
          DestLoadstoreSelect(y)  :=           DestLoadstoreSelect(y+1) 
          DestROB(y)  :=           DestROB(y+1) 
          //DestRegister(y)  :=           DestRegister(y+1) 
          DestAddress(y)  :=           DestAddress(y+1) 
          DestValue(y)  :=           DestValue(y+1) 
          ValidEntry(y)  :=           ValidEntry(y+1) 
          DestAddressValid(y)  :=           DestAddressValid(y+1) 
          DestValueValid(y)  :=           DestValueValid(y+1) 
          OrderingBit(y)  :=           OrderingBit(y+1) 

        }

          rs1(31) := UInt(0)
          rs1_valid(31) := UInt(0)
          rs1_tag(31) := UInt(0)
          rs2(31) := UInt(0)
          rs2_valid(31) := UInt(0)
          rs2_tag(31) := UInt(0)

          Opcode(31) := UInt(0)
          DestTag(31) := UInt(0)
          func3(31) := UInt(0)
          func7(31) := UInt(0)
          Imm(31) := UInt(0)
          DestLoadstoreSelect(31) := UInt(0)
          DestROB(31) := UInt(0)
          //DestRegister(31) := UInt(0)
          DestAddress(31) := UInt(0)
          DestValue(31) := UInt(0)

          ValidEntry(31) := UInt(0)
          DestAddressValid(31) := UInt(0)
          DestValueValid(31) := UInt(0)

          OrderingBit(31) := UInt(0)

      //}





    }// when to take the value out from the register to output ports
// update the dest address and dest value for all store instructions
  when((ValidEntry(k)===UInt(1)) && ((DestValueValid(k)===UInt(0)) || (DestAddressValid(k)===UInt(0))) && (DestLoadstoreSelect(k)===Bool(true))){

    when(rs1_valid(k)===UInt(1)){
      //DestAddress:= Cat(rs1(63,12),(rs1(11,0) + Imm))
      DestAddress(k):= rs1(k) + Cat(Fill(52,UInt(0,width=1)),Imm(k))
      DestAddressValid(k):= UInt(1)
    }
    when(rs2_valid(k)===UInt(1)){
      // SB instruction take 8 bits from rs2 sign extend it to 64 bit
      when(func3(k)===UInt(0)){
        // Sign extention
        when(rs2(k)(7)===UInt(1)){
          sign_extended_value0:=Fill(56,UInt(1,width=1))
      
          DestValue(k):= Cat(sign_extended_value0,rs2(k)(7,0))
          DestValueValid(k):=UInt(1)
        }
        .elsewhen(rs2(k)(7)===UInt(0)){
          sign_extended_value0:=Fill(56,UInt(0,width=1))
      
          DestValue(k):= Cat(sign_extended_value0,rs2(k)(7,0))
          DestValueValid(k):=UInt(1)
        }
      
      }
      // SH instruction take 16 bits from rs2 sign extend it to 64 bit
      .elsewhen(func3(k)===UInt(1)){
        // Sign extention
        when(rs2(k)(15)===UInt(1)){
          sign_extended_value1:=Fill(48,UInt(1,width=1))
      
          DestValue(k):= Cat(sign_extended_value1,rs2(k)(15,0))
          DestValueValid(k):=UInt(1)
        }
        .elsewhen(rs2(k)(15)===UInt(0)){
          sign_extended_value1:=Fill(48,UInt(0,width=1))
      
          DestValue(k):= Cat(sign_extended_value1,rs2(k)(15,0))
          DestValueValid(k):=UInt(1)
        }
      
      }
      // SW instruction take 32 bits from rs2 sign extend it to 64 bit
      .elsewhen(func3(k)===UInt(2)){
        // Sign extention
        when(rs2(k)(32)===UInt(1)){
          sign_extended_value2:=Fill(32,UInt(1,width=1))
      
          DestValue(k):= Cat(sign_extended_value2,rs2(k)(31,0))
          DestValueValid(k):=UInt(1)
        }
        .elsewhen(rs2(k)(32)===UInt(0)){
          sign_extended_value2:=Fill(32,UInt(0,width=1))
      
          DestValue(k):= Cat(sign_extended_value2,rs2(k)(31,0))
          DestValueValid(k):=UInt(1)
        }
      
      }
      // SD instruction take 64 bits from rs2 sign extend it to 64 bit
      .elsewhen(func3(k)===UInt(3)){

        DestValue(k):= rs2(k)
        DestValueValid(k):=UInt(1)
      }
      // SBU instruction take 8 bits from rs2 sign extend it to 64 bit
      .elsewhen(func3(k)===UInt(4)){
        // Un-Sign extention
          sign_extended_value0:=Fill(56,UInt(0,width=1))
      
          DestValue(k):= Cat(sign_extended_value0,rs2(k)(7,0))
          DestValueValid(k):=UInt(1)
      
      }
      // SHU instruction take 16 bits from rs2 sign extend it to 64 bit
      .elsewhen(func3(k)===UInt(5)){
        // Un-Sign extention
          sign_extended_value1:=Fill(48,UInt(0,width=1))
      
          DestValue(k):= Cat(sign_extended_value1,rs2(k)(15,0))
          DestValueValid(k):=UInt(1)
      
      }
      // SWU instruction take 32 bits from rs2 sign extend it to 64 bit
      .elsewhen(func3(k)===UInt(6)){
        // Un-Sign extention
          sign_extended_value2:=Fill(32,UInt(0,width=1))
      
          DestValue(k):= Cat(sign_extended_value2,rs2(k)(31,0))
          DestValueValid(k):=UInt(1)
    
      }


    }
  }
// update the dest address and make it ready to  send a read req to cache for all load instructions
  when((ValidEntry(k)===UInt(1)) && ((DestAddressValid(k)===UInt(0))) && (DestLoadstoreSelect(k)===Bool(false))){

    when(rs1_valid(k)===UInt(1)){
      //DestAddress:= Cat(rs1(63,12),(rs1(11,0) + Imm))
      DestAddress(k):= rs1(k) + Cat(Fill(52,UInt(0,width=1)),Imm(k))
      DestAddressValid(k):= UInt(1)
    }
  }


// check for a ready load instruction and send a request to the D-Cache
  when((ValidEntry(k)===UInt(1)) && ((DestAddressValid(k)===UInt(1))) && (DestLoadstoreSelect(k)===Bool(false))&&(DestValueValid(k)===UInt(0))&&(io.Dcache_busy===UInt(0))){

    io.Cache_enable:= UInt(1)
    io.Cache_Address:= DestAddress(k)
    io.Cache_Compare:= UInt(1)
    io.Cache_tag_in:= DestROB(k)
    io.Cache_valid_in:= UInt(1)

  }


  }//for loop

}//Module

class LoadStoreQueueTester(l:LoadStoreQueueModule) extends Tester(l) {
  
        val selectRow0 = 0
        val selectRow1 = 1
        val selectRow2 = 2
        val selectRow3 = 3
        val selectRow4 = 4
        val selectRow5 = 5
        val selectRow6 = 6
        val selectRow7 = 7
        val selectRow8 = 8
        val selectRow9 = 9
        val selectRow10 = 10
        val selectRow11 = 11
        val selectRow12 = 12
        val selectRow13 = 13
        val selectRow14 = 14
        val selectRow15 = 15
        val rand =rnd.nextInt(213123)
        val SourceA = 10
        val SourceB = 11

     // selecting only one row select for store instruction
     poke(l.io.RenameLoadStoreValid0,1)
     poke(l.io.RenameLoadStoreRowSelect0,0)
     poke(l.io.RenameSourceAValue0,SourceA)
     poke(l.io.RenameSourceAValueValid0,1)
     poke(l.io.RenameSourceATag0,0)
     poke(l.io.RenameSourceBValue0,SourceB)
     poke(l.io.RenameSourceBValueValid0,1)
     poke(l.io.RenameSourceBTag0,1)

     poke(l.io.Rename_Opcode_0,rand)
     poke(l.io.Rename_destTag_0,rand)
     poke(l.io.Rename_func3_0,rand)
     poke(l.io.Rename_func7_0,rand)
     poke(l.io.Rename_Imm_0,rand)
     poke(l.io.RenameStoreSelect0,true)
     poke(l.io.RenameROBtag0,12)

     step(1)
     poke(l.io.RenameLoadStoreValid0,0)
     poke(l.io.RenameLoadStoreRowSelect0,0)
     poke(l.io.RenameSourceAValue0,0)
     poke(l.io.RenameSourceAValueValid0,0)
     poke(l.io.RenameSourceATag0,0)
     poke(l.io.RenameSourceBValue0,0)
     poke(l.io.RenameSourceBValueValid0,0)
     poke(l.io.RenameSourceBTag0,0)

     poke(l.io.Rename_Opcode_0,0)
     poke(l.io.Rename_destTag_0,0)
     poke(l.io.Rename_func3_0,0)
     poke(l.io.Rename_func7_0,0)
     poke(l.io.Rename_Imm_0,0)
     poke(l.io.RenameStoreSelect0,false)
     poke(l.io.RenameROBtag0,0)
     step(51)

  //for(i<-0 until 4) {

  //   poke(l.io.RenameValid,1)
  //   // port_0
  //   poke(l.io.RenameLoadStoreRowSelect0,t)
  //   poke(l.io.RenameSourceAValue0,t)
  //   poke(l.io.RenameSourceAValueValid0,0)
  //   poke(l.io.RenameSourceATag0,t+4)
  //   poke(l.io.RenameSourceBValue0,t+1)
  //   poke(l.io.RenameSourceBValueValid0,0)
  //   poke(l.io.RenameSourceBTag0,t+5)

  //   //poke(l.io.Rename_Opcode_0,t+9B)
  //   poke(l.io.Rename_Opcode_0,rand)
  //   poke(l.io.Rename_destTag_0,rand)
  //   poke(l.io.Rename_func3_0,rand)
  //   poke(l.io.Rename_func7_0,rand)
  //   poke(l.io.Rename_Imm_0,rand)
  //   poke(l.io.RenameStoreSelect0,true)
  //   poke(l.io.RenameROBtag0,rand)

  //   // port_1
  //   poke(l.io.RenameLoadStoreRowSelect1,t+1)
  //   poke(l.io.RenameSourceAValue1,t+1)
  //   poke(l.io.RenameSourceAValueValid1,0)
  //   poke(l.io.RenameSourceATag1,t+5)
  //   poke(l.io.RenameSourceBValue1,t+2)
  //   poke(l.io.RenameSourceBValueValid1,0)
  //   poke(l.io.RenameSourceBTag1,t+6)

  //   poke(l.io.Rename_Opcode_1,rand)
  //   poke(l.io.Rename_destTag_1,rand)
  //   poke(l.io.Rename_func3_1,rand)
  //   poke(l.io.Rename_func7_1,rand)
  //   poke(l.io.Rename_Imm_1,rand)
  //   poke(l.io.RenameStoreSelect1,true)
  //   poke(l.io.RenameROBtag1,rand)

  //   // port_2
  //   poke(l.io.RenameLoadStoreRowSelect2,t+2)
  //   poke(l.io.RenameSourceAValue2,t+2)
  //   poke(l.io.RenameSourceAValueValid2,0)
  //   poke(l.io.RenameSourceATag2,t+6)
  //   poke(l.io.RenameSourceBValue2,t+3)
  //   poke(l.io.RenameSourceBValueValid2,0)
  //   poke(l.io.RenameSourceBTag2,t+7)

  //   poke(l.io.Rename_Opcode_2,rand)
  //   poke(l.io.Rename_destTag_2,rand)
  //   poke(l.io.Rename_func3_2,rand)
  //   poke(l.io.Rename_func7_2,rand)
  //   poke(l.io.Rename_Imm_2,rand)
  //   poke(l.io.RenameStoreSelect2,true)
  //   poke(l.io.RenameROBtag2,rand)

  //   // port_3
  //   poke(l.io.RenameLoadStoreRowSelect3,t+3)
  //   poke(l.io.RenameSourceAValue3,t+3)
  //   poke(l.io.RenameSourceAValueValid3,0)
  //   poke(l.io.RenameSourceATag3,t+7)
  //   poke(l.io.RenameSourceBValue3,t+5)
  //   poke(l.io.RenameSourceBValueValid3,0)
  //   poke(l.io.RenameSourceBTag3,t+9)

  //   poke(l.io.Rename_Opcode_3,rand)
  //   poke(l.io.Rename_destTag_3,rand)
  //   poke(l.io.Rename_func3_3,rand)
  //   poke(l.io.Rename_func7_3,rand)
  //   poke(l.io.Rename_Imm_3,rand)
  //   poke(l.io.RenameStoreSelect3,true)
  //   poke(l.io.RenameROBtag3,rand)

  //   step(1)
  //  
  //}

//        poke(l.io.FUBroadcastValid0,1)
//        poke(l.io.FUBroadcastTag0,0)
//        poke(l.io.FUBroadcastValue0,52)
//        
//        poke(l.io.FUBroadcastValid1,1)
//        poke(l.io.FUBroadcastTag1,1)
//        poke(l.io.FUBroadcastValue1,57)
//
//     step(1)
//     // selecting only one row select for load instruction
//     poke(l.io.RenameLoadStoreValid0,1)
//     poke(l.io.RenameLoadStoreRowSelect0,1)
//     poke(l.io.RenameSourceAValue0,SourceB)
//     poke(l.io.RenameSourceAValueValid0,1)
//     poke(l.io.RenameSourceATag0,0)
//     poke(l.io.RenameSourceBValue0,SourceA)
//     poke(l.io.RenameSourceBValueValid0,1)
//     poke(l.io.RenameSourceBTag0,1)
//
//     poke(l.io.Rename_Opcode_0,rand)
//     poke(l.io.Rename_destTag_0,rand)
//     poke(l.io.Rename_func3_0,rand)
//     poke(l.io.Rename_func7_0,rand)
//     poke(l.io.Rename_Imm_0,rand)
//     poke(l.io.RenameStoreSelect0,false)
//     poke(l.io.RenameROBtag0,15)
//     poke(l.io.Dcache_busy,1)
//
//     step(1)
     //poke(l.io.RenameLoadStoreValid0,0)
     //poke(l.io.RenameLoadStoreRowSelect0,0)
     //poke(l.io.RenameSourceAValue0,0)
     //poke(l.io.RenameSourceAValueValid0,0)
     //poke(l.io.RenameSourceATag0,0)
     //poke(l.io.RenameSourceBValue0,0)
     //poke(l.io.RenameSourceBValueValid0,0)
     //poke(l.io.RenameSourceBTag0,0)

     //poke(l.io.Rename_Opcode_0,0)
     //poke(l.io.Rename_destTag_0,0)
     //poke(l.io.Rename_func3_0,0)
     //poke(l.io.Rename_func7_0,0)
     //poke(l.io.Rename_Imm_0,0)
     //poke(l.io.RenameStoreSelect0,false)
     //poke(l.io.RenameROBtag0,0)
     //poke(l.io.Dcache_busy,0)
//     step(1)
//        
//        poke(l.io.FUBroadcastValid2,1)
//        poke(l.io.FUBroadcastTag2,8)
//        poke(l.io.FUBroadcastValue2,32)
//        
//        poke(l.io.LoadStoreDestValid,1)
//        poke(l.io.LoadStoreDestTag,14)
//        poke(l.io.LoadStoreDestVal,54)
//        
//     step(1)
//        poke(l.io.FUBroadcastValid3,1)
//        poke(l.io.FUBroadcastTag3,15)
//        poke(l.io.FUBroadcastValue3,93)
//        poke(l.io.Dcache_Valid,1)
//        poke(l.io.Dcache_busy,0)
//        poke(l.io.Dcache_tag_out,12)
//        poke(l.io.Dcache_data_out,83)
//     step(1)
//        poke(l.io.Dcache_Valid,0)
//        poke(l.io.Dcache_busy,1)
//        poke(l.io.Dcache_tag_out,12)
//        poke(l.io.Dcache_data_out,83)
//     step(20)


    // selecting only one row select for load instruction
    poke(l.io.RenameLoadStoreValid0,1)
    poke(l.io.RenameLoadStoreRowSelect0,0)
    poke(l.io.RenameSourceAValue0,SourceB)
    poke(l.io.RenameSourceAValueValid0,1)
    poke(l.io.RenameSourceATag0,0)
    poke(l.io.RenameSourceBValue0,SourceA)
    poke(l.io.RenameSourceBValueValid0,1)
    poke(l.io.RenameSourceBTag0,1)
                                                           
    poke(l.io.Rename_Opcode_0,rand)
    poke(l.io.Rename_destTag_0,rand)
    poke(l.io.Rename_func3_0,rand)
    poke(l.io.Rename_func7_0,rand)
    poke(l.io.Rename_Imm_0,rand)
    poke(l.io.RenameStoreSelect0,false)
    poke(l.io.RenameROBtag0,12)
                                                           
    step(1)
    poke(l.io.RenameLoadStoreValid0,0)
    poke(l.io.RenameLoadStoreRowSelect0,0)
    poke(l.io.RenameSourceAValue0,0)
    poke(l.io.RenameSourceAValueValid0,0)
    poke(l.io.RenameSourceATag0,0)
    poke(l.io.RenameSourceBValue0,0)
    poke(l.io.RenameSourceBValueValid0,0)
    poke(l.io.RenameSourceBTag0,0)
                                                           
    poke(l.io.Rename_Opcode_0,0)
    poke(l.io.Rename_destTag_0,0)
    poke(l.io.Rename_func3_0,0)
    poke(l.io.Rename_func7_0,0)
    poke(l.io.Rename_Imm_0,0)
    poke(l.io.RenameStoreSelect0,false)
    poke(l.io.RenameROBtag0,0)
    step(51)

    poke(l.io.Dcache_tag_out,12)
    poke(l.io.Dcache_data_out,1234)
    poke(l.io.Dcache_Valid,1)

    step(51)

































}

class LoadStoreQueueTestGenerator extends TestGenerator {
  def genMod(): Module = Module(new LoadStoreQueueModule())
  def genTest[T <: Module](l: T): Tester[T] = 
    (new LoadStoreQueueTester(l.asInstanceOf[LoadStoreQueueModule])).asInstanceOf[Tester[T]]
}

