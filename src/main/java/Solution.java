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
}
