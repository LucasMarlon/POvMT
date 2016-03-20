var express = require("express");
var app = express();
var mongo = require("mongodb");
var bodyParser = require("body-parser");
var schedule = require("node-schedule");
 
 
app.use(bodyParser.json());
app.use(bodyParser.urlencoded({
    extended:true
}));
 
new mongo.Db("povmt", new mongo.Server(process.env.OPENSHIFT_MONGODB_DB_HOST || "localhost", process.env.OPENSHIFT_MONGODB_DB_PORT || 27017)).open(function(err, client){
    if (err) {
        console.log("ERRO: " + err);
    } else {
        client.authenticate(process.env.OPENSHIFT_MONGODB_DB_USERNAME, process.env.OPENSHIFT_MONGODB_DB_PASSWORD, function(err) {
            
			if (err) {
                console.log("ERRO DE AUTENTICAÇÃO: " + err);
            } else {
                console.log("SUCESSO!");
                client.collection("atividade", function(err, collection){
                    if (err){
                        console.log("ERRO: " + err);
                    } else {
						require("./models/atividade")(mongo, app, collection, schedule);
                    }
                });
            }
        });
    }
});
 
app.get("/", function(req, res) {
    res.send("Hello world");
});
 
app.listen(process.env.OPENSHIFT_NODEJS_PORT || 8080, process.env.OPENSHIFT_NODEJS_IP || "localhost");