# -*- coding: utf-8 -*-
# Generated by Django 1.9.5 on 2016-11-16 18:42
from __future__ import unicode_literals

from django.db import migrations, models
import django.db.models.deletion


class Migration(migrations.Migration):

    dependencies = [
        ('sosokan', '0005_remove_ad_active'),
    ]

    operations = [
        migrations.AlterField(
            model_name='ad',
            name='category',
            field=models.ForeignKey(blank=True, null=True, on_delete=django.db.models.deletion.CASCADE, to='sosokan.Category'),
        ),
    ]
