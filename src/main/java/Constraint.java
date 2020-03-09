import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class Constraint {
    String [] constraint;
    boolean hc1; boolean hc2; double hc3; boolean hc4;
    LocalTime hc5a; LocalTime hc5b; LocalTime hc5c; LocalTime hc5d; LocalTime hc5e; LocalTime hc5f;
    int hc6; int hc7;

    public Constraint(String[] constraint){
        this.constraint = constraint;
    }

    public void setHc1() {
        if(constraint[0].equals("Yes")){
            this.hc1 = true;
        }if(constraint[0].equals("N/A")) {
            this.hc1 = false;
        }
    }

    public boolean getHc1() {
        return hc1;
    }

    public void setHc2() {
        if(constraint[1].equals("Yes")){
            this.hc2 = true;
        }if(constraint[1].equals("N/A")) {
            this.hc2 = false;
        }
    }

    public boolean getHc2(){
        return hc2;
    }

    public void setHc3() {
        this.hc3 = Double.parseDouble(constraint[2].substring(1,2));
    }

    public double getHc3() { return hc3 / 100; }

    public void setHc4() {
        if(constraint[3].equals("Yes")){
            this.hc4 = true;
        }if(constraint[3].equals("N/A")) {
            this.hc4 = false;
        }
    }

    public boolean getHc4() {
        return hc4;
    }

    public void setHc5a (){
        String time = constraint[4] +":00";
        this.hc5a = LocalTime.parse(time);
    }

    public LocalTime getHc5a() {
        return hc5a;
    }

    public void setHc5b (){
        String time = constraint[5] +":00";
        this.hc5b = LocalTime.parse(time);
    }

    public LocalTime getHc5b() {
        return hc5b;
    }

    public void setHc5c (){
        String time = constraint[6] +":00";
        this.hc5c = LocalTime.parse(time);
    }

    public LocalTime getHc5c() {
        return hc5c;
    }

    public void setHc5d (){
        String time = constraint[7] +":00";
        this.hc5d = LocalTime.parse(time);
    }

    public LocalTime getHc5d() {
        return hc5d;
    }

    public void setHc5e (){
        String time = constraint[8] +":00";
        this.hc5e = LocalTime.parse(time);
    }

    public LocalTime getHc5e() {
        return hc5e;
    }

    public void setHc5f (){
        String time = constraint[9] +":00";
        this.hc5f = LocalTime.parse(time);
    }

    public LocalTime getHc5f() {
        return hc5f;
    }

    public void setHc6() {
        this.hc6 = Integer.parseInt(constraint[10]);
    }

    public int getHc6() {
        return hc6;
    }

    public void setHc7() {
        this.hc7 = Integer.parseInt(constraint[11]);
    }

    public int getHc7() {
        return hc7;
    }

    public String[] getConstraint() {
        return constraint;
    }
}
