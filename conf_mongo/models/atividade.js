module.exports = function(mongodb, app, atividadeCollection) {
	app.post("/cadastrarAtividade", function (req, res){
		var usuario = req.body.usuario;
		var nomeAtividade = req.body.nomeAtividade;
		var dataInicioSemana = req.body.dataInicioSemana;
		var dataFimSemana = req.body.dataFimSemana;
		var prioridade = req.body.prioridade;
		var	foto = req.body.foto;
		var	categoria = req.body.categoria;
		var dataAtividade = req.body.dataAtividade;
		
		atividadeCollection.find({
			usuario: usuario,
			nomeAtividade: nomeAtividade,			
			dataInicioSemana: dataInicioSemana			
			
		}).toArray(function(err, array){
			if(err) {
				res.send('{ "ok" : 0, "msg" : "' + err + '" }');
			} else {
				if(array.length > 0) {
					res.send('{ "ok" : 0, "msg" : "ATIVIDADE EXISTENTE" }');
				} else {
					atividadeCollection.insert({
						usuario: usuario,
						nomeAtividade: nomeAtividade,
						dataInicioSemana: dataInicioSemana,
						dataFimSemana: dataFimSemana,
						prioridade: prioridade,
						foto: foto,
						categoria: categoria,
						dataAtividade: dataAtividade,						
						tempoInvestido : 0
					
					},function(err, doc){
						if(err){
							res.send('{ "ok" : 0, "msg" : "' + err + '" }');
						} else {
							res.send('{"ok": 1}');	
						}
					});
				}				
			}
		});			
	});
	
	app.post("/incrementaTempoInvestido", function (req, res){
		var usuario = req.body.usuario;
		var dataInicioSemana = req.body.dataInicioSemana;
		var nomeAtividade = req.body.nomeAtividade;
		var tempoInvestido = req.body.tempoInvestido;
		
		atividadeCollection.find({
			usuario: usuario,
			nomeAtividade: nomeAtividade,
			dataInicioSemana: dataInicioSemana
			
		}).toArray(function(err, array){
			if(err) {
				res.send('{ "ok" : 0, "msg" : "' + err + '" }');
			} else {
				if(array.length == 0) {
					res.send('{ "ok" : 0, "msg" : "ATIVIDADE INEXISTENTE" }');
				} else {
					atividadeCollection.update({
						usuario: usuario,
						nomeAtividade: nomeAtividade, 
						dataInicioSemana: dataInicioSemana
					},
					{ $inc: { tempoInvestido: parseInt(tempoInvestido) } 
					
					},function(err, doc){
						if(err){
							res.send('{"ok" : 0, "msg" : "' + err + '"}');
						} else {
							res.send('{"ok": 1}');	
							
						}
					});
				}				
			}
		});
		
	});
	
	app.post("/updateAtividade", function (req, res){
		var usuario = req.body.usuario;
		var nomeAtividade = req.body.nomeAtividade
		var dataInicioSemana = req.body.dataInicioSemana;
		var dataFimSemana = req.body.dataFimSemana;
		var prioridade = req.body.prioridade;
		var	foto = req.body.foto;
		var	categoria = req.body.categoria;
		var dataAtividade = req.body.dataAtividade;
		var tempoInvestido = req.body.tempoInvestido;
		
		atividadeCollection.find({
			usuario: usuario,
			nomeAtividade: nomeAtividade,
			dataInicioSemana: dataInicioSemana
			
		}).toArray(function(err, array){
			if(err) {
				res.send('{ "ok" : 0, "msg" : "' + err + '" }');
			} else {
				if(array.length == 0) {
					res.send('{ "ok" : 0, "msg" : "ATIVIDADE INEXISTENTE" }');
				} else {
					atividadeCollection.update({
						usuario: usuario,
						nomeAtividade:nomeAtividade, 
						dataInicioSemana: dataInicioSemana
					},
					{ 
						usuario: usuario,
						nomeAtividade: nomeAtividade,
						dataInicioSemana: dataInicioSemana,
						dataFimSemana: dataFimSemana,
						prioridade: prioridade,
						foto: foto,
						categoria: categoria,
						dataAtividade: dataAtividade,						
						tempoInvestido : parseInt(tempoInvestido) 
					
					},function(err, doc){
						if(err){
							res.send('{ "ok" : 0, "msg" : "' + err + '" }');
						} else {
							res.send('{ "ok" : 1 }');	
						}
					});
				}				
			}
		});
		
	});
	
	app.post("/deleteAtividade", function (req, res){
		var usuario = req.body.usuario;
		var nomeAtividade = req.body.nomeAtividade;
		var dataInicioSemana = req.body.dataInicioSemana;
		
		atividadeCollection.find({
			usuario: usuario,
			nomeAtividade: nomeAtividade,
			dataInicioSemana: dataInicioSemana
			
		}).toArray(function(err, array){
			if(err) {
				res.send('{ "ok" : 0, "msg" : "' + err + '" }');
			} else {
				if(array.length == 0) {
					res.send('{ "ok" : 0, "msg" : "ATIVIDADE INEXISTENTE" }');
				} else {
					atividadeCollection.remove({
						usuario: usuario,
						nomeAtividade: nomeAtividade,
						dataInicioSemana: dataInicioSemana
					}, function(err, doc){
						if(err){
							res.send('{ "ok" : 0, "msg" : "' + err + '" }');
						} else {
							res.send('{ "ok" : 1 }');	
						}
					});
				}				
			}
		});
		
	});
	
	
	app.post("/findAtividade", function (req, res){
		var usuario = req.body.usuario;
		var nomeAtividade = req.body.nomeAtividade;
		var dataInicioSemana = req.body.dataInicioSemana;
		
		atividadeCollection.findOne({
			usuario: usuario,
			nomeAtividade: nomeAtividade,
			dataInicioSemana: dataInicioSemana
			
		}, function(err, doc){
			if(err){
				res.send('{ "ok" : 0, "msg" : "' + err + '" }');
			} else {
				res.json(doc);	
			}
		});
		
	});
	
	app.post("/findAtividadesSemana", function(req, res){
		var usuario = req.body.usuario;
		var dataInicioSemana = req.body.dataInicioSemana;

		atividadeCollection.find({
			usuario: usuario,
			dataInicioSemana: dataInicioSemana
			
		}).toArray(function(err, array){
			if(err) {
				res.send('{ "ok" : 0, "msg" : "' + err + '" }');
			} else {
				res.json({ok : 1, result : array});       		
			}
		});	
		
	});
	
	return this;
}