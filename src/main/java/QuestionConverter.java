import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class QuestionConverter {
    public static List<Question> convertToQuestions(JSONArray outputArray) {
        List<Question> questions = new ArrayList<>();

        for (int i = 0; i < outputArray.length(); i++) {
            JSONObject questionObject = outputArray.getJSONObject(i);
            String questionText = getQuestionText(questionObject);
            List<String> choices = getChoices(questionObject);
            String correctAnswer = getCorrectAnswer(questionObject);

            Question question = new Question(questionText, choices, correctAnswer);
            questions.add(question);
        }

        return questions;
    }

    private static String getQuestionText(JSONObject questionObject) {
        String questionText = questionObject.getString("utterance");
        return questionText.substring(0, questionText.indexOf("\n"));
    }

    private static List<String> getChoices(JSONObject questionObject) {
        JSONArray choicesArray = questionObject.getJSONArray("choices");
        List<String> choices = new ArrayList<>();

        for (int i = 0; i < choicesArray.length(); i++) {
            JSONObject choiceObject = choicesArray.getJSONObject(i);
            String choice = choiceObject.getString("utterance");
            choices.add(choice);
        }

        return choices;
    }

    private static List<String> getChoices2(JSONObject contentObject) {
        String choicesText = contentObject.getString("utterance");
        String[] choicesArray = choicesText.split("\n");
        List<String> choices = new ArrayList<>();

        for (int i = 1; i < choicesArray.length; i++) {
            choices.add(choicesArray[i]);
        }

        return choices;
    }



    private static String getCorrectAnswer(JSONObject questionObject) {
        String correctAnswer = questionObject.getString("correct");
        return correctAnswer.substring(0, 1);
    }

    public static List<Question> convertToQuestions2(JSONArray outputArray) {
        List<Question> questions = new ArrayList<>();

        JSONArray contentsArray = outputArray.getJSONObject(0).getJSONArray("contents");
        JSONArray labelsArray = outputArray.getJSONObject(0).getJSONArray("labels");

        for (int i = 0; i < contentsArray.length(); i++) {
            JSONObject contentObject = contentsArray.getJSONObject(i);
            JSONObject labelObject = labelsArray.getJSONObject(i);

            String questionText = labelObject.getString("span_text");
            List<String> choices = getChoices2(contentObject);
            String correctAnswer = labelObject.getJSONArray("output_spans")
                    .getJSONObject(0)
                    .getString("span_text");

            Question question = new Question(questionText, choices, correctAnswer);
            questions.add(question);
        }

        return questions;
    }



    public static void main(String[] args) {
        // Assuming the outputArray is a JSONArray obtained from your API response
        JSONArray outputArray = new JSONArray("[{\"utterance\":\"1) What is the Pythagorean theorem?\\na) A² + B² = C \\nb) A² - B² =\",\"choices\":[{\"utterance\":\"A² + B² = C \"},{\"utterance\":\"A² - B² = C \"},{\"utterance\":\" A³ + B³ = C\"}],\"correct\":\"a)\"},{\"utterance\":\"2) What is the formula for finding the area of a circle?\\na) A = πr² \\nb) A =\",\"choices\":[{\"utterance\":\"A = πr² \"},{\"utterance\":\"A = 2πr \"},{\"utterance\":\" A = 4πr²\"}],\"correct\":\"a)\"}]");

        List<Question> questions = convertToQuestions(outputArray);

        for (Question question : questions) {
            System.out.println("Question: " + question.getQuestion());
            System.out.println("Choices: " + question.getChoices());
            System.out.println("Correct Answer: " + question.getCorrectAnswer());
            System.out.println();
        }
    }
}
