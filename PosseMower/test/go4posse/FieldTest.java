package go4posse;

import static org.junit.Assert.*;

import org.junit.Test;

public class FieldTest {

    String nopmp = "Should have thrown PosseMowerPanic exception: ";
    String pmp = "Unexpected PosseMowerPanic exception: ";
    
    //------------------------------------------------------------------------
    
    String dim(int w, int h) {
        return "Field dimensions " + w + " x " + h;
    }
    
    /**
     * @return true on caught PosseMowerPanic
     */
    boolean testFieldGotPmp(int w, int h) {
        try {
            @SuppressWarnings("unused")
            Field tester = new Field(w, h);
        } catch (PosseMowerPanic e) {
            return true;
        }
        return false;
    }
    
    @Test
    public void testField() {
        int w;
        int h;
        
        w = 1; h = 1;
        if (!testFieldGotPmp(w, h)) fail(nopmp + dim(w, h));
        w = -100; h = 100;
        if (!testFieldGotPmp(w, h)) fail(nopmp + dim(w, h));
        w = 100; h = -100;
        if (!testFieldGotPmp(w, h)) fail(nopmp + dim(w, h));
        w = 0; h = 2;
        if (!testFieldGotPmp(w, h)) fail(nopmp + dim(w, h));
        w = 2; h = 0;
        if (!testFieldGotPmp(w, h)) fail(nopmp + dim(w, h));
        
        w = 1; h = 2;
        if (testFieldGotPmp(w, h)) fail(pmp + dim(w, h));
        w = 2; h = 1;
        if (testFieldGotPmp(w, h)) fail(pmp + dim(w, h));
        w = 2; h = 2;
        if (testFieldGotPmp(w, h)) fail(pmp + dim(w, h));
        w = 100; h = 100;
        if (testFieldGotPmp(w, h)) fail(pmp + dim(w, h));
    }
    
}
