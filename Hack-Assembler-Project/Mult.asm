// This file is part of www.nand2tetris.org
// and the book "The Elements of Computing Systems"
// by Nisan and Schocken, MIT Press.
// File name: projects/04/Mult.asm

// Multiplies R0 and R1 and stores the result in R2.
// (R0, R1, R2 refer to RAM[0], RAM[1], and RAM[3], respectively.)

// Put your code here.
@2
M=0
//initialize R2 to 0
(LOOP)
@0
D=M
@END
D;JEQ


@2
D=M
@1
D=D+M
//R2=R2+R1

@2
M=D
@0
M=M-1
//R0--
@LOOP
0;JMP
(END)