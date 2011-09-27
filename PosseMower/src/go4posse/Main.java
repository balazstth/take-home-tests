package go4posse;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Stack;

import tb.Q;
import tb.QueCommand;

public class Main {

    // Command stack to be executed upon normal finish / error
    Stack<QueCommand> finishOk;
    Stack<QueCommand> error;

    //------------------------------------------------------------------------
    
    Main(String[] args) {
        finishOk = Q.addCom(finishOk, Q.Quit, 0);
        finishOk = Q.addCom(finishOk, Q.Con, 
                "----------------------------------------------------------------\nOK.");
        
        error = Q.addCom(error, Q.Quit, 1);
        error = Q.addCom(error, Q.Con, 
                "----------------------------------------------------------------\nThere was a mishap.");
        
        Q.con("POSSE makes you happy - even mowing your lawn! Don't ever panic.");
        Q.con("----------------------------------------------------------------");
        
        if (args == null || args.length < 1) {
            usage();
            Q.callComs(finishOk);
        }
        
        if (args[0].equalsIgnoreCase("-h") || args[0].equalsIgnoreCase("-?")) {
            usage();
            Q.callComs(finishOk);
        } else if (args.length == 1) {
            Q.con("Please use PosseMower with -b infile.txt for advanced mode, normal mode not yet implemented.");
            Q.callComs(error);
        } else if (!args[0].equalsIgnoreCase("-b")) {
            usage();
            Q.callComs(error);
        }

        // Good to go, -b mode
        
        runBonus(args[1]);
        
        Q.callComs(finishOk);
    }

    void usage() {
        Q.con("Usage: java -jar PosseMower.jar [-b|h|?] <input.txt>");
        Q.con("-b stands for 'bonus round' mode");
        Q.con("-h or -? triggers this help text");
    }
    
    // Read the first line of the input file
    String loadBonusInput(String filename) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filename));
            return reader.readLine();
        } catch (FileNotFoundException e) {
            Q.con("File not found: " + filename);
            Q.callComs(error);
        } catch (IOException e) {
            Q.con("Cannot read file: " + filename);
            Q.callComs(error);
        }
        return null;
    }
        
    //------------------------------------------------------------------------
    
    void runBonus(String filename) {
        String bonusInput = loadBonusInput(filename);
        
        // parse input
        String[] input = bonusInput.split(" ");
        if (input.length < 3) {
            Q.con("Invalid input file format, should look something like this: 5 5 3");
            Q.callComs(error);
        }
        int w = 0, h = 0, mowCount = 0;
        try {
            w = Integer.parseInt(input[0]) + 1;     // Read values denote coordinates, conversion to dimensions
            h = Integer.parseInt(input[1]) + 1;
            mowCount = Integer.parseInt(input[2]);
        } catch (NumberFormatException e) {
            Q.con("Invalid input file format, should look something like this: 5 5 3");
            Q.callComs(error);
        }
        
        // start processing, input in w, h, mowCount, field size will be checked by Field()
        
        Field field = null;
        try {
            field = new Field(w, h);
        } catch (PosseMowerPanic e) {
            Q.con(e.getErr());
            Q.callComs(error);
        }
        
        try {
            field.populate(mowCount);
        } catch (PosseMowerPanic e) {
            Q.con(e.getErr());
            Q.callComs(error);
        }
        
        Q.con("Start position:");
        field.printComposite();
        
        while (field.moveIt()) {
            Q.nop();
        }
        
        Q.con("End position:");
        field.printComposite();

        Q.con("Command history of mowers:");
        field.printHistory();
    }
    
    //------------------------------------------------------------------------
    
    /**
     * @param args, see usage()
     */
    public static void main(String[] args) {
        @SuppressWarnings("unused")
        Main main = new Main(args);
    }

}
