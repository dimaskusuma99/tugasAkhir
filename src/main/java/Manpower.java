public class Manpower {
    String shift; int monday; int tuesday; int wednesday; int thursday;
    int friday; int saturday; int sunday; int id;

    public Manpower(String shift, String monday, String tuesday, String wednesday, String thursday,
                 String friday, String saturday, String sunday, String id){
        this.shift = shift;
        this.monday = Integer.parseInt(monday);
        this.tuesday = Integer.parseInt(tuesday);
        this.wednesday = Integer.parseInt(wednesday);
        this.thursday = Integer.parseInt(thursday);
        this.friday = Integer.parseInt(friday);
        this.saturday = Integer.parseInt(saturday);
        this.sunday = Integer.parseInt(sunday);
        this.id = Integer.parseInt(id);
    }

    public String getShift() { return shift; }

    public int getMonday(){
        return monday;
    }

    public int getTuesday(){
        return tuesday;
    }

    public int getWednesday(){
        return wednesday;
    }

    public int getThursday(){
        return thursday;
    }

    public int getFriday(){
        return friday;
    }

    public int getSaturday(){
        return saturday;
    }

    public int getSunday(){
        return sunday;
    }

    public int getId(){
        return id;
    }
}
