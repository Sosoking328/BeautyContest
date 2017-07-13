from django.views.generic import TemplateView, View, DetailView, CreateView, ListView
from django.http import HttpResponse
from sosokan.models import Category, Ad, UserProfile, AdImage
from django.utils.translation import get_language
from django.http import HttpResponse
from django.shortcuts import render, redirect
from django.contrib.auth import get_user_model
from django.contrib import messages
from django.db.models import Q
from rest_framework.decorators import api_view
from rest_framework.response import Response
from rest_framework import permissions
from django.middleware.csrf import get_token
import requests
from django.views.decorators.csrf import csrf_exempt
from rest_framework.views import APIView
from serializers import CsrfExemptSessionAuthentication
from django.core import serializers
from django.contrib.auth.models import User
from django.contrib.auth import login
from rest_framework.authtoken.models import Token
import os
from rest_framework.exceptions import APIException
from django_comments.models import Comment
from updown.views import AddRatingFromModel
import json
import pytz
import time
from django.conf import settings
import urllib
import urllib2
from BeautifulSoup import BeautifulSoup
from rest_framework.generics import get_object_or_404
from updown.exceptions import InvalidRating, AuthRequired, CannotChangeVote
from django.utils.html import strip_tags
import pyrebase
import python_jwt as jwt  
import Crypto.PublicKey.RSA as RSA  
import re
import datetime
from django.http import HttpResponseRedirect
from django.contrib.gis.geos import GEOSGeometry
from geoposition.fields import Geoposition
from django.core.files.storage import default_storage
from django.core.files.base import ContentFile
from django.core.files import File
from django.http import Http404
from django.template import loader
from django.core.paginator import Paginator, EmptyPage, PageNotAnInteger
from django.http import JsonResponse
from django.utils import timezone
from django.views.generic.edit import DeleteView
from django.core.urlresolvers import reverse_lazy

class PhoneLogin(TemplateView):
    template_name="account/phone_login.html"

    def post(self, request):
        context = self.get_context_data()
        if request.POST.get('code'):
            data = {
            'api_key':'925f2dcf5370e76a26be65e7e3c0a7b4a1094a27', 
            'code': request.POST.get('code'),
            'phone': request.POST.get('phone')
            }
            r = requests.post('https://api.ringcaptcha.com/ymo7o5ara1y3y2ymato2/verify', data=data)
            json_response = r.json()
            user = None
            if r.json()['status'] == "SUCCESS":
                try:
                    user_profile = UserProfile.objects.get(phoneNumber=request.POST.get('phone'))
                    user = user_profile.user
                    user.backend = 'django.contrib.auth.backends.ModelBackend'
                    user.save()
                except Exception, e:
                    user, created = User.objects.get_or_create(username=request.POST.get('phone'))
                    user.backend = 'django.contrib.auth.backends.ModelBackend'
                    user.save()
                login(request, user)
                token, created = Token.objects.get_or_create(user=user)
                messages.success(self.request, "logged in as: "+user.username)
                return HttpResponseRedirect("/")
            else:
                messages.error(self.request, r.json()['message'])
                return super(PhoneLogin, self).post(request, *args, **kwargs)

        else:
            if not request.POST.get('countryCode'):
                messages.error(self.request, 'Country Code is missing')
                return super(PhoneLogin, self).post(request, *args, **kwargs)
            if not request.POST.get('phone'):
                messages.error(self.request, 'Phone number is missing')
                return super(PhoneLogin, self).post(request, *args, **kwargs)
            
            phone_number = "+"+re.sub('[^0-9]','', request.POST.get('countryCode')+request.POST.get('phone'))

            data = {
            'api_key':'925f2dcf5370e76a26be65e7e3c0a7b4a1094a27', 
            'app_key':'ymo7o5ara1y3y2ymato2',
            'phone': phone_number,
            }
            r = requests.post('https://api.ringcaptcha.com/ymo7o5ara1y3y2ymato2/code/SMS', data=data)
            json_response = r.json()
            user = None 
            
            if json_response['status'] == "ERROR":
                context['message'] = json_response['message']
            
            context['status'] = json_response['status']
            
            context['phone'] = phone_number
        return super(PhoneLogin, self).render_to_response(context)


