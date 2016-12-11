package risc5

import Chisel._


class IssueQueueModule extends Module {

  val io = new Bundle {
    // All INPUT ports ( all the ports in the doc for the issue queue needs to be updated to 64 bits , which is not the case in the doc
    val RenameRowSelect0 = UInt(INPUT,6)  
    val RenameRowSelect1 = UInt(INPUT,6)  
    val RenameRowSelect2 = UInt(INPUT,6)  
    val RenameRowSelect3 = UInt(INPUT,6)  

    //val RenameValid = UInt(INPUT,1)
    // Reservation Station 0

    //val RenameRowSelect_0 = UInt(INPUT,4)

    val RenameValid_0 = UInt(INPUT,1)


    val RenameSourceAValue_0 = UInt(INPUT,64)
    val RenameSourceAValueValid_0 = UInt(INPUT,1)
    val RenameSourceATag_0 = UInt(INPUT,7)

    val RenameSourceBValue_0 = UInt(INPUT,64)
    val RenameSourceBValueValid_0 = UInt(INPUT,1)
    val RenameSourceBTag_0 = UInt(INPUT,8)

    val RenameDestTag_0 = UInt(INPUT,10)

    val LoadStoreDestVal_0 = UInt(INPUT,64)
    val LoadStoreDestTag_0 = UInt(INPUT,10)
    val LoadStoreDestValid_0 = UInt(INPUT,1)

    val Decode_Opcode_0 = UInt(INPUT,7)
    val Decode_Func3_0 = UInt(INPUT,3)
    val Decode_Func7_0 = UInt(INPUT,7)
    val Decode_Imm_0 = UInt(INPUT,20)
    val DecodeROB_0 = UInt(INPUT,7)
    val DecodeType_0 = UInt(INPUT,3)
        
    val FUBroadcastValue0_0 = UInt(INPUT,64)
    val FUBroadcastTag0_0 = UInt(INPUT,7)
    val FUBroadcastValid0_0 = UInt(INPUT,1)

    val FUBroadcastValue1_0 = UInt(INPUT,64)
    val FUBroadcastTag1_0 = UInt(INPUT,7)
    val FUBroadcastValid1_0 = UInt(INPUT,1)

    val FUBroadcastValue2_0 = UInt(INPUT,64)
    val FUBroadcastTag2_0 = UInt(INPUT,7)
    val FUBroadcastValid2_0 = UInt(INPUT,1)

    val FUBroadcastValue3_0 = UInt(INPUT,64)
    val FUBroadcastTag3_0 = UInt(INPUT,7)
    val FUBroadcastValid3_0 = UInt(INPUT,1)
    val FUisBusy_0 = UInt(INPUT,1)
    val RenamePC0 = UInt(INPUT,64)
  
    // Reservation Station 1

    //val RenameRowSelect_1 = UInt(INPUT,4)

    val RenameValid_1 = UInt(INPUT,1)


    val RenameSourceAValue_1 = UInt(INPUT,64)
    val RenameSourceAValueValid_1 = UInt(INPUT,1)
    val RenameSourceATag_1 = UInt(INPUT,7)

    val RenameSourceBValue_1 = UInt(INPUT,64)
    val RenameSourceBValueValid_1 = UInt(INPUT,1)
    val RenameSourceBTag_1 = UInt(INPUT,8)

    val RenameDestTag_1 = UInt(INPUT,10)

    val LoadStoreDestVal_1 = UInt(INPUT,64)
    val LoadStoreDestTag_1 = UInt(INPUT,10)
    val LoadStoreDestValid_1 = UInt(INPUT,1)

    val Decode_Opcode_1 = UInt(INPUT,7)
    val Decode_Func3_1 = UInt(INPUT,3)
    val Decode_Func7_1 = UInt(INPUT,7)
    val Decode_Imm_1 = UInt(INPUT,20)
    val DecodeROB_1 = UInt(INPUT,7)
    val DecodeType_1 = UInt(INPUT,3)
        
    val FUBroadcastValue0_1 = UInt(INPUT,64)
    val FUBroadcastTag0_1 = UInt(INPUT,7)
    val FUBroadcastValid0_1 = UInt(INPUT,1)

    val FUBroadcastValue1_1 = UInt(INPUT,64)
    val FUBroadcastTag1_1 = UInt(INPUT,7)
    val FUBroadcastValid1_1 = UInt(INPUT,1)

    val FUBroadcastValue2_1 = UInt(INPUT,64)
    val FUBroadcastTag2_1 = UInt(INPUT,7)
    val FUBroadcastValid2_1 = UInt(INPUT,1)

    val FUBroadcastValue3_1 = UInt(INPUT,64)
    val FUBroadcastTag3_1 = UInt(INPUT,7)
    val FUBroadcastValid3_1 = UInt(INPUT,1)
    val FUisBusy_1 = UInt(INPUT,1)
    val RenamePC1 = UInt(INPUT,64)

    // Reservation Station 2

    //val RenameRowSelect_2 = UInt(INPUT,4)

    val RenameValid_2 = UInt(INPUT,1)


    val RenameSourceAValue_2 = UInt(INPUT,64)
    val RenameSourceAValueValid_2 = UInt(INPUT,1)
    val RenameSourceATag_2 = UInt(INPUT,7)

    val RenameSourceBValue_2 = UInt(INPUT,64)
    val RenameSourceBValueValid_2 = UInt(INPUT,1)
    val RenameSourceBTag_2 = UInt(INPUT,8)

    val RenameDestTag_2 = UInt(INPUT,10)

    val LoadStoreDestVal_2 = UInt(INPUT,64)
    val LoadStoreDestTag_2 = UInt(INPUT,10)
    val LoadStoreDestValid_2 = UInt(INPUT,1)

    val Decode_Opcode_2 = UInt(INPUT,7)
    val Decode_Func3_2 = UInt(INPUT,3)
    val Decode_Func7_2 = UInt(INPUT,7)
    val Decode_Imm_2 = UInt(INPUT,20)
    val DecodeROB_2 = UInt(INPUT,7)
    val DecodeType_2 = UInt(INPUT,3)
        
    val FUBroadcastValue0_2 = UInt(INPUT,64)
    val FUBroadcastTag0_2 = UInt(INPUT,7)
    val FUBroadcastValid0_2 = UInt(INPUT,1)

    val FUBroadcastValue1_2 = UInt(INPUT,64)
    val FUBroadcastTag1_2 = UInt(INPUT,7)
    val FUBroadcastValid1_2 = UInt(INPUT,1)

    val FUBroadcastValue2_2 = UInt(INPUT,64)
    val FUBroadcastTag2_2 = UInt(INPUT,7)
    val FUBroadcastValid2_2 = UInt(INPUT,1)

    val FUBroadcastValue3_2 = UInt(INPUT,64)
    val FUBroadcastTag3_2 = UInt(INPUT,7)
    val FUBroadcastValid3_2 = UInt(INPUT,1)
    val FUisBusy_2 = UInt(INPUT,1)
    val RenamePC2 = UInt(INPUT,64)

    // Reservation Station 3

    //val RenameRowSelect_3 = UInt(INPUT,4)

    val RenameValid_3 = UInt(INPUT,1)


    val RenameSourceAValue_3 = UInt(INPUT,64)
    val RenameSourceAValueValid_3 = UInt(INPUT,1)
    val RenameSourceATag_3 = UInt(INPUT,7)

    val RenameSourceBValue_3 = UInt(INPUT,64)
    val RenameSourceBValueValid_3 = UInt(INPUT,1)
    val RenameSourceBTag_3 = UInt(INPUT,8)

    val RenameDestTag_3 = UInt(INPUT,10)

    val LoadStoreDestVal_3 = UInt(INPUT,64)
    val LoadStoreDestTag_3 = UInt(INPUT,10)
    val LoadStoreDestValid_3 = UInt(INPUT,1)

    val Decode_Opcode_3 = UInt(INPUT,7)
    val Decode_Func3_3 = UInt(INPUT,3)
    val Decode_Func7_3 = UInt(INPUT,7)
    val Decode_Imm_3 = UInt(INPUT,20)
    val DecodeROB_3 = UInt(INPUT,7)
    val DecodeType_3 = UInt(INPUT,3)
        
    val FUBroadcastValue0_3 = UInt(INPUT,64)
    val FUBroadcastTag0_3 = UInt(INPUT,7)
    val FUBroadcastValid0_3 = UInt(INPUT,1)

    val FUBroadcastValue1_3 = UInt(INPUT,64)
    val FUBroadcastTag1_3 = UInt(INPUT,7)
    val FUBroadcastValid1_3 = UInt(INPUT,1)

    val FUBroadcastValue2_3 = UInt(INPUT,64)
    val FUBroadcastTag2_3 = UInt(INPUT,7)
    val FUBroadcastValid2_3 = UInt(INPUT,1)

    val FUBroadcastValue3_3 = UInt(INPUT,64)
    val FUBroadcastTag3_3 = UInt(INPUT,7)
    val FUBroadcastValid3_3 = UInt(INPUT,1)
    val FUisBusy_3 = UInt(INPUT,1)
    val RenamePC3 = UInt(INPUT,64)


    // All OUTPUT ports

    val IssueQueueFull = UInt(OUTPUT,1)

    // Reservation Station 0
    val IssueSourceValA_0 = UInt(OUTPUT,64)
    val IssueSourceValB_0 = UInt(OUTPUT,64)
    
    val IssueFUOpcode_0 = UInt(OUTPUT,7)
    val Issue_Func3_0 = UInt(OUTPUT,3)
    val Issue_Func7_0 = UInt(OUTPUT,7)
    val Issue_Imm_0 = UInt(OUTPUT,20)
    val Issue_Type_0 = UInt(OUTPUT,3)
    
    val IssuedestTag_0 = UInt(OUTPUT,10)
    val Full_0 = UInt(OUTPUT,1)
    val Valid_0 = UInt(OUTPUT,1)
    val IssuePC0 = UInt(OUTPUT,64)
  
    val IssueBroadCastFreeRow_0 = UInt(OUTPUT,4)

    // Reservation Station 1
    val IssueSourceValA_1 = UInt(OUTPUT,64)
    val IssueSourceValB_1 = UInt(OUTPUT,64)
    
    val IssueFUOpcode_1 = UInt(OUTPUT,7)
    val Issue_Func3_1 = UInt(OUTPUT,3)
    val Issue_Func7_1 = UInt(OUTPUT,7)
    val Issue_Imm_1 = UInt(OUTPUT,20)
    val Issue_Type_1 = UInt(OUTPUT,3)
    
    val IssuedestTag_1 = UInt(OUTPUT,10)
    val Full_1 = UInt(OUTPUT,1)
    val Valid_1 = UInt(OUTPUT,1)
    val IssuePC1 = UInt(OUTPUT,64)
    val IssueBroadCastFreeRow_1 = UInt(OUTPUT,4)
  
    // Reservation Station 2
    val IssueSourceValA_2 = UInt(OUTPUT,64)
    val IssueSourceValB_2 = UInt(OUTPUT,64)
    
    val IssueFUOpcode_2 = UInt(OUTPUT,7)
    val Issue_Func3_2 = UInt(OUTPUT,3)
    val Issue_Func7_2 = UInt(OUTPUT,7)
    val Issue_Imm_2 = UInt(OUTPUT,20)
    val Issue_Type_2 = UInt(OUTPUT,3)
    
    val IssuedestTag_2 = UInt(OUTPUT,10)
    val Full_2 = UInt(OUTPUT,1)
    val Valid_2 = UInt(OUTPUT,1)
    val IssuePC2 = UInt(OUTPUT,64)
    val IssueBroadCastFreeRow_2 = UInt(OUTPUT,4)
  
    // Reservation Station 3
    val IssueSourceValA_3 = UInt(OUTPUT,64)
    val IssueSourceValB_3 = UInt(OUTPUT,64)
    
    val IssueFUOpcode_3 = UInt(OUTPUT,7)
    val Issue_Func3_3 = UInt(OUTPUT,3)
    val Issue_Func7_3 = UInt(OUTPUT,7)
    val Issue_Imm_3 = UInt(OUTPUT,20)
    val Issue_Type_3 = UInt(OUTPUT,3)
    
    val IssuedestTag_3 = UInt(OUTPUT,10)
    val Full_3 = UInt(OUTPUT,1)
    val Valid_3 = UInt(OUTPUT,1)
    val IssuePC3 = UInt(OUTPUT,64)
    val IssueBroadCastFreeRow_3 = UInt(OUTPUT,4)
  
  }
    val rS_0 = Module(new ReservationStationModule())
    
