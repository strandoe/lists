package com.oysteinstrand.lists.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.Editable;
import android.text.InputType;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;

import java.util.UUID;

public class Dialogs {

    public static abstract class InputDialogListener {
        public abstract void savedWithText(String text);
    }

    public static void showInputDialog(Context context, String title, String hint, final InputDialogListener delegate) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        final EditText input = new EditText(context);
        input.setHint(hint);
        input.setPadding(24, 48, 24, 24);
        input.setRawInputType(InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);

        builder.setView(input);
        builder.setTitle(title);

        final UUID uuid = UUID.randomUUID();

        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface diag, int id) {
                Editable text = input.getText();
                if (text.length() > 0) {
                    delegate.savedWithText(text.toString());
                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });

        final AlertDialog dialog = builder.create();

        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                input.requestFocus();
            }
        });

        input.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                }
            }
        });

        dialog.show();
    }
}
