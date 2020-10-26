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

import java.lang.ref.WeakReference;

import androidx.constraintlayout.widget.ConstraintLayout;
import services.BackendService;

public class LoginViewPresenter implements UserCredentialsValidationService.IBackendResponseUseronCredentialValidation {

    //#public final static variables
    private final static String TAG = LoginViewPresenter.class.getSimpleName();
    public final static String INTENT_KEY_AUTHENTICATION_PROCESS_STATE = "authentication process state";
    public final static String INTENT_VALUE_REQUEST_START_AUTHENTICATING_USER = "start authentication process in the backend";

    //#member variables
    protected EditText mETUserName = null;
    protected EditText mETUserPassword = null;
    protected Button mBtnLogin = null;
    protected ProgressBar mProgressBarForLoginInProgress = null;
    protected ConstraintLayout mMainContainer = null;
    protected UserCredentialsValidationService mUserCredentialsValidationService = null;
    private WeakReference<MainActivity> mMainActivityWeakReference = null;

    //#Interfaces
    interface IObjectsInitializer {
        ConstraintLayout onInitializingMainContainer();
        EditText onInitializingETUserName();
        EditText onInitializingETUserPassword();
        Button onInitializingButtonLogin();
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
        void onAuthenticationError();
        void onAuthenticationSuccessful();
        void onAuthenticationFailed();
        void onAuthenticationUnknown();
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
            this.startBackendAuthenticationService();
            mMainActivityWeakReference.get().onProgressBarVisibilitySetToVisible();
        }
    }

    private void startBackendAuthenticationService() {
        Intent intent = new Intent(this.mMainActivityWeakReference.get(), BackendService.class);
        intent.putExtra(INTENT_KEY_AUTHENTICATION_PROCESS_STATE, INTENT_VALUE_REQUEST_START_AUTHENTICATING_USER);
        intent.putExtra(BackendService.INTENT_KEY_FOR_RESULT_RECEIVER, new MyResultReceiver(new Handler()));
        this.mMainActivityWeakReference.get().startService(intent);
    }

    private void hideProgressBar() {
        this.mMainActivityWeakReference.get().onProgressBarVisibilitySetToGone();
    }

    private void showProgressBar() {
        this.mMainActivityWeakReference.get().onProgressBarVisibilitySetToVisible();
    }

    public void initializeObjects() {
        this.mMainContainer = this.mMainActivityWeakReference.get().onInitializingMainContainer();
        this.mETUserName = this.mMainActivityWeakReference.get().onInitializingETUserName();
        this.mETUserPassword = this.mMainActivityWeakReference.get().onInitializingETUserPassword();
        this.mBtnLogin = this.mMainActivityWeakReference.get().onInitializingButtonLogin();
        this.mProgressBarForLoginInProgress = this.mMainActivityWeakReference.get().onInitializingProgressBar();
    }

    private void onAuthenticationError() {
        mMainActivityWeakReference.get().onProgressBarVisibilitySetToGone();
        mMainActivityWeakReference.get().onAuthenticationError();
    }

    private void onAuthenticationSuccessful() {
        mMainActivityWeakReference.get().onProgressBarVisibilitySetToGone();
        mMainActivityWeakReference.get().onAuthenticationSuccessful();
    }

    private void onAuthenticationFailed() {
        mMainActivityWeakReference.get().onProgressBarVisibilitySetToGone();
        mMainActivityWeakReference.get().onAuthenticationFailed();
    }

    private void onAuthenticationUnknown() {
        mMainActivityWeakReference.get().onProgressBarVisibilitySetToGone();
        mMainActivityWeakReference.get().onAuthenticationUnknown();
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
            String backendToPresenterSentResult = resultData.getString(BackendService.BUNDLE_KEY_ON_RESULT_RECEIVER_SEND);

            switch (resultCode) {
                case Activity.RESULT_OK:
                    switch (backendToPresenterSentResult) {
                        case BackendService.BUNDLE_VALUE_ON_RESULT_RECEIVER_SEND_ON_AUTHENTICATION_ERROR:
                            Log.d(LoginViewPresenter.TAG + "." + TAG, "RESULT_OK " + BackendService.BUNDLE_VALUE_ON_RESULT_RECEIVER_SEND_ON_AUTHENTICATION_ERROR);
                            onAuthenticationError();
                            break;
                        case BackendService.BUNDLE_VALUE_ON_RESULT_RECEIVER_SEND_ON_AUTHENTICATION_SUCCESSFUL:
                            Log.d(LoginViewPresenter.TAG + "." + TAG, "RESULT_OK " + BackendService.BUNDLE_VALUE_ON_RESULT_RECEIVER_SEND_ON_AUTHENTICATION_SUCCESSFUL);
                            onAuthenticationSuccessful();
                            break;
                        case BackendService.BUNDLE_VALUE_ON_RESULT_RECEIVER_SEND_ON_AUTHENTICATION_FAILED:
                            Log.d(LoginViewPresenter.TAG + "." + TAG, "RESULT_OK " + BackendService.BUNDLE_VALUE_ON_RESULT_RECEIVER_SEND_ON_AUTHENTICATION_FAILED);
                            onAuthenticationFailed();
                            break;
                        /*case BackendService.BUNDLE_VALUE_ON_RESULT_RECEIVER_SEND_ON_AUTHENTICATION_UNKNOWN:
                            Log.d(LoginViewPresenter.TAG + "." + TAG, "RESULT_OK " + BackendService.BUNDLE_VALUE_ON_RESULT_RECEIVER_SEND_ON_AUTHENTICATION_UNKNOWN);
                            onAuthenticationUnknown();
                            break;*/
                    }
                    break;

                case Activity.RESULT_CANCELED:
                    break;
            }
        }

    }

}