class ShareView(View):
    def get(self, request):
        soup = BeautifulSoup(urllib2.urlopen(request.GET.get('url')))
        description = soup.find('meta', attrs={'name':'og:description'}) or soup.find('meta', attrs={'property':'description'}) or soup.find('meta', attrs={'name':'description'})
        if description:
            page_description = description.get('content')
        else:
            page_description = ""
        og_image = soup.find('meta', attrs={'property': 'og:image'})
        
        if og_image:
            page_image = og_image.get('content')
        else:
            link = soup.find(itemprop="image")
            if link:
                page_image = request.GET.get('url')+ link.get('content')
            else:
                page_image = ''
        Ad.objects
        obj = Ad.objects.create(name=soup.title.string, description=page_description, user=request.user)
        if obj and page_image:
            adimage = AdImage.objects.create(ad=obj)
            
            image_content = ContentFile(requests.get(page_image).content)
            adimage.image.save(self.name, image_content)
            

        print obj.id
        return HttpResponse(
            "shared: " +request.GET.get('url') + 
            "title: "+ soup.title.string + 
            "description: "+ page_description + 
            "image: <img src='"+page_image+ "'>"
            )

class Upload(View):
    def post(self, request):
        data = request.FILES.get('file[]')
        result = default_storage.save("files\/" + request.FILES.get('file[]').name, ContentFile(data.read()))
        return HttpResponse(request.FILES.get('file[]').name)

class HomeView(TemplateView):
    template_name = "index.html"

    def get_context_data(self, **kwargs):
        context = super(HomeView, self).get_context_data(**kwargs)
        context['categories'] = Category.objects.all().filter(popular__gte=80).order_by('-popular')

        if get_language() == "en":
        	context['ads'] = Ad.objects.exclude(name="-").exclude(chinese=True).exclude(created_on__gte=timezone.now()).exclude(isChinese=True)[:30]
        else:
        	context['ads'] = Ad.objects.exclude(name="-").exclude(chinese=False).exclude(created_on__gte=timezone.now()).exclude(isChinese=False)[:30]

        
        return context


def lazy_load_posts(request):
  page = request.POST.get('page')


  if get_language() == "en":
    posts = Ad.objects.exclude(name="-").exclude(chinese=True).exclude(created_on__gte=timezone.now()).exclude(isChinese=True)
  else:
    posts = Ad.objects.exclude(name="-").exclude(chinese=False).exclude(created_on__gte=timezone.now()).exclude(isChinese=False)

  # use Django's pagination
  # https://docs.djangoproject.com/en/dev/topics/pagination/
  results_per_page = 30
  paginator = Paginator(posts, results_per_page)
  try:
    posts = paginator.page(page)
  except PageNotAnInteger:
    posts = paginator.page(2)
  except EmptyPage:
    posts = paginator.page(paginator.num_pages)
        
  # build a html posts list with the paginated posts
  posts_html = loader.render_to_string('posts.html', {'ads': posts})
  
  # package output data and return it as a JSON object
  output_data = {'posts_html': posts_html, 'has_next': posts.has_next()}
  return JsonResponse(output_data)

class AdListView(ListView):
    ordering = "created_on"
    context_object_name = "ads"
    model = Ad
    template_name = "list.html"


    def get_queryset(self, *args, **kwargs):
        keyword = self.request.GET.get("keyword", None)
        category = self.request.GET.get("category", None)
        radius = self.request.GET.get("radius", None)
        filters = Q()
        if keyword:
             filters = filters | Q(name__icontains=keyword)
        
        if get_language() == "en":
            ads = self.model.objects.all().filter(filters).exclude(chinese=True).exclude(isChinese=True).exclude(created_on__gte=timezone.now()).order_by(self.ordering)
        else:
            ads = self.model.objects.all().filter(filters).exclude(chinese=False).exclude(isChinese=False).exclude(isChinese=True).exclude(created_on__gte=timezone.now()).order_by(self.ordering)
        if category:
            ads = ads.filter(category__slug=category)


        return ads

    def get_context_data(self, *args, **kwargs):
        context = super(AdListView, self).get_context_data(**kwargs)
        context['keyword'] = self.request.GET.get('keyword')
        context['categories'] = Category.objects.all().filter(popular__gte=80).order_by('-popular')
        return context


