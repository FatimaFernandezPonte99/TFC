from django.db import models

# Create your models here.


class User(models.Model):
    name = models.CharField(max_length=50, null=False, unique=True)
    password = models.CharField(max_length=50, null=False, unique=True)
    country = models.CharField(max_length=50)
    token = models.CharField(unique=True, max_length=45)


class Stand(models.Model):
    name = models.CharField(max_length=50, null=False, unique=True)
    address = models.CharField(max_length=250, null=False, unique=True)


class Book(models.Model):
    title = models.CharField(max_length=100, null=False)
    author = models.CharField(max_length=50, null=False)
    num_pages = models.IntegerField(null=False)
    editorial = models.CharField(max_length=50, null=False)
    review = models.CharField(max_length=500, null=False)
    stand_name = models.ForeignKey(Stand, on_delete=models.CASCADE)
    user_name = models.ForeignKey(User, on_delete=models.CASCADE)

    def to_json(self):
        return {
            "title": self.title,
            "author": self.author,
            "num_pages": self.num_pages
        }