# -*- coding: utf-8 -*-
# Generated by Django 1.9.5 on 2016-11-19 21:23
from __future__ import unicode_literals

from django.db import migrations, models


class Migration(migrations.Migration):

    dependencies = [
        ('sosokan', '0024_auto_20161119_1616'),
    ]

    operations = [
        migrations.AlterField(
            model_name='category',
            name='advertiseCount',
            field=models.IntegerField(blank=True, default=None, null=True),
        ),
    ]
