# -*- coding: utf-8 -*-
# Generated by Django 1.9.5 on 2016-12-14 20:29
from __future__ import unicode_literals

import datetime
from django.db import migrations, models


class Migration(migrations.Migration):

    dependencies = [
        ('sosokan', '0052_auto_20161214_1521'),
    ]

    operations = [
        migrations.AlterField(
            model_name='ad',
            name='createdAt',
            field=models.FloatField(default=1481747390.084),
        ),
        migrations.AlterField(
            model_name='ad',
            name='created_on',
            field=models.DateTimeField(default=datetime.datetime(2016, 12, 14, 15, 29, 50, 83000)),
        ),
        migrations.AlterField(
            model_name='ad',
            name='descendingTime',
            field=models.FloatField(default=-1481747390.084),
        ),
        migrations.AlterField(
            model_name='ad',
            name='updatedAt',
            field=models.FloatField(default=1481747390.084),
        ),
        migrations.AlterField(
            model_name='adimage',
            name='createdAt',
            field=models.FloatField(default=1481747390.085),
        ),
        migrations.AlterField(
            model_name='adimage',
            name='descendingTime',
            field=models.FloatField(default=-1481747390.085),
        ),
        migrations.AlterField(
            model_name='adimage',
            name='updatedAt',
            field=models.FloatField(default=1481747390.085),
        ),
    ]
