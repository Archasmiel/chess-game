import window.Window;

public class Application {

    public static void main(String[] args) {
        Window window = new Window.Builder().title("Chess").size(1280, 720).build();
        window.run();
    }

}
