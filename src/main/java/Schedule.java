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
    static Constraint soft;
    static Row row;
    static int [][] noPairWeekday;
    static int [][] noPairWeekend;
    static int [][] week;
    static double [][] clockLimit;
    static double workingHours;
    static int df;
    static int [][] pdc;
    static int [][] ndc;
    static double [][] mec;
    static int[] manpowerCatagory;
    static Scanner input;
    static int choice;
    static int file;

    static String XLSX_FILE_PATH;
    static String fileOptimization;

    public static void main(String[] args) throws IOException, InvalidFormatException, ParseException {
        System.out.println("=======NURSE ROSTERING PROBLEM OPTIMIZATION=======");
        System.out.println("1. Initial Solution");
        System.out.println("2. Optimization");
        System.out.println("What is your choice?");
        input = new Scanner(System.in);
        choice = input.nextInt();
        for (int i = 0; i < 7; i++) {
            System.out.println((i+1) + ". OpTur" + (i+1));
        }
        System.out.println("Select your file :");
        file = input.nextInt();
        fileOptimization = "D:\\ITS\\Semester 8\\Tugas Akhir\\Nurse Rostering\\nurserost\\Solusi\\OpTur" + (file) +".txt";
        XLSX_FILE_PATH = "D:\\ITS\\Semester 8\\Tugas Akhir\\Nurse Rostering\\nurserost\\OpTur" + (file)+".xls";

        String[][] employee = employees().clone();
        String[][] shift = shifts().clone();
        String[][] manpower = manpower().clone();
        String[] hc = hardconstraint().clone();
        String[] sc = softconstraint().clone();

        if (file == 7)
            df = 2;
        if (file == 3)
            df = 1;
        if (file != 7 && file != 3)
            df = 1;

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
            }
        }
        emp = new Employee[employee.length];
        for (int i = 0; i < employee.length; i++){
            emp[i]= new Employee(employee[i][0], employee[i][1], week[i], employee[i][3]);
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

        double [][] duration = new double[shift.length][7];
        for (int i = 0; i < duration.length; i++) {
            for (int j = 0; j < duration[i].length; j++) {
                duration[i][j] = Double.parseDouble(shift[i][j+1]);
            }
        }

        shiftx = new Shift[shift.length];
        for (int i = 0; i < shift.length; i++){
            shiftx[i]= new Shift(shift[i][0], duration[i], shift[i][8], shift[i][9], times[i][0], times[i][1],
                    shift[i][14]);
        }

        //OBJEK MANPOWER
        int [][] need = new int[manpower.length][7];
        for (int i = 0; i < need.length; i++) {
            for (int j = 0; j < need[i].length; j++) {
                need[i][j] = Integer.parseInt(manpower[i][j+1]);
            }
        }
        plan = new Manpower[manpower.length];
        for (int i = 0; i < manpower.length; i++){
            plan[i]= new Manpower(manpower[i][0], manpower[i][8]);
        }
        for (int i = 0; i < manpower.length; i++) {
            plan[i].setNeed(need[i]);
        }

        //OBJEK CONSTRAINT
        hard = new Constraint(hc);
        hard.setHc1(); hard.setHc2(); hard.setHc3(); hard.setHc5a(); hard.setHc5b();
        hard.setHc5c(); hard.setHc5d(); hard.setHc5e(); hard.setHc5f(); hard.setHc6(); hard.setHc7();
        soft = new Constraint(sc);
        soft.setSc1a(); soft.setSc1b(); soft.setSc1c(); soft.setSc2(); soft.setSc3a(); soft.setSc3b(); soft.setSc3c();
        soft.setSc4(); soft.setSc5aMax(); soft.setSc5aMin(); soft.setSc5bMax(); soft.setSc5bMin(); soft.setSc5cMax();
        soft.setSc5cMin();
        soft.setSc6(); soft.setSc7(); soft.setSc8(); soft.setSc9();

        //MEMBUAT PASANGAN SHIFT YANG DILARANG
        int count = 0;
        for (int i = 0; i < shiftx.length; i++) {
            for (int j = 0; j < shiftx.length; j++) {
                if (shiftx[i].getCategory() == 3 && shiftx[j].getCategory() == 2)
                    if (hard.getHc5a().compareTo(shiftx[j].getStart().minusHours(shiftx[i].getFinish().getHour()).minusMinutes(shiftx[i].getFinish().getMinute())) > 0)
                        count++;
                if (shiftx[i].getCategory() == 2 && shiftx[j].getCategory() == 1)
                    if (hard.getHc5c().compareTo(shiftx[j].getStart().minusHours(shiftx[i].getFinish().getHour()).minusMinutes(shiftx[i].getFinish().getMinute())) > 0)
                        count++;
                if (shiftx[i].getCategory() == 3 && shiftx[j].getCategory() == 1)
                    count++;
            }
        }
        noPairWeekday = new int[count][2];
        count = 0;
        for (int i = 0; i < shiftx.length; i++) {
            for (int j = 0; j < shiftx.length; j++) {
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
                if (shiftx[i].getCategory() == 3 && shiftx[j].getCategory() == 1) {
                    noPairWeekday[count][0] = i + 1;
                    noPairWeekday[count][1] = j + 1;
                    count++;
                }
            }
        }

        count = 0;
        for (int i = 0; i < shiftx.length; i++) {
            for (int j = 0; j < shiftx.length; j++) {
                if (shiftx[i].getCategory() == 3 && shiftx[j].getCategory() == 2)
                    if (hard.getHc5b().compareTo(shiftx[j].getStart().minusHours(shiftx[i].getFinish().getHour()).minusMinutes(shiftx[i].getFinish().getMinute())) > 0)
                        count++;
                if (shiftx[i].getCategory() == 2 && shiftx[j].getCategory() == 1)
                    if (hard.getHc5d().compareTo(shiftx[j].getStart().minusHours(shiftx[i].getFinish().getHour()).minusMinutes(shiftx[i].getFinish().getMinute())) > 0)
                        count++;
                if (shiftx[i].getCategory() == 3 && shiftx[j].getCategory() == 1)
                    count++;
            }
        }
        noPairWeekend = new int[count][2];
        count = 0;
        for (int i = 0; i < shiftx.length; i++) {
            for (int j = 0; j < shiftx.length; j++) {
                if (shiftx[i].getCategory() == 3 && shiftx[j].getCategory() == 2)
                    if (hard.getHc5b().compareTo(shiftx[j].getStart().minusHours(shiftx[i].getFinish().getHour()).minusMinutes(shiftx[i].getFinish().getMinute())) > 0) {
                        noPairWeekend[count][0] = i + 1;
                        noPairWeekend[count][1] = j + 1;
                        count++;
                    }
                if (shiftx[i].getCategory() == 2 && shiftx[j].getCategory() == 1)
                    if (hard.getHc5d().compareTo(shiftx[j].getStart().minusHours(shiftx[i].getFinish().getHour()).minusMinutes(shiftx[i].getFinish().getMinute())) > 0) {
                        noPairWeekend[count][0] = i + 1;
                        noPairWeekend[count][1] = j + 1;
                        count++;
                    }
                if (shiftx[i].getCategory() == 3 && shiftx[j].getCategory() == 1) {
                    noPairWeekend[count][0] = i + 1;
                    noPairWeekend[count][1] = j + 1;
                    count++;
                }
            }
        }

        clockLimit = new double[emp.length][3];
        for (int i = 0; i < clockLimit.length; i++) {
            clockLimit [i][0] = emp[i].getHours() - (emp[i].getHours()*hard.getHc3());
            clockLimit [i][1] = emp[i].getHours();
            clockLimit [i][2] = emp[i].getHours() + (emp[i].getHours()*hard.getHc3());
        }

        if(choice==1)
            initialSolution();
        if(choice==2)
            System.out.println("belum");

    }

    public static void initialSolution() throws IOException {
        pdc = new int [3][7];
        int s1 = 0;
        int s2 = 0;
        int s3 = 0;
        for(int i=0; i<7; i++) {
            for (int j = 0; j < plan.length; j++) {
                if (plan[j].getNeed(i) != 0) {
                    if(shiftx[j].getCategory()==1)
                        s1 =  s1 + plan[j].getNeed(i);
                    if(shiftx[j].getCategory()==2)
                        s2 = s2 + plan[j].getNeed(i);
                    if(shiftx[j].getCategory()==3)
                        s3 = s3 + plan[j].getNeed(i);
                }
            }
            pdc[0][i] = s1; s1 =0;
            pdc[1][i] = s2; s2 =0;
            pdc[2][i] = s3; s3 =0;
        }
        ndc = new int[3][7];
        int [][] clonepdc = new  int [3][7];
        int [][] clonendc = new int [3][7];
        for(int i=0; i<clonendc.length; i++)
            for(int j=0 ;  j<clonendc[i].length;j++)
            {
                clonendc[i][j] = ndc[i][j];
                clonepdc[i][j] = pdc[i][j];
            }
        //print(batasjam);
        manpowerCatagory = new int[3];
        for(int i=0; i<7; i++)
            for(int j=0; j<plan.length; j++)
                if(plan[j].getNeed(i)!=0)
                {
                    if(shiftx[j].getCategory()==1)
                        manpowerCatagory[0] = manpowerCatagory[0]+plan[j].getNeed(i);
                    if(shiftx[j].getCategory()==2)
                        manpowerCatagory[1] = manpowerCatagory[1]+plan[j].getNeed(i);
                    if(shiftx[j].getCategory()==3)
                        manpowerCatagory[2] = manpowerCatagory[2]+plan[j].getNeed(i);
                }
        for(int i=0; i<manpowerCatagory.length; i++) {
            manpowerCatagory[i] = manpowerCatagory[i] * 6;
        }
        int []mpclone = new int[3];
        for(int i=0 ;i<mpclone.length; i++)
            mpclone[i] = manpowerCatagory[i];
        mec = new double[3][emp.length];
        for(int i=0; i<emp.length; i++)
            for(int j=0; j<mec.length; j++)
                mec[j][i]  = manpowerCatagory[j]/emp[i].getHours();

        int [][] matrixsol = new int[emp.length][planningHorizon(week)*7];
        for(int i = 0; i<matrixsol.length; i++)
            for(int j=0; j<matrixsol[i].length;  j++)
                matrixsol[i][j] = 0;

        for (int i = 5; i < planningHorizon(week)*7; i=i+7) {
            while (!validDay(matrixsol, i)) {
                int shift = isMissing(matrixsol, i);
                int randomEmp = (int) (Math.random() * emp.length);
                if (checkHC2(matrixsol, i, shift, randomEmp))
                    matrixsol[randomEmp][i] = shift;
            }
        }
        for (int i = 6; i < planningHorizon(week)*7; i=i+7) {
            while (!validDay(matrixsol, i)) {
                int shift = isMissing(matrixsol, i);
                int randomEmp = (int) (Math.random() * emp.length);
                if (checkHC2(matrixsol, i, shift, randomEmp))
                    matrixsol[randomEmp][i] = shift;
            }
        }
        for (int i = 0; i < planningHorizon(week)*7; i++) {
            if (i % 7 == 5 || i % 7 == 6)
                continue;
            while (!validDay(matrixsol, i)) {
                int shift = isMissing(matrixsol, i);
                int randomEmp = (int) (Math.random() * emp.length);
                if (checkHC2(matrixsol, i, shift, randomEmp))
                    matrixsol[randomEmp][i] = shift;
            }
        }

        int[][] matrixsolTemp =  new int[emp.length][planningHorizon(week)*7];
        copyArray(matrixsol, matrixsolTemp);
        double solutionHour = diffHour(matrixsol);
        for(int  i =0; i<50000; i++)
        {
            int llh = (int) (Math.random()*3);
            if(llh == 0)
                twoExchange(matrixsolTemp);
            if(llh == 1)
                threeExchange(matrixsolTemp);
            if(llh == 2)
                doubleTwoExchange(matrixsolTemp);
            if(validHC4Competence(matrixsolTemp))
            {
                if(validHC5(matrixsolTemp))
                {
                    if(validHC7(matrixsolTemp))
                    {
                        if(diffHour(matrixsolTemp)<=solutionHour)
                        {
                            copyArray(matrixsolTemp, matrixsol);
                            solutionHour = diffHour(matrixsolTemp);
                        }
                        else
                        {
                            copyArray(matrixsol, matrixsolTemp);
                        }
                    }
                    else
                    {
                        copyArray(matrixsol, matrixsolTemp);
                    }
                }
                else
                {
                    copyArray(matrixsol, matrixsolTemp);
                }
            }
            else
            {
                copyArray(matrixsol, matrixsolTemp);
            }
            System.out.println("Iterasi ke " + (i+1) + " " + solutionHour);
        }

        if(validHC2(matrixsol))
        {
            if(validHC3(matrixsol))
            {
                if(validHC4Competence(matrixsol))
                {
                    if(validHC5(matrixsol))
                    {
                        if(validHC7(matrixsol))
                        {
                            if(validHC6(matrixsol))
                            {
                            }
                            else {
                                int[][] matrixsolTemp2 = new int[emp.length][planningHorizon(week)*7];
                                copyArray(matrixsol, matrixsolTemp2);
                                int iterasi = 0;
                                do {
                                    System.out.println("Iterasi ke " + (iterasi + 1) + " pada do while");
                                    iterasi++;
                                    int llh = (int) (Math.random() * 3);
//                                    System.out.println("sudah sampek sini" + llh);
                                    if (llh == 0)
                                        twoExchange(matrixsolTemp2);
                                    if (llh == 1)
                                        threeExchange(matrixsolTemp2);
                                    if (llh == 2)
                                        doubleTwoExchange(matrixsolTemp2);
                                    //System.out.println("sudah sampek sini");
                                    if (validHC3(matrixsolTemp2)) {
                                        if (validHC4Competence(matrixsolTemp2)) {
                                            if (validHC5(matrixsolTemp2)) {
                                                if (validHC7(matrixsolTemp2)) {
                                                    copyArray(matrixsolTemp2, matrixsol);
                                                } else
                                                    copyArray(matrixsol, matrixsolTemp2);
                                            } else
                                                copyArray(matrixsol, matrixsolTemp2);
                                        } else
                                            copyArray(matrixsol, matrixsolTemp2);
                                    } else
                                        copyArray(matrixsol, matrixsolTemp2);
                                } while (!validHC6(matrixsol));
                            }
                        }
                        else
                            System.out.println("hc 7 tidak feasible");
                    }
                    else
                        System.out.println("hc5 tidak feasible");
                }
                else
                    System.out.println("hc4 tidak feasible");
            }
            else
                System.out.println("hc3 tidak feasible");
        }
        else
            System.out.println("hc 2 tidak feasible");


        if(validAll(matrixsol)==0) {
            print(matrixsol);
        }
        else {
            System.out.println("HC " + validAll(matrixsol) + " tidak feasible");
        }
    }

    public static void print(int[][] cetak)
    {
        for(int i = 0; i<cetak.length; i++)
        {
            for(int j=0; j<cetak[i].length; j++)
            {
                System.out.print(cetak[i][j] + " ");
            }
            System.out.println();
        }
    }

    public static boolean validDay(int[][] solution, int day) {
        for (int i = 0; i < plan.length; i++)
            if (plan[i].getNeed(day % 7) != needs(solution, i+1, day))
                return false;
        return true;
    }

    public static int isMissing(int[][] solution, int day) {
        for (int i = 0; i < plan.length; i++)
            if (plan[i].getNeed(day % 7) > needs(solution, i+1, day))
                return i+1;
        return 0;
    }

    public static void copyArray (int [][] solution, int [][]solutionTemp) {
        for (int i = 0; i < solutionTemp.length; i++) {
            for (int j = 0; j < solutionTemp[i].length; j++) {
                solutionTemp[i][j] = solution[i][j];
            }
        }
    }

    public static int validAll (int [][] solution) {
        if (!validHC2(solution))
            return 2;
        if (!validHC3(solution))
            return 3;
        if (!validHC4Competence(solution))
            return 4;
        if (!validHC5(solution))
            return 5;
        if (!validHC6(solution))
            return 6;
        if (!validHC7(solution))
            return 7;
        return 0;
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

    public static void twoExchange(int [][] solution) {
        int randomDay =  -1;
        do{
            randomDay = random(planningHorizon(week)*7);
        } while (randomDay % 7 == 5 || randomDay % 7 == 6);

        int randomEmp1 = -1;
        int randomEmp2 = -1;
        do {
            randomEmp1 = random(emp.length);
        } while (solution[randomEmp1][randomDay]==0);

        do {
            randomEmp2 = random(emp.length);
        } while (solution[randomEmp2][randomDay]!=0);

        int shiftExchange = solution[randomEmp1][randomDay];
        solution[randomEmp1][randomDay] = solution[randomEmp2][randomDay];
        solution[randomEmp2][randomDay] = shiftExchange;
    }

    public static void threeExchange(int [][] solution) {
        int randomDay = random(planningHorizon(week)*7);
        int randomEmp1 = -1;
        int randomEmp2 = -1;
        int randomEmp3 = -1;

        do {
            randomEmp1 = random(emp.length);
        } while (solution[randomEmp1][randomDay] == 0 || randomEmp1 == randomEmp2 || randomEmp1 == randomEmp3);
        do {
            randomEmp2 = random(emp.length);
        } while (solution[randomEmp2][randomDay] == 0 || randomEmp2 == randomEmp1 || randomEmp2 == randomEmp3);
        do {
            randomEmp3 = random(emp.length);
        } while (solution[randomEmp3][randomDay] == 0 || randomEmp3 == randomEmp1 || randomEmp3 == randomEmp2);

        int shiftExchange = solution[randomEmp1][randomDay];
        solution[randomEmp1][randomDay] = solution[randomEmp2][randomDay];
        solution[randomEmp2][randomDay] = solution[randomEmp3][randomDay];
        solution[randomEmp3][randomDay] = shiftExchange;
    }

    public static void doubleTwoExchange(int [][] solution) {
        int randomEmp1 = -1;
        int randomEmp2 = -1;
        int randomDay1 = -1;
        int randomDay2 = -1;
        while (randomEmp1 == -1 || randomEmp2 == -1) {
            do {
                randomDay1 = random(planningHorizon(week)*7);
                randomDay2 = random(planningHorizon(week)*7);
            } while (randomDay1 % 7 == 5 || randomDay1 % 7 == 6 || randomDay2 % 7 == 5 || randomDay2 % 7 == 6);
            for (int i = 0; i < emp.length; i++) {
                for (int j = 0; j < emp.length; j++) {
                    if (solution[i][randomDay1] != 0 && solution[j][randomDay2] != 0 && shiftx[solution[i][randomDay1]-1].getCategory() == shiftx[solution[j][randomDay2]-1].getCategory())
                        if (solution[i][randomDay2] == 0 && solution[j][randomDay1] == 0) {
                            randomEmp1 = i;
                            randomEmp2 = j;
                        }
                }
            }
        }
        solution[randomEmp2][randomDay1] = solution[randomEmp1][randomDay1];
        solution[randomEmp1][randomDay1] = 0;
        solution[randomEmp1][randomDay2] = solution[randomEmp2][randomDay2];
        solution[randomEmp2][randomDay2] = 0;
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

    public static double[] sumHours (int[][] solution, int week){
        double [] weekHours = new double[emp.length];

        for (int i = 0; i < emp.length; i++) {
            for (int j = week*7; j < (week+1)*7; j++) {
                if (solution [i][j] != 0) {
                    weekHours[i] = weekHours[i] + shiftx[solution[i][j]-1].getDuration(j % 7);
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

    public static double diffHour (int [][] solution) {
        double hour = 0;
        double [] first = avgHours(solution, planningHorizon(week)).clone();
        for (int i = 0; i < first.length; i++) {
            hour = hour + (double) (Math.abs(first[i]-clockLimit[i][df]));
        }
        return hour;
    }

    public static int needs (int [][] solution, int shift, int day) {
        int count = 0;
        for (int i = 0; i < solution.length; i++)
            if (solution[i][day] == shift)
                count++;
        return count;
    }

    public static boolean checkHC2(int [][] solution, int day, int shift, int employee){
        if(plan[shift-1].getNeed(day % 7) > needs(solution, shift, day))
            if(checkHC4Competence(shift, employee))
                if(checkHC4Week(day, employee))
                    if(checkHC7(solution, employee, day, shift))
                        if(checkHC5(solution, employee, shift, day))
                            return true;
        //System.out.println("Error di ok2");
        return false;
    }

    public static boolean checkHC4Competence (int shift, int employee) {
        if(shiftx[shift-1].getCompetence().equals("A") && emp[employee].getCompetence().equals(""))
            return false;
        return true;
    }

    public static boolean checkHC4Week (int day, int employee) {
        if(day%7 == 5 || day%7 == 6) {
            for (int i = 0; i < emp[employee].getWeek().length; i++) {
                if (day / 7 == emp[employee].getWeek()[i] - 1)
                    return true;
            }
        } else
            return true;
        return false;
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

    public static boolean checkHC7 (int solution [][], int employee,  int day, int shift){
        double workingHour = shiftx[shift-1].getDuration(day % 7);
        if (workingHours(solution, day, employee)+workingHour <= hard.getHc7())
            return true;

        return false;
    }

    public static double workingHours (int [][] solution, int day, int employee) {
        double count = 0;
        int week = day/7;
        for (int i = week*7; i < (week+1)*7; i++) {
            if (solution[employee][i] != 0)
                count = count + shiftx[solution[employee][i]-1].getDuration(day % 7);
        }
        return count;
    }

    public static boolean validHC2 (int [][] solution) {
        for (int i = 0; i < planningHorizon(week)*7; i++) {
            for (int j = 0; j < plan.length; j++) {
                if (plan[j].getNeed(i % 7) != needs(solution, j+1, i))
                    return false;
            }
        }
        return true;
    }

    public static boolean validHC3 (int [][] solution){
        double [] hour = avgHours(solution, planningHorizon(week)).clone();
        for (int i = 0; i < hour.length; i++) {
            if (hour[i] > clockLimit[i][2])
                return false;
            if (hour[i] < clockLimit[i][0])
                return false;
        }
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

    public static boolean validHC5 (int [][] solution) {
        for (int i = 0; i < planningHorizon(week)*7; i++) {
            for (int j = 0; j < emp.length; j++) {
                if (i % 7 != 5 || i % 7 != 6) {
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

    public static boolean[] checkFreeWeekend(int[][] solution, int week){
        boolean[] isOk = new boolean[emp.length];
        for(int  i=0; i<isOk.length; i++)
            isOk[i] = false;
        label : for(int j=0;  j<emp.length; j++)
        {
            for(int i=7*week; i<(week+1)*7;i++)
            {
                if(solution[j][i]==0)
                {
                    if(i==week*7)
                    {
                        if(solution[j][i+1]==0)
                        {
                            isOk[j] = true;
                            continue label;
                        }
                        else
                        {
                            if(shiftx[solution[j][i+1]-1].getCategory()!=1)
                            {
                                isOk[j] = true;
                                continue label;
                            }
                            else
                            {
                                LocalTime lessthan =  LocalTime.parse("08:00");
                                if(shiftx[solution[j][i+1]-1].getStart().isAfter(lessthan));
                                {
                                    isOk[j] = true;
                                    continue label;
                                }
                            }
                        }
                    }
                    if(i>week*7 && i<((week+1)*7)-1)
                    {
                        if(solution[j][i+1]==0||solution[j][i-1]==0)
                        {
                            isOk[j] = true;
                            continue label;
                        }
                        if(solution[j][i+1]!=0&&shiftx[solution[j][i+1]-1].getCategory()!=1)
                        {
                            isOk[j] = true;
                            continue label;
                        }
                        if(solution[j][i+1]!=0&&shiftx[solution[j][i+1]-1].getCategory()==1)
                        {
                            if(shiftx[solution[j][i-1]-1].getCategory()!=3)
                            {
                                LocalTime lessthan  = LocalTime.parse("08:00");
                                LocalTime limit = LocalTime.parse("00:00");
                                LocalTime before = limit.minusHours(shiftx[solution[j][i-1]-1].getFinish().getHour()).minusMinutes(shiftx[solution[j][i-1]-1].getFinish().getMinute());
                                LocalTime total = before.plusHours(shiftx[solution[j][i+1]-1].getStart().getHour()).minusMinutes(shiftx[solution[j][i+1]-1].getStart().getMinute());
                                if(total.isAfter(lessthan))
                                {
                                    isOk[j] = true;
                                    continue label;
                                }
                            }
                        }
                    }
                    if(i==((week+1)*7)-1)
                    {
                        if(solution[j][i-1]==0)
                        {
                            isOk[j] = true;
                            continue label;
                        }
                        else
                        {
                            LocalTime  lessthan  = LocalTime.parse("08:00");
                            LocalTime limit = LocalTime.parse("00:00");
                            LocalTime before = limit.minusHours(shiftx[solution[j][i-1]-1].getFinish().getHour()).minusMinutes(shiftx[solution[j][i-1]-1].getFinish().getMinute());
                            if(before.isAfter(lessthan))
                            {
                                isOk[j] = true;
                                continue label;
                            }
                        }
                    }
                }
            }
        }
        return isOk;
    }

    public static boolean validHC6 (int [][] solution) {
        boolean [][] check = new boolean[emp.length][planningHorizon(week)];
        for (int i = 0; i < planningHorizon(week); i++) {
            boolean [] check2 = checkFreeWeekend(solution, i).clone(); {
                for (int j = 0; j < check2.length; j++) {
                    check[j][i] = check2[j];
                }
            }
        }

        for (int i = 0; i < check.length; i++) {
            for (int j = 0; j < check[i].length; j++) {
                if (!check[i][j]) {
                    return false;
                }
            }
        }
        return true;
    }

    public static boolean validHC7 (int [][] solution) {
        for(int i=0; i<planningHorizon(week)*7; i++)
        {
            for(int j=0; j<emp.length; j++)
            {
                if(workingHours(solution, i, j) > hard.getHc7())
                    return false;
            }
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

    public static String[] softconstraint() throws IOException, InvalidFormatException {
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
        String [] cons = new String[data.length];
        for (int i = 0; i < data.length; i++) {
            cons[i] = data[i][3];
        }
        return cons;
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