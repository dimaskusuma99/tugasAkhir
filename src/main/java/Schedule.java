import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import java.io.File;
import java.io.IOException;
import java.sql.SQLOutput;
import java.sql.Time;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;

public class Schedule {
    static int last = 0;
    static int side = 0;
    static int sidex = 0;
    static int first = 5;
    static Row row;

    public static final String XLSX_FILE_PATH =
            "D:\\ITS\\Semester 8\\Tugas Akhir\\Nurse Rostering\\nurserost\\OpTur1.xls";

    public static void main(String[] args) throws IOException, InvalidFormatException, ParseException {
//        employees();
//        shifts();
//        manpower();
//        softconstraint();
        hardconstraint();
//        wantedpattern();
    }

    public static void employees() throws IOException, InvalidFormatException {
        Workbook workbook = WorkbookFactory.create(new File(XLSX_FILE_PATH));
        Sheet sheet = workbook.getSheetAt(0);
        DataFormatter dataFormatter = new DataFormatter();

        for (int i = first; i <= sheet.getLastRowNum(); i++) {
            row = sheet.getRow(i);
            for (Cell cell: row) {
                side++;
//                if(row == sheet.getRow(sheet.getLastRowNum()))
//                    cell.getAddress();
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
        }
        String [][] weekend = new String[data.length][];
        for(int i=0; i < data.length; i++)
            weekend[i] = data[i][2].split(", |,");
        int kanan = 0;
        for(int i=0; i < weekend.length; i++){
            if (kanan < weekend[i].length)
                kanan = weekend[i].length;
        }
        int [][] week = new int[weekend.length][kanan];
        for(int i=0; i < weekend.length; i++) {
            for (int j = 0; j < weekend[i].length; j++) {
                week[i][j] = Integer.parseInt(weekend[i][j]);
                System.out.print(week[i][j] + "\t");
            }
            System.out.println();
        }

        Employee[] emp = new Employee[last];
        for (int i = 0; i < last; i++){
            emp[i]= new Employee(data[i][0], data[i][1], week[i], data[i][3]);
//            System.out.println(emp[i].getId() + "\t" + emp[i].getHours() + "\t" + week[i] + "\t" +
//                                emp[i].getCompetence());
        }
    }

    public static void shifts() throws IOException, InvalidFormatException, ParseException {
        Workbook workbook = WorkbookFactory.create(new File(XLSX_FILE_PATH));
        Sheet sheet = workbook.getSheetAt(1);
        DataFormatter dataFormatter = new DataFormatter();

        for (int i = first-1; i <= sheet.getLastRowNum(); i++) {
            row = sheet.getRow(i);
            for (Cell cell: row) {
                side++;
//                if(row == sheet.getRow(sheet.getLastRowNum()))
//                    cell.getAddress();
            }
            if (sidex < side)
                sidex = side;
            side = 0;
            last++;
        }
        String [][] data = new String[last][sidex];
//        System.out.println(last + " " + sidex);
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
        DateFormat time = new SimpleDateFormat("hh:mm");
        LocalTime[][] times = new LocalTime[data.length][2];
        for (int i = 0; i < data.length; i++) {
            String first = data[i][10]+":"+data[i][11];
            String last = data[i][12]+":"+data[i][13];
            Time start = new Time(time.parse(first).getTime());
            Time finish = new Time(time.parse(last).getTime());
            LocalTime startx = start.toLocalTime();
            LocalTime finishx = finish.toLocalTime();
            times[i][0] = startx;
            times[i][1] = finishx;
        }

        Shift[] shift = new Shift[last];
        for (int i = 0; i < last; i++){
            shift[i]= new Shift(data[i][0], data[i][1], data[i][2], data[i][3], data[i][4], data[i][5],
                                data[i][6], data[i][7], data[i][8], data[i][9], times[i][0], times[i][1],
                                data[i][14]);
            System.out.println(shift[i].getId() + "\t" + shift[i].getMonday() + "\t" + shift[i].getTuesday() + "\t" +
                                shift[i].getWednesday() + "\t" + shift[i].getThursday() + "\t" + shift[i].getFriday() + "\t" +
                                shift[i].getSaturday() + "\t" + shift[i].getSunday() + "\t" + shift[i].getCategory() + "\t" +
                                shift[i].getName() + "\t" + shift[i].getCompetence());
        }
    }

    public static void manpower() throws IOException, InvalidFormatException {
        Workbook workbook = WorkbookFactory.create(new File(XLSX_FILE_PATH));
        Sheet sheet = workbook.getSheetAt(2);
        DataFormatter dataFormatter = new DataFormatter();

        for (int i = first; i <= sheet.getLastRowNum(); i++) {
            row = sheet.getRow(i);
            for (Cell cell: row) {
                side++;
//                if(row == sheet.getRow(sheet.getLastRowNum()))
//                    cell.getAddress();
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
        }
        Manpower[] plan = new Manpower[last];
        for (int i = 0; i < last; i++){
            plan[i]= new Manpower(data[i][0], data[i][1], data[i][2], data[i][3], data[i][4], data[i][5],
                                    data[i][6], data[i][7], data[i][8]);
            System.out.println(plan[i].getShift() + "\t" + plan[i].getMonday() + "\t" + plan[i].getTuesday() + "\t" +
                    plan[i].getWednesday() + "\t" + plan[i].getThursday() + "\t" + plan[i].getFriday() + "\t" +
                    plan[i].getSaturday() + "\t" + plan[i].getSunday() + "\t" + plan[i].getId());
        }
    }

    public static void softconstraint() throws IOException, InvalidFormatException {
        Workbook workbook = WorkbookFactory.create(new File(XLSX_FILE_PATH));
        Sheet sheet = workbook.getSheetAt(3);
        DataFormatter dataFormatter = new DataFormatter();

        for (int i = first-1; i <= 15; i++) {
            row = sheet.getRow(i);
            for (Cell cell: row) {
                side++;
//                if(row == sheet.getRow(sheet.getLastRowNum()))
//                    cell.getAddress();
            }
            if (sidex < side)
                sidex = side;
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
        Constraint[] soft = new Constraint[last];
        for (int i = 0; i < soft.length; i++){
            soft[i]= new Constraint(data[i][0], data[i][1], data[i][2], data[i][3]);
            System.out.println(soft[i].getValue());
        }
    }

    public static void hardconstraint() throws IOException, InvalidFormatException {
        Workbook workbook = WorkbookFactory.create(new File(XLSX_FILE_PATH));
        Sheet sheet = workbook.getSheetAt(3);
        DataFormatter dataFormatter = new DataFormatter();

        for (int i = first+13; i <= sheet.getLastRowNum(); i++) {
            row = sheet.getRow(i);
            for (Cell cell: row) {
                side++;
//                if(row == sheet.getRow(sheet.getLastRowNum()))
//                    cell.getAddress();
            }
            if (sidex < side)
                sidex = side;
            side = 0;
            last++;
        }
        String [][] data = new String[last][sidex];
//        System.out.println(last + " " + sidex);
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
        Constraint[] hard = new Constraint[last];
        for (int i = 0; i < hard.length; i++){
            hard[i]= new Constraint(data[i][0], data[i][1], data[i][2], data[i][3]);
//            Row hc1 = sheet.getRow(1);
//            System.out.println(hc1);
            System.out.println(hard[i].getValue());
        }
    }

    public static void wantedpattern() throws IOException, InvalidFormatException {
        Workbook workbook = WorkbookFactory.create(new File(XLSX_FILE_PATH));
        Sheet sheet = workbook.getSheetAt(4);
        DataFormatter dataFormatter = new DataFormatter();

        for (int i = first-1; i <= 12; i++) {
            row = sheet.getRow(i);
            for (Cell cell: row) {
                side++;
//                if(row == sheet.getRow(sheet.getLastRowNum()))
//                    cell.getAddress();
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