class iOSView(View):

    def get(self, request):
        if "micromessenger" in request.META.get('HTTP_USER_AGENT').lower():
            from PIL import Image
            img = Image.open("sosokan/static/images/open-in-safari.png")
            response = HttpResponse(content_type="image/png")
            img.save(response, "PNG")
            return response
        else:
            response = HttpResponse("", status=302)
            response['Location'] = "https://itunes.apple.com/us/app/sosokan/id1023454229"
        return response

class AndroidView(View):

    def get(self, request):
        if "micromessenger" in request.META.get('HTTP_USER_AGENT').lower():
            from PIL import Image
            img = Image.open("sosokan/static/images/open-in-safari.png")
            response = HttpResponse(content_type="image/png")
            img.save(response, "PNG")
            return response
        else:
            response = HttpResponse("", status=302)
            response['Location'] = "https://play.google.com/store/apps/details?id=com.sosokan.android"
        return response

class AppleView(TemplateView):
    template_name = "redirect_to_app_store.html"

    def get(self, request, slug, *args, **kwargs):
        if "micromessenger" in request.META.get('HTTP_USER_AGENT').lower():

            from PIL import Image

            img = Image.open("sosokan/static/images/open-in-safari.png")

            response = HttpResponse(content_type="image/png")
            img.save(response, "PNG")
            return response
        return super(AppleView, self).get(request, *args, **kwargs)

    def get_context_data(self, *args, **kwargs):
        context = super(AppleView, self).get_context_data(**kwargs)
        context['link'] = self.kwargs['slug']
        return context

def profile(request):
    try:
        return redirect('/profile/' + request.user.username)
    except Exception:
        return redirect('/')

def static(request, slug):
     return render(request, slug+"-"+get_language()+".html")

class UserProfileDetailView(DetailView):
    model = get_user_model()
    slug_field = "username"
    template_name = "profile.html"

    def get(self, request, *args, **kwargs):
        try:
            self.object = self.get_object()
        except Http404:
            messages.error(self.request, 'That user was not found.')
            return redirect("/")
        return super(UserProfileDetailView, self).get(request, *args, **kwargs)

    def get_context_data(self, **kwargs):
        context = super(UserProfileDetailView, self).get_context_data(**kwargs)
        return context

