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
    res.send("Corriendo");
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
      for(let i = 0; i < drivers.length; i++)
      {
        //console.log(drivers[i]['mountpoints'][0]['path'].replace('\\', "/"));
        //escribir los drivers
        res.write('<ul>');
      
        res.write("<li>" + "<a href=" + "\'" + "/irA?ruta=" + drivers[i]['mountpoints'][0]['path'].replace('\\', "/") + "\'" + "a>" + drivers[i]['mountpoints'][0]['path'].replace('\\', "") + "</a>" + '</li>');
        
        res.write('</ul>');
        
      }
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
        res.write('<a href=' + "\'" + "/directorios" + "\'" + ">" + "INICIO" + "</a>")
        res.write('<h3>' + ruta + "</h3>");
        res.write('<ul>');
        for(var i in directorios)
        {
          //ver si el archivo se puede leer
          try{
            //ver si es una carpeta
            if(fs.lstatSync(ruta + "/" + directorios[i]).isDirectory())
            {
              res.write("<li>" + "<a href=" + "\'" + "/irA?ruta=" + ruta + "/" + directorios[i] + "\'" + "a>" + directorios[i] + "</a>" + '</li>');
              //ver si es un archivo
            }else if(fs.lstatSync(ruta + "/" + directorios[i]).isFile())
            {
              var Laruta = ruta + "/";
              var elArchivo = directorios[i]
              res.write("<li>" + 
                  '<ul>' + 
                    '<li>' + directorios[i] + '</li>' +
                    '<li><a href=' + "\'" + "descargar?ruta=" + Laruta + "&archivo=" + elArchivo + "\'" + ">Descargar</a>" + '</li>' +
                  '</ul>' +  
              "</li>");
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