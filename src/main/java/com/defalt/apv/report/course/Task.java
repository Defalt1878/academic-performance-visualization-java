package com.defalt.apv.report.course;

public record Task(String name, int score) {

    @Override
    public String toString() {
        return String.format("%s (Score: %s)", name, score);
    }
}