class PostAdView(CreateView):
    template_name = "post.html"
    model = Ad
    fields = ['name','category','price','description','address']

    def form_valid(self, form):
        obj = form.save(commit=False)
        obj.user = self.request.user
        obj.save()      


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

        try:
            category_id = obj.category.legacy_id
        except Exception:
            category_id = ""

        obj.descriptionPlainText = strip_tags(obj.description) 

        if self.request.POST.get('latitude') and self.request.POST.get('longitude'):
            obj.location = GEOSGeometry('POINT(%s %s)' % (self.request.POST.get('longitude'), self.request.POST.get('latitude')))
            obj.position = Geoposition(self.request.POST.get('latitude'),self.request.POST.get('longitude'))

        try:
            userId = obj.user.userprofile.legacy_id or obj.userId
        except Exception:
            userId = obj.userId
        timestamp = time.mktime(pytz.timezone("America/New_York").localize(datetime.datetime.now(), is_dst=None).timetuple())


        data = {
            "name": obj.name,
            "Address": obj.address,
            "categoryId": category_id,
            "chinese": obj.chinese and True or False,
            "coupon": obj.coupon,
            "createdAt": timestamp,
            "descendingTime": obj.descendingTime,
            "description": obj.description,
            "descriptionPlainText": obj.descriptionPlainText,
            "enableEmail": obj.enableEmail and True or False,
            "enablePhone": obj.enablePhone and True or False,
            "favoriteCount": obj.favoriteCount,
            "feature": obj.feature and True or False,
            "flagCount": obj.flagCount,
            "isChinese": obj.isChinese and True or False,
            "isFeature": obj.isFeatured and True or False,
            "isHtmlDes": obj.description and True or False,
            "hidden": obj.hidden and True or False,
            "isStandout": obj.isStandout,
            "price": float(obj.price or 0),
            "saleOff": obj.saleOff,
            "standout": obj.standout and True or False,
            "updatedAt": timestamp,
            "userId": userId,
            "platform": ('development' if settings.DEBUG else os.environ.get('ENVIRONMENT', 'production'))
            }

        try:
            if obj.category.legacy_id:

                obj.legacy_id = db.child("advertises").push(data)['name']
                db.child("advertises").child(obj.legacy_id).update({'id':obj.legacy_id})
                obj.categoryId = obj.category.legacy_id

                category_count = db.child("categories").child(obj.category.legacy_id).child('advertiseCount').get()
                db.child("categories").child(obj.category.legacy_id).update({'advertiseCount':(category_count.val() or 0)+1})
                
                if obj.category.deepLevel != 0:
                    all_count = db.child("categories").child('sosokanCategoryAll').child('advertiseCount').get()
                    db.child("categories").child('sosokanCategoryAll').update({'advertiseCount':all_count.val()+1})
     
                db.child("categories").child(obj.category.legacy_id).child('advertises'+(obj.isChinese and "Chinese" or "English")).update({obj.legacy_id:obj.descendingTime})
                
                if obj.category.deepLevel != 0:
                    db.child("categories").child('sosokanCategoryAll').child('advertises'+(obj.isChinese and "Chinese" or "English")).update({obj.legacy_id:obj.descendingTime})

                if obj.category.parentId:
                    db.child("categories").child(obj.category.parentId).child('advertises'+(obj.isChinese and "Chinese" or "English")).update({obj.legacy_id:obj.descendingTime})
                
                meta = {
                    "advertiseId": obj.legacy_id,
                    "ascendingTime": obj.createdAt,
                    "descendingTime": obj.descendingTime,
                    "id": obj.legacy_id,
                    "stringData": obj.name + " " + obj.descriptionPlainText,
                }
                db.child("advertiseMetas").push(meta)

                if self.request.POST.get('latitude') and self.request.POST.get('longitude'):
                    geoFire = {
                        "g": 'testdata',
                        "l": {
                            "0":  float(self.request.POST.get('latitude')),
                            "1":  float(self.request.POST.get('longitude')),
                        }
                    }
                    db.child("geoFire").update({obj.legacy_id:geoFire})
                    data = {
                        "location": {       
                            "latitude": float(self.request.POST.get('latitude')),     
                            "longitude": float(self.request.POST.get('longitude')),       
                        }
                    }
                    db.child("advertises").child(obj.legacy_id).update(data)
        except AttributeError:
            messages.error(self.request, 'Category Legacy ID is blank '+str(obj.category))
            return 

        obj.save()
        for afile in self.request.POST.getlist('parent_id[]'):
            reopen = default_storage.open('files\/'+ afile, 'rb')
            django_file = File(reopen)
            AdImage(image=django_file, ad=obj).save()
        messages.success(self.request, 'Saved Ad '+str(obj.legacy_id))

        return super(PostAdView, self).form_valid(form)
  
    def get_context_data(self, **kwargs):
        context = super(PostAdView, self).get_context_data(**kwargs)
        context['categories'] = Category.objects.all()
        return context


