#Project with OM2M platform and included CEP library

This project contains full OM2M platform with custom created plug-in example which contains CEP library. With it we can create, edit, delete, modify CEP rules for all conected devices. You can find more about CEP library [here](https://github.com/gasperinn/om2m-cep).

The custom created plug-in contains one connected sensor with GUI and predefined DATA container for that sensor. The sensor represents combined blood pressure sensor and accelerometer. With it we can send data on OM2M platform about systolic, diastolic value of blood pressure sensor and X, Y, Z axis values of accelerometer at the same time. With help of CEP library we can create rules which can detect complex events and send the event to specified data container. 

##Steps to run OM2M with custom made plug-in
- Import this project to your Eclipse Kepler workspace and build it ([more](https://wiki.eclipse.org/OM2M/one/Clone)) or just download that [project](om2m-with-cep-usage-modules). 
- Once you have built OM2M platform or downloaded whole project start file
"org.eclipse.om2m/org.eclipse.om2m.site.in-cse/target/products/in-cse/<os>/<ws>/<arch>\start.bat" on windows or 
"org.eclipse.om2m/org.eclipse.om2m.site.in-cse/target/products/in-cse/<os>/<ws>/<arch>\start.sh" on linux
- It opens the terminal of OM2M platform. Type "ss" to see which plug-ins are running.
- Type "start 33" to start org.eclipse.om2m.sample.sensor plug-in.

If the GUI of sensor simulator for sending data on OM2M platform shows up, you have successfully run the plug-in. 

To generate CEP rules visit [OM2M platform IP]:8081/cep e.g. http://localhost:8081/cep. Default username and password for login are admin:admin. Here you can create, edit and delete CEP rules for (in org.eclipse.om2m.sample.sensor plug-in) one conected sensor. Rules are defined in [Esper EPL syntax](http://www.espertech.com/esper/release-5.3.0/esper-reference/html/epl_clauses.html).

##CEP rule example
Below defined CEP rule catches the event if the average of the last 4 recieved messages shows that user has got systolic value of blood preasure higher than 140, diastolic value of blood preasure higher than 90 and if the average of accelerometer X, Y and Z axis values are smaller than 0.5.

This meens that our user is not under physical activity (accelerometer values) and the blood preasure is abnormal high.  
```
select avg(systolic) AS systolic, avg(diastolic) AS diastolic, avg(x) AS x, avg(y) AS y, avg(z) AS z from Sensor.win:length(4) having (avg(systolic) > 140 AND avg(diastolic) > 90) AND (avg(x) < 0.5 AND avg(y) < 0.5 AND avg(z) < 0.5 ) 
```
