# -*- coding: utf-8 -*-
# Generated by Django 1.9.6 on 2017-05-10 16:29
from __future__ import unicode_literals

from django.db import migrations, models


class Migration(migrations.Migration):

    dependencies = [
        ('sosokan', '0102_auto_20170428_1436'),
    ]

    operations = [
        migrations.AlterField(
            model_name='ad',
            name='createdAt',
            field=models.FloatField(default=1494433759.649784),
        ),
        migrations.AlterField(
            model_name='ad',
            name='descendingTime',
            field=models.FloatField(default=-1494433759.649305),
        ),
        migrations.AlterField(
            model_name='ad',
            name='updatedAt',
            field=models.FloatField(default=1494433759.649802),
        ),
        migrations.AlterField(
            model_name='adimage',
            name='createdAt',
            field=models.FloatField(default=1494433759.652044),
        ),
        migrations.AlterField(
            model_name='adimage',
            name='descendingTime',
            field=models.FloatField(default=-1494433759.652068),
        ),
        migrations.AlterField(
            model_name='adimage',
            name='updatedAt',
            field=models.FloatField(default=1494433759.652273),
        ),
        migrations.AlterField(
            model_name='categoryimage',
            name='createdAt',
            field=models.FloatField(default=1494433759.657284),
        ),
        migrations.AlterField(
            model_name='categoryimage',
            name='descendingTime',
            field=models.FloatField(default=-1494433759.657333),
        ),
        migrations.AlterField(
            model_name='categoryimage',
            name='updatedAt',
            field=models.FloatField(default=1494433759.657577),
        ),
    ]
