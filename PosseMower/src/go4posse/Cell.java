package go4posse;

public class Cell {
    
    int x, y;
    Mower.Heading onStart;
    Mower.Command afterEntry;

    public Cell(int x, int y, Mower.Heading onStart, Mower.Command afterEntry) {
        this.x = x;
        this.y = y;
        this.onStart = onStart;
        this.afterEntry = afterEntry;
    }
    
}
