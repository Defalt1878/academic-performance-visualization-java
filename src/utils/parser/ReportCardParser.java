package utils.parser;

import report.ReportCard;

public interface ReportCardParser {
    ReportCard parse(String courseName, String filePath);
}
