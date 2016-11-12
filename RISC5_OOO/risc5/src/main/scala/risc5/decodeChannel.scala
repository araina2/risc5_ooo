//
    //TYPE   |                     RV32I Base Instruction Set             |  OPCODE | NAME
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
        val fetchRobTag  = UInt(INPUT, 6)
        
        //Outputs
        val decodeOpcode = UInt(OUTPUT, 7)
        val decodeType = UInt(OUTPUT, 3)
        val decodeRd = UInt(OUTPUT, 5)
        val decodeFunky3 = UInt(OUTPUT, 3)
        val decodeRs1 = UInt(OUTPUT, 5)
        val decodeRs2 = UInt(OUTPUT, 5)
        val decodeFunky7 = UInt(OUTPUT, 7)
        val decodeImm_I_S = UInt(OUTPUT, 12)
        val decodeImm_U = UInt(OUTPUT, 20)
        val decodeRobTag = UInt(OUTPUT, 6)
        val decodeAddress = UInt(OUTPUT, 64)
	
	//the following 6 bits are used to indicate which riscV instruction format the decoded instruction is
	//only one of these will be high at any given time
        val isR = UInt(OUTPUT, 1)
        val isI = UInt(OUTPUT, 1)
        val isS = UInt(OUTPUT, 1)
        val isSB = UInt(OUTPUT, 1)
        val isU = UInt(OUTPUT, 1)
        val isUJ = UInt(OUTPUT, 1)
    }
    
    
    io.decodeAddress := io.fetchAddress
    io.decodeOpcode  := io.fetchInstruction(6,0)
    io.decodeRd      := io.fetchInstruction(11,7)
    io.decodeFunky3  := io.fetchInstruction(14,12)
    io.decodeRs1     := io.fetchInstruction (19,15)
    io.decodeRs2     := io.fetchInstruction(24,20)
    io.decodeFunky7  := io.fetchInstruction(31,25)
    
    //type detection logic//

    io.isR  := (io.decodeOpcode === UInt("b0111011")) || (io.decodeOpcode === UInt("b0110011"))
    io.isI  := (io.decodeOpcode === UInt("b0000011")) || (io.decodeOpcode === UInt("b0011011")) || (io.decodeOpcode === UInt("b1100111"))
    io.isS  := (io.decodeOpcode === UInt("b0100011"))
    io.isSB := (io.decodeOpcode === UInt("b1100011"))
    io.isU  := (io.decodeOpcode === UInt("b0110111")) || (io.decodeOpcode === UInt("b0010111"))
    io.isUJ := (io.decodeOpcode === UInt("b1101111"))


    //Immediate value selection logic//    
    when(io.isS === UInt(1)){
    io.decodeImm_I_S := Cat(io.fetchInstruction(31,25), io.fetchInstruction(11,7)) 
    }
    .elsewhen (io.isSB === UInt(1)){
    io.decodeImm_I_S := Cat(io.fetchInstruction(31), io.fetchInstruction(30,25), io.fetchInstruction(11,8), io.fetchInstruction(11,7))
    }
    .otherwise{
    io.decodeImm_I_S := io.fetchInstruction(31,20) 
    }
    
   
    when(io.isUJ === UInt(1)){
       io.decodeImm_U := Cat(io.fetchInstruction(31),  io.fetchInstruction(30, 21), io.fetchInstruction(20), io.fetchInstruction(19,12))
    }
    .otherwise{
        io.decodeImm_U := io.fetchInstruction(31,12)
    }
    
    io.decodeRobTag := io.fetchRobTag    
    
    
}

class DecodeTester(d:DecodeChannel) extends Tester(d) {

//There are 6 tests in this class, one for each instruction format

/////////////I-Type Test///////////////////


    //poke values
    val IsampleInstruction = BigInt("b110011001100_10000_000_00001_0010011")
    val IsampleAddress = BigInt("b0000000000000000000000000000000000000000000000000000000000000000")
    val IsampleRobTag = BigInt("b101100")

    //expected values
    val IexpectedOpcode  = BigInt("b0010011")
    val IexpectedRd      = BigInt("b00001")
    val IexpectedFunky3  = BigInt("b000")
    val IexpectedRs1     = BigInt("b10000")
    val IexpectedRs2     = BigInt("b01100")
    val IexpectedFunky7  = BigInt("b1100110")
    val IexpectedImm_I_S = BigInt("b110011001100")
    val IexpectedImm_U   = BigInt("b11001100110010000000")


        poke(d.io.fetchInstruction, IsampleInstruction)
        poke(d.io.fetchAddress, IsampleAddress)
        poke(d.io.fetchAddress, IsampleRobTag) 

