# Project si.fri.mag.gasperin.android.gcm

Application is created for android devices which can receive messages from GCM services. It is created from the [https://github.com/googlesamples/google-services/tree/master/android/gcm](QuickStart project) of google samples. It is modified to save history of received messages and show it on main activity.

##Steps to succesffully build the project
- Create GCM server API key on [console.cloud.google.com](google developer console).
- Copy created API key and replace the *** sign in file [/si.fri.mag.gasperin.android.gcm/android/gcm/gcmsender/src/main/java/gcm/play/android/samples/com/gcmsender/GcmSender.java](GcmSender.java) in line 36 (public static final String API_KEY = "***";)


