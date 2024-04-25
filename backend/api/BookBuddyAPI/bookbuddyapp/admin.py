from django.contrib import admin

# Register your models here.
from .models import User, Stand, Book

admin.site.register(User)
admin.site.register(Stand)
admin.site.register(Book)
