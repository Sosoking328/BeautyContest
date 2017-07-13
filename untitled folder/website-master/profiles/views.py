from django.shortcuts import render
from django.core.urlresolvers import reverse, reverse_lazy
from django.views.generic.edit import UpdateView
from django.views.generic.list import ListView
from sosokan.models import Ad
from profiles.forms import AdsForm
from allauth.socialaccount.providers.facebook.views import FacebookOAuth2Adapter, fb_complete_login
from allauth.socialaccount.providers.weixin.views import WeixinOAuth2Adapter
from allauth.socialaccount.providers.google.views import GoogleOAuth2Adapter
from allauth.socialaccount.helpers import complete_social_login
from rest_framework import permissions
from rest_framework.response import Response
from rest_framework.decorators import api_view
from rest_auth.registration.views import SocialLoginView
from sosokan.serializers import CsrfExemptSessionAuthentication, UserProfileSerializer
from allauth.socialaccount.models import SocialApp, SocialToken, SocialLogin
from rest_framework.authentication import TokenAuthentication


@api_view(['GET'])
def current_user(request):
    profile = request.user.userprofile
    serializer = UserProfileSerializer(profile)
    return Response(serializer.data)


class AdsListView(ListView):
    model = Ad
    context_object_name = 'ads_list'
    template_name = 'ads_list.html'

    def get_queryset(self):
        return Ad.objects.filter(user=self.request.user)


class AdsUpdateView(UpdateView):
    model = Ad
    template_name = 'ads_form.html'
    form_class = AdsForm

    def get_success_url(self):
        return reverse('ads-list')

    def get_form_kwargs(self):
        kwargs = super(AdsUpdateView, self).get_form_kwargs()
        return kwargs


class GmailLogin(SocialLoginView):
    permission_classes = (permissions.AllowAny,)
    adapter_class = GoogleOAuth2Adapter


class FacebookLogin(SocialLoginView):
    #authentication_classes = (TokenAuthentication,)
    #permission_classes = (permissions.AllowAny,)
    adapter_class = FacebookOAuth2Adapter


class WechatLogin(SocialLoginView):
    permission_classes = (permissions.AllowAny,)
    adapter_class = WeixinOAuth2Adapter
