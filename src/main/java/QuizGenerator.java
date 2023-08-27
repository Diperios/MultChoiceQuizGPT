import okhttp3.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class QuizGenerator {
    private static final String API_KEY = System.getenv("OPENAI_API_KEY");
    private static final String API_URL = System.getenv("OPENAI_API_URL");

    public static String generateFormatQuizTom(String subject) throws IOException {
        OkHttpClient client = new OkHttpClient();

        JSONObject payload = new JSONObject();
        payload.put("input", "Create an interesting multiple choice quiz about " + subject + ". make it creative, fun, entertaining, and engaging.");
        payload.put("input_type", "article");
        payload.put("output_type", "json");
        JSONObject multilingual = new JSONObject();
        multilingual.put("enabled", true);
        payload.put("multilingual", multilingual);

        JSONArray steps = new JSONArray();

        JSONObject gptStep = new JSONObject();
        gptStep.put("skill", "gpt");
        JSONObject gptParams = new JSONObject();
        gptParams.put("gpt_engine", "text-davinci-003");
        gptParams.put("prompt_position", "end");
        gptParams.put("temperature", 1);
        gptParams.put("prompt", "Given the subject " + subject + " generate a quiz with a question, and 4 choices.\nThe quiz should be formatted like so-\n\n Q:  the question?\n  A: first choice\n  B: second choice\n  C: third choice\n  D: fourth choice\n\n;\nmake sure to follow this guideline to the tea.");
        gptStep.put("params", gptParams);
        steps.put(gptStep);

        payload.put("steps", steps);

        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(JSON, payload.toString());
        Request request = new Request.Builder()
                .url(API_URL)
                .header("api-key", API_KEY)
                .header("content-type", "application/json")
                .post(body)
                .build();

        Call call = client.newCall(request);
        Response response = call.execute();
        String responseBody = response.body().string();

        return responseBody;
    }




    public static String generateFormatQuizFrenchGuy(String subject, int n) throws IOException {
        OkHttpClient client = new OkHttpClient();

        JSONObject payload = new JSONObject();
        payload.put("input", "Create an engaging multiple-choice quiz about " + subject + ". Make it creative, fun, and informative. The quiz should consist of 10 questions, each with 4 answer choices. Format each question and answer choices in the following way:\n\nQ: <QUESTION>\nA) <CHOICE_A>\nB) <CHOICE_B>\nC) <CHOICE_C>\nD) <CHOICE_D>\n\nYour quiz should cover various aspects of <SUBJECT> and provide interesting and relevant options for each question. Keep the language simple and concise to ensure clarity. Make sure that each question has a correct answer and the answer choices are distinct and plausible. Aim to create an enjoyable quiz experience for the participants.");

        payload.put("input_type", "article");
        payload.put("output_type", "json");
        JSONObject multilingual = new JSONObject();
        multilingual.put("enabled", true);
        payload.put("multilingual", multilingual);

        JSONArray steps = new JSONArray();

        JSONObject gptStep = new JSONObject();
        gptStep.put("skill", "gpt");
        JSONObject gptParams = new JSONObject();
        gptParams.put("gpt_engine", "text-davinci-003");
        gptParams.put("prompt_position", "end");
        gptParams.put("temperature", 0.8);
        gptParams.put("prompt", "Given the subject- " + subject + ", generate an engaging multiple-choice quiz. Your quiz should consist of " + n + " questions, each with 4 answer choices. Format each question and answer choices in the following way:\n\nQ: <QUESTION>\nA) <CHOICE_A>\nB) <CHOICE_B>\nC) <CHOICE_C>\nD) <CHOICE_D>\n\nMake sure that each question has a correct answer and the answer choices are distinct and plausible. The quiz should cover various aspects of " + subject + " and provide interesting and relevant options for each question. Keep the language simple and concise to ensure clarity. Aim to create an enjoyable quiz experience for the participants.");
        gptStep.put("params", gptParams);
        steps.put(gptStep);

        payload.put("steps", steps);

        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(JSON, payload.toString());
        Request request = new Request.Builder()
                .url(API_URL)
                .header("api-key", API_KEY)
                .header("content-type", "application/json")
                .post(body)
                .build();

        Call call = client.newCall(request);
        Response response = call.execute();
        String responseBody = response.body().string();

        return responseBody;
    }


    private static void printQuestionList(List<Question> questions) {
        if(questions == null) {
            System.out.println("No questions generated.");
            return;
        }
        for (int i = 0; i < questions.size(); i++) {
            Question question = questions.get(i);
            System.out.println("Question " + (i + 1) + ": " + question.getQuestion());
            System.out.println("Choices: " + question.getChoices());
            System.out.println("Correct Answer: " + question.getCorrectAnswer());
            System.out.println();
        }

    }

    private static String extractResponseString(JSONObject jsonObject) {
        JSONArray outputArray = jsonObject.getJSONArray("output");
        JSONObject outputObject = outputArray.getJSONObject(0);
        JSONArray contentsArray = outputObject.getJSONArray("contents");
        JSONObject contentsObject = contentsArray.getJSONObject(0);
        return contentsObject.getString("utterance");
    }


        private static List<Question> parseQuiz(String jsonString) {
            List<Question> questions = new ArrayList<>();

            try {
                JSONObject jsonObject = new JSONObject(jsonString);
                JSONArray contentsArray = jsonObject.getJSONArray("output").getJSONObject(0).getJSONArray("contents");

                for (int i = 0; i < contentsArray.length(); i++) {
                    JSONObject contentObj = contentsArray.getJSONObject(i);
                    String utterance = contentObj.getString("utterance");

                    // Extract question and answer choices
                    String[] parts = utterance.split("\\n");
                    if (parts.length >= 2 && parts[0].startsWith("Q:")) {
                        String question = parts[0].substring(3).trim();
                        List<String> choices = new ArrayList<>();
                        String correctAnswer = "";

                        for (int j = 1; j < parts.length; j++) {
                            String part = parts[j].trim();
                            if (part.matches("[A-D]:.*")) {
                                choices.add(part.substring(3));
                                if (part.startsWith("A:")) {
                                    correctAnswer = part.substring(3);
                                }
                            }
                        }

                        questions.add(new Question(question, choices, correctAnswer));
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return questions;
        }


    private static final String QUESTION_PATTERN = "Q: (.+)\nA\\) (.+)\nB\\) (.+)\nC\\) (.+)\nD\\) (.+)";

//    public static List<Question> parseQuestions(String quizString) {
//        List<Question> questions = new ArrayList<>();
//
//        Pattern pattern = Pattern.compile(QUESTION_PATTERN);
//        Matcher matcher = pattern.matcher(quizString);
//
//        while (matcher.find()) {
//            String questionText = matcher.group(1);
//            String choiceA = matcher.group(2);
//            String choiceB = matcher.group(3);
//            String choiceC = matcher.group(4);
//            String choiceD = matcher.group(5);
//
//            Question question = new Question(questionText, choiceA, choiceB, choiceC, choiceD);
//            questions.add(question);
//        }
//
//        return questions;
//    }


    private static List<Question> parseQuestionsFromString(String questionsString) {
        List<Question> questionList = new ArrayList<>();

        String[] questionStrings = questionsString.split("\n\nQ: ");
        for (String questionString : questionStrings) {
            String[] parts = questionString.split("\n");
            String question = parts[0].trim();
            List<String> choices = new ArrayList<>();
            String correctAnswer = null;

            for (int i = 1; i < parts.length; i++) {
                String choiceString = parts[i].trim();
                if (choiceString.startsWith("A) ")) {
                    choices.add(choiceString.substring(3));
                    correctAnswer = choiceString.substring(3);
                } else if (choiceString.startsWith("B) ")) {
                    choices.add(choiceString.substring(3));
                } else if (choiceString.startsWith("C) ")) {
                    choices.add(choiceString.substring(3));
                } else if (choiceString.startsWith("D) ")) {
                    choices.add(choiceString.substring(3));
                }
//                else if (choiceString.startsWith("Correct Answer: ")) {
//                    correctAnswer = choiceString.substring(16);
//                }
            }

            questionList.add(new Question(question, choices, null));
        }

        return questionList;
    }





    public static void main(String[] args) {
        for (int i = 0; i < 4; i++) {
            try {
                String quiz = generateFormatQuizTom("AI");
                System.out.println(quiz);
                System.out.println("trying to parse quiz");
                String response = extractResponseString(new JSONObject(quiz));
                List<Question> questionList = new ArrayList<>();
                questionList = parseQuestionsFromString(response);
                printQuestionList(questionList);

            } catch (IOException e) {
                e.printStackTrace();
            }

            System.out.println("\n\n--------------------\n\n");

            try {
                String quizz = generateFormatQuizFrenchGuy("Android Development!!!", 5);
                System.out.println(quizz);
                System.out.println("trying to parse quiz");
                String response2 = extractResponseString(new JSONObject(quizz));
                List<Question> questionList2 = parseQuestionsFromString(response2);
                printQuestionList(questionList2);
            } catch (IOException e) {
                e.printStackTrace();
            }

            System.out.println("\n\n--------------------\n\n");

        }

    }




}