class AdView(DetailView):
    model = Ad
    slug_field = "id"

    def get_template_names(self):
        if self.request.is_ajax:
            return "detail_ajax.html"
        else:
            return "detail.html"

    def get_context_data(self, **kwargs):
        from firebase import firebase
        ad = Ad.objects.get(pk=self.kwargs['pk'])
        firebase = firebase.FirebaseApplication('https://sosokan-1452b.firebaseio.com', authentication=None)
        views = firebase.get('/views/'+ad.legacy_id, None) or 0

        config = {
          "apiKey": "AIzaSyAo7r6nWAosfSCDISUKlAZOgGhNl_vEjHo",
          "authDomain": "sosokan-1452b.firebaseapp.com",
          "databaseURL": "https://sosokan-1452b.firebaseio.com",
          "storageBucket": "sosokan-1452b.appspot.com",
          "messagingSenderId": "935208892140",
          "serviceAccount": "sosokan-service.json"
        }

        firebase_put = pyrebase.initialize_app(config)
        db = firebase_put.database()
        total_views = int(views) + 1
        db.child('/views/').update({ad.legacy_id:total_views})
        ad.views = total_views
        ad.save()
        context = super(AdView, self).get_context_data(**kwargs)
        context['related_ads'] = ""
        return context


class APIPhoneLogin(APIView):
    """Send phone 

    (with country code) and code received from ringcaptcha
    it will return user_id, username, csrftoken and key"""
    authentication_classes = (CsrfExemptSessionAuthentication,)
    permission_classes = (permissions.AllowAny,)

    def post(self, request, format=None):
        if not request.data.get('code'):
            raise APIException("4 digit 'code' is required.")
        if not request.data.get('phone'):
            raise APIException("Phone number is reuqired")
        data = {
        'api_key':'925f2dcf5370e76a26be65e7e3c0a7b4a1094a27', 
        'code': request.data.get('code'),
        'phone': request.data.get('phone')
        }
        r = requests.post('https://api.ringcaptcha.com/ymo7o5ara1y3y2ymato2/verify', data=data)
        json_response = r.json()
        user = None
        if r.json()['status'] == "SUCCESS":
            try:
                user_profile = UserProfile.objects.get(phoneNumber=request.data['phone'])
                user = user_profile.user
                user.backend = 'django.contrib.auth.backends.ModelBackend'
                user.save()
            except Exception, e:
                user, created = User.objects.get_or_create(username=request.data['phone'])
                user.backend = 'django.contrib.auth.backends.ModelBackend'
                user.save()
            login(request, user)
        if user:
            token, created = Token.objects.get_or_create(user=user)
            json_response.update({
                'user_id': user.id,
                'username': user.username or "",
                'csrftoken': get_token(request),
                'key': token.key,
                })

        return Response(json_response)




class AddRatingAPI(APIView):
  
    def post(self, request, *args, **kwargs):
        params = {
            'app_label': 'django_comments',
            'model': 'Comment',
            'field_name': 'rating'
        }
        params.update(kwargs)
        response = AddRatingFromModel()(request, **params)

        if response.status_code == 200:

            return HttpResponse(Comment.objects.get(id=kwargs['object_id']).rating.likes)
        return HttpResponse(json.dumps({'error': 9, 'message': response.content}))
  
    def get(self, request, *args, **kwargs):
        return self.post(request, *args, **kwargs)


