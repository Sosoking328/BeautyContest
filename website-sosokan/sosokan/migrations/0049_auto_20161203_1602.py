# -*- coding: utf-8 -*-
# Generated by Django 1.9.5 on 2016-12-03 21:02
from __future__ import unicode_literals

import datetime
from django.db import migrations, models


class Migration(migrations.Migration):

    dependencies = [
        ('sosokan', '0048_auto_20161203_1418'),
    ]

    operations = [
        migrations.AlterField(
            model_name='ad',
            name='createdAt',
            field=models.FloatField(default=1480798945.445),
        ),
        migrations.AlterField(
            model_name='ad',
            name='created_on',
            field=models.DateTimeField(default=datetime.datetime(2016, 12, 3, 16, 2, 25, 445000)),
        ),
        migrations.AlterField(
            model_name='ad',
            name='descendingTime',
            field=models.FloatField(default=-1480798945.445),
        ),
        migrations.AlterField(
            model_name='ad',
            name='isHtmlDes',
            field=models.BooleanField(default=True),
        ),
        migrations.AlterField(
            model_name='ad',
            name='isStandout',
            field=models.BooleanField(default=False),
        ),
        migrations.AlterField(
            model_name='ad',
            name='updatedAt',
            field=models.FloatField(default=1480798945.445),
        ),
        migrations.AlterField(
            model_name='adimage',
            name='createdAt',
            field=models.FloatField(default=1480798945.445),
        ),
        migrations.AlterField(
            model_name='adimage',
            name='descendingTime',
            field=models.FloatField(default=-1480798945.445),
        ),
        migrations.AlterField(
            model_name='adimage',
            name='updatedAt',
            field=models.FloatField(default=1480798945.445),
        ),
    ]