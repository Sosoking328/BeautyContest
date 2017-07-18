from django.contrib.auth.models import User
from sosokan.models import *
from sosokan.permissions import IsOwnerOrReadOnly
from rest_framework import routers, serializers, viewsets, filters
from rest_framework.decorators import detail_route
from django.views.generic import TemplateView, View
from sorl_thumbnail_serializer.fields import HyperlinkedSorlImageField
from rest_framework.permissions import AllowAny
from django_comments.models import Comment
from django.contrib.contenttypes.models import ContentType
from django.conf import settings
from rest_framework.generics import get_object_or_404
from rest_framework.response import Response
from rest_framework_gis.filters import DistanceToPointFilter
from rest_framework.authtoken.models import Token
from rest_framework import exceptions
from rest_framework.decorators import detail_route, list_route
from rest_framework.authentication import SessionAuthentication, TokenAuthentication
from rest_framework import pagination
from rest_framework import permissions
from geopy.geocoders import Nominatim
from django.contrib.gis.geos import Point
from django.contrib.gis.measure import D
import django_filters.rest_framework
from django.utils.decorators import method_decorator
from django.views.decorators.cache import cache_page
from rest_framework.fields import CurrentUserDefault

class CsrfExemptSessionAuthentication(SessionAuthentication):

    def enforce_csrf(self, request):
        return

def words_to_point(q):
    geolocator = Nominatim()
    location = geolocator.geocode(q)
    point = Point(location.longitude, location.latitude)
    return point

class UserSerializer(serializers.HyperlinkedModelSerializer):
    class Meta:
        model = User
        fields = ('id', 'url', 'username', 'email', 'is_staff','date_joined','first_name','last_name','is_superuser','last_login','is_active',)


class FlagSerializer(serializers.HyperlinkedModelSerializer):
    class Meta:
        model = Flag
        fields = ('id', 'ad', 'user', 'reason', 'content')

class FlagChoiceSerializer(serializers.HyperlinkedModelSerializer):
    class Meta:
        model = FlagChoice
        fields = ('id', 'reason')

class UserProfileSerializer(serializers.ModelSerializer):
    image_url = serializers.CharField()
    access_token = serializers.SerializerMethodField()
    liked = serializers.SerializerMethodField()
    user_categories = serializers.SerializerMethodField()

    class Meta:
        model = UserProfile
        fields = ('id', 'image_url', 'address', 'callAble', 'city', 'companyName', 'credit', 'emailAble',
                  'faxNumber', 'legacy_id', 'myAdvertiseCount', 'note', 'display_name', 'website',
                  'phoneNumber', 'role', 'state', 'zip', 'user', 'access_token', 'categories', 'liked', 'user_categories')

    def get_access_token(self, obj):
        try:
            token = Token.objects.get(user=obj.user)
        except Token.DoesNotExist:
            token = None

        if token:
            return token.key
        return None

    def get_liked(self, obj):
        liked = Favorite.objects.filter(user=obj.user).count()
        return liked

    def get_user_categories(self, obj):
        user = obj.user
        ads = user.ad_set.values('category')
        categories = list(set([ad.get('category') for ad in ads]))
        return categories


class AdImageSerializer(serializers.HyperlinkedModelSerializer):
    class Meta:
        model = AdImage
        fields = ('id','image','width','height')

class HeaderImageSerializer(serializers.Serializer):
    url = serializers.CharField()
    width = serializers.CharField()
    height = serializers.CharField()
    class Meta:
        fields = '__all__'

class CategoryImageSerializer(serializers.HyperlinkedModelSerializer):
    class Meta:
        model = CategoryImage
        fields = ('id', 'ad', 'image')


class TokenSerializer(serializers.ModelSerializer):
    class Meta:
        model = Token
        fields = '__all__'


class SplashSerializer(serializers.ModelSerializer):
    class Meta:
        model = Splash
        fields = '__all__'


class FavoriteSerializer(serializers.ModelSerializer):
    class Meta:
        model = Favorite
        fields = ('ad','user')


