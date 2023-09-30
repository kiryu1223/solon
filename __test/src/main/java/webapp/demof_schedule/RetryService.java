package webapp.demof_schedule;

import org.noear.solon.annotation.Component;
import org.noear.solon.scheduling.annotation.Retry;

@Component
public class RetryService {
    @Retry(
            maxAttempts = 5,
            include = ArithmeticException.class
    )
    public String m1(String aa) {
        System.out.println("123 = " + aa);
        int a = 1 / 0;
        return "ok";
    }

    @Retry(NullPointerException.class)
    public String m2(String aa) {
        System.out.println("123 = " + aa);
        int a = 1 / 0;
        return "ok";
    }
}
