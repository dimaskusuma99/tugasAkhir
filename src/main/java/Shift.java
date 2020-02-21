import java.time.LocalTime;

public class Shift {
    int id; double monday; double tuesday; double wednesday; double thursday;
    double friday; double saturday; double sunday; int category; String name;
    LocalTime start; LocalTime finish; String competence;

    public Shift(String id, String monday, String tuesday, String wednesday, String thursday,
            String friday, String saturday, String sunday, String category, String name, LocalTime start,
            LocalTime finish, String competence){
        this.id = Integer.parseInt(id);
        this.monday = Double.parseDouble(monday);
        this.tuesday = Double.parseDouble(tuesday);
        this.wednesday = Double.parseDouble(wednesday);
        this.thursday = Double.parseDouble(thursday);
        this.friday = Double.parseDouble(friday);
        this.saturday = Double.parseDouble(saturday);
        this.sunday = Double.parseDouble(sunday);
        this.category = Integer.parseInt(category);
        this.name = name;
        this.start = start;
        this.finish = finish;
        this.competence = competence;
    }

    public int getId() { return id; }

    public double getMonday(){
        return monday;
    }

    public double getTuesday(){
        return tuesday;
    }

    public double getWednesday(){
        return wednesday;
    }

    public double getThursday(){
        return thursday;
    }

    public double getFriday(){
        return friday;
    }

    public double getSaturday(){
        return saturday;
    }

    public double getSunday(){
        return sunday;
    }

    public int getCategory(){
        return category;
    }

    public String getName(){
        return name;
    }

    public String getCompetence(){
        return competence;
    }
}
