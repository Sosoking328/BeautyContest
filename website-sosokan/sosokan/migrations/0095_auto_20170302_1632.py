# -*- coding: utf-8 -*-
# Generated by Django 1.9.6 on 2017-03-02 21:32
from __future__ import unicode_literals

from django.db import migrations, models


class Migration(migrations.Migration):

    dependencies = [
        ('sosokan', '0094_auto_20170301_1937'),
    ]

    operations = [
        migrations.AlterField(
            model_name='ad',
            name='createdAt',
            field=models.FloatField(default=1488490345.266),
        ),
        migrations.AlterField(
            model_name='ad',
            name='descendingTime',
            field=models.FloatField(default=-1488490345.266),
        ),
        migrations.AlterField(
            model_name='ad',
            name='saleOff',
            field=models.CharField(blank=True, max_length=255, null=True),
        ),
        migrations.AlterField(
            model_name='ad',
            name='updatedAt',
            field=models.FloatField(default=1488490345.266),
        ),
        migrations.AlterField(
            model_name='adimage',
            name='createdAt',
            field=models.FloatField(default=1488490345.266),
        ),
        migrations.AlterField(
            model_name='adimage',
            name='descendingTime',
            field=models.FloatField(default=-1488490345.266),
        ),
        migrations.AlterField(
            model_name='adimage',
            name='updatedAt',
            field=models.FloatField(default=1488490345.266),
        ),
        migrations.AlterField(
            model_name='categoryimage',
            name='createdAt',
            field=models.FloatField(default=1488490345.266),
        ),
        migrations.AlterField(
            model_name='categoryimage',
            name='descendingTime',
            field=models.FloatField(default=-1488490345.266),
        ),
        migrations.AlterField(
            model_name='categoryimage',
            name='updatedAt',
            field=models.FloatField(default=1488490345.266),
        ),
    ]
