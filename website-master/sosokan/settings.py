gettext_noop = lambda s: s
import os
import dj_database_url
from django.utils.translation import ugettext_lazy as _
from django.http import Http404


#python manage.py makemessages --ignore venv
# Languages we provide translations for, out of the box.
LANGUAGES = [
    #('af', gettext_noop('Afrikaans')),
    #('ar', gettext_noop('Arabic')),
    #('ast', gettext_noop('Asturian')),
    #('az', gettext_noop('Azerbaijani')),
    ('bg', gettext_noop('Bulgarian')),
    ('be', gettext_noop('Belarusian')),
    ('bn', gettext_noop('Bengali')),
    #('br', gettext_noop('Breton')),
    ('bs', gettext_noop('Bosnian')),
    #('ca', gettext_noop('Catalan')),
    ('cs', gettext_noop('Czech')),
    ('cy', gettext_noop('Welsh')),
    #('da', gettext_noop('Danish')),
    ('de', gettext_noop('German')),
    ('el', gettext_noop('Greek')),
    ('en', gettext_noop('English')),
    #('en-au', gettext_noop('Australian English')),
    #('en-gb', gettext_noop('British English')),
    #('eo', gettext_noop('Esperanto')),
    ('es', gettext_noop('Spanish')),
    #('es-ar', gettext_noop('Argentinian Spanish')),
    #('es-co', gettext_noop('Colombian Spanish')),
    #('es-mx', gettext_noop('Mexican Spanish')),
    #('es-ni', gettext_noop('Nicaraguan Spanish')),
    #('es-ve', gettext_noop('Venezuelan Spanish')),
    #('et', gettext_noop('Estonian')),
    #('eu', gettext_noop('Basque')),
    #('fa', gettext_noop('Persian')),
    #('fi', gettext_noop('Finnish')),
    ('fr', gettext_noop('French')),
    #('fy', gettext_noop('Frisian')),
    #('ga', gettext_noop('Irish')),
    #('gd', gettext_noop('Scottish Gaelic')),
    #('gl', gettext_noop('Galician')),
    ('he', gettext_noop('Hebrew')),
    ('hi', gettext_noop('Hindi')),
    ('hr', gettext_noop('Croatian')),
    ('hu', gettext_noop('Hungarian')),
    #('ia', gettext_noop('Interlingua')),
    #('id', gettext_noop('Indonesian')),
    #('io', gettext_noop('Ido')),
    #('is', gettext_noop('Icelandic')),
    ('it', gettext_noop('Italian')),
    ('ja', gettext_noop('Japanese')),
    #('ka', gettext_noop('Georgian')),
    #('kk', gettext_noop('Kazakh')),
    #('km', gettext_noop('Khmer')),
    #('kn', gettext_noop('Kannada')),
    ('ko', gettext_noop('Korean')),
   # ('lb', gettext_noop('Luxembourgish')),
    #('lt', gettext_noop('Lithuanian')),
    #('lv', gettext_noop('Latvian')),
    #('mk', gettext_noop('Macedonian')),
    #('ml', gettext_noop('Malayalam')),
    #('mn', gettext_noop('Mongolian')),
    #('mr', gettext_noop('Marathi')),
    #('my', gettext_noop('Burmese')),
    #('nb', gettext_noop('Norwegian Bokmal')),
    #('ne', gettext_noop('Nepali')),
    #('nl', gettext_noop('Dutch')),
   # ('nn', gettext_noop('Norwegian Nynorsk')),
    #('os', gettext_noop('Ossetic')),
    #('pa', gettext_noop('Punjabi')),
    ('pl', gettext_noop('Polish')),
    ('pt', gettext_noop('Portuguese')),
    #('pt-br', gettext_noop('Brazilian Portuguese')),
    #('ro', gettext_noop('Romanian')),
    ('ru', gettext_noop('Russian')),
    #('sk', gettext_noop('Slovak')),
   # ('sl', gettext_noop('Slovenian')),
    #('sq', gettext_noop('Albanian')),
    #('sr', gettext_noop('Serbian')),
    #('sr-latn', gettext_noop('Serbian Latin')),
    #('sv', gettext_noop('Swedish')),
    #('sw', gettext_noop('Swahili')),
    #('ta', gettext_noop('Tamil')),
    #('te', gettext_noop('Telugu')),
    #('th', gettext_noop('Thai')),
    #('tr', gettext_noop('Turkish')),
    #('tt', gettext_noop('Tatar')),
    #('udm', gettext_noop('Udmurt')),
   # ('uk', gettext_noop('Ukrainian')),
    #('ur', gettext_noop('Urdu')),
    ('vi', gettext_noop('Vietnamese')),
    ('zh-hans', gettext_noop('Simplified Chinese')),
    #('zh-hant', gettext_noop('Traditional Chinese')),
]

