// OverrideExample.java

class Superclass {
    public void myMethod() {
        System.out.println("Superclass method");
    }
}

class Subclass extends Superclass {
    @Override
    public void myMethod() {
        System.out.println("Subclass method");
    }
}

public class OverrideExample {
    public static void main(String[] args) {
        Subclass obj = new Subclass();
        obj.myMethod(); // Output: Subclass method
    }
}
