import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import java.io.File;
import java.io.IOException;

import java.sql.Time;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.Arrays;

public class Schedule {
    static Employee[] emp;
    static Shift[] shiftx;
    static Manpower[] plan;
    static Constraint hard;
    static Row row;

    public static final String XLSX_FILE_PATH =
            "D:\\ITS\\Semester 8\\Tugas Akhir\\Nurse Rostering\\nurserost\\OpTur7.xls";

    public static void main(String[] args) throws IOException, InvalidFormatException, ParseException {
        String[][] employee = employees().clone();
        String[][] shift = shifts().clone();
        String[][] manpower = manpower().clone();
        String[] constraint = hardconstraint().clone();

        //OBJEK EMPLOYEE
        String[][] weekend = new String[employee.length][];
        for (int i = 0; i < employee.length; i++)
            weekend[i] = employee[i][2].split(", |,");
        int kanan = 0;
        for (int i = 0; i < weekend.length; i++) {
            if (kanan < weekend[i].length)
                kanan = weekend[i].length;
        }
        int[][] week = new int[weekend.length][kanan];
        for (int i = 0; i < weekend.length; i++){
            for (int j = 0; j < weekend[i].length; j++){
                week[i][j] = Integer.parseInt(weekend[i][j]);
//                System.out.print(week[i][j] + " ");
            }
//            System.out.println();
        }

        emp = new Employee[employee.length];
        for (int i = 0; i < employee.length; i++){
            emp[i]= new Employee(employee[i][0], employee[i][1], week[i], employee[i][3]);
//            System.out.println(emp[i].getWeek());
//            System.out.println(Arrays.toString(week[i]));
        }


        //OBJEK SHIFT
        DateFormat time = new SimpleDateFormat("hh:mm");
        LocalTime[][] times = new LocalTime[shift.length][2];
        for (int i = 0; i < shift.length; i++) {
            String first = shift[i][10]+":"+shift[i][11];
            String last = shift[i][12]+":"+shift[i][13];
            Time start = new Time(time.parse(first).getTime());
            Time finish = new Time(time.parse(last).getTime());
            LocalTime startx = start.toLocalTime();
            LocalTime finishx = finish.toLocalTime();
            times[i][0] = startx;
            times[i][1] = finishx;
//            LocalTime total = finishx.minusHours(startx.getHour()).plusMinutes(startx.getMinute());
//            System.out.println(total);
        }

        shiftx = new Shift[shift.length];
        for (int i = 0; i < shift.length; i++){
            shiftx[i]= new Shift(shift[i][0], shift[i][1], shift[i][2], shift[i][3], shift[i][4], shift[i][5],
                    shift[i][6], shift[i][7], shift[i][8], shift[i][9], times[i][0], times[i][1],
                    shift[i][14]);
        }
//        double workingHour = shiftx[0].getSunday() + shiftx[0].getMonday() + shiftx[0].getTuesday();
//        System.out.println(workingHour);


        //OBJEK MANPOWER
        plan = new Manpower[manpower.length];
        for (int i = 0; i < manpower.length; i++){
            plan[i]= new Manpower(manpower[i][0], manpower[i][1], manpower[i][2], manpower[i][3],
                    manpower[i][4], manpower[i][5], manpower[i][6], manpower[i][7], manpower[i][8]);
        }

        //OBJEK CONSTRAINT
        hard = new Constraint(constraint);
        hard.setHc7();
//        System.out.println(hard.getHc5a());

        int [][] matrixsol = new int[employee.length][42];

        for (int i = 0; i < 42; i++) {
            for (int j = 0; j < employee.length; j++){
                for (int k = 0; k < shift.length; k++){
                    if (checkHC2(matrixsol, i, k, j) && checkHC4Competence(k, j) && checkHC4Week(i,j) && checkHC7(matrixsol, j, i, k)) {
                        matrixsol[j][i] = k + 1;
                        break;
                    }
                }
            }
        }

        for (int i = 0; i < matrixsol.length; i++) {
            for (int j = 0; j < matrixsol[i].length; j++) {
                System.out.print(matrixsol[i][j] + " ");
            }
            System.out.println();
        }
//        for (int i = 0; i < shiftx.length ; i++) {
//            double total = shiftx[i].getMonday() + shiftx[i].getSunday() + shiftx[i].getTuesday() +
//                    shiftx[i].getThursday() + shiftx[i].getWednesday() + shiftx[i].getFriday() + shiftx[i].getSaturday();
//            System.out.println(total);
//        }

    }

