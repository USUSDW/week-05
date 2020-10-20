package com.github.ususdw;

import com.github.ususdw.commands.SpecialMessage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        //noinspection InfiniteLoopStatement
        while (true) {
            System.out.print("?> ");
            String input = scanner.nextLine();
            List<String> parsedInput = parseInput(input);

            // Check for when there's not enough input.
            if (parsedInput.size() < 1) {
                System.out.println("Invalid command.");
                continue;
            }

            // Pull command off the front.
            String command = parsedInput.get(0);
            List<String> parsedArgs = parsedInput.subList(1, parsedInput.size());

            if (command.equalsIgnoreCase("echo")) {
                String echoMessage = String.join(" ", parsedArgs);
                System.out.println(echoMessage);
            }
            else if (command.equalsIgnoreCase("fi")) {
                String filename = parsedArgs.get(0);
                File file = new File(filename);
                if (file.exists()) {
                    try {
                        System.out.println("File " + file.getName());
                        System.out.println("      Path: " + file.getAbsolutePath());
                        System.out.println("    Length: " + file.length());
                        System.out.println("  Readable: " + (file.canRead() ? "Yes" : "No"));
                        System.out.println(" Writeable: " + (file.canWrite() ? "Yes" : "No"));
                        System.out.println("Executable: " + (file.canExecute() ? "Yes" : "No"));
                        System.out.println("  Contents: ");
                        System.out.println(String.join("\n", Files.readAllLines(file.toPath())));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                else {
                    System.out.println("File does not exist.");
                }
            }
            else if (command.equalsIgnoreCase("sm")) {
                System.out.println(SpecialMessage.specialMessage(parsedArgs));
            }
            else if (command.equalsIgnoreCase("exit")) {
                System.exit(0);
            }
        }
    }

    private static List<String> parseInput(String input) {
        return List.of(input.split(" "));
    }
}
