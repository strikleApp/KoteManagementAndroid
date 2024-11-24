package com.android.kotemanagement.authentication;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import java.util.concurrent.Executor;

public class FingerprintAuthenticator {

    public interface AuthenticationCallback {
        void onAuthenticationResult(boolean isSuccess);
    }

    private final Context context;

    public FingerprintAuthenticator(Context context) {
        this.context = context;
    }

    public void authenticate(@NonNull FragmentActivity activity, @NonNull AuthenticationCallback callback) {
        // Check if biometric authentication is available
        BiometricManager biometricManager = BiometricManager.from(context);
        if (biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG) != BiometricManager.BIOMETRIC_SUCCESS) {
            // Biometric not supported or unavailable
            callback.onAuthenticationResult(false);
            return;
        }

        // Executor for the authentication
        Executor executor = ContextCompat.getMainExecutor(context);

        // BiometricPrompt instance
        BiometricPrompt biometricPrompt = new BiometricPrompt(activity, executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                new Handler(Looper.getMainLooper()).post(() -> callback.onAuthenticationResult(true));
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                new Handler(Looper.getMainLooper()).post(() -> callback.onAuthenticationResult(false));
            }

            @Override
            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                new Handler(Looper.getMainLooper()).post(() -> callback.onAuthenticationResult(false));
            }
        });

        // BiometricPrompt information
        BiometricPrompt.PromptInfo promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Authenticate with Fingerprint")
                .setSubtitle("Place your finger on the sensor to proceed")
                .setNegativeButtonText("Cancel")
                .build();

        // Show the biometric prompt
        biometricPrompt.authenticate(promptInfo);
    }
}
