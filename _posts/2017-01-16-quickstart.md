---
layout: post
title:  "QuickStart"
date:   2017-01-16
tags: [Configuration, QuickStart]
---
# How To Get Up And Running #
----------

This document focuses on the basics of configuring Veracode2Rally. Those wanting to explore more advanced configurations such as modifying the underlying XSL file for complex field mapping or review the XSD (XML Schema Definition) for veracode2rally_config.xml should review the documentation at [http://veracode2rally.com](http://veracode2rally.com "http://veracode2rally.com")


#Requirements#

1. Veracode Credentials - A regular human account or non-human API account with access to the application in Veracode and granted the REVIEWER role. 
2. Rally Credentials - A regular user account or Rally API Key with EDITOR permissions granted for the Rally project. 
3. Oracle Java 1.8 installed.


#Installation#
Download **veracode2rally_app.zip** located on GitHub at:
[https://github.com/SecureDevOps/veracode2rally/blob/master/app/veracode2rally_app.zip?raw=true](https://github.com/SecureDevOps/veracode2rally/blob/master/app/veracode2rally_app.zip?raw=true "cc")

Unzip the contents into a directory. You'll see these files in the following directory structure

    Veracode2Rally_QuickStart.pdf
    veracode2rally.jar
    veracode2rallyConfig.jar
    resources/veracode2rally_config.xml
    resources/veracode2rally.xsl

Once configured, you can run veracode2rally at a command prompt by typing:

    java -jar veracode2rally.jar



#Configuring#

Before running Veracode2Rally, it must be configured to map a Veracode application to its corresponding Rally project, store credentials and designate any desired custom fields. Configuration information is saved to **veracode2rally_config.xml** in the resources folder. 

The recommended method to configure Veracode2Rally is run the included configuration utility **veracode2rallyConfig.jar**. This application must use the Oracle version of Java, not OpenJDK. Launch the application by entering:

    java -jar veracocde2rallyConfig.jar

to view the main screen.


![](https://github.com/SecureDevOps/veracode2rally/blob/gh-pages/images/veracode2rally_config_open.png?raw=true)




Open the blank **veracode2rally_config.xml** file in the resources folder by clicking FILE, then OPEN and save back to that location by clicking FILE, then SAVE when configuration is complete. Each tab represents a needed configuration for Veracode2Rally.

#Project Mapping Tab#

The purpose of the Project Mapping tab is to map what application in Veracode corresponds to what project in Rally. Project mapping records are created with a 1:1 relationship. Each application in Veracode corresponds to a project in Rally. A single record or many project mapping records can be created. Each Veracode2Rally synchronization will cycle through all project mapping records created. Click New to create a new Project mapping record.

![](https://github.com/SecureDevOps/veracode2rally/blob/gh-pages/images/project_mapping.png?raw=true)


**Veracode App Name - Optional**
<br>
The "Application Name" as listed in Veracode.

**Veracode App ID - Required**
<br>
To find the appid of your application, navigate to the main profile page for that application in Veracode. The appid is the second number in the URL between the second and third colon.   


![](https://github.com/SecureDevOps/veracode2rally/blob/gh-pages/images/veracode_appid.png?raw=tru)

The Veracode appid for the example above is: 123456

**Rally Project Name - Optional**
<br>
The project name as listed in Rally. This field is not required.

**Rally Project ID - Required**
<br>
To find the Rally ID for the project, navigate to the Rally dashboard for that project.The project ID is the first number listed in the URL.

![](https://github.com/SecureDevOps/veracode2rally/blob/gh-pages/images/rally_project_id.png?raw=true)

The Rally Project ID for the example above is: 12345678901

**Import - Required**
<br>
Pick one of the four choices of what flaws are to be exported out of Veracode and into Rally.

- All flaws
- Flaws affecting policy
- All unmitigated flaws
- Unmitigated flaws affecting policy


#Veracode Credentials Tab#

Veracode credentials are stored in **veracode2rally_config.xml**. A Username/Password combination, API ID/Key combination or both can be stored. For Veracode accounts that use Single sign-on (SSO), an API ID/Key should be used.

![](https://github.com/SecureDevOps/veracode2rally/blob/gh-pages/images/veracode_credentials.png?raw=true)


When using a human account, enter the Veracode Username/Veracode Password and select Username/Password in the **"Login with"** dropdown.

For an API account, enter the Veracode API ID/Veracode API Key and select  API ID/Key in the **"Login with"** dropdown.

Credentials for both Username/Password and API ID/Key can be entered but only the credential selected by the **"Login with"** dropdown will be used during synchronization.

**Encrypt Password and Key**

Checking this box stores the Veracode Password and API Key in encrypted form when writing to **veracode2rally_config.xml**. If you wish to use this feature, it is recommended that you change the default password located in credentials.java and veracode2rallyConfig.java.


#Rally Credentials Tab#

Rally credentials are stored in **veracode2rally_config.xml**. A Username/Password combination, API Key or both can be stored. For Rally accounts that use Single sign-on (SSO), an API Key should be used. Users can acquire a Rally API Key by navigating to:

[https://rally1.rallydev.com/login/](https://rally1.rallydev.com/login/ "Rally Account Page")

and click API KEYS


![](https://github.com/SecureDevOps/veracode2rally/blob/gh-pages/images/rally_credentials.png?raw=true)

When using a regular user account, enter the Rally Username and Password and select Username/Password in the **"Login with"** dropdown.

For an API account, enter the Rally API Key and select  API Key in the **"Login with"** dropdown. Only the Rally API Key is needed when using this option.

Credentials for both Username/Password and API Key can be entered but only the credential selected by the **"Login with"** dropdown will be used during synchronization.

**Encrypt Password and Key**

Checking this box stores the Rally Password and API Key in encrypted form when writing to **veracode2rally_config.xml**. If you wish to use this feature, it is recommended that you change the default password located in credentials.java and veracode2rallyConfig.java.

#Rally Custom Fields Tab#

Veracode2Rally can use custom fields in Rally. This enables developers to propose mitigations in Rally and synchronize them with Veracode. To see more about this, please view the demo video at:

[http://veracode2rally.com](http://veracode2rally.com "http://veracode2rally.com")
<br>

 **THIS FEATURE IS OPTIONAL**. None of the entries on this tab are required. If you do not want to use the propose mitigation from Rally feature, leave these boxes blank.

![](https://github.com/SecureDevOps/veracode2rally/blob/gh-pages/images/rally_custom_fields.png?raw=true)


Below are sample specifications to be given to a Rally administrator to create these custom fields. What specifically to name the field is optional, but be aware that custom fields in Rally when called through the API require that a "c_" be placed in front of the field name as demonstrated in the example above.  

**Mitigation Action - Optional**
<br>
Maps to the Mitigation Action dropdown in Veracode
<br>
**In Rally, Add Custom Field**
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



**Mitigation Comment - Optional**
<br>
Maps to the Comment text box located next to the Mitigation Action dropdown in Veracode
<br>
**In Rally, Add Custom Field**
<br>
Name: Comment
<br>
DisplayName: Comment
<br>
Type: Text
<br>
Visible This Project: Yes
<br>



**Mitigation History - Optional**
<br>
Represents a history of all mitigation actions and comments stored in Veracode
<br>
**In Rally, Add Custom Field**
<br>
Name: MitigationHistory
<br>
DisplayName: MitigationHistory
<br>
Type: Text
<br>
Visible This Project: Yes
<br>



**Unique ID - Optional**
<br>
Veracode2Rally creates a unique ID for each flaw and by default stores it in the Rally Description field. Users who want to store this identifier in a separate field for reporting purposes should create a Rally custom field using the specifications below and indicate the name of that field in this box.
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


