public class HelloWorld extends ParentClass {
    public static void main(String[] args) {
        System.out.println("Hello, World!");
    }

    @Override
    public void greet() {
        System.out.println("Hello from HelloWorld!");
    }
}

