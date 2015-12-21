package com.sdk.snippets.modules;

import android.content.Context;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;

import com.digits.sdk.android.AuthCallback;
import com.digits.sdk.android.Digits;
import com.digits.sdk.android.DigitsException;
import com.digits.sdk.android.DigitsOAuthSigning;
import com.digits.sdk.android.DigitsSession;
import com.quickblox.core.QBEntityCallbackImpl;
import com.quickblox.core.exception.BaseServiceException;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.core.helper.Lo;
import com.quickblox.core.server.BaseService;
import com.quickblox.auth.QBAuth;
import com.quickblox.auth.model.QBProvider;
import com.quickblox.auth.model.QBSession;
import com.quickblox.users.model.QBUser;
import com.sdk.snippets.core.ApplicationConfig;
import com.sdk.snippets.core.AsyncSnippet;
import com.sdk.snippets.core.Snippet;
import com.sdk.snippets.core.Snippets;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterAuthToken;
import com.twitter.sdk.android.core.TwitterCore;

import java.util.List;
import java.util.Map;

import io.fabric.sdk.android.Fabric;

/**
 * Created by vfite on 22.01.14.
 */
public class SnippetsAuth extends Snippets{

    private static final String TAG = SnippetsAuth.class.getSimpleName();

    private TwitterAuthConfig authConfig;

    public SnippetsAuth(Context context) {
        super(context);

        snippets.add(createSession);
        snippets.add(createSessionSynchronous);
        //
        snippets.add(createSessionWithUser);
        snippets.add(createSessionWithUserSynchronous);
        //
        snippets.add(createSessionWithSocialProvider);
        snippets.add(createSessionWithSocialProviderSynchronous);
        //
        snippets.add(createSessionWithTwitterDigits);
        snippets.add(createSessionWithTwitterDigitsSynchronous);
        //
        snippets.add(destroySession);
        snippets.add(destroySessionSynchronous);
        //
        snippets.add(getSession);
        snippets.add(getSessionSynchronous);
    }


    //
    /////////////////////////////////// Create session /////////////////////////////////////////////
    //


    Snippet createSession = new Snippet("create session") {
        @Override
        public void execute() {

            QBAuth.createSession(new QBEntityCallbackImpl<QBSession>() {

                @Override
                public void onSuccess(QBSession session, Bundle params) {
                    Log.i(TAG, "session created, token = " + session.getToken());
                }

                @Override
                public void onError(List<String> errors) {
                    Log.i(TAG, "session creation, error = " + errors);
                    handleErrors(errors);
                }
            });
        }
    };

    Snippet createSessionSynchronous = new AsyncSnippet("create session (synchronous)", context) {
        @Override
        public void executeAsync() {
            QBSession session = null;
            try {
                session = QBAuth.createSession();
            } catch (QBResponseException e) {
                setException(e);
            }

            if(session != null){
                Log.i(TAG, "session created, token = " + session.getToken());
            }
        }
    };


    //
    /////////////////////////////// Create session with user ///////////////////////////////////////
    //


    Snippet createSessionWithUser = new Snippet("create session", "with user") {
        @Override
        public void execute() {

            QBAuth.createSession(new QBUser(ApplicationConfig.getInstance().getTestUserLogin1(),
                    ApplicationConfig.getInstance().getTestUserPassword1()), new QBEntityCallbackImpl<QBSession>() {
                @Override
                public void onSuccess(QBSession session, Bundle args) {
                    super.onSuccess(session, args);
                    Log.i(TAG, "session created, token = " + session.getToken());
                }

                @Override
                public void onError(List<String> errors) {
                    handleErrors(errors);
                }
            });

            try {
                BaseService.createFromExistentToken("31ed199120fb998dc472aea785a1825809ad5c04", null);
            } catch (BaseServiceException e) {
                e.printStackTrace();
            }
        }
    };

    Snippet createSessionWithUserSynchronous = new AsyncSnippet("create session (synchronous)", "with user", context) {
        @Override
        public void executeAsync() {
            QBSession session = null;
            try {
                QBUser user = new QBUser(ApplicationConfig.getInstance().getTestUserLogin1(),
                        ApplicationConfig.getInstance().getTestUserPassword1());
                session = QBAuth.createSession(user);
            } catch (QBResponseException e) {
                setException(e);
            }
            if(session != null){
                Log.i(TAG, "session created, token = " + session.getToken());
            }
        }
    };


    //
    ////////////////////////// Create session with social provider /////////////////////////////////
    //


    Snippet createSessionWithSocialProvider = new Snippet("create session", "with social provider") {
        @Override
        public void execute() {

            String facebookAccessToken = "AAAEra8jNdnkBABYf3ZBSAz9dgLfyK7tQNttIoaZA1cC40niR6HVS0nYuufZB0ZCn66VJcISM8DO2bcbhEahm2nW01ZAZC1YwpZB7rds37xW0wZDZD";

            QBAuth.createSessionUsingSocialProvider(QBProvider.FACEBOOK, facebookAccessToken, null, new QBEntityCallbackImpl<QBSession>() {

                @Override
                public void onSuccess(QBSession session,  Bundle args) {
                    Log.i(TAG, "session created, token = "+session.getToken());
                }

                @Override
                public void onError(List<String> eroors) {
                    handleErrors(eroors);
                }
            });
        }
    };

