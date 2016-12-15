<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
    <head>
        <title>Musica Evolutiva / EVALAB / Universidad del Valle / Cali, Colombia</title>
        <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
        <link href="ratingfiles/ratings.css" rel="stylesheet" type="text/css" />
        <link href="principal.css" rel="stylesheet" type="text/css" />
        <link rel="stylesheet" href="http://code.jquery.com/ui/1.10.3/themes/smoothness/jquery-ui.css" />
        <script src="http://code.jquery.com/jquery-1.9.1.js"></script>
        <script src="http://code.jquery.com/ui/1.10.3/jquery-ui.js"></script>
        <script src="ratingfiles/ratings.js" type="text/javascript"></script>
        <script>
            $(function() {
                $( "#tabs" ).tabs();
            });
        </script>
        </head>
    <body>
        <div class="encabezado">
            <h1>M&uacute;sica Evolutiva </h1>
        </div>
        <div id="tabs" class="contenidoBox">
            <ul>
                <li><a href="#proyecto">El proyecto</a></li>
                <li><a href="#funcionamiento">Funcionamiento</a></li>
                <li><a href="#encuesta">Evaluaci&oacute;n</a></li>

            </ul>
            <div id="proyecto">
                <p>
                    M&uacute;sica evolutiva es un proyecto investigativo que busca generar m&uacute;sica, con la ayuda de algoritmos evolutivos (Ver pesta&ntilde;a de Funcionamiento), a partir de sonidos aleatorios.
                </p>
                <p> 
                    Para lograr tal fin, por favor seleccione qu&eacute; sonido considera mejor.
                </p>
                <p>
                    Tenga en cuenta los siguientes criterios de acuerdo al n&uacute;mero de estrellas:
                </p>
                <div class="cajaCalificacion">
                    
                    <div class="linea"><div class="stars"><div class="stars1"></div></div>Regular</div>
                    <div class="linea"><div class="stars"><div class="stars2"></div></div>Aceptable</div>
                    <div class="linea"><div class="stars"><div class="stars3"></div></div>Bueno</div>
                    <div class="linea"><div class="stars"><div class="stars4"></div></div>Muy Bueno</div>
                    <div class="linea"><div class="stars"><div class="stars5"></div></div>Exelente</div>
                </div>
                <p>
                    Tambi&eacute;n debe saber que cada vez que ingrese nuevamente, se recargue la p&aacute;gina, o d&eacute; click en Nuevos sonidos, aparecer&aacute;n seis sonidos distintos.
                </p>



                <?php

                    $fichero = @fopen('ratingtxt/listado.txt', 'rb', true);

                    if (!$fichero) {
                        echo 'No se puede abrir el fichero.';
                    }

                    $lineas = fgets($fichero);
                    $conjunto_audio_box = array();
                    $contenido='';

                    for($i=0;$i<$lineas;$i++){
                        $linea = fgets($fichero);
                        $token = strtok($linea, " ");
                        $archivo = $token;
                        $audio_box =
                            '<div class="audio_box">
                                 <div class="Audio">
                                     <audio controls> 
                                         <source src="newGeneration/'.$archivo.'" type="audio/wav">
                                     </audio>
                                 </div>
                                 <div class="srtgs" id="rt_'.$archivo.'"></div>
                             </div>
                            ';
                        array_push($conjunto_audio_box, $audio_box);
                    }

                    $ramdomKeys = array_rand($conjunto_audio_box, 6);

                    for($i=0;$i<count($ramdomKeys);$i++){
                        $contenido .= $conjunto_audio_box[$ramdomKeys[$i]];	
                    }

                    $contenido .= 
                                '<form method="post">  
                                    <input type="button" value="Nuevos sonidos" onclick="window.location.reload()" />
                                 </form>';   

                    echo '<div class="contenido">'.$contenido.'</div>';
                ?>  
            </div>
            <div id="funcionamiento">
                <p>
                   Como en los algoritmos evolutivos, las bases de este proyecto son las mismas:</br></br>

1. Tener una poblaci&oacute;n: en este caso, un grupo de sonidos tomados del medio ambiente y otros tantos generados computacionalmente. Esto cumple con una regla importante que es la distinci&oacute;n entre los individuos de la poblaci&oacute;n: la variabilidad.</br></br>

2. Tener la capacidad de reproducirse o mezclarse: En este sentido, gracias a que los sonidos se pueden representar mediante ondas, podemos tomar partes ellas y mezclarlas (sumarlas y promediarlas), o tambi&eacute;n tomar partes de una y juntarlas con partes de otra.</br></br>

3. Generar mutaciones o variabilidad: existen unas funciones que toman a los sonidos y les aplican distintos filtros de audio que incluyen paso-alto, paso-bajo, ecos, entre otras. Esto lleva a que las canciones var&iacute;en a&uacute;n m&aacute;s y sean siempre distintas.</br></br>

4. La selecci&oacute;n: es el objetivo de esta p&aacute;gina, puesto que la evoluci&oacute;n debe tener un direccionamiento y as&iacute; saber qu&eacute; individuos pasar&aacute;n a la siguiente generaci&oacute;n.</br></br>

Teniendo en cuenta las bases mencionadas, estas se repiten una y otra vez hasta generar, en este caso, sonidos, a pesar de que no sean tan armoniosos, como una composici&oacute;n normal. y no sean lineales. Esto ocurre debido a que todas las funciones seleccionadas, las mezclas y los gustos de las personas son aleatorios.</br></br>

Con esto se podr&iacute;a generar una nueva definici&oacute;n de lo que es la m&uacute;sica, teniendo en cuenta la uni&oacute;n, en un sonido, de los distintos gustos musicales de las personas que voten en este proyecto.</br></br>
                </p>
            </div>
            <div id="encuesta">
                <iframe src="https://docs.google.com/forms/d/1zdf2AFr3RuQOM_XNQwZODM7N-aIinBKx6QX5i6OhXq0/viewform?embedded=true" width="480" height="500" frameborder="0" marginheight="0" marginwidth="0">Loading...</iframe></a></p>
            </div>
        </div>
    </body>
</html>
