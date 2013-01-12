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
    
}
