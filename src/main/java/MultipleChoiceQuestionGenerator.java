
import okhttp3.*;
import okhttp3.MediaType;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

//import java.io.Console;

public class MultipleChoiceQuestionGenerator {

    private static final String API_KEY = System.getenv("OPENAI_API_KEY");
    private static final String API_URL = System.getenv("OPENAI_API_URL");
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");


    public static JSONArray generateMultipleChoiceQuestions(String subject) throws IOException {
        OkHttpClient client = new OkHttpClient();

        JSONObject payload = new JSONObject();
        payload.put("input", subject);
        payload.put("input_type", "article");
        payload.put("output_type", "json");
        payload.put("multilingual", new JSONObject().put("enabled", true));

        JSONArray steps = new JSONArray();

        JSONObject gptStep = new JSONObject();
        gptStep.put("skill", "gpt");
        JSONObject gptParams = new JSONObject();
        gptParams.put("gpt_engine", "gpt-3-5-turbo");
        gptParams.put("prompt_position", "start");
        gptParams.put("temperature", 1);
        gptParams.put("prompt", "give me a multiple choice quiz about ");
        gptStep.put("params", gptParams);
        steps.put(gptStep);

        JSONObject sentencesStep = new JSONObject();
        sentencesStep.put("skill", "sentences");
        steps.put(sentencesStep);

        payload.put("steps", steps);

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

        JSONObject json = new JSONObject(responseBody);
        JSONArray outputArray = json.getJSONArray("output");

        System.out.println(outputArray);
        return outputArray;
//
//        for (int i = 0; i < outputArray.length(); i++) {
//            JSONObject output = outputArray.getJSONObject(i);
//            if (output.has("contents")) {
//                JSONArray contents = output.getJSONArray("contents");
//                for (int j = 0; j < contents.length(); j++) {
//                    JSONObject content = contents.getJSONObject(j);
//                    if (content.has("utterance")) {
//                        String utterance = content.getString("utterance");
//                        System.out.println(utterance);
//                    }
//                }
//            }
//        }
    }

    @java.lang.Deprecated
    public static JSONArray generateFormatedMultipleChoiceQuestions(String subject) throws IOException {
        OkHttpClient client = new OkHttpClient();

        JSONObject payload = new JSONObject();
        payload.put("input", "Generate multiple choice questions about " + subject);
        payload.put("input_type", "text");
        payload.put("output_type", "json");
        payload.put("max_tokens", 500);
        payload.put("temperature", 0.7);

        JSONArray steps = new JSONArray();

        JSONObject gptStep = new JSONObject();
        gptStep.put("skill", "gpt");
        JSONObject gptParams = new JSONObject();
        gptParams.put("gpt_engine", "gpt-3.5-turbo");
        gptParams.put("prompt_position", "replace");
        gptParams.put("prompt", "### ");
        gptStep.put("params", gptParams);
        steps.put(gptStep);

        JSONObject regexStep = new JSONObject();
        regexStep.put("skill", "regex");
        JSONObject regexParams = new JSONObject();
        regexParams.put("pattern", "\\d+\\)"); // Extracts the question number
        regexParams.put("group_names", new JSONArray().put("question_number"));
        regexStep.put("params", regexParams);
        steps.put(regexStep);

        JSONObject sentencesStep = new JSONObject();
        sentencesStep.put("skill", "sentences");
        steps.put(sentencesStep);

        payload.put("steps", steps);

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

        JSONObject json = new JSONObject(responseBody);
        JSONArray outputArray = json.getJSONArray("output");

        return outputArray;
    }


