package report.course;

public class Task {
    private final String name;

    private int score;

    public Task(String name) {
        this(name, 0);
    }

    public Task(String name, int score) {
        this.name = name;
        this.score = score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getName() {
        return name;
    }

    public int getScore() {
        return score;
    }

    @Override
    public String toString() {
        return String.format("%s (Score: %s)", name, score);
    }
}
