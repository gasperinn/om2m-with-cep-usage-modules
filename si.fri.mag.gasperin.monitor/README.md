# Project si.fri.mag.gasperin.monitor

Application Monitor can be subscribed to receive data from platform OM2M (default on port 1400 under context /monitor) and post messages to GCM service (Google Cloud Messaging). For subscribing application to platform you need any REST client application (e.g. Advanced REST client for Google Chrome).

Steps to subscribe application to OM2M platform and forward data to GCM service:
- The OM2M platform has to be running
- Edit the 43. and 44. line of Monitor.java file (replace *** sign with **GCM API KEY** and **GCM DEVICE ID** which you can get on [GCM service](https://developers.google.com/cloud-messaging/))
- Run the Monitor.java application
- Post the subscription HTTP POST message to the OM2M platform

##Subscription HTTP POST message
URL: 
```
[OM2M platform IP]:[PORT]/[path to plug-in product]/[device name]/[container name]
```

HEADERS:
```
X-M2M-Origin: [username]:[password]
X-M2M-NM: [name of the container]
Content-Type: application/xml;ty=23
```

CONTENT:
```
<m2m:sub xmlns:m2m="http://www.onem2m.org/xml/protocols">
    <nu>[Monitor IP]:[Monitor PORT]/[Monitor context]</nu>
    <nct>2</nct>
</m2m:sub>
```

###Example message

URL: 
```
http://127.0.0.1:8080/~/in-cse/in-name/SENSOR/CEP_DATA
```

HEADERS:
```
X-M2M-Origin: admin:admin
X-M2M-NM: SUB_MY_SENSOR
Content-Type: application/xml;ty=23
```

CONTENT:
```
<m2m:sub xmlns:m2m="http://www.onem2m.org/xml/protocols">
    <nu>http://localhost:1400/monitor</nu>
    <nct>2</nct>
</m2m:sub>
```