        step(1)
        expect(d.io.decodeOpcode,  IexpectedOpcode)
        expect(d.io.decodeRd,      IexpectedRd)
        expect(d.io.decodeFunky3,  IexpectedFunky3)
        expect(d.io.decodeRs1,     IexpectedRs1)
        expect(d.io.decodeRs2,     IexpectedRs2)
        expect(d.io.decodeFunky7,  IexpectedFunky7)
        expect(d.io.decodeImm_I_S, IexpectedImm_I_S)
        expect(d.io.decodeImm_U,   IexpectedImm_U)
        expect(d.io.decodeRobTag,  IsampleRobTag)
        expect(d.io.decodeAddress, IsampleAddress)
        expect(d.io.isR,  0)
        expect(d.io.isI,  1)
        expect(d.io.isS,  0)
        expect(d.io.isSB, 0)
        expect(d.io.isU,  0)
        expect(d.io.isUJ, 0)
        

/////////////R-Type Test///////////////////


    //poke values
    val RsampleInstruction = BigInt("b0000000_01010_01011_000_10101_0110011")
    val RsampleAddress = BigInt("b0000000000000000000000000000000000000000000000000000000000000001")
    val RsampleRobTag = BigInt("b101101")

    //expected values
    val RexpectedOpcode  = BigInt("b0110011")
    val RexpectedRd      = BigInt("b10101")
    val RexpectedFunky3  = BigInt("b000")
    val RexpectedRs1     = BigInt("b01011")
    val RexpectedRs2     = BigInt("b01010")
    val RexpectedFunky7  = BigInt("b0000000")
    val RexpectedImm_I_S = BigInt("b000000001010")
    val RexpectedImm_U   = BigInt("b00000000101001011000")


        poke(d.io.fetchInstruction, RsampleInstruction)
        poke(d.io.fetchAddress, RsampleAddress)
        poke(d.io.fetchAddress, RsampleRobTag) 

        step(1)
        expect(d.io.decodeOpcode,  RexpectedOpcode)
        expect(d.io.decodeRd,      RexpectedRd)
        expect(d.io.decodeFunky3,  RexpectedFunky3)
        expect(d.io.decodeRs1,     RexpectedRs1)
        expect(d.io.decodeRs2,     RexpectedRs2)
        expect(d.io.decodeFunky7,  RexpectedFunky7)
        expect(d.io.decodeImm_I_S, RexpectedImm_I_S)
        expect(d.io.decodeImm_U,   RexpectedImm_U)
        expect(d.io.decodeRobTag,  RsampleRobTag)
        expect(d.io.decodeAddress, RsampleAddress)
        expect(d.io.isR,  1)
        expect(d.io.isI,  0)
        expect(d.io.isS,  0)
        expect(d.io.isSB, 0)
        expect(d.io.isU,  0)
        expect(d.io.isUJ, 0)


/////////////S-Type Test///////////////////


    //poke values
    val SsampleInstruction = BigInt("b1010101_11111_01111_001_01010_0100011")
    val SsampleAddress = BigInt("b0000000000000000000000000000000000000000000000000000000000000000")
    val SsampleRobTag = BigInt("b101100")

    //expected values
    val SexpectedOpcode  = BigInt("b0100011")
    val SexpectedRd      = BigInt("b01010")
    val SexpectedFunky3  = BigInt("b001")
    val SexpectedRs1     = BigInt("b01111")
    val SexpectedRs2     = BigInt("b11111")
    val SexpectedFunky7  = BigInt("b1010101")
    val SexpectedImm_I_S = BigInt("b101010111111")
    val SexpectedImm_U   = BigInt("b10101011111101111001")


        poke(d.io.fetchInstruction, SsampleInstruction)
        poke(d.io.fetchAddress, SsampleAddress)
        poke(d.io.fetchAddress, SsampleRobTag) 

        step(1)
        expect(d.io.decodeOpcode,  SexpectedOpcode)
        expect(d.io.decodeRd,      SexpectedRd)
        expect(d.io.decodeFunky3,  SexpectedFunky3)
        expect(d.io.decodeRs1,     SexpectedRs1)
        expect(d.io.decodeRs2,     SexpectedRs2)
        expect(d.io.decodeFunky7,  SexpectedFunky7)
        expect(d.io.decodeImm_I_S, SexpectedImm_I_S)
        expect(d.io.decodeImm_U,   SexpectedImm_U)
        expect(d.io.decodeRobTag,  SsampleRobTag)
        expect(d.io.decodeAddress, SsampleAddress)
        expect(d.io.isR,  0)
        expect(d.io.isI,  0)
        expect(d.io.isS,  1)
        expect(d.io.isSB, 0)
        expect(d.io.isU,  0)
        expect(d.io.isUJ, 0)


/////////////SB-Type Test///////////////////


    //poke values
    val SBsampleInstruction = BigInt("b1111111_00001_00010_100_00011_1100011")
    val SBsampleAddress = BigInt("b0000000000000000000000000000000000000000000000000000000000000000")
    val SBsampleRobTag = BigInt("b101100")

