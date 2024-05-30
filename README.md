# TFC BookBuddy 

![logo_bookbuddy](https://github.com/fatimafernandezponte/TFC/assets/145338446/d134ae2a-46c6-485f-a1fc-852e6d6b7731)


## Descripción
BookBuddy es una aplicación que consiste en un servicio de intercambio de libros entre ciudadanos. En la aplicación se podrá ver un mapa con los puestos, los libros disponibles en cada puesto, así como también dará la opción de subir un libro a un puesto. En cada libro se podrán ver sus datos, así como una breve reseña dejada por el usuario que ha subido el libro. Esta aplicación nace con el deseo y necesidad de promover la cultura lectora en A Coruña.

## Características

- Ver un mapa con los puestos de libros.
- Ver los libros disponibles en cada puesto.
- Ver los datos de cada libro, así como una breve reseña del usuario que ha subido el libro.
- Subir un libro a un puesto.
- Ver el historial de los libros subidos por el usuario.
- Adquirir un libro de un puesto.
- Modificar la información del perfil de usuario.
- Iniciar sesión, registrarse y cerrar sesión.


## Estado del proyecto
Actualmente, este proyecto se encuentra finalizado.


## Tecnologías utilizadas
- Django (Python)
- Android Studio
- Miro


## Requisitos del sistema

### Requisitos de Hardware: 
 - Procesador: Intel Core i5 o superior.
 - Memoria RAM: Mínimo 8 GB de RAM.
 - Espacio en disco: Al menos 5 GB de espacio libre en el disco duro.
 - Tarjeta gráfica: NVIDIA GeForce GTX 1050 
   Intel(R) UHD Graphics 630 o superior
 - Conectividad: Conexión a internet de banda ancha estable

### Requisitos de Software:
 - Sistema operativo: Windows 10 o superior.
   #### Para el Backend (Django):
      - Python: Python 3.x.
      - Gestor de base de datos: SQLite
      ##### Librerías utilizadas: 
         - asgiref==3.8.1
         - bcrypt==4.1.2
         - certifi==2024.2.2
         - charset-normalizer==3.3.2
         - Django==5.0.4
         - idna==3.7
         - requests==2.31.0
         - sqlparse==0.5.0
         - tzdata==2024.1
         - urllib3==2.2.1
   #### Para el Frontend (Android Studio):
      - Android Studio Hedgehog 2023.1.1 o superior
      - JDK 8 o superior
      - SDK de Android.
      ##### Librerías utilizadas:
         - implementation("androidx.appcompat:appcompat:1.6.1")
         - implementation("com.google.android.material:material:1.9.0")
         - implementation("androidx.constraintlayout:constraintlayout:2.1.4")
         - implementation("com.android.volley:volley:1.2.1")
         - implementation("com.google.android.gms:play-services-location:21.2.0")
         - implementation("com.google.maps.android:android-maps-utils:2.3.0")
         - implementation("com.google.android.gms:play-services-maps:18.2.0")
         - testImplementation("junit:junit:4.13.2")
         - androidTestImplementation("androidx.test.ext:junit:1.1.5")
         - androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

## Acceso al proyecto
Para acceder al proyecto, siga los siguientes pasos:
1. Clone el repositorio a su máquina local:
git clone git@github.com:fatimafernandezponte/TFC.git

2. Instale las librerías mencionadas en el apartado anterior

3. Abra el CMD y navegue hasta el directorio en el que se encuentra el archivo manage.py 

4. Inicie el servidor escribiendo en el cmd lo siguiente:
python manage.py runserver

5. Abra Android Studio y ejecute la aplicación


## Personas desarrolladoras del proyecto
 **[Fátima Fernández Ponte](https://github.com/fatimafernandezponte)**

 ## Licencia
Este proyecto no tiene una licencia específica. Todos los derechos están reservados.

