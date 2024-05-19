import java.util.HashMap;
import java.util.List;

public class TextIndex {


    public static double calculateIndex(String text) {
        int totalCharacters = text.length();


        HashMap<Character, Integer> letterCounts = new HashMap<>();
        for (char ch : text.toCharArray()) {
            if (Character.isLetter(ch)) {
                letterCounts.put(ch, letterCounts.getOrDefault(ch, 0) + 1);
            }
        }

        double sum = 0.0;
        for (int count : letterCounts.values()) {
            sum += count * (count - 1);
        }
        double floor = (double) totalCharacters * (totalCharacters-1);

        return sum / floor;
    }

    public static void main(String[] args) {
            String text = Vigener.readTextFromFile("texts/openText.txt");
            double indexWithSpace = calculateIndex(text);
            System.out.println("Індекс відповідності відкритого тексту: " + indexWithSpace);

            List<String> keys = List.of("ак", "каф", "шифр", "индек", "анализ", "шифрування", "карикатурамасло","дизюктивнаядизфункци");

            for (String key: keys) {
                String enText = Vigener.encryptVigener(text, key);
                double indexEnText = calculateIndex(enText);
                System.out.println("Індекс відповідності шифротексту з довжиною ключа " + key.length() + ": " + indexEnText);
            }
    }
}