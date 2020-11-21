package com.example.mvp_v3;

import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import services.BackendAuthenticationService;


public class MainActivity extends AppCompatActivity implements LoginViewPresenter.IObjectsInitializer,
        LoginViewPresenter.IProgressBarVisibilitySwitch,
        LoginViewPresenter.ILoginButtonEnableState,
        LoginViewPresenter.ILoginButtonClicked,
        LoginViewPresenter.ITextViewVisibilitySwitch,
        LoginViewPresenter.INavigatetoMVVMDatabinindingButtonEnableStateChanged,
        LoginViewPresenter.IUserCredentialsValidationService,
        LoginViewPresenter.IAuthenticationResult,
        LoginViewPresenter.IConfigureAndStatesOfBackendAuthenticationService {

    public final static String INTENT_KEY_START_BACKEND_SERVICE_FOR_AUTHENTICATION_PROCESS = "KEY_BACKEND_SERVICE_FOR_AUTHENTICATION_PROCESS";
    public final static String INTENT_VALUE_COMMENCE_BACKEND_SERVICE_FOR_AUTHENTICATING_PROCESS = "VALUE_START_BACKEND_SERVICE_FOR_AUTHENTICATING_PROCESS";

    //member variables
    protected LoginViewPresenter mLoginViewPresenter = null;
    protected EditText mETUserName = null;
    protected EditText mETUserPassword = null;
    protected Button mBtnLogin = null;
    protected TextView mTextViewBackendAuthenticationFeedBack = null;
    protected ProgressBar mProgressBarLoginInProgress = null;
    protected ConstraintLayout mMainContainer = null;
    protected Intent mIntentStartBackendAuthenticationService;
    protected Button mBtnNavigateToMVVM = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mLoginViewPresenter = new LoginViewPresenter(this);
        mLoginViewPresenter.onCreate();
    }

    //#Implementation of Interfaces

    //#implementation of LoginViewPresenter.IObjectsInitializer
    public ConstraintLayout onInitializingMainContainerConstraintLayout() {
        return this.mMainContainer = findViewById(R.id.constraintLayout);
    }

    public EditText onInitializingUserNameEditText() {
        return this.mETUserName = findViewById(R.id.etUserName);
    }

    public EditText onInitializingUserPasswordEditText() {
        return this.mETUserPassword = findViewById(R.id.etUserPassword);
    }

    public Button onInitializingLoginButton() {
        return this.mBtnLogin = findViewById(R.id.btnLogin);
    }

    public TextView onInitializingForBackendResultTextView() {
        return this.mTextViewBackendAuthenticationFeedBack = findViewById(R.id.tvBackendResult);
    }

    public ProgressBar onInitializingWaitingForBackendToAuthenticateProgressBar() {
        return this.mProgressBarLoginInProgress = findViewById(R.id.progressBarLoginInProgress);
    }

    public Button onInitializingNavigateToMVVMDataBindingButton() {
        return this.mBtnNavigateToMVVM = findViewById(R.id.btnNavigateToMVVM_Databinding_v1);
    }
    //#implementation of LoginViewPresenter.IProgressBarVisibilitySwitch
    @Override
    public void onProgressBarVisibilitySetToVisible() {
        this.mProgressBarLoginInProgress.setVisibility(View.VISIBLE);
    }

    @Override
    public void onProgressBarVisibilitySetToGone() {
        this.mProgressBarLoginInProgress.setVisibility(View.GONE);
    }

    //#implementation of LoginViewPresenter.ITextViewVisibilitySwitch
    @Override
    public void onTextViewVisibilitySetToVisible() {
        this.mTextViewBackendAuthenticationFeedBack.setVisibility(View.VISIBLE);
    }

    @Override
    public void onTextViewVisibilitySetToGone() {
        this.mTextViewBackendAuthenticationFeedBack.setVisibility(View.GONE);
    }

    //#implementation of LoginViewPresenter.ILoginButtonEnableState
    @Override
    public void onLoginButtonEnabled() {
        this.mBtnLogin.setEnabled(true);
    }

    @Override
    public void onLoginButtonDisabled() {
        this.mBtnLogin.setEnabled(false);
    }

    //#implementation of LoginViewPresenter.ILoginButtonClicked
    @Override
    public void clearEditTextInputForUserName() {
        this.mETUserName.setText("");
    }

    @Override
    public void clearEditTextInputForPassword() {
        this.mETUserPassword.setText("");
    }

    @Override
    public void clearBackendAuthenticationFeedbackTextView() {
        this.mTextViewBackendAuthenticationFeedBack.setText("");
    }

    //#implementation of LoginViewPresenter.IUserCredentialsValidationService
    public void onUserCredentialsAreValid() {
        Toast.makeText(this, "User credentials entered are valid and matching the template.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onUserNameIsInvalid() {
        Toast.makeText(this, "User name entered is not valid", Toast.LENGTH_SHORT).show();
        if (!this.mBtnLogin.isEnabled()) {
            this.mBtnLogin.setEnabled(true);
        }
    }

    @Override
    public void onUserPasswordIsInvalid() {
        Toast.makeText(this, "User password entered is not valid", Toast.LENGTH_SHORT).show();
        if (!this.mBtnLogin.isEnabled()) {
            this.mBtnLogin.setEnabled(true);
        }
    }

    @Override
    public void onUserNameIsNull() {
        Toast.makeText(this, "User name member variable is null", Toast.LENGTH_SHORT).show();
        if (!this.mBtnLogin.isEnabled()) {
            this.mBtnLogin.setEnabled(true);
        }
    }

    @Override
    public void onUserPasswordIsNull() {
        Toast.makeText(this, "User password member variable is null", Toast.LENGTH_SHORT).show();
        if (!this.mBtnLogin.isEnabled()) {
            this.mBtnLogin.setEnabled(true);
        }
    }


    //#implementation of LoginViewPresenter.IAuthenticationResult
    @Override
    public void onAuthenticationError(String resultMsg) {
        this.mTextViewBackendAuthenticationFeedBack.setText(resultMsg);
    }

    @Override
    public void onAuthenticationSuccessful(String resultMsg) {
        this.mTextViewBackendAuthenticationFeedBack.setText(resultMsg);
    }

    @Override
    public void onAuthenticationFailed(String resultMsg) {
        this.mTextViewBackendAuthenticationFeedBack.setText(resultMsg);
    }

    //#Lifcycle callbacks
    @Override
    protected void onStart() {
        super.onStart();
        this.mLoginViewPresenter.onStart();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        this.mLoginViewPresenter.onRestart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        this.mLoginViewPresenter.onResume();
        //to-do if not logging in activate
        //activateLoginButton();
        this.mBtnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userName = mETUserName.getText().toString();
                String userPassword = mETUserPassword.getText().toString();
                mLoginViewPresenter.validateUserCredentials(userName, userPassword);
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        mLoginViewPresenter.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mLoginViewPresenter.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mLoginViewPresenter.onDestroy();
    }

    @Override
    public void onConfigureIntentForStartingBackendAuthenticationServiceWith(ResultReceiver resultReceiver) {
        mIntentStartBackendAuthenticationService = new Intent(this, BackendAuthenticationService.class);
        mIntentStartBackendAuthenticationService.putExtra(INTENT_KEY_START_BACKEND_SERVICE_FOR_AUTHENTICATION_PROCESS, INTENT_VALUE_COMMENCE_BACKEND_SERVICE_FOR_AUTHENTICATING_PROCESS);
        mIntentStartBackendAuthenticationService.putExtra(BackendAuthenticationService.INTENT_KEY_FOR_RESULT_RECEIVER, resultReceiver);
    }

    @Override
    public void onStartBackendAuthenticationService() {
        this.startService(this.mIntentStartBackendAuthenticationService);
    }

    @Override
    public void onStopBackendAuthenticationService() {
        this.stopService(this.mIntentStartBackendAuthenticationService);
    }

    @Override
    public void enableNavigateToMVVMDataBindingButton() {
        this.mBtnNavigateToMVVM.setEnabled(true);
    }

    @Override
    public void disableNavigateToMVVMDataBindingButton() {
        this.mBtnNavigateToMVVM.setEnabled(false);
    }
}
