/*
* @author Merli Mejia
* @version 0.1
*
* ESTE SOFTWARE FUE CREADO CON EL FIN DE PRACTICAR TANTO JAVA COMO NODE JS
*
* NODE JS DEBE ESTAR INSTALADO EN LA PC PARA SU BUEN FUNCIONAMIENTO
* 
* GIT: https://github.com/JavaNigga/JNTF


*
 */

var express = require('express')
var fs = require("fs"), 
path = require("path");
var url = require('url');
const drivelist = require('drivelist');
var http = require('http');
var router = express.Router();

//inicializar app
var app = express()
app.get('/', function (req, res) {
    res.redirect("/directorios");
  console.log('corriendo');
})
//Poder usar archivos del server
app.use(express.static(__dirname));


//Mostrar los directorios principales: los discos
app.get('/directorios', function(req, res){
  //escribir un archivo html que esta en los archivos
  var array = __dirname.split('\\');
  var laRuta;
  for(var i = 0; i < array.length; i++)
  {
    if(i == 0)
    {
      laRuta = array[i]
    }else
    {
      laRuta += "/" + array[i];
    }
  }
  console.log(laRuta);
  fs.readFile(laRuta + '/public/index.html' , 'utf8', function(err, html){
    res.write(html);
    //obtener la lista de los drivers en la pc
    drivelist.list((err, drivers, callback = function(){res.end()})=>{
      if(err)
      {
        throw err;
      }
      res.write("<ul style='padding: 0; display: grid'>");
      for(let i = 0; i < drivers.length; i++)
      {
        //console.log(drivers[i]['mountpoints'][0]['path'].replace('\\', "/"));
        //escribir los drivers
        
      
        //res.write("<a href=" + "\'" + "/irA?ruta=" + drivers[i]['mountpoints'][0]['path'].replace('\\', "/") + "\'" + "a>" + "<li class='items'>" + drivers[i]['mountpoints'][0]['path'].replace('\\', "") + '</li>' + "</a>");
        res.write("<li onClick=" + "\"" + "window.location.href = '/irA?ruta=" + 
        drivers[i]['mountpoints'][0]['path'].replace('\\', "/") + "\'" + "\"" + "class='items'>" +  
        "<ul class='contenedorItems' style='padding:0;'>" + "<li style='float:left'>" + "<img class='imagenes' src='/public/folder.png'>" + "</li>" +  
        "<li class='contenedorLetras' style='float:left'>" + "<h1 class='letras'>" + drivers[i]['mountpoints'][0]['path'].replace('\\', "") + "</h1>" + '</li>' + "</ul>")
        
        
      }
      res.write('</ul>');
      callback();
      
    })
    
  });

});

app.get('/descargar', function(req, res){

  var ruta = url.parse(req.url, true).query.ruta;
  var archivo = url.parse(req.url, true).query.archivo;
  console.log(ruta + "\n" + archivo)
  res.download(ruta+archivo);

})

//Ir a una determinada ruta
app.get('/irA', function(req, res){
  var ruta = url.parse(req.url, true).query.ruta;
  var directorios = new Array();
  var files = fs.readdirSync(ruta);
  for(var i in files)
      {
        directorios.push(files[i]);
      }
      var array = __dirname.split('\\');
      var laRuta;
      for(var i = 0; i < array.length; i++)
      {
        if(i == 0)
        {
          laRuta = array[i]
        }else
        {
          laRuta += "/" + array[i];
        }
      }
      fs.readFile(laRuta + '/public/index.html', 'utf8', function(err, html){
        res.write(html);
        res.write("<a id='inicio' href=" + "\'" + "/directorios" + "\'" + ">" + "INICIO" + "</a>")
        res.write("<h3 id='indicadorRuta'>" + ruta + "</h3>");
        res.write("<ul style='padding: 0'>");
        for(var i in directorios)
        {
          //ver si el archivo se puede leer
          try{
            //ver si es una carpeta
            if(fs.lstatSync(ruta + "/" + directorios[i]).isDirectory())
            {
              //res.write("<li class='items'>" + "<a href=" + "\'" + "/irA?ruta=" + ruta + "/" + directorios[i] + "\'" + "a>" + directorios[i] + "</a>" + '</li>');
              
              res.write("<li onClick=" + "\"" + "window.location.href = '/irA?ruta=" + 
              ruta + "/" + directorios[i] + "\'" + "\"" + "class='items'>" +  
              "<ul class='contenedorItems' style='padding:0;'>" + "<li style='float:left'>" + "<img class='imagenes' src='/public/folder.png'>" + "</li>" +  
              "<li class='contenedorLetras' style='float:left'>" + "<h1 class='letras'>" + directorios[i] + "</h1>" + '</li>' + "</ul>")
              
              
              //ver si es un archivo
            }else if(fs.lstatSync(ruta + "/" + directorios[i]).isFile())
            {
              var Laruta = ruta + "/";
              var elArchivo = directorios[i]
             /* res.write("<li class='items'>" + 
                  '<ul>' + 
                    '<li>' + directorios[i] + '</li>' +
                    '<li><a href=' + "\'" + "descargar?ruta=" + Laruta + "&archivo=" + elArchivo + "\'" + ">Descargar</a>" + '</li>' +
                  '</ul>' +  
              "</li>");*/

              var archivoText = directorios[i];
              if(archivoText.length > 15)
              {
                archivoText = archivoText.substring(0,15) + "...";
              }

              res.write("<li class='items'>" +  
              "<ul class='contenedorItems' style='padding:0;'>" + "<li style='float:left'>" + "<img class='imagenes' src='/public/file.png'>" + "</li>" +  
              "<li class='contenedorLetras' style='float:left'>" + "<h1 class='letras'>" + archivoText + "</h1>" + '</li>' +
              "<li onClick=" + "\"" + "window.location.href = 'descargar?ruta=" + Laruta + "&archivo=" + elArchivo + "\'" + "\"" + "class='contenedorLetras' style='float:right'>" + "<img class='imagenes descargar' src='/public/download.png'>" + "</li>" + "</ul>")
            }
          }catch(err)
          {
            //console.log(err)
          }
          
        }
        res.write('</ul>');
        res.end();
      })
      
})

app.listen(3000)