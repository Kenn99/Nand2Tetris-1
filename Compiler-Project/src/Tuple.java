/**
 * Created by xiaoyifan on 11/27/14.
 */
public class Tuple {

    private String type, kind;
    private int index;

    public Tuple(String type, String kind, int index) {
        this.type = type;
        this.kind = kind;
        this.index = index;
    }

    public String getType() {
        return type;
    }

    public String getKind() {
        return kind;
    }

    public int getIndex() {
        return index;
    }
}
