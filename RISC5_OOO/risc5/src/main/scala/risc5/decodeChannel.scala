//
    //TYPE   |                     RV32I/64I Base Instruction Set             |  OPCODE | NAME
  //  
    //   U   | imm[31:12]                        rd                       | 0110111 | LUI
    //   U   | imm[31:12]                        rd                       | 0010111 | AUIPC
    //   UJ  | imm[20|10:1|11|19:12]                 rd                   | 1101111 | JAL
    //   I   | imm[11:0]                 rs1    000       rd              | 1100111 | JALR
    //   SB  | imm[12|10:5]           rs2       rs1    000   imm[4:1|11]  | 1100011 | BEQ
    //   SB  | imm[12|10:5]           rs2       rs1    001   imm[4:1|11]  | 1100011 | BNE
    //   SB  | imm[12|10:5]           rs2       rs1    100   imm[4:1|11]  | 1100011 | BLT
    //   SB  | imm[12|10:5]           rs2       rs1    101   imm[4:1|11]  | 1100011 | BGE
    //   SB  | imm[12|10:5]           rs2       rs1    110   imm[4:1|11]  | 1100011 | BLTU
    //   SB  | imm[12|10:5]           rs2       rs1    111   imm[4:1|11]  | 1100011 | BGEU
    //   I   | imm[11:0]                 rs1    000       rd              | 0000011 | LB
    //   I   | imm[11:0]                 rs1    001       rd              | 0000011 | LH
    //   I   | imm[11:0]                 rs1    010       rd              | 0000011 | LW
    //   I   | imm[11:0]                 rs1    100       rd              | 0000011 | LBU
    //   I   | imm[11:0]                 rs1    101       rd              | 0000011 | LHU
    //   S   | imm[11:5]         rs2           rs1    000    imm[4:0]     | 0100011 | SB
    //   S   | imm[11:5]         rs2           rs1    001    imm[4:0]     | 0100011 | SH
    //   S   | imm[11:5]         rs2           rs1    010    imm[4:0]     | 0100011 | SW
    //   I   | imm[11:0]                 rs1    000       rd              | 0010011 | ADDI
    //   I   | imm[11:0]                 rs1    010       rd              | 0010011 | SLTI
    //   I   | imm[11:0]                 rs1    011       rd              | 0010011 | SLTIU
    //   I   | imm[11:0]                 rs1    100       rd              | 0010011 | XORI
    //   I   | imm[11:0]                 rs1    110       rd              | 0010011 | ORI
    //   I   | imm[11:0]                 rs1    111       rd              | 0010011 | ANDI
    //   I   | 0000000         shamt          rs1    001       rd         | 0010011 | SLLI 
    //   I   | 0000000         shamt          rs1    101       rd         | 0010011 | SRLI
    //   I   | 0100000         shamt          rs1    101       rd         | 0010011 | SRAI
    //   R   | 0000000          rs2           rs1    000       rd         | 0110011 | ADD
    //   R   | 0100000          rs2           rs1    000       rd         | 0110011 | SUB
    //   R   | 0000000          rs2           rs1    001       rd         | 0110011 | SLL
    //   R   | 0000000          rs2           rs1    010       rd         | 0110011 | SLT
    //   R   | 0000000          rs2           rs1    011       rd         | 0110011 | SLTU
    //   R   | 0000000          rs2           rs1    100       rd         | 0110011 | XOR
    //   R   | 0000000          rs2           rs1    101       rd         | 0110011 | SRL
    //   R   | 0100000          rs2           rs1    101       rd         | 0110011 | SRA
    //   R   | 0000000          rs2           rs1    110       rd         | 0110011 | OR
    //   R   | 0000000          rs2           rs1    111       rd         | 0110011 | AND
    //       | 0000        pred     succ      00000    000      00000     | 0001111 | FENCE
    //       | 0000        0000     0000      00000    001      00000     | 0001111 | FENCE.I
    //       | 000000000000              00000    000      00000          | 1110011 | ECALL
    //       | 000000000001              00000    000      00000          | 1110011 | EBREAK
    //   I   |     csr                    rs1    001       rd             | 1110011 | CSRRW
    //   I   |     csr                    rs1    010       rd             | 1110011 | CSRRS
    //   I   |     csr                    rs1    011       rd             | 1110011 | CSRRC
    //   I   |     csr                   zimm    101       rd             | 1110011 | CSRRWI
    //   I   |     csr                   zimm    110       rd             | 1110011 | CSRRSI
    //   I   |     csr                   zimm    111       rd             | 1110011 | CSRRCI
    //   I    |  imm[11:0]               rs1      110        rd           | 0000011 |    LWU
    //   I    |  imm[11:0]               rs1      011        rd           | 0000011 |    LD
    //   S    |  imm[11:5]          rs2         rs1      011     imm[4:0] | 0100011 |    SD
    //   I    |  000000          shamt          rs1      001        rd    | 0010011 |    SLLI
    //   I    |  000000          shamt          rs1      101        rd    | 0010011 |    SRLI
    //   I    |  010000          shamt          rs1      101        rd    | 0010011 |    SRAI
    //   I    |  imm[11:0]               rs1      000        rd           | 0011011 |    ADDIW
    //   I    |  0000000          shamt        rs1      001        rd     | 0011011 |    SLLIW
    //   I    |  0000000          shamt        rs1      101        rd     | 0011011 |    SRLIW
    //   I    |  0100000          shamt        rs1      101        rd     | 0011011 |    SRAIW
    //   R    |  0000000           rs2         rs1      000        rd     | 0111011 |    ADDW
    //   R    |  0100000           rs2         rs1      000        rd     | 0111011 |    SUBW
    //   R    |  0000000           rs2         rs1      001        rd     | 0111011 |    SLLW
    //   R    |  0000000           rs2         rs1      101        rd     | 0111011 |    SRLW
    //   R    |  0100000           rs2         rs1      101        rd     | 0111011 |    SRAW
    //                                                                      |         |
    //         |                     RV32M Standard Extension             |         |
    //   R     |    0000001           rs2         rs1      000        rd  | 0110011 |    MUL
    //   R     |    0000001           rs2         rs1      001        rd  | 0110011 |    MULH
    //   R     |    0000001           rs2         rs1      010        rd  | 0110011 |    MULHSU
    //   R     |    0000001           rs2         rs1      011        rd  | 0110011 |    MULHU
    //   R     |    0000001           rs2         rs1      100        rd  | 0110011 |    DIV
    //   R     |    0000001           rs2         rs1      101        rd  | 0110011 |    DIVU
    //   R     |    0000001           rs2         rs1      110        rd  | 0110011 |    REM
    //   R     |    0000001           rs2         rs1      111        rd  | 0110011 |    REMU
    //         |                                                          |         |
    //         |                                                          |         |
    //         |         RV64M Standard Extension (in addition to RV32M)  |         |
    //   R     |    0000001           rs2         rs1      000        rd  | 0111011 |    MULW
    //   R     |    0000001           rs2         rs1      100        rd  | 0111011 |    DIVW
    //   R     |    0000001           rs2         rs1      101        rd  | 0111011 |    DIVUW
    //   R     |    0000001           rs2         rs1      110        rd  | 0111011 |    REMW
    //   R     |    0000001           rs2         rs1      111        rd  | 0111011 |    REMUW

    
    
    
    
    
    
    
    //Opcode    = instruction [6:0]
    //Rd        = instruction[11:7]
    //funct3    = instruction[14:12]
    //Rs1       = instruction[19:15]
    //Rs2       = instruction[24:20]
    //funct7    = instruction[31:25]
    
