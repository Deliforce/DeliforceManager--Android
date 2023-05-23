package com.app.deliforcemanager.CustomDialogView;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.app.deliforcemanager.R;
import com.google.android.material.textfield.TextInputLayout;

public class TaskDetailEditTextDialog extends Dialog {
    private Button btnOK;
    private TextInputLayout tlView;
    private EditText edView;
    private TextView tvHeader;
    private editTextInterface textInterface;
    private String selectedValue;
    private  String headerValue;
    private int typeclass;
    private String edType;
    private Context context;
    
    public TaskDetailEditTextDialog(@NonNull Context context, int myDialogStyle,
                                    String previousValue, String fieldName, String fieldValue,
                                    int typeClassNumber, String dataType, editTextInterface textInterface) {
        super(context, myDialogStyle);
        this.selectedValue =fieldValue;
        this.edType =dataType;
        this.typeclass=typeClassNumber;
        this.headerValue =fieldName;
        this.textInterface =textInterface;
        this.context =context;
        
    }

  

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.alertdialog_content_view);
        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

        tvHeader=findViewById(R.id.tvTitle);
        tlView=findViewById(R.id.name_text_input);
        edView=findViewById(R.id.name_edit_text);
        btnOK=findViewById(R.id.btn_ok);
        
        tvHeader.setText(headerValue);
        edView.setText(selectedValue);
        edView.setInputType(typeclass);
        edView.setSelection(selectedValue.length());


        if (typeclass == 32 ) {
            tlView.setHint(context.getString(R.string.error_msg_invalid_email));

        } else if (typeclass == 31) {
            tlView.setHint(context. getString(R.string.error_url));

        }

        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (typeclass==32){
                    if (!vaidateEmail()){
                        return;
                    }
                }else if (typeclass==31){
                    if (!validateUrl()){
                        return;
                    }
                }else{
                    if (!validateEmpty()){
                        return;
                    }
                }

                if (textInterface!=null){
                    textInterface.getEditValue(edView.getText().toString().trim());
                }
                dismiss();
                
                
            }
        });
    }

    private boolean validateEmpty() {
        if(edView.getText().toString().isEmpty()){
            edView.setError(context.getString(R.string.please_enter));
            return false;

        }else{
            edView.setError(null);

        }
        return true;
    }

    private boolean validateUrl() {
        if(edView.getText().toString().isEmpty() &!isValidURLPattern(edView.getText().toString()) ){
            edView.setError(context.getString(R.string.error_url));
            return false;

        }else{
            edView.setError(null);

        }
        return true;
    }

    private boolean vaidateEmail() {

        if(edView.getText().toString().isEmpty() &!isValidPattern(edView.getText().toString()) ){
            edView.setError(context.getString(R.string.error_msg_invalid_email));
            return false;

        }else{
            edView.setError(null);

        }
        return true;
    }

    public interface editTextInterface{
        void getEditValue(String value);
    }

    public static boolean isValidPattern(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }

    public static boolean isValidURLPattern(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.WEB_URL.matcher(target).matches());
    }

}
