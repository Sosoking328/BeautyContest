# -*- coding: utf-8 -*-
# Generated by Django 1.9.5 on 2016-11-16 18:43
from __future__ import unicode_literals

from django.conf import settings
from django.db import migrations, models
import django.db.models.deletion


class Migration(migrations.Migration):

    dependencies = [
        ('sosokan', '0006_auto_20161116_1342'),
    ]

    operations = [
        migrations.AlterField(
            model_name='ad',
            name='user',
            field=models.ForeignKey(blank=True, null=True, on_delete=django.db.models.deletion.CASCADE, to=settings.AUTH_USER_MODEL),
        ),
    ]
