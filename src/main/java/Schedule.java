import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;

import javax.sound.midi.Soundbank;
import javax.xml.bind.SchemaOutputResolver;
import java.io.File;
import java.io.IOException;

import java.sql.Time;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.*;

public class Schedule {
    static Employee[] emp;
    static Shift[] shiftx;
    static Manpower[] plan;
    static Constraint hard;
    static Row row;
    static int [][] noPairWeekday;
    static int [][] noPairWeekend;
    static int [][] week;
    static double workingHours;

    public static final String XLSX_FILE_PATH =
            "D:\\ITS\\Semester 8\\Tugas Akhir\\Nurse Rostering\\nurserost\\OpTur7.xls";

//    static int planningHorizon = Collections.max(Arrays.asList())

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
        week = new int[weekend.length][kanan];
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
        }

        shiftx = new Shift[shift.length];
        for (int i = 0; i < shift.length; i++){
            shiftx[i]= new Shift(shift[i][0], shift[i][1], shift[i][2], shift[i][3], shift[i][4], shift[i][5],
                    shift[i][6], shift[i][7], shift[i][8], shift[i][9], times[i][0], times[i][1],
                    shift[i][14]);
        }

        //OBJEK MANPOWER
        plan = new Manpower[manpower.length];
        for (int i = 0; i < manpower.length; i++){
            plan[i]= new Manpower(manpower[i][0], manpower[i][1], manpower[i][2], manpower[i][3],
                    manpower[i][4], manpower[i][5], manpower[i][6], manpower[i][7], manpower[i][8]);
        }

        //OBJEK CONSTRAINT
        hard = new Constraint(constraint);
        hard.setHc3();
        hard.setHc7();
        hard.setHc5a();
        hard.setHc5b();
        hard.setHc5c();
        hard.setHc5d();
        hard.setHc5e();
        hard.setHc5f();

        //MEMBUAT PASANGAN SHIFT YANG DILARANG
        int count = 0;
        for (int i = 0; i < shiftx.length; i++) {
            for (int j = 0; j < shiftx.length; j++) {
                if (shiftx[i].getStart().compareTo(shiftx[j].getFinish()) < 0) {
                    if (shiftx[i].getCategory() == 3 && shiftx[j].getCategory() == 2)
                        if (hard.getHc5a().compareTo(shiftx[j].getStart().minusHours(shiftx[i].getFinish().getHour()).minusMinutes(shiftx[i].getFinish().getMinute())) > 0)
                            count++;
                    if (shiftx[i].getCategory() == 2 && shiftx[j].getCategory() == 1)
                        if (hard.getHc5c().compareTo(shiftx[j].getStart().minusHours(shiftx[i].getFinish().getHour()).minusMinutes(shiftx[i].getFinish().getMinute())) > 0)
                            count++;
                    if (shiftx[i].getCategory() == 3 && shiftx[j].getCategory() == 1)
                        if (hard.getHc5e().compareTo(shiftx[j].getStart().minusHours(shiftx[i].getFinish().getHour()).minusMinutes(shiftx[i].getFinish().getMinute())) > 0)
                            count++;
                } else if (shiftx[i].getStart().compareTo(shiftx[j].getFinish()) > 0) {
                    if (shiftx[i].getCategory() == 3 && shiftx[j].getCategory() == 2)
                        if (hard.getHc5a().compareTo(shiftx[i].getStart().minusHours(shiftx[j].getFinish().getHour()).minusMinutes(shiftx[j].getFinish().getMinute())) > 0)
                            count++;
                    if (shiftx[i].getCategory() == 2 && shiftx[j].getCategory() == 1)
                        if (hard.getHc5c().compareTo(shiftx[i].getStart().minusHours(shiftx[j].getFinish().getHour()).minusMinutes(shiftx[j].getFinish().getMinute())) > 0)
                            count++;
                    if (shiftx[i].getCategory() == 3 && shiftx[j].getCategory() == 1)
                        if (hard.getHc5e().compareTo(shiftx[i].getStart().minusHours(shiftx[j].getFinish().getHour()).minusMinutes(shiftx[j].getFinish().getMinute())) > 0)
                            count++;
                }
            }
        }
        noPairWeekday = new int[count][2];
        count = 0;
        for (int i = 0; i < shiftx.length; i++) {
            for (int j = 0; j < shiftx.length; j++) {
                if (shiftx[i].getStart().compareTo(shiftx[j].getFinish()) < 0) {
                    if (shiftx[i].getCategory() == 3 && shiftx[j].getCategory() == 2)
                        if (hard.getHc5a().compareTo(shiftx[j].getStart().minusHours(shiftx[i].getFinish().getHour()).minusMinutes(shiftx[i].getFinish().getMinute())) > 0) {
                            noPairWeekday[count][0] = i + 1;
                            noPairWeekday[count][1] = j + 1;
                            count++;
                        }
                    if (shiftx[i].getCategory() == 2 && shiftx[j].getCategory() == 1)
                        if (hard.getHc5c().compareTo(shiftx[j].getStart().minusHours(shiftx[i].getFinish().getHour()).minusMinutes(shiftx[i].getFinish().getMinute())) > 0) {
                            noPairWeekday[count][0] = i + 1;
                            noPairWeekday[count][1] = j + 1;
                            count++;
                        }
                    if (shiftx[i].getCategory() == 3 && shiftx[j].getCategory() == 1)
                        if (hard.getHc5e().compareTo(shiftx[j].getStart().minusHours(shiftx[i].getFinish().getHour()).minusMinutes(shiftx[i].getFinish().getMinute())) > 0) {
                            noPairWeekday[count][0] = i + 1;
                            noPairWeekday[count][1] = j + 1;
                            count++;
                        }
                } else if (shiftx[i].getStart().compareTo(shiftx[j].getFinish()) > 0) {
                    if (shiftx[i].getCategory() == 3 && shiftx[j].getCategory() == 2)
                        if (hard.getHc5a().compareTo(shiftx[i].getStart().minusHours(shiftx[j].getFinish().getHour()).minusMinutes(shiftx[j].getFinish().getMinute())) > 0) {
                            noPairWeekday[count][0] = i + 1;
                            noPairWeekday[count][1] = j + 1;
                            count++;
                        }
                    if (shiftx[i].getCategory() == 2 && shiftx[j].getCategory() == 1)
                        if (hard.getHc5c().compareTo(shiftx[i].getStart().minusHours(shiftx[j].getFinish().getHour()).minusMinutes(shiftx[j].getFinish().getMinute())) > 0) {
                            noPairWeekday[count][0] = i + 1;
                            noPairWeekday[count][1] = j + 1;
                            count++;
                        }
                    if (shiftx[i].getCategory() == 3 && shiftx[j].getCategory() == 1)
                        if (hard.getHc5e().compareTo(shiftx[i].getStart().minusHours(shiftx[j].getFinish().getHour()).minusMinutes(shiftx[j].getFinish().getMinute())) > 0) {
                            noPairWeekday[count][0] = i + 1;
                            noPairWeekday[count][1] = j + 1;
                            count++;
                        }
                }
            }
        }

        count = 0;
        for (int i = 0; i < shiftx.length; i++) {
            for (int j = 0; j < shiftx.length; j++) {
                if (shiftx[i].getStart().compareTo(shiftx[j].getFinish()) < 0) {
                    if (shiftx[i].getCategory() == 3 && shiftx[j].getCategory() == 2)
                        if (hard.getHc5b().compareTo(shiftx[j].getStart().minusHours(shiftx[i].getFinish().getHour()).minusMinutes(shiftx[i].getFinish().getMinute())) > 0)
                            count++;
                    if (shiftx[i].getCategory() == 2 && shiftx[j].getCategory() == 1)
                        if (hard.getHc5d().compareTo(shiftx[j].getStart().minusHours(shiftx[i].getFinish().getHour()).minusMinutes(shiftx[i].getFinish().getMinute())) > 0)
                            count++;
                    if (shiftx[i].getCategory() == 3 && shiftx[j].getCategory() == 1)
                        if (hard.getHc5f().compareTo(shiftx[j].getStart().minusHours(shiftx[i].getFinish().getHour()).minusMinutes(shiftx[i].getFinish().getMinute())) > 0)
                            count++;
                } else if (shiftx[i].getStart().compareTo(shiftx[j].getFinish()) > 0) {
                    if (shiftx[i].getCategory() == 3 && shiftx[j].getCategory() == 2)
                        if (hard.getHc5b().compareTo(shiftx[i].getStart().minusHours(shiftx[j].getFinish().getHour()).minusMinutes(shiftx[j].getFinish().getMinute())) > 0)
                            count++;
                    if (shiftx[i].getCategory() == 2 && shiftx[j].getCategory() == 1)
                        if (hard.getHc5d().compareTo(shiftx[i].getStart().minusHours(shiftx[j].getFinish().getHour()).minusMinutes(shiftx[j].getFinish().getMinute())) > 0)
                            count++;
                    if (shiftx[i].getCategory() == 3 && shiftx[j].getCategory() == 1)
                        if (hard.getHc5f().compareTo(shiftx[i].getStart().minusHours(shiftx[j].getFinish().getHour()).minusMinutes(shiftx[j].getFinish().getMinute())) > 0)
                            count++;
                }
            }
        }
        noPairWeekend = new int[count][2];
        count = 0;
        for (int i = 0; i < shiftx.length; i++) {
            for (int j = 0; j < shiftx.length; j++) {
                if (shiftx[i].getStart().compareTo(shiftx[j].getFinish()) < 0) {
                    if (shiftx[i].getCategory() == 3 && shiftx[j].getCategory() == 2)
                        if (hard.getHc5a().compareTo(shiftx[j].getStart().minusHours(shiftx[i].getFinish().getHour()).minusMinutes(shiftx[i].getFinish().getMinute())) > 0) {
                            noPairWeekend[count][0] = i + 1;
                            noPairWeekend[count][1] = j + 1;
                            count++;
                        }
                    if (shiftx[i].getCategory() == 2 && shiftx[j].getCategory() == 1)
                        if (hard.getHc5c().compareTo(shiftx[j].getStart().minusHours(shiftx[i].getFinish().getHour()).minusMinutes(shiftx[i].getFinish().getMinute())) > 0) {
                            noPairWeekend[count][0] = i + 1;
                            noPairWeekend[count][1] = j + 1;
                            count++;
                        }
                    if (shiftx[i].getCategory() == 3 && shiftx[j].getCategory() == 1)
                        if (hard.getHc5e().compareTo(shiftx[j].getStart().minusHours(shiftx[i].getFinish().getHour()).minusMinutes(shiftx[i].getFinish().getMinute())) > 0) {
                            noPairWeekend[count][0] = i + 1;
                            noPairWeekend[count][1] = j + 1;
                            count++;
                        }
                } else if (shiftx[i].getStart().compareTo(shiftx[j].getFinish()) > 0) {
                    if (shiftx[i].getCategory() == 3 && shiftx[j].getCategory() == 2)
                        if (hard.getHc5a().compareTo(shiftx[i].getStart().minusHours(shiftx[j].getFinish().getHour()).minusMinutes(shiftx[j].getFinish().getMinute())) > 0) {
                            noPairWeekend[count][0] = i + 1;
                            noPairWeekend[count][1] = j + 1;
                            count++;
                        }
                    if (shiftx[i].getCategory() == 2 && shiftx[j].getCategory() == 1)
                        if (hard.getHc5c().compareTo(shiftx[i].getStart().minusHours(shiftx[j].getFinish().getHour()).minusMinutes(shiftx[j].getFinish().getMinute())) > 0) {
                            noPairWeekend[count][0] = i + 1;
                            noPairWeekend[count][1] = j + 1;
                            count++;
                        }
                    if (shiftx[i].getCategory() == 3 && shiftx[j].getCategory() == 1)
                        if (hard.getHc5e().compareTo(shiftx[i].getStart().minusHours(shiftx[j].getFinish().getHour()).minusMinutes(shiftx[j].getFinish().getMinute())) > 0) {
                            noPairWeekend[count][0] = i + 1;
                            noPairWeekend[count][1] = j + 1;
                            count++;
                        }
                }
            }
        }

        //ASSIGN WEEKEND TERLEBIH DAHULLU
        int [][] matrixsol = new int[employee.length][planningHorizon(week)*7];

        for (int i = 0; i < planningHorizon(week)*7; i++) {
            if (i%7 == 5 || i%7 == 6) {
            for (int j = 0; j < employee.length; j++) {
                for (int k = 1; k <= shift.length; k++) {
                    if (checkHC2(matrixsol, i, k, j) && checkHC4Competence(k, j) && checkHC4Week(i, j)) {
                        matrixsol[j][i] = k;
                        break;
                        }
                    }
                }
            }
        }

        //ASSIGN WEEKDAYS
        for (int i = 0; i < planningHorizon(week)*7; i++) {
            for (int j = 0; j < employee.length; j++){
                for (int k = 1; k <= shift.length; k++){
                    if (checkHC2(matrixsol, i, k, j) && checkHC7(matrixsol, j, i, k)
                            && checkHC5(matrixsol, j, k, i)) {
                        matrixsol[j][i] = k;
                        break;
                    }
                }
            }
        }

        //PRINT SOLUSI