package risc5

import Chisel._


//This is one Decode Channel. There should be four total in the top level
class DecodeChannel extends Module {



    val io = new Bundle {
    
        //Inputs
        val fetchInstruction = UInt(INPUT, 32)
        val fetchAddress = UInt(INPUT, 64)
        val fetchRobTag  = UInt(INPUT, 7)
        val fetchBranchTaken = UInt(INPUT, 1)
	val fetchValid = UInt(INPUT, 1)
	val lSFull = UInt(INPUT, 1)
	val issueFull = UInt(INPUT, 1)       	

 
        //Outputs
        val decodeBranchTaken = UInt(OUTPUT, 1)
        val decodeValid = UInt(OUTPUT, 1)
        val decodeOpcode = UInt(OUTPUT, 7)
        val decodeType = UInt(OUTPUT, 3)
        val decodeRd = UInt(OUTPUT, 5)
        val decodeFunky3 = UInt(OUTPUT, 3)
        val decodeRs1 = UInt(OUTPUT, 5)
        val decodeRs2 = UInt(OUTPUT, 5)
        val decodeFunky7 = UInt(OUTPUT, 7)
        //val decodeImm_I_S = UInt(OUTPUT, 12) //no longer in use, all immediates go to 20 bit output
        val decode_Imm = UInt(OUTPUT, 20)
        val decodeRobTag = UInt(OUTPUT, 8)
        val decodeAddress = UInt(OUTPUT, 64)
	val decodeQueueSelect = UInt(OUTPUT, 1)	
	val decodeIsStore = UInt(OUTPUT, 1)
	//the following 6 bits are used to indicate which riscV instruction format the decoded instruction is
	}    

/////////////////////////////////////////////////////////
//		Branch Taken Register
/////////////////////////////////////////////////////////
    
