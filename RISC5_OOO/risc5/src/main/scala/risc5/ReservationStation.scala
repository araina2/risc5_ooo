package risc5

import Chisel._


class ReservationStationModule extends Module {

  val io = new Bundle {
        // All INPUT ports ( all the ports in the doc for the issue queue needs to be updated to 64 bits , which is not the case in the doc )
    
        val RenameRowSelect = UInt(INPUT,4)
        val RenameValid = UInt(INPUT,1) // need to add a valid bit in the doc


	val RenameSourceAValue = UInt(INPUT,64)
	val RenameSourceAValueValid = UInt(INPUT,1)
	val RenameSourceATag = UInt(INPUT,7)

	val RenameSourceBValue = UInt(INPUT,64)
	val RenameSourceBValueValid = UInt(INPUT,1)
	val RenameSourceBTag = UInt(INPUT,8)

        val RenameDestTag = UInt(INPUT,10)

	val LoadStoreDestVal = UInt(INPUT,64)
	val LoadStoreDestTag = UInt(INPUT,10)
	val LoadStoreDestValid = UInt(INPUT,1) // need to be added in the document

	val Decode_Opcode = UInt(INPUT,7)
	val Decode_Func3 = UInt(INPUT,3)
	val Decode_Func7 = UInt(INPUT,7)
	val Decode_Imm = UInt(INPUT,20)
	val DecodeROB = UInt(INPUT,7)
	val DecodeType = UInt(INPUT,3)
        
        // Design is implementing four functional units
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
        val FUisBusy = UInt(INPUT,1) // need to be added in the document  
        
        // All OUPUT ports
	val IssueSourceValA = UInt(OUTPUT,64)
	val IssueSourceValB = UInt(OUTPUT,64)

	val IssueFUOpcode = UInt(OUTPUT,7)
        val Issue_Func3 = UInt(OUTPUT,3)
        val Issue_Func7 = UInt(OUTPUT,7)
        val Issue_Imm = UInt(OUTPUT,20)
        val Issue_Type = UInt(OUTPUT,3)

	val IssuedestTag = UInt(OUTPUT,10)
        val Full = UInt(OUTPUT,1)
        val Valid = UInt(OUTPUT,1) // one valid bit need to be added
        val FreeRow = UInt(OUTPUT,4)
        

    
  }



  val DestTag = Vec.fill(16){Reg(init = UInt(0,width=10))}

  val IssueInstructionROB = Vec.fill(16){Reg(init = UInt(0,width=7))}

  val IssueSourceValAValid = Vec.fill(16){Reg(init = UInt(0,width=1))}
  val IssueSourceValA = Vec.fill(16){Reg(init = UInt(0,width=64))}
  val IssueSourceValATag = Vec.fill(16){Reg(init = UInt(0,width=7))}

  val IssueSourceValBValid = Vec.fill(16){Reg(init = UInt(0,width=1))}
  val IssueSourceValB = Vec.fill(16){Reg(init = UInt(0,width=64))}
  val IssueSourceValBTag = Vec.fill(16){Reg(init = UInt(0,width=7))}

  val Decode_Opcode = Vec.fill(16){Reg(init = UInt(0,width=7))}
  val Decode_Func3 = Vec.fill(16){Reg(init = UInt(0,width=3))}
  val Decode_Func7 = Vec.fill(16){Reg(init = UInt(0,width=7))}
  val Decode_Imm = Vec.fill(16){Reg(init = UInt(0,width=20))}
  val DecodeType = Vec.fill(16){Reg(init = UInt(0,width=3))}

  val ValidEntry = Vec.fill(16){Reg(init = UInt(0,width=1))}

  val Full = Reg(init = UInt(0,width=1))

  val rowPlace = Reg(init = SInt(-1,width=1))
 
  // setting the default values for output ports    
  io.IssueSourceValA := UInt(0)
  io.IssueSourceValB := UInt(0)
  io.IssueFUOpcode := UInt(0)
  io.IssuedestTag := UInt(0)
  io.Valid := UInt(0)
  io.FreeRow := UInt(0)

  io.Issue_Func3 := UInt(0)
  io.Issue_Func7 := UInt(0)
  io.Issue_Imm := UInt(0)
  io.Issue_Type:= UInt(0)
 