    //rS_0.io.RenameRowSelect := io.RenameRowSelect_0
    rS_0.io.RenameValid := io.RenameValid_0
    

    rS_0.io.RenameSourceAValue := io.RenameSourceAValue_0
    rS_0.io.RenameSourceAValueValid := io.RenameSourceAValueValid_0
    rS_0.io.RenameSourceATag := io.RenameSourceATag_0

    rS_0.io.RenameSourceBValue := io.RenameSourceBValue_0
    rS_0.io.RenameSourceBValueValid := io.RenameSourceBValueValid_0
    rS_0.io.RenameSourceBTag := io.RenameSourceBTag_0

    rS_0.io.RenameDestTag := io.RenameDestTag_0

    rS_0.io.LoadStoreDestVal := io.LoadStoreDestVal_0
    rS_0.io.LoadStoreDestTag := io.LoadStoreDestTag_0
    rS_0.io.LoadStoreDestValid := io.LoadStoreDestValid_0

    rS_0.io.Decode_Opcode := io.Decode_Opcode_0
    rS_0.io.Decode_Func3 := io.Decode_Func3_0
    rS_0.io.Decode_Func7 := io.Decode_Func7_0
    rS_0.io.Decode_Imm := io.Decode_Imm_0
    rS_0.io.DecodeROB := io.DecodeROB_0
    rS_0.io.DecodeType := io.DecodeType_0
    
