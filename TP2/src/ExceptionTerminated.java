public class ExceptionTerminated extends Exception {
    @Override
    public void printStackTrace() {
        System.err.println("Terminated by user.");
    }
}