  // reset all the register values
  when(reset) {
    for(i<-0 until 16){
      DestTag(i) := UInt(0)

      IssueInstructionROB(i) := UInt(0)

      IssueSourceValAValid(i) := UInt(0)
      IssueSourceValA(i) := UInt(0)
      IssueSourceValATag(i) := UInt(0)

      IssueSourceValBValid(i) := UInt(0)
      IssueSourceValB(i) := UInt(0)
      IssueSourceValBTag(i) := UInt(0)
      
      Decode_Opcode(i) := UInt(0)
      Decode_Func3(i) := UInt(0)
      Decode_Imm(i) := UInt(0)
      DecodeType(i) := UInt(0)

      ValidEntry(i) := UInt(0)
    }

      Full := UInt(0)
      rowPlace := SInt(-1)
  }  
  
  
  // Update the entry in the reservation station based on FU broadcast for FU0
  when((io.FUBroadcastValid0===UInt(1))){
      
      for(j<-0 until 16){
        when((io.FUBroadcastTag0 === IssueSourceValATag(j))&&(IssueSourceValAValid(j)===UInt(0))&&(ValidEntry(j)===UInt(1))){
            IssueSourceValA(j):=io.FUBroadcastValue0
            IssueSourceValAValid(j):=UInt(1)
            //printf("\nupdated with FU0 values A")
        }
        when((io.FUBroadcastTag0 === IssueSourceValBTag(j))&&(IssueSourceValBValid(j)===UInt(0))&&(ValidEntry(j)===UInt(1))){
            IssueSourceValB(j):=io.FUBroadcastValue0       
            IssueSourceValBValid(j):=UInt(1)
            //printf("\nupdated with FU0 values B")

        }

     }
  }
    
  // Update the entry in the reservation station based on FU broadcast for FU1
  when((io.FUBroadcastValid1===UInt(1))){
      
      for(j<-0 until 16){
        when((io.FUBroadcastTag1 === IssueSourceValATag(j))&& (IssueSourceValAValid(j)===UInt(0))&& (ValidEntry(j)===UInt(1))){
            IssueSourceValA(j):=io.FUBroadcastValue1
            IssueSourceValAValid(j):=UInt(1)
            //printf("\nupdated with FU1 values A")
        }
        when((io.FUBroadcastTag1 === IssueSourceValBTag(j))&&(IssueSourceValBValid(j)===UInt(0))&&(ValidEntry(j)===UInt(1))){
            IssueSourceValB(j):=io.FUBroadcastValue1       
            IssueSourceValBValid(j):=UInt(1)
            //printf("\nupdated with FU1 values B")

        }

      }
  }
 
  // Update the entry in the reservation station based on FU broadcast for FU2
  when((io.FUBroadcastValid2===UInt(1))){
      
      for(j<-0 until 16){
        when((io.FUBroadcastTag2 === IssueSourceValATag(j))&&(IssueSourceValAValid(j)===UInt(0))&&(ValidEntry(j)===UInt(1))){
            IssueSourceValA(j):=io.FUBroadcastValue2
            IssueSourceValAValid(j):=UInt(1)
            //printf("\nupdated with FU2 values A")
        }
        when((io.FUBroadcastTag2 === IssueSourceValBTag(j))&&(IssueSourceValBValid(j)===UInt(0))&&(ValidEntry(j)===UInt(1))){
            IssueSourceValB(j):=io.FUBroadcastValue2       
            IssueSourceValBValid(j):=UInt(1)
            //printf("\nupdated with FU2 values B")

        }

     }
  }
    
  // Update the entry in the reservation station based on FU broadcast for FU3
  when((io.FUBroadcastValid3===UInt(1))){
      
      for(j<-0 until 16){
        when((io.FUBroadcastTag3 === IssueSourceValATag(j))&&(IssueSourceValAValid(j)===UInt(0))&&(ValidEntry(j)===UInt(1))){
            IssueSourceValA(j):=io.FUBroadcastValue3
            IssueSourceValAValid(j):=UInt(1)
            //printf("\nupdated with FU3 values A")
        }
        when((io.FUBroadcastTag3 === IssueSourceValBTag(j))&&(IssueSourceValBValid(j)===UInt(0))&&(ValidEntry(j)===UInt(1))){
            IssueSourceValB(j):=io.FUBroadcastValue3       
            IssueSourceValBValid(j):=UInt(1)
            //printf("\nupdated with FU3 values B")

        }

    }
  }
    
    
    
