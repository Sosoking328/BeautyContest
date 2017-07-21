from django.contrib import admin
from sosokan.models import *
import time
from django.utils.html import strip_tags
from django.contrib.auth.admin import UserAdmin
from mptt.admin import MPTTModelAdmin
from mptt.admin import DraggableMPTTAdmin
from django.contrib.gis.geos import GEOSGeometry
#from django.db import migrations
from updown.models import Vote
from django.contrib.contenttypes.models import ContentType
from django.contrib import messages
from django.utils import timezone
import pytz
import datetime
from django.conf import settings
import os

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

class AdImageInline(admin.TabularInline):
    model = AdImage
    extra = 0

class AdAdmin(admin.ModelAdmin):
    #list_per_page = 500
    inlines = [ AdImageInline, ]
    # def get_actions(self, request):
    #     actions = super(AdAdmin, self).get_actions(request)
    #     if 'delete_selected' in actions:
    #         del actions['delete_selected']
    #     return actions
    actions = ['delete_selected']


    list_display = ('id','image_tag', 'name','user','category','views','isChinese','language','price','isFeatured','isStandout','legacy_id','created_on','userId')
    list_display_links = ('id',)
    fields =('category','user','userId','name','expires_on','created_on','description','position','price','coupon','saleOff','isChinese','chinese','language',
        'isFeatured','isStandout','isHtmlDes', 'views', 'favoriteCount', 'hidden','enableEmail','enablePhone','Referral_ID')
    list_filter = ('isChinese', 'flagCount','category')
    search_fields = ('name', 'description','user__username')

    def delete_model(self, request, obj):
        #remove advertisemetas
        #remove images
        #remove geoFire
        if obj.isChinese or obj.chinese:
            db.child("categories").child(obj.categoryId).child('advertisesChinese').child(obj.legacy_id).remove()
            db.child("categories").child('sosokanCategoryAll').child('advertisesChinese').child(obj.legacy_id).remove()
        else:
            db.child("categories").child(obj.categoryId).child('advertisesEnglish').child(obj.legacy_id).remove()
            db.child("categories").child('sosokanCategoryAll').child('advertisesEnglish').child(obj.legacy_id).remove()
        db.child("advertises").child(obj.legacy_id).remove()
        obj.delete()

    def delete_selected(self, request, obj):
        for o in obj.all():
            if o.isChinese or o.chinese:
                db.child("categories").child(o.categoryId).child('advertisesChinese').child(o.legacy_id).remove()
                db.child("categories").child('sosokanCategoryAll').child('advertisesChinese').child(o.legacy_id).remove()
            else:
                db.child("categories").child(o.categoryId).child('advertisesEnglish').child(o.legacy_id).remove()
                db.child("categories").child('sosokanCategoryAll').child('advertisesEnglish').child(o.legacy_id).remove()
            db.child("advertises").child(o.legacy_id).remove()
            o.delete()
    delete_selected.short_description = 'Delete Selected'

    def save_model(self, request, obj, form, change): 
        if obj.categoryId:
            category_id = obj.categoryId
        else:
            try:
                category_id = obj.category.legacy_id
            except Exception:
                category_id = ""

        obj.descriptionPlainText = strip_tags(obj.description) 
        if obj.position:
            obj.location = GEOSGeometry('POINT(%s %s)' % (obj.position.longitude, obj.position.latitude))
        try:
            userId = obj.user.userprofile.legacy_id or obj.userId
        except Exception:
            userId = obj.userId
        timestamp = time.mktime(pytz.timezone("America/New_York").localize(datetime.datetime.now(), is_dst=None).timetuple())
        if obj.created_on > pytz.timezone("America/New_York").localize(datetime.datetime.now(), is_dst=None):
            timestamp = time.mktime(obj.created_on.timetuple())

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
        if change:
            db.child("advertises").child(obj.legacy_id).update(data)
        else:
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

                    if obj.position:
                        if obj.position.latitude and obj.position.longitude:
                            geoFire = {
                                "g": 'testdata',
                                "l": {
                                    "0":  float(obj.position.latitude),
                                    "1":  float(obj.position.longitude),
                                }
                            }
                            db.child("geoFire").update({obj.legacy_id:geoFire})
                            data = {
                                "location": {       
                                    "latitude": float(obj.position.latitude),     
                                    "longitude": float(obj.position.longitude),       
                                }
                            }
                            db.child("advertises").child(obj.legacy_id).update(data)
            except AttributeError:
                messages.error(request, 'Category Legacy ID is blank '+str(obj.category))
                return 

        obj.save()
        messages.success(request, 'Saved Ad '+str(obj.legacy_id))