    Snippet createSessionWithSocialProviderSynchronous = new AsyncSnippet("create session (synchronous)", "with social provider", context) {
        @Override
        public void executeAsync() {
            QBSession session = null;
            try {
                String facebookAccessToken = "AAAEra8jNdnkBABYf3ZBSAz9dgLfyK7tQNttIoaZA1cC40niR6HVS0nYuufZB0ZCn66VJcISM8DO2bcbhEahm2nW01ZAZC1YwpZB7rds37xW0wZDZD";

                session = QBAuth.createSessionUsingSocialProvider(QBProvider.FACEBOOK, facebookAccessToken, null);
            } catch (QBResponseException e) {
                setException(e);
            }
            if(session != null){
                Log.i(TAG, "session created, token = " + session.getToken());
            }
        }
    };


    //
    ////////////////////////// Create session with Twitter Digits /////////////////////////////////
    //


    Snippet createSessionWithTwitterDigits = new Snippet("create session", "with Twitter Digits") {
        @Override
        public void execute() {
            initTwitterDigits();
            authenticateWithTwitterDigits(false);

        }
    };

    Snippet createSessionWithTwitterDigitsSynchronous = new AsyncSnippet("create session (synchronous)", "with Twitter Digits", context) {
        @Override
        public void executeAsync() {
            initTwitterDigits();
            authenticateWithTwitterDigits(true);
        }
    };

    private void initTwitterDigits() {
        if(authConfig == null) {
            // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
            String consumerKey = "A1NXq7BxZ74NZ3dDzXA1HcSN7";
            String consumerSecret = "Piuy52Kf2m2iHVKpfpffi6xjvOYVI904O6sl1c50TLpntTVsl6";

            authConfig = new TwitterAuthConfig(consumerKey, consumerSecret);
            Fabric.with(context, new TwitterCore(authConfig), new Digits());
        }
    }

    private void authenticateWithTwitterDigits(final boolean isSync) {
        Digits.authenticate(new AuthCallback() {
            @Override
            public void success(DigitsSession session, String phoneNumber) {
                Map<String, String> authHeaders = getAuthHeadersBySession(session);

                Lo.g(authHeaders);

                String xAuthServiceProvider = authHeaders.get("X-Auth-Service-Provider");
                String xVerifyCredentialsAuthorization = authHeaders.get("X-Verify-Credentials-Authorization");

                if(isSync){
                    QBSession qbSession = null;
                    try {
                        qbSession = QBAuth.createSessionUsingTwitterDigits(xAuthServiceProvider, xVerifyCredentialsAuthorization);
                    } catch (QBResponseException e) {
                        e.printStackTrace();
                    }
                    if(session != null){
                        Log.i(TAG, "session created: " + qbSession);
                    }
                }else{
                    QBAuth.createSessionUsingTwitterDigits(xAuthServiceProvider, xVerifyCredentialsAuthorization, new QBEntityCallbackImpl<QBSession>() {
                        @Override
                        public void onSuccess(QBSession qbSession, Bundle params) {
                            Log.i(TAG, "session created: "+qbSession);
                        }

                        @Override
                        public void onError(List<String> errors) {
                            handleErrors(errors);
                        }
                    });
                }
            }

            @Override
            public void failure(DigitsException exception) {
                log(exception.getMessage());
            }
        }, "+38");
    }

    private Map<String, String> getAuthHeadersBySession(DigitsSession digitsSession) {
        TwitterAuthToken authToken = (TwitterAuthToken) digitsSession.getAuthToken();
        DigitsOAuthSigning oauthSigning = new DigitsOAuthSigning(authConfig, authToken);

        return oauthSigning.getOAuthEchoHeadersForVerifyCredentials();
    }


    //
    ///////////////////////////////////// Destroy session //////////////////////////////////////////
    //


    Snippet destroySession = new Snippet("destroy session") {
        @Override
        public void execute() {
            QBAuth.deleteSession(new QBEntityCallbackImpl<Void>() {

                @Override
                public void onSuccess() {

                }

                @Override
                public void onError(List<String> list) {

                }
            });
        }
    };

    Snippet destroySessionSynchronous = new AsyncSnippet("delete session (synchronous)", context) {
        @Override
        public void executeAsync() {
            try {
                QBAuth.deleteSession();
                Log.i(TAG, "session destroyed ");
            } catch (QBResponseException e) {
                Log.i(TAG, "destroy fail");
                setException(e);
            }
        }
    };


    //
    ///////////////////////////////////// Get session //////////////////////////////////////////
    //


    Snippet getSession = new Snippet("get session") {
        @Override
        public void execute() {

            QBAuth.getSession(new QBEntityCallbackImpl<QBSession>() {
                @Override
                public void onSuccess(QBSession qbSession, Bundle bundle) {
                    Log.i(TAG, "session: " + qbSession);
                }

                @Override
                public void onError(List<String> strings) {

                }
            });
        }
    };

    Snippet getSessionSynchronous = new AsyncSnippet("get session (synchronous)", context) {
        @Override
        public void executeAsync() {
            try {
                QBSession session = QBAuth.getSession();
                Log.i(TAG, "session: " + session);
            } catch (QBResponseException e) {
                Log.i(TAG, "get session fail");
                setException(e);
            }
        }
    };

}
