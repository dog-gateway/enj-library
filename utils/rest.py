'''
Created on Apr 4, 2014

@author: bonino

Copyright (c) 2014 Dario Bonino
 
Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0
 
Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License
'''
import urllib2, json

def send(method = 'GET', url=None, data = None, headers = {}):
    #the response dictionary, initially empty
    response_dict = dict()
    
    #check that the url is not empty
    if(url!=None):
        #build the request
        req = urllib2.Request(url, data, headers)
        req.get_method = lambda: method
        
        #try to call the url
        result = None
        try:
            #get the result
            result = urllib2.urlopen(req)
        except urllib2.URLError, e:
            #print the error
            print e.reason
        
        #check result
        if(result != None):
            #decode the result
            result_as_string = result.read().decode('utf8')
            
            if(result_as_string != ''):
                #convert the response into a dictionary
                response_dict = json.loads(result_as_string)
            
    
    return response_dict