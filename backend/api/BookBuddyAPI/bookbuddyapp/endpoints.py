import secrets
import traceback
import bcrypt

from django.http import JsonResponse
from django.views.decorators.csrf import csrf_exempt
from django.core.exceptions import ValidationError
from .models import User, Stand, Book
import json


@csrf_exempt
def register(request):
    if request.method == 'POST':
        body_json = json.loads(request.body)

        if 'name' not in body_json or 'password' not in body_json:
            return JsonResponse({'error': 'Missing parameters or incorrect parameters'}, status=400)

        json_name = body_json['name']
        json_password = body_json['password']
        encrypted_password = bcrypt.hashpw(json_password.encoide('utf8'), bcrypt.gensalt()).decode('utf8')

        if User.objects.filter(name=json_name).exists() or User.objects.filter(password=json_password).exists():
            return JsonResponse({'error': 'This username already exists'}, status=409)

        token = secrets.token_hex(10)
        User.objects.create(name=json_name, password=encrypted_password, token=token)
        return JsonResponse({'message': 'User created successfully'}, status=201)

    else:
        return JsonResponse({'error': 'Internal Server Error'}, status=500)