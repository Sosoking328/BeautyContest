# -*- coding: utf-8 -*-
# Generated by Django 1.9.5 on 2016-12-01 01:52
from __future__ import unicode_literals

from django.db import migrations, models
import geoposition.fields


class Migration(migrations.Migration):

    dependencies = [
        ('sosokan', '0039_auto_20161130_2049'),
    ]

    operations = [
        migrations.AlterField(
            model_name='ad',
            name='createdAt',
            field=models.FloatField(default=1480557168.777),
        ),
        migrations.AlterField(
            model_name='ad',
            name='descendingTime',
            field=models.FloatField(default=-1480557168.777),
        ),
        migrations.AlterField(
            model_name='ad',
            name='position',
            field=geoposition.fields.GeopositionField(blank=True, max_length=42, null=True),
        ),
        migrations.AlterField(
            model_name='ad',
            name='updatedAt',
            field=models.FloatField(default=1480557168.777),
        ),
        migrations.AlterField(
            model_name='adimage',
            name='createdAt',
            field=models.FloatField(default=1480557168.778),
        ),
        migrations.AlterField(
            model_name='adimage',
            name='descendingTime',
            field=models.FloatField(default=-1480557168.779),
        ),
        migrations.AlterField(
            model_name='adimage',
            name='updatedAt',
            field=models.FloatField(default=1480557168.779),
        ),
    ]
