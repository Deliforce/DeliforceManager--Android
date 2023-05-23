package com.app.deliforcemanager.CustomDialogView;

import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.FragmentActivity;

import com.app.deliforcemanager.ModelClass.TaskDetailsApi.ActualCustomer;
import com.app.deliforcemanager.R;
import com.app.deliforcemanager.RawHeaders.TaskInfo.AddActualCustomer;
import com.app.deliforcemanager.Utils.LoginPrefManager;

public class AddActualCustomerDialog extends Dialog {

    public LoginPrefManager loginPrefManager;
    private FragmentActivity activity;
    private AddActualCustomerListener addActualCustomerListener;
    private Button btnCancel, btnAdd;
    EditText etCustomerName,etCustomerId;
    String task_id;
    ActualCustomer actualCustomer;
    TextView tv_add_reason_title;

    public AddActualCustomerDialog(@NonNull FragmentActivity activity, int themeResId, ActualCustomer actualCustomer,String task_id,AddActualCustomerListener addActualCustomerListener) {
        super(activity, themeResId);
        this.activity = activity;
        this.addActualCustomerListener = addActualCustomerListener;
        this.actualCustomer = actualCustomer;
        this.task_id = task_id;
        loginPrefManager = new LoginPrefManager(activity);
    }

    @RequiresApi(api = 28)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        setContentView(R.layout.dialog_actual_customer);

        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        getWindow().getAttributes().windowAnimations = R.style.PauseDialogAnimation;


        tv_add_reason_title = findViewById(R.id.tv_add_reason_title);
        btnCancel = findViewById(R.id.btnCancel);
        btnAdd = findViewById(R.id.btnAdd);
        etCustomerId = findViewById(R.id.etCustomerId);
        etCustomerName = findViewById(R.id.etCustomerName);

        loginPrefManager.setIsFailedOrCancelled(false);
        if(actualCustomer!=null) {
            etCustomerId.setText(actualCustomer.getActualCustomerId());
            etCustomerName.setText(actualCustomer.getActualCustomerName());
            btnAdd.setText(activity.getString(R.string.btn_update));
            tv_add_reason_title.setText(activity.getString(R.string.task_details_update_actual_customer));
        }
        onClickevetns();
    }


    private void onClickevetns() {

        btnAdd.setOnClickListener(view -> {
            if (!isValid()) {
                return;
            }

            if (addActualCustomerListener != null) {
                boolean isExisting = false;
                AddActualCustomer addActualCustomer = new AddActualCustomer();
                if(actualCustomer!=null) {
                    isExisting = true;
                    addActualCustomer.setActualCustomerId(etCustomerId.getText().toString());
                    addActualCustomer.setActualCustomerName(etCustomerName.getText().toString());
                    addActualCustomer.setTaskId(task_id);
                    addActualCustomer.set_id(actualCustomer.get_id());
                } else {
                    addActualCustomer.setActualCustomerId(etCustomerId.getText().toString());
                    addActualCustomer.setActualCustomerName(etCustomerName.getText().toString());
                    addActualCustomer.setTaskId(task_id);
                }
                addActualCustomerListener.onAdd(addActualCustomer,isExisting);
            }
        });

        btnCancel.setOnClickListener(view -> {
            dismiss();
        });
    }

    private boolean isValid() {
        if (etCustomerId.getText().toString().isEmpty()) {
            etCustomerId.setError(activity.getString(R.string.error_customer_id));
            return false;
        } else if (etCustomerName.getText().toString().isEmpty()) {
            etCustomerName.setError(activity.getString(R.string.error_customer_name));
            return false;
        }
        return true;
    }

    public interface AddActualCustomerListener {
        void onAdd(AddActualCustomer addActualCustomer,boolean isExisting);
    }
}
