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
        val RenameValid = UInt(INPUT,1)
   
        // Source "A" parameters in the loadstore queue
        val RenameSourceAValue0 = UInt(INPUT,64)
        val RenameSourceAValue1 = UInt(INPUT,64)
        val RenameSourceAValue2 = UInt(INPUT,64)
        val RenameSourceAValue3 = UInt(INPUT,64)
        
        val RenameSourceAValueValid0 = UInt(INPUT,1)
        val RenameSourceAValueValid1 = UInt(INPUT,1)
        val RenameSourceAValueValid2 = UInt(INPUT,1)
        val RenameSourceAValueValid3 = UInt(INPUT,1)

        val RenameSourceATag0 = UInt(INPUT,1)
        val RenameSourceATag1 = UInt(INPUT,1)
        val RenameSourceATag2 = UInt(INPUT,1)
        val RenameSourceATag3 = UInt(INPUT,1)

        // Source "B" parameters in the loadstore queue
        val RenameSourceBValue0 = UInt(INPUT,64)
        val RenameSourceBValue1 = UInt(INPUT,64)
        val RenameSourceBValue2 = UInt(INPUT,64)
        val RenameSourceBValue3 = UInt(INPUT,64)
        
        val RenameSourceBValueValid0 = UInt(INPUT,1)
        val RenameSourceBValueValid1 = UInt(INPUT,1)
        val RenameSourceBValueValid2 = UInt(INPUT,1)
        val RenameSourceBValueValid3 = UInt(INPUT,1)

        val RenameSourceBTag0 = UInt(INPUT,1)
        val RenameSourceBTag1 = UInt(INPUT,1)
        val RenameSourceBTag2 = UInt(INPUT,1)
        val RenameSourceBTag3 = UInt(INPUT,1)

        //Decode parameters

        val Rename_Opcode_0 = UInt(INPUT,7)
        val Rename_Opcode_1 = UInt(INPUT,7)
        val Rename_Opcode_2 = UInt(INPUT,7)
        val Rename_Opcode_3 = UInt(INPUT,7)

        
        val Rename_destTag_0 = UInt(INPUT,10)
        val Rename_destTag_1 = UInt(INPUT,10)
        val Rename_destTag_2 = UInt(INPUT,10)
        val Rename_destTag_3 = UInt(INPUT,10)

        val Rename_func3_0 = UInt(INPUT,7)
        val Rename_func3_1 = UInt(INPUT,7)
        val Rename_func3_2 = UInt(INPUT,7)
        val Rename_func3_3 = UInt(INPUT,7)
        
        val Rename_func7_0 = UInt(INPUT,7)
        val Rename_func7_1 = UInt(INPUT,7)
        val Rename_func7_2 = UInt(INPUT,7)
        val Rename_func7_3 = UInt(INPUT,7)
        
        val Rename_Imm_0 = UInt(INPUT,7)
        val Rename_Imm_1 = UInt(INPUT,7)
        val Rename_Imm_2 = UInt(INPUT,7)
        val Rename_Imm_3 = UInt(INPUT,7)
        
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

        //(destination register names)
        val Rename_dest_0 = UInt(INPUT,5)
        val Rename_dest_1 = UInt(INPUT,5)
        val Rename_dest_2 = UInt(INPUT,5)
        val Rename_dest_3 = UInt(INPUT,5)

        //D-cache parameters
        val Dcache_Val0 = UInt(INPUT,64)
        val Dcache_Val1 = UInt(INPUT,64)
        val Dcache_Val2 = UInt(INPUT,64)
        val Dcache_Val3 = UInt(INPUT,64)

        val Dcache_Valid0 = UInt(INPUT,1)// one valid bit must be there for decache
        val Dcache_Valid1 = UInt(INPUT,1)// one valid bit must be there for decache
        val Dcache_Valid2 = UInt(INPUT,1)// one valid bit must be there for decache
        val Dcache_Valid3 = UInt(INPUT,1)// one valid bit must be there for decache

        val Dcache_Tag0 = UInt(INPUT,64)
        val Dcache_Tag1 = UInt(INPUT,64)
        val Dcache_Tag2 = UInt(INPUT,64)
        val Dcache_Tag3 = UInt(INPUT,64)

	val FUBroadcastValue0 = UInt(INPUT,64)
	val FUBroadcastTag0 = UInt(INPUT,7)
	val FUBroadcastValid0 = UInt(INPUT,1) //need to be added in the document

	val FUBroadcastValue1 = UInt(INPUT,64)
	val FUBroadcastTag1 = UInt(INPUT,7)
	val FUBroadcastValid1 = UInt(INPUT,1) //need to be added in the document

	val FUBroadcastValue2 = UInt(INPUT,64)
	val FUBroadcastTag2 = UInt(INPUT,7)
	val FUBroadcastValid2 = UInt(INPUT,1) //need to be added in the document

	val FUBroadcastValue3 = UInt(INPUT,64)
	val FUBroadcastTag3 = UInt(INPUT,7)
	val FUBroadcastValid3 = UInt(INPUT,1) //need to be added in the document
	
        val LoadStoreDestVal = UInt(INPUT,64)
	val LoadStoreDestTag = UInt(INPUT,7)
	val LoadStoreDestValid = UInt(INPUT,1) // need to be added in the document

        // All OUPUT ports

        val LoadStoreDestTag_out = UInt(OUTPUT,10)
        val LoadStoreDestAddress = UInt(OUTPUT,64)
        val LoadStoreDestValue = UInt(OUTPUT,64)
        val LoadStoreDestValid_out = UInt(OUTPUT,1)

        val LoadStoreSelect = Bool(OUTPUT)
        val LoadStoreROBTag = UInt(OUTPUT,7)

        val LoadStoreFull = UInt(OUTPUT,1)
        
        

    
  }

  // Registers

  val rs1 = Vec.fill(32){Reg(init = UInt(0,width=64))}
  val rs1_valid = Vec.fill(32){Reg(init = UInt(0,width=1))}
  val rs1_tag = Vec.fill(32){Reg(init = UInt(0,width=7))}
  val rs2 = Vec.fill(32){Reg(init = UInt(0,width=64))}
  val rs2_valid = Vec.fill(32){Reg(init = UInt(0,width=1))}
  val rs2_tag = Vec.fill(32){Reg(init = UInt(0,width=7))}

  val Opcode = Vec.fill(32){Reg(init = UInt(0,width=7))}
  val DestTag = Vec.fill(32){Reg(init = UInt(0,width=10))}
  val func3 = Vec.fill(32){Reg(init = UInt(0,width=3))}
  val func7 = Vec.fill(32){Reg(init = UInt(0,width=7))}
  val Imm = Vec.fill(32){Reg(init = UInt(0,width=12))}
  val DestLoadstoreSelect = Vec.fill(32){Reg(init =Bool(false))}
  val DestROB = Vec.fill(32){Reg(init = UInt(0,width=7))}
  val DestRegister = Vec.fill(32){Reg(init = UInt(0,width=5))}
  val DestAddress = Vec.fill(32){Reg(init = UInt(0,width=64))}
  val DestValue = Vec.fill(32){Reg(init = UInt(0,width=64))}

  val ValidEntry = Vec.fill(32){Reg(init=UInt(0,width=1))}
  val DestAddressValid = Vec.fill(32){Reg(init = UInt(0,width=1))}
  val DestValueValid = Vec.fill(32){Reg(init = UInt(0,width=1))}

  val OrderingBit = Vec.fill(32){Reg(init = UInt(0,width=1))}

  val rowPlace = Reg(init = SInt(-1,width=1))

  val lowestROB_Load = UInt(width=7)

  val ROB_list = Vec.fill(128){Reg(init = UInt(0,width=1))}


