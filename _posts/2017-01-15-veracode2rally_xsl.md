---
layout: post
title:  "Field Mapping"
date:   2016-12-30
tags: [Configuration, Field Mapping]
---

Field mapping determines what field in Veracode is copied to what field in Rally and is performed through XSLT (Extensible Stylesheet Language Transformations).
Using the veracode2rally.xsl file located in the resources folder and the Veracode Detailed Flaw Report (detailedreport.xml), a new XML file (veracode2rally.xml) is generated for each application.
This is the file that Veracode2Rally uses for calling Rally APIs to create and update tickets. Documentation for 
<a href="{{site.baseurl}}/web/xsl/veracode2rally-xsl.html" target="_blank" title="Veracode2Rally XSL">veracode2rally.xsl</a>
is available to customize Veracode2Rally field mapping and can be viewed 
<a href="{{site.baseurl}}/web/xsl/veracode2rally.html" target="_blank" title="Veracode2Rally XSL">here</a>.

Below is the default Veracode2Rally field mapping configuration. Veracode2rallyID is a unique identifier and should not be deleted but can be mapped to another Rally field. 
Run the veracode2rally_Config utility and enter the field name designated to be the Unique ID in the "Unique ID" text box. Instructions on how to use this utility is available in the QuickStart guide. 

<style>
	.demo {
		width:85%;
		height:40%;
		border:1px solid #C0C0C0;
		border-collapse:collapse;
		padding:5px;
	}
	.demo th {
		border:1px solid #C0C0C0;
		padding:5px;
		background:#F0F0F0;
	}
	.demo td {
		border:1px solid #C0C0C0;
		padding:5px;
	}
</style>
<table class="demo">
	<thead>
	<tr>
		<th>Veracode</th>
		<th>Rally</th>
	</tr>
	</thead>
	<tbody>
	<tr>
		<td>&nbsp;categoryname</td>
		<td>name&nbsp;</td>
	</tr>
	<tr>
		<td>&nbsp;issueid (Veracode Flaw ID)</td>
		<td>description</td>
	</tr>
	<tr>
		<td>&nbsp;cweid (CWE)</td>
		<td>description</td>
	</tr>
	<tr>
		<td>&nbsp;module (Module)</td>
		<td>description</td>
	</tr>
	<tr>
		<td>&nbsp;sourcefile/line (Source)</td>
		<td>description</td>
	</tr>
	<tr>
		<td>&nbsp;type (Attack Vector)</td>
		<td>description</td>
	</tr>
	<tr>
		<td>&nbsp;severity (Severity)</td>
		<td>description</td>
	</tr>
	<tr>
		<td>&nbsp;exploitLevel (Exploitability)</td>
		<td>description</td>
	</tr>
	<tr>
		<td>&nbsp;remediationeffort (Effort to Fix)</td>
		<td>description</td>
	</tr>
	<tr>
		<td>&nbsp;description (Description/Remediation)</td>
		<td>description</td>
	</tr>
	<tr>
		<td>&nbsp;veracode2rallyID (veracode2rally ID)</td>
		<td>description</td>
	</tr>
	<tbody>