    rS_0.io.FUBroadcastValue0 := io.FUBroadcastValue0_0
    rS_0.io.FUBroadcastTag0 := io.FUBroadcastTag0_0
    rS_0.io.FUBroadcastValid0 := io.FUBroadcastValid0_0

    rS_0.io.FUBroadcastValue1 := io.FUBroadcastValue1_0
    rS_0.io.FUBroadcastTag1 := io.FUBroadcastTag1_0
    rS_0.io.FUBroadcastValid1 := io.FUBroadcastValid1_0

    rS_0.io.FUBroadcastValue2 := io.FUBroadcastValue2_0
    rS_0.io.FUBroadcastTag2 := io.FUBroadcastTag2_0
    rS_0.io.FUBroadcastValid2 := io.FUBroadcastValid2_0

    rS_0.io.FUBroadcastValue3 := io.FUBroadcastValue3_0
    rS_0.io.FUBroadcastTag3 := io.FUBroadcastTag3_0
    rS_0.io.FUBroadcastValid3 := io.FUBroadcastValue3_0
    rS_0.io.FUisBusy := io.FUisBusy_0
    rS_0.io.RenamePC := io.RenamePC0


    io.IssueSourceValA_0 := rS_0.io.IssueSourceValA
    io.IssueSourceValB_0 := rS_0.io.IssueSourceValB
    
