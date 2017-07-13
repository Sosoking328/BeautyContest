from django.contrib.gis.db import models
from django.contrib.auth.models import User
from django.core.validators import RegexValidator
from django.db.models.signals import post_save
from tinymce.models import HTMLField
import pyrebase
import datetime
import time
import json
import re
from django.db.models.signals import post_save
from django.dispatch import receiver
from django.conf.global_settings import LANGUAGES
from geoposition.fields import GeopositionField
from mptt.models import MPTTModel, TreeForeignKey
from django.core.exceptions import ValidationError
from imagekit.models import ImageSpecField
from imagekit.processors import ResizeToFit
from django_comments.models import Comment
from django.contrib.contenttypes.models import ContentType
from django.db.models.signals import post_save
from django.core.mail import send_mail
from rest_framework.authtoken.models import Token
from updown.fields import RatingField
from updown.models import Vote
from django.utils import timezone
from django.contrib.humanize.templatetags.humanize import naturaltime
from django.utils.translation import ugettext_lazy as _


class Category(MPTTModel):
    name = models.CharField(max_length=200)
    nameChinese = models.CharField(max_length=200, default="", blank=True, null=True)
    slug = models.SlugField()
    advertiseCount = models.IntegerField(default=None, blank=True, null=True)
    createdAt = models.CharField(max_length=255, default="")
    deepLevel = models.IntegerField(default=0)
    legacy_id = models.TextField(max_length=255, default="")
    parentId = models.CharField(max_length=255, default="", blank=True, null=True)
    parent = TreeForeignKey('self', null=True, blank=True, related_name='children', db_index=True)
    popular = models.IntegerField(default=0)
    sort = models.IntegerField(default=0)
    updatedAt = models.CharField(max_length=255, default="", blank=True, null=True)
    top_bar = models.BooleanField(default=False)
    
    def __unicode__(self):
        p_list = self._recurse_for_parents(self)
        p_list.append(self.name)
        return self.get_separator().join(p_list)
    
    def _recurse_for_parents(self, cat_obj):
        p_list = []
        if cat_obj.parentId:
            try:
                parent_category = Category.objects.get(legacy_id=cat_obj.parentId)
                p = parent_category
                p_list.append(p.name)
                more = self._recurse_for_parents(p)
                p_list.extend(more)
            except:
                pass
        if cat_obj == self and p_list:
            p_list.reverse()
        return p_list
    
    def get_separator(self):
        return ' :: '
    
    def _parents_repr(self):
        p_list = self._recurse_for_parents(self)
        return self.get_separator().join(p_list)
    _parents_repr.short_description = 'Category parents'

    class MPTTMeta:
        order_insertion_by = ['name']

    def get_absolute_url(self):
        return "https://console.firebase.google.com/project/sosokan-1452b/database/data/categories/" + str(self.legacy_id)

    class Meta:
        verbose_name_plural = u'categories'
        ordering = ['name']

    @property
    def iconChinese(self):
        try:
            return CategoryImage.objects.filter(category=self).filter(language="zh-hans")[0].image.url
        except:
            return None

    @property
    def iconEnglish(self):
        try:
            return CategoryImage.objects.filter(category=self).filter(language="en")[0].image.url
        except:
            return None

    @property
    def background_image_url(self):
        try:
            return CategoryImage.objects.filter(category=self).last().background.url
        except:
            return None

    def image_tag(self):
        return u'<img src="%s" height="30"/>' % self.iconEnglish
    image_tag.short_description = 'Image'
    image_tag.allow_tags = True

    @property
    def count(self):
        return Ad.objects.filter(category=self).count()

    @property
    def count_en(self):
        return Ad.objects.filter(category=self, language="en").count()

    @property
    def count_zh_hans(self):
        return Ad.objects.filter(category=self, language="zh-hans").count()