//        for (int i = 0; i < matrixsol.length; i++) {
//            for (int j = 0; j < matrixsol[i].length; j++) {
//                System.out.print(matrixsol[i][j] + " ");
//            }
//            System.out.println();
//        }
        int [][] matrixsolTemp = new int [matrixsol.length][];
        for (int i = 0; i < matrixsol.length; i++) {
            matrixsolTemp[i] = matrixsol[i].clone();
        }

        //COBA SOLVE
//        for (int iterasi = 0; iterasi < 50000; iterasi++) {
        do {
            threeExchange(matrixsolTemp);
            if (checkRandomShift(matrixsolTemp)) {
                for (int i = 0; i < matrixsolTemp.length; i++) {
                    for (int j = 0; j < matrixsolTemp[i].length; j++) {
                        matrixsol[i][j] = matrixsolTemp[i][j];
                    }
                }
                System.out.println("benar");
            }
            else {
                for (int i = 0; i < matrixsolTemp.length; i++) {
                    for (int j = 0; j < matrixsolTemp[i].length; j++) {
                        matrixsolTemp[i][j] = matrixsol[i][j];
                    }
                }
                System.out.println("salah");
            }
        } while (validHC3(matrixsol) == false);

//        threeExchange(matrixsolTemp);
//            if (checkRandomShift(matrixsolTemp)) {
//                for (int i = 0; i < matrixsolTemp.length; i++) {
//                    for (int j = 0; j < matrixsolTemp[i].length; j++) {
//                        matrixsol[i][j] = matrixsolTemp[i][j];
//                    }
//                }
//                System.out.println("benar");
//            }
//            else {
//                for (int i = 0; i < matrixsolTemp.length; i++) {
//                    for (int j = 0; j < matrixsolTemp[i].length; j++) {
//                        matrixsolTemp[i][j] = matrixsol[i][j];
//                    }
//                }
//                System.out.println("salah");
//            }