    io.IssueFUOpcode_0 := rS_0.io.IssueFUOpcode
    io.Issue_Func3_0 := rS_0.io.Issue_Func3
    io.Issue_Func7_0 := rS_0.io.Issue_Func7
    io.Issue_Imm_0 := rS_0.io.Issue_Imm
    io.Issue_Type_0 := rS_0.io.Issue_Type
    
    io.IssuedestTag_0 := rS_0.io.IssuedestTag
    io.Valid_0 := rS_0.io.Valid
    io.IssueBroadCastFreeRow_0 := rS_0.io.FreeRow
    io.Full_0 := rS_0.io.Full


    val rS_1 = Module(new ReservationStationModule())
    
    //rS_1.io.RenameRowSelect := io.RenameRowSelect_1
    rS_1.io.RenameValid := io.RenameValid_1
    

    rS_1.io.RenameSourceAValue := io.RenameSourceAValue_1
    rS_1.io.RenameSourceAValueValid := io.RenameSourceAValueValid_1
    rS_1.io.RenameSourceATag := io.RenameSourceATag_1

    rS_1.io.RenameSourceBValue := io.RenameSourceBValue_1
    rS_1.io.RenameSourceBValueValid := io.RenameSourceBValueValid_1
    rS_1.io.RenameSourceBTag := io.RenameSourceBTag_1