     val decodeBranchTaken = Reg(next = io.fetchBranchTaken) //branchTaken reg
     io.decodeBranchTaken := decodeBranchTaken

////////////////////////////////////////////////////////
//		Valid Register
////////////////////////////////////////////////////////
    val valid = Reg(init = UInt(0))

    when( (io.lSFull === UInt(1)) || (io.issueFull === UInt(1))){
	valid := UInt(0)
	}
    .otherwise{
	when(io.fetchValid === UInt(1)){
	valid := UInt(1)
	}
	.otherwise{
	valid := UInt(0)
	}
    }

     io.decodeValid := valid
/////////////////////////////////////////////////////////
//		ADDRESS/ROB TAG REGISTERS
/////////////////////////////////////////////////////////
    val decodeAddress = Reg(next = io.fetchAddress) //Address reg
    val decodeRobTag  = Reg(next = io.fetchRobTag) //RobTag Reg

    io.decodeAddress := decodeAddress
    io.decodeRobTag  := decodeRobTag

/////////////////////////////////////////////////////////
//             INSTRUCTIONI/MAIN DECODE REGISTER
/////////////////////////////////////////////////////////  
  val instruction = Reg(next = io.fetchInstruction) //Instruction Reg
  val fetchOpcode = io.fetchInstruction(6,0)

    io.decodeOpcode  := instruction(6,0)
    io.decodeRd      := instruction(11,7)
    io.decodeFunky3  := instruction(14,12)
    io.decodeRs1     := instruction (19,15)
    io.decodeRs2     := instruction(24,20)
    io.decodeFunky7  := instruction(31,25)


//////////////////////////////////////////////////////////
//		QUEUE SELECT REGISTERS
//////////////////////////////////////////////////////////
    val LSQ = Reg(UInt())
    val store = Reg(UInt())

	when(fetchOpcode === UInt(0x03)){
		LSQ := UInt(1)
		store := UInt(0)
	}
	.elsewhen(fetchOpcode === UInt(0x23)){
		LSQ := UInt(1)
		store := UInt(1)
        }
	.otherwise{
		LSQ := UInt(0)
		store := UInt(0)
	}
	io.decodeQueueSelect := LSQ
	io.decodeIsStore := store



/////////////////////////////////////////////////////////////////	
//		TYPE REGISTERS and IMM REGISTERS
/////////////////////////////////////////////////////////////////

	val iType = Reg(UInt())
	val immediate = Reg(UInt())

