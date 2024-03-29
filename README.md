# TALLER 1: APLICACIONES DISTRIBUIDAS (HTTP, SOCKETS, HTML, JS,MAVEN, GIT)

En este proyecto se creo una aplicación usando Java, la cual crea un servidor usando WebSockets para realizar la conexión cliente-servidor, esta aplicación funciona como gateway entre el cliente y el API https://www.omdbapi.com/, con la finalidad de conocer información basica de una pelicula especifica a través del titulo de la pelicula. En este proyecto se priorizo la eficiencia realizando la implementación de una memoria caché que almacena las busquedas que se realizan para asi ahorrar recursos en la consulta al API y mejorar los tiempos de respuesta.

![image](https://github.com/Mateo0laya/Taller-1-Aplicacion-distribuidas---AREP/assets/89365336/923aa8bb-5f9c-40aa-ac45-871f70cf207b)

## Arquitectura 
La arquitectura debe tener las siguientes características.

1. El cliente Web debe ser un cliente asíncrono que corra en el browser  y use Json como formato para los mensajes.
2. El servidor de servirá como un gateway para encapsular llamadas a otros servicios Web externos.
3. La aplicación debe ser multiusuario.
4. Todos los protocolos de comunicación serán sobre HTTP.
5. Los formatos de los mensajes de intercambio serán siempre JSON.
6. La interfaz gráfica del cliente debe ser los más limpia y agradableolo HTML y JS (Evite usar librerías complejas). Para invocar métodos REST desde el cliente usted puede utilizar la tecnología que desee.
7. Debe construir un cliente Java que permita probar las funciones del servidor fachada. El cliente utiliza simples conexiones http para conectarse a los servicios. Este cliente debe hacer pruebas de concurrencia en su servidor de backend.
8. La fachada de servicios tendrá un caché que permitirá que llamados que ya se han realizado a las implementaciones concretas con parámetros específicos no se realicen nuevamente. Puede almacenar el llamado como un String con su respectiva respuesta, y comparar el string respectivo. Recuerde que el caché es una simple estructura de datos.
9. Se debe poder extender fácilmente, por ejemplo, es fácil agregar nuevas funcionalidades, o es fácil cambiar el proveedor de una funcionalidad.
10. Debe utilizar maven para gestionar el ciclo de vida, git y github para almacenar al código fuente y heroku como plataforma de producción.
11. En el backend debe utilizar solo Java. No puede utilizar frameworks como SPRING.

## Diseño de la aplicación

La aplicacipon fue diseñada de tal manera que se cumplan los requisitos establecidos en la arquitectura.

- La clase `HttpServer` contiene la lógica del servidor. Recibe las solicitudes de los usuarios y hace el llamado a la API.
- El servidor entrega a los usuarios un cliente asíncrono al cual pueden acceder desde cualquier navegador.
- Al realizar una consulta desde la aplicación, el servidor hace el llamado a una API externa.
- La respuesta que es dada por la API es validada para devolver al usuario la información correcta. En caso de que la película no se encuentra en la API, se mostrará al usuario un mensaje adecuado.
- La clase `HttpConnection` realiza la conexión a OMDb API en el método `query`, al cual se le pasa como argumento el título de la película. Si la película es encontrada, se retorna un String con los datos, de lo contrario, se establecen mecanismos para validar si la película no fue encontrada y mostrar al usuario el estado de la consulta.
- `HttpServer` almacena en una estructura de datos concurrente `HashMap` un caché de las consultas hechas a la API, lo que acorta considerablemente los tiempos de respuesta.

# Extensión de la aplicación

- Desde la clase HttpConnection se encuentran guardadas las variables como url para ocnsultar a la API, desde alli basta con crear nuevos metodos modificando la url para agregar nuevas funcionalidades que permite la API.
- En la clase HttpServer se agrego una variable port, la cual permite cambiar de manera rapida y sencilla el puerto atraves del cual se ejecutará el servidor, ya sea por que el 35000 se encuentra ocupado o por que desean establecer la comunicación po run puerto especifico, se recomienda no hacer uso de los well known ports

## Guia de inicio

Estas instrucciones le permitirán obtener una copia del proyecto en funcionamiento en su máquina local para fines de desarrollo y prueba. Consulte implementación para obtener notas sobre cómo implementar el proyecto en un sistema en vivo.

### Prerequisitos

- Java 8
- Maven
- Git
- Navegador web

### Instalación

Ubiquese en el directorio en donde desea descargar el repositorio

`git clone https://github.com/Mateo0laya/Taller-1-Aplicacion-distribuidas---AREP.git`

Cambie al directorio del repositorio

`cd Taller-1-Aplicacion.distribuidas---AREP`

Compile el proyecto

`mvn compile`

Empaquete el proyecto

`mvn package`

Inicie el servidor

`java -cp target\appps-distribuidas-1.0-SNAPSHOT.jar edu.escuelaing.AREP.Taller1.HttpServer`

Una alternativa a la linea de comandos es realizar la ejecución desde un IDE. En este caso Visual Studio Code
![image](https://github.com/Mateo0laya/Taller-1-Aplicacion-distribuidas---AREP/assets/89365336/3db3884b-61d5-4a6f-a735-14e72480b78a)

## Probando la aplicación

Una vez recibimos el mensaje de confirmacion (Ready to recive...) estamos listos para acceder al servidor desde un navegador como Google chrome, Mozilla, Safari, etc.
Para lo cual debemos dirigirmos a la dirección
http://localhost:35000/

En caso de cambiar el puerto debemos reemplazar 35000 por el puerto que seleccionamos en el codigo fuente de la aplicación

Una vez dentro encontraremos una interfaz muy sencilla invitandonos a introducir el nomobre de la pelicula
![image](https://github.com/Mateo0laya/Taller-1-Aplicacion-distribuidas---AREP/assets/89365336/f355ab4a-f19b-4320-a374-fe5fb4bc2f82)

Es importante que al introducir el titulo de la pelicula que deseamos buscar, realicemos la busqueda dando click en el boton "submit", de lo contrario al dar enter no mostrará el resultado correctamente

A partir de aqui plantemaos dos posibles escenarios

### Escenario #1: El nombre de la pelicula fue encontrado en la API
En este caso recibiremos la información correspondiente a la pelicula que deseamos consultar.
![image](https://github.com/Mateo0laya/Taller-1-Aplicacion-distribuidas---AREP/assets/89365336/858bea99-3c08-4db4-a9a4-8600b1682ea2)

### Escenario #2: El nombre de la pelicula no fue encontrado en la API
En este caso recibiremos un error indicandonos que la pelicula no fue encontrada
![image](https://github.com/Mateo0laya/Taller-1-Aplicacion-distribuidas---AREP/assets/89365336/114138e5-0588-4071-8319-d13a36a7e1f9)

## Pruebas unitarias

Para correr las pruebas unitarias del proyecto, desde la terminal y en el directorio del proyecto ejecutamos
'mvn test' 

![image](https://github.com/Mateo0laya/Taller-1-Aplicacion-distribuidas---AREP/assets/89365336/431a3d1a-1fd6-4189-8941-854752623a22)
De igual modo lo podremos realizar desde el entorno grafico del IDE de nuestra preferencia

## Construido con

* [Java](https://www.java.com/es/) - The main programming language
* [Maven](https://maven.apache.org/) - Dependency Management

## Version

Version 1.0.0.

## Autor

Mateo Olaya Garzon
