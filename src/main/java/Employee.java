public class Employee {

    int id;
    double hours;
    int[] week;
    String competence;

    public Employee(String id, String hours, int[] week, String competence){
        this.id = Integer.parseInt(id);
        this.hours = Double.parseDouble(hours);
        this.week = week;
        this.competence = competence;
    }

    public int getId() { return id; }

    public double getHours(){
        return hours;
    }

    public String getCompetence(){
        return competence;
    }
}