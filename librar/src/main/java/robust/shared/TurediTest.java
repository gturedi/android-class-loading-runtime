package robust.shared;

import java.util.concurrent.Callable;

public class TurediTest
        implements Callable<String> {

    public void test() {
        System.out.print("test");
    }

    public String sayHello(String name) {
        return "hello " + name;
    }

    @Override
    public String call() throws Exception {
        return "bla bla";
    }

}