    public static boolean checkHC3 (int solution [][], int employee, int day, int shift){
        double limit = 0;
        int week = day/7;



        return false;
    }

    public static boolean checkHC7 (int solution [][], int employee,  int day, int shift){
        double workingHours = 0;
        int week = day/7;

        for (int i = week * 7; i < (week+1)*7; i++) {
            if (solution[employee][i] != 0){
                if (day % 7 == 0)
                    workingHours += shiftx[solution[employee][i]-1].getMonday();
                if (day % 7 == 1)
                    workingHours += shiftx[solution[employee][i]-1].getTuesday();
                if (day % 7 == 2)
                    workingHours += shiftx[solution[employee][i]-1].getWednesday();
                if (day % 7 == 3)
                    workingHours += shiftx[solution[employee][i]-1].getThursday();
                if (day % 7 == 4)
                    workingHours += shiftx[solution[employee][i]-1].getFriday();
                if (day % 7 == 5)
                    workingHours += shiftx[solution[employee][i]-1].getSaturday();
                if (day % 7 == 6)
                    workingHours += shiftx[solution[employee][i]-1].getSunday();
            }
        }
        if (workingHours + shiftx[shift].getMonday() <= hard.getHc7())
            return true;
        if (workingHours + shiftx[shift].getTuesday() <= hard.getHc7())
            return true;
        if (workingHours + shiftx[shift].getWednesday() <= hard.getHc7())
            return true;
        if (workingHours + shiftx[shift].getThursday() <= hard.getHc7())
            return true;
        if (workingHours + shiftx[shift].getFriday() <= hard.getHc7())
            return true;
        if (workingHours + shiftx[shift].getSaturday() <= hard.getHc7())
            return true;
        if (workingHours + shiftx[shift].getSunday() <= hard.getHc7())
            return true;


//        double rata = 0;
//        for (int i = 0; i < emp.length; i++) {
//            rata = workingHours/6;
//            System.out.println(rata);
//            System.out.println(workingHours);
//        }

        return false;
    }

    public static int needs (int [][] solution, int shift, int day){
        int count = 0;
        for (int i = 0; i < solution.length; i++) {
            if (solution[i][day] == shift+1)
                count++;
        }
        return count;
    }

    public static boolean checkHC2(int [][] solution, int day, int shift, int employee){
        if (day % 7 == 0)
            if (needs(solution, shift, day) < plan[shift].getMonday())
                return true;
        if (day % 7 == 1)
            if (needs(solution, shift, day) < plan[shift].getTuesday())
                return true;
        if (day % 7 == 2)
            if (needs(solution, shift, day) < plan[shift].getWednesday())
                return true;
        if (day % 7 == 3)
            if (needs(solution, shift, day) < plan[shift].getThursday())
                return true;
        if (day % 7 == 4)
            if (needs(solution, shift, day) < plan[shift].getFriday())
                return true;
        if (day % 7 == 5)
            if (needs(solution, shift, day) < plan[shift].getSaturday())
                return true;
        if (day % 7 == 6)
            if (needs(solution, shift, day) < plan[shift].getSunday())
                return true;
        return false;
    }

    public static boolean checkHC4Competence (int shift, int employee){
        if(shiftx[shift].getCompetence().equals("A") && emp[employee].getCompetence().equals(""))
            return false;
        return true;
    }

