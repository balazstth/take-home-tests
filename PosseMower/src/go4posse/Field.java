package go4posse;

import java.util.ArrayList;

import tb.Q;
import go4posse.Mower;
import go4posse.Mower.Command;

public class Field {

    private int width;
    private int height;

    private int maxMowers;
    
    private ArrayList<Mower> mowers = new ArrayList<Mower>();
    
    // One dimensional array of cells of the field in a multiple S shape
    private Cell[] cells;
    
    /**
     * G: grass, L: mowed lawn
     */
    public enum Terrain { 
        G, L;
        
        @Override
        public String toString() {
            if (this == G) return "!";
            else return ".";
        }
    }
    
    private Terrain[][] geoMap;
    
    //------------------------------------------------------------------------
   
    public Field(int width, int height) throws PosseMowerPanic {
        if ((width < 1 || height < 1) || (width < 2 && height < 2)) {
            throw new PosseMowerPanic(
                    "Your mowers are inept in handling fields of this size: " + width + " by " + height + 
                    ". Minimum lawn dimensions are 1 by 2, please use a sickle for now.");
        }
        this.width = width;
        this.height = height;
        maxMowers = width * height;         // don't allow more mowers than the whole area to be deployed
        
        geoMap = new Terrain[width][height];
        Q.fill2(geoMap, Terrain.G);         // grow grass everywhere
        
        formCellArray();
    }
    
    //------------------------------------------------------------------------

    /**
     * Form a linear array of cells
     */
    public void formCellArray() {
        cells = new Cell[width * height];
                
        if (width >= height) {
            for (int i = 0; i < width; i++) {
                for (int j = 0; j < height; j++) {
                    Cell cell;
                    // Start cell
                    if (i == 0 && j == 0) {
                        cell = new Cell(i, j, Mower.Heading.E, Mower.Command.M);
                    // 0, even y
                    } else if (i == 0 && j % 2 == 0) {
                        cell = new Cell(i, j, Mower.Heading.E, Mower.Command.R);
                    // 0, odd y
                    } else if (i == 0 && j % 2 != 0) {
                        cell = new Cell(i, j, Mower.Heading.N, Mower.Command.R);
                    }
                    
                    // width - 1, even y
                    else if (i == width - 1 && j % 2 == 0) {
                        cell = new Cell(i, j, Mower.Heading.N, Mower.Command.L);
                    // width - 1, odd y
                    } else if (i == width - 1 && j % 2 != 0) {
                        cell = new Cell(i, j, Mower.Heading.W, Mower.Command.L);
                    }
                    
                    // even lines
                    else if (j % 2 == 0) {
                        cell = new Cell(i, j, Mower.Heading.E, Mower.Command.M);
                    // odd lines
                    } else if (j % 2 != 0) {
                        cell = new Cell(i, j, Mower.Heading.W, Mower.Command.M);
                    }

                    // Something went awfully wrong, uncovered cell found
                    else {
                        Q.con("Examine me!!! formCellArray()");
                        cell = null;
                    }
                    
                    int pos;
                    if (j == 0) {
                        pos = i;
                    } else if (j % 2 == 0) {
                        pos = j * width + i;
                    } else if (j % 2 != 0) {
                        pos = j * width + width - i - 1;
                    } else {
                        Q.con("Examine me!!! formCellArray()");
                        pos = 0xdeadbeef;
                    }
                    cells[pos] = cell;
                }
            }
            
        // height > width    
        } else {
            for (int i = 0; i < width; i++) {
                for (int j = 0; j < height; j++) {
                    Cell cell;
                    // Start cell
                    if (i == 0 && j == 0) {
                        cell = new Cell(i, j, Mower.Heading.N, Mower.Command.M);
                    // even x, 0
                    } else if (i % 2 == 0 && j == 0) {
                        cell = new Cell(i, j, Mower.Heading.N, Mower.Command.L);
                    // odd x, 0
                    } else if (i % 2 != 0 && j == 0) {
                        cell = new Cell(i, j, Mower.Heading.E, Mower.Command.L);
                    }
                    
                    // even x, height - 1
                    else if (i % 2 == 0 && j == height - 1) {
                        cell = new Cell(i, j, Mower.Heading.E, Mower.Command.R);
                    // odd x, height - 1
                    } else if (i % 2 != 0 && j == height - 1) {
                        cell = new Cell(i, j, Mower.Heading.S, Mower.Command.R);
                    }
                    
                    // even lines
                    else if (i % 2 == 0) {
                        cell = new Cell(i, j, Mower.Heading.N, Mower.Command.M);
                    // odd lines
                    } else if (i % 2 != 0) {
                        cell = new Cell(i, j, Mower.Heading.S, Mower.Command.M);
                    }

                    // Something went awfully wrong, uncovered cell found
                    else {
                        Q.con("Examine me!!! formCellArray()");
                        cell = null;
                    }
                    
                    int pos;
                    if (i == 0) {
                        pos = j;
                    } else if (i % 2 == 0) {
                        pos = i * height + j;
                    } else if (i % 2 != 0) {
                        pos = i * height + height - j - 1;
                    } else {
                        Q.con("Examine me!!! formCellArray()");
                        pos = 0xdeadbeef;
                    }
                    cells[pos] = cell;
                }
            }
        }
    }
    
