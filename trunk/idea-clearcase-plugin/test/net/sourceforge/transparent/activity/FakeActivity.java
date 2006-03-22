package net.sourceforge.transparent.activity;

/**
 * @author Gilles Philippart
 */
public class FakeActivity extends Activity {

    private String name;
    private String owner;

    public FakeActivity(String name, String owner) {
        super(null);
        this.name = name;
        this.owner = owner;
    }

    public String getHeadline() {
        return name;
    }

    public String getOwner() {
        return owner;
    }

}