    //expected values
    val SBexpectedOpcode  = BigInt("b1100011")
    val SBexpectedRd      = BigInt("b00011")
    val SBexpectedFunky3  = BigInt("b100")
    val SBexpectedRs1     = BigInt("b00010")
    val SBexpectedRs2     = BigInt("b00001")
    val SBexpectedFunky7  = BigInt("b1111111")
    val SBexpectedImm_U   = BigInt("b11111110000100010100")


        poke(d.io.fetchInstruction, SBsampleInstruction)
        poke(d.io.fetchAddress, SBsampleAddress)
        poke(d.io.fetchAddress, SBsampleRobTag) 

        step(1)
        expect(d.io.decodeOpcode,  SBexpectedOpcode)
        expect(d.io.decodeRd,      SBexpectedRd)
        expect(d.io.decodeFunky3,  SBexpectedFunky3)
        expect(d.io.decodeRs1,     SBexpectedRs1)
        expect(d.io.decodeRs2,     SBexpectedRs2)
        expect(d.io.decodeFunky7,  SBexpectedFunky7)
        expect(d.io.decodeImm_U,   SBexpectedImm_U)
        expect(d.io.decodeRobTag,  SBsampleRobTag)
        expect(d.io.decodeAddress, SBsampleAddress)
        expect(d.io.isR,  0)
        expect(d.io.isI,  0)
        expect(d.io.isS,  0)
        expect(d.io.isSB, 1)
        expect(d.io.isU,  0)
        expect(d.io.isUJ, 0)


/////////////U-Type Test///////////////////


    //poke values
    val UsampleInstruction = BigInt("b11111111111111111111_00001_0110111")
    val UsampleAddress = BigInt("b0000000000000000000000000000000000000000000000000000000000000000")
    val UsampleRobTag = BigInt("b101100")

    //expected values
    val UexpectedOpcode  = BigInt("b0110111")
    val UexpectedRd      = BigInt("b00001")
    val UexpectedFunky3  = BigInt("b111")
    val UexpectedRs1     = BigInt("b11111")
    val UexpectedRs2     = BigInt("b11111")
    val UexpectedFunky7  = BigInt("b1111111")
    val UexpectedImm_I_S = BigInt("b111111111111")
    val UexpectedImm_U   = BigInt("b11111111111111111111")


        poke(d.io.fetchInstruction, UsampleInstruction)
        poke(d.io.fetchAddress, UsampleAddress)
        poke(d.io.fetchAddress, UsampleRobTag) 

        step(1)
        expect(d.io.decodeOpcode,  UexpectedOpcode)
        expect(d.io.decodeRd,      UexpectedRd)
        expect(d.io.decodeFunky3,  UexpectedFunky3)
        expect(d.io.decodeRs1,     UexpectedRs1)
        expect(d.io.decodeRs2,     UexpectedRs2)
        expect(d.io.decodeFunky7,  UexpectedFunky7)
        expect(d.io.decodeImm_I_S, UexpectedImm_I_S)
        expect(d.io.decodeImm_U,   UexpectedImm_U)
        expect(d.io.decodeRobTag,  UsampleRobTag)
        expect(d.io.decodeAddress, UsampleAddress)
        expect(d.io.isR,  0)
        expect(d.io.isI,  0)
        expect(d.io.isS,  0)
        expect(d.io.isSB, 0)
        expect(d.io.isU,  1)
        expect(d.io.isUJ, 0)


/////////////UJ-Type Test///////////////////


    //poke values
    val UJsampleInstruction = BigInt("b10000000000100000000_00001_1101111")
    val UJsampleAddress = BigInt("b0000000000000000000000000000000000000000000000000000000000000000")
    val UJsampleRobTag = BigInt("b101100")

    //expected values
    val UJexpectedOpcode  = BigInt("b1101111")
    val UJexpectedRd      = BigInt("b00001")
    val UJexpectedFunky3  = BigInt("b000")
    val UJexpectedImm_U   = BigInt("b10000000000100000000")


        poke(d.io.fetchInstruction, UJsampleInstruction)
        poke(d.io.fetchAddress, UJsampleAddress)
        poke(d.io.fetchAddress, UJsampleRobTag) 

        step(1)
        expect(d.io.decodeOpcode,  UJexpectedOpcode)
        expect(d.io.decodeRd,      UJexpectedRd)
        expect(d.io.decodeFunky3,  UJexpectedFunky3)
        expect(d.io.decodeImm_U,   UJexpectedImm_U)
        expect(d.io.decodeRobTag,  UJsampleRobTag)
        expect(d.io.decodeAddress, UJsampleAddress)
        expect(d.io.isR,  0)
        expect(d.io.isI,  0)
        expect(d.io.isS,  0)
        expect(d.io.isSB, 0)
        expect(d.io.isU,  0)
        expect(d.io.isUJ, 1)


}


//Test Generator
class DecodeModuleGenerator extends TestGenerator {
  def genMod(): Module = Module(new DecodeChannel())
  def genTest[T <: Module](d: T): Tester[T] = 
    (new DecodeTester(d.asInstanceOf[DecodeChannel])).asInstanceOf[Tester[T]]
}
