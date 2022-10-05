package com.defalt.apv.util.parser;

import com.defalt.apv.report.ReportCard;

public interface ReportCardParser {
    ReportCard parse(String courseName, String filePath);
}
