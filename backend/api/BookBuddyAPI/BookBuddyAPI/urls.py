"""
URL configuration for BookBuddyAPI project.

The `urlpatterns` list routes URLs to views. For more information please see:
    https://docs.djangoproject.com/en/5.0/topics/http/urls/
Examples:
Function views
    1. Add an import:  from my_app import views
    2. Add a URL to urlpatterns:  path('', views.home, name='home')
Class-based views
    1. Add an import:  from other_app.views import Home
    2. Add a URL to urlpatterns:  path('', Home.as_view(), name='home')
Including another URLconf
    1. Import the include() function: from django.urls import include, path
    2. Add a URL to urlpatterns:  path('blog/', include('blog.urls'))
"""
from django.contrib import admin
from django.urls import path
from bookbuddyapp import endpoints

urlpatterns = [
    path('admin/', admin.site.urls),
    path('api/BookBuddy/session', endpoints.register),
    path('api/BookBuddy/login', endpoints.login),
    path('api/BookBuddy/book/<stand_id>', endpoints.upload_book),
    path('api/BookBuddy/profile', endpoints.profile_info),
    path('api/BookBuddy/update_profile', endpoints.update_profile),
    path('api/BookBuddy/books_history', endpoints.books_history),
    path('api/BookBuddy/book_info/<book_id>', endpoints.book_info),
    path('api/BookBuddy/stand_info/<stand_id>', endpoints.stand_info),
    path('api/BookBuddy/stand_books/<stand_id>', endpoints.stand_books),
    path('api/BookBuddy/exchanged_book/<book_id>', endpoints.exchanged_book)
]
