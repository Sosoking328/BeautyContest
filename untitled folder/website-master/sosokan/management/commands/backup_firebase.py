from django.core.management.base import BaseCommand, CommandError
import time
from django.core.files.storage import default_storage
from django.conf import settings
import json
import urllib2

timestr = time.strftime("%Y%m%d-%H%M%S")

class Command(BaseCommand):
    help = 'Backup Firebase'

    def handle(self, *args, **options):

        from firebase import firebase
        firebase = firebase.FirebaseApplication('https://sosokan-1452b.firebaseio.com', authentication=None)
        file = default_storage.open('backups/backup_'+timestr+".json", 'w')
        file.write(firebase.get('/', None))
        file.close()

        
        response = urllib2.urlopen('https://sosokan-1452b.firebaseio.com/.json?print=pretty&format=export&download=sosokan-1452b-export.json')
        html = response.read()
        file = default_storage.open('backups/backup_json'+timestr+".json", 'w')
        file.write(html)
        file.close()    

        
