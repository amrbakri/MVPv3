package com.example.mvp_v3.loginScreen;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.lang.ref.WeakReference;

import androidx.constraintlayout.widget.ConstraintLayout;
import services.BackendAuthenticationService;

public class LoginViewPresenter implements UserCredentialsValidationService.IBackendResponseUseronCredentialValidation {

    //#public final static variables
    private final static String TAG = LoginViewPresenter.class.getSimpleName();
    //#member variables
    private EditText mETUserName = null;
    private EditText mETUserPassword = null;
    private Button mBtnLogin = null;
    private ProgressBar mProgressBarForLoginInProgress = null;
    private ConstraintLayout mMainContainer = null;
    protected UserCredentialsValidationService mUserCredentialsValidationService = null;
    protected Intent mIntentStartBackendAuthenticationService = null;
    private WeakReference<MainActivity> mMainActivityWeakReference = null;
    private TextView mTextViewBackendResult = null;
    private Button mBtnNavigateToMVVM = null;

    //#Interfaces
    interface IObjectsInitializer {
        ConstraintLayout onInitializingMainContainerConstraintLayout();
        EditText onInitializingUserNameEditText();
        EditText onInitializingUserPasswordEditText();
        Button onInitializingLoginButton();
        TextView onInitializingForBackendResultTextView();
        ProgressBar onInitializingWaitingForBackendToAuthenticateProgressBar();
        Button onInitializingNavigateToMVVMDataBindingButton();

    }

    interface IProgressBarVisibilitySwitch {
        void onProgressBarVisibilitySetToVisible();
        void onProgressBarVisibilitySetToGone();
    }

    interface ILoginButtonEnableState {
        void enableLoginButton();
        void disabledLoginButton();
    }

    interface ILoginButtonClicked {
        void clearUserNameEditText();
        void clearPasswordEditText();
        void clearBackendAuthenticationFeedbackTextView();
    }

    interface ITextViewVisibilitySwitch {
        void changeBackendFeedBackTextViewToVisible();
        void onTextViewVisibilitySetToGone();
    }

    interface INavigatetoMVVMDatabininBingButtonEnableStateChanged {
        void enableNavigateToMVVMDataBindingButton();
        void disableNavigateToMVVMDataBindingButton();
    }

    interface IUserCredentialsValidationService {
        void onUserCredentialsAreValid();
        void onUserNameIsInvalid();
        void onUserPasswordIsInvalid();
        void onUserNameIsNull();
        void onUserPasswordIsNull();
    }

    interface IAuthenticationResult {
        void onAuthenticationError(String resultMsg);
        void onAuthenticationSuccessful(String resultMsg);
        void onAuthenticationFailed(String resultMsg);
    }
    interface IConfigureAndStatesOfBackendAuthenticationService {
        void onConfigureIntentForStartingBackendAuthenticationServiceWith(ResultReceiver resultReceiver);
        void onStartBackendAuthenticationService();
        void onStopBackendAuthenticationService();
    }
    interface IOnNavigateToMVVMDataBindingButtonClicked {
        void configureIntentForStartingMVVMDataBindingV1Activity();
        void starMVVMDataBindingV1Activity();
    }

    //#Constructor
    LoginViewPresenter(Context ctx) {
        this.mMainActivityWeakReference = new WeakReference<>((MainActivity) (ctx));
    }

    //#implementation of Interfaces
    //#implemenattion of UserCredentialsValidationService.IBackendResponseUseronCredentialValidation
    @Override
    public void onBackendResponseUserCredentialsAreValid() {
        this.mMainActivityWeakReference.get().onUserCredentialsAreValid();
    }

    @Override
    public void onBackendResponseUserNameIsInvalid() {
        this.mMainActivityWeakReference.get().onUserNameIsInvalid();
    }

    @Override
    public void onBackendResponseUserPasswordIsInvalid() {
        this.mMainActivityWeakReference.get().onUserPasswordIsInvalid();
    }

    @Override
    public void onBackendResponseUserNameIsNull() {
        this.mMainActivityWeakReference.get().onUserNameIsNull();
    }

    @Override
    public void onBackendResponseUserPasswordIsNull() {
        this.mMainActivityWeakReference.get().onUserPasswordIsNull();
    }

    //#Methods
    public void validateUserCredentials(String userName, String userPassword) {
        UserCredentialsValidationService userCredentialsValidationService = new UserCredentialsValidationService(this, userName, userPassword);
        if (userCredentialsValidationService.getCredentialsValidityStateTo() == true) {
            //do connect to backend for authenticating users' credentials
            this.mMainActivityWeakReference.get().disabledLoginButton();
            this.mMainActivityWeakReference.get().clearUserNameEditText();
            this.mMainActivityWeakReference.get().clearPasswordEditText();
            this.mMainActivityWeakReference.get().clearBackendAuthenticationFeedbackTextView();
            this.mMainActivityWeakReference.get().disableNavigateToMVVMDataBindingButton();
            this.mMainActivityWeakReference.get().onConfigureIntentForStartingBackendAuthenticationServiceWith(new LoginViewPresenter.MyResultReceiver(new Handler()));
            this.mMainActivityWeakReference.get().onStartBackendAuthenticationService();
            this.mMainActivityWeakReference.get().onProgressBarVisibilitySetToVisible();
        }
    }


    public void ongNavigateToMVVMDataBindingButtonClicked() {
        //mMainActivityWeakReference.get().ongNavigateToMVVMDataBindingButtonClicked();
    }

