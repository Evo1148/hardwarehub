# Aviso de correccion posterior asistida por IA

Este conjunto de cambios fue preparado por **OpenAI Codex** el **8 de agosto de 2026**, despues de la version original de HardwareHub.

La intervencion corrige la ejecucion automatizada de pruebas y dos problemas de prioridad alta detectados durante la revision:

- Se creo un perfil de pruebas con una base de datos H2 efimera.
- Se activo ese perfil en la prueba de carga del contexto de Spring.
- Se agrego un workflow de GitHub Actions para ejecutar `mvn verify` con Java 17.
- Se limito el detalle de un pedido para que cada vendedor solo vea sus propias lineas y su subtotal, sin exponer las ventas de otros vendedores.
- Se bloqueo de forma pesimista cada fila de producto durante el checkout para impedir sobreventas concurrentes.
- Los bloqueos de stock se adquieren en un orden estable para reducir el riesgo de interbloqueos.
- Se incorporaron pruebas de regresion para los permisos del detalle y el control transaccional del stock.

La correccion parte del commit `7b49d1f8d5ec6ba038bfa50c343db35394d285bc` de `main`. Este aviso atribuye exclusivamente la correccion posterior; no atribuye a la IA la autoria del proyecto original.

Los cambios deben revisarse como cualquier otra contribucion antes de integrarse en la rama principal.
