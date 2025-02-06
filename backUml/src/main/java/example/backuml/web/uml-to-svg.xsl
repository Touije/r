<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:uml="http://www.uml.org">
    <xsl:output method="xml" indent="yes"/>

    <!-- Template pour transformer une classe en SVG -->
    <xsl:template match="uml:class">
        <svg width="200" height="200" xmlns="http://www.w3.org/2000/svg">
            <rect x="10" y="10" width="180" height="180" style="fill:white;stroke:black;stroke-width:2"/>
            <text x="20" y="30"><xsl:value-of select="uml:name"/></text>
        </svg>
    </xsl:template>
</xsl:stylesheet>
