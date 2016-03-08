# Arquivo de detalhamento das requisições POST para um banco de dados mongoDB

# cadastra uma atividade com tempo investido inicial igual a zero
http://povmt-armq.rhcloud.com/cadastrarAtividade
  	nomeAtividade;
	dataInicioSemana;
	dataFimSemana;
	prioridade;
	foto;
	categoria;
	dataAtividade;

# incrementa o tempo insvestido da atividade cujo nome e data inicial da semana foram passados na requisição
http://povmt-armq.rhcloud.com/incrementaTempoInvestido
  	nomeAtividade;
	dataInicioSemana;
	tempoInvestido;
	
# Atualiza todos os campos da atividade cujo nome e data inicial da semana foram passados na requisição
http://povmt-armq.rhcloud.com/updateAtividade
 	nomeAtividade;
	dataInicioSemana;
	dataFimSemana;
	prioridade;
	foto;
	categoria;
	dataAtividade;
	tempoInvestido;

# Deleta a atividade cujo nome e data inicial da semana foram passados na requisição
http://povmt-armq.rhcloud.com/deleteAtividade
  	nomeAtividade;
	dataInicioSemana;

# Recupera as informações da atividade cujo nome e data inicial da semana foram passados na requisição
http://povmt-armq.rhcloud.com/findAtividade
  	nomeAtividade;
	dataInicioSemana;

# Recupera as atividades cadastranas na semana com data inicial passada na requisição
http://povmt-armq.rhcloud.com/findAtividadesSemana
	dataInicioSemana;
