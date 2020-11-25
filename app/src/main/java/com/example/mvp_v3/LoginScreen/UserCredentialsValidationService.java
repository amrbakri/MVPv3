package com.example.mvp_v3.LoginScreen;

public class UserCredentialsValidationService {

    private LoginViewPresenter mLoginViewPresenter = null;
    private String mUserName = null;
    private String mUserPassword = null;
    private boolean mCredentialsValidityState = false;

    interface IBackendResponseUseronCredentialValidation {
        void onBackendResponseUserCredentialsAreValid();
        void onBackendResponseUserNameIsInvalid();
        void onBackendResponseUserPasswordIsInvalid();
        void onBackendResponseUserNameIsNull();
        void onBackendResponseUserPasswordIsNull();
    }

    public UserCredentialsValidationService(LoginViewPresenter loginViewPresenter, String userName, String userPassword) {
        this.mLoginViewPresenter = loginViewPresenter;
        this.mUserName = userName;
        this.mUserPassword = userPassword;

        if (this.mUserName != null) {
            if (this.mUserPassword != null) {
                if (this.mUserName.trim().equals("") && this.mUserPassword.trim().equals("")) {
                    this.mLoginViewPresenter.onBackendResponseUserNameIsInvalid();
                    this.mLoginViewPresenter.onBackendResponseUserPasswordIsInvalid();
                    setCredentialsValidityStateTo(false);
                } else if (this.mUserName.trim().equals("") && !this.mUserPassword.trim().equals("")) {
                    this.mLoginViewPresenter.onBackendResponseUserNameIsInvalid();
                    setCredentialsValidityStateTo(false);
                } else if (!this.mUserName.trim().equals("") && this.mUserPassword.trim().equals("")) {
                    this.mLoginViewPresenter.onBackendResponseUserPasswordIsInvalid();
                    setCredentialsValidityStateTo(false);
                } else {
                    this.mLoginViewPresenter.onBackendResponseUserCredentialsAreValid();
                    setCredentialsValidityStateTo(true);
                }
            } else {
                this.mLoginViewPresenter.onBackendResponseUserPasswordIsNull();
                setCredentialsValidityStateTo(false);
            }
        } else {
            this.mLoginViewPresenter.onBackendResponseUserNameIsNull();
            setCredentialsValidityStateTo(false);
        }
    }

    public void setCredentialsValidityStateTo(boolean state) {
        this.mCredentialsValidityState = state;
    }

    public boolean getCredentialsValidityStateTo() {
        return this.mCredentialsValidityState;
    }
}
