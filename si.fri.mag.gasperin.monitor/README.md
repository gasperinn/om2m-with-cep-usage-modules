# Project si.fri.mag.gasperin.monitor

Application Monitor can be subscribed to receive data from platform OM2M (default on port 1400 under context /monitor) and post messages to GCM service (Google Cloud Messaging).

##Steps to subscribe application to OM2M platform and forward data to GCM service
- Edit the file [config.properties](https://github.com/gasperinn/om2m-with-cep-usage-modules/blob/master/si.fri.mag.gasperin.monitor/jar/config.properties) in [jar](https://github.com/gasperinn/om2m-with-cep-usage-modules/tree/master/si.fri.mag.gasperin.monitor/jar) folder to your needs (file description is below)
- Run OM2M platform
- Open CMD and navigate to [jar](https://github.com/gasperinn/om2m-with-cep-usage-modules/tree/master/si.fri.mag.gasperin.monitor/jar) folder
- execute command "java -jar monitor.jar"

##config.properties file
```
MONITOR_CONTEXT=/monitor                //Context of your Monitor application 
MONITOR_PORT=1400                       //Port of your Monitor application
MONITOR_AUTO_SUBSCRIBE=true             //Auto subscribe on OM2M platform
OM2M_USERNAME=admin                     //Username of OM2M platform
OM2M_PASSWORD=admin                     //Password of OM2M platform
OM2M_IP=127.0.0.1                       //Ip address of the server where OM2M platform is hosted
OM2M_PATH_TO_PRODUCT=/in-cse/in-name    //Path to product for subscription
OM2M_DEVICE_NAME=SENSOR                 //Device name
OM2M_CONTAINER_NAME=CEP_DATA            //Container name
OM2M_SUB_CONTAINER=SUB_MY_SENSOR        //Custom name of subcontainer where subscription will be placed
POST_TO_GCM=false                       //Post to GCM when data is received
GCM_API_KEY=***                         //GCM API key of the application 
GCM_DEVICE_ID=***                       //GCM DEVICE ID
GCM_TITLE=Alert                         //Title of the message which will be posted to GCM
```

##Manually subscribe monitor via HTTP POST message
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
