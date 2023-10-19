package os;

import java.util.OptionalDouble;

public class MyThread extends Thread{
    private final String functionName;
    private final Double x;

    public OptionalDouble result;
    public boolean calculated = false;

    public MyThread(String functionName, Double x) {
        this.functionName = functionName;
        this.x = x;
    }

    @Override
    public void run() {
        try {
            Thread.sleep(5000);

            if(functionName.equals("f")) {
                result = OptionalDouble.of(Func.tryF(x).get());
                synchronized (this){
                    if(result.isPresent()){
                        calculated = true;
                    }
                }
            }

            else if (functionName.equals("g")) {
                result = OptionalDouble.of(Func.tryG(x).get());
                synchronized (this){
                    if(result.isPresent()){
                        calculated = true;
                    }
                }
            }
        } catch (Exception e){
//            System.out.println("Couldn't compute function " + this.functionName);
        }
    }
}
