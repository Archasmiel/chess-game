package engine.entity.entitybase;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class RandomIds {

    private static final Random RANDOM = new Random();
    private static final Set<Long> IDS = new HashSet<>();

    private RandomIds() {

    }

    public static long generate() {
        long res = RANDOM.nextLong();
        if (IDS.contains(res)) {
            return generate();
        } else {
            IDS.add(res);
            return res;
        }
    }

    public static void remove(long id) {
        if (IDS.contains(id)) {
            IDS.remove(id);
        } else {
            throw new IllegalStateException("Trying to remove wrong id");
        }
    }

}