    rS_1.io.RenameDestTag := io.RenameDestTag_1

    rS_1.io.LoadStoreDestVal := io.LoadStoreDestVal_1
    rS_1.io.LoadStoreDestTag := io.LoadStoreDestTag_1
    rS_1.io.LoadStoreDestValid := io.LoadStoreDestValid_1

    rS_1.io.Decode_Opcode := io.Decode_Opcode_1
    rS_1.io.Decode_Func3 := io.Decode_Func3_1
    rS_1.io.Decode_Func7 := io.Decode_Func7_1
    rS_1.io.Decode_Imm := io.Decode_Imm_1
    rS_1.io.DecodeROB := io.DecodeROB_1
    rS_1.io.DecodeType := io.DecodeType_1
    
    rS_1.io.FUBroadcastValue0 := io.FUBroadcastValue0_1
    rS_1.io.FUBroadcastTag0 := io.FUBroadcastTag0_1
    rS_1.io.FUBroadcastValid0 := io.FUBroadcastValid0_1

    rS_1.io.FUBroadcastValue1 := io.FUBroadcastValue1_1
    rS_1.io.FUBroadcastTag1 := io.FUBroadcastTag1_1
    rS_1.io.FUBroadcastValid1 := io.FUBroadcastValid1_1

    rS_1.io.FUBroadcastValue2 := io.FUBroadcastValue2_1
    rS_1.io.FUBroadcastTag2 := io.FUBroadcastTag2_1
    rS_1.io.FUBroadcastValid2 := io.FUBroadcastValid2_1

    rS_1.io.FUBroadcastValue3 := io.FUBroadcastValue3_1
    rS_1.io.FUBroadcastTag3 := io.FUBroadcastTag3_1
    rS_1.io.FUBroadcastValid3 := io.FUBroadcastValue3_1
    rS_1.io.FUisBusy := io.FUisBusy_1
    rS_1.io.RenamePC := io.RenamePC1


    io.IssueSourceValA_1 := rS_1.io.IssueSourceValA
    io.IssueSourceValB_1 := rS_1.io.IssueSourceValB
    
    io.IssueFUOpcode_1 := rS_1.io.IssueFUOpcode
    io.Issue_Func3_1 := rS_1.io.Issue_Func3
    io.Issue_Func7_1 := rS_1.io.Issue_Func7
    io.Issue_Imm_1 := rS_1.io.Issue_Imm
    io.Issue_Type_1 := rS_1.io.Issue_Type
    
