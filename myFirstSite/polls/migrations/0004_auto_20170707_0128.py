# -*- coding: utf-8 -*-
# Generated by Django 1.11.2 on 2017-07-07 05:28
from __future__ import unicode_literals

from django.db import migrations, models


class Migration(migrations.Migration):

    dependencies = [
        ('polls', '0003_candidate_assigned_number'),
    ]

    operations = [
        migrations.AlterField(
            model_name='candidate',
            name='assigned_number',
            field=models.IntegerField(default=1),
        ),
    ]