# -*- coding: utf-8 -*-
# Generated by Django 1.9.5 on 2017-01-02 20:51
from __future__ import unicode_literals

import datetime
from django.db import migrations, models


class Migration(migrations.Migration):

    dependencies = [
        ('sosokan', '0071_auto_20170102_1548'),
    ]

    operations = [
        migrations.DeleteModel(
            name='ZipCode',
        ),
        migrations.AlterField(
            model_name='ad',
            name='createdAt',
            field=models.FloatField(default=1483390293.441),
        ),
        migrations.AlterField(
            model_name='ad',
            name='created_on',
            field=models.DateTimeField(default=datetime.datetime(2017, 1, 2, 15, 51, 33, 440000)),
        ),
        migrations.AlterField(
            model_name='ad',
            name='descendingTime',
            field=models.FloatField(default=-1483390293.441),
        ),
        migrations.AlterField(
            model_name='ad',
            name='updatedAt',
            field=models.FloatField(default=1483390293.441),
        ),
        migrations.AlterField(
            model_name='adimage',
            name='createdAt',
            field=models.FloatField(default=1483390293.442),
        ),
        migrations.AlterField(
            model_name='adimage',
            name='descendingTime',
            field=models.FloatField(default=-1483390293.442),
        ),
        migrations.AlterField(
            model_name='adimage',
            name='updatedAt',
            field=models.FloatField(default=1483390293.442),
        ),
    ]
