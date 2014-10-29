SWE Common Library (lib-swe-common)
===================================

This open source project aims at building a JAVA API and implementation of the SWE (Sensor Web Enablement) Common Data Model, that can be easily used to access and produce SWE Common Data, and convert in to/from existing formats such as NetCDF, HDF, GeoTIFF, JPEG2000, etc... It can also be used to quickly develop SWE services such as SOS, SPS and SAS which all rely on the SWE Common Data Model.

The project was initiated by the VAST Team at the University of Alabama in Huntsville (UAH) but is now maintained by Sensia Software and Botts Innovative Research, Inc..

Dependencies
============

The following libraries are needed in order to compile the code published on SVN or in the download section:

* [Xerces J 2.9.0](http://archive.apache.org/dist/xml/xerces-j) - XML Parser
* [Vecmath library](http://swe-common-users.googlegroups.com/web/vecmath.jar?gda=UhEE7z0AAABmV-nEGeyOAwjBq61_Gv7ev2ireQPZzIye8CVrvhPv8_mwBFKd3SXrNiFzfP3zubLlNv--OykrTYJH3lVGu2Z5&gsc=dpJJIwsAAAAKbx6IcGny51zyzM-9xWCv) extracted from [Java3D 1.4](https://java3d.dev.java.net/)
* [JTS 1.9](http://sourceforge.net/project/showfiles.php?group_id=128875&package_id=141251 JTS 1.9) - Java Topology Library
* [StAX API](http://stax.codehaus.org/) and a working implementation
* [Apache Common Logging 1.1.1](http://commons.apache.org/downloads/download_logging.cgi) and a logger implementation such as Apache [Log4j 1.2](http://logging.apache.org/log4j/1.2/download.html)

For dealing with existing data formats, the following libraries are also needed:

* [JAI & ImageIO 1.1](https://jai-imageio.dev.java.net/binary-builds.html) - Java Advanced Imaging (if not already included in your JDK)
* [JJ2000 5.1](http://jpeg2000.epfl.ch/) - JPEG 2000 support
* [NetCDF Java Library](http://www.unidata.ucar.edu/software/netcdf-java/) - NetCDF data library
