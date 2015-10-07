import java.io.FileWriter;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Hashtable;

import static java.lang.System.exit;

/**
 * Created by xiaoyifan on 11/26/14.
 */
public class symbolTable {

    private Hashtable<String,Tuple> classScope;
    private Hashtable<String,Tuple> subScope;
    private Hashtable<String,Tuple> currentScope;

    private int subArgumentIndex, subVarIndex, classStaticIndex, classFieldIndex;

    public symbolTable()
    {
        classScope = new Hashtable<String, Tuple>();
        subScope = new Hashtable<String, Tuple>();
        currentScope = subScope;

        subArgumentIndex =0;
        subVarIndex =0;
        classStaticIndex = 0;
        classFieldIndex = 0;

    }

    public void startSubroutine(String subroutineType)
    {
        subScope.clear();

        currentScope = subScope;

                if(subroutineType.equals("method"))
                {
                    subArgumentIndex = 1;
                }
                else {
                    subArgumentIndex = 0;
                }

        subVarIndex = 0;
    }

    public void Define(String name, String type, String kind)
    {
        int i = -1;
        Tuple temp = null;

        if (kind.equals("STATIC") || kind.equals("FIELD")){


            if (kind.equals("STATIC"))
            {
                i = classStaticIndex++;
            }
            else if (kind.equals("FIELD"))
            {
                i = classFieldIndex++;
            }

            temp = classScope.put(name, new Tuple(type, kind, i));

            if (temp != null)
            {
                System.out.println("multiple declarations of class identifier");
                exit(1);
            }
        }
        else if (kind.equals("ARG") || kind.equals("VAR"))
        {
            if (kind.equals("ARG"))
            {
                i = subArgumentIndex++;
            }
            else if (kind.equals("VAR"))
            {
                i = subVarIndex++;
            }

            temp = subScope.put(name, new Tuple(type,kind,i));

            if (temp != null)
            {
                System.out.println("multiple declarations");
                exit(1);
            }

        }
        else throw new IllegalArgumentException("something wrong");

    }

    public int VarCount(String kind)
    {
        int count = 0;
        Hashtable<String, Tuple> tempScope = null;
        Enumeration<String> e;

        if(kind.equals("VAR") || kind.equals("ARG"))
            tempScope = subScope;
        else if(kind.equals("FIELD") || kind.equals("STATIC"))
            tempScope = classScope;
        else {
            System.out.println("Expected kind: static, field, argument or var.");
            exit(1);
        }

        e = tempScope.keys();
        while(e.hasMoreElements()) {
            String key = e.nextElement();
            if(tempScope.get(key) != null && tempScope.get(key).getKind().equals(kind))
                count++;
        }

        return count;
    }



    public String KindOf(String name)
    {
        Tuple temp = currentScope.get(name);
        String kind = null;

        if(temp != null)
            return temp.getKind();

        if(currentScope != classScope)
        {
            temp = classScope.get(name);
            if(temp != null)
                return temp.getKind();
        }

        return "NONE";
    }


    public String TypeOf(String name)
    {
        Tuple temp = currentScope.get(name);

        if(temp != null)
            return temp.getType();

        if(currentScope != classScope) {
            temp = classScope.get(name);
            if(temp != null)
                return temp.getType();
        }

        return null;
    }

    public int IndexOf(String name)
    {
        Tuple tmp = currentScope.get(name);
        if(tmp != null)
            return tmp.getIndex();

        if(currentScope != classScope) {
            tmp = classScope.get(name);
            if(tmp != null)
                return tmp.getIndex();
        }

        return -1;
    }

}