    io.IssuedestTag_1 := rS_1.io.IssuedestTag
    io.Valid_1 := rS_1.io.Valid
    io.IssueBroadCastFreeRow_1 := rS_1.io.FreeRow
    io.Full_1 := rS_1.io.Full

    val rS_2 = Module(new ReservationStationModule())
    
    //rS_2.io.RenameRowSelect := io.RenameRowSelect_2
    rS_2.io.RenameValid := io.RenameValid_2
    

    rS_2.io.RenameSourceAValue := io.RenameSourceAValue_2
    rS_2.io.RenameSourceAValueValid := io.RenameSourceAValueValid_2
    rS_2.io.RenameSourceATag := io.RenameSourceATag_2

    rS_2.io.RenameSourceBValue := io.RenameSourceBValue_2
    rS_2.io.RenameSourceBValueValid := io.RenameSourceBValueValid_2
    rS_2.io.RenameSourceBTag := io.RenameSourceBTag_2

    rS_2.io.RenameDestTag := io.RenameDestTag_2

    rS_2.io.LoadStoreDestVal := io.LoadStoreDestVal_2
    rS_2.io.LoadStoreDestTag := io.LoadStoreDestTag_2
    rS_2.io.LoadStoreDestValid := io.LoadStoreDestValid_2

    rS_2.io.Decode_Opcode := io.Decode_Opcode_2
    rS_2.io.Decode_Func3 := io.Decode_Func3_2
    rS_2.io.Decode_Func7 := io.Decode_Func7_2
    rS_2.io.Decode_Imm := io.Decode_Imm_2
    rS_2.io.DecodeROB := io.DecodeROB_2
    rS_2.io.DecodeType := io.DecodeType_2
    
    rS_2.io.FUBroadcastValue0 := io.FUBroadcastValue0_2
    rS_2.io.FUBroadcastTag0 := io.FUBroadcastTag0_2
    rS_2.io.FUBroadcastValid0 := io.FUBroadcastValid0_2

    rS_2.io.FUBroadcastValue1 := io.FUBroadcastValue1_2
    rS_2.io.FUBroadcastTag1 := io.FUBroadcastTag1_2
    rS_2.io.FUBroadcastValid1 := io.FUBroadcastValid1_2

    rS_2.io.FUBroadcastValue2 := io.FUBroadcastValue2_2
    rS_2.io.FUBroadcastTag2 := io.FUBroadcastTag2_2
    rS_2.io.FUBroadcastValid2 := io.FUBroadcastValid2_2

    rS_2.io.FUBroadcastValue3 := io.FUBroadcastValue3_2
    rS_2.io.FUBroadcastTag3 := io.FUBroadcastTag3_2
    rS_2.io.FUBroadcastValid3 := io.FUBroadcastValue3_2
    rS_2.io.FUisBusy := io.FUisBusy_2
    rS_2.io.RenamePC := io.RenamePC2


    io.IssueSourceValA_2 := rS_2.io.IssueSourceValA
    io.IssueSourceValB_2 := rS_2.io.IssueSourceValB
    
    io.IssueFUOpcode_2 := rS_2.io.IssueFUOpcode
    io.Issue_Func3_2 := rS_2.io.Issue_Func3
    io.Issue_Func7_2 := rS_2.io.Issue_Func7
    io.Issue_Imm_2 := rS_2.io.Issue_Imm
    io.Issue_Type_2 := rS_2.io.Issue_Type
    
    io.IssuedestTag_2 := rS_2.io.IssuedestTag
    io.Valid_2 := rS_2.io.Valid
    io.IssueBroadCastFreeRow_2 := rS_2.io.FreeRow
    io.Full_2 := rS_2.io.Full

    val rS_3 = Module(new ReservationStationModule())
    
    //rS_3.io.RenameRowSelect := io.RenameRowSelect_3
    rS_3.io.RenameValid := io.RenameValid_3
    

