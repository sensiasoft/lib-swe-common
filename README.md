SWE Common Library (lib-swe-common)
===================================

This open source project aims at building a JAVA API and implementation of the SWE (Sensor Web Enablement) Common Data Model, that can be easily used to access and produce SWE Common Data, and convert in to/from existing formats such as NetCDF, HDF, GeoTIFF, JPEG2000, etc... It can also be used to quickly develop SWE services such as SOS, SPS and SAS which all rely on the SWE Common Data Model.

The project was initiated by the VAST Team at the University of Alabama in Huntsville (UAH) but is now maintained by Sensia Software and Botts Innovative Research, Inc..

Dependencies
============

The following libraries are needed in order to compile the code published on SVN or in the download section:

[Xerces J 2.9.0](http://archive.apache.org/dist/xml/xerces-j) - XML Parser
[Vecmath library] extracted from Java3D 1.4
[JTS 1.9] - Java Topology Library
[StAX API] and a working implementation
[Apache Common Logging 1.1.1] and a logger implementation such as Apache [Log4j 1.2]

For dealing with existing data formats, the following libraries are also needed:

[JAI & ImageIO 1.1] - Java Advanced Imaging (if not already included in your JDK)
[JJ2000 5.1] - JPEG 2000 support
[NetCDF Java Library] - NetCDF data library
