package com.example.mvp_v3;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;


public class MainActivity extends AppCompatActivity implements LoginViewPresenter.IObjectsInitializer,
                                                               LoginViewPresenter.IProgressBarVisibilitySwitch,
                                                               LoginViewPresenter.IUserCredentialsValidationService,
                                                               LoginViewPresenter.IAuthenticationResult
{
    //member variables
    protected LoginViewPresenter mLoginViewPresenter = null;
    protected EditText mETUserName = null;
    protected EditText mETUserPassword = null;
    protected Button mBtnLogin = null;
    protected ProgressBar mProgressBarLoginInProgress = null;
    protected ConstraintLayout mMainContainer = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mLoginViewPresenter = new LoginViewPresenter(this);
        mLoginViewPresenter.onCreate();
    }

    //#member methods
    private void deactivateLoginButton() {
        this.mBtnLogin.setEnabled(false);
    }
    private void activateLoginButton() {
        this.mBtnLogin.setEnabled(true);
    }

    //#Implementation of Interfaces

    //#implementation of LoginViewPresenter.IObjectsInitializer
    public ConstraintLayout onInitializingMainContainer() {
        return this.mMainContainer = findViewById(R.id.constraintLayout);
    }

    public EditText onInitializingETUserName() {
        return this.mETUserName = findViewById(R.id.etUserName);
    }

    public EditText onInitializingETUserPassword() {
         return this.mETUserPassword = findViewById(R.id.etUserPassword);
    }

    public Button onInitializingButtonLogin() {
         return this.mBtnLogin = findViewById(R.id.btnLogin);
    }

    public ProgressBar onInitializingProgressBar() {
         return this.mProgressBarLoginInProgress = findViewById(R.id.progressBarLoginInProgress);
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
    //#implementation of LoginViewPresenter.IUserCredentialsValidationService

    public void onUserCredentialsAreValid() {
        Toast.makeText(this, "User credentials entered are valid and matching the template.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onUserNameIsInvalid() {
        Toast.makeText(this, "User name entered is not valid", Toast.LENGTH_SHORT).show();
        if (!this.mBtnLogin.isEnabled()){
            this.mBtnLogin.setEnabled(true);
        }
    }

    @Override
    public void onUserPasswordIsInvalid() {
        Toast.makeText(this, "User password entered is not valid", Toast.LENGTH_SHORT).show();
        if (!this.mBtnLogin.isEnabled()){
            this.mBtnLogin.setEnabled(true);
        }
    }

    @Override
    public void onUserNameIsNull() {
        Toast.makeText(this, "User name member variable is null", Toast.LENGTH_SHORT).show();
        if (!this.mBtnLogin.isEnabled()){
            this.mBtnLogin.setEnabled(true);
        }
    }

    @Override
    public void onUserPasswordIsNull() {
        Toast.makeText(this, "User password member variable is null", Toast.LENGTH_SHORT).show();
        if (!this.mBtnLogin.isEnabled()){
            this.mBtnLogin.setEnabled(true);
        }
    }


    //#implementation of LoginViewPresenter.IAuthenticationResult
    @Override
    public void onAuthenticationError() {
        if(!mBtnLogin.isEnabled()) {
            activateLoginButton();
        }
    }

    @Override
    public void onAuthenticationSuccessful() {
        if(!mBtnLogin.isEnabled()) {
            activateLoginButton();
        }
    }

    @Override
    public void onAuthenticationFailed() {
        if(!mBtnLogin.isEnabled()) {
            activateLoginButton();
        }
    }

    @Override
    public void onAuthenticationUnknown() {
        if(!mBtnLogin.isEnabled()) {
            activateLoginButton();
        }
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
                deactivateLoginButton();
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
}
