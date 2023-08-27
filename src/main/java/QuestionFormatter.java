import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class QuestionFormatter {
    public static List<Question> formatQuestions(JSONArray json) {
        List<Question> questionList = new ArrayList<>();

        for (int i = 0; i < json.length(); i++) {
            JSONObject item = json.getJSONObject(i);

            String questionNumber = item.getJSONArray("labels").getJSONObject(0).getString("span_text").replace(".", "");
            String questionText = item.getJSONArray("labels").getJSONObject(1).getString("span_text");
            String answerText = item.getJSONArray("labels").getJSONObject((Integer.parseInt(questionNumber) * 2) + 10).getString("span_text").replace("\n", "");

            int optionsStartIndex = (Integer.parseInt(questionNumber) * 2) + 2;
            int optionsEndIndex = optionsStartIndex + 4;
            List<String> options = new ArrayList<>();
            for (int j = optionsStartIndex; j < optionsEndIndex; j++) {
                String optionText = item.getJSONArray("labels").getJSONObject(j).getString("span_text").replace("\n", "");
                options.add(optionText);
            }

            Question question = new Question(questionText, options, answerText);
            questionList.add(question);
        }

        return questionList;
    }

    public static void main(String[] args) {
        // Assuming you have the JSON response stored in a variable called jsonResponse
//        JSONArray json = new JSONArray(jsonResponse);
//        List<Question> questions = formatQuestions(json);

        // Print the formatted questions
//        for (Question question : questions) {
//            System.out.println("Question " + question.getQuestionNumber() + ": " + question.getQuestionText());
//            System.out.println("Options: " + question.getOptions());
//            System.out.println("Answer: " + question.getAnswer() + "\n");
//        }

//        for (Question question : questions) {
//            System.out.println("Question: " + question.getQuestion());
//            System.out.println("Choices: " + question.getChoices());
//            System.out.println("Correct Answer: " + question.getCorrectAnswer());
//            System.out.println();
//        }
    }
}
