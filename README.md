# Demonstrating interaction between z/OS and Watson Machine Learning for z/OS Online Scoring Community Edition

This project intends to demonstrate a call to WMLz OSCE instance by using a REST API from a simple z/OS Java app. 
The purpose is to create a simple and easily deployable scenario that can be used on z/OS to understand concepts.

The model we will use performs a matrix multiplication of a [1,5] input tensor by a [5,1] weight tensor, where all weights are defined with a value of 1. This results in an output tensor shape of [1,1].

For example, with this model, an input tensor of [[1,2,3,4,5]] is multiplied by a weights tensor of [[1],[1],[1],[1],[1]], 
resulting in a value of [[15.]]

Note: since this project is intended for deployment to z/OS, we are avoiding managing dependencies through sub-moduling or Maven/Ant. The intent is to quickly try this project without installing additional software. 
So, will use a Makefile to compile and to run the Java app. The required `.jar` files can be pulled from the references below.

## Deploying an instance of IBM Watson® Machine Learning for z/OS 

WMLz OSCE is available under trial. To deploy an instance, please follow the instructions and documentation found [here](https://www.ibm.com/products/machine-learning-for-zos)

## Updating and Deploying the z/OS Java Application

- First, deploy the project to your host system. This program is intended for Unix environments.
   - Suggestion on how to get GitHub projects to z/OS can be found here: https://github.com/IBM/IBM-Z-zOS
- The following Jar files must be in the project root directory;
   - Apache Commons Logging: [commons-logging-1.1.3.jar](http://archive.apache.org/dist/commons/logging/binaries/)
   - Apache HttpClient: [httpclient-4.5.13.jar](https://repo1.maven.org/maven2/org/apache/httpcomponents/httpclient/4.5.13/)
   - Apache Httpcore: [httpcore-4.4.14.jar](https://repo1.maven.org/maven2/org/apache/httpcomponents/httpcore/4.4.14/)
   - Json Processing API [javax.json-1.0.jar](https://repo1.maven.org/maven2/org/glassfish/javax.json/1.0/)
   - We suggest using *SFTP* to transmit the Jar files to the z/OS environment.
   - Note: If different versions are used, update the Makefile to reference the correct version.
- Run `make` to build compile the `.java` files
- Issue the following command to try issue an IBM WMLz OSCE request:   
   - `make run HOSTIP=wmlz-osce-ip PORT=wmlz-osce-port TOKEN=wmlz-osce-api-token MODEL_DIR=model-dir PAYLOAD=input-tensor`
       - **wmlz-osce-ip** is the IP address of the server or instance hosting WMLz OSCE instance
       - **wmlz-osce-port** is the WMLz OSCE instance REST port
       - **wmlz-osce-api-token** is the API Token generated when deploying WMLz OSCE
       - **model-dir** is the model path, which should is `/models/X` where `x` is a number assigned to the model when it was deployed.
       - **input-tensor** consists of 5 comma separated decimal values. 
       
   For example, if we mapped the WMLz OSCE REST port to 8507, we would use something like this (changing the HOSTIP, TOKEN, and MODEL_DIR 
   to ones in use): `make run HOSTIP=127.0.0.1 PORT=8507 TOKEN=XXXXXXXXXX MODEL_DIR=/models/1 PAYLOAD=1.0,2.0,3.0,4.0,5.0`
   
   Assuming you used the model in this demo for the above make command, the result would be:

   ```
   [{"dims":[1,1], "data":[15.0]}]
   ```
   
