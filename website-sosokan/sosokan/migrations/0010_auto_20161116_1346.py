# -*- coding: utf-8 -*-
# Generated by Django 1.9.5 on 2016-11-16 18:46
from __future__ import unicode_literals

from django.db import migrations, models


class Migration(migrations.Migration):

    dependencies = [
        ('sosokan', '0009_auto_20161116_1345'),
    ]

    operations = [
        migrations.AlterField(
            model_name='ad',
            name='isFeatured',
            field=models.NullBooleanField(default=None),
        ),
    ]