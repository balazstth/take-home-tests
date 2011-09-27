package go4posse;

public class PosseMowerPanic extends Exception {

    private String err = "The real posse.";
    
    public PosseMowerPanic(String err) {
        super();
        this.err = err;
    }

    public String getErr() {
        return err;
    }
    
    private static final long serialVersionUID = -2087540318677220803L;
    
}
