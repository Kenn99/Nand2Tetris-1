import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;

import static java.lang.System.exit;

/**
 * Created by xiaoyifan on 11/5/14.
 */
public class VMtranslator {

    public static void main(String[] args) throws FileNotFoundException {

        File FileIn = new File(args[0]);

        if(args.length<1)
        {
            System.out.println("input with no file");
            exit(0);
        }//*/
        if (FileIn.isDirectory() && FileIn.exists())
        {
            File files[] = FileIn.listFiles();
            if (files != null)
            {
                for (int i=0;i<files.length;i++)
                {
                    String filename = files[i].getName();
                    if (filename.endsWith(".vm"))
                    {
                        translate(filename);
                    }
                }//browse all the files in this directory
            }

        }
        else translate(args[0]);

    }
    //if the argument is wrong just return
    //if the argument is the filename, go ahead and parse that file
    //if the argument is a directory, list all the stuff inside the directoy to check is the item is a .vm file
    //and if true, translate that file.



    public static void translate (String FileName) throws FileNotFoundException
    {

        String[] splitInputFileName;
        splitInputFileName = FileName.split("\\.");

        String outName = splitInputFileName[0]+".asm";
        //the output filename should be the same and the input, but the suffix should be ".asm"

        File inputFile = new File(FileName);

        Scanner in = new Scanner(inputFile);

        PrintWriter out = new PrintWriter(outName);
        //initialize the printer

        codeGenerator codeGenerate = new codeGenerator();


        code newcode = new code();

        while(in.hasNextLine())//if the file is not end
        {

            String Processing = in.nextLine().trim();

            if (Processing.length() != 0) //get rid of the empty or blank lines
            {

                String noComment = stripComment(Processing);//get rid of the comments

                String noCommentCommand = noComment.trim();
                //get rid of the situation that the comment is the whole line, and when the
                //comment is killed, all the line becomes a new blank line.

                if (noCommentCommand.length()!=0) {
                 //do the parse stuff to the commands, all the spaces and comments are trimmed.

                 InstructionInfo info = parser.decodeCommand(noCommentCommand);
                 //since the InstructionInfo is included


                 String instructions = codeGenerator.generate(info,splitInputFileName[0]);

                 out.println(instructions);

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
