import java.io.IOException;
import java.util.LinkedList;

public class Solution {
    int [][] solution;

    public Solution (int [][] solution) {
        this.solution = solution;
    }

    public double countPenalty() {
        return penalty1() + penalty2() + penalty3() + penalty4() + penalty5() +
                penalty6() + penalty7() + penalty8() + penalty9();
    }

    private double penalty1() {
        double penalty1 = 0;
        for (int i = 0; i < 3; i++) {
            int day = 0;
            int softConstraint = -1;
            if (i == 0) {
                softConstraint = Schedule.soft.getSc1a();
                if (Schedule.soft.getSc1a() != 0) {
                    day = (Schedule.planningHorizon[(Schedule.file - 1)] * 7) - (Schedule.soft.getSc1a());
                } else
                    continue;;
            }
            if (i == 1) {
                softConstraint = Schedule.soft.getSc1b();
                if (Schedule.soft.getSc1b() != 0) {
                    day = (Schedule.planningHorizon[(Schedule.file - 1)] * 7) - (Schedule.soft.getSc1b());
                } else
                    continue;;
            }
            if (i == 2) {
                softConstraint = Schedule.soft.getSc1c();
                if (Schedule.soft.getSc1c() != 0) {
                    day = (Schedule.planningHorizon[(Schedule.file - 1)] * 7) - (Schedule.soft.getSc1c());
                } else
                    continue;;
            }
            for (int j = 0; j < Schedule.emp.length; j++) {
                for (int k = 0; k < day; k++) {
                    int count = (softConstraint+1);
                    for (int l = k; l <= k+softConstraint ; l++) {
                        if (solution[j][l] != 0) {
                            if (Schedule.shiftx[solution[j][l]-1].getCategory() == (i+1)) {
                                count--;
                            }
                        }
                    }
                    if (count == 0)
                        penalty1 = penalty1 + 1;
                }
            }
        }
        return penalty1;
    }

    private double penalty2() {
        double penalty2 = 0;
        int softConstraint = Schedule.soft.getSc2();
        if (softConstraint != 0) {
            int day = (Schedule.planningHorizon[Schedule.file - 1] * 7) - softConstraint;
            for (int i = 0; i < Schedule.emp.length; i++) {
                for (int j = 0; j < day; j++) {
                    int count = (softConstraint + 1);
                    for (int k = j; k <= j + softConstraint; k++) {
                        if (solution[i][k] > 0)
                            count--;
                    }
                    if (count == 0)
                        penalty2 = penalty2 + 1;
                }
            }
        }
        return penalty2;
    }

    private double penalty3() {
        double penalty3 = 0;
        for (int i = 0; i < 3; i++) {
            int day = 0;
            int softConstraint = -1;
            if (i == 0) {
                softConstraint = Schedule.soft.getSc3a();
                if (Schedule.soft.getSc3a() != 0) {
                    day = (Schedule.planningHorizon[(Schedule.file - 1)] * 7) - (Schedule.soft.getSc3a());
                } else
                    continue;;
            }
            if (i == 1) {
                softConstraint = Schedule.soft.getSc3b();
                if (Schedule.soft.getSc3b() != 0) {
                    day = (Schedule.planningHorizon[(Schedule.file - 1)] * 7) - (Schedule.soft.getSc3b());
                } else
                    continue;;
            }
            if (i == 2) {
                softConstraint = Schedule.soft.getSc3c();
                if (Schedule.soft.getSc3c() != 0) {
                    day = (Schedule.planningHorizon[(Schedule.file - 1)] * 7) - (Schedule.soft.getSc3c());
                } else
                    continue;;
            }
            for (int j = 0; j < Schedule.emp.length; j++) {
                for (int k = 0; k < day; k++) {
                    if (solution[j][k+1] != 0) {
                        if (Schedule.shiftx[solution[j][k+1]-1].getCategory() == (i+1)) {
                            if (solution[j][k] == 0 || Schedule.shiftx[solution[j][k] - 1].getCategory() != (i + 1)) {
                                double calculate = 0;
                                for (int l = k + 1; l <= k + softConstraint; l++) {
                                    int count = 1;
                                    for (int m = k + 1; m <= k + softConstraint; m++) {
                                        if (solution[j][m] == 0 || Schedule.shiftx[solution[j][m] - 1].getCategory() != (i + 1))
                                            count = 0;
                                    }
                                    if (count == 1)
                                        calculate = calculate + 1;
                                }
                                double penalti = softConstraint - calculate;
                                penalty3 = penalty3 + penalti;
                            }
                        }
                    }
                }
            }
        }
        return penalty3;
    }

    private double penalty4() {
        double penalty4 = 0;
        int softConstraint = Schedule.soft.getSc4();
        if (softConstraint != 0) {
            int day = (Schedule.planningHorizon[(Schedule.file-1)] * 7) - softConstraint;
            for (int i = 0; i < Schedule.emp.length; i++) {
                for (int j = 0; j < day; j++) {
                    if (solution[i][j+1] != 0 && solution[i][j] == 0) {
                        double calculate = 0;
                        for (int k = j+1; k <= j+softConstraint; k++) {
                            int count = 1;
                            for (int l = j+1; l <= j+softConstraint; l++) {
                                if (solution[i][l] == 0)
                                    count = 0;
                            }
                            if (count == 1)
                                calculate = calculate + 1;
                        }
                        double penalti = softConstraint - calculate;
                        penalty4 = penalty4 - penalti;
                    }
                }
            }
        }
        return penalty4;
    }

    private double penalty5() {
        double penalty5 = 0;
        for (int i = 0; i < 3; i++) {
            int min = 0;
            int max = 0;
            if (i == 0) {
                min = Schedule.soft.getSc5aMin();
                max = Schedule.soft.getSc5aMax();
            }
            if (i == 1) {
                min = Schedule.soft.getSc5bMin();
                max = Schedule.soft.getSc5aMax();
            }
            if (i == 2) {
                min = Schedule.soft.getSc5cMin();
                max = Schedule.soft.getSc5cMax();
            }
            if (min == 0 && max == 0)
                continue;

            for (int j = 0; j < Schedule.emp.length; j++) {
                int count = 0;
                for (int k = 0; k < Schedule.planningHorizon[(Schedule.file - 1)] * 7; k++) {
                    if (solution[j][k] != 0)
                        if (Schedule.shiftx[solution[j][k]-1].getCategory() == (i+1))
                            count++;
                }
                min = min - count;
                max = count - max;
                if (min>max && min>0)
                    penalty5 = penalty5 + (min*min);
                if (max>min && max>0)
                    penalty5 = penalty5 + (max*max);
            }
        }
        penalty5 = Math.sqrt(penalty5);
        return penalty5;
    }

    private double penalty6 () {
        double penalty6 = 0;
        if (Schedule.soft.getSc6()) {
            for (int i = 0; i < Schedule.emp.length; i++) {
                double hour = (Schedule.clockLimit[i][1]) * (Schedule.planningHorizon[(Schedule.file - 1)]);
                double workingHour = 0;
                for (int j = 0; j < Schedule.planningHorizon[(Schedule.file - 1)] * 7; j++) {
                    if (solution[i][j] != 0)
                        workingHour = workingHour + Schedule.shiftx[solution[i][j]-1].getDuration(j % 7);
                }
                hour = hour - workingHour;
                penalty6 = penalty6 + (hour*hour);
            }
            penalty6 = Math.sqrt(penalty6);
        }
        return penalty6;
    }

