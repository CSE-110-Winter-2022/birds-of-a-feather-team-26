package com.example.birdsofafeather;

import android.content.Context;

import androidx.appcompat.app.AlertDialog;

import java.util.Optional;

/**
 * DESCRIPTION
 * The Utilities module holds utility functions for showing alerts and parsing count
 **/

public class Utilities {
    public static void showAlert(Context context, String message) {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);

        alertBuilder
                .setTitle("Alert!")
                .setMessage(message)
                .setPositiveButton("Ok", (dialog, id) -> {
                    dialog.cancel();
                })
                .setCancelable(true);

        AlertDialog alertDialog = alertBuilder.create();
        alertDialog.show();
    }

    public static Optional<Integer> parseCount(String str) {
        try {
            int maxCount = Integer.parseInt(str);
            return Optional.of(maxCount);
        } catch (NumberFormatException e) {
            return Optional.empty();
        }
    }
}
