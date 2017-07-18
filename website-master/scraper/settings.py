import os

PROJECT_ROOT = os.path.abspath(os.path.dirname(__file__))
os.environ.setdefault("DJANGO_SETTINGS_MODULE", "scraper.settings") #Changed in DDS v.0.3

BOT_NAME = 'open_news'

SPIDER_MODULES = ['scraper.spiders', 'sosokan.scraper',]
USER_AGENT = '%s/%s' % (BOT_NAME, '1.0')

#Scrapy 0.20+
ITEM_PIPELINES = {
    'scraper.pipelines.ValidationPipeline': 400,
    'sosokan.scraper.pipelines.DjangoWriterPipeline': 800,
}

#Scrapy up to 0.18
ITEM_PIPELINES = [
    'scraper.pipelines.ValidationPipeline',
    'sosokan.scraper.pipelines.DjangoWriterPipeline',
]