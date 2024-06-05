package Models;

public class Display {
    String Exercise;
    String Sets;
    String Reps;
    String Image;

    public Display( String Exercise, String Sets, String Reps, String Image) {
        this.Exercise = Exercise;
        this.Sets = Sets;
        this.Reps = Reps;
        this.Image = Image;

    }
    public String getExercise() {
        return Exercise;
    }
    public String getSets() {
        return Sets;
        }
    public String getReps() {
        return Reps;
    }
    public String getImage ( ) {
        return Image;
    }

}