class Ad(models.Model):
    category = models.ForeignKey(Category, blank=True, null=True)
    user = models.ForeignKey(User, default=1)
    created_on = models.DateTimeField(default=timezone.now) #change to timezone sensitive
    expires_on = models.DateTimeField(blank=True, null=True)
    address = models.CharField(max_length=255, default="", blank=True, null=True)
    position = GeopositionField(blank=True, null=True)
    location = models.PointField(null=True, blank=True,)
    advertiseId = models.CharField(max_length=255, default="", blank=True, null=True)
    categoryId = models.CharField(max_length=255, default="", blank=True, null=True)
    chinese = models.BooleanField(default=False)
    language = models.CharField(max_length=7, choices=LANGUAGES, default="en")
    coupon = models.CharField(max_length=255, default="", blank=True, null=True)
    descendingTime = models.FloatField(default=-time.time())
    description = HTMLField(default=" ")
    descriptionPlainText = models.TextField(default=" ",blank=True, null=True)
    enableEmail = models.BooleanField(default=False)
    enablePhone = models.BooleanField(default=False)
    favoriteCount = models.IntegerField(default=0)
    views = models.IntegerField(default=0)
    feature = models.NullBooleanField(default=False, blank=True, null=True)
    flagCount = models.IntegerField(default=0)
    hidden = models.BooleanField(default=False)
    legacy_id = models.TextField(max_length=255, default="", blank=True, null=True)
    isChinese = models.NullBooleanField(default=False, blank=True, null=True)
    isFeatured = models.NullBooleanField(default=False, blank=True, null=True)
    isHtmlDes = models.BooleanField(default=True)
    isStandout = models.BooleanField(default=False)
    name = models.CharField(max_length=255, default="")
    price = models.DecimalField(max_digits=20, decimal_places=2, default=0, blank=True, null=True)
    saleOff = models.CharField(max_length=255, blank=True, null=True)
    shareCount = models.IntegerField(default=0)
    standout = models.NullBooleanField(default=False, blank=True, null=True)
    createdAt = models.FloatField(default=time.time()) 
    updatedAt = models.FloatField(default=time.time())
    userId = models.CharField(max_length=255, default="HZETmXH25SXNDWVc4ZtNaR07Ru13")
    Referral_ID = models.CharField(max_length=255, blank=True, null=True)
    rating = RatingField(can_change_vote=True)
    # tags
    # video = models.URLField(default="", null=True, blank=True, max_length=256)


    # def get_absolute_url(self):
    #     return "https://console.firebase.google.com/project/sosokan-1452b/database/data/advertises/" + str(self.legacy_id)

    def get_absolute_url(self):
        return "/ad/" + str(self.id)


    def __unicode__(self):
        return self.name

    def expired(self):
        if self.expires_on <= datetime.datetime.now():
            return True
        else:
            return False
    
    @property
    def imageHeader(self):
        try:
            reduce_by = float(self.adimage.first().width) / 300
            return {
            "url":self.adimage.first().header.url, 
            "width": "300", 
            "height": int(round(self.adimage.first().height / reduce_by))
            }
        except:
            return None

    @property
    def short_description(self):
        if self.descriptionPlainText:
            cleanr = re.compile('<.*?>')
            r_unwanted = re.compile("[\n\t\r]")
            stripped = r_unwanted.sub("", self.descriptionPlainText)
            cleantext = re.sub(cleanr, '', stripped)[:80]
            return cleantext
        else:
            return ""

    @property
    def defualt_image(self):
        if self.isChinese:
            try:
                category = self.category
                return category.iconChinese
            except AttributeError:
                return ""
        else:
            try:
                category = self.category
                return category.iconEnglish
            except AttributeError:
                return ""


    def save(self, *args, **kwargs):
        if self.created_on is None:
            if not self.language:
                self.language = (self.isChinese or self.chinese) and "zh-hans" or "en"
            self.created_on = datetime.datetime.now()
        super(Ad, self).save(*args, **kwargs)

    class Meta:
        ordering = ['-created_on']

    @property
    def comments(self):
        instance = self
        content_type = ContentType.objects.get_for_model(instance.__class__)
        obj_id = instance.id
        qs = Comment.objects.filter(content_type=content_type, object_pk=obj_id)
        return qs

    def image_tag(self):
        if self.imageHeader:
            return u'<img src="%s" height="30"/>' % self.imageHeader.get('url')
        else:
            return u''
    image_tag.short_description = 'Image'
    image_tag.allow_tags = True

    def user_voted(self):
        content_type = ContentType.objects.get_for_model(self.__class__)
        try:
            voted = Vote.objects.get(object_id=self.id, content_type=content_type, user=User.objects.get(id=self.user_id), vote=1)
            return True
        except Exception, e:
            return False

    def time_since(self):
        if (timezone.now() - self.created_on).days > 30:
            return _("30+ days ago")
        else:
            return naturaltime(self.created_on)
        
def validate_image(fieldfile_obj):
        filesize = fieldfile_obj.file.size
        megabyte_limit = 1.0
        if filesize > megabyte_limit*1024*1024:
            raise ValidationError("Max file size is %sMB" % str(megabyte_limit))

