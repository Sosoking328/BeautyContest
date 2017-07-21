from django.conf.urls import include, url
from django.contrib import admin
from profiles.views import AdsListView, AdsUpdateView
from django.contrib.auth.decorators import login_required

urlpatterns = [
    url(r'^ads-list/', login_required(AdsListView.as_view()), name="ads-list"),
    url(r'^ads/edit/(?P<pk>\d+)/$', login_required(AdsUpdateView.as_view()),
        name='edit_ad'),
]