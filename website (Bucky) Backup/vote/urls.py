from django.conf.urls import url
from . import views

app_name = 'vote'

urlpatterns = [

        # /vote/
        url(r'^$', views.index, name='index'),

        # /vote/

        # /vote/ID#/
        url(r'^(?P<album_id>[0-9]+)/$', views.detail, name='detail'),

        url(r'^(?P<album_id>[0-9]+)/favorite/$', views.favorite, name='favorite'),


]
