package go4posse;

import go4posse.Field.Terrain;

public class Mower {

    public enum Heading { N, E, S, W }
    public enum Command { L, R, M }

    int from;
    int to;
    int current;
    Heading head;
    boolean alreadyTurned = false;
    StringBuffer commandHistory = new StringBuffer();

    public Mower(Terrain[][] geoMap, Cell[] cells, int from, int to, Heading head) {
        this.from = from;
        this.to = to;
        this.current = from;
        this.head = head;

        // Initial pos
        commandHistory.append(cells[from].x + " " + cells[from].y + " " + head.toString() + "\n");
        
        geoMap[cells[from].x][cells[from].y] = Terrain.L;
    }
    
    public void move(Terrain[][] geoMap, Cell[] cells) {
        if (current < to) {
            this.current++;
            commandHistory.append(Command.M.toString());
            alreadyTurned = false;
            geoMap[cells[current].x][cells[current].y] = Terrain.L;
        }
    }
    
    // (hopefully in, it is 0:30am)
    public void turn(Command com) {
        switch (com) {
        case L:
            commandHistory.append(com.toString());
            alreadyTurned = true;
            switch (head) {
            case N:
                head = Heading.W;
                break;
            case W:
                head = Heading.S;
                break;
            case S:
                head = Heading.E;
                break;
            case E:
                head = Heading.N;
                break;
            }
            break;
        case R:
            commandHistory.append(com.toString());
            alreadyTurned = true;
            switch (head) {
            case N:
                head = Heading.E;
                break;
            case E:
                head = Heading.S;
                break;
            case S:
                head = Heading.W;
                break;
            case W:
                head = Heading.N;
                break;
            }
            break;
        }
    }

}
