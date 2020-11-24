package com.example.mvp_v3.loginScreen;

import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mvp_v3.R;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import services.BackendAuthenticationService;


public class MainActivity extends AppCompatActivity implements LoginViewPresenter.IObjectsInitializer,
        LoginViewPresenter.IProgressBarVisibilitySwitch,
        LoginViewPresenter.ILoginButtonEnableState,
        LoginViewPresenter.ILoginButtonClicked,
        LoginViewPresenter.ITextViewVisibilitySwitch,
        LoginViewPresenter.INavigatetoMVVMDatabininBingButtonEnableStateChanged,
        LoginViewPresenter.IUserCredentialsValidationService,
        LoginViewPresenter.IAuthenticationResult,
        LoginViewPresenter.IConfigureAndStatesOfBackendAuthenticationService,
        LoginViewPresenter.IOnNavigateToMVVMDataBindingButtonClicked
{

    private final static String TAG = MainActivity.class.getSimpleName();
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
    protected Intent mIntentStartBackendAuthenticationService = null;
    protected Intent mIntentStartMVVMDataBindingV1Activity = null;
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
    public void changeBackendFeedBackTextViewToVisible() {
        this.mTextViewBackendAuthenticationFeedBack.setVisibility(View.VISIBLE);
    }

    @Override
    public void onTextViewVisibilitySetToGone() {
        this.mTextViewBackendAuthenticationFeedBack.setVisibility(View.GONE);
    }

    //#implementation of LoginViewPresenter.ILoginButtonEnableState
    @Override
    public void enableLoginButton() {
        this.mBtnLogin.setEnabled(true);
    }

    @Override
    public void disabledLoginButton() {
        this.mBtnLogin.setEnabled(false);
    }

    //#implementation of LoginViewPresenter.ILoginButtonClicked
    @Override
    public void clearUserNameEditText() {
        this.mETUserName.setText("");
    }

    @Override
    public void clearPasswordEditText() {
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
        this.mBtnLogin.setOnClickListener(v-> {

                String userName = mETUserName.getText().toString();
                String userPassword = mETUserPassword.getText().toString();
                mLoginViewPresenter.validateUserCredentials(userName, userPassword);
            });

        this.mBtnNavigateToMVVM.setOnClickListener(view -> {
            Toast.makeText(getApplicationContext(), "CLICKED", Toast.LENGTH_SHORT).show();
            ongNavigateToMVVMDataBindingButtonClicked();
        });
    }

    public void ongNavigateToMVVMDataBindingButtonClicked() {
        mLoginViewPresenter.ongNavigateToMVVMDataBindingButtonClicked();
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

    //#implementation of interface IConfigureAndStatesOfBackendAuthenticationService
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


    //#implementation of interface IOnNavigateToMVVMDataBindingButtonClicked
    @Override
    public void configureIntentForStartingMVVMDataBindingV1Activity() {
        this.mIntentStartMVVMDataBindingV1Activity = new Intent();
    }

    @Override
    public void starMVVMDataBindingV1Activity() {
        startActivity(this.mIntentStartMVVMDataBindingV1Activity);
    }

    //#implementation of interface INavigatetoMVVMDatabininBingButtonEnableStateChanged
    @Override
    public void enableNavigateToMVVMDataBindingButton() {
        this.mBtnNavigateToMVVM.setEnabled(true);
    }

    @Override
    public void disableNavigateToMVVMDataBindingButton() {
        this.mBtnNavigateToMVVM.setEnabled(false);
    }

}