    @java.lang.Deprecated
    public static JSONArray generateFormatedMultipleChoiceQuestions2(String subject) throws IOException {
        OkHttpClient client = new OkHttpClient();

        JSONObject payload = new JSONObject();
        payload.put("input", subject);
        payload.put("input_type", "text");
        payload.put("output_type", "json");
        payload.put("max_tokens", 400);

        JSONArray steps = new JSONArray();

        // Step 1: Prompt for the multiple choice questions
        JSONObject promptStep = new JSONObject();
        promptStep.put("role", "prompt");
        promptStep.put("content", "Please generate a multiple-choice quiz about " + subject + ". The quiz should contain 10 questions. Each question should have 4 answer options labeled A, B, C, and D. Ensure that each question is clear and concise.");
        steps.put(promptStep);

        // Step 2: Generate the multiple choice questions
        JSONObject gptStep = new JSONObject();
        gptStep.put("role", "system");
        gptStep.put("model", "gpt-3.5-turbo");
        gptStep.put("prompt", "Q:");
        gptStep.put("temperature", 0.7);
        gptStep.put("max_tokens", 100);
        gptStep.put("stop", "\n");
        steps.put(gptStep);

        payload.put("steps", steps);

        RequestBody body = RequestBody.create(JSON, payload.toString());
        Request request = new Request.Builder()
                .url(API_URL)
                .header("Authorization", "Bearer " + API_KEY)
                .header("Content-Type", "application/json")
                .post(body)
                .build();

        Call call = client.newCall(request);
        Response response = call.execute();
        String responseBody = response.body().string();

        JSONObject json = new JSONObject(responseBody);
        JSONArray outputArray = json.getJSONArray("choices").getJSONObject(0).getJSONArray("text");

        return outputArray;
    }


    public static JSONArray generateFormatedMultipleChoiceQuestions3(String subject) throws IOException {
        OkHttpClient client = new OkHttpClient();

        JSONObject payload = new JSONObject();
        payload.put("input", subject);
        payload.put("input_type", "article");
        payload.put("output_type", "json");
        payload.put("multilingual", new JSONObject().put("enabled", true));

        JSONArray steps = new JSONArray();

        JSONObject gptStep = new JSONObject();
        gptStep.put("skill", "gpt");
        JSONObject gptParams = new JSONObject();
        gptParams.put("gpt_engine", "gpt-3.5-turbo");
        gptParams.put("prompt_position", "replace");
        gptParams.put("temperature", 0.8);
        gptParams.put("max_tokens", 500);
        gptParams.put("prompt", "Given the subject: " + subject + ", please generate a multiple-choice quiz with 10 questions. Each question should follow the format:\n\n[Question Number]) [Question Text]\nA) [Option A]\nB) [Option B]\nC) [Option C]\nD) [Option D]\n\nMake sure to provide clear and concise questions related to the subject matter. Limit the answer choices to one correct option (A, B, C, or D).");

        gptStep.put("params", gptParams);
        steps.put(gptStep);

        JSONObject sentencesStep = new JSONObject();
        sentencesStep.put("skill", "sentences");
        steps.put(sentencesStep);

        payload.put("steps", steps);

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

        JSONObject json = new JSONObject(responseBody);
        JSONArray outputArray = json.getJSONArray("output");

        return outputArray;
    }




    public static void main(String[] args) {
        JSONArray outputArray = null;
        String s = ";";
        do {
            try {
                outputArray = generateMultipleChoiceQuestions("Math");
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println(e.getMessage());
                s = e.getMessage();
            }
        } while (Objects.equals(s, "timeout"));
        if(outputArray == null) {
            System.out.println("outputArray is null");
            return;
        }
        String str = outputArray.toString();

        JSONArray array = new JSONArray(str);

        List<Question> questions = new ArrayList<>();
        try {
            questions = QuestionConverter.convertToQuestions(outputArray);
        } catch (Exception e) {
            e.printStackTrace();
        }

        for (Question question : questions) {
            System.out.println("Question: " + question.getQuestion());
            System.out.println("Choices: " + question.getChoices());
            System.out.println("Correct Answer: " + question.getCorrectAnswer());
            System.out.println();
        }

        System.out.println("--------------------------------------------------");

        List<Question> questions2 = new ArrayList<>();
        try {
            questions2 = QuestionConverter.convertToQuestions2(array);
        } catch (Exception e) {
            e.printStackTrace();
        }

        for (Question question : questions2) {
            System.out.println("Question: " + question.getQuestion());
            System.out.println("Choices: " + question.getChoices());
            System.out.println("Correct Answer: " + question.getCorrectAnswer());
            System.out.println();
        }


        System.out.println("--------------------------------------------------");


        List<Question> questions3 = QuestionFormatter.formatQuestions(new JSONArray(str));


        for (Question question : questions3) {
            System.out.println("Question: " + question.getQuestion());
            System.out.println("Choices: " + question.getChoices());
            System.out.println("Correct Answer: " + question.getCorrectAnswer());
            System.out.println();
        }

    }
}

