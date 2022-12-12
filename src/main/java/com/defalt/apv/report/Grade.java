package com.defalt.apv.report;

public enum Grade {
    TWO(2, 0, 39),
    THREE(3, 40, 59),
    FOUR(4, 60, 79),
    FIVE(5, 80, 100);

    public final int value;
    public final int minScore;
    public final int maxScore;

    Grade(int value, int minScore, int maxScore) {
        this.value = value;
        this.minScore = minScore;
        this.maxScore = maxScore;
    }


    @Override
    public String toString() {
        return String.format("%s (%s - %s)", value, minScore, maxScore);
    }
}
