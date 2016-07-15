# Project si.fri.mag.gasperin.android.gcm

Application is created for android devices which can receive messages from GCM services. It is created from the [QuickStart project](https://github.com/googlesamples/google-services/tree/master/android/gcm) of google samples and it's modified to save history of received messages and show it on main activity.

##Steps to succesffully build the project
- Create GCM server API key on [google developer console](https://console.cloud.google.com).
- Copy created API key and replace the *** sign in file [GcmSender.java](/si.fri.mag.gasperin.android.gcm/android/gcm/gcmsender/src/main/java/gcm/play/android/samples/com/gcmsender/GcmSender.java) in line 36 (public static final String API_KEY = "***";)
- Create a google services configuration file. For help you can use this [link](https://developers.google.com/cloud-messaging/android/client).


