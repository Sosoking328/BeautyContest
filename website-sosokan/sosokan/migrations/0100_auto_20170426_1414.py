# -*- coding: utf-8 -*-
# Generated by Django 1.9.6 on 2017-04-26 18:14
from __future__ import unicode_literals

from django.db import migrations, models


class Migration(migrations.Migration):

    dependencies = [
        ('sosokan', '0099_auto_20170327_1353'),
    ]

    operations = [
        migrations.AlterField(
            model_name='ad',
            name='createdAt',
            field=models.FloatField(default=1493230460.868),
        ),
        migrations.AlterField(
            model_name='ad',
            name='descendingTime',
            field=models.FloatField(default=-1493230460.867),
        ),
        migrations.AlterField(
            model_name='ad',
            name='updatedAt',
            field=models.FloatField(default=1493230460.868),
        ),
        migrations.AlterField(
            model_name='adimage',
            name='createdAt',
            field=models.FloatField(default=1493230460.869),
        ),
        migrations.AlterField(
            model_name='adimage',
            name='descendingTime',
            field=models.FloatField(default=-1493230460.869),
        ),
        migrations.AlterField(
            model_name='adimage',
            name='updatedAt',
            field=models.FloatField(default=1493230460.869),
        ),
        migrations.AlterField(
            model_name='categoryimage',
            name='createdAt',
            field=models.FloatField(default=1493230460.871),
        ),
        migrations.AlterField(
            model_name='categoryimage',
            name='descendingTime',
            field=models.FloatField(default=-1493230460.871),
        ),
        migrations.AlterField(
            model_name='categoryimage',
            name='updatedAt',
            field=models.FloatField(default=1493230460.871),
        ),
    ]
