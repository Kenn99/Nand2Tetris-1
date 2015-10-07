import java.util.HashMap;

/**
 * Created by xiaoyifan on 11/5/14.
 */
public class code {

    public static HashMap<String, String> map;
    public static HashMap<String, String> operand_map;
    public code()
    {

        map = new HashMap<String, String>();
        map.put( "local","LCL");
        map.put("argument","ARG");
        map.put("this","THIS");
        map.put("that","THAT");
        map.put("pointer","3");
        map.put("temp","5");

        operand_map = new HashMap<String, String>();
        operand_map.put("add", "@SP\n" +
                                "A=M\n" +
                                "A=A-1\n" +
                                "D=M\n" +
                                "A=A-1\n" +
                                "M=M+D\n" +
                                "@SP\n" +
                                "M=M-1\n");
        operand_map.put("sub", "@SP\n" +
                                "A=M\n" +
                                "A=A-1\n" +
                                "D=M\n" +
                                "A=A-1\n" +
                                "M=M-D\n" +
                                "@SP\n" +
                                "M=M-1\n");

        operand_map.put("neg","@SP\n" +
                              "A=M\n" +
                              "A=A-1\n" +
                              "M=-M\n");

        operand_map.put("eq", "@SP\n" +
                              "M=M-1\n" +
                              "A=M\n" +
                              "D=M\n" +
                              "@SP\n" +
                              "M=M-1\n" +
                              "@SP\n" +
                              "A=M\n" +
                              "D=M-D\n" +
                              "@_LABEL_0_\n" +
                              "D;JEQ\n" +
                              "@SP\n" +
                              "A=M\n" +
                              "M=0\n" +
                              "@_LABEL_1_\n" +
                              "0;JMP\n" +
                              "(_LABEL_0_)\n" +
                              "@SP\n" +
                              "A=M\n" +
                              "M=-1\n" +
                              "(_LABEL_1_)\n" +
                              "@SP\n" +
                              "M=M+1");

        operand_map.put("lt", "@SP\n" +
                                "M=M-1\n" +
                                "A=M\n" +
                "D=M\n" +
                "@SP\n" +
                "M=M-1\n" +
                "@SP\n" +
                "A=M\n" +
                "D=M-D\n" +
                "@_LABEL_0_\n" +
                "D;JLT\n" +
                "@SP\n" +
                "A=M\n" +
                "M=0\n" +
                "@_LABEL_1_\n" +
                "0;JMP\n" +
                "(_LABEL_0_)\n" +
                "@SP\n" +
                "A=M\n" +
                "M=-1\n" +
                "(_LABEL_1_)\n" +
                "@SP\n" +
                "M=M+1\n");

        operand_map.put("gt","@SP\n" +
                "M=M-1\n" +
                "A=M\n" +
                "D=M\n" +
                "@SP\n" +
                "M=M-1\n" +
                "@SP\n" +
                "A=M\n" +
                "D=M-D\n" +
                "@_LABEL_0_\n" +
                "D;JGT\n" +
                "@SP\n" +
                "A=M\n" +
                "M=0\n" +
                "@_LABEL_1_\n" +
                "0;JMP\n" +
                "(_LABEL_0_)\n" +
                "@SP\n" +
                "A=M\n" +
                "M=-1\n" +
                "(_LABEL_1_)\n" +
                "@SP\n" +
                "M=M+1\n");

        operand_map.put("and","@SP\n" +
                "A=M\n" +
                "A=A-1\n" +
                "D=M\n" +
                "A=A-1\n" +
                "M=M&D\n" +
                "@SP\n" +
                "M=M-1\n");

        operand_map.put("or","@SP\n" +
                "A=M\n" +
                "A=A-1\n" +
                "D=M\n" +
                "A=A-1\n" +
                "M=M|D\n" +
                "@SP\n" +
                "M=M-1\n");

        operand_map.put("not","@SP\n" +
                "A=M\n" +
                "A=A-1\n" +
                "M=!M\n");
    }

    public static String pushLATT(String dest, int index)
    {
        dest = map.get(dest);
        return
        "@"+index +'\n'  +
        "D=A\n"  +
        "@"+dest+'\n'  +
        "A=M\n"  +
        "A=A+D\n"  +
        "D=M\n"  +
        "@SP\n"  +
        "A=M\n"  +
        "M=D\n"  +
        "@SP\n"  +
        "M=M+1\n";
    }

    public static String pushPT(String dest, int index)
    {
        dest = map.get(dest);
        return
        "@"+ index+"\n"+
        "D=A\n"+
        "@"+dest+"\n"+
        "A=A+D\n" +
        "D=M\n" +
        "@SP\n" +
        "A=M\n" +
        "M=D\n" +
        "@SP\n" +
        "M=M+1\n";

    }

    public static String pushStatic(String dest, int index, String moduleName)
    {
        dest = moduleName + "." + index;
        return
        "@" + dest +'\n'+
        "D=M\n" +
        "@SP\n" +
        "A=M\n" +
        "M=D\n" +
        "@SP\n" +
        "M=M+1\n";

    }

    public static String pushConstant(int index)
    {

        return
        "@"+index +'\n' +
        "D=A\n" +
        "@SP\n" +
        "A=M\n" +
        "M=D\n" +
        "@SP\n" +
        "M=M+1\n";
    }

    public static String popLATT(String dest, int index)
    {
        dest = map.get(dest);
        return
        "@"+ index +'\n'+
        "D=A\n"+
        "@"+ dest +'\n' +
        "A=M\n" +
        "D=A+D\n"+
        "@SP\n"+
        "A=M\n"+
        "M=D\n"+
        "@SP\n"+
        "A=M\n"+
        "A=A-1\n" +
        "D=M\n" +
        "A=A+1\n" +
        "A=M\n"+
        "M=D\n"+
        "@SP\n"+
        "M=M-1\n";
    }

    public static String popPT(String dest, int index)
    {
        dest = map.get(dest);
        return
        "@" + index + '\n'+
        "D=A\n" +
        "@" + dest+'\n'+
        "D=A+D\n"+
        "@SP\n"+
        "A=M\n"+
        "M=D\n"+
        "@SP\n"+
        "A=M\n"+
        "A=A-1\n" +
        "D=M\n" +
        "A=A+1\n" +
        "A=M\n"+
        "M=D\n"+
        "@SP\n"+
        "M=M-1\n";
    }

    public static String popStatic(int index,String moduleName)
    {
       String dest = moduleName + "." + index;
        return
        "@SP\n"+
        "AM=M-1\n" +
        "D=M\n" +
        "@" + dest + '\n' +
        "M=D\n";
    }

}
