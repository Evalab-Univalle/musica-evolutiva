


# Música evolutiva 




## Contenido del CD


-`Archivos_interfaz` 			Directorio con el código fuente del documento.
    -`newGeneration`			Directorio con los archivos de sonido de la ultima generación producida.
    -`oldGeneration`			Directorio con los archivos de sonido de la generación anterior.
    -`ratingfiles` 			Directorio que contiene los archivos del script utilizado en la votación.
    -`ratingtxt`				Directorio que contiene los archivos producidos por la votación .
    -`index.php`				Interfaz de votación de la aplicación.
    -`principal.css`			Archivo con los estilos de la interfaz de votación.

-`Proyecto`				    Directorio con el código fuente de la aplicación.
    -`build`				    Directorio con archivos generados por netBeans.
    -`nbproyect`				Directorio con archivos generados por netBeans.
    -`newGeneration`			Directorio con los archivos de sonido de la ultima generación producida.
    -`oldGeneration`			Directorio con los archivos de sonido de la generación anterior.
    -`primeraGeneation`		Directorio con los archivos de sonido de la población inicial.
    -`ratingtxt`				Directorio que contiene los archivos producidos por la votación .
    -`src`					    Directorio que contiene el código fuente de la aplicación.
    -`build.xmlcion`			Archivo generado por netBeans.
    -`manifest.mf`				Archivo generado por netBeans.

-`Pruebas_Musica`			    Directorio con los archivos de sonido correspondientes a las pruebas realizadas con canciones.

-`Musica_Evolutiva.pdf`		Documento del proyecto

Las carpetas `newGeneration`,  `oldGeneration` y `ratingtxt` de `Proyecto` y `Archivos_interfaz` contendran los mismos archivos. 




## Uso

El contenido de la carpeta de Archivos_interfaz se copia donde este la carpeta que contiene los archivos web, y se le dan todos los permisos a la carpeta de `ratingtxt` .

Al tener los votos suficientes de la interfaz se toma el archivo `rtgitems` contenido en la carpeta `ratingtxt` de la interfaz y se copia en la carpeta con el mismo nombre del proyecto, se ejecuta el proyecto, y se copian las carpetas  `newGeneration`, `oldGeneration` y `ratingtxt` del proyecto a la carpeta de la interfaz remplazando los archivos anteriores.
