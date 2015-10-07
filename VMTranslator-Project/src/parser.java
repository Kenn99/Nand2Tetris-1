import static java.lang.System.exit;

/**
 * Created by xiaoyifan on 11/5/14.
 */
public class parser {


    public static InstructionInfo decodeCommand(String command)
    {
      String[] commandParse = command.split(" ");
        InstructionInfo info;
      if(commandParse.length==3)
      {
          if (commandParse[0].equals("push"))
          {
              int command_type = InstructionType.c_push;
              String operand = commandParse[0];
              String dest = commandParse[1];
              String index = commandParse[2];

              info = new InstructionInfo(command_type, operand, dest, index);
              return info;
          }
          else if (commandParse[0].equals("pop"))
          {
              int command_type = InstructionType.c_pop;
              String operand = commandParse[0];
              String dest = commandParse[1];
              String index = commandParse[2];

              info = new InstructionInfo(command_type, operand, dest, index);
              return info;
          }
          else return null;
      }
      else if (commandParse.length==1)
      {
          if (commandParse[0].equals("add")||commandParse[0].equals("sub")||commandParse[0].equals("neg")||
              commandParse[0].equals("eq") ||commandParse[0].equals("gt") ||commandParse[0].equals("lt") ||
              commandParse[0].equals("and")||commandParse[0].equals("or") ||commandParse[0].equals("not"))
          {
              int command_type = InstructionType.c_arithmetic;
              String operand = commandParse[0];

              info = new InstructionInfo(command_type, operand);

              return info;
          }
          else return null;
      }
      else
      {
          System.out.println("command input is wrong");
          return null;
      }

    }
}
