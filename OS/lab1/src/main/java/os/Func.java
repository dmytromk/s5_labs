package os;

import java.util.Optional;

public class Func {
    public static Optional<Double> tryF(Double x){
        return Optional.of((double)x+3);
    }

    public static Optional<Double> tryG(Double x){
        return Optional.of((double)x/2);
    }
}
