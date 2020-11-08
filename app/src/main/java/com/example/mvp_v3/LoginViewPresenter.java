package com.example.mvp_v3;

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

    //#Interfaces
    interface IObjectsInitializer {
        ConstraintLayout onInitializingMainContainer();
        EditText onInitializingETUserName();
        EditText onInitializingETUserPassword();
        Button onInitializingButtonLogin();
        TextView onInitializingTextViewBackendResult();
        ProgressBar onInitializingProgressBar();
    }

    interface IProgressBarVisibilitySwitch {
        void onProgressBarVisibilitySetToVisible();
        void onProgressBarVisibilitySetToGone();
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
            this.mMainActivityWeakReference.get().onConfigureIntentForStartingBackendAuthenticationServiceWith(new LoginViewPresenter.MyResultReceiver(new Handler()));
            this.mMainActivityWeakReference.get().onStartBackendAuthenticationService();
            mMainActivityWeakReference.get().onProgressBarVisibilitySetToVisible();
        }
    }

    public void initializeObjects() {
        this.mMainContainer = this.mMainActivityWeakReference.get().onInitializingMainContainer();
        this.mETUserName = this.mMainActivityWeakReference.get().onInitializingETUserName();
        this.mETUserPassword = this.mMainActivityWeakReference.get().onInitializingETUserPassword();
        this.mBtnLogin = this.mMainActivityWeakReference.get().onInitializingButtonLogin();
        this.mTextViewBackendResult = this.mMainActivityWeakReference.get().onInitializingTextViewBackendResult();
        this.mProgressBarForLoginInProgress = this.mMainActivityWeakReference.get().onInitializingProgressBar();
    }

    private void onAuthenticationError(String errorMsg) {
        mMainActivityWeakReference.get().onProgressBarVisibilitySetToGone();
        mMainActivityWeakReference.get().onAuthenticationError(errorMsg);
        mMainActivityWeakReference.get().onStopBackendAuthenticationService();
    }

    private void onAuthenticationSuccessful(String resultMsg) {
        mMainActivityWeakReference.get().onProgressBarVisibilitySetToGone();
        mMainActivityWeakReference.get().onAuthenticationSuccessful(resultMsg);
        mMainActivityWeakReference.get().onStopBackendAuthenticationService();
    }

    private void onAuthenticationFailed(String failureMsg) {
        mMainActivityWeakReference.get().onProgressBarVisibilitySetToGone();
        mMainActivityWeakReference.get().onAuthenticationFailed(failureMsg);
        mMainActivityWeakReference.get().onStopBackendAuthenticationService();
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
            String backendToPresenterSentResult = resultData.getString(BackendAuthenticationService.BUNDLE_KEY_ON_RESULT_RECEIVER_SEND);

            mMainActivityWeakReference.get().onProgressBarVisibilitySetToGone();
            switch (resultCode) {
                case Activity.RESULT_OK:
                    switch (backendToPresenterSentResult) {
                        case BackendAuthenticationService.BUNDLE_VALUE_ON_RESULT_RECEIVER_SEND_ON_AUTHENTICATION_ERROR:
                            Log.d(LoginViewPresenter.TAG + "." + TAG, "RESULT_OK " + BackendAuthenticationService.BUNDLE_VALUE_ON_RESULT_RECEIVER_SEND_ON_AUTHENTICATION_ERROR);
                            onAuthenticationError("Connection terminated unexpectedly. Try again.");
                            break;
                        case BackendAuthenticationService.BUNDLE_VALUE_ON_RESULT_RECEIVER_SEND_ON_AUTHENTICATION_SUCCESSFUL:
                            Log.d(LoginViewPresenter.TAG + "." + TAG, "RESULT_OK " + BackendAuthenticationService.BUNDLE_VALUE_ON_RESULT_RECEIVER_SEND_ON_AUTHENTICATION_SUCCESSFUL);
                            onAuthenticationSuccessful("Authentication_Successful.");
                            break;
                        case BackendAuthenticationService.BUNDLE_VALUE_ON_RESULT_RECEIVER_SEND_ON_AUTHENTICATION_FAILED:
                            Log.d(LoginViewPresenter.TAG + "." + TAG, "RESULT_OK " + BackendAuthenticationService.BUNDLE_VALUE_ON_RESULT_RECEIVER_SEND_ON_AUTHENTICATION_FAILED);
                            onAuthenticationFailed("User can not be authenticated. Invalid user credentials provided.");
                            break;
                    }
                    break;

                case Activity.RESULT_CANCELED:
                    break;
            }
        }

    }

}
