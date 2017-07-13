from django.core.management.base import BaseCommand, CommandError
from sosokan.models import Ad, AdImage, Category, User
import datetime
from unidecode import unidecode
import time
from django.conf import settings
from urlparse import urlparse
import urllib
from geoposition import Geoposition
from decimal import Decimal
from django.contrib.gis.geos import GEOSGeometry
import requests
import os

FIREBASE_URL="https://firebasestorage.googleapis.com/v0/b/sosokan-1452b.appspot.com/o/images%2F"
class Command(BaseCommand):
    help = 'Resets Ads from Firebase'

    def handle(self, *args, **options):
        #Ad.objects.all().delete()
        #AdImage.objects.all().delete()
        from firebase import firebase
        print "Getting Ads from Firebase..."
        firebase = firebase.FirebaseApplication('https://sosokan-1452b.firebaseio.com', authentication=None)
        print "Connected"
        ads = firebase.get('/advertises/', None)
        print "Processing..."
        counter = 0
        for k,v in ads.items():
            if v:
                try:
                    counter+=1
                    print counter
                    print "importing ", k,  unidecode(v.get('name','')), unidecode(v.get('categoryId','category_id_blank'))

                    geo_location = firebase.get('/geoFire/'+k, None)
                    
                    latitude = float(v.get('latitude',0))
                    longitude = float(v.get('longitude',0))
                    position = None
                    location = None
                    
                    if geo_location:
                        if geo_location.get('l'):
                            latitude = geo_location.get('l')[0]
                            longitude= geo_location.get('l')[1]
                            position = Geoposition(float(latitude), float(longitude))
                            location = GEOSGeometry('POINT(%s %s)' % (longitude, latitude))

                    obj, created = Ad.objects.update_or_create(
                        legacy_id = v.get('id'), defaults= {
                        'category': Category.objects.filter(legacy_id=v.get('categoryId')).first(),
                        'user': User.objects.filter(userprofile__legacy_id=v.get('userId','')).first(),
                        'address': v.get('Address'),
                        'advertiseId': v.get('advertiseId'),
                        'categoryId': v.get('categoryId'),
                        'chinese': v.get('chinese', False),
                        'coupon': v.get('coupon'),
                        'created_on': datetime.datetime.utcfromtimestamp(v.get('createdAt',time.time())).strftime('%Y-%m-%d %H:%M:%SZ'),
                        'createdAt': v.get('createdAt', time.time()),
                        'descendingTime': v.get('descendingTime',-time.time()),
                        'description': v.get('description',''),
                        'descriptionPlainText': v.get('descriptionPlainText'),
                        'enableEmail': v.get('enableEmail', False),
                        'enablePhone': v.get('enablePhone', False),
                        'favoriteCount': int(v.get('favoriteCount','0')),
                        
                        'feature': v.get('feature'),
                        'flagCount': int(v.get('flagCount','0')),
                        'hidden': v.get('hidden', False),
                        'legacy_id': v.get('id'),
                        'isChinese': v.get('isChinese', False),
                        'language': v.get('isChinese', False) and "zh-hans" or "en",
                        'isFeatured': v.get('isFeature', False),
                        'isHtmlDes': v.get('isHtmlDes', True),
                        'isStandout': v.get('isStandout', False),

                        'position': position,
                        'location': location,

                        'name': v.get('name', '-'),
                        'price': float(v.get('price',0)),
                        'saleOff': int(v.get('saleOff','0')),
                        'shareCount': int(v.get('shareCount','0')),
                        'standout': v.get('standout',False),
                        'updatedAt': v.get('createdAt', time.time()),
                        'userId': v.get('userId','')}
                    )
                    if v.get('images'):
                        for fb_image in v.get('images'):
                            print fb_image
                            image = firebase.get('/images/'+fb_image, None)

                            if image:
                                if settings.MEDIA_URL in image.get('imageUrl'):
                                    the_image = urllib.unquote(image.get('imageUrl').replace(settings.MEDIA_URL,""))
                                elif FIREBASE_URL in image.get('imageUrl'):
                                    o = urlparse(image.get('imageUrl'))
                                    url_without_query_string = o.scheme + "://" + o.netloc + o.path
                                    the_image = "images/"+urllib.unquote(url_without_query_string.replace(FIREBASE_URL,""))
                                else:
                                    print "--------------"
                                if type(the_image) == unicode:
                                    the_image = the_image.encode('latin-1').decode('utf-8')
                                img_obj, img_created = AdImage.objects.update_or_create(
                                    ad=obj,
                                    image = the_image,
                                    createdAt = image.get('createdAt'),
                                    descendingTime = image.get('descendingTime'),
                                    height = image.get('height'),
                                    width = image.get('width'),
                                    legacy_id = image.get('id'),
                                    imageUrl = image.get('imageUrl'),
                                    isStoredInStorage = image.get('isStoredInStorage', False),
                                    isVideoThumb = image.get('isVideoThumb', False),
                                    status = image.get('status'),
                                    updatedAt = image.get('updatedAt'),
                                    userId = image.get('userId'),
                                )
                except Exception:
                    pass
        payload={"text": "imported "+str(counter)+" ads from "+str(counter) + " in environment " + ('development' if settings.DEBUG else os.environ.get('ENVIRONMENT', 'production')) + " total: " +str(Category.objects.all().count())}
        webhook_url = "https://hooks.slack.com/services/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"
        response = requests.post(webhook_url, json=payload)