class AdImage(models.Model):
    ad = models.ForeignKey(Ad, related_name='adimage')
    image = models.ImageField(upload_to='images/', width_field='width', height_field='height', validators=[validate_image])
    createdAt = models.FloatField(default=time.time())
    descendingTime = models.FloatField(default=-time.time())
    height = models.IntegerField(default=0)
    width = models.IntegerField(default=0)
    legacy_id = models.TextField(max_length=255, default="", blank=True, null=True)
    imageUrl = models.URLField(default="", null=True, blank=True, max_length=256)
    isStoredInStorage = models.BooleanField(default=True)
    isVideoThumb = models.BooleanField(default=False)
    status = models.BooleanField(default=True)
    updatedAt = models.FloatField(default=time.time())
    userId = models.CharField(max_length=255, default="Sosokan")

    header = ImageSpecField(source='image',
                                      processors=[ResizeToFit(300)],
                                      format='JPEG',
                                      options={'quality': 60})



class FlagChoice(models.Model):
    reason = models.TextField(max_length=255)
    
    def __unicode__(self):
        return self.reason

class Flag(models.Model):
    ad = models.ForeignKey(Ad)
    user = models.ForeignKey(User)
    reason = models.ForeignKey(FlagChoice)
    content = models.TextField(blank=True, null=True)

    def __unicode__(self):
        return self.ad.name + " " + self.reason.reason


def create_flag(sender, **kwargs):
    flag = kwargs["instance"]
    if kwargs["created"]:
        send_mail(
            'Flag on ' + flag.ad.name,
            "Flag was set on " + flag.ad.name + " by user: " + flag.user.username + " for reason: "+ flag.reason.reason + " content:  "+ flag.content,
            'report@sosokanapp.com',
            ["report@sosokanapp.com"],
        )      

post_save.connect(create_flag, sender=Flag)


class CategoryImage(models.Model):
    category = models.ForeignKey(Category)
    image = models.ImageField(upload_to='images/', validators=[validate_image])
    createdAt = models.FloatField(default=time.time())
    descendingTime = models.FloatField(default=-time.time())
    language = models.CharField(max_length=7, choices=LANGUAGES, default="en")
    legacy_id = models.TextField(max_length=255, default="", blank=True, null=True)
    imageUrl = models.URLField(default="", null=True, blank=True, max_length=256)
    updatedAt = models.FloatField(default=time.time())
    background = models.ImageField(upload_to='images/',
                                   validators=[validate_image], default=None, null=True)

    def __unicode__(self):
        return self.category.name


class Splash(models.Model):
    video = models.FileField(upload_to='videos/')
    language = models.CharField(max_length=7, choices=LANGUAGES, default="en")
    created = models.DateTimeField(auto_now_add=True)
    modified = models.DateTimeField(auto_now=True)

@receiver(post_save, sender=AdImage, dispatch_uid="process_image")
def process_image(sender, instance, **kwargs):

        config = {
          "apiKey": "AIzaSyAo7r6nWAosfSCDISUKlAZOgGhNl_vEjHo",
          "authDomain": "sosokan-1452b.firebaseapp.com",
          "databaseURL": "https://sosokan-1452b.firebaseio.com",
          "storageBucket": "sosokan-1452b.appspot.com",
          "messagingSenderId": "935208892140",
          "serviceAccount": "sosokan-service.json"
        }

        data = {
            "createdAt": instance.createdAt,
            "descendingTime": instance.descendingTime,
            "height": instance.height,
            "width": instance.width,
            "imageUrl": instance.image.url,
            "isStoredInStorage": instance.isStoredInStorage and True or False,
            "isVideoThumb": instance.isVideoThumb and True or False,
            "status": instance.status and True or False,
            "updatedAt": instance.updatedAt,
            "userId": instance.userId
        }

        if not instance.legacy_id:
            firebase = pyrebase.initialize_app(config)
            db = firebase.database()
            
            legacy_id = db.child("images").push(data)['name']
            instance.legacy_id = legacy_id
            updated = db.child("images").child(legacy_id).update({'id':legacy_id})
            if len(AdImage.objects.filter(ad=instance.ad))>1:
                updated2 = db.child("advertises").child(instance.ad.legacy_id).child('images').update({instance.legacy_id:instance.descendingTime})
            else:
                updated2 = db.child("advertises").child(instance.ad.legacy_id).child('imageHeader').update(data)
                updated = db.child("advertises").child(instance.ad.legacy_id).child('imageHeader').update({'id':legacy_id})
                time.sleep(2)
                updated3 = db.child("advertises").child(instance.ad.legacy_id).child('images').update({instance.legacy_id:instance.descendingTime})


