# -*- coding: utf-8 -*-
# Generated by Django 1.9.6 on 2017-02-15 23:16
from __future__ import unicode_literals

from django.db import migrations, models


class Migration(migrations.Migration):

    dependencies = [
        ('sosokan', '0089_auto_20170211_2353'),
    ]

    operations = [
        migrations.CreateModel(
            name='User Ad',
            fields=[
            ],
            options={
                'proxy': True,
            },
            bases=('sosokan.ad',),
        ),
        migrations.AlterField(
            model_name='ad',
            name='createdAt',
            field=models.FloatField(default=1487218597.0),
        ),
        migrations.AlterField(
            model_name='ad',
            name='descendingTime',
            field=models.FloatField(default=-1487200597.574),
        ),
        migrations.AlterField(
            model_name='ad',
            name='updatedAt',
            field=models.FloatField(default=1487218597.0),
        ),
        migrations.AlterField(
            model_name='adimage',
            name='createdAt',
            field=models.FloatField(default=1487200597.576),
        ),
        migrations.AlterField(
            model_name='adimage',
            name='descendingTime',
            field=models.FloatField(default=-1487200597.576),
        ),
        migrations.AlterField(
            model_name='adimage',
            name='updatedAt',
            field=models.FloatField(default=1487200597.576),
        ),
        migrations.AlterField(
            model_name='categoryimage',
            name='createdAt',
            field=models.FloatField(default=1487200597.578),
        ),
        migrations.AlterField(
            model_name='categoryimage',
            name='descendingTime',
            field=models.FloatField(default=-1487200597.578),
        ),
        migrations.AlterField(
            model_name='categoryimage',
            name='updatedAt',
            field=models.FloatField(default=1487200597.578),
        ),
    ]
