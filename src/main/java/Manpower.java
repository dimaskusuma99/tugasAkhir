public class Manpower {
    String shift; int [] need; int id;

    public Manpower(String shift, String id){
        this.shift = shift;
        this.id = Integer.parseInt(id);
    }

    public String getShift() { return shift; }

    public void setNeed(int[] need) { this.need = need; }

    public int getNeed(int index) { return need[index]; }

    public int getId(){
        return id;
    }
}
