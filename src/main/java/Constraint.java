import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class Constraint {
    String [] constraint;
    boolean hc1; boolean hc2; double hc3; boolean hc4;
    LocalTime hc5a; LocalTime hc5b; LocalTime hc5c; LocalTime hc5d; LocalTime hc5e; LocalTime hc5f;
    int hc6; int hc7;
    int sc1a; int sc1b; int sc1c; int sc2; int sc3a; int sc3b; int sc3c; int sc4;
    int sc5aMax; int sc5aMin; int sc5bMax; int sc5bMin; int sc5cMax; int sc5cMin;
    boolean sc6; boolean sc7; int sc8; int sc9;

    public Constraint(String[] constraint){
        this.constraint = constraint;
    }

    public String[] getConstraint() {
        return constraint;
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
        if (constraint[4].length()==1)
            hc5a = LocalTime.parse("0" + constraint[4]+":00");
        else
            hc5a = LocalTime.parse(constraint[4]+":00");
    }

    public LocalTime getHc5a() {
        return hc5a;
    }

    public void setHc5b (){
        if (constraint[5].length()==1)
            hc5b = LocalTime.parse("0" + constraint[5]+":00");
        else
            hc5b = LocalTime.parse(constraint[5]+":00");
    }

    public LocalTime getHc5b() {
        return hc5b;
    }

    public void setHc5c (){
        if (constraint[6].length()==1)
            hc5c = LocalTime.parse("0" + constraint[6]+":00");
        else
            hc5c = LocalTime.parse(constraint[6]+":00");
    }

    public LocalTime getHc5c() {
        return hc5c;
    }

    public void setHc5d (){
        if (constraint[7].length()==1)
            hc5d = LocalTime.parse("0" + constraint[7]+":00");
        else
            hc5d = LocalTime.parse(constraint[7]+":00");
    }

    public LocalTime getHc5d() {
        return hc5d;
    }

    public void setHc5e (){
        if (constraint[8].length()==1)
            hc5e = LocalTime.parse("0" + constraint[8]+":00");
        else
            hc5e = LocalTime.parse(constraint[8]+":00");
    }

    public LocalTime getHc5e() {
        return hc5e;
    }

    public void setHc5f (){
        if (constraint[9].length()==1)
            hc5f = LocalTime.parse("0" + constraint[9]+":00");
        else
            hc5f = LocalTime.parse(constraint[9]+":00");
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

    public void setSc1a () {
        if (constraint[0].equals("N/A"))
            sc1a =0;
        else
            sc1a = Integer.parseInt(constraint[0]);
    }

    public void setSc1b () {
        if (constraint[1].equals("N/A"))
            sc1b =0;
        else
            sc1b = Integer.parseInt(constraint[1]);
    }

    public void setSc1c () {
        if (constraint[2].equals("N/A"))
            sc1c = 0;
        else
            sc1c = Integer.parseInt(constraint[2]);
    }

    public void setSc2 () {
        if (constraint[3].equals("N/A"))
            sc2 =0;
        else
            sc2 = Integer.parseInt(constraint[3]);
    }

    public void setSc3a () {
        if (constraint[4].equals("N/A"))
            sc3a =0;
        else
            sc3a = Integer.parseInt(constraint[4]);
    }

    public void setSc3b () {
        if (constraint[5].equals("N/A"))
            sc3b =0;
        else
            sc3b = Integer.parseInt(constraint[5]);
    }

    public void setSc3c () {
        if (constraint[6].equals("N/A"))
            sc3c =0;
        else
            sc3c = Integer.parseInt(constraint[6]);
    }

    public void setSc4 () {
        if (constraint[7].equals("N/A"))
            sc4 =0;
        else
            sc4 = Integer.parseInt(constraint[7]);
    }

    public void setSc5aMax () {
        if (constraint[8].equals("N/A"))
            sc5aMax =0;
        else
            sc5aMax = Integer.parseInt(constraint[8].substring(0, 1));
    }

    public void setSc5aMin () {
        if (constraint[8].equals("N/A"))
            sc5aMin =0;
        else
            sc5aMin = Integer.parseInt(constraint[8].substring(2, 3));
    }

    public void setSc5bMax () {
        if (constraint[9].equals("N/A"))
            sc5bMax =0;
        else
            sc5bMax = Integer.parseInt(constraint[9].substring(0, 1));
    }

    public void setSc5bMin () {
        if (constraint[9].equals("N/A"))
            sc5bMin =0;
        else
            sc5bMin = Integer.parseInt(constraint[9].substring(2, 3));
    }

    public void setSc5cMax () {
        if (constraint[10].equals("N/A"))
            sc5cMax = 0;
        else
            sc5cMax = Integer.parseInt(constraint[10].substring(0, 1));
    }

    public void setSc5cMin () {
        if (constraint[10].equals("N/A"))
            sc5cMin = 0;
        else
            sc5cMin = Integer.parseInt(constraint[10].substring(2, 3));
    }

    public void setSc6 () {
        if (constraint[11].equals("N/A"))
            sc6 = false;
        if (constraint[11].equals("Yes"))
            sc6 = true;
    }

    public void setSc7 () {
        if (constraint[12].equals("N/A"))
            sc7 = false;
        if (constraint[12].equals("Yes"))
            sc7 = true;
    }

    public void setSc8 () {
        if (constraint[13].equals("N/A"))
            sc8 = 0;
        else
            sc8 = Integer.parseInt(constraint[13]);
    }

    public void setSc9 () {
        if (constraint[14].equals("N/A"))
            sc9 = 0;
        else
            sc9 = Integer.parseInt(constraint[14]);
    }

    public int getSc1a() {
        return sc1a;
    }

    public int getSc1b() {
        return sc1b;
    }

    public int getSc1c() {
        return sc1c;
    }

    public int getSc2() {
        return sc2;
    }

    public int getSc3a() {
        return sc3a;
    }

    public int getSc3b() {
        return sc3b;
    }

    public int getSc3c() {
        return sc3c;
    }

    public int getSc4() {
        return sc4;
    }

    public int getSc5aMax() {
        return sc5aMax;
    }

    public int getSc5aMin() {
        return sc5aMin;
    }

    public int getSc5bMax() {
        return sc5bMax;
    }

    public int getSc5bMin() {
        return sc5bMin;
    }

    public int getSc5cMax() {
        return sc5cMax;
    }

    public int getSc5cMin() {
        return sc5cMin;
    }

    public boolean getSc6() {
        return sc6;
    }

    public boolean getSc7() {
        return sc7;
    }

    public int getSc8() {
        return sc8;
    }

    public int getSc9() {
        return sc9;
    }
}
