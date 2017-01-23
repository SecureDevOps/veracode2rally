**Veracode2Rally**
------------------

Veracode2rally is a java application that exports  the results of Veracode static analysis scans into Rally. With each synchronization, Rally defect tickets are opened, updated or closed based on security flaws found in the most recent Veracode scan. Users can choose what type of flaws are exported.

 1. All flaws
 2. Flaws affecting policy
 3. All unmitigated flaws
 4. Unmitigated flaws affecting policy

Developers will review defect tickets in Rally to work on a remediation to fix the security flaw or propose a mitigation. If the developer fixes the flaw, veracode2rally closes the corresponding Rally ticket after the application has been rescanned and synchronized. Mitigations can be proposed within Rally by the developer. Veracode2Rally synchronizes the mitigation request back to Veracode. Once the proposed mitigation is approved, veracode2rally closes the Rally ticket during the next synchronization.