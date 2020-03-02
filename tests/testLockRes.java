import org.junit.jupiter.api.Test;
import resourceManager.ResourceError;
import resourceManager.ResourceSystem;

import java.util.Random;

public class testLockRes {

    @Test
    public void testLockRes6() throws  ResourceError{
        ResourceSystem resourceSystem = new ResourceSystem();
        resourceSystem.addResource("A", 20); // The resource - may be used up to 20 times
        Random rand = new Random();
        resourceSystem.addUser("1",0.8);
        resourceSystem.addUser("2",0.1);
        resourceSystem.addUser("3",0.2);
        resourceSystem.addUser("4",0.5);
        resourceSystem.addUser("5",0.2);
        resourceSystem.addUser("6",0.5);
        resourceSystem.run();
    }
}
