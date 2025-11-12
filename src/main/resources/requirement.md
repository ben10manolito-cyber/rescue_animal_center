Enunciado del Proyecto:
Desarrollar una API REST para la gestión de un sistema de centro de adopcion y rescate para animales
inteligentes, implementada en Java (versión superior a Java 23),utilizando Spring
Boot como framework principal y Lombok para simplificar la escritura de código
repetitivo. Los datos deberán ser almacenados y gestionados en un archivo CSV
(separado por comas) que actúe como repositorio local de información.
El sistema debe permitir realizar operaciones CRUD sobre al menos 10 clases del
dominio, entre ellas:  a,habitat, eventoEducativo,Donacion,TratamientoMedico, Adoptante (extends Persona),Voluntario (extends Persona),Rescatista (extends Persona), Persona,Animal
El proyecto deberá aplicar los principios clave de la Programación Orientada a Objetos
(POO), evidenciando su uso de forma clara y estructurada:
· Encapsulamiento: controlando el acceso a los atributos mediante
getters/setters generados con Lombok.
·Herencia:estableciendo jerarquías como diferentes tipos de vehículos (Auto,
Moto, Camioneta) o distintos tipos de empleados.
· Polimorfismo:permitiendo distintos comportamientos para operaciones
compartidas, como el cálculo del costo total según el tipo de vehículo o seguro.
· Interfaces: definiendo contratos comunes, como servicios de
persistencia, facturación o validación de datos.
·Composición y agregación: representando relaciones entre clases,por ejemplo,
un contrato que agrupa vehículo, cliente y seguro, o una sucursal que
administra múltiples reservas.
El proyecto deberá incluir además clases abstractas, records y enumeradores para
modelar comportamientos y valores constantes del dominio (por ejemplo, estado del
vehículo, tipo de combustible, o modalidad de pago).
La API REST debe exponer endpoints bien estructurados para gestionar cada
entidad,siguiendo buenas prácticas de arquitectura, modularidad,documentación y
mantenimiento del código.