package services;

import android.app.Activity;
import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.ResultReceiver;
import android.util.Log;

import com.example.mvp_v3.loginScreen.MainActivity;

import java.lang.ref.WeakReference;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;
import enums.AuthenticationResult;

public class BackendAuthenticationService extends IntentService {

    //#static final variable
    private final static String TAG = BackendAuthenticationService.class.getSimpleName();
    public static final String INTENT_KEY_FOR_RESULT_RECEIVER = "result receiver listener";
    private static final String BUNDLE_KEY_BACKEND_TO_FRONTEND_DECLARATION_OF_AUTHENTICATION_RESULT = "on authentication result issued";

    private static final String BUNDLE_KEY_FRONTEND_TO_BACKEND_ACKNOWLEDGMENT_OF_RECEIPT_OF_AUTHENTICATION_RESULT = "frontend acknowledge of receipt of results from backend";
    private static final String BUNDLE_KEY_BACKEND_TO_FRONTEND_POST_RECEIPT_ACKNOWLEDGMENT_FROM_FRONTEND = "frontend reply to backend on authentication result issued";
    private static final String ACKNOWLEDGEMENT_OF_AUTHENTICATION_RESULT_IS_SUCCESSFUL = "onSuccessful";
    private static final String ACKNOWLEDGEMENT_OF_AUTHENTICATION_RESULT_IS_FAILED = "onFailed";
    private static final String ACKNOWLEDGEMENT_OF_AUTHENTICATION_RESULT_IS_ERROR = "onError";

    public static final String BUNDLE_KEY_RESULT_RECEIVER_TO_SEND_AUTHENTICATION_RESULT_TO_LOGIN_VIEW_PRESENTER = "onResultReceiver sends";
    public static final String BUNDLE_VALUE_SEND_RESULT_CODE_ON_AUTHENTICATION_RESULT_IS_ERROR = "onResultReceiver sends call onError";
    public static final String BUNDLE_VALUE_SEND_RESULT_CODE_ON_AUTHENTICATION_RESULT_IS_SUCCESSFUL = "onResultReceiver sends call onSuccessful";
    public static final String BUNDLE_VALUE_SEND_RESULT_CODE_ON_AUTHENTICATION_RESULT_IS_FAILED = "onResultReceiver sends call onFailed";
    public static final String BUNDLE_VALUE_ON_RESULT_RECEIVER_SEND_ON_RESULT_CANCELED = "onResultReceiver sends RESULT_CANCELED";

    private static final String BUNDLE_VALUE_ON_AUTHENTICATION_ERROR = "presenter to call onError";
    private static final String BUNDLE_VALUE_ON_AUTHENTICATION_SUCCESSFUL = "presenter to call onSuccessful";
    private static final String BUNDLE_VALUE_ON_AUTHENTICATION_FAILED = "presenter to call onFailed";

    private static final String QUIT = "quit consumer handler";
    private static final String QUIT_SAFELY = "quit consumer handler safely";

    private static final int AUTHENTICATION_RESULT_IS_ERROR = 0;
    private static final int AUTHENTICATION_RESULT_IS_SUCCESSFUL = 1;
    private static final int AUTHENTICATION_RESULT_IS_FAILED = 2;

    //#member variables
    private BackendHandlerThread mBackendHandlerThread = null;
    private Handler mFrontendHandler = null;
    private ResultReceiver mResultReceiverListener = null;

    //#constructors
    //default constructor
    public BackendAuthenticationService() {
        super("");
    }

    public BackendAuthenticationService(String name) {
        super(name);
    }

