package org.example;

import java.util.Scanner;

public class InputUtil  {
    private static final Scanner sc = new Scanner(System.in);

    public static String getLine(String message) {
        System.out.print(message);
        return sc.nextLine();
    }

    public static int getInt(String ... answers){
        String question = makeQuestion(answers);
        System.out.println(question);
        int input = sc.nextInt();
        sc.nextLine();
        return input;
    }
    private static String makeQuestion(String... answers){
        StringBuilder question = new StringBuilder();
        for (int i=0;i<answers.length;i++){
            question.append(String.format("[%d] %s ", i + 1, answers[i]));
        }
        question.append("[0] 종료");
        return question.toString();
    }
}
