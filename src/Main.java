public class Main {
    public static void main(String[] args) {
        //Running module test for Driver class
        Subscriber sub1 = new Subscriber();
        ModTestClass mod = new ModTestClass();
        try {
            mod.controlCheckService(sub1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
