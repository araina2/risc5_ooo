#!/usr/bin/perl

use strict;
use warnings;

my @lines = `/u/a/r/araina/private/CS752/risc5_ooo/riscv-tools/bin/spike -l @ARGV 2>&1`;

open(my $pc, '>:raw', '/tmp/pc.bin') or die "Unable to open: $!";
open(my $inst, '>:raw', '/tmp/inst.bin') or die "Unable to open: $!";

for my $instr (@lines) {

    # core   0: 0x0000000080003450 (0xfe078ce3) beqz
    if ($instr =~ /core\s+0: 0x([0-9a-f]+) \(0x([0-9a-f]+)\)/i ) {
        print $pc pack('q<',hex($1)) ;
        print $inst pack('q<',hex($2)) ;
    }

}

close($pc);
close($inst);

exit 0;
