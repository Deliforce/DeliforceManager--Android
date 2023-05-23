/*
package com.app.deliforce.Cognito;

import android.content.Context;
import android.util.Log;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.iot.AWSIotKeystoreHelper;
import com.amazonaws.mobileconnectors.iot.AWSIotMqttClientStatusCallback;
import com.amazonaws.mobileconnectors.iot.AWSIotMqttLastWillAndTestament;
import com.amazonaws.mobileconnectors.iot.AWSIotMqttManager;
import com.amazonaws.mobileconnectors.iot.AWSIotMqttQos;
import com.amazonaws.regions.Region;
import com.amazonaws.services.iot.AWSIotClient;
import com.amazonaws.services.iot.model.AttachPrincipalPolicyRequest;
import com.amazonaws.services.iot.model.CreateKeysAndCertificateRequest;
import com.amazonaws.services.iot.model.CreateKeysAndCertificateResult;

import java.security.KeyStore;

import static com.amazonaws.mobile.auth.core.internal.util.ThreadUtils.runOnUiThread;

public class AWSIoTConfig {

    private static final String TAG = "AWSIoTConfig";
    private static AWSIoTConfig awsIoTConfig;

    //IoT related
    private Context appContext;
    private AWSIotClient awsIotClient;
    private AWSIotMqttManager mqttManager;
    private KeyStore clientKeyStore = null;
    private String certificateId;
    private String keystorePath;
    private String keystoreName;
    private String keystorePassword;
    private CognitoCachingCredentialsProvider credentialsProvider;
    private boolean isConnectdWithMqtt = false;

    public void initContext(Context context) {
        if (appContext == null) {
            appContext = context;
        }
    }

    public Context getContext() {
        return appContext;
    }

    public static Context get() {
        return getInstance().getContext();
    }

    public synchronized static AWSIoTConfig getInstance() {

        if (awsIoTConfig == null) {
            awsIoTConfig = new AWSIoTConfig();
        }
        return awsIoTConfig;
    }

    public void initAWSIot() {
        // Initialize the AWS Cognito credentials provider
        credentialsProvider = new CognitoCachingCredentialsProvider(
                appContext, // context
                AppHelper.COGNITO_POOL_ID, // Identity Pool ID
                AppHelper.cognitoRegion // Region
        );

        Region region = Region.getRegion(AppHelper.cognitoRegion);

        // MQTT Client
        mqttManager = new AWSIotMqttManager(AppHelper.APP_CLIENT_ID, AppHelper.CUSTOMER_SPECIFIC_ENDPOINT);

        // Set keepalive to 10 seconds.  Will recognize disconnects more quickly but will also send
        // MQTT pings every 10 seconds.
        mqttManager.setKeepAlive(60);


        // Set Last Will and Testament for MQTT.  On an unclean disconnect (loss of connection)
        // AWS IoT will publish this message to alert other clients.
        AWSIotMqttLastWillAndTestament lwt = new AWSIotMqttLastWillAndTestament("my/lwt/topic", "Android client lost connection", AWSIotMqttQos.QOS1);

        // IoT Client (for creation of certificate if needed)
        awsIotClient = new AWSIotClient(credentialsProvider);
        awsIotClient.setRegion(region);

        mqttManager.setFullQueueToKeepOldestMessages();

        keystorePath = appContext.getFilesDir().getPath();
        keystoreName = AppHelper.KEYSTORE_NAME;
        keystorePassword = AppHelper.KEYSTORE_PASSWORD;
        certificateId = AppHelper.CERTIFICATE_ID;

        // To load cert/key from keystore on filesystem
        try {
            if (AWSIotKeystoreHelper.isKeystorePresent(keystorePath, keystoreName)) {
                if (AWSIotKeystoreHelper.keystoreContainsAlias(certificateId, keystorePath,
                        keystoreName, keystorePassword)) {
                    Log.e(TAG, "Certificate " + certificateId + " found in keystore - using for MQTT.");
                    // load keystore from file into memory to pass on connection
                    clientKeyStore = AWSIotKeystoreHelper.getIotKeystore(certificateId, keystorePath, keystoreName, keystorePassword);
                    //btnConnect.setEnabled(true);
                } else {
                    Log.e(TAG, "Key/cert " + certificateId + " not found in keystore.");
                }
            } else {
                Log.e(TAG, "Keystore " + keystorePath + "/" + keystoreName + " not found.");
            }
        } catch (Exception e) {
            Log.e(TAG, "An error occurred retrieving cert/key from keystore.", e);
        }

        if (clientKeyStore == null) {
            Log.e(TAG, "Cert/key was not found in keystore - creating new key and certificate.");

            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        // Create a new private key and certificate. This call
                        // creates both on the server and returns them to the
                        // device.
                        CreateKeysAndCertificateRequest createKeysAndCertificateRequest = new CreateKeysAndCertificateRequest();
                        createKeysAndCertificateRequest.setSetAsActive(true);
                        final CreateKeysAndCertificateResult createKeysAndCertificateResult;
                        createKeysAndCertificateResult = awsIotClient.createKeysAndCertificate(createKeysAndCertificateRequest);
                        Log.e(TAG, "Cert ID: " + createKeysAndCertificateResult.getCertificateId() + " created.");

                        // store in keystore for use in MQTT client
                        // saved as alias "default" so a new certificate isn't
                        // generated each run of this application
                        AWSIotKeystoreHelper.saveCertificateAndPrivateKey(certificateId,
                                createKeysAndCertificateResult.getCertificatePem(),
                                createKeysAndCertificateResult.getKeyPair().getPrivateKey(),
                                keystorePath, keystoreName, keystorePassword);

                        // load keystore from file into memory to pass on
                        // connection
                        clientKeyStore = AWSIotKeystoreHelper.getIotKeystore(certificateId, keystorePath, keystoreName, keystorePassword);

                        // Attach a policy to the newly created certificate.
                        // This flow assumes the policy was already created in
                        // AWS IoT and we are now just attaching it to the
                        // certificate.
                        AttachPrincipalPolicyRequest policyAttachRequest =
                                new AttachPrincipalPolicyRequest();
                        policyAttachRequest.setPolicyName(AppHelper.AWS_IOT_POLICY_NAME);
                        policyAttachRequest.setPrincipal(createKeysAndCertificateResult
                                .getCertificateArn());
                        awsIotClient.attachPrincipalPolicy(policyAttachRequest);

                    } catch (Exception e) {
                        Log.e(TAG, "Exception occurred when generating new private key and certificate." + " " + e.getMessage(), e);
                    }
                }
            }).start();
        }
    }


    public AWSIotMqttManager connectWithMQTT() {
        //Connect with server
        try {
            mqttManager.connect(clientKeyStore, (status, throwable) -> {
                Log.d(TAG, "Status = " + String.valueOf(status));

                runOnUiThread(() -> {
                    if (status == AWSIotMqttClientStatusCallback.AWSIotMqttClientStatus.Connecting) {
//                        Log.e(TAG, "Connecting...");

                    } else if (status == AWSIotMqttClientStatusCallback.AWSIotMqttClientStatus.Connected) {
//                        Log.e(TAG, "Connected");

                    } else if (status == AWSIotMqttClientStatusCallback.AWSIotMqttClientStatus.Reconnecting) {
                        if (throwable != null) {
                            Log.e(TAG, "error Reconnecting.", throwable);
                        }
//                                Log.e(TAG, "Reconnecting");
                    } else if (status == AWSIotMqttClientStatusCallback.AWSIotMqttClientStatus.ConnectionLost) {
                        if (throwable != null) {
                            Log.e(TAG, "error ConnectionLost.", throwable);
                        }
//                        Log.e(TAG, "Disconnected");
                    } else {
//                        Log.e(TAG, "Disconnected");
                    }
                });
            });
        } catch (final Exception e) {
            Log.e(TAG, "Error! " + e.getMessage());
        }
        return mqttManager;
    }
}
*/
