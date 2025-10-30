package com.cwk.ui;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.ServerSocket;
import java.net.URL;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    private static final int BACKEND_PORT = 8080;
    private static final String BACKEND_URL = "http://localhost:" + BACKEND_PORT + "/api/products";

    @Override
    public void start(Stage stage) throws Exception {
        ensureBackendRunning();

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/login.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);
        stage.setTitle("Billing Software - CWK");
        stage.setScene(scene);
        stage.show();
    }

    private void ensureBackendRunning() {
        try {
            if (isPortInUse(BACKEND_PORT)) {
                System.out.println("⚙️ Backend already running on port " + BACKEND_PORT);
            } else {
                System.out.println("🚀 Starting backend...");
                startBackendProcess();
                waitForBackendReady();
            }
        } catch (Exception e) {
            System.err.println("❌ Error ensuring backend: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void startBackendProcess() throws IOException {
        ProcessBuilder processBuilder = new ProcessBuilder(
                "java", "-jar", "../backend/target/billing-backend-0.0.1-SNAPSHOT.jar"
        );
        processBuilder.inheritIO();
        processBuilder.start();
    }

    private boolean isPortInUse(int port) {
        try (ServerSocket ignored = new ServerSocket(port)) {
            return false; // port available
        } catch (IOException e) {
            return true; // port in use
        }
    }

    private void waitForBackendReady() throws InterruptedException {
        int waited = 0;
        boolean isUp = false;

        while (waited < 40) { // wait max 40 seconds
            try {
                URL url = new URL(BACKEND_URL);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setConnectTimeout(2000);
                conn.connect();

                if (conn.getResponseCode() == 200) {
                    isUp = true;
                    break;
                }
            } catch (IOException ignored) {}

            System.out.println("⏳ Waiting for backend... " + waited + "s");
            Thread.sleep(2000);
            waited += 2;
        }

        if (!isUp) {
            throw new RuntimeException("Backend failed to start within 40 seconds");
        }

        System.out.println("✅ Backend is ready and running on port " + BACKEND_PORT);
    }

    public static void main(String[] args) {
        launch();
    }
}
