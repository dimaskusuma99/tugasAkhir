import java.time.LocalTime;

public class Shift {
    int id; double [] duration; int category; String name;
    LocalTime start; LocalTime finish; String competence;

    public Shift(String id, double [] duration, String category, String name, LocalTime start,
            LocalTime finish, String competence){
        this.id = Integer.parseInt(id);
        this.duration = duration;
        this.category = Integer.parseInt(category);
        this.name = name;
        this.start = start;
        this.finish = finish;
        this.competence = competence;
    }

    public int getId() { return id; }

    public double getDuration(int index) { return duration[index];}

    public int getCategory(){
        return category;
    }

    public String getName(){
        return name;
    }

    public LocalTime getStart() {
        return start;
    }

    public LocalTime getFinish() {
        return finish;
    }

    public String getCompetence(){
        return competence;
    }
}
