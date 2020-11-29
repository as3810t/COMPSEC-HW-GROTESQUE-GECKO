# CAFF Store

This repository contains the Computer Security homework of team Grotesque Gecko (fall of 2020)!

## Native component

[![Quality gate](https://sonarcloud.io/api/project_badges/quality_gate?project=as3810t_COMPSEC-HW-GROTESQUE-GECKO-native)](https://sonarcloud.io/dashboard?id=as3810t_COMPSEC-HW-GROTESQUE-GECKO-native)

This component is implemented using C/C++ and its goal is to parse [CAFF files](https://www.crysys.hu/downloads/vihima06/2020/CAFF.txt). The sources for the component can be found in the [native](https://github.com/as3810t/COMPSEC-HW-GROTESQUE-GECKO/tree/main/native) folder.

The component has a Java binding that will enable the server side code to call the CAFF parser. The sources or the Java binding can be found in the [native-component](https://github.com/as3810t/COMPSEC-HW-GROTESQUE-GECKO/tree/main/native-component) folder.

## Server side application



This component is implemented in Java using Spring and Hibernate and its goal is to provide an API for the CAFF Store. The sources for the component can be found in the [backend](https://github.com/as3810t/COMPSEC-HW-GROTESQUE-GECKO/tree/main/backend) folder. 
