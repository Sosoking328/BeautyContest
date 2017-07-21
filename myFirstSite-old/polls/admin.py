from django.contrib import admin
#from .models import Question, Choice
# Register your models here.
#admin.site.register(Question)
#admin.site.register(Choice)
from . models import Candidate

admin.site.register(Candidate)
