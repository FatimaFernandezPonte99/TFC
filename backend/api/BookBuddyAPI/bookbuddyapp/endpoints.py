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
        encrypted_password = bcrypt.hashpw(json_password.encode('utf8'), bcrypt.gensalt()).decode('utf8')

        if User.objects.filter(name=json_name).exists() or User.objects.filter(password=json_password).exists():
            return JsonResponse({'error': 'This username already exists'}, status=409)

        token = secrets.token_hex(10)
        User.objects.create(name=json_name, password=encrypted_password, token=token)
        return JsonResponse({'message': 'User created successfully'}, status=201)

    else:
        return JsonResponse({'error': 'Internal Server Error'}, status=500)


@csrf_exempt
def login(request):
    if request.method == 'POST':
        body_json = json.loads(request.body)

        if 'name' not in body_json or 'password' not in body_json:
            return JsonResponse({'error': 'Missing parameters or incorrect parameters'}, status=400)

        json_name = body_json['name']
        json_password = body_json['password']

        try:
            user = User.objects.get(name=json_name)
        except User.DoesNotExist:
            return JsonResponse({'error': 'User does not exist'}, status=404)
        if bcrypt.checkpw(json_password.encode('utf8'), user.password.encode('utf8')):
            token = secrets.token_hex(10)
            user.token = token
            user.save()
            return JsonResponse({'token': token}, status=201)
        else:
            return JsonResponse({'error': 'Incorrect password'}, status=401)

    if request.method == 'DELETE':
        try:
            token = request.headers.get("token")
        except:
            return JsonResponse({'error': 'Non existent token'}, status=400)

        if not User.objects.filter(token=token).exists():
            return JsonResponse({'error': 'User not found'}, status=404)

        user = User.objects.get(token=token)
        user.token = ""
        user.save()

        return JsonResponse({'success': 'Session closed'})
    else:
        return JsonResponse({'error': 'Internal Server Error'}, status=500)


#NO VA, MIRA POR QUÉ, EL TOKEN LLEGA NULL
#TIENE QUE LLEVAR EL STAND EN LA PETICIÓN
@csrf_exempt
def upload_book(request, stand_name):
    if request.method == 'POST':
        try:
            token = request.headers.get("token")
        except:
            return JsonResponse({'error': 'Missing data'}, status=400)

        if not User.objects.filter(token=token).exists():
            return JsonResponse({'error': 'User not found'}, status=404)

        try:
            # Decodificando el cuerpo de la solicitud con la codificación 'latin-1'
            data = json.loads(request.body.decode('latin-1'))
        except json.JSONDecodeError:
            return JsonResponse({'error': 'Invalid JSON format in request body'}, status=400)

        user = User.objects.get(token=token)

        title = data["title"]
        author = data["author"]
        num_pages = data["num_pages"]
        editorial = data["editorial"]
        review = data["review"]
        # Buscar la instancia de Stand basada en el nombre recibido
        try:
            stand_instance = Stand.objects.get(name=stand_name)
        except Stand.DoesNotExist:
            return JsonResponse({'error': 'Stand not found'}, status=404)

        # Crear el libro con la instancia de Stand correcta
        Book.objects.create(
            title=title,
            author=author,
            num_pages=num_pages,
            editorial=editorial,
            review=review,
            stand_name=stand_instance,  # Aquí se asigna la instancia de Stand
            user_name=user
        )

        return JsonResponse({'message': 'Book uploaded successfully'}, status=201)
