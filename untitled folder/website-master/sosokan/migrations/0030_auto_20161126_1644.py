# -*- coding: utf-8 -*-
# Generated by Django 1.9.5 on 2016-11-26 21:44
from __future__ import unicode_literals

from django.db import migrations, models


class Migration(migrations.Migration):

    dependencies = [
        ('sosokan', '0029_auto_20161126_1215'),
    ]

    operations = [
        migrations.AlterField(
            model_name='ad',
            name='createdAt',
            field=models.FloatField(default=1480196689.284),
        ),
        migrations.AlterField(
            model_name='ad',
            name='descendingTime',
            field=models.FloatField(default=-1480196689.283),
        ),
        migrations.AlterField(
            model_name='ad',
            name='saleOff',
            field=models.IntegerField(default=0),
        ),
        migrations.AlterField(
            model_name='ad',
            name='updatedAt',
            field=models.FloatField(default=1480196689.284),
        ),
    ]