	when((fetchOpcode === UInt("h3B"))||(fetchOpcode === UInt("h33"))){
		iType := UInt(0x0) //R-Type
		immediate := UInt("h0")
	}
        .elsewhen((fetchOpcode === UInt(0x13))||(fetchOpcode === UInt(0x1B))||(fetchOpcode === UInt(0x67))){
		iType := UInt(0x1) //I-Type
		immediate := io.fetchInstruction(31,20)
	}
	//S-Type
	.elsewhen(fetchOpcode === UInt(0x23)){
		iType := UInt(0x2) //S-Type
		immediate := Cat(io.fetchInstruction(31,25), io.fetchInstruction(11,7))
	}
	.elsewhen(fetchOpcode === UInt(0x63)){
		iType := UInt(0x3) //SB-type
		//immediate := Cat(instruction(31), instruction(7), instruction(30,25), instruction(11,8))
		immediate := Cat(io.fetchInstruction(31,25), io.fetchInstruction(11,7))
	}
	.elsewhen((fetchOpcode === UInt(0x37))||(fetchOpcode === UInt(0x17))){
		iType := UInt(0x4) //U-Type
		immediate := io.fetchInstruction(31,12)
	}
	.elsewhen(fetchOpcode === UInt(0x6F)){
		iType := UInt(0x5) //UJ-type
		//immediate := Cat(instruction(31), instruction(19, 12), io.instruction(20), instruction(30,21))
		immediate := io.fetchInstruction(31,12)

	}
	.otherwise{
		iType := UInt(0x7) //garbage-type
		immediate := UInt(0)
	}

	io.decodeType := iType
	io.decode_Imm := immediate(19,0)












    
}


class DecodeTester(d:DecodeChannel) extends Tester(d) {

//There are 6 tests in this class, one for each instruction format

/////////////I-Type Test///////////////////


    //poke values
    val IsampleInstruction =  0xCCC80093    //Integer.parseInt("1100 1100 1100 1000 0000 0000 1001 0011",2)
    val IsampleAddress =      0x0000000000000000 //Integer.parseInt("0000000000000000000000000000000000000000000000000000000000000000",2)
    val IsampleRobTag =       0x2C //Integer.parseInt("101100",2)

    //expected values
    val IexpectedOpcode  = 0x13 //Integer.parseInt("0010011",2)
    val IexpectedRd      = Integer.parseInt("00001",2)
    val IexpectedFunky3  = Integer.parseInt("000",2)
    val IexpectedRs1     = Integer.parseInt("10000",2)
    val IexpectedRs2     = Integer.parseInt("01100",2)
    val IexpectedFunky7  = Integer.parseInt("1100110",2)
    //val IexpectedImm_I_S = Integer.parseInt("110011001100",2)
    val Iexpected_Imm   = 0x00CCC //Integer.parseInt("0000 0000 1100 1100 1100",2)
    

        poke(d.io.fetchInstruction, IsampleInstruction)
        poke(d.io.fetchAddress, IsampleAddress)
        poke(d.io.fetchRobTag, IsampleRobTag) 
        poke(d.io.fetchValid, 0x1)
	poke(d.io.lSFull, 0x0)
	poke(d.io.issueFull, 0x0)

        step(1)
        
       // poke(d.io.fetchAddress, 0x5)

        expect(d.io.decodeOpcode,  IexpectedOpcode)
        expect(d.io.decodeRd,      IexpectedRd)
        expect(d.io.decodeFunky3,  IexpectedFunky3)
        expect(d.io.decodeRs1,     IexpectedRs1)
        expect(d.io.decodeRs2,     IexpectedRs2)
        expect(d.io.decodeFunky7,  IexpectedFunky7)
//        expect(d.io.decode_Imm, IexpectedImm_I_S)
        expect(d.io.decode_Imm,   Iexpected_Imm)
        expect(d.io.decodeRobTag,  IsampleRobTag)
        expect(d.io.decodeAddress, IsampleAddress)
        expect(d.io.decodeType,  1)
//       expect(d.io.isI,  1)
//        expect(d.io.isS,  0)
//        expect(d.io.isSB, 0)
//        expect(d.io.isU,  0)
//        expect(d.io.isUJ, 0)
	expect(d.io.decodeValid, 1)     


/////////////R-Type Test///////////////////


