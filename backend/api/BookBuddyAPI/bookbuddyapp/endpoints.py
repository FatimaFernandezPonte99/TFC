import secrets
import traceback
import bcrypt

from django.http import JsonResponse
from django.views.decorators.csrf import csrf_exempt
from django.core.exceptions import ValidationError
from .models import User, Stand, Book
import json


#Endpoint para registrar un nuevo usuario en la base de datos
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


#Endpoint para iniciar sesión y también para cerrar sesión
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


#Endpoint para subir un nuevo libro a un determinado puesto
#Le pasas el nombre del puesto en la petición, luego busca la instancia de ese puesto
@csrf_exempt
def upload_book(request, stand_id):
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
            stand_instance = Stand.objects.get(pk=stand_id)
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


#Endpoint para obtener (GET) la información del perfil del usuario
def profile_info(request):
    if request.method == 'GET':
        try:
            token = request.headers.get("token")
        except:
            return JsonResponse({'error': 'Missing data'}, status=400)

        try:
            User.objects.get(token=token)
        except User.DoesNotExist:
            return JsonResponse({'error': 'User not found'}, status=404)

        try:
            user = User.objects.get(token=token)
            json_response = {
                'name': user.name,
                'country': user.country
            }
            return JsonResponse(json_response, safe=False, status=200)
        except User.DoesNotExist:
            return JsonResponse({'error': 'User not found'}, status=404)


#Endpoint para que el usuario pueda actualizar la información del perfil
@csrf_exempt
def update_profile(request):
    if request.method == 'PATCH':
        body_json = json.loads(request.body)

        try:
            token = request.headers.get("token")
        except :
            return JsonResponse({'error': 'Miissing data'}, status=400)

        try:
            user = User.objects.get(token=token)
        except User.DoesNotExist:
            return JsonResponse({'error': 'User does not exist'}, status=404)

        if 'name' in body_json:
            user.name = body_json['name']
        if 'password' in body_json:
            user.password = body_json['password']
        if 'country' in body_json:
            user.country = body_json['country']

        user.save()

        return JsonResponse({'message': 'Profile updated successfully'}, status=201)

    else:
        return JsonResponse({'error': 'Invalido HTTP method'}, status=405)


#Endpoint para mostrar el historial de los libros subidos por el usuario
def books_history(request):
    if request.method == 'GET':
        try:
            token = request.headers.get("token")
        except:
            return JsonResponse({'error': 'Missing data'}, status=400)

        try:
            user = User.objects.get(token=token)
        except User.DoesNotExist:
            return JsonResponse({'error': 'User not found'}, status=404)

        json_response = []

        all_books = Book.objects.filter(user_name=user)

        for book in all_books:
            json_response.append(book.to_json())
        return JsonResponse(json_response, safe=False)


#Endpoint para mostrar toda la información pertinente sobre un determinado libro
def book_info(request, book_id):
    if request.method == 'GET':
        try:
            token = request.headers.get("token")
        except:
            return JsonResponse({'error': 'Missing data'}, status=400)

        try:
            book = Book.objects.get(pk=book_id)
            json_response = {
                'title': book.title,
                'author': book.author,
                'num_pages': book.num_pages,
                'editorial': book.editorial,
                'review': book.review
            }
            return JsonResponse (json_response, safe=False, status=200)
        except Book.DoesNotExist:
            return JsonResponse({'error': 'Book not found'}, status=404)


#Endpoint para mostrar la información del Stand
def stand_info(request, stand_id):
    if request.method == 'GET':
        try:
            token = request.headers.get("token")
        except:
            return JsonResponse({'error': 'Missing data'}, status=400)

        try:
            stand = Stand.objects.get(pk=stand_id)
            json_response = {
                'name': stand.name,
                'address': stand.address
            }
            return JsonResponse (json_response, safe=False, status=200)
        except Stand.DoesNotExist:
            return JsonResponse({'error': 'Stand not found'}, status=404)


#Endpoint para mostrar los libros que hay en un determinado stand
def stand_books(request, stand_id):
    if request.method == 'GET':
        try:
            token = request.headers.get("token")
        except:
            return JsonResponse({'error': 'Missing data'}, status=400)

        try:
            stand = Stand.objects.get(pk=stand_id)
        except Stand.DoesNotExist:
            return JsonResponse({'error': 'Stand not found'}, status=404)

        json_response = []

        all_books = Book.objects.filter(stand_name=stand)

        for book in all_books:
            json_response.append(book.to_json())
        return JsonResponse(json_response, safe=False)



@csrf_exempt
def exchanged_book(request, book_id):
    if request.method == 'PATCH':
        try:
            token = request.headers.get("token")
        except:
            return JsonResponse({'error': 'Missing data'}, status=400)

        try:
            book = Book.objects.get(pk=book_id)
        except Book.DoesNotExist:
            return JsonResponse({'error': 'Book not found'}, status=404)

        try:
            stand = Stand.objects.get(name="Prestados")
        except Stand.DoesNotExist:
            return JsonResponse({'error': 'Stand not found'}, status=404)

        book.stand_name = stand

        book.save()

        return JsonResponse({'message': 'Book updated to Prestados'}, status=201)

    else:
        return  JsonResponse({'error': 'Invalid HTTP method'}, status=405)