// wire 
  val onlyStoreInstructions = Bool()

  
 
  // setting the default values for output ports    
        io.LoadStoreDestTag_out := UInt(0)
        io.LoadStoreDestAddress := UInt(0)
        io.LoadStoreDestValue := UInt(0)
        io.LoadStoreDestValid_out := UInt(0)

        io.LoadStoreSelect := Bool(false)
        io.LoadStoreROBTag := UInt(0)
        io.LoadStoreFull := UInt(0)
        lowestROB_Load := UInt(127)

        onlyStoreInstructions := Bool(false)
 
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
      DestRegister(i) := UInt(0)
      DestAddress(i) := UInt(0)
      DestValue(i) := UInt(0)

      ValidEntry(i) := UInt(0)
      DestAddressValid(i) := UInt(0)
      DestValueValid(i) := UInt(0)

      OrderingBit(i) := UInt(0)

      ROB_list(i) := UInt(0)

    }
      rowPlace := SInt(-1)

  } 
  // Update the entry in the load-store-queue based on FU broadcast for FU0
  when(io.FUBroadcastValid0===UInt(1)){
    
    for(j<-0 until 32){
      when((io.FUBroadcastTag0===rs1_tag(j))&&(rs1_valid(j)===UInt(0))&&(ValidEntry(j)===UInt(1))){
      
        rs1(j) := io.FUBroadcastValue0
        rs1_valid(j) := UInt(1)
        //printf("\n updated with FU0 value for rs1")

      }
    
      when((io.FUBroadcastTag0 === rs2_tag(j))&&(rs2_valid(j)===UInt(0))&&(ValidEntry(j)===UInt(1))){
      
        rs2(j) := io.FUBroadcastValue0
        rs2_valid(j) := UInt(1)
        //printf("\n updated with FU0 value for rs2")

      }
    
    }
  }// when(io.FUBroadcastValid0===1)
  
  // Update the entry in the load-store-queue based on FU broadcast for FU1
  when(io.FUBroadcastValid1===UInt(1)){
    
    for(j<-0 until 32){
      when((io.FUBroadcastTag1 === rs1_tag(j))&&(rs1_valid(j)===UInt(0))&&(ValidEntry(j)===UInt(1))){
      
        rs1(j) := io.FUBroadcastValue1
        rs1_valid(j) := UInt(1)
        //printf("\n updated with FU1 value for rs1")

      }
    
      when((io.FUBroadcastTag1 === rs2_tag(j))&&(rs2_valid(j)===UInt(0))&&(ValidEntry(j)===UInt(1))){
      
        rs2(j) := io.FUBroadcastValue1
        rs2_valid(j) := UInt(1)
        //printf("\n updated with FU1 value for rs2")

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

  // Update the entry in the reservatio station based on L/S broadcast
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



  when(io.RenameValid===UInt(1)){

    when(io.LoadStoreFull===UInt(1)){

      //printf("\n This l/s queue is full\n")

    }
    .otherwise{

      val rownumber0 = io.RenameLoadStoreRowSelect0
      val rownumber1 = io.RenameLoadStoreRowSelect1
      val rownumber2 = io.RenameLoadStoreRowSelect2
      val rownumber3 = io.RenameLoadStoreRowSelect3

      // fix the valid entry
      ValidEntry(rownumber0) := UInt(1)
      ValidEntry(rownumber1) := UInt(1)
      ValidEntry(rownumber2) := UInt(1)
      ValidEntry(rownumber3) := UInt(1)

      rs1(rownumber0) := io.RenameSourceAValue0
      rs1(rownumber1) := io.RenameSourceAValue1
      rs1(rownumber2) := io.RenameSourceAValue2
      rs1(rownumber3) := io.RenameSourceAValue3

      rs1_valid(rownumber0) := io.RenameSourceAValueValid0
      rs1_valid(rownumber1) := io.RenameSourceAValueValid1
      rs1_valid(rownumber2) := io.RenameSourceAValueValid2
      rs1_valid(rownumber3) := io.RenameSourceAValueValid3

      rs1_tag(rownumber0) := io.RenameSourceATag0
      rs1_tag(rownumber1) := io.RenameSourceATag1
      rs1_tag(rownumber2) := io.RenameSourceATag2
      rs1_tag(rownumber3) := io.RenameSourceATag3

      rs2(rownumber0) := io.RenameSourceBValue0
      rs2(rownumber1) := io.RenameSourceBValue1
      rs2(rownumber2) := io.RenameSourceBValue2
      rs2(rownumber3) := io.RenameSourceBValue3

      rs2_valid(rownumber0) := io.RenameSourceBValueValid0
      rs2_valid(rownumber1) := io.RenameSourceBValueValid1
      rs2_valid(rownumber2) := io.RenameSourceBValueValid2
      rs2_valid(rownumber3) := io.RenameSourceBValueValid3

      rs2_tag(rownumber0) := io.RenameSourceBTag0
      rs2_tag(rownumber1) := io.RenameSourceBTag1
      rs2_tag(rownumber2) := io.RenameSourceBTag2
      rs2_tag(rownumber3) := io.RenameSourceBTag3

      Opcode(rownumber0) := io.Rename_Opcode_0
      Opcode(rownumber1) := io.Rename_Opcode_1
      Opcode(rownumber2) := io.Rename_Opcode_2
      Opcode(rownumber3) := io.Rename_Opcode_3

      DestTag(rownumber0) := io.Rename_destTag_0
      DestTag(rownumber1) := io.Rename_destTag_1
      DestTag(rownumber2) := io.Rename_destTag_2
      DestTag(rownumber3) := io.Rename_destTag_3

      func3(rownumber0) := io.Rename_func3_0
      func3(rownumber1) := io.Rename_func3_1
      func3(rownumber2) := io.Rename_func3_2
      func3(rownumber3) := io.Rename_func3_3

      func7(rownumber0) := io.Rename_func7_0
      func7(rownumber1) := io.Rename_func7_1
      func7(rownumber2) := io.Rename_func7_2
      func7(rownumber3) := io.Rename_func7_3

      DestLoadstoreSelect(rownumber0) := Bool(io.RenameStoreSelect0)
      DestLoadstoreSelect(rownumber1) := Bool(io.RenameStoreSelect1)
      DestLoadstoreSelect(rownumber2) := Bool(io.RenameStoreSelect2)
      DestLoadstoreSelect(rownumber3) := Bool(io.RenameStoreSelect3)

      DestROB(rownumber0) := io.RenameROBtag0
      DestROB(rownumber1) := io.RenameROBtag1
      DestROB(rownumber2) := io.RenameROBtag2
      DestROB(rownumber3) := io.RenameROBtag3
// to store the list of ROB tags used in the issue queue
      ROB_list(io.RenameROBtag0) := UInt(1)
      ROB_list(io.RenameROBtag1) := UInt(1)
      ROB_list(io.RenameROBtag2) := UInt(1)
      ROB_list(io.RenameROBtag3) := UInt(1)

      DestRegister(rownumber0) := io.Rename_dest_0
      DestRegister(rownumber1) := io.Rename_dest_1
      DestRegister(rownumber2) := io.Rename_dest_2
      DestRegister(rownumber3) := io.Rename_dest_3


    }
  }




// TODO rework
//val onlyStoreInstructions = (DestLoadstoreSelect(0)) && (DestLoadstoreSelect(1)) &&(DestLoadstoreSelect(2)) &&(DestLoadstoreSelect(3)) &&(DestLoadstoreSelect(4)) &&(DestLoadstoreSelect(5)) &&(DestLoadstoreSelect(6)) &&(DestLoadstoreSelect(7)) &&(DestLoadstoreSelect(8)) &&(DestLoadstoreSelect(9)) &&(DestLoadstoreSelect(10)) &&(DestLoadstoreSelect(11)) &&(DestLoadstoreSelect(12)) &&(DestLoadstoreSelect(13)) &&(DestLoadstoreSelect(14)) &&(DestLoadstoreSelect(15)) &&(DestLoadstoreSelect(16)) &&(DestLoadstoreSelect(17)) &&(DestLoadstoreSelect(18)) &&(DestLoadstoreSelect(19)) &&(DestLoadstoreSelect(20)) &&(DestLoadstoreSelect(21)) &&(DestLoadstoreSelect(22)) &&(DestLoadstoreSelect(23)) &&(DestLoadstoreSelect(24)) &&(DestLoadstoreSelect(25)) &&(DestLoadstoreSelect(26)) &&(DestLoadstoreSelect(27)) &&(DestLoadstoreSelect(28)) &&(DestLoadstoreSelect(29)) &&(DestLoadstoreSelect(30)) &&(DestLoadstoreSelect(31))
//

  for(x<-31 to 0 by -1){

    when(ValidEntry(x)===UInt(1)){
    
      onlyStoreInstructions:=Bool(true) && DestLoadstoreSelect(x)
    }
  }



  when(onlyStoreInstructions===Bool(false)){
    
      for(j<-31 to 0 by -1){
        
        OrderingBit(j):= UInt(1)
      
      }

  }
    .otherwise{

      for(j<-127 to 0 by -1){
       
        when(ROB_list(j)===UInt(1)){
        lowestROB_Load:= UInt(j,width=7)
        
        }
      }

        
          
          OrderingBit(lowestROB_Load):=UInt(1)
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
      rowPlace:=SInt(k) 
// remove the ROB tag from the ROB list
      for(d<-0 to 127) {      

        when(ROB_list(d)===DestROB(k)){
          ROB_list(d)===UInt(0)
        }
      }

      when(rowPlace===UInt(31)){
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
        DestRegister(31) := UInt(0)
        DestAddress(31) := UInt(0)
        DestValue(31) := UInt(0)

        ValidEntry(31) := UInt(0)
        DestAddressValid(31) := UInt(0)
        DestValueValid(31) := UInt(0)

        OrderingBit(31) := UInt(0)

        }

      .otherwise{

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
          DestRegister(y)  :=           DestRegister(y+1) 
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
          DestRegister(31) := UInt(0)
          DestAddress(31) := UInt(0)
          DestValue(31) := UInt(0)

          ValidEntry(31) := UInt(0)
          DestAddressValid(31) := UInt(0)
          DestValueValid(31) := UInt(0)

          OrderingBit(31) := UInt(0)

      }





    }// when to take the value out from the register to output ports
// update the dest address and dest value for all store instructions
  when((ValidEntry(k)===UInt(1)) && ((DestValueValid(k)===UInt(0)) || (DestAddressValid(k)===UInt(0))) && (DestLoadstoreSelect(k)===Bool(true))){

    when(rs1_valid(k)===UInt(1)){
      //DestAddress:= Cat(rs1(63,12),(rs1(11,0) + Imm))
      DestAddress(k):= rs1(k) + Cat(Fill(52,UInt(0,width=1)),Imm(k))
      DestAddressValid(k):= UInt(1)
    }
    when(rs2_valid(k)===UInt(1)){
      when(func3(k)===UInt(0)){
      
        DestValue(k):= Cat(Fill(56,UInt(0,width=1)),rs2(k)(7,0))
        DestValueValid(k):=UInt(1)
      }
      .elsewhen(func3(k)===UInt(2)){

        DestValue(k):= Cat(Fill(48,UInt(0,width=1)),rs2(k)(15,0))
        DestValueValid(k):=UInt(1)
      }
      .elsewhen(func3(k)===UInt(3)){

        DestValue(k):= Cat(Fill(32,UInt(0,width=1)),rs2(k)(31,0))
        DestValueValid(k):=UInt(1)
      }
      .elsewhen(func3(k)===UInt(4)){

        DestValue(k):= rs2(k)
        DestValueValid(k):=UInt(1)
      }
    
    }


  }


  }//for loop

}//Module

class LoadStoreQueueTester(l:LoadStoreQueueModule) extends Tester(l) {
  

    



}

class LoadStoreQueueTestGenerator extends TestGenerator {
  def genMod(): Module = Module(new LoadStoreQueueModule())
  def genTest[T <: Module](l: T): Tester[T] = 
    (new LoadStoreQueueTester(l.asInstanceOf[LoadStoreQueueModule])).asInstanceOf[Tester[T]]
}

