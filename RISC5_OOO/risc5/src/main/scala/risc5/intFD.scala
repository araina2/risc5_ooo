package risc5

import Chisel._


class intFD extends Module{

       val io = new Bundle {
        
        val decodeBranchTaken0 = UInt(OUTPUT, 1)
        val decodeValid0 = UInt(OUTPUT, 1)
        val decodeOpcode0 = UInt(OUTPUT, 7)
        val decodeType0 = UInt(OUTPUT, 3)
        val decodeRd0 = UInt(OUTPUT, 5)
        val decodeFunky30 = UInt(OUTPUT, 3)
        val decodeRs10 = UInt(OUTPUT, 5)
        val decodeRs20 = UInt(OUTPUT, 5)
        val decodeFunky70 = UInt(OUTPUT, 7)
        val decode_Imm0 = UInt(OUTPUT, 20)
        val decodeRobTag0 = UInt(OUTPUT, 8)
        val decodeAddress0 = UInt(OUTPUT, 64)
        val decodeQueueSelect0 = UInt(OUTPUT, 1)
        val decodeIsStore0 = UInt(OUTPUT, 1)

        val decodeBranchTaken1 = UInt(OUTPUT, 1)
        val decodeValid1 = UInt(OUTPUT, 1)
        val decodeOpcode1 = UInt(OUTPUT, 7)
        val decodeType1 = UInt(OUTPUT, 3)
        val decodeRd1 = UInt(OUTPUT, 5)
        val decodeFunky31 = UInt(OUTPUT, 3)
        val decodeRs11 = UInt(OUTPUT, 5)
        val decodeRs21 = UInt(OUTPUT, 5)
        val decodeFunky71 = UInt(OUTPUT, 7)
        val decode_Imm1 = UInt(OUTPUT, 20)
        val decodeRobTag1 = UInt(OUTPUT, 8)
        val decodeAddress1 = UInt(OUTPUT, 64)
        val decodeQueueSelect1 = UInt(OUTPUT, 1)
        val decodeIsStore1 = UInt(OUTPUT, 1)
        
        val decodeBranchTaken2 = UInt(OUTPUT, 1)
        val decodeValid2 = UInt(OUTPUT, 1)
        val decodeOpcode2 = UInt(OUTPUT, 7)
        val decodeType2 = UInt(OUTPUT, 3)
        val decodeRd2 = UInt(OUTPUT, 5)
        val decodeFunky32 = UInt(OUTPUT, 3)
        val decodeRs12 = UInt(OUTPUT, 5)
        val decodeRs22 = UInt(OUTPUT, 5)
        val decodeFunky72 = UInt(OUTPUT, 7)
        val decode_Imm2 = UInt(OUTPUT, 20)
        val decodeRobTag2 = UInt(OUTPUT, 8)
        val decodeAddress2 = UInt(OUTPUT, 64)
        val decodeQueueSelect2 = UInt(OUTPUT, 1)
        val decodeIsStore2 = UInt(OUTPUT, 1)

        val decodeBranchTaken3 = UInt(OUTPUT, 1)
        val decodeValid3 = UInt(OUTPUT, 1)
        val decodeOpcode3 = UInt(OUTPUT, 7)
        val decodeType3 = UInt(OUTPUT, 3)
        val decodeRd3 = UInt(OUTPUT, 5)
        val decodeFunky33 = UInt(OUTPUT, 3)
        val decodeRs13 = UInt(OUTPUT, 5)
        val decodeRs23 = UInt(OUTPUT, 5)
        val decodeFunky73 = UInt(OUTPUT, 7)
        val decode_Imm3 = UInt(OUTPUT, 20)
        val decodeRobTag3 = UInt(OUTPUT, 8)
        val decodeAddress3 = UInt(OUTPUT, 64)
        val decodeQueueSelect3 = UInt(OUTPUT, 1)
        val decodeIsStore3 = UInt(OUTPUT, 1)
       }

//run perl script


//instantiates Instrucion Fetch rom


//need 4x decode channles

}


