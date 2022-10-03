import report.person.Student;
import utils.parser.ReportCardParser;
import utils.parser.CsvReportParser;

public class Main {
    public static void main(String[] args) {
        var path = "misc/basicprogramming_2.csv";
        ReportCardParser parser = new CsvReportParser(Student::new);
        var report = parser.parse("C#", path);
    }
}
