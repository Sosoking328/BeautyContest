# -*- coding: utf-8 -*-
# Generated by Django 1.9.5 on 2016-12-01 00:53
from __future__ import unicode_literals

from django.db import migrations, models
import django.db.models.deletion


class Migration(migrations.Migration):

    dependencies = [
        ('sosokan', '0036_auto_20161127_0035'),
    ]

    operations = [
        migrations.CreateModel(
            name='Banner',
            fields=[
                ('id', models.AutoField(auto_created=True, primary_key=True, serialize=False, verbose_name='ID')),
                ('categoryId', models.CharField(blank=True, default=b'', max_length=255, null=True)),
                ('image', models.ImageField(upload_to=b'banners/')),
                ('url', models.URLField()),
                ('address', models.CharField(blank=True, default=b'', max_length=255, null=True)),
                ('latitude', models.FloatField(blank=True, null=True)),
                ('longitude', models.FloatField(blank=True, null=True)),
                ('created', models.DateTimeField(auto_now_add=True)),
                ('modified', models.DateTimeField(auto_now=True)),
                ('category', models.ForeignKey(on_delete=django.db.models.deletion.CASCADE, to='sosokan.Category')),
            ],
        ),
        migrations.AlterModelOptions(
            name='ad',
            options={'ordering': ['-created_on']},
        ),
        migrations.AlterField(
            model_name='ad',
            name='createdAt',
            field=models.FloatField(default=1480553619.016),
        ),
        migrations.AlterField(
            model_name='ad',
            name='descendingTime',
            field=models.FloatField(default=-1480553619.016),
        ),
        migrations.AlterField(
            model_name='ad',
            name='updatedAt',
            field=models.FloatField(default=1480553619.016),
        ),
        migrations.AlterField(
            model_name='adimage',
            name='createdAt',
            field=models.FloatField(default=1480553619.017),
        ),
        migrations.AlterField(
            model_name='adimage',
            name='descendingTime',
            field=models.FloatField(default=-1480553619.017),
        ),
        migrations.AlterField(
            model_name='adimage',
            name='updatedAt',
            field=models.FloatField(default=1480553619.018),
        ),
    ]