    private double penalty7 () {
        double penalty7 = 0;
        if (Schedule.soft.getSc7()) {
            for (int i = 0; i < Schedule.emp.length; i++) {
                int count = 0;
                for (int j = 0; j < (Schedule.planningHorizon[(Schedule.file - 1)] * 7)-1; j++) {
                    if (solution[i][j] != 0 && solution[i][j+1] == 0)
                        count++;
                }
                penalty7 = penalty7 + (count*count);
            }
            penalty7 = Math.sqrt(penalty7);
        }
        return penalty7;
    }

    private double penalty8 () {
        double penalty8 = 0;
        int day = (Schedule.planningHorizon[(Schedule.file-1)] * 7);
        int emp = Schedule.emp.length;
        if (Schedule.soft.getSc8() != 0) {
            penalty8 = (double) emp * day;
            for (int i = 0; i < emp; i++) {
                for (int j = 0; j < day; j++) {
                    for (int k = 0; k < Schedule.want.length; k++) {
                        if (day % 7 == Schedule.want[k].day) {
                            if (solution[i][j] != 0) {
                                if (Schedule.shiftx[solution[i][j]-1].getName().equals(Schedule.want[k].pattern[0])) {
                                    if (j <= day - (Schedule.want[k].pattern.length)) {
                                        int count = Schedule.want[k].pattern.length;
                                        for (int l = 0; l < Schedule.want[k].pattern.length; l++) {
                                            if (solution[i][j+l] != 0) {
                                                if (Schedule.shiftx[solution[i][j + l] - 1].getName().equals(Schedule.want[k].pattern[l])) {
                                                    count--;
                                                }
                                            }
                                            else {
                                                if (Schedule.want[k].pattern[l].equals("<Free>")) {
                                                    count--;
                                                }
                                            }
                                        }
                                        if (count == 0)
                                            penalty8 = penalty8 - 1;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return penalty8;
    }

    private double penalty9 () {
        double penalty9 = 0;
        int day = (Schedule.planningHorizon[(Schedule.file-1)] * 7);
        int emp = Schedule.emp.length;
        if (Schedule.soft.getSc9() != 0) {
            for (int i = 0; i < emp; i++) {
                for (int j = 0; j < day; j++) {
                    for (int k = 0; k < Schedule.unwant.length; k++) {
                        if (day % 7 == Schedule.unwant[k].day) {
                            if (solution[i][j] != 0) {
                                if (Schedule.shiftx[solution[i][j]-1].getName().equals(Schedule.unwant[k].pattern[0])) {
                                    if (j <= day - (Schedule.unwant[k].pattern.length)) {
                                        int count = Schedule.unwant[k].pattern.length;
                                        for (int l = 0; l < Schedule.unwant[k].pattern.length; l++) {
                                            if (solution[i][j+l] != 0) {
                                                if (Schedule.shiftx[solution[i][j + l] - 1].getName().equals(Schedule.unwant[k].pattern[l])) {
                                                    count--;
                                                }
                                            }
                                            else {
                                                if (Schedule.unwant[k].pattern[l].equals("<Free>")) {
                                                    count--;
                                                }
                                            }
                                        }
                                        if (count == 0)
                                            penalty9 = penalty9 - 1;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return penalty9;
    }

    public void tryToFeasible() {
        int day = Schedule.planningHorizon[(Schedule.file - 1)] * 7;
        int [][] newSolution = new int [solution.length][day];
        Schedule.copyArray(solution, newSolution);
        double solutionHour = Schedule.diffHour(solution);
        int count = Schedule.countHC6(solution);
        for(int i =0; i<50000; i++)
        {
            int llh = (int) (Math.random()*3);
            if(llh == 0)
                Schedule.twoExchange(newSolution);
            if(llh == 1)
                Schedule.threeExchange(newSolution);
            if(llh == 2)
                Schedule.doubleTwoExchange(newSolution);
            if(Schedule.validHC4Competence(newSolution))
            {
                if(Schedule.validHC5(newSolution))
                {
                    if(Schedule.validHC7(newSolution))
                    {
                        if(Schedule.diffHour(newSolution)<=solutionHour)
                        {
                            if (Schedule.countHC6(newSolution) <= count) {
                                Schedule.copyArray(newSolution, solution);
                                count = Schedule.countHC6(newSolution);
                                solutionHour = Schedule.diffHour(newSolution);
                            }
                            else {
                                Schedule.copyArray(solution, newSolution);
                            }
                        }
                        else {
                            Schedule.copyArray(solution, newSolution);
                        }
                    }
                    else {
                        Schedule.copyArray(solution, newSolution);
                    }
                }
                else {
                    Schedule.copyArray(solution, newSolution);
                }
            }
            else {
                Schedule.copyArray(solution, newSolution);
            }
            System.out.println("Iterasi ke " + (i+1) + " " + solutionHour +  " hc6 " + Schedule.countHC6(solution));
        }
    }

    public void hillClimbing () throws IOException {
        for (int e = 0; e < 9; e++) {
            int day = Schedule.planningHorizon[(Schedule.file - 1)] * 7;
            int[][] newSolution = new int[solution.length][day];
            int [][] baseSolution = new int [solution.length][day];
            double penalty = countPenalty();
            Schedule.copyArray(solution, newSolution);
            Schedule.copyArray(solution, baseSolution);
            double[][] plot = new double[101][2];
            int p = 0;
            long startTime = System.nanoTime();
            for (int i = 0; i < 10000; i++) {
                int llh = (int) (Math.random() * 3);
                switch (llh) {
                    case 0:
                        Schedule.twoExchange(solution);
                    case 1:
                        Schedule.twoExchange(solution);
                    case 2:
                        Schedule.doubleTwoExchange(solution);
                }
                if (Schedule.validAll(solution) == 0) {
                    if (countPenalty() <= penalty) {
                        penalty = countPenalty();
                        Schedule.copyArray(solution, newSolution);
                    } else {
                        Schedule.copyArray(newSolution, solution);
                    }
                } else {
                    Schedule.copyArray(newSolution, solution);
                }
                System.out.println("iterasi ke " + (i + 1) + " penalti : " + countPenalty());
                if ((i + 1) % 10000 == 0) {
                    plot[p][0] = i + 1;
                    plot[p][1] = penalty;
                    p = p + 1;
                }
            }

            long endTime = System.nanoTime();
            long time = (endTime-startTime) / 1000000000;
            plot [100][0] = time;
            Schedule.saveOptimation(plot, e);
            System.out.println(penalty);
            Schedule.copyArray(baseSolution, solution);
//            for (int j = 0; j < plot.length; j++) {
//                for (int k = 1; k < plot[j].length; k++) {
//                    System.out.print(plot[j][k] + " ");
//                }
//                System.out.println();
//            }
        }
    }

    public int countRF (int [] rf) {
        int index = -1000000;
        int llh = -1;
        LinkedList<Integer> rl = new LinkedList<Integer>();
        for (int i = 0; i < rf.length; i++) {
            if (rf[i] > index) {
                llh = i;
                index = rf[i];
                for (int j = 0; j < rf.length; j++) {
                    if (rf[j] == index)
                        rl.add(j);
                }
                if (!rl.isEmpty()) {
                    int x = -1;
                    x = (int) (Math.random() * 3);
                    while (!rl.contains(x)) {
                        x = (int) (Math.random() * 3);
                    }
                    rl.clear();
                }
            }
        }
        return llh;
    }

    public void reinforcementLearning1 () {
        int p = 0;
        int day = Schedule.planningHorizon[(Schedule.file - 1)] * 7;
        int [][] newSolution = new int [solution.length][day];
        double S = countPenalty();
        double s = S;
        double currPenalty; double bestPenalty;
        currPenalty = bestPenalty = countPenalty();
        Schedule.copyArray(solution, newSolution);
        double [] fitness = new double[200];
        for (int i = 0; i < fitness.length; i++) {
            fitness[i] = S;
        }
        int llh = -1;
        int [] rf = {0, 0, 0};
        double diff = 0;
        double d = 0;
        double prob = 0;
        double TAwal = 10000000;
        double coolingrate = 0.99995;
        for (int i = 0; i < 1000000; i++) {
//            llh = -1;
            llh = countRF(rf);
            if (llh == 0)
                Schedule.twoExchange(solution);
            if (llh == 1)
                Schedule.threeExchange(solution);
            if (llh == 2)
                Schedule.doubleTwoExchange(solution);
            if (Schedule.validAll(solution) == 0) {
                diff = countPenalty() - currPenalty;
                d = Math.abs(diff)/TAwal;
                prob = Math.exp(-d);
//                System.out.println("feasible");
                if (countPenalty() <= currPenalty) {
                    currPenalty = countPenalty();
                    Schedule.copyArray(solution, newSolution);
                    if (currPenalty <= bestPenalty) {
                        bestPenalty = currPenalty;
                        Schedule.copyArray(solution, newSolution);
                        rf[llh] = rf[llh]+1;
                    } else {
                        Schedule.copyArray(newSolution, solution);
                    }
                } else {
                    if (prob >= Math.random()) {
                        System.out.println("solusi jelek diterima");
                        currPenalty = countPenalty();
                        Schedule.copyArray(solution, newSolution);
                    } else {
                        Schedule.copyArray(newSolution, solution);
                        rf[llh] = rf[llh] - 1;
                    }
                }
            } else {
                Schedule.copyArray(newSolution, solution);
                rf[llh] = rf[llh] - 1;
            }
            TAwal = TAwal * coolingrate;
//            System.out.println(rf[llh]);
//            System.out.println("Iterasi ke " + (i+1) + " s " + s);
            System.out.println("Iterasi : " + (i+1) +" suhu : " + TAwal + " diff : " + diff + " d : " + d + " prob : " + prob + " penalti : " + countPenalty());
        }
        System.out.println(bestPenalty);
    }

    public void reinforcementLearning () throws IOException {
        for (int e = 0; e < 3; e++) {
            int[] score = {500, 500, 500};
            int score2Exchange = score[0];
            int score3Exchange = score[1];
            int scoredoubleExchange = score[2];
            int day = Schedule.planningHorizon[(Schedule.file - 1)] * 7;
            int[][] newSolution = new int[Schedule.emp.length][day];
            int[][] baseSolution = new int[Schedule.emp.length][day];
            double penalty = countPenalty();
            double[][] plot = new double[101][1];
            int p = 0;
            long startTime = System.nanoTime();
            Schedule.copyArray(solution, newSolution);
            Schedule.copyArray(solution, baseSolution);
            for (int i = 0; i < 1000000; i++) {
                double epsilon = 1 / (Math.sqrt(i));
                if (Math.random() < epsilon) {
                    int llh = (int) (Math.random() * 3);
                    switch (llh) {
                        case 0:
                            Schedule.twoExchange(solution);
                        case 1:
                            Schedule.threeExchange(solution);
                        case 2:
                            Schedule.doubleTwoExchange(solution);
                    }
                } else {
                    if (score2Exchange > score3Exchange && score2Exchange > scoredoubleExchange) {
                        Schedule.twoExchange(solution);
                        if (countPenalty() <= penalty) {
                            if (score2Exchange < 1000) {
                                score2Exchange = score2Exchange + 10;
                            } else {
                                score2Exchange = 1000;
                            }
                        } else {
                            if (score2Exchange > 0) {
                                score2Exchange = score2Exchange - 10;
                            } else {
                                score2Exchange = 0;
                            }
                        }
                    }
                    if (score3Exchange > score2Exchange && score3Exchange > scoredoubleExchange) {
                        Schedule.threeExchange(solution);
                        if (countPenalty() <= penalty) {
                            if (score3Exchange < 1000) {
                                score3Exchange = score3Exchange + 10;
                            } else {
                                score3Exchange = 1000;
                            }
                        } else {
                            if (score3Exchange > 0) {
                                score3Exchange = score3Exchange - 10;
                            } else {
                                score3Exchange = 0;
                            }
                        }
                    }
                    if (scoredoubleExchange > score2Exchange && scoredoubleExchange > score3Exchange) {
                        Schedule.doubleTwoExchange(solution);
                        if (countPenalty() <= penalty) {
                            if (scoredoubleExchange < 1000) {
                                scoredoubleExchange = scoredoubleExchange + 10;
                            } else {
                                scoredoubleExchange = 1000;
                            }
                        } else {
                            if (scoredoubleExchange > 0) {
                                scoredoubleExchange = scoredoubleExchange - 10;
                            } else {
                                scoredoubleExchange = 0;
                            }
                        }
                    }
                    if (score2Exchange == score3Exchange && score2Exchange == scoredoubleExchange) {
                        if (Math.random() < 0.3) {
                            Schedule.twoExchange(solution);
                            if (countPenalty() <= penalty) {
                                if (score2Exchange < 1000) {
                                    score2Exchange = score2Exchange + 10;
                                } else {
                                    score2Exchange = 1000;
                                }
                            } else {
                                if (score2Exchange > 0) {
                                    score2Exchange = score2Exchange - 10;
                                } else {
                                    score2Exchange = 0;
                                }
                            }
                        }
                        if (Math.random() > 0.3 && Math.random() < 0.6) {
                            Schedule.threeExchange(solution);
                            if (countPenalty() <= penalty) {
                                if (score3Exchange < 1000) {
                                    score3Exchange = score3Exchange + 10;
                                } else {
                                    score3Exchange = 1000;
                                }
                            } else {
                                if (score3Exchange > 0) {
                                    score3Exchange = score3Exchange - 10;
                                } else {
                                    score3Exchange = 0;
                                }
                            }
                        }
                        if (Math.random() > 0.6) {
                            Schedule.doubleTwoExchange(solution);
                            if (countPenalty() <= penalty) {
                                if (scoredoubleExchange < 1000) {
                                    scoredoubleExchange = scoredoubleExchange + 10;
                                } else {
                                    scoredoubleExchange = 1000;
                                }
                            } else {
                                if (scoredoubleExchange > 0) {
                                    scoredoubleExchange = scoredoubleExchange - 10;
                                } else {
                                    scoredoubleExchange = 0;
                                }
                            }
                        }
                    }
                    if (score2Exchange == scoredoubleExchange && score2Exchange > score3Exchange) {
                        if (Math.random() < 0.5) {
                            Schedule.twoExchange(solution);
                            if (countPenalty() <= penalty) {
                                if (score2Exchange < 1000) {
                                    score2Exchange = score2Exchange + 10;
                                } else {
                                    score2Exchange = 1000;
                                }
                            } else {
                                if (score2Exchange > 0) {
                                    score2Exchange = score2Exchange - 10;
                                } else {
                                    score2Exchange = 0;
                                }
                            }
                        }
                        if (Math.random() > 0.5) {
                            Schedule.doubleTwoExchange(solution);
                            if (countPenalty() <= penalty) {
                                if (scoredoubleExchange < 1000) {
                                    scoredoubleExchange = scoredoubleExchange + 10;
                                } else {
                                    scoredoubleExchange = 1000;
                                }
                            } else {
                                if (scoredoubleExchange > 0) {
                                    scoredoubleExchange = scoredoubleExchange - 10;
                                } else {
                                    scoredoubleExchange = 0;
                                }
                            }
                        }
                    }
                    if (score2Exchange == score3Exchange && score2Exchange > scoredoubleExchange) {
                        if (Math.random() < 0.5) {
                            Schedule.twoExchange(solution);
                            if (countPenalty() <= penalty) {
                                if (score2Exchange < 1000) {
                                    score2Exchange = score2Exchange + 10;
                                } else {
                                    score2Exchange = 1000;
                                }
                            } else {
                                if (score2Exchange > 0) {
                                    score2Exchange = score2Exchange - 10;
                                } else {
                                    score2Exchange = 0;
                                }
                            }
                        }
                        if (Math.random() > 0.5) {
                            Schedule.threeExchange(solution);
                            if (countPenalty() <= penalty) {
                                if (score3Exchange < 1000) {
                                    score3Exchange = score3Exchange + 10;
                                } else {
                                    score3Exchange = 1000;
                                }
                            } else {
                                if (score3Exchange > 0) {
                                    score3Exchange = score3Exchange - 10;
                                } else {
                                    score3Exchange = 0;
                                }
                            }
                        }
                    }
                    if (score3Exchange == scoredoubleExchange && score3Exchange > score2Exchange) {
                        if (Math.random() < 0.5) {
                            Schedule.threeExchange(solution);
                            if (countPenalty() <= penalty) {
                                if (score3Exchange < 1000) {
                                    score3Exchange = score3Exchange + 10;
                                } else {
                                    score3Exchange = 1000;
                                }
                            } else {
                                if (score3Exchange > 0) {
                                    score3Exchange = score3Exchange - 10;
                                } else {
                                    score3Exchange = 0;
                                }
                            }
                        }
                        if (Math.random() > 0.5) {
                            Schedule.doubleTwoExchange(solution);
                            if (countPenalty() <= penalty) {
                                if (scoredoubleExchange < 1000) {
                                    scoredoubleExchange = scoredoubleExchange + 10;
                                } else {
                                    scoredoubleExchange = 1000;
                                }
                            } else {
                                if (scoredoubleExchange > 0) {
                                    scoredoubleExchange = scoredoubleExchange - 10;
                                } else {
                                    scoredoubleExchange = 0;
                                }
                            }
                        }
                    }
                }
//            System.out.println("2Exchange : " +score2Exchange+ "\t 3Exchange : " +score3Exchange+ "\t double : " +scoredoubleExchange);
                if (Schedule.validAll(solution) == 0) {
                    if (countPenalty() <= penalty) {
                        penalty = countPenalty();
                        Schedule.copyArray(solution, newSolution);
                    } else {
                        Schedule.copyArray(newSolution, solution);
                    }
                } else {
                    Schedule.copyArray(newSolution, solution);
                }

                System.out.println("iterasi ke " + (i + 1) + " penalti : " + countPenalty());
//            System.out.println("score2Exchange : " + score2Exchange + " score3Exchange : " + score3Exchange + " double : " + scoredoubleExchange);
                if ((i + 1) % 10000 == 0) {
                    plot[p][0] = penalty;
                    p = p + 1;
                }
            }

            long endTime = System.nanoTime();
            long time = (endTime-startTime) / 1000000000;
            plot [100][0] = time;
            Schedule.saveOptimation(plot, e);
            System.out.println(penalty);
            Schedule.copyArray(baseSolution, solution);

//            for (int j = 0; j < plot.length; j++) {
//                for (int k = 0; k < plot[j].length; k++) {
//                    System.out.print(plot[j][k] + " ");
//                }
//                System.out.println();
//            }
        }
    }

    public void simulatedAnnealing () {
        int day = Schedule.planningHorizon[(Schedule.file - 1)] * 7;
        int [][] newSolution = new int [Schedule.emp.length][day];
        Schedule.copyArray(solution, newSolution);
        double bestPenalty; double currPenalty;
        bestPenalty = currPenalty = countPenalty();
        double TAwal = 10000000;
        double coolingrate = 0.99995;
        double diff = 0;
        double d = 0;
        double prob = 0;
        int p = 0;
        double [][] plot = new double [100][4];
//
        for (int i = 0; i < 1000000; i++) {
            int llh = (int) (Math.random() * 3);
            if (llh == 0)
                Schedule.twoExchange(solution);
            if (llh == 1)
                Schedule.threeExchange(solution);
            if (llh == 2)
                Schedule.doubleTwoExchange(solution);
//            currPenalty = countPenalty();
            if (Schedule.validAll(solution) == 0) {
                diff = countPenalty() - currPenalty;
                d = Math.abs(diff)/TAwal;
                prob = Math.exp(-d);
//                System.out.println("feasible");
                if (countPenalty() <= currPenalty) {
                    currPenalty = countPenalty();
                    Schedule.copyArray(solution, newSolution);
                    if (currPenalty <= bestPenalty) {
                        bestPenalty = currPenalty;
                        Schedule.copyArray(solution, newSolution);
                    } else {
                        Schedule.copyArray(newSolution, solution);
                    }
                } else {
                    if (prob >= Math.random()) {
//                        System.out.println("solusi jelek diterima");
                        currPenalty = countPenalty();
                        Schedule.copyArray(solution, newSolution);
                    } else {
                        Schedule.copyArray(newSolution, solution);
                    }
                }
            } else {
                Schedule.copyArray(newSolution, solution);
            }
            TAwal = TAwal * coolingrate;
            System.out.println("Iterasi : " + (i+1) + /**" suhu : " + TAwal + " diff : " + diff + " d : " + d + " prob : " + prob + **/ " penalti : " + countPenalty());
            if ((i+1)%10000 == 0){
                plot[p][0] = i+1;
                plot[p][1] = TAwal;
                plot[p][2] = currPenalty;
                plot[p][3] = bestPenalty;
                p = p+1;
            }
        }
        System.out.println(bestPenalty);
        for (int j = 0; j < plot.length; j++) {
            for (int k = 0; k < plot[j].length; k++) {
                System.out.print(plot[j][k] + " ");
            }
            System.out.println();
        }
    }

    public void RL_SA () throws IOException {
        for (int e = 0; e < 3; e++) {
            int[] score = {500, 500, 500};
            int score2Exchange = score[0];
            int score3Exchange = score[1];
            int scoredoubleExchange = score[2];
            int day = Schedule.planningHorizon[(Schedule.file - 1)] * 7;
            int[][] newSolution = new int[Schedule.emp.length][day];
            int[][] baseSolution = new int[Schedule.emp.length][day];
            double currPenalty;
            double bestPenalty;
            currPenalty = bestPenalty = countPenalty();
            double TAwal = 10000000;
            double coolingrate = 0.99995;
            double diff = 0;
            double prob = 0;
            double penalty = countPenalty();
            double[][] plot = new double[101][3];
            int p = 0;
            long startTime = System.nanoTime();
            Schedule.copyArray(solution, newSolution);
            Schedule.copyArray(solution, baseSolution);
            for (int i = 0; i < 1000000; i++) {
                double epsilon = 1 / (Math.sqrt(i));
                if (Math.random() < epsilon) {
                    int llh = (int) (Math.random() * 3);
                    switch (llh) {
                        case 0:
                            Schedule.twoExchange(solution);
                        case 1:
                            Schedule.threeExchange(solution);
                        case 2:
                            Schedule.doubleTwoExchange(solution);
                    }
                } else {
                    if (score2Exchange > score3Exchange && score2Exchange > scoredoubleExchange) {
                        Schedule.twoExchange(solution);
                        if (countPenalty() <= bestPenalty) {
                            if (score2Exchange < 1000) {
                                score2Exchange = score2Exchange + 10;
                            } else {
                                score2Exchange = 1000;
                            }
                        } else {
                            if (score2Exchange > 0) {
                                score2Exchange = score2Exchange - 10;
                            } else {
                                score2Exchange = 0;
                            }
                        }
                    }
                    if (score3Exchange > score2Exchange && score3Exchange > scoredoubleExchange) {
                        Schedule.threeExchange(solution);
                        if (countPenalty() <= bestPenalty) {
                            if (score3Exchange < 1000) {
                                score3Exchange = score3Exchange + 10;
                            } else {
                                score3Exchange = 1000;
                            }
                        } else {
                            if (score3Exchange > 0) {
                                score3Exchange = score3Exchange - 10;
                            } else {
                                score3Exchange = 0;
                            }
                        }
                    }
                    if (scoredoubleExchange > score2Exchange && scoredoubleExchange > score3Exchange) {
                        Schedule.doubleTwoExchange(solution);
                        if (countPenalty() <= bestPenalty) {
                            if (scoredoubleExchange < 1000) {
                                scoredoubleExchange = scoredoubleExchange + 10;
                            } else {
                                scoredoubleExchange = 1000;
                            }
                        } else {
                            if (scoredoubleExchange > 0) {
                                scoredoubleExchange = scoredoubleExchange - 10;
                            } else {
                                scoredoubleExchange = 0;
                            }
                        }
                    }
                    if (score2Exchange == score3Exchange && score2Exchange == scoredoubleExchange) {
                        if (Math.random() < 0.3) {
                            Schedule.twoExchange(solution);
                            if (countPenalty() <= bestPenalty) {
                                if (score2Exchange < 1000) {
                                    score2Exchange = score2Exchange + 10;
                                } else {
                                    score2Exchange = 1000;
                                }
                            } else {
                                if (score2Exchange > 0) {
                                    score2Exchange = score2Exchange - 10;
                                } else {
                                    score2Exchange = 0;
                                }
                            }
                        }
                        if (Math.random() > 0.3 && Math.random() < 0.6) {
                            Schedule.threeExchange(solution);
                            if (countPenalty() <= bestPenalty) {
                                if (score3Exchange < 1000) {
                                    score3Exchange = score3Exchange + 10;
                                } else {
                                    score3Exchange = 1000;
                                }
                            } else {
                                if (score3Exchange > 0) {
                                    score3Exchange = score3Exchange - 10;
                                } else {
                                    score3Exchange = 0;
                                }
                            }
                        }
                        if (Math.random() > 0.6) {
                            Schedule.doubleTwoExchange(solution);
                            if (countPenalty() <= bestPenalty) {
                                if (scoredoubleExchange < 1000) {
                                    scoredoubleExchange = scoredoubleExchange + 10;
                                } else {
                                    scoredoubleExchange = 1000;
                                }
                            } else {
                                if (scoredoubleExchange > 0) {
                                    scoredoubleExchange = scoredoubleExchange - 10;
                                } else {
                                    scoredoubleExchange = 0;
                                }
                            }
                        }
                    }
                    if (score2Exchange == scoredoubleExchange && score2Exchange > score3Exchange) {
                        if (Math.random() < 0.5) {
                            Schedule.twoExchange(solution);
                            if (countPenalty() <= bestPenalty) {
                                if (score2Exchange < 1000) {
                                    score2Exchange = score2Exchange + 10;
                                } else {
                                    score2Exchange = 1000;
                                }
                            } else {
                                if (score2Exchange > 0) {
                                    score2Exchange = score2Exchange - 10;
                                } else {
                                    score2Exchange = 0;
                                }
                            }
                        }
                        if (Math.random() > 0.5) {
                            Schedule.doubleTwoExchange(solution);
                            if (countPenalty() <= bestPenalty) {
                                if (scoredoubleExchange < 1000) {
                                    scoredoubleExchange = scoredoubleExchange + 10;
                                } else {
                                    scoredoubleExchange = 1000;
                                }
                            } else {
                                if (scoredoubleExchange > 0) {
                                    scoredoubleExchange = scoredoubleExchange - 10;
                                } else {
                                    scoredoubleExchange = 0;
                                }
                            }
                        }
                    }
                    if (score2Exchange == score3Exchange && score2Exchange > scoredoubleExchange) {
                        if (Math.random() < 0.5) {
                            Schedule.twoExchange(solution);
                            if (countPenalty() <= bestPenalty) {
                                if (score2Exchange < 1000) {
                                    score2Exchange = score2Exchange + 10;
                                } else {
                                    score2Exchange = 1000;
                                }
                            } else {
                                if (score2Exchange > 0) {
                                    score2Exchange = score2Exchange - 10;
                                } else {
                                    score2Exchange = 0;
                                }
                            }
                        }
                        if (Math.random() > 0.5) {
                            Schedule.threeExchange(solution);
                            if (countPenalty() <= bestPenalty) {
                                if (score3Exchange < 1000) {
                                    score3Exchange = score3Exchange + 10;
                                } else {
                                    score3Exchange = 1000;
                                }
                            } else {
                                if (score3Exchange > 0) {
                                    score3Exchange = score3Exchange - 10;
                                } else {
                                    score3Exchange = 0;
                                }
                            }
                        }
                    }
                    if (score3Exchange == scoredoubleExchange && score3Exchange > score2Exchange) {
                        if (Math.random() < 0.5) {
                            Schedule.threeExchange(solution);
                            if (countPenalty() <= bestPenalty) {
                                if (score3Exchange < 1000) {
                                    score3Exchange = score3Exchange + 10;
                                } else {
                                    score3Exchange = 1000;
                                }
                            } else {
                                if (score3Exchange > 0) {
                                    score3Exchange = score3Exchange - 10;
                                } else {
                                    score3Exchange = 0;
                                }
                            }
                        }
                        if (Math.random() > 0.5) {
                            Schedule.doubleTwoExchange(solution);
                            if (countPenalty() <= bestPenalty) {
                                if (scoredoubleExchange < 1000) {
                                    scoredoubleExchange = scoredoubleExchange + 10;
                                } else {
                                    scoredoubleExchange = 1000;
                                }
                            } else {
                                if (scoredoubleExchange > 0) {
                                    scoredoubleExchange = scoredoubleExchange - 10;
                                } else {
                                    scoredoubleExchange = 0;
                                }
                            }
                        }
                    }
                }
//
//            System.out.println("2Exchange : " +score2Exchange+ "\t 3Exchange : " +score3Exchange+ "\t double : " +scoredoubleExchange);
//            System.out.println(currPenalty);
//            System.out.println(countPenalty());

//            System.out.println(prob);
                if (Schedule.validAll(solution) == 0) {
                    diff = countPenalty() - currPenalty;
                    prob = Math.exp(-(Math.abs(diff) / TAwal));
//                System.out.println("feasible");
                    if (countPenalty() <= currPenalty) {
                        currPenalty = countPenalty();
                        Schedule.copyArray(solution, newSolution);
                        if (currPenalty <= bestPenalty) {
                            bestPenalty = currPenalty;
                            Schedule.copyArray(solution, newSolution);
                        } else {
                            Schedule.copyArray(newSolution, solution);
                        }
                    } else {
                        if (prob >= Math.random()) {
                            currPenalty = countPenalty();
                            Schedule.copyArray(solution, newSolution);
                        } else {
                            Schedule.copyArray(newSolution, solution);
                        }
                    }
                } else {
                    Schedule.copyArray(newSolution, solution);
                }
                TAwal = TAwal * coolingrate;
                System.out.println("Iterasi : " + (i + 1) + /**" suhu : " + TAwal + " diff : " + diff +  " prob : " + prob + **/" penalti : " + countPenalty());
                if ((i + 1) % 10000 == 0) {
                    plot[p][0] = TAwal;
                    plot[p][1] = currPenalty;
                    plot[p][2] = bestPenalty;
                    p = p + 1;
                }
            }

            long endTime = System.nanoTime();
            long time = (endTime-startTime) / 1000000000;
            plot [100][0] = time;
            Schedule.saveOptimation(plot, e);
//            System.out.println(penalty);
            Schedule.copyArray(baseSolution, solution);

//            System.out.println(bestPenalty);
//            for (int j = 0; j < plot.length; j++) {
//                for (int k = 1; k < plot[j].length; k++) {
//                    System.out.print(plot[j][k] + " ");
//                }
//                System.out.println();
//            }
        }
    }

    public static boolean stuck(double currPenalty, double previousCost, double currentStagnatCount) {
        double diff = currPenalty - previousCost;
        if (diff < 0.01) {
            currentStagnatCount = currentStagnatCount + 1;
        } else {
            currentStagnatCount = 0;
        }
        if (currentStagnatCount > 5) {
            return true;
        } else {
            return false;
        }
    }

    public void SAR() {
        int day = Schedule.planningHorizon[(Schedule.file - 1)] * 7;
        int [][] newSolution = new int [Schedule.emp.length][day];
        int [][] bestSolution = new int [Schedule.emp.length][day];
        Schedule.copyArray(solution, newSolution);
        Schedule.copyArray(solution, bestSolution);
        double bestPenalty; double currPenalty;
        bestPenalty = currPenalty = countPenalty();
        double TAwal = 10000000; //countPenalty() * 0.01;
        double coolingrate = 0.99995;
        double diff = 0;
        double d = 0;
        double prob = 0;
        double currentStagnantCount = 0;
        double previousCost = countPenalty();
        double stuckedBestCost = countPenalty();
        double stuckedCurrentCost = countPenalty();
        double [] prevCost = new double[1000000];

        double heat = 0;
        double[][] plot = new double[100][4];
        int p = 0;
//
        for (int i = 0; i < 1000000; i++) {
            int llh = (int) (Math.random() * 3);
            if (llh == 0)
                Schedule.twoExchange(solution);
            if (llh == 1)
                Schedule.threeExchange(solution);
            if (llh == 2)
                Schedule.doubleTwoExchange(solution);
//            currPenalty = countPenalty();
            if (Schedule.validAll(solution) == 0) {
                diff = countPenalty() - currPenalty;
                d = Math.abs(diff)/TAwal;
                prob = Math.exp(-d);
//                System.out.println("feasible");
                if (countPenalty() <= currPenalty) {
                    currPenalty = countPenalty();
                    Schedule.copyArray(solution, newSolution);
                    if (currPenalty <= bestPenalty) {
                        bestPenalty = currPenalty;
                        Schedule.copyArray(solution, newSolution);
                        Schedule.copyArray(solution, bestSolution);
                    } else {
                        Schedule.copyArray(newSolution, solution);
                    }
                } else {
                    if (prob >= Math.random()) {
//                        System.out.println("solusi jelek diterima");
                        currPenalty = countPenalty();
                        Schedule.copyArray(solution, newSolution);
                    } else {
                        Schedule.copyArray(newSolution, solution);
                    }
                }
                if (i > 0) {
                    if (Math.abs(currPenalty - prevCost[i-1]) <= 0.01) {
//                    System.out.println("stuck");
                        currentStagnantCount = currentStagnantCount + 1;
//                    System.out.println("stagnant " + currentStagnantCount);
                        if (currentStagnantCount >= 10000) {
                            Schedule.copyArray(bestSolution, solution);
                            if (bestPenalty == stuckedBestCost) {
                                if (currPenalty - stuckedCurrentCost < 0.02) {
                                    heat = heat + 1;
                                } else {
                                    heat = 0;
                                }
                            } else {
                                heat = 0;
                            }
                            currentStagnantCount = 0;
//                            System.out.println("heat " + heat);
                            TAwal = (heat * 0.2 * currPenalty + currPenalty) * 0.01;
                            stuckedBestCost = bestPenalty;
                            stuckedCurrentCost = currPenalty;
                        }
                        heat = 0;
                    }
                }
            } else {
                Schedule.copyArray(newSolution, solution);
            }
            TAwal = TAwal * coolingrate;
            System.out.println("Iterasi : " + (i+1) + /**" suhu : " + TAwal + " diff : " + diff + " d : " + d + " prob : " + prob + **/ " penalti : " + countPenalty());
            if ((i+1)%10000 == 0) {
                plot[p][0] = i+1;
                plot[p][1] = TAwal;
                plot[p][2] = currPenalty;
                plot[p][3] = bestPenalty;
                p = p+1;
            }
            prevCost[i] = currPenalty;
        }
        System.out.println(bestPenalty);
        for (int j = 0; j < plot.length; j++) {
            for (int k = 0; k < plot[j].length; k++) {
                System.out.print(plot[j][k] + " ");
            }
            System.out.println();
        }
    }

    public void RL_SAR() throws IOException {
        for (int e = 0; e < 3; e++) {
            int[] score = {500, 500, 500};
            int score2Exchange = score[0];
            int score3Exchange = score[1];
            int scoredoubleExchange = score[2];
            int day = Schedule.planningHorizon[(Schedule.file - 1)] * 7;
            int[][] newSolution = new int[Schedule.emp.length][day];
            int[][] baseSolution = new int[Schedule.emp.length][day];
            double currPenalty;
            double bestPenalty;
            currPenalty = bestPenalty = countPenalty();
            double TAwal = 10000000;
            double coolingrate = 0.99995;
            double diff = 0;
            double prob = 0;
            double d = 0;
            double penalty = countPenalty();
            Schedule.copyArray(solution, newSolution);
            Schedule.copyArray(solution, baseSolution);
            double stuckedBestCost = 0;
            double stuckedCurrentCost = 0;
            double currentStagnantCount = 0;
            double heat = 0;
            int reheating = 0;
            double discountFactor = 0.9;
//        double epsilon = 0.1;
            double[] prevCost = new double[1000000];
            double[][] plot = new double[101][3];
            int p = 0;
            long startTime = System.nanoTime();
            for (int i = 0; i < 1000000; i++) {
                double epsilon = 1 / (Math.sqrt(i));
                if (Math.random() < epsilon) {
                    int llh = (int) (Math.random() * 3);
                    switch (llh) {
                        case 0:
                            Schedule.twoExchange(solution);
                        case 1:
                            Schedule.threeExchange(solution);
                        case 2:
                            Schedule.doubleTwoExchange(solution);
                    }
                } else {
                    if (score2Exchange > score3Exchange && score2Exchange > scoredoubleExchange) {
                        Schedule.twoExchange(solution);
                        if (countPenalty() <= bestPenalty) {
                            if (score2Exchange < 1000) {
//                            score2Exchange = (score2Exchange + 10) * (int) (Math.pow(discountFactor, i));
//                            score2Exchange = (i * score2Exchange + 10) / i;
                                score2Exchange = score2Exchange + 10;
                            } else {
                                score2Exchange = 1000;
                            }
                        } else {
                            if (score2Exchange > 0) {
//                            score2Exchange = (score2Exchange - 10) * (int) (Math.pow(discountFactor, i));
//                            score2Exchange = (i * score2Exchange - 10) / i;
                                score2Exchange = score2Exchange - 10;
                            } else {
                                score2Exchange = 0;
                            }
                        }
                    }
                    if (score3Exchange > score2Exchange && score3Exchange > scoredoubleExchange) {
                        Schedule.threeExchange(solution);
                        if (countPenalty() <= bestPenalty) {
                            if (score3Exchange < 1000) {
//                            score3Exchange = (score3Exchange + 10) * (int) (Math.pow(discountFactor, i));
//                            score3Exchange = (i * score3Exchange + 10) / i;
                                score3Exchange = score3Exchange + 10;
                            } else {
                                score3Exchange = 1000;
                            }
                        } else {
                            if (score3Exchange > 0) {
//                            score3Exchange = (score3Exchange - 10) * (int) (Math.pow(discountFactor, i));
//                            score3Exchange = (i * score3Exchange - 10) / i;
                                score3Exchange = score3Exchange - 10;
                            } else {
                                score3Exchange = 0;
                            }
                        }
                    }
                    if (scoredoubleExchange > score2Exchange && scoredoubleExchange > score3Exchange) {
                        Schedule.doubleTwoExchange(solution);
                        if (countPenalty() <= bestPenalty) {
                            if (scoredoubleExchange < 1000) {
//                            scoredoubleExchange = (scoredoubleExchange + 10) * (int) (Math.pow(discountFactor, i));
//                            scoredoubleExchange = (i * scoredoubleExchange + 10) / i;
                                scoredoubleExchange = scoredoubleExchange + 10;
                            } else {
                                scoredoubleExchange = 1000;
                            }
                        } else {
                            if (scoredoubleExchange > 0) {
//                            scoredoubleExchange = (scoredoubleExchange - 10) * (int) (Math.pow(discountFactor, i));
//                            scoredoubleExchange = (i * scoredoubleExchange - 10) / i;
                                scoredoubleExchange = scoredoubleExchange - 10;
                            } else {
                                scoredoubleExchange = 0;
                            }
                        }
                    }
                    if (score2Exchange == score3Exchange && score2Exchange == scoredoubleExchange) {
                        if (Math.random() < 0.3) {
                            Schedule.twoExchange(solution);
                            if (countPenalty() <= bestPenalty) {
                                if (score2Exchange < 1000) {
//                                score2Exchange = (score2Exchange + 10) * (int) (Math.pow(discountFactor, i));
//                                score2Exchange = (i * score2Exchange + 10) / i;
                                    score2Exchange = score2Exchange + 10;
                                } else {
                                    score2Exchange = 1000;
                                }
                            } else {
                                if (score2Exchange > 0) {
//                                score2Exchange = (score2Exchange - 10) * (int) (Math.pow(discountFactor, i));
//                                score2Exchange = (i * score2Exchange - 10) / i;
                                    score2Exchange = score2Exchange - 10;
                                } else {
                                    score2Exchange = 0;
                                }
                            }
                        }
                        if (Math.random() > 0.3 && Math.random() < 0.6) {
                            Schedule.threeExchange(solution);
                            if (countPenalty() <= bestPenalty) {
                                if (score3Exchange < 1000) {
//                                score3Exchange = (score3Exchange + 10) * (int) (Math.pow(discountFactor, i));
//                                score3Exchange = (i * score3Exchange + 10) / i;
                                    score3Exchange = score3Exchange + 10;
                                } else {
                                    score3Exchange = 1000;
                                }
                            } else {
                                if (score3Exchange > 0) {
//                                score3Exchange = (score3Exchange - 10) * (int) (Math.pow(discountFactor, i));
//                                score3Exchange = (i * score3Exchange - 10) / i;
                                    score3Exchange = score3Exchange - 10;
                                } else {
                                    score3Exchange = 0;
                                }
                            }
                        }
                        if (Math.random() > 0.6) {
                            Schedule.doubleTwoExchange(solution);
                            if (countPenalty() <= bestPenalty) {
                                if (scoredoubleExchange < 1000) {
//                                scoredoubleExchange = (scoredoubleExchange + 10) * (int) (Math.pow(discountFactor, i));
//                                scoredoubleExchange = (i * scoredoubleExchange + 10) / i;
                                    scoredoubleExchange = scoredoubleExchange + 10;
                                } else {
                                    scoredoubleExchange = 1000;
                                }
                            } else {
                                if (scoredoubleExchange > 0) {
//                                scoredoubleExchange = (scoredoubleExchange - 10) * (int) (Math.pow(discountFactor, i));
//                                scoredoubleExchange = (i * scoredoubleExchange - 10) / i;
                                    scoredoubleExchange = scoredoubleExchange - 10;
                                } else {
                                    scoredoubleExchange = 0;
                                }
                            }
                        }
                    }
                    if (score2Exchange == scoredoubleExchange && score2Exchange > score3Exchange) {
                        if (Math.random() < 0.5) {
                            Schedule.twoExchange(solution);
                            if (countPenalty() <= bestPenalty) {
                                if (score2Exchange < 1000) {
//                                score2Exchange = (score2Exchange + 10) * (int) (Math.pow(discountFactor, i));
//                                score2Exchange = (i * score2Exchange + 10) / i;
                                    score2Exchange = score2Exchange + 10;
                                } else {
                                    score2Exchange = 1000;
                                }
                            } else {
                                if (score2Exchange > 0) {
//                                score2Exchange = (score2Exchange - 10) * (int) (Math.pow(discountFactor, i));
//                                score2Exchange = (i * score2Exchange - 10) / i;
                                    score2Exchange = score2Exchange - 10;
                                } else {
                                    score2Exchange = 0;
                                }
                            }
                        }
                        if (Math.random() > 0.5) {
                            Schedule.doubleTwoExchange(solution);
                            if (countPenalty() <= bestPenalty) {
                                if (scoredoubleExchange < 1000) {
//                                scoredoubleExchange = (scoredoubleExchange + 10) * (int) (Math.pow(discountFactor, i));
//                                scoredoubleExchange = (i * scoredoubleExchange + 10) / i;
                                    scoredoubleExchange = scoredoubleExchange + 10;
                                } else {
                                    scoredoubleExchange = 1000;
                                }
                            } else {
                                if (scoredoubleExchange > 0) {
//                                scoredoubleExchange = (scoredoubleExchange -10) * (int) (Math.pow(discountFactor, i));
//                                scoredoubleExchange = (i * scoredoubleExchange - 10) / i;
                                    scoredoubleExchange = scoredoubleExchange - 10;
                                } else {
                                    scoredoubleExchange = 0;
                                }
                            }
                        }
                    }
                    if (score2Exchange == score3Exchange && score2Exchange > scoredoubleExchange) {
                        if (Math.random() < 0.5) {
                            Schedule.twoExchange(solution);
                            if (countPenalty() <= bestPenalty) {
                                if (score2Exchange < 1000) {
//                                score2Exchange = (score2Exchange + 10) * (int) (Math.pow(discountFactor, i));
//                                score2Exchange = (i * score2Exchange + 10) / i;
                                    score2Exchange = score2Exchange + 10;
                                } else {
                                    score2Exchange = 1000;
                                }
                            } else {
                                if (score2Exchange > 0) {
//                                score2Exchange = (score2Exchange - 10) * (int) (Math.pow(discountFactor, i));
//                                score2Exchange = (i * score2Exchange - 10) / i;
                                    score2Exchange = score2Exchange - 10;
                                } else {
                                    score2Exchange = 0;
                                }
                            }
                        }
                        if (Math.random() > 0.5) {
                            Schedule.threeExchange(solution);
                            if (countPenalty() <= bestPenalty) {
                                if (score3Exchange < 1000) {
//                                score3Exchange = (score3Exchange + 10) * (int) (Math.pow(discountFactor, i));
//                                score3Exchange = (i * score3Exchange + 10) / i;
                                    score3Exchange = score3Exchange + 10;
                                } else {
                                    score3Exchange = 1000;
                                }
                            } else {
                                if (score3Exchange > 0) {
//                                score3Exchange = (score3Exchange - 10) * (int) (Math.pow(discountFactor, i));
//                                score3Exchange = (i * score3Exchange - 10) / i;
                                    score3Exchange = score3Exchange - 10;
                                } else {
                                    score3Exchange = 0;
                                }
                            }
                        }
                    }
                    if (score3Exchange == scoredoubleExchange && score3Exchange > score2Exchange) {
                        if (Math.random() < 0.5) {
                            Schedule.threeExchange(solution);
                            if (countPenalty() <= bestPenalty) {
                                if (score3Exchange < 1000) {
//                                score3Exchange = (score3Exchange + 10) * (int) (Math.pow(discountFactor, i));
//                                score3Exchange = (i * score3Exchange + 10) / i;
                                    score3Exchange = score3Exchange + 10;
                                } else {
                                    score3Exchange = 1000;
                                }
                            } else {
                                if (score3Exchange > 0) {
//                                score3Exchange = (score3Exchange - 10) * (int) (Math.pow(discountFactor, i));
//                                score3Exchange = (i * score3Exchange - 10) / i;
                                    score3Exchange = score3Exchange - 10;
                                } else {
                                    score3Exchange = 0;
                                }
                            }
                        }
                        if (Math.random() > 0.5) {
                            Schedule.doubleTwoExchange(solution);
                            if (countPenalty() <= bestPenalty) {
                                if (scoredoubleExchange < 1000) {
//                                scoredoubleExchange = (scoredoubleExchange + 10) * (int) (Math.pow(discountFactor, i));
//                                scoredoubleExchange = (i * scoredoubleExchange + 10) / i;
                                    scoredoubleExchange = scoredoubleExchange + 10;
                                } else {
                                    scoredoubleExchange = 1000;
                                }
                            } else {
                                if (scoredoubleExchange > 0) {
//                                scoredoubleExchange = (scoredoubleExchange - 10) * (int) (Math.pow(discountFactor, i));
//                                scoredoubleExchange = (i * scoredoubleExchange - 10) / i;
                                    scoredoubleExchange = scoredoubleExchange - 10;
                                } else {
                                    scoredoubleExchange = 0;
                                }
                            }
                        }
                    }
                }
//            System.out.println("2Exchange : " +score2Exchange+ "\t 3Exchange : " +score3Exchange+ "\t double : " +scoredoubleExchange);
//            System.out.println(currPenalty);
//            System.out.println(countPenalty());

//            System.out.println(prob);
                if (Schedule.validAll(solution) == 0) {
                    diff = countPenalty() - currPenalty;
                    d = Math.abs(diff) / TAwal;
                    prob = Math.exp(-d);
//                System.out.println("feasible");
                    if (countPenalty() <= currPenalty) {
                        currPenalty = countPenalty();
                        Schedule.copyArray(solution, newSolution);
                        if (currPenalty <= bestPenalty) {
                            bestPenalty = currPenalty;
                            Schedule.copyArray(solution, newSolution);
//                        Schedule.copyArray(solution, bestSolution);
                        } else {
                            Schedule.copyArray(newSolution, solution);
                        }
                    } else {
                        if (prob >= Math.random()) {
//                        System.out.println("solusi jelek diterima");
                            currPenalty = countPenalty();
                            Schedule.copyArray(solution, newSolution);
                        } else {
                            Schedule.copyArray(newSolution, solution);
                        }
                    }
                    if (i > 0 && reheating <= 5) {
                        if (Math.abs(currPenalty - prevCost[i - 1]) <= 0.01) {
//                    System.out.println("stuck");
                            currentStagnantCount = currentStagnantCount + 1;
//                    System.out.println("stagnant " + currentStagnantCount);
                            if (currentStagnantCount >= 50000) {
//                            Schedule.copyArray(bestSolution, solution);
                                if (bestPenalty == stuckedBestCost) {
                                    if (currPenalty - stuckedCurrentCost < 0.02) {
                                        heat = heat + 1;
                                    } else {
                                        heat = 0;
                                    }
                                } else {
                                    heat = 0;
                                }
                                currentStagnantCount = 0;
//                            System.out.println("heat " + heat);
                                TAwal = (heat * 0.2 * currPenalty + currPenalty) * 0.01;
                                stuckedBestCost = bestPenalty;
                                stuckedCurrentCost = currPenalty;
                                reheating++;
                            }
                            heat = 0;
                        }
                    }
                } else {
                    Schedule.copyArray(newSolution, solution);
                }
                TAwal = TAwal * coolingrate;
                System.out.println("Iterasi : " + (i + 1) + /**" suhu : " + TAwal + " diff : " + diff +  " prob : " + prob + **/" penalti : " + countPenalty());
                if ((i + 1) % 10000 == 0) {
                    plot[p][0] = TAwal;
                    plot[p][1] = currPenalty;
                    plot[p][2] = bestPenalty;
                    p = p + 1;
                }
                prevCost[i] = currPenalty;
            }

            long endTime = System.nanoTime();
            long time = (endTime-startTime) / 1000000000;
            plot [100][0] = time;
            Schedule.saveOptimation(plot, e);
//            System.out.println(penalty);
            Schedule.copyArray(baseSolution, solution);
//            System.out.println(bestPenalty);
//            for (int j = 0; j < plot.length; j++) {
//                for (int k = 1; k < plot[j].length; k++) {
//                    System.out.print(plot[j][k] + " ");
//                }
//                System.out.println();
//            }
        }
    }
}
