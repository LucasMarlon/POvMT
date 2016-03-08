module.exports = function(app, usersCollection) {
	app.post("/cadastrar", function (req, res){
		var login = req.body.login;
		var email = req.body.email;
		var senha = req.body.senha;
		
		usersCollection.find({
			login: login
			
		}).toArray(function(err, array){
			if(err) {
				console.log("ERRO: " + err);
			} else {
				if(array.length > 0) {
					res.send("LOGIN EXISTENTE");
				} else {
					usersCollection.insert({
					login: login,
					email: email,
					senha: senha
					
					},function(err, doc){
						if(err){
							res.send("ERRO: " + err);
						} else {
							res.send("SUCESSO");	
						}
					});
				}				
			}
		});			
	});

	app.post("/login", function(req, res){
		var login = req.body.login;
		var senha = req.body.senha;
		
		usersCollection.find({
			login:login,
			senha: senha
			
		}).toArray(function(err, array) {
			if(err){
				console.log("ERRO: " + err);
			} else {
				if(array.length == 0){
					res.send("FALHA");
				} else if (array.length > 0){
					res.send("LOGIN SUCESSO");
				}
			}
			
		});
	});
	return this;
}