    rS_3.io.RenameSourceAValue := io.RenameSourceAValue_3
    rS_3.io.RenameSourceAValueValid := io.RenameSourceAValueValid_3
    rS_3.io.RenameSourceATag := io.RenameSourceATag_3

    rS_3.io.RenameSourceBValue := io.RenameSourceBValue_3
    rS_3.io.RenameSourceBValueValid := io.RenameSourceBValueValid_3
    rS_3.io.RenameSourceBTag := io.RenameSourceBTag_3

    rS_3.io.RenameDestTag := io.RenameDestTag_3

    rS_3.io.LoadStoreDestVal := io.LoadStoreDestVal_3
    rS_3.io.LoadStoreDestTag := io.LoadStoreDestTag_3
    rS_3.io.LoadStoreDestValid := io.LoadStoreDestValid_3

    rS_3.io.Decode_Opcode := io.Decode_Opcode_3
    rS_3.io.Decode_Func3 := io.Decode_Func3_3
    rS_3.io.Decode_Func7 := io.Decode_Func7_3
    rS_3.io.Decode_Imm := io.Decode_Imm_3
    rS_3.io.DecodeROB := io.DecodeROB_3
    rS_3.io.DecodeType := io.DecodeType_3
    
    rS_3.io.FUBroadcastValue0 := io.FUBroadcastValue0_3
    rS_3.io.FUBroadcastTag0 := io.FUBroadcastTag0_3
    rS_3.io.FUBroadcastValid0 := io.FUBroadcastValid0_3

    rS_3.io.FUBroadcastValue1 := io.FUBroadcastValue1_3
    rS_3.io.FUBroadcastTag1 := io.FUBroadcastTag1_3
    rS_3.io.FUBroadcastValid1 := io.FUBroadcastValid1_3

    rS_3.io.FUBroadcastValue2 := io.FUBroadcastValue2_3
    rS_3.io.FUBroadcastTag2 := io.FUBroadcastTag2_3
    rS_3.io.FUBroadcastValid2 := io.FUBroadcastValid2_3

    rS_3.io.FUBroadcastValue3 := io.FUBroadcastValue3_3
    rS_3.io.FUBroadcastTag3 := io.FUBroadcastTag3_3
    rS_3.io.FUBroadcastValid3 := io.FUBroadcastValue3_3
    rS_3.io.FUisBusy := io.FUisBusy_3
    rS_3.io.RenamePC := io.RenamePC3


    io.IssueSourceValA_3 := rS_3.io.IssueSourceValA
    io.IssueSourceValB_3 := rS_3.io.IssueSourceValB
    
    io.IssueFUOpcode_3 := rS_3.io.IssueFUOpcode
    io.Issue_Func3_3 := rS_3.io.Issue_Func3
    io.Issue_Func7_3 := rS_3.io.Issue_Func7
    io.Issue_Imm_3 := rS_3.io.Issue_Imm
    io.Issue_Type_3 := rS_3.io.Issue_Type
    
    io.IssuedestTag_3 := rS_3.io.IssuedestTag
    io.Valid_3 := rS_3.io.Valid
    io.IssueBroadCastFreeRow_3 := rS_3.io.FreeRow
    io.Full_3 := rS_3.io.Full

    io.IssueQueueFull := rS_0.io.Full | rS_1.io.Full | rS_2.io.Full | rS_3.io.Full
    
    rS_0.io.RenameRowSelect := UInt(0)
    rS_1.io.RenameRowSelect := UInt(0)
    rS_2.io.RenameRowSelect := UInt(0)
    rS_3.io.RenameRowSelect := UInt(0)

    //rS_0.io.RenameValid := UInt(0)
    //rS_1.io.RenameValid := UInt(0)
    //rS_2.io.RenameValid := UInt(0)
    //rS_3.io.RenameValid := UInt(0)

