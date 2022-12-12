package com.defalt.apv.report;

public class GradeInfo {
    private final double score;
    private final Grade grade;

    private GradeInfo(double score, Grade grade) {
        this.score = score;
        this.grade = grade;
    }

    public static GradeInfo forScore(double score) {
        if (score < Grade.TWO.minScore)
            throw new IllegalArgumentException("Score cannot be negative!");
        if (score <= Grade.TWO.maxScore)
            return new GradeInfo(score, Grade.TWO);
        if (score <= Grade.THREE.maxScore)
            return new GradeInfo(score, Grade.THREE);
        if (score <= Grade.FOUR.maxScore)
            return new GradeInfo(score, Grade.FOUR);
        if (score <= Grade.FIVE.maxScore)
            return new GradeInfo(score, Grade.FIVE);
        throw new IllegalArgumentException("Score cannot be greater than 100!");
    }

    public double getScore() {
        return score;
    }

    public Grade getGrade() {
        return grade;
    }

    @Override
    public String toString() {
        return String.format("%s (%s)", grade.value, Math.round(score));
    }
}
