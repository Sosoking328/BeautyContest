from django.db import models
from django.contrib.auth.models import User

class Candidate(models.Model):
    assigned_number = models.IntegerField(default=1)
    first_name = models.CharField(max_length=100)
    last_name = models.CharField(max_length=100)
    vote_count = models.IntegerField(default=0)
    candidate_image = models.ImageField(upload_to='static/polls/images/')
    hit_count = models.IntegerField(default=0)
    def __str__(self):
        return self.first_name + " " + self.last_name

class Voter(models.Model):
    user = models.ForeignKey(User)
    choosen = models.ForeignKey(Candidate)

