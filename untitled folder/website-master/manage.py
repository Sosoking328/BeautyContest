#!/usr/bin/env python
import os
import sys

from os.path import join, dirname
from dotenv import load_dotenv

dotenv_path = join(dirname(__file__), '.env')
load_dotenv(dotenv_path)

if __name__ == "__main__":
    os.environ.setdefault("DJANGO_SETTINGS_MODULE", "sosokan.settings")

    from django.core.management import execute_from_command_line

    execute_from_command_line(sys.argv)