class UserProfile(models.Model):
    user = models.OneToOneField(User, related_name="userprofile")
    phone_regex = RegexValidator(regex=r'^\+?1?\d{9,15}$', message="Phone number must be entered in the format: '+999999999'. Up to 15 digits allowed.")
    address = models.CharField(max_length=255, default="", blank=True, null=True)
    callAble = models.BooleanField(default=True)
    city = models.CharField(max_length=30, blank=True, null=True)
    companyName = models.CharField(max_length=255, default="", blank=True, null=True)
    credit = models.DecimalField(max_digits=20, decimal_places=2, default=0, blank=True, null=True)
    emailAble = models.BooleanField(default=True)
    faxNumber = models.CharField(max_length=50, blank=True, null=True)
    legacy_id = models.CharField(max_length=255, default="")
    myAdvertiseCount = models.IntegerField(default=0)
    note = models.CharField(max_length=255, default="", blank=True, null=True)
    display_name = models.CharField(max_length=255, default="", blank=True, null=True)
    website = models.CharField(max_length=255, default="", blank=True, null=True)
    phoneNumber = models.CharField(max_length=30, validators=[phone_regex], blank=True)
    role = models.CharField(max_length=50, default="Writer")
    state = models.CharField(max_length=50, blank=True, null=True)
    zip = models.CharField(max_length=50, blank=True, null=True)
    image = models.ImageField(max_length=255, upload_to='avatars/', validators=[validate_image], null=True, blank=True)
    categories = models.ManyToManyField(Category)

    def get_absolute_url(self):
        return "https://console.firebase.google.com/project/sosokan-1452b/database/data/users/" + str(self.legacy_id)

    @property
    def number_of_posts(self):
        return Ad.objects.filter(user=self.user).count()

    def image_tag(self):
        return u'<img src="%s" height="30" style="border-radius: 25px;"/>' % self.image
    image_tag.short_description = 'Image'
    image_tag.allow_tags = True

    def image_url(self):
        try:
            return self.image
        except:
            return ""

    def save(self, *args, **kwargs):
        if self.user.username is None:
            self.user.username = self.user.email
            self.user.save()
        super(UserProfile, self).save(*args, **kwargs)


def create_profile(sender, **kwargs):
    user = kwargs["instance"]
    if kwargs["created"]:
        profile = UserProfile(user=user)
        profile.save()

    #      "apiKey": "AIzaSyAo7r6nWAosfSCDISUKlAZOgGhNl_vEjHo",
    #      "authDomain": "sosokan-1452b.firebaseapp.com",
    #      "databaseURL": "https://sosokan-1452b.firebaseio.com",
    #      "storageBucket": "sosokan-1452b.appspot.com",
    #      "messagingSenderId": "935208892140",
    #      "serviceAccount": "sosokan-service.json"
    #    }

     #   firebase = pyrebase.initialize_app(config)
     #   db = firebase.database()

        

post_save.connect(create_profile, sender=User)

@receiver(post_save, sender=User)
def create_auth_token(sender, instance=None, created=False, **kwargs):
    instance.username = instance.email
    if created:
        Token.objects.create(user=instance)

class Favorite(models.Model):
    user = models.ForeignKey(User)
    ad = models.ForeignKey(Ad, related_name="favorite")

    class Meta:
        unique_together = ("user", "ad")

    def get_absolute_url(self):
        return str(self.id)

class Banner(models.Model):
    category = models.ForeignKey(Category, blank=True, null=True, on_delete=models.SET_NULL)
    categoryId = models.CharField(max_length=255, default="", blank=True, null=True)
    image = models.ImageField(upload_to='banners/')
    url = models.URLField()
    language = models.CharField(max_length=7, choices=LANGUAGES, default="en")
    address = models.CharField(max_length=255, default="", blank=True, null=True)
    latitude = models.FloatField(blank=True, null=True)
    longitude = models.FloatField(blank=True, null=True)
    created = models.DateTimeField(auto_now_add=True)
    modified = models.DateTimeField(auto_now=True)


class Status(models.Model):
    category = models.ForeignKey(Category)
    text = models.TextField(max_length=255)

    def __unicode__(self):
        return self.category.name + " | " + self.text

def firebase_user_id(self):
    return UserProfile.objects.get(user__id=self.user_id).legacy_id or 0

def comment_user_avatar(self):
    return UserProfile.objects.get(user__id=self.user_id).image or ""

def user_voted(self):
    content_type = ContentType.objects.get_for_model(self.__class__)
    try:
        voted = Vote.objects.get(object_id=self.id, content_type=content_type, user=User.objects.get(id=self.user_id), vote=1)
        return True
    except Exception, e:
        return False

Comment.add_to_class('firebase_user_id', firebase_user_id)

Comment.add_to_class('rating', RatingField(can_change_vote=True))

Comment.add_to_class('avatar', comment_user_avatar)

Comment.add_to_class('user_voted', user_voted)
