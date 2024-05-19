import java.util.*;

public class VigenerAttack {

    // Визначення довжини ключа за допомогою індексу відповідності
    public static int findKeyLength(String ciphertext) {
        int maxLength = 40; // Максимальна довжина ключа, яку будемо перевіряти

        double[] indexCoincidence = new double[maxLength];

        for (int r = 1; r <= maxLength; r++) {
            double sum = 0.0;
            int count = 0;

            // Розділяємо шифртекст на блоки довжиною r
            List<StringBuilder> blocks = splitIntoBlocks(ciphertext, r);

            // Обчислюємо індекси відповідності для кожного блоку
            for (StringBuilder block : blocks) {
                Map<Character, Integer> charCounts = new HashMap<>();
                int blockLength = block.length();

                // Рахуємо кількість кожної літери в блоку
                for (int i = 0; i < blockLength; i++) {
                    char ch = block.charAt(i);
                    charCounts.put(ch, charCounts.getOrDefault(ch, 0) + 1);
                }

                // Обчислюємо індекс відповідності для поточного блоку
                double blockIndexCoincidence = 0.0;
                for (char ch : charCounts.keySet()) {
                    int countCh = charCounts.get(ch);
                    blockIndexCoincidence += (double) (countCh * (countCh - 1)) / (blockLength * (blockLength - 1));
                }

                sum += blockIndexCoincidence;
                count++;
            }

            // Середнє значення індексів відповідності для всіх блоків
            indexCoincidence[r - 1] = sum / count;
        }

        // Знаходимо довжину ключа, для якої індекс відповідності максимальний
        int keyLength = 0;
        double maxIndexCoincidence = 0.0;
        for (int i = 0; i < maxLength; i++) {
            if (indexCoincidence[i] > maxIndexCoincidence) {
                maxIndexCoincidence = indexCoincidence[i];
                keyLength = i + 1;
            }
        }
        return keyLength;
    }

    public static List<StringBuilder> splitIntoBlocks(String ciphertext, int blockLength) {
        List<StringBuilder> blocks = new ArrayList<>();
        for (int i = 0; i < blockLength; i++) {
            blocks.add(new StringBuilder());
        }
        for (int i = 0; i < ciphertext.length(); i++) {
            blocks.get(i % blockLength).append(ciphertext.charAt(i));
        }
        return blocks;
    }

    public static List<int[]> occurrences(List<StringBuilder> blocks) {
        List<int[]> occurrencesList = new ArrayList<>();
        for (StringBuilder block : blocks) {
            int[] occurrences = new int[32];
            for (int i = 0; i < block.length(); i++) {
                char currentChar = block.charAt(i);
                occurrences[currentChar - 'а']++;
            }
            occurrencesList.add(occurrences);
        }
        return occurrencesList;
    }

    static String findKey(String ciphertext, int keyLength) {
        StringBuilder key = new StringBuilder();

        double[] letterProbabilities = {
                0.0801, 0.0159, 0.0454, 0.017, 0.0298, 0.0845, 0.0094, 0.0165,
                0.0735, 0.0121, 0.0349, 0.044, 0.0321, 0.067, 0.1097, 0.0281,
                0.0473, 0.0547, 0.0626, 0.0262, 0.0026, 0.0097, 0.0048, 0.0144,
                0.0073, 0.0036, 0.0004, 0.019, 0.0174, 0.0032, 0.0064, 0.0201
        };

        List<int[]> occur = occurrences(splitIntoBlocks(ciphertext, keyLength));

        for (int blockIndex = 0; blockIndex < keyLength; blockIndex++) {
            List<Double> m = new ArrayList<>();

            for (int g = 0; g < 32; g++) {
                double sum = 0;
                int[] totalOccurrences = occur.get(blockIndex);

                for (int i = 0; i < 32; i++) {
                    sum += letterProbabilities[i] * totalOccurrences[(i + g) % 32];
                }
                m.add(sum);
            }

            int charIndex = m.indexOf(Collections.max(m));
            char keyChar = Vigener.russianAlphabet.charAt(charIndex);
            key.append(keyChar);
        }

        return key.toString();
    }

    public static void main(String[] args) {
        String ciphertext = Vigener.readTextFromFile("texts/task.txt").toLowerCase();
        int keyLength = findKeyLength(ciphertext);
        System.out.println("Key length: " + keyLength);

        String key = findKey(ciphertext, keyLength);
        System.out.println("Key: " + key);

        System.out.println("""
                1. Change key
                or press 'Enter' to continue
                """);
        Scanner sc = new Scanner(System.in);
        var r = sc.nextLine();
        if ("1".equals(r)) {
            System.out.println("Enter changed key");
            key = sc.nextLine().toLowerCase();
        }
        String decryptedText = Vigener.decryptVigener(ciphertext, key);
        System.out.println("Decrypt text: " + decryptedText);
        Vigener.writeTextToFile("texts/HackedText.txt", decryptedText);
    }
}