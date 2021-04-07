import java.util.Scanner;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class Task1 {
    public static boolean matches(String text, String regex) {
        final Boolean[] result = {false};

        Runnable runnable = () -> {
            try {
                result[0] = Pattern.compile(regex).matcher(text).matches();
            } catch (PatternSyntaxException ignored) {} // единственное возможное исключение
        };

        Thread thread = new Thread(runnable);
        thread.start();

        try {
            Thread.sleep(500); // функция будет работать ровно только времени
            thread.interrupt();
        } catch (InterruptedException ignored) {}

        return result[0]; // если было исключение или не хватило времени, то возвращаем false
    }

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        String text = in.nextLine();
        String reg = in.nextLine();
        System.out.println(matches(text, reg));
    }
}
