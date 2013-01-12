package plg.gr3;

public final class Util {
    
    public static Object stringToNatural (String str) {
        try {
            int i = Integer.parseInt(str);
            if (i < 0) {
                return null;
            }
            
            return i;
            
        } catch (NumberFormatException exc) {
            return null;
        }
    }
    
    public static Object stringToFloat (String str) {
        try {
            float f = Float.parseFloat(str);
            if (f < 0) {
                return null;
            }
            
            return f;
            
        } catch (NumberFormatException exc) {
            return null;
        }
    }
    
}