    public static boolean checkHC4Week (int day, int employee){
        if(day%7 == 5 || day%7 == 6) {
            for (int i = 0; i < emp[employee].getWeek().length; i++) {
                if (day / 7 == emp[employee].getWeek()[i] - 1)
                    return true;
            } return false;
        }
        return true;
    }



    public static String[][] employees() throws IOException, InvalidFormatException {
        Workbook workbook = WorkbookFactory.create(new File(XLSX_FILE_PATH));
        Sheet sheet = workbook.getSheetAt(0);
        DataFormatter dataFormatter = new DataFormatter();

        int last = 0;
        int side = 0;
        int sidex = 0;
        int first = 5;

        for (int i = first; i <= sheet.getLastRowNum(); i++) {
            row = sheet.getRow(i);
            for (Cell cell: row) {
                side++;
            }
            if (sidex < side)
                sidex = side;
            side = 0;
            last++;
        }
        String [][] data = new String[last][sidex];
//        System.out.println(last + " " + sidex);
        last = 0;
        for (int i = first; i <= sheet.getLastRowNum(); i++) {
            row = sheet.getRow(i);
            for (Cell cell : row) {
                String cellValue = dataFormatter.formatCellValue(cell);
                data[last][side] = cellValue;
                side++;
            }
            side = 0;
            last++;
        } return data;
    }

    public static String[][] shifts() throws IOException, InvalidFormatException{
        Workbook workbook = WorkbookFactory.create(new File(XLSX_FILE_PATH));
        Sheet sheet = workbook.getSheetAt(1);
        DataFormatter dataFormatter = new DataFormatter();

        int last = 0;
        int side = 0;
        int sidex = 0;
        int first = 5;

        for (int i = first-1; i <= sheet.getLastRowNum(); i++) {
            row = sheet.getRow(i);
            for (Cell cell: row) {
                side++;
            }
            if (sidex < side)
                sidex = side;
            side = 0;
            last++;
        }
        String [][] data = new String[last][sidex];
//        System.out.println(lasta + " " + sidex);
        last = 0;
        for (int i = first-1; i <= sheet.getLastRowNum(); i++) {
            row = sheet.getRow(i);
            for (Cell cell : row) {
                String cellValue = dataFormatter.formatCellValue(cell);
                data[last][side] = cellValue;
                side++;
            }
            side = 0;
            last++;
        }
        return data;
    }

    public static String[][] manpower() throws IOException, InvalidFormatException {
        Workbook workbook = WorkbookFactory.create(new File(XLSX_FILE_PATH));
        Sheet sheet = workbook.getSheetAt(2);
        DataFormatter dataFormatter = new DataFormatter();

        int last = 0;
        int side = 0;
        int sidex = 0;
        int first = 5;

        for (int i = first; i <= sheet.getLastRowNum(); i++) {
            row = sheet.getRow(i);
            for (Cell cell: row) {
                side++;
            }
            if (sidex < side)
                sidex = side;
            side = 0;
            last++;
        }
        String [][] data = new String[last][sidex];
        //System.out.println(last + " " + sidex);
        last = 0;
        for (int i = first; i <= sheet.getLastRowNum(); i++) {
            row = sheet.getRow(i);
            for (Cell cell : row) {
                String cellValue = dataFormatter.formatCellValue(cell);
                data[last][side] = cellValue;
                side++;
            }
            side = 0;
            last++;
        } return data;
    }

