# -*- coding: utf-8 -*-
# Generated by Django 1.9.5 on 2016-11-16 18:49
from __future__ import unicode_literals

from django.db import migrations, models


class Migration(migrations.Migration):

    dependencies = [
        ('sosokan', '0013_auto_20161116_1348'),
    ]

    operations = [
        migrations.AlterField(
            model_name='ad',
            name='shareCount',
            field=models.TextField(blank=True, default=b'', max_length=255, null=True),
        ),
    ]