class SyncAd(APIView):
    """Send the firebase id as "ad_id", and it will return "success" if the ad is synced to postgresql"""
    authentication_classes = (CsrfExemptSessionAuthentication,)
    permission_classes = (permissions.AllowAny,)

    
    def post(self, request, *args, **kwargs):
        from firebase import firebase
        from urlparse import urlparse
        firebase = firebase.FirebaseApplication('https://sosokan-1452b.firebaseio.com', authentication=None)
        ad_id = request.data.get('ad_id')
        ad = firebase.get('/advertises/'+ad_id, None)
        if ad and ad_id:
            try:
                FIREBASE_URL="https://firebasestorage.googleapis.com/v0/b/sosokan-1452b.appspot.com/o/images%2F"

                geo_location = firebase.get('/geoFire/'+ad_id, None)
                
                latitude = float(ad.get('latitude',0))
                longitude = float(ad.get('longitude',0))
                position = None
                location = None
                
                if geo_location:
                    if geo_location.get('l'):
                        latitude = geo_location.get('l')[0]
                        longitude= geo_location.get('l')[1]
                        position = Geoposition(float(latitude), float(longitude))
                        location = GEOSGeometry('POINT(%s %s)' % (longitude, latitude))

                obj, created = Ad.objects.update_or_create(legacy_id = ad_id, defaults={
                    'category': Category.objects.filter(legacy_id=ad.get('categoryId')).first(),
                    'user': User.objects.filter(userprofile__legacy_id=ad.get('userId','')).first(),
                    'address': ad.get('Address'),
                    'advertiseId': ad.get('advertiseId'),
                    'categoryId': ad.get('categoryId'),
                    'chinese': ad.get('chinese', False),
                    'coupon': ad.get('coupon'),
                    'created_on': datetime.datetime.utcfromtimestamp(ad.get('createdAt',time.time())).strftime('%Y-%m-%d %H:%M:%SZ'),
                    'createdAt': ad.get('createdAt', time.time()),
                    'descendingTime': ad.get('descendingTime',-time.time()),
                    'description': ad.get('description',''),
                    'descriptionPlainText': ad.get('descriptionPlainText'),
                    'enableEmail': ad.get('enableEmail', False),
                    'enablePhone': ad.get('enablePhone', False),
                    'favoriteCount': int(ad.get('favoriteCount','0')),
                    'feature': ad.get('feature'),
                    'flagCount': int(ad.get('flagCount','0')),
                    'hidden': ad.get('hidden', False),
                    'isChinese': ad.get('isChinese', False),
                    'language': ad.get('isChinese', False) and "zh-hans" or "en",
                    'isFeatured': ad.get('isFeature', False),
                    'isHtmlDes': ad.get('isHtmlDes', True),
                    'isStandout': ad.get('isStandout', False),
                    'position': position,
                    'location': location,
                    'name': ad.get('name', '-'),
                    'price': float(ad.get('price',0)),
                    'saleOff': int(ad.get('saleOff','0')),
                    'shareCount': int(ad.get('shareCount','0')),
                    'standout': ad.get('standout',False),
                    'updatedAt': ad.get('createdAt', time.time()),
                    'userId': ad.get('userId','')
                    }
                )
                if ad.get('images'):
                    for fb_image in ad.get('images'):
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
                return HttpResponse(json.dumps({'status': 'success'}))
            except Exception, e:
                return HttpResponse(json.dumps({'status': 'error + ' +str(e)}))
        else:
            return HttpResponse(json.dumps({'status': 'error + ad ' + request.data.get('ad_id') + " does not exist "}))


