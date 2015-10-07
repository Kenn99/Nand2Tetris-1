import static java.lang.System.exit;

/**
 * Created by xiaoyifan on 11/5/14.
 */
public class codeGenerator {

    public static int labelNumber;



    public codeGenerator()
    {
        labelNumber =0;
    }


    public static String generate(InstructionInfo info,String moduleName)
    {
        String instruction="";

        if(info.commandType == InstructionType.c_push)
        {
            if (info.dest.equals("local")||info.dest.equals("argument")||info.dest.equals("this")||info.dest.equals("that"))
            {
               instruction+= code.pushLATT(info.dest, info.index);
            }
            else if(info.dest.equals("pointer")||info.dest.equals("temp"))
            {
                instruction+= code.pushPT(info.dest, info.index);
            }
            else if(info.dest.equals("static"))
            {
                instruction+= code.pushStatic(info.dest, info.index, moduleName);
            }
            else if(info.dest.equals("constant"))
            {
                instruction+= code.pushConstant(info.index);
            }
            else{
                System.out.println("things wrong from the push instruction");
                exit(1);
            }
        }
        else if(info.commandType == InstructionType.c_pop)
        {
            if (info.dest.equals("local")||info.dest.equals("argument")||info.dest.equals("this")||info.dest.equals("that"))
            {
                instruction+= code.popLATT(info.dest, info.index);
            }
            else if(info.dest.equals("pointer")||info.dest.equals("temp"))
            {
                instruction+= code.popPT(info.dest, info.index);
            }
            else if(info.dest.equals("static"))
            {
                instruction+= code.popStatic(info.index, moduleName);
            }
            else{
                System.out.println("things wrong from the pop instruction");
                exit(1);
            }

        }
        else if(info.commandType == InstructionType.c_arithmetic)
        {
           String instructionwithLabel = code.operand_map.get(info.op);
           instruction = replaceLabel(instructionwithLabel);
        }
        else
        {

            System.out.println("things wrong from the instruction");
            exit(1);

        }

        return instruction;
    }

    public static String replaceLabel(String instructionwithLabel)
    {

      String replaceOne = instructionwithLabel.replace("_LABEL_0_", "label"+labelNumber);
        labelNumber++;
       String replaceTwo = replaceOne.replace("_LABEL_1_", "label"+labelNumber);
        labelNumber++;

        return replaceTwo;

    }


}
