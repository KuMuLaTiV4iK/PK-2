import java.io.*;
import java.util.InputMismatchException;
import java.util.Scanner;

class Vigener {
    static String russianAlphabet = "абвгдежзийклмнопрстуфхцчшщъыьэюя ";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);


        System.out.println("""
                Chose function:
                1. Encryption text
                2. Decryption text
                3. Exit""");
        try {
            int function = scanner.nextInt();
            scanner.nextLine();
            menu(function);
        }catch (InputMismatchException e) { System.out.println("Введено невірне значення");}

        main(scanner.delimiter().split(" "));
    }

    public static void menu(int function) {
        Scanner scannerM = new Scanner(System.in);

        switch (function) {
            case 1 -> {
                System.out.println("""
                        Chose function:
                        1. Enter the text
                        2. Read from file openText.txt""");
                int func1 = scannerM.nextInt();
                if (func1 == 1) {
                    System.out.println("Enter the text:");
                    scannerM.nextLine();
                    String text = scannerM.nextLine();
                    text = filterText(text.toLowerCase(), russianAlphabet);
//отступление завтра утром
                    System.out.println("Enter the key:");
                    String key = scannerM.nextLine().toLowerCase();
//курсант
                    String encryptedText = encryptVigener(text, key);
                    System.out.println("Encrypted text:");
                    System.out.println(encryptedText);
                    writeTextToFile("texts/EncryptedText.txt", encryptedText);
                } else if (func1 == 2) {
                    String text = readTextFromFile("texts/openText.txt").replaceAll("\\s", "");

                    System.out.println("Enter the key:");
                    String key = new Scanner(System.in).nextLine().toLowerCase();
//курсантивитилюблятьпрограмувати
                    String encryptedText = encryptVigener(text, key);
                    System.out.println("Encrypted text:");
                    System.out.println(encryptedText);
                    writeTextToFile("texts/EncryptedText.txt", encryptedText);
                } else {
                    System.out.println("Incorrect function");
                    menu(function);
                }
            }
            case 2 -> {
                System.out.println("""
                        Chose function:
                        1. Enter the encrypted text
                        2. Read encrypted text from file""");
                int func2 = scannerM.nextInt();
                if (func2 == 1) {
                    System.out.println("Enter the encrypted text:");
                    scannerM.nextLine();
                    String encryptedText = scannerM.nextLine();
                    encryptedText = filterText(encryptedText.toLowerCase(), russianAlphabet);

                    System.out.println("Enter the key:");
                    String key = new Scanner(System.in).nextLine().toLowerCase();

                    String decryptedText = decryptVigener(encryptedText, key);
                    System.out.println("Decrypted text:");
                    System.out.println(decryptedText);
                    writeTextToFile("texts/DecryptedText.txt", decryptedText);
                } else if (func2 == 2) {
                    String encryptedText = readTextFromFile("texts/EncryptedText.txt");

                    System.out.println("Enter the key:");
                    String key = new Scanner(System.in).nextLine().toLowerCase();

                    String decryptedText = decryptVigener(encryptedText, key);
                    System.out.println("Decrypted text:");
                    System.out.println(decryptedText);
                    writeTextToFile("texts/DecryptedText.txt", decryptedText);
                } else {
                    System.out.println("Incorrect function");
                    menu(function);
                }
            }
            case 3 -> scannerM.close();
            default -> {
                System.out.println("Enter correct function");
                main(scannerM.delimiter().split(" "));
            }
        }
    }

    public static String filterText(String text, String alphabet) {
        StringBuilder filteredText = new StringBuilder();
        for (int i = 0; i < text.length(); i++) {
            char currentChar = text.charAt(i);
            if (alphabet.contains(String.valueOf(currentChar))) {
                filteredText.append(currentChar);
            } else if (currentChar == 'ё') {
                filteredText.append('е'); // Replace 'ё' with 'е'
            }
        }
        return filteredText.toString();
    }

    public static String encryptVigener(String text, String key) {
        StringBuilder encryptedText = new StringBuilder();
        int keyIndex = 0;
        for (int i = 0; i < text.length(); i++) {
            char currentChar = text.charAt(i);
            char keyChar = key.charAt(keyIndex);
            int shift = keyChar - 'а';
            char encryptedChar = (char) ((currentChar - 'а' + shift) % 32 + 'а');
            encryptedText.append(encryptedChar);
            keyIndex = (keyIndex + 1) % key.length();
        }
        return encryptedText.toString();
    }

    public static String decryptVigener(String text, String key) {
        StringBuilder plaintext = new StringBuilder();
        int textLength = text.length();
        int keyLength = key.length();

        for (int i = 0, j = 0; i < textLength; i++) {
            char currentChar = text.charAt(i);
            char keyChar = key.charAt(j);

            // Розшифрування символу
            char decryptedChar;
            if (Character.isUpperCase(currentChar)) {
                decryptedChar = (char) ((currentChar - keyChar + 64) % 32 + 1040);
            } else if (Character.isLowerCase(currentChar)) {
                decryptedChar = (char) ((currentChar - keyChar + 96) % 32 + 1072);
            } else {
                // Залишаємо символи, які не є буквами без змін
                decryptedChar = currentChar;
            }

            plaintext.append(decryptedChar);

            // Перехід до наступного символу ключа
            j = (j + 1) % keyLength;
        }

        return plaintext.toString();
    }

    public static String readTextFromFile(String filename) {
        StringBuilder text = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                text.append(line);
            }
        } catch (IOException e) {
            System.out.println("Error reading text from file: " + e.getMessage());
        }
        return text.toString();

    }

    public static void writeTextToFile(String filename, String text) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filename))) {
            bw.write(text);
        } catch (IOException e) {
            System.out.println("Error writing text to file: " + e.getMessage());
        }
    }
}