class AdAdminPage(AdAdmin):
    pass

class UserProfileInline(admin.TabularInline):
    model = UserProfile

class CategoryImageInline(admin.TabularInline):
    model = CategoryImage
    extra = 0


class BannerAdmin(admin.ModelAdmin):
    list_display = ('category', 'categoryId', 'language', 'image', 'url', 'address','latitude','longitude','created')

    def save_model(self, request, obj, form, change):
        if obj.category.legacy_id:
            obj.categoryId = obj.category.legacy_id
        obj.save()

class FavoriteAdmin(admin.ModelAdmin):
    list_display = ('ad', 'user')

class AdImageAdmin(admin.ModelAdmin):
    list_display = ('ad', 'image', 'imageUrl')

class FlagAdmin(admin.ModelAdmin):
    list_display = ('ad', 'user', 'reason')

class FlagChoiceAdmin(admin.ModelAdmin):
    list_display = ('reason',)

class UserProfileAdmin(admin.ModelAdmin):
    list_display = ('image_tag', 'user', 'address',  'callAble', 'city', 'companyName', 'credit', 'emailAble', 'faxNumber', 'legacy_id', 
        'myAdvertiseCount', 'note', 'phoneNumber', 'role', 'state', 'zip')
    search_fields = ('user__username','phoneNumber')

def create_modeladmin(modeladmin, model, name = None):
    class  Meta:
        proxy = True
        app_label = model._meta.app_label

    attrs = {'__module__': '', 'Meta': Meta}

    newmodel = type(name, (model,), attrs)

    admin.site.register(newmodel, modeladmin)
    return modeladmin

class AdAdminRaw(AdAdmin):
    fields = []
    def get_queryset(self, request):
        return self.model.objects.all()

create_modeladmin(AdAdminPage, name='User Ad', model=Ad)

create_modeladmin(AdAdminRaw, name='Full Ad', model=Ad)

admin.site.unregister(User)

UserAdmin.list_display = ('username','email', 'first_name',  'last_name', 'is_active', 'date_joined', 'is_staff')
    
UserAdmin.search_fields = ('username','email','first_name','last_name', 'userprofile__phoneNumber')

admin.site.register(User, UserAdmin)



admin.site.register(Ad, AdAdmin)

admin.site.register(Banner, BannerAdmin)

admin.site.register(Flag, FlagAdmin)

admin.site.register(FlagChoice, FlagChoiceAdmin)

admin.site.register(     
    Category,       
    DraggableMPTTAdmin,     
    list_display=( 
    'image_tag',     
         'tree_actions', 'name', 
         'indented_title','nameChinese',     
          'advertiseCount','createdAt','deepLevel','legacy_id', 'level', 'lft', 'rght', 'tree_id',
          'parent','parentId','popular','sort','updatedAt'        
         # ...more fields if you feel like it...     
     ),      
    inlines = [ CategoryImageInline, ],
    list_display_links=(        
         'indented_title',       
    ),      
)


admin.site.register(Vote)
admin.site.register(Status)
admin.site.register(ContentType)
admin.site.register(Splash)
admin.site.register(CategoryImage)
#admin.site.register(migrations)

admin.site.register(AdImage, AdImageAdmin)
admin.site.register(Favorite, FavoriteAdmin)

admin.site.register(UserProfile, UserProfileAdmin)
