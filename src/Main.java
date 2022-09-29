import parser.ReportParserCVS;

public class Main {
    public static void main(String[] args) {
        var path = "basicprogramming_2.csv";
        var parser = new ReportParserCVS();
        var report = parser.parse("C#", path);
    }
}