    //#lifecycle callbacks
    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "onCreate");
        this.mFrontendHandler = new Handler(this.getMainLooper(), new MyFrontendHandlerCallback(this));
        this.mBackendHandlerThread = new BackendHandlerThread("Login-Thread-1");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.i(TAG, "onHandleIntent");
        this.mBackendHandlerThread.start();
        this.mBackendHandlerThread.initializeHandler();
        Bundle extras = intent.getExtras();
        String token = extras.getString(MainActivity.INTENT_KEY_START_BACKEND_SERVICE_FOR_AUTHENTICATION_PROCESS);
        mResultReceiverListener = intent.getParcelableExtra(BackendAuthenticationService.INTENT_KEY_FOR_RESULT_RECEIVER);
        this.mBackendHandlerThread.enqueueMessage(token);
    }

    //#private classes
    private class BackendHandlerThread extends HandlerThread {

        private final String TAG = BackendHandlerThread.class.getSimpleName();
        private MyBackendHandler mMyBackendHandler = null;

        public BackendHandlerThread(String name) {
            super(name);
        }

        public void initializeHandler() {
            while (this.getLooper() == null) {
                onLooperPrepared();
            }
            this.mMyBackendHandler = new MyBackendHandler(this.getLooper());
        }

        public MyBackendHandler getInstanceOfConsumerHandler() {
            return this.mMyBackendHandler;
        }

        @Override
        public Looper getLooper() {
            return super.getLooper();
        }

        @Override
        protected void onLooperPrepared() {
            super.onLooperPrepared();
        }

        protected void enqueueMessage(String token) {
            //#Messages creation
            Message msg = Message.obtain();
            msg.obj = token;
            msg.setTarget(mMyBackendHandler);
            this.mMyBackendHandler.sendMessage(msg);
        }

        @Override
        public void run() {
            Log.i(BackendAuthenticationService.TAG + "." + TAG, "run");
            super.run();
        }

        @Override
        public boolean quit() {
            Log.i(BackendAuthenticationService.TAG + "." + TAG, "quitted");
            return super.quit();
        }

        @Override
        public boolean quitSafely() {
            Log.i(BackendAuthenticationService.TAG + "." + TAG, "quitedSafely");
            return super.quitSafely();
        }

        private class MyBackendHandler extends Handler {

            //#private final static variables
            private final String TAG = MyBackendHandler.class.getSimpleName();

            //#constructor
            public MyBackendHandler(Looper looper) {
                super(looper);
            }

            @Override
            public void handleMessage(@NonNull Message message) {
                String op = (String) message.obj;
                Message msg = new Message();
                msg.setTarget(mFrontendHandler);
                Bundle bundle = new Bundle();
                int authenticationResult;
                Random random = new Random();

                switch (op) {
                    case MainActivity.INTENT_VALUE_COMMENCE_BACKEND_SERVICE_FOR_AUTHENTICATING_PROCESS:
                        int authenticationElapsedTime = random.nextInt(4) + 2;
                        for (int i = 0; i < authenticationElapsedTime; i++) {
                            try {
                                TimeUnit.SECONDS.sleep(1);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            Log.d(BackendAuthenticationService.TAG + "." + this.TAG, "waiting for authenticating user: " + (i + 1) + " seconds");
                        }
                        //Log.d(BackendAuthenticationService.TAG + "." + this.TAG, "authentication process took: " + authenticationElapsedTime + " seconds");

                        authenticationResult = random.nextInt(3);
                        switch (authenticationResult) {
                            case AUTHENTICATION_RESULT_IS_ERROR:
                                Log.i(BackendAuthenticationService.TAG + "." + this.TAG, "authentication process result: " + AuthenticationResult.ERROR.bytName());
                                bundle.putInt(BackendAuthenticationService.BUNDLE_KEY_BACKEND_TO_FRONTEND_DECLARATION_OF_AUTHENTICATION_RESULT, AuthenticationResult.ERROR.byNum());
                                break;
                            case AUTHENTICATION_RESULT_IS_SUCCESSFUL:
                                Log.i(BackendAuthenticationService.TAG + "." + this.TAG, "authentication process result: " + AuthenticationResult.SUCCESSFUL.bytName());
                                bundle.putInt(BackendAuthenticationService.BUNDLE_KEY_BACKEND_TO_FRONTEND_DECLARATION_OF_AUTHENTICATION_RESULT, AuthenticationResult.SUCCESSFUL.byNum());
                                break;
                            case AUTHENTICATION_RESULT_IS_FAILED:
                                Log.i(BackendAuthenticationService.TAG + "." + this.TAG, "authentication process result: " + AuthenticationResult.FAILED.bytName());
                                bundle.putInt(BackendAuthenticationService.BUNDLE_KEY_BACKEND_TO_FRONTEND_DECLARATION_OF_AUTHENTICATION_RESULT, AuthenticationResult.FAILED.byNum());
                                break;
                            default:
                                Log.e(BackendAuthenticationService.TAG + "." + this.TAG, "UNHANDLED_CASE. Backend service can deliver back results either " +
                                        "Successful, Failed or Error. Backend service will be quite-safely");
                                quitSafely();

                                break;
                        }
                        msg.setData(bundle);
                        msg.sendToTarget();
                        break;

                    case BackendAuthenticationService.ACKNOWLEDGEMENT_OF_AUTHENTICATION_RESULT_IS_ERROR:
                        Log.w(BackendAuthenticationService.TAG + "." + this.TAG, "call" + " " + BackendAuthenticationService.ACKNOWLEDGEMENT_OF_AUTHENTICATION_RESULT_IS_ERROR);
                        bundle.putString(BackendAuthenticationService.BUNDLE_KEY_BACKEND_TO_FRONTEND_POST_RECEIPT_ACKNOWLEDGMENT_FROM_FRONTEND,
                                BackendAuthenticationService.BUNDLE_VALUE_ON_AUTHENTICATION_ERROR);
                        msg.setData(bundle);
                        msg.sendToTarget();
                        break;

                    case BackendAuthenticationService.ACKNOWLEDGEMENT_OF_AUTHENTICATION_RESULT_IS_SUCCESSFUL:
                        Log.w(BackendAuthenticationService.TAG + "." + this.TAG, "call" + " " + BackendAuthenticationService.ACKNOWLEDGEMENT_OF_AUTHENTICATION_RESULT_IS_SUCCESSFUL);
                        bundle.putString(BackendAuthenticationService.BUNDLE_KEY_BACKEND_TO_FRONTEND_POST_RECEIPT_ACKNOWLEDGMENT_FROM_FRONTEND,
                                BackendAuthenticationService.BUNDLE_VALUE_ON_AUTHENTICATION_SUCCESSFUL);
                        msg.setData(bundle);
                        msg.sendToTarget();
                        break;

                    case BackendAuthenticationService.ACKNOWLEDGEMENT_OF_AUTHENTICATION_RESULT_IS_FAILED:
                        Log.w(BackendAuthenticationService.TAG + "." + this.TAG, "call" + " " + ACKNOWLEDGEMENT_OF_AUTHENTICATION_RESULT_IS_FAILED);
                        bundle.putString(BackendAuthenticationService.BUNDLE_KEY_BACKEND_TO_FRONTEND_POST_RECEIPT_ACKNOWLEDGMENT_FROM_FRONTEND,
                                BackendAuthenticationService.BUNDLE_VALUE_ON_AUTHENTICATION_FAILED);
                        msg.setData(bundle);
                        msg.sendToTarget();
                        break;

                    case BackendAuthenticationService.QUIT:
                        quit();
                        break;

                    case BackendAuthenticationService.QUIT_SAFELY:
                        quitSafely();
                        break;
                }
                super.handleMessage(message);
            }

            @Override
            public void dispatchMessage(@NonNull Message msg) {
                Log.v(BackendAuthenticationService.TAG + "." + this.TAG, "dispatchMessage: ");
                super.dispatchMessage(msg);
            }

            @NonNull
            @Override
            public String getMessageName(@NonNull Message message) {
                Log.v(BackendAuthenticationService.TAG + "." + this.TAG, "super.getMessageName(message): " + super.getMessageName(message));
                return super.getMessageName(message);
            }
        }
    }

    private class MyFrontendHandlerCallback implements Handler.Callback {

        //#private final static variables
        private final String TAG = MyFrontendHandlerCallback.class.getSimpleName();

        private WeakReference<BackendAuthenticationService> mBackendServiceWeakReference = null;

        MyFrontendHandlerCallback(BackendAuthenticationService backendServiceContext) {
            this.mBackendServiceWeakReference = new WeakReference<>(backendServiceContext);
        }

        @Override
        public boolean handleMessage(@NonNull Message message) {
            Bundle data = message.getData();
            boolean op1 = data.containsKey(BackendAuthenticationService.BUNDLE_KEY_BACKEND_TO_FRONTEND_DECLARATION_OF_AUTHENTICATION_RESULT);
            boolean op2 = data.containsKey(BackendAuthenticationService.BUNDLE_KEY_BACKEND_TO_FRONTEND_POST_RECEIPT_ACKNOWLEDGMENT_FROM_FRONTEND);
            Message msg = new Message();
            msg.setTarget(mBackendHandlerThread.getInstanceOfConsumerHandler());
            Bundle bundle = new Bundle();

            if (op1) {
                int backendAuthenticationResult = data.getInt(BackendAuthenticationService.BUNDLE_KEY_BACKEND_TO_FRONTEND_DECLARATION_OF_AUTHENTICATION_RESULT);
                switch (backendAuthenticationResult) {
                    case AUTHENTICATION_RESULT_IS_ERROR:
                        Log.i(BackendAuthenticationService.TAG + "." + this.TAG, "AUTHENTICATION ERROR");
                        msg.obj = BackendAuthenticationService.ACKNOWLEDGEMENT_OF_AUTHENTICATION_RESULT_IS_ERROR;
                        bundle.putString(BackendAuthenticationService.BUNDLE_KEY_FRONTEND_TO_BACKEND_ACKNOWLEDGMENT_OF_RECEIPT_OF_AUTHENTICATION_RESULT,
                                BackendAuthenticationService.ACKNOWLEDGEMENT_OF_AUTHENTICATION_RESULT_IS_ERROR);
                        break;

                    case AUTHENTICATION_RESULT_IS_SUCCESSFUL:
                        Log.i(BackendAuthenticationService.TAG + "." + this.TAG, "AUTHENTICATION SUCCESSFUL");
                        msg.obj = BackendAuthenticationService.ACKNOWLEDGEMENT_OF_AUTHENTICATION_RESULT_IS_SUCCESSFUL;
                        bundle.putString(BackendAuthenticationService.BUNDLE_KEY_FRONTEND_TO_BACKEND_ACKNOWLEDGMENT_OF_RECEIPT_OF_AUTHENTICATION_RESULT,
                                BackendAuthenticationService.ACKNOWLEDGEMENT_OF_AUTHENTICATION_RESULT_IS_SUCCESSFUL);
                        break;

                    case AUTHENTICATION_RESULT_IS_FAILED:
                        Log.i(BackendAuthenticationService.TAG + "." + this.TAG, "AUTHENTICATION FAILED");
                        msg.obj = BackendAuthenticationService.ACKNOWLEDGEMENT_OF_AUTHENTICATION_RESULT_IS_FAILED;
                        bundle.putString(BackendAuthenticationService.BUNDLE_KEY_FRONTEND_TO_BACKEND_ACKNOWLEDGMENT_OF_RECEIPT_OF_AUTHENTICATION_RESULT,
                                BackendAuthenticationService.ACKNOWLEDGEMENT_OF_AUTHENTICATION_RESULT_IS_FAILED);
                        break;

                    default:
                }
            } else if (op2) {
                String res = data.getString(BackendAuthenticationService.BUNDLE_KEY_BACKEND_TO_FRONTEND_POST_RECEIPT_ACKNOWLEDGMENT_FROM_FRONTEND);
                Bundle bundleResRec = null;
                switch (res) {
                    case BackendAuthenticationService.BUNDLE_VALUE_ON_AUTHENTICATION_ERROR:
                        Log.i(BackendAuthenticationService.TAG + "." + this.TAG, BUNDLE_VALUE_ON_AUTHENTICATION_ERROR);
                        bundleResRec = new Bundle();
                        bundleResRec.putString(BackendAuthenticationService.BUNDLE_KEY_RESULT_RECEIVER_TO_SEND_AUTHENTICATION_RESULT_TO_LOGIN_VIEW_PRESENTER,
                                BackendAuthenticationService.BUNDLE_VALUE_SEND_RESULT_CODE_ON_AUTHENTICATION_RESULT_IS_ERROR);
                        mResultReceiverListener.send(Activity.RESULT_OK, bundleResRec);
                        break;

                    case BackendAuthenticationService.BUNDLE_VALUE_ON_AUTHENTICATION_SUCCESSFUL:
                        Log.i(BackendAuthenticationService.TAG + "." + this.TAG, BUNDLE_VALUE_ON_AUTHENTICATION_SUCCESSFUL);
                        bundleResRec = new Bundle();
                        bundleResRec.putString(BackendAuthenticationService.BUNDLE_KEY_RESULT_RECEIVER_TO_SEND_AUTHENTICATION_RESULT_TO_LOGIN_VIEW_PRESENTER,
                                BackendAuthenticationService.BUNDLE_VALUE_SEND_RESULT_CODE_ON_AUTHENTICATION_RESULT_IS_SUCCESSFUL);
                        mResultReceiverListener.send(Activity.RESULT_OK, bundleResRec);
                        break;

                    case BackendAuthenticationService.BUNDLE_VALUE_ON_AUTHENTICATION_FAILED:
                        Log.i(BackendAuthenticationService.TAG + "." + this.TAG, BUNDLE_VALUE_ON_AUTHENTICATION_FAILED);
                        bundleResRec = new Bundle();
                        bundleResRec.putString(BackendAuthenticationService.BUNDLE_KEY_RESULT_RECEIVER_TO_SEND_AUTHENTICATION_RESULT_TO_LOGIN_VIEW_PRESENTER,
                                BackendAuthenticationService.BUNDLE_VALUE_SEND_RESULT_CODE_ON_AUTHENTICATION_RESULT_IS_FAILED);
                        mResultReceiverListener.send(Activity.RESULT_OK, bundleResRec);
                        break;

                    default:
                        bundleResRec = new Bundle();
                        bundleResRec.putString(BackendAuthenticationService.BUNDLE_KEY_RESULT_RECEIVER_TO_SEND_AUTHENTICATION_RESULT_TO_LOGIN_VIEW_PRESENTER,
                                BackendAuthenticationService.BUNDLE_VALUE_ON_RESULT_RECEIVER_SEND_ON_RESULT_CANCELED);
                        mResultReceiverListener.send(Activity.RESULT_CANCELED, bundleResRec);
                }
                msg.obj = BackendAuthenticationService.QUIT_SAFELY;
            } else {

            }
            msg.setData(bundle);
            msg.sendToTarget();
            return true;
        }
    }
}