# Build paths inside the project like this: os.path.join(BASE_DIR, ...)
BASE_DIR = os.path.dirname(os.path.dirname(__file__))

SITE_ID=1
# Quick-start development settings - unsuitable for production
# See https://docs.djangoproject.com/en/1.7/howto/deployment/checklist/

# SECURITY WARNING: keep the secret key used in production secret!
SECRET_KEY = "&cg=30*7(bwz=+a0h@%s!))!bjrof5htk$ckhb2i)n_m2$$t$x"

# SECURITY WARNING: don't run with debug turned on in production!
DEBUG = True



ALLOWED_HOSTS = []

REST_FRAMEWORK = {
    'DEFAULT_PERMISSION_CLASSES': [
        'rest_framework.permissions.IsAuthenticated'],
    'DEFAULT_PAGINATION_CLASS': 'rest_framework.pagination.LimitOffsetPagination',
    'PAGE_SIZE': 100,

    'DEFAULT_AUTHENTICATION_CLASSES': (
        'rest_framework.authentication.BasicAuthentication',
        'rest_framework.authentication.SessionAuthentication',
        'rest_framework.authentication.TokenAuthentication',
    ),
    'COERCE_DECIMAL_TO_STRING': False,
    'DEFAULT_RENDERER_CLASSES': (
        'rest_framework.renderers.JSONRenderer',
        'rest_framework.renderers.BrowsableAPIRenderer',
    ),


}

# REST_FRAMEWORK = {
#     'DEFAULT_RENDERER_CLASSES': (
#         'rest_framework.renderers.JSONRenderer',
#     )
# }



REST_SESSION_LOGIN = False
# Application definition

INSTALLED_APPS = (
    'django.contrib.admin',
    'django.contrib.auth',
    'django.contrib.contenttypes',
    'django.contrib.sessions',
    'django.contrib.messages',
    'django.contrib.staticfiles',
    'django.contrib.gis',
    'django.contrib.humanize',
    'sosokan',
    'rest_framework',
    'rest_framework.authtoken',
    'rest_auth',
    'django.contrib.sites',
    'allauth',
    'allauth.account',

    'django_filters',
    'rest_auth.registration',
    'allauth.socialaccount',
    'allauth.socialaccount.providers.facebook',
    'allauth.socialaccount.providers.weixin',
    'allauth.socialaccount.providers.google',
    'tinymce',
    "geoposition",
    'imagekit',
    'rest_framework_docs',
    'mptt',
    'django_comments',
    'rosetta',
    'updown',
    'profiles',
    # 'scraper',
    'dynamic_scraper',
    #'django_weixin',
    'rest_framework_swagger',
)
SITE_ID = 1
#WECHAT_TOKEN = u'your_token'
#APP_ID = u'wx79982ec7495cf6b0'
#APP_SECRET = u'61355a79aeead68b6d306b12e703aa36'
#AES_KEY, WX_TOKEN

IMAGEKIT_DEFAULT_CACHEFILE_STRATEGY = 'imagekit.cachefiles.strategies.Optimistic'
GEOPOSITION_GOOGLE_MAPS_API_KEY = "AIzaSyA95HkslCd5JM4b74b1iQ2_PQvgSxGp9hA"
GEOPOSITION_MAP_WIDGET_HEIGHT = 300

MIDDLEWARE_CLASSES = (
    'django.contrib.sessions.middleware.SessionMiddleware',
    'django.middleware.common.CommonMiddleware',
    'django.middleware.locale.LocaleMiddleware',
    'django.middleware.csrf.CsrfViewMiddleware',
    'django.contrib.auth.middleware.AuthenticationMiddleware',
    'django.contrib.auth.middleware.SessionAuthenticationMiddleware',
    'django.contrib.messages.middleware.MessageMiddleware',
    'django.middleware.clickjacking.XFrameOptionsMiddleware',
    'rollbar.contrib.django.middleware.RollbarNotifierMiddleware',
)

ROOT_URLCONF = 'sosokan.urls'

WSGI_APPLICATION = 'sosokan.wsgi.application'

