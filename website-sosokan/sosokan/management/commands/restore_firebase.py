from django.core.management.base import BaseCommand, CommandError
import time
from django.core.files.storage import default_storage
from django.conf import settings
import pyrebase 
import json
import json
import codecs
import json, ast

timestr = time.strftime("%Y%m%d-%H%M%S")

class Command(BaseCommand):
    help = 'Converts a file for restoring to Firebase'

    def handle(self, *args, **options):

        config = {
          "apiKey": "AIzaSyAo7r6nWAosfSCDISUKlAZOgGhNl_vEjHo",
          "authDomain": "sosokan-1452b.firebaseapp.com",
          "databaseURL": "https://sosokan-1452b.firebaseio.com",
          "storageBucket": "sosokan-1452b.appspot.com",
          "messagingSenderId": "935208892140",
          "serviceAccount": "sosokan-service.json"
        }

        firebase = pyrebase.initialize_app(config)
        db = firebase.database()

        fd = open("backup4.json", 'r')
        text = fd.read()
        
        cb = eval(text)
        json_obj = json.dumps(cb, sort_keys=True, indent=4, separators=(',', ': '))


        fd.close()
        file2 = open('backup3.json', 'w')
        file2.write(json_obj)
        file2.close()