    //poke values
   val RsampleInstruction = 0x00528093//Integer.parseInt("0000 0000 1010 0101 1000 1010 1011 0011",2)
   val RsampleAddress     = 0x0000000000000000 //Integer.parseInt("0000000000000000000000000000000000000000000000000000000000000001",2)
    val RsampleRobTag      = 0x2D //Integer.parseInt("101101",2)

    //expected values
    val RexpectedOpcode  = Integer.parseInt("0010011",2)
    val RexpectedRd      = Integer.parseInt("00001",2)
    val RexpectedFunky3  = Integer.parseInt("000",2)
    val RexpectedRs1     = Integer.parseInt("00101",2)
    val RexpectedRs2     = Integer.parseInt("00000",2)
    val RexpectedFunky7  = Integer.parseInt("0000000",2)
    val Rexpected_Imm    = 0x5 //Integer.parseInt("0000 0000 0000 0000 0000",2)


        poke(d.io.fetchInstruction, RsampleInstruction)
        poke(d.io.fetchAddress, RsampleAddress)
        poke(d.io.fetchRobTag, RsampleRobTag) 
	poke(d.io.fetchValid, 0)

        step(1)
        expect(d.io.decodeOpcode,  RexpectedOpcode)
        expect(d.io.decodeRd,      RexpectedRd)
        expect(d.io.decodeFunky3,  RexpectedFunky3)
        expect(d.io.decodeRs1,     RexpectedRs1)
        //expect(d.io.decodeRs2,     RexpectedRs2)
        expect(d.io.decodeFunky7,  RexpectedFunky7)
//        expect(d.io.decode_Imm, RexpectedImm_I_S)
        expect(d.io.decode_Imm,   Rexpected_Imm)
        expect(d.io.decodeRobTag,  RsampleRobTag)
        expect(d.io.decodeAddress, RsampleAddress)
        expect(d.io.decodeType,  0)
//        expect(d.io.isI,  0)
//        expect(d.io.isS,  0)
//        expect(d.io.isSB, 0)
//        expect(d.io.isU,  0)
//        expect(d.io.isUJ, 0)
	expect(d.io.decodeQueueSelect, 0)
	expect(d.io.decodeValid, 0)
///////////////valid test///////////////////////
	poke(d.io.fetchValid, 1)

        step(1)
        expect(d.io.decodeOpcode,  RexpectedOpcode)
        expect(d.io.decodeRd,      RexpectedRd)
        expect(d.io.decodeFunky3,  RexpectedFunky3)
        expect(d.io.decodeRs1,     RexpectedRs1)
        expect(d.io.decodeRs2,     RexpectedRs2)
        expect(d.io.decodeFunky7,  RexpectedFunky7)
//        expect(d.io.decode_Imm, RexpectedImm_I_S)
        expect(d.io.decode_Imm,   Rexpected_Imm)
        expect(d.io.decodeRobTag,  RsampleRobTag)
        expect(d.io.decodeAddress, RsampleAddress)
        expect(d.io.decodeType,  0)
//        expect(d.io.isI,  0)
//        expect(d.io.isS,  0)
//        expect(d.io.isSB, 0)
//        expect(d.io.isU,  0)
//        expect(d.io.isUJ, 0)
	expect(d.io.decodeQueueSelect, 0)
	expect(d.io.decodeValid, 1)



/////////////S-Type Test///////////////////


    //poke values
    val SsampleInstruction = 0xABF79523 //Integer.parseInt("1010 1011 1111 0111 100 101010 0100011",2)
    val SsampleAddress     = 0x0000000000000000 //Integer.parseInt("0000000000000000000000000000000000000000000000000000000000000000",2)
    val SsampleRobTag      = 0x2C //Integer.parseInt("101100",2)

