from django.conf.urls import url
from . import views

app_name = 'vote'

urlpatterns = [

        # /vote/
        url(r'^$', views.IndexView.as_view(), name='index'),

        url(r'^register/$', views.UserFormView.as_view(), name='register'),


        url(r'^(?P<pk>[0-9]+)/$', views.DetailView.as_view(), name='detail'),
        # /vote/album/add/
        url(r'album/add/$', views.AlbumCreate.as_view(), name='album-add'),
        # /vote/album/update/
        url(r'album/^(?P<pk>[0-9]+)/$', views.AlbumUpdate.as_view(), name='album-update'),
        # /vote/album/delete/
        url(r'album/^(?P<pk>[0-9]+)/$', views.AlbumDelete.as_view(), name='album-delete'),


]
