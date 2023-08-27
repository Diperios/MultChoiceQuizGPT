import java.util.ArrayList;
import java.util.List;

public class Question {
    private String question;
    private List<String> choices;
    private String correctAnswer;

    public Question(String question, List<String> choices, String correctAnswer) {
        this.question = question;
        this.choices = choices;
        this.correctAnswer = correctAnswer;
    }

    public String getQuestion() {
        return question;
    }

    public List<String> getChoices() {
        return choices;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public static Question fromText(String text) {
        String[] parts = text.split("\n\n");
        String question = parts[0];
        List<String> choices = new ArrayList<>();
        String correctAnswer = "";

        for (int i = 1; i < parts.length; i++) {
            String part = parts[i];
            if (part.startsWith("a) ") || part.startsWith("a. ")) {
                choices.add(part.substring(3).trim());
            } else if (part.startsWith("b) ") || part.startsWith("b. ")) {
                choices.add(part.substring(3).trim());
            } else if (part.startsWith("c) ") || part.startsWith("c. ")) {
                choices.add(part.substring(3).trim());
            } else {
                correctAnswer = part.trim();
            }
        }

        return new Question(question, choices, correctAnswer);
    }

    public static void main(String[] args) {
        String text = "1) What is the Pythagorean theorem?\n" +
                "a) A² + B² = C \n" +
                "b) A² - B² = C \n" +
                "c) A³ + B³ = C";

        Question question = Question.fromText(text);
        System.out.println("Question: " + question.getQuestion());
        System.out.println("Choices: " + question.getChoices());
        System.out.println("Correct Answer: " + question.getCorrectAnswer());
    }
}