    //------------------------------------------------------------------------
    
    /**
     * Populates field with mowers
     */
    void populate(int mowCount) throws PosseMowerPanic {
        if (mowCount > maxMowers || mowCount < 1) {
            throw new PosseMowerPanic("Invalid mower count in input, please reconsider.");
        }
        
        // we have a correct amount of mowers and rightly sized, 
        // empty field of grass at this point
        
        int toCover = maxMowers;   // start with the whole area
        int pos = 0;
        for (int i = 0; i < mowCount; i++) {
            int from;
            int to;
            // the last segment
            if (i == mowCount - 1) {
                from = pos;
                to = maxMowers - 1;
                mowers.add(new Mower(geoMap, cells, from, to, cells[from].onStart));
                break;
            }
            int currentSegment = toCover / (mowCount - i);
            toCover -= currentSegment;
            from = pos;
            to = pos + currentSegment - 1;
            mowers.add(new Mower(geoMap, cells, from, to, cells[from].onStart));
            pos += currentSegment;
        }
        
    }
        
    //------------------------------------------------------------------------
    
    /**
     * I like to move it move it - especially at 0am
     * @return true if there are still mowers around with unfinished business
     */
    public boolean moveIt() {
        boolean indicator = false;
        
        for (Mower mow : mowers) {
            // this one met its destiny
            if (mow.current == mow.to) continue;
            
            if (!mow.alreadyTurned && cells[mow.current].afterEntry != Command.M) {
                mow.turn(cells[mow.current].afterEntry);
            } else {
                mow.move(geoMap, cells);
            }
            
            indicator = true;
        }
        return indicator;
    }
    
    //------------------------------------------------------------------------
    
    /**
     * Prints geoMap to the console if not overly large
     */
    private StringBuffer[] stringGeo() {            
        StringBuffer[] rows = new StringBuffer[height];
        Q.Command NewSB = new Q.Command() {
            @Override
            public Object execute(Object data) {
                return new StringBuffer();
            }
        };        
        Q.fill1(rows, NewSB, null);
        
        for (int i = 0; i < geoMap.length; i++) {
            for (int j = 0; j < geoMap[i].length; j++) {
                rows[j].append(geoMap[i][j].toString());
            }
        }
        
        return rows;
    }
    
    private void printRows(StringBuffer[] rows) {
        for (int i = rows.length-1; i >= 0; i--) {
            Q.con(rows[i]);
        }
    }
    
    public void printComposite() {
        if (width > 78 || height > 78) {
            Q.con("[map is too large to display, will not fit 78 x 78]");
            return;
        }
        
        StringBuffer[] rows = stringGeo();
        
        for (Mower mow : mowers) {
            rows[cells[mow.current].y].replace(cells[mow.current].x, cells[mow.current].x+1, mow.head.toString());
        }
        
        printRows(rows);
    }    
    
    //------------------------------------------------------------------------
    
    public void printHistory() {
        for (Mower mow : mowers) {
            Q.con(mow.commandHistory);
        }
    }
    
    //------------------------------------------------------------------------
    
    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getMaxMowers() {
        return maxMowers;
    }

    public ArrayList<Mower> getMowers() {
        return mowers;
    }
    
}
