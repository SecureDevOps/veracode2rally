<?xml version="1.0" encoding="UTF-8" ?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
   <xsl:output method="xml" version="1.0" omit-xml-declaration="no" encoding="UTF-8" indent="yes"/>
 
<!-- This xsl file maps Veracode fields to Rally fields when using the veracode2rally application.    
XSLT is performed with a Veracode DetailedReport.xml file. The result is a new file used by the veracode2rally 
application called veracode2rally.xml. 

Mapping more fields or modifying field mapping should be done using this file
 
 -->
    <!-- SECTION ONE  -->
    <!-- A separate veracode2rally.xml file is used for each application being processed.
   Flaw elements from the Veracode detailedreport.xml file are matched and the Section TWO Template is appliced
      -->
    <xsl:template match="/">
        <xsl:text>&#xa;</xsl:text>
        <veracode2rally>
            <xsl:text/>
            <flaws>
                <xsl:apply-templates/>
                <xsl:text/>
            </flaws>
        </veracode2rally>
    </xsl:template>
    
    <!-- SECTION TWO  -->
    <!-- Get all flaws from DetailedReport.xml   -->
    <!-- Header info for each flaw is created in this section. It consists of attributes, followed by Veracode/Rally field mapping in Section THREE  -->
    <xsl:template match="flaw">
        <xsl:text/>
        <xsl:element name="flaw">
            <xsl:attribute name="veracode_issueid">
                <xsl:value-of select="@issueid"/>
            </xsl:attribute>
            <xsl:attribute name="veracode_appid">
                <xsl:value-of select="/*/@app_id"/>
            </xsl:attribute>
            <xsl:attribute name="affects_policy_compliance">
                <xsl:value-of select="@affects_policy_compliance"/>
            </xsl:attribute>
            <xsl:attribute name="mitigation_status">
                <xsl:value-of select="@mitigation_status"/>
            </xsl:attribute>
            <xsl:attribute name="remediation_status">
                <xsl:value-of select="@remediation_status"/>
            </xsl:attribute>
            <xsl:attribute name="veracode2rallyid">
                <xsl:value-of select="@issueid"/>
                <xsl:text>_</xsl:text>
                <xsl:value-of select="/*/@app_id"/>
            </xsl:attribute>
             
            <!-- SECTION THREE  -->
            <!-- Now add Veracode/Rally field mappingThis can be modified to change Veracode/Rally field mapping -->
            <!-- To add a field, add a new attribute. Attribute name is the field name in Rally = the Veracode field located in DetailedReport.xml -->
            <xsl:text/>
            <rally_fields>
                <xsl:element name="rally_field">
                    <xsl:attribute name="name">
                        <xsl:value-of select="@categoryname"/>
                    </xsl:attribute>
                </xsl:element>
                <xsl:element name="rally_field">
                    <xsl:variable name="cwelink" >
                        <xsl:text>http://cwe.mitre.org/data/definitions/</xsl:text><xsl:value-of select="@cweid"/><xsl:text>.html</xsl:text> 
                    </xsl:variable>
                    <xsl:attribute name="description">
                        <xsl:call-template name="StaticFlawDescription">
                        </xsl:call-template>   
                    </xsl:attribute>
                </xsl:element>
            </rally_fields>
        </xsl:element>
    </xsl:template>
     
    <xsl:template name="StaticFlawDescription" >
        <xsl:variable name="cwelink" >
            <xsl:text>http://cwe.mitre.org/data/definitions/</xsl:text><xsl:value-of select="@cweid"/><xsl:text>.html</xsl:text> 
        </xsl:variable>
               
        <xsl:text>&#60;br&#62;</xsl:text>
        <xsl:text>&#60;B&#62;Veracode Flaw ID: &#60;&#47;B&#62;</xsl:text>
        <xsl:value-of select="@issueid"/>
        <xsl:text>&#60;br&#62;&#60;br&#62;</xsl:text>  
        <xsl:text>&#60;B&#62;CWE: &#60;&#47;B&#62;</xsl:text>
        <xsl:text>&#60;a href="</xsl:text><xsl:value-of select="$cwelink"/><xsl:text>" target="_blank"&#62;</xsl:text><xsl:value-of select="@cweid"/><xsl:text>&#60;&#47;a&#62;</xsl:text>
        <xsl:text>&#32;</xsl:text>
        <xsl:value-of select="@categoryname"/>
        <xsl:text>&#60;br&#62;&#60;br&#62;</xsl:text>
        <xsl:text>&#60;B&#62;Module: &#60;&#47;B&#62;</xsl:text>
        <xsl:value-of select="@module"/>
        <xsl:text>&#60;br&#62;&#60;br&#62;</xsl:text>
        <xsl:text>&#60;B&#62;Source: &#60;&#47;B&#62;</xsl:text>
        <xsl:value-of select="@sourcefile"/><xsl:text>:</xsl:text><xsl:value-of select="@line"/>
        <xsl:text>&#60;br&#62;&#60;br&#62;</xsl:text>
        <xsl:text>&#60;B&#62;Attack Vector: &#60;&#47;B&#62;</xsl:text>
        <xsl:value-of select="@type"/>
        <xsl:text>&#60;br&#62;&#60;br&#62;</xsl:text>
        
        <xsl:text>&#60;B&#62;Severity: &#60;&#47;B&#62;</xsl:text>
        <xsl:choose>  
            <xsl:when test="@severity = '0'">Informational</xsl:when>   
            <xsl:when test="@severity = '1'">Very Low</xsl:when>   
            <xsl:when test="@severity = '2'">Low</xsl:when>   
            <xsl:when test="@severity = '3'">Medium</xsl:when>   
            <xsl:when test="@severity = '4'">High</xsl:when>   
            <xsl:when test="@severity = '5'">Very High</xsl:when>   
            <xsl:otherwise>No Data</xsl:otherwise>   
        </xsl:choose>
        
        <xsl:text>&#60;br&#62;&#60;br&#62;</xsl:text> 
        <xsl:text>&#60;B&#62;Exploitability: &#60;&#47;B&#62;</xsl:text>
        <xsl:choose>  
            <xsl:when test="@exploitLevel = '-2'">Very Unlikely</xsl:when>   
            <xsl:when test="@exploitLevel = '-1'">Unlikely</xsl:when>   
            <xsl:when test="@exploitLevel = '0'">Neutral</xsl:when>   
            <xsl:when test="@exploitLevel = '1'">Likely</xsl:when>   
            <xsl:when test="@exploitLevel = '2'">Very Likely</xsl:when>   
            <xsl:otherwise>No Data</xsl:otherwise>   
        </xsl:choose>
        
        <xsl:text>&#60;br&#62;&#60;br&#62;</xsl:text>
        <xsl:text>&#60;B&#62;Effort to Fix: &#60;&#47;B&#62;</xsl:text>
        <xsl:choose>  
            <xsl:when test="@remediationeffort = '1'">1 - Trivial implementation error. Fix is up to 5 lines of code. One hour or less to fix.</xsl:when>   
            <xsl:when test="@remediationeffort = '2'">2 - Implementation error. Fix is approx. 6-50 lines of code. 1 day to fix.</xsl:when>   
            <xsl:when test="@remediationeffort = '3'">3 - Complex implementation error. Fix is approx. 51-500 lines of code. Up to 5 days to fix.</xsl:when>   
            <xsl:when test="@remediationeffort = '4'">4 - Simple design error. Requires redesign and up to 5 days to fix.</xsl:when>   
            <xsl:when test="@remediationeffort = '5'">5 - Complex design error. Requires significant redesign.</xsl:when>   
            <xsl:otherwise>No Data</xsl:otherwise>   
        </xsl:choose>
        
        <xsl:text>&#60;br&#62;&#60;br&#62;</xsl:text>
        <xsl:text>&#60;B&#62;Description: &#60;&#47;B&#62;</xsl:text><xsl:call-template name="DescT"></xsl:call-template>
        <xsl:text>&#60;br&#62;</xsl:text>
        <xsl:text>&#60;br&#62;</xsl:text>
        <xsl:text>&#60;br&#62;</xsl:text>
        <xsl:text>&#60;br&#62;</xsl:text>
        <xsl:text>veracode2rally ID: </xsl:text>
        <xsl:value-of select="@issueid"/>
        <xsl:text>_</xsl:text>
        <xsl:value-of select="/*/@app_id"/>
        <xsl:text> (do not delete)</xsl:text>
        <xsl:text>&#60;br&#62;&#60;br&#62;</xsl:text>  
    </xsl:template>
      
    <!-- This template checks flaw/@description, if it contains"References:", 
  then divides it 2 parts with 1 part before "References:", and 2nd part after "References:".
  And formats the 2nd part (i.e. after "References:") to enable Hyperlinks as per Jira format/style.   -->
    <xsl:template name="DescT" >
        <xsl:variable name="desc" >
            <xsl:value-of select="@description"/>
        </xsl:variable>
        <xsl:variable name="descFormatted" >
            <xsl:choose>
                <xsl:when test="contains($desc, 'References:')">
                    <xsl:value-of select="substring-before($desc, 'References:')" />
                    <xsl:call-template name="RefT">
                        <xsl:with-param name="var" select="substring-after($desc, 'References:')"/>
                    </xsl:call-template>                   
                </xsl:when>
                <xsl:otherwise>
                    <xsl:value-of select="$desc" />
                </xsl:otherwise>
            </xsl:choose>
        </xsl:variable>
          
        <!--  Insert a carriage return and Remediation title between the first and second 
    paragraph in the description if there is one
   -->
   
        <xsl:choose>
            <xsl:when test="contains($descFormatted,'&#xd;&#xa;&#xd;&#xa;')">
                <xsl:value-of select="concat(substring-before($descFormatted,'&#xd;&#xa;&#xd;&#xa;'),'&#60;br&#62;&#60;br&#62;&#60;B&#62;Remediation: &#60;&#47;B&#62;',substring-after($descFormatted,'&#xd;&#xa;&#xd;&#xa;'))"/> 
            </xsl:when>
            <xsl:otherwise>
                <xsl:value-of select="$descFormatted" />
            </xsl:otherwise>
        </xsl:choose>
        
    </xsl:template>
    
    <!-- This template takes the "References:", part and checks if it contains "links" with
    format "(link)" eg: CWE (http://cwe.mitre.org/data/definitions/89.html),
   then it will be displayed as CWE hyperlinked to appropriate page
   -->
    
    <xsl:template name="RefT"  >
        <xsl:param name="var"/>
        <xsl:variable name="var1"><xsl:value-of select="normalize-space($var)"/> </xsl:variable>
        <xsl:text>&#60;br&#62;&#60;br&#62;</xsl:text>
        <xsl:text>&#60;B&#62;References: &#60;&#47;B&#62;</xsl:text>
        <xsl:choose>
            <xsl:when test="(contains($var1, 'http')) and (contains($var1, ')')) and contains($var1, '(')">
                <xsl:variable name="refToken">
                    <xsl:value-of select="translate($var1, '()', '|]')"/>
                </xsl:variable>
                <xsl:variable name="refFinal"> </xsl:variable>
                <xsl:call-template name="refFormat">
                    <xsl:with-param name="refTokenLength" select="string-length($refToken)"/>
                    <xsl:with-param name="refStr" select="$refToken"/>
                    <xsl:with-param name="refFinal" select="$refFinal"/>
                </xsl:call-template>
            </xsl:when>
            <xsl:otherwise>
                <xsl:value-of select="$var1"/>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>
    
    <xsl:template name="refFormat"  >
        <xsl:param name="refTokenLength"/>
        <xsl:param name="refStr"/>
        <xsl:param name="refFinal"/>
        
        <xsl:if test="($refTokenLength > 0)">
            <xsl:variable name="refTokenBef"  >
                <xsl:value-of select="substring-before($refStr, ']')"/>
            </xsl:variable>
            <xsl:variable name="refTokenAft"  >
                <xsl:value-of select="substring-after($refStr, ']')"/>
            </xsl:variable>
            <xsl:variable name="refToken3"  >
                <xsl:text>  </xsl:text>
                <xsl:text>&#60;a href="</xsl:text><xsl:value-of select="substring-after($refTokenBef, ' |')"/><xsl:text>" target="_blank"&#62;</xsl:text><xsl:value-of select="substring-before($refTokenBef, ' |')"/><xsl:text>&#60;&#47;a&#62;</xsl:text>
                <xsl:text>  </xsl:text>
            </xsl:variable>
              
            <xsl:variable name="refToken4"  >
                <xsl:value-of select="concat($refFinal, $refToken3 )" />
            </xsl:variable>
            <xsl:choose>
                <xsl:when test="(string-length($refTokenAft) > 0)">
                    <xsl:call-template name="refFormat">
                        <xsl:with-param name="refTokenLength" select="string-length($refTokenAft)"/>
                        <xsl:with-param name="refStr" select="normalize-space($refTokenAft)"/>
                        <xsl:with-param name="refFinal" select="$refToken4"/>
                    </xsl:call-template>
                </xsl:when>
                <xsl:otherwise>
                    <xsl:value-of select="$refToken4"/>
                </xsl:otherwise>
            </xsl:choose>
        </xsl:if>
    </xsl:template>
    
 </xsl:stylesheet>

