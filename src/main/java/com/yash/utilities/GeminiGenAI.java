package com.yash.utilities;

import com.google.genai.Client;
import com.google.genai.types.GenerateContentResponse;
import com.yash.Configuration;

public class GeminiGenAI {
	private GeminiGenAI() {
		super();
	}

	public static void main(String[] args) {
		System.out.println(generateSubtasks("Website for e-commerce", "A website that has delivery option and payment gateway for online payment"));
	}

	public static String generateSubtasks(final String title, final String description) {
		final Client client = new Client.Builder().apiKey(Configuration.GEMINI_API_KEY).build();
		final String prompt =
			"You are a project analysis assistant." + "\n" +
			"\n" +
			"Analyze the following task and respond ONLY with a valid JSON object." + "\n" +
			"Do NOT include any explanation, markdown, or code fences." + "\n" +
			"Your response must strictly match this JSON schema:" + "\n" +
			"\n" +
			"{" + "\n" +
			"	\"tasks\": [" + "\n" +
			"		{" + "\n" +
			"			\"title\": \"string\"," + "\n" +
			"			\"description\": \"string\"," + "\n" +
			"			\"priority\": \"URGENT | MAJOR | USUAL | MINOR | SLIGHT\"," + "\n" +
			"			\"dueDate\": \"date\"" + "\n" +
			"		}" + "\n" +
			"	]" + "\n" +
			"}" + "\n" +
			"\n" +
			"Task to analyze:" + "\n" +
			"This is the title \"" + title + "\" and this is the description \"" + description +  "\".\n" +
			"Make number of tasks to acheive this goal by generating given JSON schema." + "\n";
		final GenerateContentResponse generateContentResponse = client.models.generateContent("gemini-2.5-flash", prompt, null);
		// System.out.println(generateContentResponse.text());
		return generateContentResponse.text();
	}
}
