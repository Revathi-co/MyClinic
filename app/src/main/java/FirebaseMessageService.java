import android.util.Log;

public class FirebaseMessageService extends com.google.firebase.messaging.FirebaseMessagingService {

    String TAG ="FirebaseMessageService" ;

    @Override
    public void onNewToken(String token) {
        Log.d(TAG, "Refreshed token: " + token);
    }


}
