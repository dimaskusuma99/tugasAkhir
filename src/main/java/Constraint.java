public class Constraint {
    String softconstraint; String a; String b; String value;

    public Constraint(String softconstraint, String a, String b, String value){
        this.softconstraint = softconstraint;
        this.a = a;
        this.b = b;
        this.value = value;
    }

    public String getSoftconstraint() { return softconstraint; }

    public String getA() { return a; }

    public String getB() { return b; }

    public String getValue() { return value; }
}