  // Update the entry in the reservatio station based on L/S broadcast
    when((io.LoadStoreDestValid===UInt(1))){
      
      for(j<-0 until 16){
        when((io.LoadStoreDestTag === IssueSourceValATag(j))&&(IssueSourceValAValid(j)===UInt(0))&&(ValidEntry(j)===UInt(1))){
            IssueSourceValA(j):=io.LoadStoreDestVal
            IssueSourceValAValid(j):=UInt(1)
            //printf("\nupdated with l/s values A")
        }
        when((io.LoadStoreDestTag === IssueSourceValBTag(j))&&(IssueSourceValBValid(j)===UInt(0))&&(ValidEntry(j)===UInt(1))){
            IssueSourceValB(j):=io.LoadStoreDestVal       
            IssueSourceValBValid(j):=UInt(1)
            //printf("\nupdated with l/s values B")

        }
        } 

  }
  when((io.RenameValid===UInt(1))){
    
      when(io.Full===UInt(1)){
        //printf("\nThis entry was not stored in the reservation station as the reservation station is full: DestTag=%d ROB=%d RowNumber=%d",io.RenameDestTag,io.DecodeROB,io.RenameRowSelect)
      }
      .otherwise{
        val rownumber = io.RenameRowSelect
        ValidEntry(rownumber) := UInt(1)

        DestTag(rownumber) := io.RenameDestTag
        IssueInstructionROB := io.DecodeROB

        IssueSourceValAValid(rownumber) := io.RenameSourceAValueValid
        IssueSourceValA(rownumber) := io.RenameSourceAValue
        IssueSourceValATag(rownumber) := io.RenameSourceATag

        IssueSourceValBValid(rownumber) := io.RenameSourceBValueValid
        IssueSourceValB(rownumber) := io.RenameSourceBValue
        IssueSourceValBTag(rownumber) := io.RenameSourceBTag

        Decode_Opcode(rownumber) := io.Decode_Opcode
        Decode_Func3(rownumber) := io.Decode_Func3
        Decode_Func7(rownumber) := io.Decode_Func7
        Decode_Imm(rownumber) := io.Decode_Imm
        DecodeType(rownumber) := io.DecodeType
      }



  }

  io.Full := ValidEntry(0) & ValidEntry(1) & ValidEntry(2) & ValidEntry(3) & ValidEntry(4) & ValidEntry(5) & ValidEntry(6) & ValidEntry(7) & ValidEntry(8) & ValidEntry(9) & ValidEntry(10) & ValidEntry(11) & ValidEntry(12) & ValidEntry(13) & ValidEntry(14) & ValidEntry(15)
  

  for(b<-15 to 0 by -1){
    when((ValidEntry(b)===UInt(0) && (io.Full===UInt(0)))) {
        io.FreeRow := UInt(b) 
    
    }
  }


