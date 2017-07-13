# -*- coding: utf-8 -*-
# Generated by Django 1.9.6 on 2017-02-07 03:32
from __future__ import unicode_literals

from django.db import migrations, models


class Migration(migrations.Migration):

    dependencies = [
        ('django_comments', '0003_add_submit_date_index'),
    ]

    operations = [
        migrations.AddField(
            model_name='comment',
            name='rating_dislikes',
            field=models.PositiveIntegerField(blank=True, default=0, editable=False),
        ),
        migrations.AddField(
            model_name='comment',
            name='rating_likes',
            field=models.PositiveIntegerField(blank=True, default=0, editable=False),
        ),
    ]
