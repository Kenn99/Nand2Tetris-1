/**
 * Created by xiaoyifan on 11/5/14.
 */
public class InstructionInfo {

   public  int commandType;
    public  String op;
    public  String dest;
    public  int index;


    InstructionInfo(int commandType, String op, String dest, String index)
    {
        this.commandType = commandType;
        this.op = op;
        this.dest = dest;
        this.index = Integer.parseInt(index);
    }

    InstructionInfo(int commandType, String op)
    {
        this.commandType = commandType;
        this.op = op;
        this.dest = null;
        this.index = -1;
    }


}
