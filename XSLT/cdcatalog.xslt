<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet version="2.0"
xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:uml="http://schema.omg.org/spec/UML/2.2" xmlns:xmi="http://schema.omg.org/spec/XMI/2.1"
xmlns:exslt="http://exslt.org/common">
<!--<xsl:template match="uml:Model">
  <xsl:for-each select="packagedElement[@xmi:type='uml:Class']">
   <xsl:if test="@name = 'ClassC'">
packagedElement:<xsl:value-of select="@xmi:id"/>
      </xsl:if>

  </xsl:for-each>
</xsl:template>-->


<xsl:template match="uml:Model">
MODEL============
<xsl:apply-templates select="packagedElement"/>
<xsl:apply-templates select="profileApplication"/>
<xsl:apply-templates select="packageImport"/>
</xsl:template>



<xsl:template match="packageImport">
packageImport = <xsl:value-of select="@xmi:type"/>, type = <xsl:value-of select="@xmi:type"/>

</xsl:template>

<xsl:template match="packagedElement">
packagedElement = <xsl:value-of select="@xmi:type"/>, type = <xsl:value-of select="@xmi:type"/>
<xsl:apply-templates select="ownedBehavior"/>
</xsl:template>

<xsl:template match="packagedElement[@xmi:type='uml:Class']">
this is a CLASS = <xsl:value-of select="@xmi:type"/>, type = <xsl:value-of select="@xmi:type"/>
<xsl:apply-templates select="ownedBehavior"/>
</xsl:template>

<xsl:template match="profileApplication">
profileApplication = <xsl:value-of select="@xmi:type"/>, type = <xsl:value-of select="@xmi:type"/>
</xsl:template>

<xsl:template match="ownedBehavior">
=================
  ownedBehavior = <xsl:value-of select="@name"/>, type = <xsl:value-of select="@xmi:type"/>
  <xsl:apply-templates select="fragment"/>
   <xsl:apply-templates select="message"/>
       <xsl:apply-templates select="lifeline"/>
=================
</xsl:template>

<xsl:template match="fragment">
    fragment = <xsl:value-of select="@xmi:type"/>
    <xsl:apply-templates select="operand"/>
</xsl:template>


<xsl:template match="operand">
	operand = <xsl:value-of select="@xmi:type"/>
		<xsl:apply-templates select="fragment"/>
</xsl:template>

<xsl:template match="message">
    message = <xsl:value-of select="@xmi:type"/>  name= <xsl:value-of select="@name"/>
</xsl:template>

<xsl:template match="lifeline">
    lifeline = <xsl:value-of select="@xmi:type"/>  name= <xsl:value-of select="@name"/>
</xsl:template>


</xsl:stylesheet>