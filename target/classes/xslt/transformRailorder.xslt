<?xml version="1.0" encoding="UTF-8"?>
<?altova_samplexml file:///D:/Users/dingjunjia/Desktop/camel/TestMqMessage2.xml?>
<xsl:stylesheet version="2.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:xs="http://www.w3.org/2001/XMLSchema"
	xmlns:fn="http://www.w3.org/2005/xpath-functions">
	<xsl:output method="xml" version="1.0" encoding="UTF-8"
		indent="yes" />
	<xsl:strip-space elements="*" />
	<xsl:variable name="format">
		<xsl:copy-of select="document('format.xml')" />
	</xsl:variable>
	<xsl:variable name="offset" select="660" />
	<xsl:variable name="order" select="/order" />
	<xsl:template match="/">
		<xsl:element name="{$format/MessageFormat/@name}">
			<!-- <xsl:value-of select="$format/MessageFormat/@name"></xsl:value-of> -->
			<xsl:element
				name="{$format/MessageFormat/FieldFormat/@name}">
				<!--<xsl:value-of select="$format/MessageFormat/FieldFormat/@length" 
					/> -->
				<!--<xsl:value-of select="substring(/order, 660, 18)"/> -->
				<xsl:value-of
					select="fn:substring(/order, $offset, number($format/MessageFormat/FieldFormat/@length))" />
			</xsl:element>
			<xsl:call-template name="StructFormat" />
		</xsl:element>
	</xsl:template>

	<xsl:template name="StructFormat">
		<xsl:for-each select="$format/MessageFormat/StructFormat">
			<xsl:variable name="structFormat" select="./@name" />
			<xsl:element name="{./@name}">
				<xsl:for-each
					select="$format/MessageFormat/StructFormat[./@name=$structFormat]/FieldFormat">
					<xsl:variable name="nodeName" select="./@name" />
					<xsl:element name="{./@name}">
						<xsl:value-of
							select="normalize-space(substring($order, ($offset + sum($format/MessageFormat/StructFormat[./@name=$structFormat]/FieldFormat[./@name = $nodeName]/preceding::FieldFormat/@length)), ./@length))" />
					</xsl:element>
				</xsl:for-each>
			</xsl:element>
		</xsl:for-each>
	</xsl:template>
</xsl:stylesheet>