class CommentSerializer(serializers.HyperlinkedModelSerializer):
    firebase_user_id = serializers.CharField(required=False, read_only=True)
    avatar = serializers.CharField(required=False, read_only=True)
    user_voted = serializers.BooleanField(read_only=True)
    class Meta:
        model = Comment
        fields = (
            'id',
            'object_pk',
            'user_name',
            'user_email',
            'user_url',
            'comment',
            'ip_address',
            'is_public',
            'is_removed',
            'submit_date',
            'firebase_user_id',
            'rating_likes',
            'avatar',
            'user_voted',
        )


class AdSerializerDetails(serializers.ModelSerializer):
    comments = CommentSerializer(many=True)
    adimage = AdImageSerializer(many=True)
    favorite = FavoriteSerializer(many=True)
    owner_email = serializers.SerializerMethodField()
    owner_phone = serializers.SerializerMethodField()
    owner_avatar = serializers.SerializerMethodField()
    owner_profile_id = serializers.SerializerMethodField()
   # current_user_favorited = serializers.SerializerMethodField()

    class Meta:
        model = Ad
        fields = (
            'id',
            'name',
            #'chinese',
            'categoryId',
            'category',
            #'isChinese',
            'language',
            'price',
            'favoriteCount',
           # 'current_user_favorited',
            'legacy_id',
            'created_on',
            'user',
            'userId',
            'imageHeader',
            'descriptionPlainText',
            'saleOff',
            'comments',
            'created_on',
            'expires_on',
            'address',
            'position',
            'location',
            'coupon',
            'descendingTime',
            'description',
            'enableEmail',
            'enablePhone',
            'feature',
            'flagCount',
            'hidden',
            'isFeatured',
            'isHtmlDes',
            'isStandout',
            'shareCount',
            'standout',
            'createdAt',
            'updatedAt',
            'adimage',
            'favorite',
            'views',
            'owner_phone',
            'owner_email',
            'owner_avatar',
            'owner_profile_id',
        )

    def get_current_user_favorited(self, inst):
        try:
            return Favorite.objects.filter(user=CurrentUserDefault(), ad=inst).exists()
        except Exception as e:
            return None
            
    def get_owner_phone(self, inst):
        if inst.user:
            try:
                phone =  inst.user.userprofile.phoneNumber
                return phone
            except Exception as e:
                return None

    def get_owner_email(self, inst):
        if inst.user:
            try:
                email =  inst.user.email
                return email
            except Exception as e:
                return None
 
    def get_owner_avatar(self, inst):
        if inst.user:
            try:
                avatar =  inst.user.userprofile.image.url
                return avatar
            except Exception as e:
                return None

    def get_owner_profile_id(self, inst):
        if inst.user:
            try:
                profile_id =  inst.user.userprofile.pk
                return profile_id
            except Exception as e:
                return None



class AdSerializer(serializers.HyperlinkedModelSerializer):
    imageHeader = HeaderImageSerializer(required=False, read_only=True)
    favorite = FavoriteSerializer(many=True, read_only=True)

    owner_avatar = serializers.SerializerMethodField()
    owner_profile_id = serializers.SerializerMethodField()
    owner_name = serializers.SerializerMethodField()
    owner_username = serializers.SerializerMethodField()
    current_user_favorited = serializers.SerializerMethodField()

    class Meta:
        model = Ad
        fields = (
            'url',
            'id',
            'name',
            #'chinese',
            'categoryId',
            #'isChinese',
            'language',
            'price',
            'favoriteCount',
            'current_user_favorited',
            'shareCount',
            'feature',
            'isFeatured',
            'isStandout',
            'standout',
            'legacy_id',
            'category',
            'created_on',
            'user',
            'owner_avatar',
            'owner_name',
            'owner_username',
            'owner_profile_id',
            'userId',
            'imageHeader',
            'short_description',
            'saleOff',
            'location',
            'position',
            'favorite',
            'views',
        )

    def get_current_user_favorited(self, inst):
        try:
            return Favorite.objects.filter(user=CurrentUserDefault(), ad=inst).exists()
        except Exception as e:
            return None

    def get_owner_name(self, inst):
        if inst.user:
            try:
                name =  inst.user.first_name + " " + inst.user.last_name
                return name
            except Exception as e:
                return None

    def get_owner_username(self, inst):
        if inst.user:
            try:
                username =  inst.user.username
                return username
            except Exception as e:
                return None

 
    def get_owner_avatar(self, inst):
        if inst.user:
            try:
                avatar =  inst.user.userprofile.image.url
                return avatar
            except Exception as e:
                return None

    def get_owner_profile_id(self, inst):
        if inst.user:
            try:
                profile_id =  inst.user.userprofile.pk
                return profile_id
            except Exception as e:
                return None

