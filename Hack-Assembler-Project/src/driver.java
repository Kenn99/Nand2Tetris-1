import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;

/**
 * Created by xiaoyifan on 10/30/14.
 */
public class driver {

    public static void main(String args[])throws FileNotFoundException
    {
        String FileName = "Fill.asm";

        String[] splitInputFileName;
        splitInputFileName = FileName.split("\\.");

        String outName = splitInputFileName[0]+".hack";
        //System.out.println(outName);

        symbol symbolTable = new symbol();



//first pass, create the table for labels
        File inputFile = new File(FileName);
        //create the file object in this program
        //and initialize it with the input file
        Scanner in = new Scanner(inputFile);

        PrintWriter out = new PrintWriter(outName);
        //initialize the printer

        int CommandCounter=0;

        while(in.hasNextLine())//if the file is not end
        {

            String Processing = in.nextLine().trim();

            if (Processing.length() != 0) //get rid of the empty or blank lines
            {


                String noComment = stripComment(Processing);//get rid of the comments

                String noCommentCommand = noComment.trim();

                if (noCommentCommand.length()!=0) {
                    //System.out.println(noCommentCommand);


                    if (parser.whichType(noCommentCommand).equals("L_COMMAND"))
                    {

                        symbolTable.addLPair(noCommentCommand.substring(1,noCommentCommand.length()-1),CommandCounter);
                        //System.out.println(noCommentCommand.substring(1,noCommentCommand.length()-1));
                        //System.out.println(CommandCounter);
                    }
                    else if(parser.whichType(noCommentCommand).equals("A_COMMAND") || parser.whichType(noCommentCommand).equals("C_COMMAND")) {
                        CommandCounter++;

                    }

                }


            }//if the line being processed is empty, just skip to the next
        }

        in.close();
        out.close();




   //second pass started
        inputFile = new File(FileName);
        //create the file object in this program
        //and initialize it with the input file
        in = new Scanner(inputFile);

        out = new PrintWriter(outName);
        //initialize the printer

        while(in.hasNextLine())//if the file is not end
        {

            String Processing = in.nextLine().trim();

            if (Processing.length() != 0) //get rid of the empty or blank lines
            {

                String noComment = stripComment(Processing);//get rid of the comments

                String noCommentCommand = noComment.trim();

            if (noCommentCommand.length()!=0) {

               if (noCommentCommand.charAt(0)!='(') {
                   String machineCode = parser.commandParse(noCommentCommand, symbolTable);
                   //the commandParse is implemented in the parser class.

                   out.println(machineCode);
               }
            }

            }//if the line being processed is empty, just skip to the next
        }

        in.close();
        out.close();

    }


    public static String stripComment(String inputLine){

        int flag = 0;

        String outString="";

        for (int i=0;i<inputLine.length();i++)
        {

            char c = inputLine.charAt(i);
            if (i < inputLine.length() - 1)
            {
                char cNext = inputLine.charAt(i + 1);
                //read the next two char
                if (c == '/' && cNext == '/') {
                    flag = 1;

                }//if there is a that the remain of this string is comment, set the flag to 1
            }

            if (flag==0){

                    outString+=c;
            }
        }
        return outString;
    }


}