MIGRATION_MODULES = {
    #'updown': 'sosokan.migrations.updown',
    'django_comments': 'sosokan.migrations.django_comments',
}

# Database
# https://docs.djangoproject.com/en/1.7/ref/settings/#databases

# DATABASES = {
#     'default': {
#         'ENGINE': 'django.db.backends.sqlite3',
#         'NAME': os.path.join(BASE_DIR, 'db.sqlite3'),
#     }
# }

DATABASES = {
  'default': {
    'ENGINE': 'django.contrib.gis.db.backends.postgis',
    'NAME': os.environ.get('PGNAME','test'),
    'USER': os.environ.get('PGUSER'),
    'PASSWORD': os.environ.get('PGPASSWORD'),
    'HOST': '127.0.0.1',
  }
}

# Internationalization
# https://docs.djangoproject.com/en/1.7/topics/i18n/

LANGUAGE_CODE = 'en-us'
TIME_ZONE = 'America/New_York'
USE_I18N = True
USE_L10N = True
USE_TZ = True

LOGIN_URL = "/accounts/login/"
if 'DATABASE_URL' in os.environ:
    db_from_env = dj_database_url.config(conn_max_age=500)
    DATABASES['default'].update(db_from_env)
    DATABASES['default']['ENGINE'] = 'django.contrib.gis.db.backends.postgis'

# Parse database configuration from $DATABASE_URL
if 'ENVIRONMENT' in os.environ:

    DEBUG = False

    ROLLBAR = {
        'access_token': os.environ.get('ROLLBAR_ACCESS_TOKEN', '859cb21a95c34df9a431fead714c99c4'),
        'environment': 'development' if DEBUG else os.environ.get('ENVIRONMENT', 'production'),
        'root': BASE_DIR,
        'exception_level_filters': [
            (Http404, 'ignored')
        ]
    }
    import rollbar
    rollbar.init(**ROLLBAR)

    EMAIL_HOST = 'smtp.sendgrid.net'
    EMAIL_HOST_USER = os.environ.get('SENDGRID_USERNAME', 'blank')
    EMAIL_HOST_PASSWORD = os.environ.get('SENDGRID_PASSWORD', 'blank')
    EMAIL_PORT = 587
    EMAIL_USE_TLS = True
    SECURE_SSL_REDIRECT = True


GS_ACCESS_KEY_ID = os.environ.get('GS_ACCESS_KEY_ID', 'GOOGVENSZJIZJY6BCU6I')
GS_SECRET_ACCESS_KEY = os.environ.get('GS_SECRET_ACCESS_KEY', 'xSGUy3ltZiPT0/wNz1VFQoK1TMKax4uEv/Z7Mm4P')
GS_BUCKET_NAME = 'sosokan-1452b.appspot.com'
DEFAULT_FILE_STORAGE = 'storages.backends.gs.GSBotoStorage'
GS_QUERYSTRING_AUTH = False
GS_FILE_OVERWRITE = False
MEDIA_URL = "https://sosokan-1452b.appspot.com.storage.googleapis.com/"


# Honor the 'X-Forwarded-Proto' header for request.is_secure()
SECURE_PROXY_SSL_HEADER = ('HTTP_X_FORWARDED_PROTO', 'https')

# Allow all host headers
ALLOWED_HOSTS = ['*']
MPTT_ADMIN_LEVEL_INDENT = 200
# Static files (CSS, JavaScript, Images)
# https://docs.djangoproject.com/en/1.7/howto/static-files/

BASE_DIR = os.path.dirname(os.path.abspath(__file__))
STATIC_ROOT = 'staticfiles'
STATIC_URL = '/static/'

STATICFILES_DIRS = (
    os.path.join(BASE_DIR, 'static'),
)

TEMPLATES = [
{
    'BACKEND': 'django.template.backends.django.DjangoTemplates',
    'DIRS': [os.path.join(BASE_DIR, 'templates')],
    'APP_DIRS': True,
    'OPTIONS': {
        'context_processors': [
            'django.template.context_processors.debug',
            'django.template.context_processors.request',
            'django.contrib.auth.context_processors.auth',
            'django.contrib.messages.context_processors.messages',
        ],
    },
},
]

# ACCOUNT_SIGNUP_PASSWORD_VERIFICATION = False
# SOCIAL_AUTH_FACEBOOK_SCOPE = ['username']
# SOCIAL_AUTH_FACEBOOK_PROFILE_EXTRA_PARAMS = {
#     'fields': 'id, name, email, username'
# }