class CategorySerializer(serializers.HyperlinkedModelSerializer):

    children = serializers.PrimaryKeyRelatedField(
        queryset=Category.objects.all(),
        many=True,
        required=False
    )
    class Meta:
        model = Category
        fields = (
                'id',
                'url',
                'name',
                'nameChinese',
                'slug',
                'legacy_id',
                'advertiseCount',
                'count',
                'count_en',
                'count_zh_hans',
                'createdAt',
                'deepLevel',
                'parentId',
                'popular',
                'sort',
                'updatedAt',
                'top_bar',
                'lft',
                'rght',
                'tree_id',
                'level',
                'parent',
                'iconChinese',
                'iconEnglish',
                'children',
                'background_image_url',
                )



class BannerSerializer(serializers.HyperlinkedModelSerializer):
    class Meta:
        model = Banner
        fields = ('category', 'categoryId', 'language', 'image', 'url', 'address','latitude','longitude','created')

class ContentTypeSerializer(serializers.HyperlinkedModelSerializer):
    class Meta:
        model = ContentType
        fields = ('id', 'url', 'app_label', 'model')

class ContentTypeViewSet(viewsets.ModelViewSet):
    queryset = ContentType.objects.all()
    serializer_class = ContentTypeSerializer


class StatusTypeSerializer(serializers.HyperlinkedModelSerializer):
    class Meta:
        model = Status
        fields = ('category', 'text')

class StatusTypeViewSet(viewsets.ModelViewSet):
    queryset = Status.objects.all()
    serializer_class = StatusTypeSerializer


class UserViewSet(viewsets.ModelViewSet):
    queryset = User.objects.all()
    serializer_class = UserSerializer


class UserProfileViewSet(viewsets.ModelViewSet):
    """Adding ?legacy_id=### will return the userprofile object"""
    permission_classes = (IsOwnerOrReadOnly,)
    queryset = UserProfile.objects.all()
    serializer_class = UserProfileSerializer
    filter_fields = ('legacy_id',)
    filter_backends = (filters.DjangoFilterBackend,)

    def get_object(self):
        user_id = self.kwargs["pk"]
        if user_id == "current":
            return self.request.user.userprofile
        else:
            raise exceptions.PermissionDenied()

class FlagViewSet(viewsets.ModelViewSet):
    permission_classes = (AllowAny,)
    authentication_classes = (CsrfExemptSessionAuthentication,)
    queryset = Flag.objects.all()
    serializer_class = FlagSerializer

class FlagChoiceViewSet(viewsets.ModelViewSet):
    permission_classes = (AllowAny,)    
    queryset = FlagChoice.objects.all()
    serializer_class = FlagChoiceSerializer

class AdViewSet(viewsets.ModelViewSet):
    permission_classes = (AllowAny,)
    queryset = Ad.objects.all()
    serializer_class = AdSerializer
    distance_filter_field = 'location'
    filter_fields = ('category','categoryId','language','user','legacy_id')
    search_fields = ('name', 'descriptionPlainText', 'user__userprofile__phoneNumber')
    filter_backends = (filters.SearchFilter,django_filters.rest_framework.DjangoFilterBackend, filters.OrderingFilter, DistanceToPointFilter,)

    def retrieve(self, request, pk=None):
        queryset = Ad.objects.all()
        ad = get_object_or_404(queryset, pk=pk)
        ad.views = ad.views + 1
        ad.save()
        serializer = AdSerializerDetails(ad)
        return Response(serializer.data)

    def get_queryset(self, *args, **kwargs):
        queryset = self.queryset
        q = self.request.GET.get('city')
        distance = self.request.GET.get('dist',10)
        min_price = self.request.GET.get('min_price')
        max_price = self.request.GET.get('max_price')
        favorites = self.request.GET.get('favorites')
        if q:
            point = words_to_point(q)
            distance = D(mi=distance)
            queryset = queryset.filter(location__distance_lte=(point, distance))
            # return all records if there's no ads in the requested city
            if len(queryset) == 0:
                queryset = self.queryset

        if min_price and max_price:
            queryset = queryset.filter(price__range=(min_price, max_price))

        if favorites:
            try:
                user = User.objects.get(id=favorites)
            except User.DoesNotExist:
                user = None
            if user:
                queryset = queryset.filter(favorite__user=user)
        return queryset

    @detail_route(methods=["post"])
    def like(self, request, **kwargs):
        user = self.request.user
        ad = self.get_object()
        Favorite.objects.get_or_create(user=user, ad=ad)
        serializer = AdSerializerDetails(ad)
        return Response(serializer.data)

    @detail_route(methods=["post"])
    def unlike(self, request, pk=None):
        user = self.request.user
        ad =self.get_object()

        try:
            favt = Favorite.objects.get(user=user, ad=ad)
            favt.delete()
        except Favorite.DoesNotExist:
            raise exceptions.ValidationError(["You have not liked it to unlike"])
        serializer = AdSerializerDetails(ad)
        return Response(serializer.data)

    @method_decorator(cache_page(60))
    def dispatch(self, *args, **kwargs):
        return super(AdViewSet, self).dispatch(*args, **kwargs)

