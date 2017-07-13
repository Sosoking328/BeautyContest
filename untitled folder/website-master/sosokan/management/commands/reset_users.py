# -*- coding: utf-8 -*-
from django.core.management.base import BaseCommand, CommandError
from sosokan.models import UserProfile 
from django.contrib.auth.models import User
from unidecode import unidecode
from django.conf import settings
import requests
import os
import datetime
import time

class Command(BaseCommand):
    help = 'Imports users from Firebase'
    # TODO: Import Ratings and Favorites, conversations, followingUsers
    def handle(self, *args, **options):
        print "importing users"
        from firebase import firebase
        firebase = firebase.FirebaseApplication('https://sosokan-1452b.firebaseio.com', authentication=None)
        users = firebase.get('/users', None)
        total_users = len(users)
        import_count = 0
        users_created = 0
        profiles_created = 0

        for k,v in users.items():
            if type(v) is dict:
                #print v
                username = v.get('userName')
                if not username:
                    username = v.get('email')
                    if not username:
                        username = v.get('phoneNumber')

                if username:
                    print unidecode(username)
                    try:

                        # date_joined = datetime.datetime.utcfromtimestamp(v.get('createdAt',time.time())).strftime('%Y-%m-%d %H:%M:%SZ')
                        # print date_joined

                        obj, created = User.objects.update_or_create(
                            username = username[:30],
                            defaults={'email': v.get('email','')}
                        )
                        if created:
                            users_created += 1

                    except Exception, e:
                        print v, e
                    if v.get('avatar',""):
                        avatar_url=v.get('avatar',"")['imageUrl']
                    else:
                        avatar_url=''
                    try:
                        obj, created = UserProfile.objects.update_or_create(
                            user = obj,
                            defaults= {
                            'display_name': v.get('userName','-'),
                            'emailAble':  v.get('emailAble', False),
                            'address':  v.get('address',''),
                            'city':  v.get('city',''),
                            'companyName':  v.get('companyName',''),
                            'credit':  float(v.get('credit',0)),
                            'image':  avatar_url,
                            'faxNumber':  v.get('faxNumber',''),
                            'legacy_id':  k,
                            'myAdvertiseCount':  int(v.get('myAdvertiseCount',0)),
                            'note':  v.get('note',''),
                            'phoneNumber':  v.get('phoneNumber',''),
                            'callAble':  v.get('callAble', False),
                            'role':  v.get('role',''),
                            'website':  v.get('website',''),
                            'state':  v.get('state',''),
                            'zip':  v.get('zip','')}
                        )
                        if created:
                            profiles_created += 1
                        
                        import_count += 1

                    except Exception, e:
                        print v, e

        
        payload={"text": "profiles_created: " + str(profiles_created) + "users_created: " + str(users_created) + " imported "+str(import_count)+" \
            users from "+str(total_users) + " in environment \
            " + ('development' if settings.DEBUG else os.environ.get('ENVIRONMENT', 'production')) + " total: " +str(User.objects.all().count())}
        print payload
        if not settings.DEBUG:
            webhook_url = "https://hooks.slack.com/services/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"
            response = requests.post(webhook_url, json=payload)

