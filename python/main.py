import os
import sys
import json
import urllib.request
from typing import List,Dict

def process_data( data ):
    """Process the input data"""
    result = []
    for item in data:
        if item>10:
            result.append( item*2 )
    return result

def fetch_url( url ):
    response = urllib.request.urlopen( url )
    return response.read()

def unused_function():
    pass

def main( ):
    numbers=[1,5,15,20,25]
    processed=process_data(numbers)
    print(processed)
    x=10
    y=20
    z=x+y
    print( "Sum: "+str(z) )

if __name__=="__main__":
    main()
