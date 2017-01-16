---
layout: post
title:  "Configuring Veracode2Rally"
date:   2016-12-30
tags: [Configuration, Getting Started]
---

Before running Veracode2Rally, it must be configured to map a Veracode project with its corresponding Rally project. Configuration information is stored in an XML file called "veracode2rally_config.xml". 

The recommended method to enter this data is through the included application "veracode2rallyConfig.jar". When using this utility it is best to use the Oracle version of java, not OpenJDK. Launch the application by entering:

    java -jar veracocde2rallyConfig.jar

to view the main screen.

![](http://i.imgur.com/vFs1ovG.png)

The file created should be named veracode2rally_config.xml file should and saved to the resources folder of the veracode2rally application folder.

Each tab represents specific configuration  for Veracode2Rally

#Project Mapping Tab#

The purpose of the Project Mapping tab is to map what application in Veracode to what project in Rally. Project mapping records are created with a 1:1 relationship of a Veracode project with its corresponding Rally project. A single record or multiple project mapping records can be created. Each veracode2rally synchronization will cycle through all project mapping records. Click New to create a new Project mapping record

![](http://i.imgur.com/k7vvh75.png)


**Veracode App Name**
<br>
The name of the application in Veracode. The "Application Name" as listed in Veracode. This field is not required.

**Veracode App ID**
<br>
The Veracode appid is a number as listed in Veracode. REQUIRED. To find the appid of your application, navigate to the main profile page for that application. The appid is the second number in the URL between the second and third colon.   


![](http://i.imgur.com/uq00gT4.png)

The Veracode appid for the example above is: 123456

**Rally Project Name**
<br>
The project name as listed in Rally. This field is not required.

**Rally Project ID**
<br>
The ID for the Rally project. REQUIRED. To find the Rally ID for the project, navigate to the Rally dashboard for that project.The project ID is the first number listed in the URL.

![](http://i.imgur.com/tDuQ60d.png)

The Rally Project ID for the example above is: 12345678901

**Import**
<br>
Pick one of the four choices of what flaws are to be exported out of Veracode and into Rally.

- All flaws
- Flaws affecting policy
- All unmitigated flaws
- Unmitigated flaws affecting policy


#Veracode Credentials Tab#

A valid Veracode and Rally account is required to use the Veracode2Rally application. Credentials to these accounts are stored in veracode2rally_config.xml. To be able to access the Veracode APIs, the account used by Veracode2Rally  must either have a human or non-human API customer account and the associated user roles for specific API tasks.

![](http://i.imgur.com/J3xts2g.png)


When using a human account, enter the Veracode Username/Veracode Password and select  Username/Password for the **"Login with"** dropdown.

For an API account, enter the Veracode API ID/Veracode API Key and select  API ID/Key for the **"Login with"** dropdown.

Credentials for both Username/Password and API ID/Key can be entered but only the credential selected by the **"Login with"** dropdown will be used.

**Encrypt Password and Key**

Checking this box encrypts the Veracode Password and API Key in encrypted form when writing to veracode2rally_config.xml. If you wish to use this feature, it is recommended that you change the default password located in credentials.java and veracode2rallyConfig.java.


#Rally Credentials Tab#

A valid Veracode and Rally account is required to use the Veracode2Rally application. Credentials to these accounts are stored in veracode2rally_config.xml. To be able to access the Rally APIs, the account used by Veracode2Rally  must have the appropriate access to the Rally project. Users can acquire a Rally API Key by navigating to:

[https://rally1.rallydev.com/login/](https://rally1.rallydev.com/login/ "Rally Account Page")

and click API KEYS

Having a Veracode or Rally account associated with Single Sign-on may cause problems with access when using Veracode2Rally. For those cases, using an API account is recommended.

![](http://i.imgur.com/kiZK7MG.png)

When using a human account, enter the Rally API Key and select  Username/Password for the **"Login with"** dropdown.

For an API account, enter the Veracode API ID/Veracode API Key and select  API Key for the **"Login with"** dropdown. Only the Rally API Key is needed when using this option.

Credentials for both Username/Password and API Key can be entered but only the credential selected by the **"Login with"** dropdown will be used.

**Encrypt Password and Key**

Checking this box encrypts the Rally Password and API Key in encrypted form when writing to veracode2rally_config.xml. If you wish to use this feature, it is recommended that you change the default password located in credentials.java and veracode2rallyConfig.java.

#Rally Custom Fields Tab#

Veracode2Rally can use custom fields in Rally. This enables developers to propose mitigations from Rally back to Veracode. For more information about this, please view the demo video on Veracode2Rally at:

[http://veracode2rally.com](http://veracode2rally.com "http://veracode2rally.com")
<br>

 Once approved in Veracode, veracode2rally communicates this to Rally and closes the ticket. None of the entries on this tab are required. If you do not want to propose mitigations this way, please leave these boxes blank.

![](http://i.imgur.com/fRvKgdB.png)


Below are sample specifications to be given to a Rally administrator to create these custom fields. What specifically to name the field is optional, but be aware that custom fields in Rally when called through the API require that a "c_" be placed in front of the field name as demonstrated in the example above.  



**Add Custom Field**
<br>
Name: Comment
<br>
DisplayName: Comment
<br>
Type: Text
<br>
Visible This Project: Ye
<br>
<br>
**Add Custom Field**
<br>
Name: MitigationHistory
<br>
DisplayName: MitigationHistory
<br>
Type: Text
<br>
Visible This Project: Yes
<br>
<br>
**Add Custom Field**
<br>
Name: VeracodeID
<br>
DisplayName: VeracodeID
<br>
Type: String
<br>
Visible This Project: Yes
<br>
<br>
**Add Custom Field**
<br>
Name: Action
<br>
DisplayName: Action
<br>
Type: Drop Down List
<br>
Visible This Project: Yes
<br>
Values:
<br>
- Add Comment
<br>
- Mitigate by Design
<br>
- Mitigate by Network Environment
<br>
- Mitigate by OS Environment
-<br>
- Potential False Positive