ACCOUNT_USER_MODEL_USERNAME_FIELD = "username"
ACCOUNT_USER_MODEL_EMAIL_FIELD= 'email'
# ACCOUNT_USER_MODEL_USERNAME_FIELD = 'email'
# ACCOUNT_USER_MODEL_EMAIL_FIELD = 'email'
#ACCOUNT_EMAIL_REQUIRED = True
ACCOUNT_USERNAME_REQUIRED = False
# ACCOUNT_USER_MODEL_USERNAME_FIELD = 'username'
ACCOUNT_EMAIL_VERIFICATION = "none"
ACCOUNT_SIGNUP_PASSWORD_ENTER_TWICE = False
# Simplified static file serving.
# https://warehouse.python.org/project/whitenoise/
# STATICFILES_STORAGE = 'whitenoise.django.GzipManifestStaticFilesStorage'
STATICFILES_STORAGE = 'django.contrib.staticfiles.storage.StaticFilesStorage'

TINYMCE_DEFAULT_CONFIG = {
    'paste_data_images': True,
    'plugins': 'link image preview codesample contextmenu table code textcolor emoticons paste autoresize',
    'toolbar1': 'bold italic underline | alignleft aligncenter alignright alignjustify '
               '| bullist numlist | outdent indent | table | link image | codesample | preview code | fontselect  fontsizeselect | forecolor backcolor | emoticons',
    'contextmenu': 'formats | link image',
    'fontsize_formats': '8px 10px 12px 14px 18px 24px 36px',
    'relative_urls': False,
    'content_css' : "/static/css/custom_content.css",
    'theme_advanced_fonts' : "Andale Mono=andale mono,times;"+
                             "Arial=arial,helvetica,sans-serif;"+
                             "Arial Black=arial black,avant garde;"+
                             "Book Antiqua=book antiqua,palatino;"+
                             "Comic Sans MS=comic sans ms,sans-serif;"+
                             "Courier New=courier new,courier;"+
                             "Century Gothic=century_gothic;"+
                             "Georgia=georgia,palatino;"+
                             "Gill Sans MT=gill_sans_mt;"+
                             "Gill Sans MT Bold=gill_sans_mt_bold;"+
                             "Gill Sans MT BoldItalic=gill_sans_mt_bold_italic;"+
                             "Gill Sans MT Italic=gill_sans_mt_italic;"+
                             "Helvetica=helvetica;"+
                             "Impact=impact,chicago;"+
                             "Iskola Pota=iskoola_pota;"+
                             "Iskola Pota Bold=iskoola_pota_bold;"+
                             "Symbol=symbol;"+
                             "Tahoma=tahoma,arial,helvetica,sans-serif;"+
                             "Terminal=terminal,monaco;"+
                             "Times New Roman=times new roman,times;"+
                             "Trebuchet MS=trebuchet ms,geneva;"+
                             "Verdana=verdana,geneva;"+
                             "Webdings=webdings;"+
                             "Wingdings=wingdings,zapf dingbats",
    'cleanup_on_startup': False,
    'custom_undo_redo_levels': 10,
    'forced_root_block': False,
    'remove_linebreaks': True,
    #'theme_advanced_resize_horizontal': False,
    #'entity_encoding': "raw",
    #'verify_html' : 'false',
    'statusbar': True,
    'width': '80%',
    #'resize': "both",
    'link_assume_external_targets': False,
    'language_url' : '/static/js/zh.js',
    }
#TINYMCE_COMPRESSOR = True

def get_cache():
  import os
  try:
    os.environ['MEMCACHE_SERVERS'] = os.environ['MEMCACHIER_SERVERS'].replace(',', ';')
    os.environ['MEMCACHE_USERNAME'] = os.environ['MEMCACHIER_USERNAME']
    os.environ['MEMCACHE_PASSWORD'] = os.environ['MEMCACHIER_PASSWORD']
    return {
      'default': {
        'BACKEND': 'django_pylibmc.memcached.PyLibMCCache',
        'TIMEOUT': 500,
        'BINARY': True,
        'OPTIONS': { 'tcp_nodelay': True }
      }
    }
  except:
    return {
      'default': {
        'BACKEND': 'django.core.cache.backends.locmem.LocMemCache'
      }
    }

CACHES = get_cache()

USER_AGENTS_CACHE = 'default'
REST_SESSION_LOGIN = False
