from django.conf.urls import url
from myFirstSite import settings
from django.conf.urls.static import static
from django.contrib.staticfiles.urls import staticfiles_urlpatterns
from django.contrib.auth import views as auth_views
from . import views

urlpatterns=[
#    url(r'^$',views.IndexView.as_view(),name="index"),    #127.0.0.1/polls/
    url(r'^$', views.index, name="index"),  # 127.0.0.1/polls/
    url(r'^logged_in$',views.index_after, name="index_after"),  # 127.0.0.1/polls/
    url(r'^register/$', views.UserFormView.as_view(), name='register'),
    url(r'^results/$', views.ResultsView.as_view(), name="results"),
    url(r'^index_final$', views.ResultsView.as_view(), name="index_final"),  # 127.0.0.1/polls/
 #   url(r'^(?P<candidate_id>[0-9]+)/$', views.image_v, name="image_v"),
    url(r'^votes/$', views.votes, name="votes"),
    url(r'^login/$', auth_views.login,name="login"),
  #  url(r'^logout/$', auth_views.logout, {'template_name':'polls/log_out.html'}, name="logout"),
 #   url(r'^logout/$', views.logout, name="logout"),
    url(r'^logout/$', views.LogOutView.as_view(), name="logout"),

]
#{'template_name':'polls/log_out.html'}
urlpatterns+=staticfiles_urlpatterns()
urlpatterns+=static(settings.MEDIA_URL,document_root=settings.MEDIA_ROOT)