    when(io.RenameRowSelect0(5,4)===UInt(0)){
      rS_0.io.RenameRowSelect := io.RenameRowSelect0(3,0)
      //rS_0.io.RenameValid := io.RenameValid
    }
    when(io.RenameRowSelect0(5,4)===UInt(1)){
      rS_1.io.RenameRowSelect := io.RenameRowSelect0(3,0)
      //rS_1.io.RenameValid := io.RenameValid
    }
    when(io.RenameRowSelect0(5,4)===UInt(2)){
      rS_2.io.RenameRowSelect := io.RenameRowSelect0(3,0)
      //rS_2.io.RenameValid := io.RenameValid
    }
    when(io.RenameRowSelect0(5,4)===UInt(3)){
      rS_3.io.RenameRowSelect := io.RenameRowSelect0(3,0)
      //rS_3.io.RenameValid := io.RenameValid
    }

    when(io.RenameRowSelect1(5,4)===UInt(0)){
      rS_0.io.RenameRowSelect := io.RenameRowSelect1(3,0)
      //rS_0.io.RenameValid := io.RenameValid
    }
    when(io.RenameRowSelect1(5,4)===UInt(1)){
      rS_1.io.RenameRowSelect := io.RenameRowSelect1(3,0)
      //rS_1.io.RenameValid := io.RenameValid
    }
    when(io.RenameRowSelect1(5,4)===UInt(2)){
      rS_2.io.RenameRowSelect := io.RenameRowSelect1(3,0)
      //rS_2.io.RenameValid := io.RenameValid
    }
    when(io.RenameRowSelect1(5,4)===UInt(3)){
      rS_3.io.RenameRowSelect := io.RenameRowSelect1(3,0)
      //rS_3.io.RenameValid := io.RenameValid
    }

    when(io.RenameRowSelect2(5,4)===UInt(0)){
      rS_0.io.RenameRowSelect := io.RenameRowSelect2(3,0)
      //rS_0.io.RenameValid := io.RenameValid
    }
    when(io.RenameRowSelect2(5,4)===UInt(1)){
      rS_1.io.RenameRowSelect := io.RenameRowSelect2(3,0)
      //rS_1.io.RenameValid := io.RenameValid
    }
    when(io.RenameRowSelect2(5,4)===UInt(2)){
      rS_2.io.RenameRowSelect := io.RenameRowSelect2(3,0)
      //rS_2.io.RenameValid := io.RenameValid
    }
    when(io.RenameRowSelect2(5,4)===UInt(3)){
      rS_3.io.RenameRowSelect := io.RenameRowSelect2(3,0)
      //rS_3.io.RenameValid := io.RenameValid
    }

    when(io.RenameRowSelect3(5,4)===UInt(0)){
      rS_0.io.RenameRowSelect := io.RenameRowSelect3(3,0)
      //rS_0.io.RenameValid := io.RenameValid
    }
    when(io.RenameRowSelect3(5,4)===UInt(1)){
      rS_1.io.RenameRowSelect := io.RenameRowSelect3(3,0)
      //rS_1.io.RenameValid := io.RenameValid
    }
    when(io.RenameRowSelect3(5,4)===UInt(2)){
      rS_2.io.RenameRowSelect := io.RenameRowSelect3(3,0)
      //rS_2.io.RenameValid := io.RenameValid
    }
    when(io.RenameRowSelect3(5,4)===UInt(3)){
      rS_3.io.RenameRowSelect := io.RenameRowSelect3(3,0)
      //rS_3.io.RenameValid := io.RenameValid
    }
}

class IssueQueueTester(i:IssueQueueModule) extends Tester(i) {
  for(m<-0 until 64){
    poke(i.io.RenameRowSelect0,m)
    poke(i.io.RenameValid_0,1)
    step(1)
  }

}

class IssueQueueTestGenerator extends TestGenerator {
  def genMod(): Module = Module(new IssueQueueModule())
  def genTest[T <: Module](i: T): Tester[T] = 
    (new IssueQueueTester(i.asInstanceOf[IssueQueueModule])).asInstanceOf[Tester[T]]
}
