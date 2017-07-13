from django.core.management.base import BaseCommand, CommandError
from sosokan.models import UserProfile , Favorite, Ad
from django.contrib.auth.models import User
from unidecode import unidecode
from django.conf import settings
import requests
import os
import datetime
import time

class Command(BaseCommand):
    help = 'Imports favorites from Firebase'

    def handle(self, *args, **options):
        print "importing users"
        from firebase import firebase
        firebase = firebase.FirebaseApplication('https://sosokan-1452b.firebaseio.com', authentication=None)
        users = firebase.get('/users', None)
        total_users = len(users)
        import_count = 0
        for k,v in users.items():
            if type(v) is dict:
                #print v
                username = v.get('userName')
                if not username:
                    username = v.get('email')
                    if not username:
                        username = v.get('phoneNumber')

                if username:
                    try:
                        user = User.objects.get(username=username[:30])
                           

                        if v.get('favorites',''):
                            for favorite in v.get('favorites',''):
                                try:

                                    obj, created = Favorite.objects.get_or_create(user=user, ad=Ad.objects.get(legacy_id=favorite))
                                    if created:
                                        #print "it was created"
                                except Exception, e:
                                    pass
                                    #print v, e, favorite
                                

                        import_count += 1

                    except Exception, e:
                        print v, e

        if not settings.DEBUG:
            payload={"text": "imported "+str(import_count)+" in environment " + ('development' if settings.DEBUG else os.environ.get('ENVIRONMENT', 'production')) + " total: " +str(Favorite.objects.all().count())}
            webhook_url = "https://hooks.slack.com/services/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"
            response = requests.post(webhook_url, json=payload)

