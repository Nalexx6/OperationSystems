package os.lab1.compfunc.advanced;

import java.security.SecureRandom;
import java.util.Optional;

public class IntOps{
    public static Optional<Optional<Integer>> trialF(Integer x) {
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        SecureRandom random = new SecureRandom();

        if(random.nextInt(5) < 4) {
            return Optional.of(Optional.empty());
        } else {
            return Optional.of(Optional.of(x + 8));
        }

    }

    public static Optional<Optional<Integer>> trialG(Integer x) {
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return Optional.of(Optional.of(x * x));
    }
}
