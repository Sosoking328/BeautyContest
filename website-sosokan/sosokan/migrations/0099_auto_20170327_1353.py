# -*- coding: utf-8 -*-
# Generated by Django 1.9.6 on 2017-03-27 17:53
from __future__ import unicode_literals

from django.db import migrations, models


class Migration(migrations.Migration):

    dependencies = [
        ('sosokan', '0098_auto_20170327_1057'),
    ]

    operations = [
        migrations.AddField(
            model_name='userprofile',
            name='categories',
            field=models.ManyToManyField(to='sosokan.Category'),
        ),
    ]
