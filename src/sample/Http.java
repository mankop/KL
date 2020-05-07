/*
package sample;

import okhttp3.*;

import java.io.IOException;

public class Http {

    // one instance, reuse
    private final HttpClient httpClient = new OkHttpClient();

    public static void main(String[] args) throws Exception {

        OkHttpExample obj = new OkHttpExample();

        System.out.println("Testing 1 - Send Http GET request");
        obj.sendGet();

        System.out.println("Testing 2 - Send Http POST request");
        obj.sendPost();

    }

    private void sendGet() throws Exception {

        Request request = new Request.Builder()
                .url("")
                .build();

        try (Response response = httpClient.newCall(request).execute()) {

            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

            // Get response body
            System.out.println(response.body().string());
        }

    }

    private void sendPost(String name, Double HS;) throws Exception {

        // form parameters
        RequestBody formBody = new FormBody.Builder()
                .add("username", name)
                .add("HS", HS)
                .build();

        Request request = new Request.Builder()
                .url("https://httpbin.org/post")
                .addHeader("User-Agent", "OkHttp Bot")
                .post(formBody)
                .build();

        try (Response response = httpClient.newCall(request).execute()) {

            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

            // Get response body
            return response.body().string();
        }

    }

}*/
