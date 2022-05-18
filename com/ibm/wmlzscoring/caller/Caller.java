package com.ibm.wmlzscoring.caller;

/*
 * (C) Copyright IBM Corp. 2022.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */

public class Caller {
    public static final String INPUT_REQUIREMENTS = "Required Arguments: host port token modelpath payload";
    public static final String INPUT_EXAMPLE = "Example: make run HOSTIP=x.x.x.x PORT=XXXX TOKEN=XXXXXX-XXXX-XXXXX MODEL_DIR=/models/X PAYLOAD=1.0,2.0,3.0,4.0,5.0";

    /**
    * Our goal here is to provide a simple z/OS Java Appliction that can interface with a WMLz OSCE instance. 
    * 
    * This is intended to demonstrate the use alongside WMLz OSCE, hosted in zOS (USS), zCX or Linux on Z instance - with 
    * the simplemm model found in the project repository.
    * 
    * This uses standard apache http classes to issue rest calls. There may be better approaches to consider depending
    * on your application needs and available products. This includes numerous open-source REST Java packages, 
    * built-in capabilities of CICS and/or Websphere Liberty, and IBM products like z/OS Connect EE. 
    * 
    * simplemm model does the following:
    *    1. takes an input tensor of shape [1,5] 
    *    2. matrix multiply against a weight tensor of shape [5,1] with weights defined as 1.0
    *    3. result is a tensor of shape [1,1]
    * 
    * For example, an input of [1.0,2.0,3.0,4.0,5.0] is multipled against [[1.0],[1.0],[1.0],[1.0],[1.0]]
    * will produce a result of [[15.0]]
    * 
    * @param args  the arguments given to the program. In this case, there are 5.
    */
    public static void main(String[] args) {
        System.out.println("Starting WMLz OSCE Example ");

        if (args.length != 5) {
            System.out.println(INPUT_REQUIREMENTS);
            System.out.println(INPUT_EXAMPLE);
            return;
        }

        Caller app = new Caller();
        app.processRequest(args[0], args[1], args[2], args[3], args[4]);

    }

    /**
    * Processes the request to make a predicition against a target model hosted
    * on a WMLz OSCE instance.
    *
    * @param  host   the ip address of the WMLz OSCE instance
    * @param  port   the port of the WMLz OSCE instance
    * @param  token  the token to access the WMLz OSCE instance
    * @param  name   the location of the image, relative to the url argument
    *                for example: if the url is http://x.x.x.x:XXXX/#/models/2
    *                             the name would be "/models/2"
    */
    private void processRequest(String host, String port, String token, String modelpath, String payload) {
        Session sess = new Session(host, port, token, modelpath);
        String predict = sess.predict(payload);
        System.out.println(predict);
        System.out.println("Processing complete!");
    }

}
