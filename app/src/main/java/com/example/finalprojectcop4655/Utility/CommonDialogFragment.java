package com.example.finalprojectcop4655.Utility;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.example.finalprojectcop4655.R;


public class CommonDialogFragment extends DialogFragment {
    private String mMessage;
    private String mTitle;
    private DialogCallback dialogCallback;
    private int mType;

    public static CommonDialogFragment newInstance(String title,String message,DialogCallback dialogCallback,int type){
        CommonDialogFragment commonDialogFragment = new CommonDialogFragment();
        commonDialogFragment.mMessage = message;
        commonDialogFragment.mTitle = title;
        commonDialogFragment.dialogCallback = dialogCallback;
        commonDialogFragment.mType = type;
        commonDialogFragment.setCancelable(false);
        return commonDialogFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(savedInstanceState!=null) dismiss();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_fragment, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Get field from view
        TextView message = (TextView) view.findViewById(R.id.message_fragment);
        TextView title = (TextView) view.findViewById(R.id.title_fragment);
        Button button = (Button) view.findViewById(R.id.dialog_ok);
        // Fetch arguments from bundle and set title
        title.setText(mTitle);
        message.setText(mMessage);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                dialogCallback.onButtonPressed(mType);
            }
        });

        // Show soft keyboard automatically and request focus to field
        message.requestFocus();
        getDialog().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
    }


    public interface DialogCallback{
        void onButtonPressed(int type);
    }

}