    public void initializeObjects() {
        this.mMainContainer = this.mMainActivityWeakReference.get().onInitializingMainContainerConstraintLayout();
        this.mETUserName = this.mMainActivityWeakReference.get().onInitializingUserNameEditText();
        this.mETUserPassword = this.mMainActivityWeakReference.get().onInitializingUserPasswordEditText();
        this.mBtnLogin = this.mMainActivityWeakReference.get().onInitializingLoginButton();
        this.mTextViewBackendResult = this.mMainActivityWeakReference.get().onInitializingForBackendResultTextView();
        this.mProgressBarForLoginInProgress = this.mMainActivityWeakReference.get().onInitializingWaitingForBackendToAuthenticateProgressBar();
        this.mBtnNavigateToMVVM = this.mMainActivityWeakReference.get().onInitializingNavigateToMVVMDataBindingButton();
    }

    private void onAuthenticationError(String errorMsg) {
        mMainActivityWeakReference.get().onStopBackendAuthenticationService();
        mMainActivityWeakReference.get().onProgressBarVisibilitySetToGone();
        this.mMainActivityWeakReference.get().enableLoginButton();
        mMainActivityWeakReference.get().changeBackendFeedBackTextViewToVisible();
        mMainActivityWeakReference.get().onAuthenticationError(errorMsg);
        mMainActivityWeakReference.get().enableNavigateToMVVMDataBindingButton();
    }

    private void onAuthenticationSuccessful(String resultMsg) {
        mMainActivityWeakReference.get().onStopBackendAuthenticationService();
        mMainActivityWeakReference.get().onProgressBarVisibilitySetToGone();
        this.mMainActivityWeakReference.get().enableLoginButton();
        mMainActivityWeakReference.get().changeBackendFeedBackTextViewToVisible();
        mMainActivityWeakReference.get().onAuthenticationSuccessful(resultMsg);
        mMainActivityWeakReference.get().enableNavigateToMVVMDataBindingButton();
    }

    private void onAuthenticationFailed(String failureMsg) {
        mMainActivityWeakReference.get().onStopBackendAuthenticationService();
        mMainActivityWeakReference.get().onProgressBarVisibilitySetToGone();
        this.mMainActivityWeakReference.get().enableLoginButton();
        mMainActivityWeakReference.get().changeBackendFeedBackTextViewToVisible();
        mMainActivityWeakReference.get().onAuthenticationFailed(failureMsg);
        mMainActivityWeakReference.get().enableNavigateToMVVMDataBindingButton();
    }

    //#Lifecycle callbacks
    public void onCreate() {
        initializeObjects();
    }

    public void onStart() {

    }

    public void onRestart() {

    }

    public void onResume() {
    }

    public void onPause() {

    }

    public void onStop() {

    }

    public void onDestroy() {

    }

    //#private classes
    private class MyResultReceiver extends ResultReceiver {

        private final String TAG = MyResultReceiver.class.getSimpleName();

        public MyResultReceiver(Handler handler) {
            super(handler);
        }

        @Override
        public void send(int resultCode, Bundle resultData) {
            super.send(resultCode, resultData);
            Log.d(LoginViewPresenter.TAG + "." + TAG, "send");
        }

        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {
            super.onReceiveResult(resultCode, resultData);
            Log.d(LoginViewPresenter.TAG + "." + TAG, "onReceiveResult");
            String backendToPresenterSentResult = resultData.getString(BackendAuthenticationService.BUNDLE_KEY_RESULT_RECEIVER_TO_SEND_AUTHENTICATION_RESULT_TO_LOGIN_VIEW_PRESENTER);

            mMainActivityWeakReference.get().onProgressBarVisibilitySetToGone();
            switch (resultCode) {
                case Activity.RESULT_OK:
                    switch (backendToPresenterSentResult) {
                        case BackendAuthenticationService.BUNDLE_VALUE_SEND_RESULT_CODE_ON_AUTHENTICATION_RESULT_IS_ERROR:
                            Log.d(LoginViewPresenter.TAG + "." + TAG, "AUTHENTICATION_RESULT_IS_ERROR. RESULT_OK " + BackendAuthenticationService.BUNDLE_VALUE_SEND_RESULT_CODE_ON_AUTHENTICATION_RESULT_IS_ERROR);
                            onAuthenticationError("AUTHENTICATION_RESULT_IS_ERROR. Connection terminated unexpectedly. Try again.");
                            break;
                        case BackendAuthenticationService.BUNDLE_VALUE_SEND_RESULT_CODE_ON_AUTHENTICATION_RESULT_IS_SUCCESSFUL:
                            Log.d(LoginViewPresenter.TAG + "." + TAG, "AUTHENTICATION_RESULT_IS_SUCCESSFUL. RESULT_OK " + BackendAuthenticationService.BUNDLE_VALUE_SEND_RESULT_CODE_ON_AUTHENTICATION_RESULT_IS_SUCCESSFUL);
                            onAuthenticationSuccessful("AUTHENTICATION_RESULT_IS_SUCCESSFUL. Authentication_Successful.");
                            break;
                        case BackendAuthenticationService.BUNDLE_VALUE_SEND_RESULT_CODE_ON_AUTHENTICATION_RESULT_IS_FAILED:
                            Log.d(LoginViewPresenter.TAG + "." + TAG, "AUTHENTICATION_RESULT_IS_FAILED. RESULT_OK " + BackendAuthenticationService.BUNDLE_VALUE_SEND_RESULT_CODE_ON_AUTHENTICATION_RESULT_IS_FAILED);
                            onAuthenticationFailed("AUTHENTICATION_RESULT_IS_FAILED. User can not be authenticated. Invalid user credentials provided.");
                            break;
                    }
                    break;

                case Activity.RESULT_CANCELED:
                    break;
            }
        }

    }

}
