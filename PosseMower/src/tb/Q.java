package tb;

import java.util.LinkedList;
import java.util.Stack;

/**
 * All the hideousness you ought not ever see or write.
 * @author tb
 */
public class Q {

    //------------------------------------------------------------------------
    // Primitive ops
    
    public static final Object nil = null;
    
    public static Object nil() {
        return null;
    }
    
    public static void nop() {
    }
    
    public static void con() {
        System.out.println();
    }
    
    public static void con(Object obj) {
        System.out.println(obj);
    }
    
    public static void conc(Object obj) {
        System.out.print(obj);
    }
    
    public static void quit(int code) {
        System.exit(code);
    }    
    
    //------------------------------------------------------------------------
    // Sugar
    
    /**
     * One dimensional fill with a fixed value
     * @param obj
     * @param val
     */
    public static void fill1(Object[] obj, Object val) {
        int x = obj.length;
        for (int i = 0; i < x; i++)
            obj[i] = val;
    }
    
    /**
     * One dimensional fill with Command
     * @param obj
     * @param command
     * @param data
     */
    public static void fill1(Object[] obj, Command command, Object data) {
        int x = obj.length;
        for (int i = 0; i < x; i++)
            obj[i] = command.execute(data);
    }
    
    /**
     * Two dimensional fill with a fixed value
     * @param obj
     * @param val
     */
    public static void fill2(Object[][] obj, Object val) {
        int x = obj.length;
        for (int i = 0; i < x; i++) {
            int y = obj[i].length;
            for (int j = 0; j < y; j++) {
                obj[i][j] = val;
            }
        }
    }
    
    /**
     * Two dimensional fill with Command
     * @param obj
     * @param command
     * @param data
     */
    public static void fill2(Object[][] obj, Command command, Object data) {
        int x = obj.length;
        for (int i = 0; i < x; i++) {
            int y = obj[i].length;
            for (int j = 0; j < y; j++) {
                obj[i][j] = command.execute(data);
            }
        }
    }
    
    /**
     * Three dimensional fill with a fixed value
     * @param obj
     * @param val
     */
    public static void fill3(Object[][][] obj, Object val) {
        int x = obj.length;
        for (int i = 0; i < x; i++) {
            int y = obj[i].length;
            for (int j = 0; j < y; j++) {
                int z = obj[i][j].length;
                for (int k = 0; k < z; k++) {
                obj[i][j][k] = val;
                }
            }
        }
    }
    
    /**
     * Three dimensional fill with Command
     * @param obj
     * @param command
     * @param data
     */
    public static void fill3(Object[][][] obj, Command command, Object data) {
        int x = obj.length;
        for (int i = 0; i < x; i++) {
            int y = obj[i].length;
            for (int j = 0; j < y; j++) {
                int z = obj[i][j].length;
                for (int k = 0; k < z; k++) {
                obj[i][j][k] = command.execute(data);
                }
            }
        }
    }
    
    // TODO: recursive filln()

    //------------------------------------------------------------------------
    // Patterns

    /**
     * Basic command pattern with command queue and stack
     */
    public interface Command {
        public Object execute(Object data);
    }    

    public static void callCom(Command command, Object data) {
        command.execute(data);
    }

    //------------------------------------------------------------------------
    
    /**
     * Adds Command to command queue
     * @param queue
     * @param command
     * @return queue with appended command
     */
    public static LinkedList<QueCommand> addCom(LinkedList<QueCommand> queue, Command command, Object data) {
        LinkedList<QueCommand> res;
        QueCommand qC = new QueCommand();

        if (queue == null) {
            res = new LinkedList<QueCommand>();
        } else {
            res = queue;
        }

        if (command != null) {
            qC.command = command;
        }
        if (data != null) {
            qC.data = data;
        }

        res.add(qC);
        return res;
    }
    
    /**
     * Adds Command to command stack
     * @param stack
     * @param command
     * @return stack with pushed command
     */
    public static Stack<QueCommand> addCom(Stack<QueCommand> stack, Command command, Object data) {
        Stack<QueCommand> res;
        QueCommand qC = new QueCommand();

        if (stack == null) {
            res = new Stack<QueCommand>();
        } else {
            res = stack;
        }

        if (command != null) {
            qC.command = command;
        }
        if (data != null) {
            qC.data = data;
        }

        res.add(qC);
        return res;
    }
    
    //------------------------------------------------------------------------
    
    /**
     * Calls and removes every command in the queue omitting results
     * @param queue
     */
    public static void callComs(LinkedList<QueCommand> queue) {
        if (queue == null) return;
        while (queue.size() > 0) {
            QueCommand qC = queue.remove();
            callCom(qC.command, qC.data);
        }
    }
    
    /**
     * Pops and calls every command from the stack omitting results
     * @param stack
     */
    public static void callComs(Stack<QueCommand> stack) {
        if (stack == null) return;
        while (stack.size() > 0) {
            QueCommand qC = stack.pop();
            callCom(qC.command, qC.data);
        }
    }
    
    // TODO: e.g. reduce-like callComs()
            
    //------------------------------------------------------------------------
    
    /**
     * Simple quit command
     */
    public static Q.Command Quit = new Q.Command() {
        @Override
        public Object execute(Object data) {
            int code = (Integer) data;
            Q.quit(code);
            return null;
        }
    };

    /**
     * Simple con command
     */
    public static Q.Command Con = new Q.Command() {
        @Override
        public Object execute(Object data) {
            Q.con(data);
            return null;
        }
    };
    
}
