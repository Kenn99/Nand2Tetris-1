// Runs an infinite loop that listens to the keyboard input. 
// When a key is pressed (any key), the program blackens the screen,
// i.e. writes "black" in every pixel. When no key is pressed, the
// program clears the screen, i.e. writes "white" in every pixel.

// Put your code here.

(LOOP)
@SCREEN
D=A//A stores the address number of the top-left pixel
@PIXEL_IDX
M=D
//set the screen first pixel address in the index

@KBD
D=M//the value from keyboard
@BE_WHITE
D;JEQ //if nothing pressed(D==0), go to the BE_WHITE
D=-1
//a branch to decide the color
//0 is white
//-1 is black

(BE_WHITE)
@COLOR
M=D
//use color to store the white/black


(DRAW_LOOP)
//if this is the last element on the screen, the draw_loop should break
@PIXEL_IDX
D=M
//D store the index


@KBD 
D=D-A//A is the memory start address of the keyboard, and the end of screen
//screen from 16384 to 24575, keyboard is 24576
@LOOP
D;JGE
//if the drawing is completed, then go to the next main loop


//draw
@COLOR
D=M
//using D store the color
@PIXEL_IDX
A=M
//put the index to A
M=D
//at this time the M holds the value M[A],D is the color
//this pixel is done

D=A+1 //D stores the address of next pixel
@PIXEL_IDX
M=D
//IDX++

@DRAW_LOOP
0;JMP