//        System.out.println("SOL");
        for (int i = 0; i < matrixsol.length; i++) {
            for (int j = 0; j < matrixsol[i].length; j++) {
                System.out.print(matrixsol[i][j] + " ");
            }
            System.out.println();
        }
//        //CEK SUMHOURS
//        double[] hours = sumHours(matrixsol, 2).clone();
//        for (int i = 0; i < hours.length; i++) {
//            System.out.println(hours[i]);
//        }
//        System.out.println(Arrays.toString(avgHours(matrixsol, 6)));
//        for(int i=0; i<42; i++)
//            for(int j=1; j<=6; j++)
    }


    private static int random (int number) {
        Random random = new Random();
        return random.nextInt(number);
    }

    public static boolean checkRandomShift(int [][] solution) {
        if (validHC2(solution) && validHC4Competence(solution) && validHC5(solution) && validHC7(solution))
            return true;
        else
            return false;
    }

    public static int [][] twoExchange(int [][] solution) {
        int [][] matrixsolTemp = solution;
        int shift1; int shift2;

        int randomEmp1 = random(solution.length);
        int randomEmp2 = random(solution.length);
        while (randomEmp2 == randomEmp1) {
            randomEmp2 = random(solution.length);
        }
        int randomDay = random(planningHorizon(week)*7);
        while (randomDay % 7 == 5 || randomDay % 7 == 6) {
            randomDay = random(planningHorizon(week) * 7);
        }

        shift1 = solution[randomEmp1][randomDay];
        shift2 = solution[randomEmp2][randomDay];
        matrixsolTemp[randomEmp1][randomDay] = shift2;
        matrixsolTemp[randomEmp2][randomDay] = shift1;

        return matrixsolTemp;
    }

    public static int [][] threeExchange(int [][] solution) {
        int [][] matrixsolTemp = solution;
        int shift1; int shift2; int shift3;
        int randomEmp1; int randomEmp2; int randomEmp3;

        do {
            randomEmp1 = random(solution.length);
            randomEmp2 = random(solution.length);
            randomEmp3 = random(solution.length);
        } while (randomEmp2 == randomEmp1 || randomEmp3 == randomEmp2 || randomEmp1 == randomEmp3);

        int randomDay = random(planningHorizon(week)*7);
        while (randomDay % 7 == 5 || randomDay % 7 == 6) {
            randomDay = random(planningHorizon(week) * 7);
        }

        shift1 = solution[randomEmp1][randomDay];
        shift2 = solution[randomEmp2][randomDay];
        shift3 = solution[randomEmp3][randomDay];

        matrixsolTemp[randomEmp1][randomDay] = shift2;
        matrixsolTemp[randomEmp2][randomDay] = shift3;
        matrixsolTemp[randomEmp3][randomDay] = shift1;

        return matrixsolTemp;
    }

    public static int [][] doubleExchange(int [][] solution) {
        int [][] matrixsolTemp = solution;
        int shift1; int shift2;

        int randomEmp1 = random(solution.length);
        int randomEmp2 = random(solution.length);
        while (randomEmp2 == randomEmp1) {
            randomEmp2 = random(solution.length);
        }
        int randomDay1 = random(planningHorizon(week)*7);
        int randomDay2 = random(planningHorizon(week)*7);
        if (randomDay1 % 7 == 5 || randomDay1 % 7 == 6 || randomDay2 % 7 == 5 || randomDay2 % 7 == 6) {
            randomDay1 = random(planningHorizon(week) * 7);
            randomDay2 = random(planningHorizon(week) * 7);
        }

        shift1 = solution[randomEmp1][randomDay1];
        shift2 = solution[randomEmp2][randomDay2];
        matrixsolTemp[randomEmp1][randomDay1] = shift2;
        matrixsolTemp[randomEmp2][randomDay2] = shift1;

        return matrixsolTemp;
    }

    public static int planningHorizon(int[][] arr) {
        if ((arr.length == 0)||(arr[0].length == 0)) {
            throw new IllegalArgumentException("Empty array");
        }
        int max = arr[0][0];
        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < arr[i].length; j++) {
                if (arr[i][j] > max) {
                    max = arr[i][j];
                }
            }
        }
        return max;
    }

    public static boolean checkHC5 (int [][] solution, int employee, int shift, int day){
        if(day==0)
            return true;
        if(day % 7 == 5 || day % 7 == 6) {
            for (int i = 0; i < noPairWeekend.length; i++) {
                if(solution[employee][day-1] == noPairWeekend[i][0] && shift == noPairWeekend[i][1])
                    return false;
                if (day < (planningHorizon(week)*7)-1)
                    if(solution[employee][day+1] == noPairWeekend[i][1] && shift == noPairWeekend[i][0])
                        return false;
            }
        }
        if (day % 7 == 0 || day % 7 == 1 || day % 7 == 2 || day % 7 == 3 || day % 7 == 4) {
            for (int i = 0; i < noPairWeekday.length; i++) {
                if (solution[employee][day-1] == noPairWeekday[i][0] && shift == noPairWeekday[i][1])
                    return false;
                if (day < (planningHorizon(week)*7)-1)
                    if(solution[employee][day+1] == noPairWeekday[i][1] && shift == noPairWeekday[i][0])
                        return false;
            }
        }
        return true;
    }

    public static boolean validHC5 (int [][] solution) {
        for (int i = 0; i < planningHorizon(week)*7; i++) {
            for (int j = 0; j < emp.length; j++) {
                if (i % 7 == 5 || i % 7 == 6) {
                    for (int k = 0; k < noPairWeekday.length; k++) {
                        if (i < (planningHorizon(week)*7) - 1)
                            if (solution[j][i] == noPairWeekday[k][0] && solution[j][(i+1)] == noPairWeekday[k][1])
                                return false;
                    }
                }
                else {
                    for (int k = 0; k < noPairWeekend.length; k++) {
                        if (i < (planningHorizon(week)*7) - 1)
                            if (solution[j][i] == noPairWeekend[k][0] && solution[j][(i+1)] ==  noPairWeekend[k][1])
                                return false;
                    }
                }
            }
        }
        return true;
    }

    public static double[] sumHours (int[][] solution, int week){
        double [] weekHours = new double[emp.length];
        workingHours = 0;

        for (int i = 0; i < emp.length; i++) {
            for (int j = week*7; j < (week+1)*7; j++) {
                if (solution [i][j] != 0) {
                    if (j % 7 == 0)
                        weekHours[i] = weekHours [i] + shiftx[solution[i][j]-1].getMonday();
                    if (j % 7 == 1)
                        weekHours[i] = weekHours [i] + shiftx[solution[i][j]-1].getTuesday();
                    if (j % 7 == 2)
                        weekHours[i] = weekHours [i] + shiftx[solution[i][j]-1].getWednesday();
                    if (j % 7 == 3)
                        weekHours[i] = weekHours [i] + shiftx[solution[i][j]-1].getThursday();
                    if (j % 7 == 4)
                        weekHours[i] = weekHours [i] + shiftx[solution[i][j]-1].getFriday();
                    if (j % 7 == 5)
                        weekHours[i] = weekHours [i] + shiftx[solution[i][j]-1].getSaturday();
                    if (j % 7 == 6)
                        weekHours[i] = weekHours [i] + shiftx[solution[i][j]-1].getSaturday();
                }
            }
        }
        return weekHours;
    }

    public static double [] avgHours (int [][] solution, int week){
        double [] avg = new double[emp.length];

        for (int i = 0; i < week; i++) {
            for (int j = 0; j < emp.length; j++) {
                avg [j] = avg[j] + sumHours(solution, i)[j];
            }
        }
        for (int i = 0; i < emp.length; i++) {
            avg [i] = (avg[i]/week);
        }
        return avg;
    }

    public static boolean validHC3 (int [][] solution){
        double [] upperLimit = new double[emp.length];
        double [] lowerLimit = new double[emp.length];
        for (int i=0; i<emp.length; i++) {
            upperLimit[i] = emp[i].getHours() + emp[i].getHours()*hard.getHc3();
            lowerLimit[i] = emp[i].getHours() - emp[i].getHours()*hard.getHc3();
        }

        for (int i = 0; i < emp.length ; i++) {
            for (int j = 0; j < planningHorizon(week); j++) {
                if (avgHours(solution, j)[i] > upperLimit[i])
                    return false;
                if (avgHours(solution, j)[i] < lowerLimit[i])
                    return false;
            }
        }
        return true;
    }

    public static boolean checkHC7 (int solution [][], int employee,  int day, int shift){
        workingHours = 0;
        int week = day/7;

        for (int i = week * 7; i < (week+1)*7; i++) {
            if (solution[employee][i] != 0){
                if (day % 7 == 0)
                    workingHours += shiftx[solution[employee][i] - 1].getMonday();
                if (day % 7 == 1)
                    workingHours += shiftx[solution[employee][i] - 1].getTuesday();
                if (day % 7 == 2)
                    workingHours += shiftx[solution[employee][i] - 1].getWednesday();
                if (day % 7 == 3)
                    workingHours += shiftx[solution[employee][i] - 1].getThursday();
                if (day % 7 == 4)
                    workingHours += shiftx[solution[employee][i] - 1].getFriday();
                if (day % 7 == 5)
                    workingHours += shiftx[solution[employee][i] - 1].getSaturday();
                if (day % 7 == 6)
                    workingHours += shiftx[solution[employee][i] - 1].getSunday();
            }
        }
        if (workingHours + shiftx[shift-1].getMonday() <= hard.getHc7())
            return true;
        if (workingHours + shiftx[shift-1].getTuesday() <= hard.getHc7())
            return true;
        if (workingHours + shiftx[shift-1].getWednesday() <= hard.getHc7())
            return true;
        if (workingHours + shiftx[shift-1].getThursday() <= hard.getHc7())
            return true;
        if (workingHours + shiftx[shift-1].getFriday() <= hard.getHc7())
            return true;
        if (workingHours + shiftx[shift-1].getSaturday() <= hard.getHc7())
            return true;
        if (workingHours + shiftx[shift-1].getSunday() <= hard.getHc7())
            return true;
        return false;
    }

    public static boolean validHC7 (int [][] solution) {
        for (int i = 0; i < planningHorizon(week); i++) {
            for (int j = 0; j < emp.length; j++) {
                if(sumHours(solution,i)[j] > hard.getHc7())
                    return false;
            }
        }
        return true;
    }

    public static int needs (int [][] solution, int shift, int day){
        int count = 0;
        for (int i = 0; i < solution.length; i++) {
            if (solution[i][day] == shift)
                count++;
        }
        return count;
    }

    public static boolean checkHC2(int [][] solution, int day, int shift, int employee){
        if (day % 7 == 0)
            if (needs(solution, shift, day) < plan[shift-1].getMonday())
                return true;
        if (day % 7 == 1)
            if (needs(solution, shift, day) < plan[shift-1].getTuesday())
                return true;
        if (day % 7 == 2)
            if (needs(solution, shift, day) < plan[shift-1].getWednesday())
                return true;
        if (day % 7 == 3)
            if (needs(solution, shift, day) < plan[shift-1].getThursday())
                return true;
        if (day % 7 == 4)
            if (needs(solution, shift, day) < plan[shift-1].getFriday())
                return true;
        if (day % 7 == 5)
            if (needs(solution, shift, day) < plan[shift-1].getSaturday())
                return true;
        if (day % 7 == 6)
            if (needs(solution, shift, day) < plan[shift-1].getSunday())
                return true;
        return false;
    }

    public static boolean validHC2 (int [][] solution) {
        for (int i = 0; i < planningHorizon(week)*7; i++) {
            for (int j = 0; j < shiftx.length; j++) {
                if (i % 7 == 0)
                    if (plan[j].getMonday() != needs(solution, j+1, i)) {
                        return false;
                    }
                if (i % 7 == 1)
                    if (plan[j].getTuesday() != needs(solution, j+1, i))
                        return false;
                if (i % 7 == 2)
                    if (plan[j].getWednesday() != needs(solution, j+1, i))
                        return false;
                if (i % 7 == 3)
                    if (plan[j].getThursday() != needs(solution, j+1, i))
                        return false;
                if (i % 7 == 4)
                    if (plan[j].getFriday() != needs(solution, j+1, i))
                        return false;
                if (i % 7 == 5)
                    if (plan[j].getSaturday() != needs(solution, j+1, i))
                        return false;
                if (i % 7 == 6)
                    if (plan[j].getSunday() != needs(solution, j+1, i))
                        return false;
            }
        }
        return true;
    }

    public static boolean checkHC4Competence (int shift, int employee) {
        if(shiftx[shift-1].getCompetence().equals("A") && emp[employee].getCompetence().equals(""))
            return false;
        return true;
    }

    public static boolean validHC4Competence (int[][] solution) {
        for (int i = 0; i < solution.length; i++) {
            for (int j = 0; j < solution[i].length; j++) {
                if (solution[i][j] != 0)
                    if (shiftx[solution[i][j]-1].getCompetence().equals("A") && emp[i].getCompetence().equals(""))
                        return false;
            }
        }
        return true;
    }

    public static boolean checkHC4Week (int day, int employee) {
        if(day%7 == 5 || day%7 == 6) {
            for (int i = 0; i < emp[employee].getWeek().length; i++) {
                if (day / 7 == emp[employee].getWeek()[i] - 1)
                    return true;
            } return false;
        }
        return true;
    }

    public static boolean validHC4Week (int [][] solution, int employee) {
        for (int i = 0; i < planningHorizon(week)*7; i++) {
            if(i % 7 == 5 || i % 7 == 6)
                if (solution[employee][i] != 0)
                    for (int j = 0; j < emp[employee].getWeek().length; j++) {
                        if (i / 7 != emp[employee].getWeek()[j] - 1)
                            return false;
                    }return true;
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