class get_firebase_key(APIView):
    """Send 'uid' and receive 'key' """

    authentication_classes = (CsrfExemptSessionAuthentication,)
    permission_classes = (permissions.AllowAny,)


    def post(self, request, *args, **kwargs):


        service_account_email = "firebase-adminsdk-v71bb@sosokan-1452b.iam.gserviceaccount.com"
        private_key = RSA.importKey("-----BEGIN PRIVATE KEY-----\nMIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQDrURIqajzmeCta\nF81Sbw3h897nQs+n5bu1E6FALCR0JZd6EEDHBOyJt0T4fLFR948cfsOUOeDTCzL3\nbpcg2i0qegFkJjaFG8798Ao/AaphAw4D6GH1ZS+5vxGvisvRmc4xHkIQeZek75Y4\nThFlPOGL9Qqq2mUiQ+7BNv35R5FAGPC3wq4Q8UIjgtFZ0s734FgwoeJ6Q/kueYU9\nupEmwX6cFk3LorI/Y+GVAJas8ENqtc9zhc9N1jVJVBRvNYc1cZN+RDeIJBujwUdc\nj82e5vJ4OU4OUZu52mDtWoanogIndFI67rqj5w1MvAhMHyqLgxwPAQgg5XI+/4gi\ngWH2W7o/AgMBAAECggEAJUU151nkQ2yNETvr3T6RqFQD3gUK9sDANjHhvFRBgdPi\nZgbk+6CgBj/swJ6fRyg6lnzNZVC1dzey3tW5Qg7MUIn6Zm5W/EYnRrkaJliRL8RY\nF+5adY+NO5IzruYA2jXOHqF4PTvE9O6tkEVtI+e8oXoyMlVc50xzKE8Zcr4PF2OO\nG++cQ3YV9Zk7ZcNdWfBFedQXzmxR64V7zv1sKVa1DCqYtDfH55vfl2DKffjBAavc\npD83/xm97725Cr+RK+GJP7MYCxO3KR7wiYli6Be43GEeQjZ/q0BU88p+nmJX+sdm\nKHfERzMIK30jfmBfQeSyTGXezxJDBqGjQAbjKz1SYQKBgQD4HT7hpK2wjjQvGGfd\n0HzqexX6y9apgewLJdTDF5vktj2JgoV7uwqcsiQYMLX1egHi7kyfEBmPU9TFP1j2\nKbvdUxmTtYCjOzPVAHTzjzecsJBK9acXBF5iVta1V4QvU4p44Vlfeb2BJVY8CRKk\nbZi96iHiFySRssUb1AqGkBHlJQKBgQDyy7MMLKyyJVuhlvqZnjkfIYfrK7YCPJki\nYXYN0YSLIMAVHDdA/TOz4yXYMJSxtyrwXXhq+ug5/3gv5yP9+GfyUL6GRcVAplrl\neVtb2Y8muISLcunOW7Yxlm4OD3nn0BTWfglzQteVdq5U5+YCddCdc34YZqgQXyDT\ndGRt2B6ukwKBgQDUGHQDgbm0fqtfQvavKCVQcG2mmY9kEZsNIIbTSyBJj/vky719\nurNkpeezAhjZwjVQnzLCmomu+xOclf1fWOeuV/Y6TWKsptQ1FhHjjp8Ug96lJX9z\nbpmcaO1b0Xc+2aQ6lzDGLBRyDM+9vGfCRanXVwkl83CgV/ilRErHIusZPQKBgCFv\nXcPCbzHFJT42bWI0QMeBtZb0GQdr0ubCJyC2BpumMe12SkGf3Lvb/v0ri36Bq98X\n8BpDXTGHdcSa/wDdcWsUJXiWKRjDD+dj8+nNJwJ9DHuronjxdkCT56n2BaKfrA7x\nolmGDeweQNmeabVTBhx4qBeyI1jF0N5tu9rIlUkTAoGAH0R5YyQtYRnKZGhLm4C7\np1CNSZVmQcIvSjO64cCugwBFkoFb9ghY4252tMBYSXEAJ51GEynhRRYEVqEBC8up\n7FS5mGklBbORYkwZg+puRddYKZ+9EeR9STBoaZnRoM2/8dbv48oOPTqgRYZvRBWA\nFCKVGdkDCNmKoOzEobCUsNI=\n-----END PRIVATE KEY-----\n")

        try:
            payload = {
              "iss": service_account_email,
              "sub": service_account_email,
              "aud": "https://identitytoolkit.googleapis.com/google.identity.identitytoolkit.v1.IdentityToolkit",
              "uid": request.data.get('uid')
            }
            exp = datetime.timedelta(minutes=60)
            return HttpResponse(json.dumps({'status': 'success', 'key': jwt.generate_jwt(payload, private_key, "RS256", exp)}))
        except Exception as e:
            return HttpResponse(json.dumps({'status': 'error + ' +str(e)}))
        return None


class AdDeleteView(DeleteView):
    model = Ad
    success_url = reverse_lazy('ads-list')


class AddRating(View):
  
    def post(self, request, *args, **kwargs):
        params = {
            'app_label': 'sosokan',
            'model': 'ad',
            'field_name': 'rating'
        }
        params.update(kwargs)
        response = AddRatingFromModel()(request, **params)
        print response 
        if response.status_code == 200:

            return HttpResponse(Ad.objects.get(id=kwargs['object_id']).rating.likes)
        return HttpResponse(json.dumps({'error': 9, 'message': response.content}))
  
    def get(self, request, *args, **kwargs):
        return self.post(request, *args, **kwargs)