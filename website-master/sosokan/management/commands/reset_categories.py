from django.core.management.base import BaseCommand, CommandError
from sosokan.models import Category, CategoryImage, Banner
import datetime
from django.utils.text import slugify 
from django.conf import settings
from unidecode import unidecode
import json
import urllib
import time
from urlparse import urlparse
import requests
import os

FIREBASE_URL="https://firebasestorage.googleapis.com/v0/b/sosokan-1452b.appspot.com/o/"

class Command(BaseCommand):
    help = 'Reset categories from Firebase'

    def handle(self, *args, **options):
        print "resetting categories"
        banners = Banner.objects.all()
        banner_array={}
        for banner in banners:
            if banner.category:
                banner_array[banner.id]=banner.category.slug

        print "BACKED UP BANNERS"        
        Category.objects.all().delete()
        print "categories deleted"        
        CategoryImage.objects.all().delete()
        print "deleted categories"
        from firebase import firebase
        firebase = firebase.FirebaseApplication('https://sosokan-1452b.firebaseio.com', authentication=None)
        categories = firebase.get('/categories', None)
        total_categories = len(categories)
        import_count = 0
        print "about to import"

        for k,v in categories.items():
            if v and v.get('name'):
                print "importing ",unidecode(k)
                obj, created = Category.objects.update_or_create(
                    name = v.get('name','-'),
                    legacy_id = v.get('id',''),
                    defaults={
                    'nameChinese': v.get('nameChinese'),
                    'slug': slugify(v.get('name')),
                    'advertiseCount': int(v.get('advertiseCount',0)),
                    'createdAt': v.get('createdAt',''),
                    'deepLevel': int(v.get('deepLevel',0)),
                    'parentId': v.get('parentId'),
                    'popular': int(v.get('popular',0)),
                    'sort': int(v.get('sort',0)),
                    'updatedAt': v.get('updatedAt')}
                )
                if v.get('icons'):
                    if 'iconEnglish' in v.get('icons'):
                        o = urlparse(v.get('icons')['iconEnglish'].get('imageUrl'))
                        url_without_query_string = o.scheme + "://" + o.netloc + o.path
                        the_image = urllib.unquote(url_without_query_string.replace(FIREBASE_URL,""))

                        img_obj, img_created = CategoryImage.objects.update_or_create(
                            category=obj,
                            image = the_image,
                            language = "en",
                            createdAt = v.get('icons')['iconEnglish'].get('createdAt', time.time()),
                            descendingTime = v.get('icons')['iconEnglish'].get('descendingTime',-time.time()),
                            legacy_id = v.get('icons')['iconEnglish'].get('id'),
                            imageUrl = v.get('icons')['iconEnglish'].get('imageUrl'),
                            updatedAt = v.get('icons')['iconEnglish'].get('updatedAt', time.time()),
                        )
                    if 'iconChinese' in v.get('icons'):
                        o = urlparse(v.get('icons')['iconChinese'].get('imageUrl'))
                        url_without_query_string = o.scheme + "://" + o.netloc + o.path
                        the_image = urllib.unquote(url_without_query_string.replace(FIREBASE_URL,""))

                        img_obj, img_created = CategoryImage.objects.update_or_create(
                            category=obj,
                            image = the_image,
                            language = "zh-hans",
                            createdAt = v.get('icons')['iconChinese'].get('createdAt', time.time()),
                            descendingTime = v.get('icons')['iconChinese'].get('descendingTime',-time.time()),
                            legacy_id = v.get('icons')['iconChinese'].get('id'),
                            imageUrl = v.get('icons')['iconChinese'].get('imageUrl'),
                            updatedAt = v.get('icons')['iconChinese'].get('updatedAt', time.time()),
                        )
                import_count += 1

        for category in Category.objects.all():
            if category.parentId:
                print unidecode(category.legacy_id), category.parentId
                category.parent = Category.objects.get(legacy_id=category.parentId)
                category.save()
        
        for banner2 in banner_array:
            banner =  Banner.objects.get(id=banner2)
            banner.category = Category.objects.get(slug=banner_array[banner2])
            banner.save()

        payload={"text": "imported "+str(import_count)+" categories from "+str(total_categories) + " in environment " + ('development' if settings.DEBUG else os.environ.get('ENVIRONMENT', 'production')) + " total: " +str(Category.objects.all().count())}
        webhook_url = "https://hooks.slack.com/services/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"
        response = requests.post(webhook_url, json=payload)