    public static String[] hardconstraint() throws IOException, InvalidFormatException {
        Workbook workbook = WorkbookFactory.create(new File(XLSX_FILE_PATH));
        Sheet sheet = workbook.getSheetAt(3);
        DataFormatter dataFormatter = new DataFormatter();

        int last = 0;
        int side = 0;
        int sidex = 0;
        int first = 5;
        for (int i = first-1; i <= 15; i++) {
            row = sheet.getRow(i);
            for (Cell cell: row) {
                side++;
            }
            if (sidex < side) {
                sidex = side;
//                System.out.println(sidex);
            }
            side = 0;
            last++;
        }
        String [][] data = new String[last][sidex];
//        System.out.println(last + " " + sidex);
        last = 0;
        for (int i = first-1; i <= 15; i++) {
            row = sheet.getRow(i);
            for (Cell cell : row) {
                String cellValue = dataFormatter.formatCellValue(cell);
                data[last][side] = cellValue;
                side++;
            }
            side = 0;
            last++;
        }

        String [] cons = new String[data.length];
        for (int i = 0; i < data.length; i++) {
            cons[i] = data[i][3];
        }
        return cons;
    }

    public static Constraint[] softconstraint() throws IOException, InvalidFormatException {
        Workbook workbook = WorkbookFactory.create(new File(XLSX_FILE_PATH));
        Sheet sheet = workbook.getSheetAt(3);
        DataFormatter dataFormatter = new DataFormatter();

        int last = 0;
        int side = 0;
        int sidex = 0;
        int first = 5;

        for (int i = first+13; i <= sheet.getLastRowNum(); i++) {
            row = sheet.getRow(i);
            for (Cell cell: row) {
                side++;
            }
            if (sidex < side)
                sidex = side;
            side = 0;
            last++;
        }
        String [][] data = new String[last][sidex];
        System.out.println(last + " " + sidex);
        last = 0;
        for (int i = first+13; i <= sheet.getLastRowNum(); i++) {
            row = sheet.getRow(i);
            for (Cell cell : row) {
                String cellValue = dataFormatter.formatCellValue(cell);
                data[last][side] = cellValue;
                side++;
            }
            side = 0;
            last++;
        }
        Constraint[] soft = new Constraint[last];
        for (int i = 0; i < soft.length; i++){
//            soft[i]= new Constraint(data[i][0], data[i][1], data[i][2], data[i][3]);
//            Row hc1 = sheet.getRow(1);
//            System.out.println(hc1);
//            System.out.println(soft[i].getValue());
        }
        return soft;
    }

    public static void wantedpattern() throws IOException, InvalidFormatException {
        Workbook workbook = WorkbookFactory.create(new File(XLSX_FILE_PATH));
        Sheet sheet = workbook.getSheetAt(4);
        DataFormatter dataFormatter = new DataFormatter();

        int last = 0;
        int side = 0;
        int sidex = 0;
        int first = 5;

        for (int i = first-1; i <= 12; i++) {
            row = sheet.getRow(i);
            for (Cell cell: row) {
                side++;
            }
            if (sidex < side)
                sidex = side;
            side = 0;
            last++;
        }
        String [][] data = new String[last][sidex];
//        System.out.println(last + " " + sidex);
        last = 0;
        for (int i = first-1; i <= 12; i++) {
            row = sheet.getRow(i);
            for (Cell cell : row) {
                String cellValue = dataFormatter.formatCellValue(cell);
                data[last][side] = cellValue;
                side++;
            }
            side = 0;
            last++;
        }

        String [][] wantedpattern = new String[data.length][];
        for(int i=0; i < data.length; i++)
            wantedpattern[i] = data[i][2].split(", |,");
        int kanan = 0;
        for(int i=0; i < wantedpattern.length; i++){
            if (kanan < wantedpattern[i].length)
                kanan = wantedpattern[i].length;
        }
        String [][] wp = new String[wantedpattern.length][kanan];
        for(int i=0; i < wantedpattern.length; i++) {
            for (int j = 0; j < wantedpattern[i].length; j++) {
                wp[i][j] = wantedpattern[i][j];
                if (wp[i][0].isEmpty())
                    break;
                System.out.print(wp[i][j] + "\t");
            }
            System.out.println();
        }
        Pattern[] wanted = new Pattern[last];
        for (int i = 0; i < wanted.length; i++){
            wanted[i]= new Pattern(data[i][0], data[i][1], wp[i]);
//            System.out.println(wanted[i].getPattern());
        }
    }
}