class AdImageViewSet(viewsets.ModelViewSet):
    queryset = AdImage.objects.all()
    serializer_class = AdImageSerializer

class SplashViewSet(viewsets.ModelViewSet):
    permission_classes = (AllowAny,)
    queryset = Splash.objects.all()
    serializer_class = SplashSerializer
    filter_fields = ('language',)
    filter_backends = (filters.DjangoFilterBackend,)
    def get_paginated_response(self, data):
         return Response(data)

class ExamplePagination(pagination.PageNumberPagination):       
       page_size = None

class CategoryViewSet(viewsets.ModelViewSet):
    permission_classes = (AllowAny,) #change this when auth is connected
    queryset = Category.objects.all()
    serializer_class = CategorySerializer
    filter_fields = ('parentId','top_bar')
    pagination_class=ExamplePagination

    @method_decorator(cache_page(60))
    def dispatch(self, *args, **kwargs):
        return super(CategoryViewSet, self).dispatch(*args, **kwargs)


class FavoriteViewSet(viewsets.ModelViewSet):
    queryset = Favorite.objects.all()
    serializer_class = FavoriteSerializer
    filter_fields = ('user_id',)

class TokenViewSet(viewsets.ModelViewSet):
    queryset = Token.objects.all()
    serializer_class = TokenSerializer
    filter_fields = ('user_id',)
    filter_backends = (filters.DjangoFilterBackend,)

class BannerViewSet(viewsets.ModelViewSet):
    permission_classes = (AllowAny,) #change this when auth is connected
    queryset = Banner.objects.all()
    serializer_class = BannerSerializer
    filter_fields = ('categoryId', 'language')

    def paginate_queryset(self, queryset, view=None):
        return None

class CommentViewSet(viewsets.ModelViewSet):
    """to get comments for an ad filter with ?object_pk=###"""
    permission_classes = (AllowAny,)
    queryset = Comment.objects.all()
    serializer_class = CommentSerializer
    
    def perform_create(self, serializer):
        serializer.save(
            content_type_id=ContentType.objects.get_for_model(Ad).pk, 
            site_id=settings.SITE_ID, 
            user_id=self.request.user.id,
            user_name=self.request.user.username,
            user_email=self.request.user.email
            )
    filter_fields = ('object_pk', )



router = routers.DefaultRouter()
router.register(r'splashes', SplashViewSet)
router.register(r'users', UserViewSet)
router.register(r'userprofiles', UserProfileViewSet)
router.register(r'ads', AdViewSet)
router.register(r'adimages', AdImageViewSet)
router.register(r'categories', CategoryViewSet)
router.register(r'favorites', FavoriteViewSet)
router.register(r'banners', BannerViewSet)
router.register(r'comments', CommentViewSet)
router.register(r'flags', FlagViewSet)
router.register(r'flagchoices', FlagChoiceViewSet)
router.register(r'contenttypes', ContentTypeViewSet)
router.register(r'statuses', StatusTypeViewSet)
router.register(r'tokens', TokenViewSet)
