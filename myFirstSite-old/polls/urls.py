from django.conf.urls import url
from myFirstSite import settings
from django.conf.urls.static import static
from django.contrib.staticfiles.urls import staticfiles_urlpatterns
from django.contrib.auth import views as auth_views
from . import views

urlpatterns=[
    url(r'^$',views.IndexView.as_view(),name="index"),    #127.0.0.1/polls/
    url(r'^logged_in$',views.index_after, name="index_after"),  # 127.0.0.1/polls/
    url(r'^register/$', views.UserFormView.as_view(), name='register'),
    url(r'^results/$', views.ResultsView.as_view(), name="results"),
    #127.0.0.1/polls/100/results
#    url(r'^(?P<candidate_id>[0-9]+)/votes$', views.votes, name="votes"),
    url(r'^votes/$', views.votes, name="votes"),
    #127.0.0.1/polls/110/votes,
    url(r'^login/$', auth_views.login,name="login"),
    url(r'^logout/$', auth_views.logout, name="logout"),
]

urlpatterns+=staticfiles_urlpatterns()
urlpatterns+=static(settings.MEDIA_URL,document_root=settings.MEDIA_ROOT)
