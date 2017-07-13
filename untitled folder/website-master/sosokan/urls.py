from django.conf.urls import include, url
from django.contrib import admin
from django.contrib.auth.models import User
from django.contrib.auth.decorators import login_required
from sosokan.models import *
from django.views.generic import TemplateView, View
from views import  *
from django.views.generic import RedirectView
from serializers import router
from django.contrib.auth.decorators import login_required
from profiles.views import FacebookLogin, WechatLogin, GmailLogin, current_user


from rest_framework_swagger.views import get_swagger_view

schema_view = get_swagger_view(title='Sosokan API')


urlpatterns = [
    url(r'^$', HomeView.as_view()),
    url(r'^search/', AdListView.as_view()),
    url(r'^terms/', TemplateView.as_view(template_name="terms.html")),
    url(r"^api/comments/(?P<object_id>\d+)/rate/(?P<score>[\d\-]+)$", AddRatingAPI.as_view(), name="comment_rating"),
    url(r'^api_docs$', schema_view),
    url(r"^api/sync_ad/$", SyncAd.as_view(), name="sync_ad"),
    url(r"^api/get_firebase_key/$", get_firebase_key.as_view(), name="get_firebase_key"),
    url(r'^api/phone_login/', APIPhoneLogin.as_view(), name="api_phone_login"),
    url(r'^phone_login/', PhoneLogin.as_view(), name="phone_login"),
    url(r'^upload/', Upload.as_view(), name="upload"),
    url(r'^api/user/', current_user, name="current_user"),
    url(r'^tinymce/', include('tinymce.urls')),
    url(r'^api/', include(router.urls)),
    url(r'^admin/', include(admin.site.urls)),
    url(r'^api-auth/', include('rest_framework.urls', namespace='rest_framework')),
    url(r'^rest-auth/', include('rest_auth.urls')),
    url(r'^rest-auth/registration/', include('rest_auth.registration.urls')),
    url(r'^rest-auth/facebook/$', FacebookLogin.as_view(), name='fb_login'),
    url(r'^rest-auth/google/$', GmailLogin.as_view(), name='gmail_login'),
    url(r'^rest-auth/wechat/$', WechatLogin.as_view(), name='wechat_login'),
    url(r'^link/(?P<slug>[^/]+)/$', AppleView.as_view()),
    url(r'^share/$', ShareView.as_view()),
    url(r'^ios/$', iOSView.as_view()),
    url(r'^android/$', AndroidView.as_view()),
    url(r'^accounts/', include('allauth.urls')),
    url(r'^accounts/profile/', profile),
    url(r'^profile/(?P<slug>[^/]+)/$', login_required(UserProfileDetailView.as_view()), name="profile"),
    url(r'^lazy_load_posts/$', lazy_load_posts, name='lazy_load_posts'),
    url(r'^ad/(?P<pk>\w+)/$', AdView.as_view(), name="ad_view"),
    url(r'^ad_ajax/(?P<pk>\w+)/$', AdView.as_view(), name="ad_ajax_view"),
    url(r'^post/$', login_required(PostAdView.as_view()), name="post_ad"),
    url(r'^docs/', include('rest_framework_docs.urls')),
    url(r'^comments/', include('django_comments.urls')),
    url(r'^rosetta/', include('rosetta.urls')),
    url(r'^profiles/', include('profiles.urls')),
    url(r'^i18n/', include('django.conf.urls.i18n')),
    url(r'^delete_ad/(?P<pk>\d+)/$', AdDeleteView.as_view(), name='delete_ad'),
    url(r"^ad/(?P<object_id>\d+)/rate/(?P<score>[\d\-]+)$", login_required(AddRating.as_view()), name="ad_rating"),
    url(r'^pages/(?P<slug>[^/]+)/$', static),


    

]