  // check if all the entries in the reservation station if it is full and push the valid sources to output
  for(k<-15 to 0 by -1){
    when((ValidEntry(k)===UInt(1)) && (IssueSourceValAValid(k)===UInt(1)) && (IssueSourceValBValid(k)===UInt(1))&&(io.FUisBusy===UInt(0))){
      io.IssueSourceValA := IssueSourceValA(k)
      io.IssueSourceValB := IssueSourceValB(k)
      io.IssueFUOpcode := Decode_Opcode(k)
      io.IssuedestTag := DestTag(k)
      io.Valid := UInt(1) 

      io.Issue_Func3 := Decode_Func3(k)
      io.Issue_Func7 := Decode_Func7(k)
      io.Issue_Imm := Decode_Imm(k)
      io.Issue_Type:= DecodeType(k)
      rowPlace:=SInt(k)
      //printf("\nrowPlace:%d pushed out of the reservation station",rowPlace)

      when(rowPlace===SInt(15)){

          
          IssueSourceValA(15):= UInt(0)
          IssueSourceValATag(15):=UInt(0)
          IssueSourceValB(15):= UInt(0)
          IssueSourceValBTag(15):= UInt(0)
          Decode_Opcode(15):= UInt(0)
          DestTag(15):= UInt(0)
          Decode_Func3(15):= UInt(0)
          Decode_Func7(15):= UInt(0)
          Decode_Imm(15):= UInt(0)
          DecodeType(15):= UInt(0)
          ValidEntry(15):= UInt(0)
          IssueSourceValAValid(15):= UInt(0)
          IssueSourceValBValid(15):= UInt(0)
          IssueInstructionROB(15):= UInt(0)
      }
      .otherwise{
      
        for(t<-k until 15){

          IssueSourceValA(t):= IssueSourceValA(t+1)
          IssueSourceValATag(t):= IssueSourceValATag(t+1)
          IssueSourceValB(t):= IssueSourceValB(t+1)
          IssueSourceValBTag(t):= IssueSourceValBTag(t+1)
          Decode_Opcode(t):= Decode_Opcode(t+1)
          DestTag(t):= DestTag(t+1)
          Decode_Func3(t):= Decode_Func3(t+1)
          Decode_Func7(t):= Decode_Func7(t+1)
          Decode_Imm(t):= Decode_Imm(t+1)
          DecodeType(t):= DecodeType(t+1)
          ValidEntry(t):= ValidEntry(t+1)
          IssueSourceValAValid(t):= IssueSourceValAValid(t+1)
          IssueSourceValBValid(t):= IssueSourceValBValid(t+1)

          IssueInstructionROB(t):= IssueInstructionROB(t+1)
        }

          IssueSourceValA(15):= UInt(0)
          IssueSourceValATag(15):=UInt(0)
          IssueSourceValB(15):= UInt(0)
          IssueSourceValBTag(15):= UInt(0)
          Decode_Opcode(15):= UInt(0)
          DestTag(15):= UInt(0)
          Decode_Func3(15):= UInt(0)
          Decode_Func7(15):= UInt(0)
          Decode_Imm(15):= UInt(0)
          DecodeType(15):= UInt(0)
          ValidEntry(15):= UInt(0)
          IssueSourceValAValid(15):= UInt(0)
          IssueSourceValBValid(15):= UInt(0)

          IssueInstructionROB(15):= UInt(0)
      
      }

    }// when((ValidEntry(k)===UInt(1)) && (IssueSourceValAValid(k)===UInt(1)) && (IssueSourceValBValid(k)===UInt(1)))



  }//for(k<-15 to 0 by -1)



}//Module

class ReservationStationTester(r:ReservationStationModule) extends Tester(r) {
// Test 1 : fill all the 16 rows of the reservation station with some value and on each time step see the output , check the output till 20 cycles

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
      
        for(t<-0 until 32){
          poke(r.io.RenameValid,1)
          poke(r.io.RenameRowSelect,t)

          poke(r.io.RenameSourceAValue,t)
          poke(r.io.RenameSourceAValueValid,0)
          poke(r.io.RenameSourceATag,t)

          poke(r.io.RenameSourceBValue,t)
          poke(r.io.RenameSourceBValueValid,0)
          poke(r.io.RenameSourceBTag,t)
          poke(r.io.RenameDestTag,t)
          poke(r.io.Decode_Opcode,rand)
          poke(r.io.Decode_Func3,rand)
          poke(r.io.Decode_Func7,rand)
          poke(r.io.Decode_Imm,rand)
          poke(r.io.DecodeROB,rand)
          poke(r.io.DecodeType,rand)

          peek(r.io.IssueSourceValA) 
          peek(r.io.IssueSourceValB) 
        
          peek(r.io.IssueFUOpcode) 
          peek(r.io.Issue_Func3) 
          peek(r.io.Issue_Func7) 
          peek(r.io.Issue_Imm) 
          peek(r.io.Issue_Type) 
        
          peek(r.io.IssuedestTag) 
          peek(r.io.Full) 
          peek(r.io.FreeRow) 
          peek(r.io.Valid) 
          step(1)
        

        }
        for(m<-0 until 20) {
          peek(r.io.IssueSourceValA) 
          peek(r.io.IssueSourceValB) 
        
          peek(r.io.IssueFUOpcode) 
          peek(r.io.Issue_Func3) 
          peek(r.io.Issue_Func7) 
          peek(r.io.Issue_Imm) 
          peek(r.io.Issue_Type) 
        
          peek(r.io.IssuedestTag) 
          peek(r.io.Full) 
          peek(r.io.FreeRow) 
          peek(r.io.Valid) 
          step(1)
        }
        poke(r.io.FUBroadcastValid0,1)
        poke(r.io.FUBroadcastTag0,0)
        poke(r.io.FUBroadcastValue0,52)
        
        poke(r.io.FUBroadcastValid1,1)
        poke(r.io.FUBroadcastTag1,5)
        poke(r.io.FUBroadcastValue1,57)
        
