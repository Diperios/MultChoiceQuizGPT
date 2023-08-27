import okhttp3.*;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


@Deprecated
public class DeprecatedTempTests {

    @java.lang.Deprecated
    public static class MultipleChoiceQuestionGeneratorColdTemp {

        private static final String API_KEY = System.getenv("OPENAI_API_KEY");
        private static final String API_URL = System.getenv("OPENAI_API_URL");
        private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

        public static void generateMultipleChoiceQuestionsColdTemp(String subject) throws IOException {
            OkHttpClient client = new OkHttpClient();

            JSONObject requestBody = new JSONObject();
            requestBody.put("prompt", "Subject: " + subject + "\n\nQuestion:\n");
            requestBody.put("max_tokens", 100);
            requestBody.put("temperature", 0.5);

            RequestBody body = RequestBody.create(JSON, requestBody.toString());
            Request request = new Request.Builder()
                    .url(API_URL)
                    .header("Authorization", "Bearer " + API_KEY)
                    .post(body)
                    .build();

            Call call = client.newCall(request);
            Response response = call.execute();
            String responseBody = response.body().string();
            System.out.println(responseBody);

            JSONObject json = new JSONObject(responseBody);
            JSONArray choices = json.getJSONArray("choices");
            if (choices.length() > 0) {
                JSONObject result = choices.getJSONObject(0);
                System.out.println(result);
                String[] lines = result.getString("text").split("\n");

                String question = lines[0];
                List<String> choiceList = new ArrayList<>();
                for (int i = 1; i < lines.length - 1; i++) {
                    choiceList.add(lines[i]);
                }
                String correctChoice = lines[lines.length - 1];

                // Use the question, choiceList, and correctChoice as needed
                // E.g., pass them to your UI/UX components for display
                System.out.println("Question: " + question);
                System.out.println("Choices: " + choiceList);
                System.out.println("Correct Choice: " + correctChoice);
            }
        }

        public static void main(String[] args) {
            try {
                generateMultipleChoiceQuestionsColdTemp("HOW TO BE TALLER");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }



        @java.lang.Deprecated
        public class MultipleChoiceQuestionGeneratorHotTemp {

            private static final String API_KEY = "f5d0858e-3eab-4894-8a1e-d02f8addb54b";
            private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
            private static final String API_URL = "https://api.oneai.com/api/v0/pipeline";

            public static void generateMultipleChoiceQuestionsHotTemp(String subject) throws IOException {
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
                System.out.println(json);
            }

            public static void main(String[] args) {
                try {
                    generateMultipleChoiceQuestionsHotTemp("Math");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }



}
