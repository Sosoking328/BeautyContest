# -*- coding: utf-8 -*-
# Generated by Django 1.9.5 on 2016-12-30 01:38
from __future__ import unicode_literals

import datetime
from django.db import migrations, models


class Migration(migrations.Migration):

    dependencies = [
        ('sosokan', '0067_auto_20161229_2038'),
    ]

    operations = [
        migrations.AlterField(
            model_name='ad',
            name='createdAt',
            field=models.FloatField(default=1483061910.935),
        ),
        migrations.AlterField(
            model_name='ad',
            name='created_on',
            field=models.DateTimeField(default=datetime.datetime(2016, 12, 29, 20, 38, 30, 935000)),
        ),
        migrations.AlterField(
            model_name='ad',
            name='descendingTime',
            field=models.FloatField(default=-1483061910.935),
        ),
        migrations.AlterField(
            model_name='ad',
            name='updatedAt',
            field=models.FloatField(default=1483061910.935),
        ),
        migrations.AlterField(
            model_name='adimage',
            name='createdAt',
            field=models.FloatField(default=1483061910.936),
        ),
        migrations.AlterField(
            model_name='adimage',
            name='descendingTime',
            field=models.FloatField(default=-1483061910.936),
        ),
        migrations.AlterField(
            model_name='adimage',
            name='updatedAt',
            field=models.FloatField(default=1483061910.937),
        ),
    ]