    //expected values
    val SexpectedOpcode  = Integer.parseInt("0100011",2)
    val SexpectedRd      = Integer.parseInt("01010",2)
    val SexpectedFunky3  = Integer.parseInt("001",2)
    val SexpectedRs1     = Integer.parseInt("01111",2)
    val SexpectedRs2     = 0x1F //Integer.parseInt("11111",2)
    val SexpectedFunky7  = 0x55 //Integer.parseInt("1010101",2)
    val Sexpected_Imm = 0x00AAA //Integer.parseInt("1010 1010 1010,2)
    //val SexpectedImm_U   = 0xABF79 //Integer.parseInt("1010 1011 1111 0111 1001",2)


        poke(d.io.fetchInstruction, SsampleInstruction)
        poke(d.io.fetchAddress, SsampleAddress)
        poke(d.io.fetchRobTag, SsampleRobTag) 

        step(1)
        expect(d.io.decodeOpcode,  SexpectedOpcode)
        expect(d.io.decodeRd,      SexpectedRd)
        expect(d.io.decodeFunky3,  SexpectedFunky3)
        expect(d.io.decodeRs1,     SexpectedRs1)
        expect(d.io.decodeRs2,     SexpectedRs2)
        expect(d.io.decodeFunky7,  SexpectedFunky7)
      //  expect(d.io.decode_Imm, SexpectedImm_I_S)
        expect(d.io.decode_Imm,   Sexpected_Imm)
        expect(d.io.decodeRobTag,  SsampleRobTag)
        expect(d.io.decodeAddress, SsampleAddress)
        expect(d.io.decodeType,  2)
//        expect(d.io.isI,  0)
//        expect(d.io.isS,  1)
//        expect(d.io.isSB, 0)
//        expect(d.io.isU,  0)
//        expect(d.io.isUJ, 0)
        expect(d.io.decodeQueueSelect, 1)

/////////////SB-Type Test///////////////////


    //poke values
    val SBsampleInstruction = 0xFE1141E3 //Integer.parseInt("1111 111_0 0001 _0001 0_100 _0001 1_110 0011")
    val SBsampleAddress = 0x0000000000100000 //Integer.parseInt("0000000000000000000000000000000000000000000000000000000000000000")
    val SBsampleRobTag = 0x2C //Integer.parseInt("101100")

    //expected values
    val SBexpectedOpcode  = 0x63 //Integer.parseInt("1100011")
    val SBexpectedRd      = 0x03 // Integer.parseInt("00011")
    val SBexpectedFunky3  = 0x4  //Integer.parseInt("100")
    val SBexpectedRs1     = 0x02 //Integer.parseInt("00010")
    val SBexpectedRs2     = 0x01 //Integer.parseInt("00001")
    val SBexpectedFunky7  = 0x7F //Integer.parseInt("1111111")
    val SBexpected_Imm   = 0x00FE3 //Integer.parseInt("1 111111 0000100010100")


        poke(d.io.fetchInstruction, SBsampleInstruction)
        poke(d.io.fetchAddress, SBsampleAddress)
        poke(d.io.fetchRobTag, SBsampleRobTag) 

        step(1)
        expect(d.io.decodeOpcode,  SBexpectedOpcode)
        expect(d.io.decodeRd,      SBexpectedRd)
        expect(d.io.decodeFunky3,  SBexpectedFunky3)
        expect(d.io.decodeRs1,     SBexpectedRs1)
        expect(d.io.decodeRs2,     SBexpectedRs2)
        expect(d.io.decodeFunky7,  SBexpectedFunky7)
        expect(d.io.decode_Imm,   SBexpected_Imm)
        expect(d.io.decodeRobTag,  SBsampleRobTag)
        expect(d.io.decodeAddress, SBsampleAddress)
        expect(d.io.decodeType,  3)
//        expect(d.io.isI,  0)
//        expect(d.io.isS,  0)
//        expect(d.io.isSB, 1)
//        expect(d.io.isU,  0)
//        expect(d.io.isUJ, 0)


/////////////U-Type Test///////////////////


