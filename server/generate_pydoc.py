import django
import pydoc
import os

os.environ['DJANGO_SETTINGS_MODULE'] = 'spq_server.settings'
django.setup()
pydoc.cli()
