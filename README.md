**Veracode2Rally**
------------------

Created by DevOps teams for DevOps teams, Veracode2Rally speeds application development by directly exporting security flaws found in Veracode static analysis scans and importing them into Rally. No more manually opening a defect ticket when a Veracode flaw is found or closing one when it’s fixed. Veracode2Rally manages Rally tickets for you based on the results of the last scan.

**What about mitigating false positives?**

Veracode2Rally handles that too. Developers can propose mitigations and be notified of their approval without ever leaving Rally. Using bidirectional synchronization, Veracode2Rally copies a proposed mitigation from Rally to Veracode. If approved, the next synchronization copies a notification from Veracode back to Rally and closes the ticket!

Users can choose what type of flaws are exported.

 1. All flaws
 2. Flaws affecting policy
 3. All unmitigated flaws
 4. Unmitigated flaws affecting policy

There's documentation on how to install, configure and a video demo showing Veracode2Rally in action at
[http://veracode2rally.com](http://veracode2rally.com)