    //poke values
    val UsampleInstruction = 0xFFFFF0B7 //Integer.parseInt("1111 1111 1111 1111 1111 _0000 1_011 0111")
    val UsampleAddress = 0x0000000000000002
    val UsampleRobTag = 0x69 //Integer.parseInt("101100")

    //expected values
    val UexpectedOpcode  = 0x37//Integer.parseInt("0110111")
    val UexpectedRd      = 0x01 //Integer.parseInt("00001")
    val UexpectedFunky3  = 0x7 //Integer.parseInt("111")
    val UexpectedRs1     = 0x1F //Integer.parseInt("11111")
    val UexpectedRs2     = 0x1F //Integer.parseInt("11111")
    val UexpectedFunky7  = 0x7F //Integer.parseInt("1111111")
//    val UexpectedImm_I_S = 0xFFF //Integer.parseInt("111111111111")
    val Uexpected_Imm   = 0xFFFFF //Integer.parseInt("11111111111111111111")


        poke(d.io.fetchInstruction, UsampleInstruction)
        poke(d.io.fetchAddress, UsampleAddress)
        poke(d.io.fetchRobTag, UsampleRobTag) 

        step(1)
        expect(d.io.decodeOpcode,  UexpectedOpcode)
        expect(d.io.decodeRd,      UexpectedRd)
        expect(d.io.decodeFunky3,  UexpectedFunky3)
        expect(d.io.decodeRs1,     UexpectedRs1)
        expect(d.io.decodeRs2,     UexpectedRs2)
        expect(d.io.decodeFunky7,  UexpectedFunky7)
//        expect(d.io.decode_Imm, UexpectedImm_I_S)
        expect(d.io.decode_Imm,   Uexpected_Imm)
        expect(d.io.decodeRobTag,  UsampleRobTag)
        expect(d.io.decodeAddress, UsampleAddress)
        expect(d.io.decodeType,  4)
//        expect(d.io.isI,  0)
//        expect(d.io.isS,  0)
//        expect(d.io.isSB, 0)
//        expect(d.io.isU,  1)
//        expect(d.io.isUJ, 0)


/////////////UJ-Type Test///////////////////


    //poke values
    val UJsampleInstruction = 0x801000EF //Integer.parseInt("1 000 0000 0001  00000000  _0000 1_110 1111")
    val UJsampleAddress = 0x0000000000000004
    val UJsampleRobTag = 0x72

    //expected values
    val UJexpectedOpcode  = 0x6F //Integer.parseInt("1101111")
    val UJexpectedRd      = 0x01 //Integer.parseInt("00001")
    val UJexpectedFunky3  = 0x0 //Integer.parseInt("000")
    val UJexpected_Imm   = 0x80100 //Integer.parseInt("10000000000100000000")


        poke(d.io.fetchInstruction, UJsampleInstruction)
        poke(d.io.fetchAddress, UJsampleAddress)
        poke(d.io.fetchRobTag, UJsampleRobTag) 

        step(1)
        expect(d.io.decodeOpcode,  UJexpectedOpcode)
        expect(d.io.decodeRd,      UJexpectedRd)
        expect(d.io.decodeFunky3,  UJexpectedFunky3)
        expect(d.io.decode_Imm,   UJexpected_Imm)
        expect(d.io.decodeRobTag,  UJsampleRobTag)
        expect(d.io.decodeAddress, UJsampleAddress)
        expect(d.io.decodeType,  5)
 //       expect(d.io.isI,  0)
 //       expect(d.io.isS,  0)
 //       expect(d.io.isSB, 0)
 //       expect(d.io.isU,  0)
 //       expect(d.io.isUJ, 1)


}


//Test Generator
class DecodeModuleGenerator extends TestGenerator {
  def genMod(): Module = Module(new DecodeChannel())
  def genTest[T <: Module](d: T): Tester[T] = 
    (new DecodeTester(d.asInstanceOf[DecodeChannel])).asInstanceOf[Tester[T]]
}