        poke(r.io.FUBroadcastValid2,1)
        poke(r.io.FUBroadcastTag2,8)
        poke(r.io.FUBroadcastValue2,32)
        
        poke(r.io.FUBroadcastValid3,1)
        poke(r.io.FUBroadcastTag3,14)
        poke(r.io.FUBroadcastValue3,93)
        step(1)
        peek(r.io.IssueSourceValA) 
        peek(r.io.IssueSourceValB) 
        
        peek(r.io.IssueFUOpcode) 
        peek(r.io.Issue_Func3) 
        peek(r.io.Issue_Func7) 
        peek(r.io.Issue_Imm) 
        peek(r.io.Issue_Type) 
        
        peek(r.io.IssuedestTag) 
        peek(r.io.Full) 
        peek(r.io.FreeRow) 
        peek(r.io.Valid) 

        step(1)
        peek(r.io.IssueSourceValA) 
        peek(r.io.IssueSourceValB) 
        
        peek(r.io.IssueFUOpcode) 
        peek(r.io.Issue_Func3) 
        peek(r.io.Issue_Func7) 
        peek(r.io.Issue_Imm) 
        peek(r.io.Issue_Type) 
        
        peek(r.io.IssuedestTag) 
        peek(r.io.Full) 
        peek(r.io.FreeRow) 
        peek(r.io.Valid) 

        step(1)
        peek(r.io.IssueSourceValA) 
        peek(r.io.IssueSourceValB) 
        
        peek(r.io.IssueFUOpcode) 
        peek(r.io.Issue_Func3) 
        peek(r.io.Issue_Func7) 
        peek(r.io.Issue_Imm) 
        peek(r.io.Issue_Type) 
        
        peek(r.io.IssuedestTag) 
        peek(r.io.Full) 
        peek(r.io.FreeRow) 
        peek(r.io.Valid) 

        step(1)
        peek(r.io.IssueSourceValA) 
        peek(r.io.IssueSourceValB) 
        
        peek(r.io.IssueFUOpcode) 
        peek(r.io.Issue_Func3) 
        peek(r.io.Issue_Func7) 
        peek(r.io.Issue_Imm) 
        peek(r.io.Issue_Type) 
        
        peek(r.io.IssuedestTag) 
        peek(r.io.Full) 
        peek(r.io.FreeRow) 
        peek(r.io.Valid) 

        step(1)
        peek(r.io.IssueSourceValA) 
        peek(r.io.IssueSourceValB) 
        
        peek(r.io.IssueFUOpcode) 
        peek(r.io.Issue_Func3) 
        peek(r.io.Issue_Func7) 
        peek(r.io.Issue_Imm) 
        peek(r.io.Issue_Type) 
        
        peek(r.io.IssuedestTag) 
        peek(r.io.Full) 
        peek(r.io.FreeRow) 
        peek(r.io.Valid) 


        step(1)
        peek(r.io.IssueSourceValA) 
        peek(r.io.IssueSourceValB) 
        
        peek(r.io.IssueFUOpcode) 
        peek(r.io.Issue_Func3) 
        peek(r.io.Issue_Func7) 
        peek(r.io.Issue_Imm) 
        peek(r.io.Issue_Type) 
        
        peek(r.io.IssuedestTag) 
        peek(r.io.Full) 
        peek(r.io.FreeRow) 
        peek(r.io.Valid) 

        poke(r.io.LoadStoreDestValid,1)
        poke(r.io.LoadStoreDestTag,14)
        poke(r.io.LoadStoreDestVal,54)
        

        step(1)
        peek(r.io.IssueSourceValA) 
        peek(r.io.IssueSourceValB) 
        
        peek(r.io.IssueFUOpcode) 
        peek(r.io.Issue_Func3) 
        peek(r.io.Issue_Func7) 
        peek(r.io.Issue_Imm) 
        peek(r.io.Issue_Type) 
        
        peek(r.io.IssuedestTag) 
        peek(r.io.Full) 
        peek(r.io.FreeRow) 
        peek(r.io.Valid) 

}

class ReservationStationTestGenerator extends TestGenerator {
  def genMod(): Module = Module(new ReservationStationModule())
  def genTest[T <: Module](r: T): Tester[T] = 
    (new ReservationStationTester(r.asInstanceOf[ReservationStationModule])).asInstanceOf[Tester[T]]
}

