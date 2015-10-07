import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import static java.lang.System.exit;

/**
 * Created by xiaoyifan on 11/26/14.
 */
public class VMWriter {

    private FileWriter fWriter;

    public VMWriter(String writer) throws IOException{

        File file = new File(writer);
        fWriter = new FileWriter(file);

    }

    public void writePush(String segment, int index)throws IOException
    {
        String seg = "";
        if (segment.equals("CONST")) {
            seg = "constant";

        } else if (segment.equals("ARG")) {
            seg = "argument";

        } else if (segment.equals("LOCAL")) {
            seg = "local";

        } else if (segment.equals("VAR")) {
            seg = "local";

        } else if (segment.equals("STATIC")) {
            seg = "static";

        } else if (segment.equals("FIELD")) {
            seg = "this";

        } else if (segment.equals("THIS")) {
            seg = "this";

        } else if (segment.equals("THAT")) {
            seg = "that";

        } else if (segment.equals("POINTER")) {
            seg = "pointer";

        } else if (segment.equals("TEMP")) {
            seg = "temp";

        }

        if(!seg.isEmpty()) {
            fWriter.write("push " + seg + " " + index + "\n");

            System.out.println("push " + seg + " " + index + "\n");
        }

    }

    public void writePop(String segment, int index) throws IOException {
        String seg = "";
        if (segment.equals("CONST")) {
            seg = "constant";

        } else if (segment.equals("ARG")) {
            seg = "argument";

        } else if (segment.equals("LOCAL")) {
            seg = "local";

        } else if (segment.equals("VAR")) {
            seg = "local";

        } else if (segment.equals("STATIC")) {
            seg = "static";

        } else if (segment.equals("FIELD")) {
            seg = "this";

        } else if (segment.equals("THIS")) {
            seg = "this";

        } else if (segment.equals("THAT")) {
            seg = "that";

        } else if (segment.equals("POINTER")) {
            seg = "pointer";

        } else if (segment.equals("TEMP")) {
            seg = "temp";

        }

        if(!seg.isEmpty())
        { fWriter.write("pop " + seg + " " + index + "\n");
        System.out.println("pop " + seg + " " + index + "\n");}
    }

    public void WriteArithmetic(String command) throws IOException {
        if(command.equals("ADD") || command.equals("SUB") || command.equals("NEG") ||
           command.equals("EQ") || command.equals("GT")|| command.equals("LT") || command.equals("AND") ||
           command.equals("OR") || command.equals("NOT"))
               {
            command = command.toLowerCase(); //may not be necessary
            fWriter.write(command + "\n");
                   System.out.println(command + "\n");
                }
        else {
            System.out.println("this command causes an error: " + command);
            exit(1);
        }
    }

    public void WriteLabel(String label) throws IOException {
        fWriter.write("label " + label + "\n");
        System.out.println("label " + label + "\n");
    }

    public void WriteGoto(String label) throws IOException
    {
        fWriter.write("goto " + label + "\n");
        System.out.println("goto " + label + "\n");
    }

    public void WriteIf(String label) throws IOException
    {
        fWriter.write("if-goto " + label + "\n");
        System.out.println("if-goto " + label + "\n");
    }

    public void WriteCall(String name, int numOfArgs) throws IOException
    {
        fWriter.write("call " + name+ " " + numOfArgs+ "\n");
        System.out.println("call " + name+ " " + numOfArgs+ "\n");
    }

    public void WriteFunction(String name, int numOfLocals) throws IOException
    {
        fWriter.write("function " + name+ " " + numOfLocals + "\n");
        System.out.println("function " + name+ " " + numOfLocals + "\n");
    }

    public void writeReturn() throws IOException
    {
        fWriter.write("return\n");
        System.out.println("return\n");
    }

    public void close()
    {
        try {
            fWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
